package com.aoindustries.creditcards.test;

/*
 * Copyright 2007 by AO Industries, Inc.,
 * 816 Azalea Rd, Mobile, Alabama, 36693, U.S.A.
 * All rights reserved.
 */
import com.aoindustries.creditcards.AuthorizationResult;
import com.aoindustries.creditcards.CaptureResult;
import com.aoindustries.creditcards.CreditCard;
import com.aoindustries.creditcards.CreditResult;
import com.aoindustries.creditcards.MerchantServicesProvider;
import com.aoindustries.creditcards.SaleResult;
import com.aoindustries.creditcards.Transaction;
import com.aoindustries.creditcards.TransactionRequest;
import com.aoindustries.creditcards.TransactionResult;
import com.aoindustries.creditcards.VoidResult;
import java.io.IOException;
import java.util.Locale;
import java.util.Random;

/**
 * Provider for testing software.
 * <br>
 * Configuration parameters:
 * <ol>
 *   <li>errorChance - percentage chance of some sort of error (during storeCard, payment, capture, void) - defaults to 10</li>
 *   <li>declineChance - percentage chance of being declined, otherwise accepted (during payment) - defaults to 20</li>
 * </ol>
 *
 * TODO: Add AVS, CVV, and ReviewReason responses.
 * TODO: Add hold
 *
 * @author  AO Industries, Inc.
 */
public class TestMerchantServicesProvider implements MerchantServicesProvider {

    /** Shared by all instances to make more random */
    private static final Random random = new Random();
    
    private final String providerId;
    private final byte errorChance;
    private final byte declineChance;

    public TestMerchantServicesProvider(String providerId, byte errorChance, byte declineChance) {
        this.providerId = providerId;
        this.errorChance = errorChance;
        this.declineChance = declineChance;
    }

    /**
     * @throws  NumberFormatException when can't parse errorChance or declineChance
     */
    public TestMerchantServicesProvider(String providerId, String errorChance, String declineChance) throws NumberFormatException {
        this(
            providerId,
            errorChance==null || errorChance.length()==0 ? (byte)10 : Byte.parseByte(errorChance),
            declineChance==null || declineChance.length()==0 ? (byte)20 : Byte.parseByte(declineChance)
        );
    }

    public String getProviderId() {
        return providerId;
    }

    public byte getErrorChance() {
        return errorChance;
    }
    
    public byte getDeclineChance() {
        return declineChance;
    }

    public SaleResult sale(TransactionRequest transactionRequest, CreditCard creditCard, Locale userLocale) {
        // First allow for random errors
        if(random.nextInt(100)<errorChance) {
            // Random error class
            TransactionResult.CommunicationResult communicationResult;
            int randomInt = random.nextInt(3);
            switch(randomInt) {
                case 0: {
                    communicationResult = TransactionResult.CommunicationResult.LOCAL_ERROR;
                    break;
                }
                case 1: {
                    communicationResult = TransactionResult.CommunicationResult.IO_ERROR;
                    break;
                }
                case 2: {
                    communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                    break;
                }
                default: throw new RuntimeException("random.nextInt(3) didn't return value between 0 and 2 inclusive: "+randomInt);
            }

            // Random error code
            TransactionResult.ErrorCode[] values = TransactionResult.ErrorCode.values();
            TransactionResult.ErrorCode errorCode = values[random.nextInt(values.length)];

            return new SaleResult(
                new AuthorizationResult(
                    getProviderId(),
                    communicationResult,
                    null,
                    errorCode,
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
                ),
                new CaptureResult(
                    getProviderId(),
                    communicationResult,
                    null,
                    errorCode,
                    null,
                    null
                )
            );
        }
        
        // Second allow for declines
        if(random.nextInt(100)<declineChance) {
            // Random decline reason
            AuthorizationResult.DeclineReason[] values = AuthorizationResult.DeclineReason.values();
            AuthorizationResult.DeclineReason declineReason = values[random.nextInt(values.length)];

            // Random doesn't ensure uniquiness - but this is easily implemented without persistence
            String providerUniqueId = Long.toString(Math.abs(random.nextLong()), 16).toUpperCase();

            return new SaleResult(
                new AuthorizationResult(
                    getProviderId(),
                    TransactionResult.CommunicationResult.SUCCESS,
                    null,
                    null,
                    null,
                    providerUniqueId,
                    null,
                    AuthorizationResult.ApprovalResult.DECLINED,
                    null,
                    declineReason,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                ),
                new CaptureResult(
                    getProviderId(),
                    TransactionResult.CommunicationResult.SUCCESS,
                    null,
                    null,
                    null,
                    providerUniqueId
                )
            );
        }

        // Simulate success

        // Random doesn't ensure uniquiness - but this is easily implemented without persistence
        String providerUniqueId = Long.toString(Math.abs(random.nextLong()), 16).toUpperCase();
        
        String approvalCode =
            new StringBuilder()
                .append(random.nextInt(10))
                .append(random.nextInt(10))
                .append(random.nextInt(10))
                .append(random.nextInt(10))
                .append(random.nextInt(10))
                .append(random.nextInt(10))
                .toString();

        return new SaleResult(
            new AuthorizationResult(
                getProviderId(),
                TransactionResult.CommunicationResult.SUCCESS,
                null,
                null,
                null,
                providerUniqueId,
                null,
                AuthorizationResult.ApprovalResult.APPROVED,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                approvalCode
            ),
            new CaptureResult(
                getProviderId(),
                TransactionResult.CommunicationResult.SUCCESS,
                null,
                null,
                null,
                providerUniqueId
            )
        );
    }

    public AuthorizationResult authorize(TransactionRequest transactionRequest, CreditCard creditCard, Locale userLocale) {
        // First allow for random errors
        if(random.nextInt(100)<errorChance) {
            // Random error class
            TransactionResult.CommunicationResult communicationResult;
            int randomInt = random.nextInt(3);
            switch(randomInt) {
                case 0: {
                    communicationResult = TransactionResult.CommunicationResult.LOCAL_ERROR;
                    break;
                }
                case 1: {
                    communicationResult = TransactionResult.CommunicationResult.IO_ERROR;
                    break;
                }
                case 2: {
                    communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                    break;
                }
                default: throw new RuntimeException("random.nextInt(3) didn't return value between 0 and 2 inclusive: "+randomInt);
            }

            // Random error code
            TransactionResult.ErrorCode[] values = TransactionResult.ErrorCode.values();
            TransactionResult.ErrorCode errorCode = values[random.nextInt(values.length)];

            return new AuthorizationResult(
                getProviderId(),
                communicationResult,
                null,
                errorCode,
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
            );
        }
        
        // Second allow for declines
        if(random.nextInt(100)<declineChance) {
            // Random decline reason
            AuthorizationResult.DeclineReason[] values = AuthorizationResult.DeclineReason.values();
            AuthorizationResult.DeclineReason declineReason = values[random.nextInt(values.length)];

            // Random doesn't ensure uniquiness - but this is easily implemented without persistence
            String providerUniqueId = Long.toString(Math.abs(random.nextLong()), 16).toUpperCase();

            return new AuthorizationResult(
                getProviderId(),
                TransactionResult.CommunicationResult.SUCCESS,
                null,
                null,
                null,
                providerUniqueId,
                null,
                AuthorizationResult.ApprovalResult.DECLINED,
                null,
                declineReason,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            );
        }

        // Simulate success

        // Random doesn't ensure uniquiness - but this is easily implemented without persistence
        String providerUniqueId = Long.toString(Math.abs(random.nextLong()), 16).toUpperCase();
        
        String approvalCode =
            new StringBuilder()
                .append(random.nextInt(10))
                .append(random.nextInt(10))
                .append(random.nextInt(10))
                .append(random.nextInt(10))
                .append(random.nextInt(10))
                .append(random.nextInt(10))
                .toString();

        return new AuthorizationResult(
            getProviderId(),
            TransactionResult.CommunicationResult.SUCCESS,
            null,
            null,
            null,
            providerUniqueId,
            null,
            AuthorizationResult.ApprovalResult.APPROVED,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            approvalCode
        );
    }

    public CaptureResult capture(AuthorizationResult authorizationResult, Locale userLocale) {
        throw new RuntimeException("TODO: Implement method");
    }

    public VoidResult voidTransaction(Transaction transaction, Locale userLocale) {
        throw new RuntimeException("TODO: Implement method");
    }

    public CreditResult credit(TransactionRequest transactionRequest, CreditCard creditCard, Locale userLocale) {
        throw new RuntimeException("TODO: Implement method");
    }

    public boolean canStoreCreditCards(Locale userLocale) throws IOException {
        return true;
    }

    public String storeCreditCard(CreditCard creditCard, Locale userLocale) throws IOException {
        throw new RuntimeException("TODO: Implement method");
    }

    public void updateCreditCardNumberAndExpiration(CreditCard creditCard, String cardNumber, byte expirationMonth, short expirationYear, Locale userLocale) throws IOException {
        throw new RuntimeException("TODO: Implement method");
    }

    public void updateCreditCardExpiration(CreditCard creditCard, byte expirationMonth, short expirationYear, Locale userLocale) throws IOException {
        throw new RuntimeException("TODO: Implement method");
    }

    public void deleteCreditCard(CreditCard creditCard, Locale userLocale) throws IOException {
        throw new RuntimeException("TODO: Implement method");
    }
}
