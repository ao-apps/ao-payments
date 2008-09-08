package com.aoindustries.creditcards.usaepay;
/*
 * Copyright 2008 by AO Industries, Inc.,
 * 7262 Bull Pen Cir, Mobile, Alabama, 36695, U.S.A.
 * All rights reserved.
 */
import com.aoindustries.creditcards.AuthorizationResult;
import com.aoindustries.creditcards.CreditCard;
import com.aoindustries.creditcards.CreditCardProcessor;
import com.aoindustries.creditcards.PropertiesPersistenceMechanism;
import com.aoindustries.creditcards.Transaction;
import com.aoindustries.creditcards.TransactionRequest;
import com.aoindustries.creditcards.TransactionResult;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.security.Principal;
import java.security.acl.Group;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
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
        if(config==null) {
            InputStream in = USAePayTest.class.getResourceAsStream("USAePayTest.properties");
            if(in==null) throw new IOException("Unable to find resource: USAePayTest.properties");
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
    private List<CreditCard> testGoodCreditCards;

    public USAePayTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        userLocale = Locale.getDefault();

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

        testGoodCreditCards = new ArrayList<CreditCard>();
        testGoodCreditCards.add(
            new CreditCard(
                userLocale,
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
                "(251)607-9557",
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
                userLocale,
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
                userLocale,
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
                "(251)607-9557",
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
                userLocale,
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
        userLocale = null;
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
            processor.canStoreCreditCards(userLocale)
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
                    userLocale,
                    false,
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
                    "7262 Bull Pen Cir",
                    null,
                    "Mobile",
                    "AL",
                    "36695",
                    "US",
                    false,
                    "accounting@aoidustries.com",
                    null,
                    null,
                    "Test transaction"
                ),
                testGoodCreditCard,
                userLocale
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
