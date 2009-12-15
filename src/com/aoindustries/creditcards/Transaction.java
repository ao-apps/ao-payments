package com.aoindustries.creditcards;

/*
 * Copyright 2007-2009 by AO Industries, Inc.,
 * 7262 Bull Pen Cir, Mobile, Alabama, 36695, U.S.A.
 * All rights reserved.
 */
import com.aoindustries.util.i18n.LocalizedToString;
import java.util.Locale;

/**
 * Encapsulates all the details of one transaction, including request, credit card details, and response.
 *
 * @author  AO Industries, Inc.
 */
public class Transaction {

    public enum Status implements LocalizedToString {
        PROCESSING,
        LOCAL_ERROR,
        IO_ERROR,
        GATEWAY_ERROR,
        AUTHORIZED,
        CAPTURED,
        DECLINED,
        HOLD,
        VOID,
        CHARGEBACK
        ;

        /**
         * Gets the display value in the default locale.
         */
        @Override
        public String toString() {
            return toString(Locale.getDefault());
        }

        /**
         * Gets the display value in the provided locale.
         */
        public String toString(Locale userLocale) {
            return ApplicationResources.getMessage(userLocale, "Transaction.Status."+name());
        }
    }

    private String providerId;
    private String persistenceUniqueId;
    private String groupName;
    private TransactionRequest transactionRequest;
    private CreditCard creditCard;
    private long authorizationTime;
    private String authorizationPrincipalName;
    private AuthorizationResult authorizationResult;
    private long captureTime;
    private String capturePrincipalName;
    private CaptureResult captureResult;
    private long voidTime;
    private String voidPrincipalName;
    private VoidResult voidResult;
    private Status status;

    /**
     * Creates an empty Transaction.  The values should be set using the appropriate setter methods.
     */
    public Transaction() {
    }
    
    /**
     * Creates a Transaction providing all of the details.
     */
    public Transaction(
        String providerId,
        String persistenceUniqueId,
        String groupName,
        TransactionRequest transactionRequest,
        CreditCard creditCard,
        long authorizationTime,
        String authorizationPrincipalName,
        AuthorizationResult authorizationResult,
        long captureTime,
        String capturePrincipalName,
        CaptureResult captureResult,
        long voidTime,
        String voidPrincipalName,
        VoidResult voidResult,
        Status status
    ) {
        setProviderId(providerId);
        setPersistenceUniqueId(persistenceUniqueId);
        setGroupName(groupName);
        setTransactionRequest(transactionRequest);
        setCreditCard(creditCard);
        setAuthorizationTime(authorizationTime);
        setAuthorizationPrincipalName(authorizationPrincipalName);
        setAuthorizationResult(authorizationResult);
        setCaptureTime(captureTime);
        setCapturePrincipalName(capturePrincipalName);
        setCaptureResult(captureResult);
        setVoidTime(voidTime);
        setVoidPrincipalName(voidPrincipalName);
        setVoidResult(voidResult);
        setStatus(status);
    }

    @Override
    public Transaction clone() {
        return new Transaction(
            providerId,
            persistenceUniqueId,
            groupName,
            transactionRequest==null ? null : transactionRequest.clone(),
            creditCard==null ? null : creditCard.clone(),
            authorizationTime,
            authorizationPrincipalName,
            authorizationResult==null ? null : authorizationResult.clone(),
            captureTime,
            capturePrincipalName,
            captureResult==null ? null : captureResult.clone(),
            voidTime,
            voidPrincipalName,
            voidResult==null ? null : voidResult.clone(),
            status
        );
    }

    public String getProviderId() {
        return providerId;
    }
    
    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getPersistenceUniqueId() {
        return persistenceUniqueId;
    }

    public void setPersistenceUniqueId(String persistenceUniqueId) {
        this.persistenceUniqueId = persistenceUniqueId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public TransactionRequest getTransactionRequest() {
        return transactionRequest;
    }

    public void setTransactionRequest(TransactionRequest transactionRequest) {
        this.transactionRequest = transactionRequest;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public long getAuthorizationTime() {
        return authorizationTime;
    }
    
    public void setAuthorizationTime(long authorizationTime) {
        this.authorizationTime = authorizationTime;
    }

    public String getAuthorizationPrincipalName() {
        return authorizationPrincipalName;
    }

    public void setAuthorizationPrincipalName(String authorizationPrincipalName) {
        this.authorizationPrincipalName = authorizationPrincipalName;
    }

    public AuthorizationResult getAuthorizationResult() {
        return authorizationResult;
    }

    public void setAuthorizationResult(AuthorizationResult authorizationResult) {
        this.authorizationResult = authorizationResult;
    }
    
    public long getCaptureTime() {
        return captureTime;
    }
    
    public void setCaptureTime(long captureTime) {
        this.captureTime = captureTime;
    }

    public String getCapturePrincipalName() {
        return capturePrincipalName;
    }

    public void setCapturePrincipalName(String capturePrincipalName) {
        this.capturePrincipalName = capturePrincipalName;
    }

    public CaptureResult getCaptureResult() {
        return captureResult;
    }

    public void setCaptureResult(CaptureResult captureResult) {
        this.captureResult = captureResult;
    }

    public long getVoidTime() {
        return voidTime;
    }
    
    public void setVoidTime(long voidTime) {
        this.voidTime = voidTime;
    }

    public String getVoidPrincipalName() {
        return voidPrincipalName;
    }

    public void setVoidPrincipalName(String voidPrincipalName) {
        this.voidPrincipalName = voidPrincipalName;
    }

    public VoidResult getVoidResult() {
        return voidResult;
    }
    
    public void setVoidResult(VoidResult voidResult) {
        this.voidResult = voidResult;
    }

    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
}
