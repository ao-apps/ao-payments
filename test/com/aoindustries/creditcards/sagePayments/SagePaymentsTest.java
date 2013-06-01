/*
 * ao-credit-cards - Credit card processing API supporting multiple payment gateways.
 * Copyright (C) 2007, 2008, 2009, 2010, 2011, 2012, 2013  AO Industries, Inc.
 *     support@aoindustries.com
 *     7262 Bull Pen Cir
 *     Mobile, AL 36695
 *
 * This file is part of ao-credit-cards.
 *
 * ao-credit-cards is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ao-credit-cards is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ao-credit-cards.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aoindustries.creditcards.sagePayments;

import com.aoindustries.creditcards.CreditCard;
import com.aoindustries.creditcards.CreditCardProcessor;
import com.aoindustries.creditcards.PropertiesPersistenceMechanism;
import com.aoindustries.creditcards.Transaction;
import com.aoindustries.creditcards.TransactionRequest;
import com.aoindustries.creditcards.TransactionResult;
import com.aoindustries.creditcards.VoidResult;
import com.aoindustries.util.PropertiesUtils;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.security.Principal;
import java.security.acl.Group;
import java.sql.SQLException;
import java.util.Currency;
import java.util.Enumeration;
import java.util.Properties;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests SagePayments.
 *
 * @author  AO Industries, Inc.
 */
public class SagePaymentsTest extends TestCase {

	private static Properties config;
	synchronized private static String getConfig(String name) throws IOException {
		if(config==null) config = PropertiesUtils.loadFromResource(SagePaymentsTest.class, "SagePaymentsTest.properties");
		return config.getProperty(name);
	}

	private CreditCardProcessor processor;
	private Principal principal;
	private Group group;
	private CreditCard testCard;

	public SagePaymentsTest(String testName) {
		super(testName);
	}

	@Override
	protected void setUp() throws Exception {
		processor = new CreditCardProcessor(
			new SagePayments("SagePaymentsTest", getConfig("MERCHANT_ID"), getConfig("MERCHANT_KEY")),
			PropertiesPersistenceMechanism.getInstance(getConfig("PERSISTENCE_PATH"))
		);

		principal = new Principal() {
			public String getName() {
				return "TestPrincipal";
			}
			@Override
			public int hashCode() {
				return getName().hashCode();
			}
			@Override
			public String toString() {
				return getName();
			}
			@Override
			public boolean equals(Object obj) {
				if(obj==null) return false;
				if(getClass()!=obj.getClass()) return false;
				return this==obj;
			}
		};
		group = new Group() {
			public boolean addMember(Principal user) {
				throw new RuntimeException("Unimplemented");
			}
			public String getName() {
				return "TestGroup";
			}
			public boolean isMember(Principal member) {
				return member.getName().equals("TestPrincipal");
			}
			public Enumeration<? extends Principal> members() {
				throw new RuntimeException("Unimplemented");
			}
			public boolean removeMember(Principal user) {
				throw new RuntimeException("Unimplemented");
			}
		};

		testCard = new CreditCard(
			null,
			principal.getName(),
			group.getName(),
			null,
			null,
			getConfig("testCard.cardNumber"),
			null,
			Byte.parseByte(getConfig("testCard.expirationMonth")),
			Short.parseShort(getConfig("testCard.expirationYear")),
			getConfig("testCard.cvv2"),
			getConfig("testCard.firstName"),
			getConfig("testCard.lastName"),
			getConfig("testCard.companyName"),
			getConfig("testCard.email"),
			getConfig("testCard.phone"),
			getConfig("testCard.fax"),
			getConfig("testCard.customerId"),
			getConfig("testCard.customerTaxId"),
			getConfig("testCard.streetAddress1"),
			getConfig("testCard.streetAddress2"),
			getConfig("testCard.city"),
			getConfig("testCard.state"),
			getConfig("testCard.postalCode"),
			getConfig("testCard.countryCode"),
			getConfig("testCard.comments")
		);
	}

	@Override
	protected void tearDown() throws Exception {
		processor = null;
		principal = null;
		group = null;
		testCard = null;
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(SagePaymentsTest.class);
		return suite;
	}

	/**
	 * Tests canStoreCreditCards.
	 */
	public void testCanStoreCreditCards() throws IOException {
		// Test canStoreCreditCards
		System.out.println("canStoreCreditCards: "+processor.canStoreCreditCards());
	}

	/**
	 * Test a sale with a new card.
	 */
	public void testNewCardSale() throws IOException, SQLException {
		Transaction transaction = processor.sale(
			principal,
			group,
			new TransactionRequest(
				true,
				InetAddress.getLocalHost().getHostAddress(),
				120,
				"1",
				Currency.getInstance("USD"),
				new BigDecimal("1.00"),
				null,
				false,
				null,
				null,
				"Daniel",
				"Armstrong",
				"AO Industries, Inc.",
				"7262 Bull Pen Cir",
				null,
				"Mobile",
				"AL",
				"36695",
				"US",
				false,
				"accounting@aoindustries.com",
				null,
				null,
				"Test transaction"
			),
			testCard
		);
		try {
			System.out.println("transaction.status="+transaction.getStatus().toString());
			System.out.println("transaction.persistenceUniqueId="+transaction.getPersistenceUniqueId());
			System.out.println("transaction.authorizationResult.providerUniqueId="+transaction.getAuthorizationResult().getProviderUniqueId());
			assertEquals("transaction.status is not CAPTURED", Transaction.Status.CAPTURED, transaction.getStatus());
		} finally {
			/*
			System.out.println("Will void transaction "+transaction.getAuthorizationResult().getProviderUniqueId()+" in ten seconds...");
			try {
				Thread.sleep(10000);
			} catch(InterruptedException err) {
				logger.log(Level.WARNING, null, err);
			}*/
			// Void the transaction
			switch(transaction.getStatus()) {
				case AUTHORIZED:
				case CAPTURED:
				case HOLD:
					VoidResult voidResult = processor.voidTransaction(principal, transaction);
					// Check success of void
					assertEquals("voidResult.communicationResult is not SUCCESS", TransactionResult.CommunicationResult.SUCCESS, voidResult.getCommunicationResult());
					// Check results of void
					assertEquals("transaction.status is not VOID", Transaction.Status.VOID, transaction.getStatus());
			}
		}
	}

	/**
	 * Test a sale with a stored card.
	 */
	public void testStoredCardSale() throws SQLException, IOException {
		CreditCard creditCard = testCard.clone();

		// Test storeCreditCard
		assertNotNull("Before storing creditCard, cardNumber should not be null", creditCard.getCardNumber());
		assertNotNull("Before storing creditCard, maskedCardNumber should not be null", creditCard.getMaskedCardNumber());
		processor.storeCreditCard(
			principal,
			group,
			creditCard
		);
		try {
			System.out.println("storeCreditCard: guid="+creditCard.getProviderUniqueId());
			assertNull("After storing creditCard, cardNumber should be null", creditCard.getCardNumber());
			assertNotNull("After storing creditCard, maskedCardNumber should not be null", creditCard.getMaskedCardNumber());

			// Test sale on the stored card
			Transaction transaction = processor.sale(
				principal,
				group,
				new TransactionRequest(
					true,
					InetAddress.getLocalHost().getHostAddress(),
					120,
					"1",
					Currency.getInstance("USD"),
					new BigDecimal("1.00"),
					null,
					false,
					null,
					null,
					"Daniel",
					"Armstrong",
					"AO Industries, Inc.",
					"7262 Bull Pen Cir",
					null,
					"Mobile",
					"AL",
					"36695",
					"US",
					false,
					"accounting@aoindustries.com",
					null,
					null,
					"Test transaction"
				),
				creditCard
			);
			try {
				System.out.println("transaction.status="+transaction.getStatus().toString());
				System.out.println("transaction.persistenceUniqueId="+transaction.getPersistenceUniqueId());
				System.out.println("transaction.authorizationResult.providerUniqueId="+transaction.getAuthorizationResult().getProviderUniqueId());
				assertEquals("transaction.status is not CAPTURED", Transaction.Status.CAPTURED, transaction.getStatus());
			} finally {
				/*
				System.out.println("Will void transaction "+transaction.getAuthorizationResult().getProviderUniqueId()+" in ten seconds...");
				try {
					Thread.sleep(10000);
				} catch(InterruptedException err) {
					logger.log(Level.WARNING, null, err);
				}*/
				// Void the transaction
				switch(transaction.getStatus()) {
					case AUTHORIZED:
					case CAPTURED:
					case HOLD:
						VoidResult voidResult = processor.voidTransaction(principal, transaction);
						// Check success of void
						assertEquals("voidResult.communicationResult is not SUCCESS", TransactionResult.CommunicationResult.SUCCESS, voidResult.getCommunicationResult());
						// Check results of void
						assertEquals("transaction.status is not VOID", Transaction.Status.VOID, transaction.getStatus());
				}
			}
		} finally {
			if(creditCard.getProviderUniqueId()!=null || creditCard.getPersistenceUniqueId()!=null) {
				processor.deleteCreditCard(principal, creditCard);
				assertNull("After deleting creditCard, providerUniqueId should be null", creditCard.getProviderUniqueId());
				assertNull("After deleting creditCard, persistenceUniqueId should be null", creditCard.getPersistenceUniqueId());
			}
		}
	}
}
