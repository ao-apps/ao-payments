package com.aoindustries.creditcards.payflowPro;
/*
 * Copyright 2007-2009 by AO Industries, Inc.,
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
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests PayflowPro.
 *
 * @author  AO Industries, Inc.
 */
public class PayflowProTest extends TestCase {
    
    private static Properties config;
    synchronized private static String getConfig(String name) throws IOException {
        if(config==null) {
            InputStream in = PayflowProTest.class.getResourceAsStream("PayflowProTest.properties");
            if(in==null) throw new IOException("Unable to find resource: PayflowProTest.properties");
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

    public PayflowProTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        userLocale = Locale.getDefault();

        processor = new CreditCardProcessor(
            new PayflowPro("PayflowProTest", getConfig("user"), getConfig("vendor"), getConfig("partner"), getConfig("password")),
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

        testGoodCreditCards = new ArrayList<CreditCard>();
        testGoodCreditCards.add(
            new CreditCard(
                userLocale,
                null,
                principal.getName(),
                group.getName(),
                null,
                null,
                "378282246310005",
                null,
                (byte)11,
                (short)(Calendar.getInstance().get(Calendar.YEAR)+1),
                "123",
                "First",
                "Last",
                "Company = Inc.",     // Contains = to test special characters in Payflow Pro protocol
                "signup@aoindustries.com",
                "(251)607-9556",
                "(251)382-1197",
                "AOINDUSTRIES",
                "123-45-6789",
                "816 Azalea & Rd",  // Contains & to test special characters in Payflow Pro protocol
                null,
                "Mobile",
                "AL",
                "36693",
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
                "6011111111111117",
                null,
                (byte)7,
                (short)(Calendar.getInstance().get(Calendar.YEAR)+1),
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
                "5555555555554444",
                null,
                (byte)4,
                (short)(Calendar.getInstance().get(Calendar.YEAR)+3),
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
                userLocale,
                null,
                null,
                null,
                null,
                null,
                "4111111111111111",
                null,
                (byte)3,
                (short)(Calendar.getInstance().get(Calendar.YEAR)+7),
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
                null
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
        TestSuite suite = new TestSuite(PayflowProTest.class);
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

    /**
     * Test a sale for Japnese Yen
     */
    public void testNewCardSaleJapaneseYen() throws IOException, SQLException {
        for(CreditCard testGoodCreditCard : testGoodCreditCards) {
            Transaction transaction = processor.sale(
                principal,
                group,
                new TransactionRequest(
                    userLocale,
                    true,
                    InetAddress.getLocalHost().getHostAddress(),
                    120,
                    "1",
                    TransactionRequest.CurrencyCode.JPY,
                    new BigDecimal("1000"),
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
