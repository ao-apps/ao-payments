/*
 * ao-credit-cards - Credit card processing API supporting multiple payment gateways.
 * Copyright (C) 2008, 2009, 2010, 2011, 2012, 2013, 2016  AO Industries, Inc.
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
package com.aoindustries.creditcards.usaepay;

import com.aoindustries.creditcards.AuthorizationResult;
import com.aoindustries.creditcards.CreditCard;
import com.aoindustries.creditcards.CreditCardProcessor;
import com.aoindustries.creditcards.PropertiesPersistenceMechanism;
import com.aoindustries.creditcards.Transaction;
import com.aoindustries.creditcards.TransactionRequest;
import com.aoindustries.creditcards.TransactionResult;
import com.aoindustries.util.PropertiesUtils;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.security.Principal;
import java.security.acl.Group;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests USAePay.
 *
 * @author  AO Industries, Inc.
 */
public class USAePayTest extends TestCase {

	private static Properties config;
	synchronized private static String getConfig(String name) throws IOException {
		if(config==null) config = PropertiesUtils.loadFromResource(USAePayTest.class, "USAePayTest.properties");
		return config.getProperty(name);
	}

	private CreditCardProcessor processor;
	private Principal principal;
	private Group group;
	private List<CreditCard> testGoodCreditCards;

	public USAePayTest(String testName) {
		super(testName);
	}

	@Override
	protected void setUp() throws Exception {
		processor = new CreditCardProcessor(
			new USAePay(
				"USAePayTest",
				getConfig("postUrl"),
				getConfig("key"),
				getConfig("pin")
			),
			PropertiesPersistenceMechanism.getInstance(getConfig("persistencePath"))
		);

		principal = new Principal() {
			@Override
			public String getName() {
				return "TestPrincipal";
			}
			@Override
			public int hashCode() {
				return getName().hashCode();
			}
			@Override
			public boolean equals(Object O) {
				return super.equals(O);
			}
			@Override
			public String toString() {
				return getName();
			}
		};
		group = new Group() {
			@Override
			public boolean addMember(Principal user) {
				throw new RuntimeException("Unimplemented");
			}
			@Override
			public String getName() {
				return "TestGroup";
			}
			@Override
			public boolean isMember(Principal member) {
				return member.getName().equals("TestPrincipal");
			}
			@Override
			public Enumeration<? extends Principal> members() {
				throw new RuntimeException("Unimplemented");
			}
			@Override
			public boolean removeMember(Principal user) {
				throw new RuntimeException("Unimplemented");
			}
		};

		testGoodCreditCards = new ArrayList<CreditCard>();
		testGoodCreditCards.add(
			new CreditCard(
				null,
				principal.getName(),
				group.getName(),
				null,
				null,
				"371122223332225",
				null,
				(byte)9,
				(short)2009,
				"123",
				"First",
				"Last",
				"Company = Inc.",     // Contains = to test special characters in protocol
				"signup@aoindustries.com",
				"(251)607-9556",
				"(251)382-1197",
				"AOINDUSTRIES",
				"123-45-6789",
				"7262 Bull Pen & Cir",  // Contains & to test special characters in protocol
				null,
				"Mobile",
				"AL",
				"36695",
				"US",
				"Test AmEx card"
			)
		);
		testGoodCreditCards.add(
			new CreditCard(
				null,
				principal.getName(),
				group.getName(),
				null,
				null,
				"6011222233332224",
				null,
				(byte)9,
				(short)2009,
				null,
				"D First",
				"D Last",
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				"Test Discover card"
			)
		);
		testGoodCreditCards.add(
			new CreditCard(
				null,
				null,
				null,
				null,
				null,
				"5555444433332226",
				null,
				(byte)9,
				(short)2009,
				"123",
				"First",
				"Last",
				"AO Inc",
				"accounting@aoindustries.com",
				"(251)607-9556",
				"(251)382-1197",
				null,
				null,
				"7262 Bull Pen Cir",
				null,
				"Mobile",
				"AL",
				"36695",
				"US",
				"Test MasterCard card"
			)
		);
		testGoodCreditCards.add(
			new CreditCard(
				null,
				null,
				null,
				null,
				null,
				"4000100011112224",
				null,
				(byte)9,
				(short)2009,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				"Test Visa card"
			)
		);
	}

	@Override
	protected void tearDown() throws Exception {
		processor = null;
		principal = null;
		group = null;
		testGoodCreditCards = null;
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(USAePayTest.class);
		return suite;
	}

	/**
	 * Tests canStoreCreditCards.
	 */
	public void testCanStoreCreditCards() throws IOException {
		// Test canStoreCreditCards, expecting false
		assertEquals(
			"Expecting to not be allowed to store credit cards",
			false,
			processor.canStoreCreditCards()
		);
	}

	/**
	 * Test a sale with a new card.
	 */
	public void testNewCardSaleApproved() throws IOException, SQLException {
		for(CreditCard testGoodCreditCard : testGoodCreditCards) {
			Transaction transaction = processor.sale(
				principal,
				group,
				new TransactionRequest(
					false,
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
				testGoodCreditCard
			);
			assertEquals(
				"Transaction authorization communication result should be SUCCESS",
				TransactionResult.CommunicationResult.SUCCESS,
				transaction.getAuthorizationResult().getCommunicationResult()
			);
			assertEquals(
				"Transaction capture communication result should be SUCCESS",
				TransactionResult.CommunicationResult.SUCCESS,
				transaction.getCaptureResult().getCommunicationResult()
			);
			assertEquals(
				"Transaction should be approved",
				AuthorizationResult.ApprovalResult.APPROVED,
				transaction.getAuthorizationResult().getApprovalResult()
			);
			assertEquals(
				"transaction.status should be CAPTURED",
				Transaction.Status.CAPTURED,
				transaction.getStatus()
			);
		}
	}
}
