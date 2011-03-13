package com.aoindustries.creditcards.authorizeNet;
/*
 * Copyright 2010-2011 by AO Industries, Inc.,
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
import java.util.Currency;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests Authorize.Net.
 *
 * @author  AO Industries, Inc.
 */
public class AuthorizeNetTest extends TestCase {
    
    private static Properties config;
    synchronized private static String getConfig(String name) throws IOException {
        if(config==null) {
            InputStream in = AuthorizeNetTest.class.getResourceAsStream("AuthorizeNetTest.properties");
            if(in==null) throw new IOException("Unable to find resource: AuthorizeNetTest.properties");
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

    private CreditCardProcessor processor;
    private Principal principal;
    private Group group;
    private List<CreditCard> testGoodCreditCards;
    private CreditCard testBadCreditCard;

    public AuthorizeNetTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        processor = new CreditCardProcessor(
            new AuthorizeNet(
                "AuthorizeNetTest",
                getConfig("x_login"),
                getConfig("x_tran_key")
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
                (short)2011,
                "123",
                "First",
                "Last",
                "Company = |Inc.\"",     // Contains = " and | to test special characters in protocol
                "signup@aoindustries.com",
                "(251)607-9556",
                "(251)382-1197",
                "AOINDUSTRIES",
                "123-45-6789",
                "7262 Bull, Pen & Cir",  // Contains , and & to test special characters in protocol
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
                (short)2011,
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
                (short)2011,
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
                (short)2011,
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
        testBadCreditCard = new CreditCard(
            null,
            principal.getName(),
            group.getName(),
            null,
            null,
            "4222222222222",
            null,
            (byte)9,
            (short)2011,
            "123",
            "First",
            "Last",
            "Company Inc.",
            "signup@aoindustries.com",
            "(251)607-9556",
            "(251)382-1197",
            "AOINDUSTRIES",
            "123-45-6789",
            "7262 Bull Pen Cir",
            null,
            "Mobile",
            "AL",
            "36695",
            "US",
            "Test bad card"
        );
    }

    @Override
    protected void tearDown() throws Exception {
        processor = null;
        principal = null;
        group = null;
        testGoodCreditCards = null;
        testBadCreditCard = null;
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(AuthorizeNetTest.class);
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

    public void testAvsResponse27() throws IOException, SQLException {
        Transaction transaction = processor.sale(
            principal,
            group,
            new TransactionRequest(
                true,
                InetAddress.getLocalHost().getHostAddress(),
                120,
                "1",
                Currency.getInstance("USD"),
                new BigDecimal("27.00"),
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
            testBadCreditCard
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
            "Transaction should be DECLINED",
            AuthorizationResult.ApprovalResult.DECLINED,
            transaction.getAuthorizationResult().getApprovalResult()
        );
        assertEquals(
            "transaction.status should be DECLINED",
            Transaction.Status.DECLINED,
            transaction.getStatus()
        );
        assertEquals(
            "transaction.authorizationResult.avsResult should be NOT_APPLICABLE",
            AuthorizationResult.AvsResult.NOT_APPLICABLE,
            transaction.getAuthorizationResult().getAvsResult()
        );
    }
}
