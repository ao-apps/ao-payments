package com.aoindustries.creditcards.sagePayments;
/*
 * Copyright 2007 by AO Industries, Inc.,
 * 816 Azalea Rd, Mobile, Alabama, 36693, U.S.A.
 * All rights reserved.
 */
import com.aoindustries.creditcards.CreditCard;
import com.aoindustries.creditcards.CreditCardProcessor;
import com.aoindustries.creditcards.PropertiesPersistenceMechanism;
import com.aoindustries.creditcards.Transaction;
import com.aoindustries.creditcards.TransactionRequest;
import com.aoindustries.creditcards.TransactionResult;
import com.aoindustries.creditcards.VoidResult;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.security.Principal;
import java.security.acl.Group;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Locale;
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
        if(config==null) {
            InputStream in = SagePaymentsTest.class.getResourceAsStream("SagePaymentsTest.properties");
            if(in==null) throw new IOException("Unable to find resource: SagePaymentsTest.properties");
            try {
                Properties props = new Properties();
                props.load(in);
                config = props;
            } finally {
                in.close();
            }
        }
        return config.getProperty(name);
    }

    private Locale userLocale;
    private CreditCardProcessor processor;
    private Principal principal;
    private Group group;
    private CreditCard aoCreditCard;

    public SagePaymentsTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        userLocale = Locale.getDefault();

        processor = new CreditCardProcessor(
            new SagePayments("SagePaymentsTest", getConfig("MERCHANT_ID"), getConfig("MERCHANT_KEY")),
            PropertiesPersistenceMechanism.getInstance(getConfig("PERSISTENCE_PATH"))
        );

        principal = new Principal() {
            public String getName() {
                return "TestPrincipal";
            }
            public int hashCode() {
                return getName().hashCode();
            }
            public String toString() {
                return getName();
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

        aoCreditCard = new CreditCard(
            userLocale,
            null,
            principal.getName(),
            group.getName(),
            null,
            null,
            "4828800975234027",
            null,
            (byte)11,
            (short)2008,
            "682",
            "Daniel",
            "Armstrong",
            "AO Industries, Inc.",
            "dan@aoindustries.com",
            "(251)661-6195",
            "(251)661-6957",
            "AOINDUSTRIES",
            "88-0441445",
            "816 Azalea Rd",
            null,
            "Mobile",
            "AL",
            "36693",
            "US",
            "Test card"
        );
    }

    protected void tearDown() throws Exception {
        userLocale = null;
        processor = null;
        principal = null;
        group = null;
        aoCreditCard = null;
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
        System.out.println("canStoreCreditCards: "+processor.canStoreCreditCards(userLocale));
    }
    
    /**
     * Test a sale with a new card.
     */
    public void testNewCardSale() throws IOException, SQLException {
        Transaction transaction = processor.sale(
            principal,
            group,
            new TransactionRequest(
                userLocale,
                true,
                InetAddress.getLocalHost().getHostAddress(),
                120,
                "1",
                TransactionRequest.CurrencyCode.USD,
                new BigDecimal("1.00"),
                null,
                false,
                null,
                null,
                "Daniel",
                "Armstrong",
                "AO Industries, Inc.",
                "816 Azalea Rd",
                null,
                "Mobile",
                "AL",
                "36693",
                "US",
                false,
                "accounting@aoidustries.com",
                null,
                null,
                "Test transaction"
            ),
            aoCreditCard,
            userLocale
        );
        try {
            System.out.println("transaction.status="+transaction.getStatus().toString(userLocale));
            System.out.println("transaction.persistenceUniqueId="+transaction.getPersistenceUniqueId());
            System.out.println("transaction.authorizationResult.providerUniqueId="+transaction.getAuthorizationResult().getProviderUniqueId());
            assertEquals("transaction.status is not CAPTURED", Transaction.Status.CAPTURED, transaction.getStatus());
        } finally {
            /*
            System.out.println("Will void transaction "+transaction.getAuthorizationResult().getProviderUniqueId()+" in ten seconds...");
            try {
                Thread.sleep(10000);
            } catch(InterruptedException err) {
                err.printStackTrace();
            }*/
            // Void the transaction
            switch(transaction.getStatus()) {
                case AUTHORIZED:
                case CAPTURED:
                case HOLD:
                    VoidResult voidResult = processor.voidTransaction(principal, transaction, userLocale);
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
        CreditCard creditCard = aoCreditCard.clone();

        // Test storeCreditCard
        assertNotNull("Before storing creditCard, cardNumber should not be null", creditCard.getCardNumber());
        assertNotNull("Before storing creditCard, maskedCardNumber should not be null", creditCard.getMaskedCardNumber());
        processor.storeCreditCard(
            principal,
            group,
            creditCard,
            userLocale
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
                    userLocale,
                    true,
                    InetAddress.getLocalHost().getHostAddress(),
                    120,
                    "1",
                    TransactionRequest.CurrencyCode.USD,
                    new BigDecimal("1.00"),
                    null,
                    false,
                    null,
                    null,
                    "Daniel",
                    "Armstrong",
                    "AO Industries, Inc.",
                    "816 Azalea Rd",
                    null,
                    "Mobile",
                    "AL",
                    "36693",
                    "US",
                    false,
                    "accounting@aoidustries.com",
                    null,
                    null,
                    "Test transaction"
                ),
                creditCard,
                userLocale
            );
            try {
                System.out.println("transaction.status="+transaction.getStatus().toString(userLocale));
                System.out.println("transaction.persistenceUniqueId="+transaction.getPersistenceUniqueId());
                System.out.println("transaction.authorizationResult.providerUniqueId="+transaction.getAuthorizationResult().getProviderUniqueId());
                assertEquals("transaction.status is not CAPTURED", Transaction.Status.CAPTURED, transaction.getStatus());
            } finally {
                /*
                System.out.println("Will void transaction "+transaction.getAuthorizationResult().getProviderUniqueId()+" in ten seconds...");
                try {
                    Thread.sleep(10000);
                } catch(InterruptedException err) {
                    err.printStackTrace();
                }*/
                // Void the transaction
                switch(transaction.getStatus()) {
                    case AUTHORIZED:
                    case CAPTURED:
                    case HOLD:
                        VoidResult voidResult = processor.voidTransaction(principal, transaction, userLocale);
                        // Check success of void
                        assertEquals("voidResult.communicationResult is not SUCCESS", TransactionResult.CommunicationResult.SUCCESS, voidResult.getCommunicationResult());
                        // Check results of void
                        assertEquals("transaction.status is not VOID", Transaction.Status.VOID, transaction.getStatus());
                }
            }
        } finally {
            if(creditCard.getProviderUniqueId()!=null || creditCard.getPersistenceUniqueId()!=null) {
                processor.deleteCreditCard(principal, creditCard, userLocale);
                assertNull("After deleting creditCard, providerUniqueId should be null", creditCard.getProviderUniqueId());
                assertNull("After deleting creditCard, persistenceUniqueId should be null", creditCard.getPersistenceUniqueId());
            }
        }
    }
}
