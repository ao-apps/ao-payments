package com.aoindustries.creditcards;

/*
 * Copyright 2007-2008 by AO Industries, Inc.,
 * 7262 Bull Pen Cir, Mobile, Alabama, 36695, U.S.A.
 * All rights reserved.
 */
import java.io.IOException;
import java.security.Principal;
import java.security.acl.Group;
import java.sql.SQLException;
import java.util.Locale;

/**
 * Processes credit card payments with pluggable merchant services providers and persistence mechanisms.
 *
 * TODO: Age check methods
 *
 * TODO: Make sure that no calls result in cross-provider data, like a card stored with on provider
 *       being used for transactions on another.
 *
 * TODO: Provide batch close calls?
 *
 * @author  AO Industries, Inc.
 */
public class CreditCardProcessor {

    final protected MerchantServicesProvider provider;
    final protected PersistenceMechanism persistenceMechanism;

    public CreditCardProcessor(MerchantServicesProvider provider, PersistenceMechanism persistenceMechanism) {
        this.provider = provider;
        this.persistenceMechanism = persistenceMechanism;
    }

    /**
     * Gets the uniqueId of the provider this processor is using.
     */
    public String getProviderId() {
        return provider.getProviderId();
    }

    /**
     * Performs an immediate sale, effectively a combination of <code>authorize</code> and <code>capture</code>.
     * The transaction is inserted into the persistence layer first in a PROCESSING state,
     * the provider performs the sale, and then the persistence layer transaction state is changed to the appropriate
     * final state.  Thus, any problem or restart will not lose record of the transaction, and the PROCESSING
     * state transaction may be manually resolved.
     *
     * @param  principal  <code>null</code> is acceptable
     * @param  group      <code>null</code> is acceptable
     *
     * @see  #authorize
     * @see  #capture
     */
    public Transaction sale(Principal principal, Group group, TransactionRequest transactionRequest, CreditCard creditCard, Locale userLocale) throws SQLException {
        // Insert into persistence layer
        long currentTimeMillis = System.currentTimeMillis();
        Transaction transaction = new Transaction(
            provider.getProviderId(),
            null, // persistenceUniqueId
            group==null ? null : group.getName(),
            transactionRequest,
            creditCard,
            currentTimeMillis,
            principal==null ? null : principal.getName(),
            null, // authorizationResult
            currentTimeMillis,
            principal==null ? null : principal.getName(),
            null, // captureResult
            (long)-1, // voidTime
            null, // voidPrincipalName
            null, // voidResult
            Transaction.Status.PROCESSING
        );
        String persistenceUniqueId = persistenceMechanism.insertTransaction(principal, group, transaction, userLocale);
        transaction.setPersistenceUniqueId(persistenceUniqueId);

        // Perform sale
        SaleResult saleResult = provider.sale(transactionRequest, creditCard, userLocale);
        long completedTimeMillis = System.currentTimeMillis();
        transaction.setAuthorizationResult(saleResult.getAuthorizationResult());
        transaction.setCaptureTime(completedTimeMillis);
        transaction.setCapturePrincipalName(principal==null ? null : principal.getName());
        transaction.setCaptureResult(saleResult.getCaptureResult());
        Transaction.Status status;
        switch(saleResult.getAuthorizationResult().getCommunicationResult()) {
            case LOCAL_ERROR:
                status = Transaction.Status.LOCAL_ERROR;
                break;
            case IO_ERROR:
                status = Transaction.Status.IO_ERROR;
                break;
            case GATEWAY_ERROR:
                status = Transaction.Status.GATEWAY_ERROR;
                break;
            case SUCCESS:
                switch(saleResult.getAuthorizationResult().getApprovalResult()) {
                    case APPROVED:
                        status = Transaction.Status.CAPTURED;
                        break;
                    case DECLINED:
                        status = Transaction.Status.DECLINED;
                        break;
                    case HOLD:
                        status = Transaction.Status.HOLD;
                        break;
                    default:
                        throw new LocalizedSQLException(userLocale, "CreditCardProcessor.sale.unexpectedApprovalResult", saleResult.getAuthorizationResult().getApprovalResult());
                }
                break;
            default:
                throw new LocalizedSQLException(userLocale, "CreditCardProcessor.sale.unexpectedCommunicationResult", saleResult.getAuthorizationResult().getCommunicationResult());
        }
        transaction.setStatus(status);

        // Update persistence layer
        persistenceMechanism.saleCompleted(
            principal,
            transaction,
            userLocale
        );

        return transaction;
    }

    /**
     * Authorizes a sale.  The funds are reserved but not captured until a later call to capture.
     * The transaction is inserted into the persistence layer first in a PROCESSING state,
     * the provider performs the authorization, and then the persistence layer transaction state is changed to the appropriate
     * final state.  Thus, any problem or restart will not lose record of the transaction, and the PROCESSING
     * state transaction may be manually resolved.
     *
     * @param  principal  <code>null</code> is acceptable
     * @param  group      <code>null</code> is acceptable
     * 
     * @see  #capture
     * @see  #voidTransaction
     */
    public Transaction authorize(Principal principal, Group group, TransactionRequest transactionRequest, CreditCard creditCard, Locale userLocale) throws SQLException {
        // Insert into persistence layer
        long currentTimeMillis = System.currentTimeMillis();
        Transaction transaction = new Transaction(
            provider.getProviderId(),
            null, // persistenceUniqueId
            group==null ? null : group.getName(),
            transactionRequest,
            creditCard,
            currentTimeMillis,
            principal==null ? null : principal.getName(),
            null, // authorizationResult
            (long)-1, // captureTime
            null, // capturePricipalName
            null, // captureResult
            (long)-1, // voidTime
            null, // voidPrincipalName
            null, // voidResult
            Transaction.Status.PROCESSING
        );
        String persistenceUniqueId = persistenceMechanism.insertTransaction(principal, group, transaction, userLocale);
        transaction.setPersistenceUniqueId(persistenceUniqueId);

        // Perform authorization
        AuthorizationResult authorizationResult = provider.authorize(transactionRequest, creditCard, userLocale);
        transaction.setAuthorizationResult(authorizationResult);
        Transaction.Status status;
        switch(authorizationResult.getCommunicationResult()) {
            case LOCAL_ERROR:
                status = Transaction.Status.LOCAL_ERROR;
                break;
            case IO_ERROR:
                status = Transaction.Status.IO_ERROR;
                break;
            case GATEWAY_ERROR:
                status = Transaction.Status.GATEWAY_ERROR;
                break;
            case SUCCESS:
                switch(authorizationResult.getApprovalResult()) {
                    case APPROVED:
                        status = Transaction.Status.AUTHORIZED;
                        break;
                    case DECLINED:
                        status = Transaction.Status.DECLINED;
                        break;
                    case HOLD:
                        status = Transaction.Status.HOLD;
                        break;
                    default:
                        throw new LocalizedSQLException(userLocale, "CreditCardProcessor.sale.unexpectedApprovalResult", authorizationResult.getApprovalResult());
                }
                break;
            default:
                throw new LocalizedSQLException(userLocale, "CreditCardProcessor.sale.unexpectedCommunicationResult", authorizationResult.getCommunicationResult());
        }
        transaction.setStatus(status);

        // Update persistence layer
        persistenceMechanism.authorizeCompleted(
            principal,
            transaction,
            userLocale
        );

        return transaction;
    }

    /**
     * Captures the funds from a previous call to <code>authorize</code>.
     *
     * @see  #authorize
     */
    public CaptureResult capture(AuthorizationResult authorizationResult, Locale userLocale) {
        throw new RuntimeException("TODO: Implement method");
        // TODO: return provider.capture(authorizationResult, userLocale);
    }

    /**
     * Voids a previous transaction.
     * Updates the persistence mechanism.
     * Updates the voidResult on the transaction.
     * If the void was successful, updates the status of the transaction passed in.
     * Transaction status must be AUTHORIZED, CAPTURED, or HOLD.
     *
     * @throws  IOException   when unable to contact the bank
     * @throws  SQLException  when unable to update the persistence layer
     * 
     * @see  #sale
     * @see  #authorize
     * @see  #capture
     */
    public VoidResult voidTransaction(Principal principal, Transaction transaction, Locale userLocale) throws SQLException {
        Transaction.Status status = transaction.getStatus();
        if(
            status==Transaction.Status.AUTHORIZED
            || status==Transaction.Status.CAPTURED
            || status==Transaction.Status.HOLD
        ) {
            // Void on the merchant
            if(
                transaction.getAuthorizationResult()!=null
                && transaction.getAuthorizationResult().getProviderUniqueId()!=null
                && transaction.getAuthorizationResult().getProviderUniqueId().length()>0
            ) {
                VoidResult voidResult = provider.voidTransaction(transaction, userLocale);
                // Update the status
                transaction.setVoidResult(voidResult);
                if(voidResult.getCommunicationResult()==TransactionResult.CommunicationResult.SUCCESS) transaction.setStatus(Transaction.Status.VOID);
                persistenceMechanism.voidCompleted(principal, transaction, userLocale);

                return voidResult;
            } else {
                throw new LocalizedIllegalArgumentException(userLocale, "CreditCardProcessor.voidTransaction.providerUniqueId.required");
            }
        } else {
            throw new LocalizedIllegalArgumentException(userLocale, "CreditCardProcessor.voidTransaction.invalidStatus", status==null ? null : status.toString(userLocale));
        }
    }

    /**
     * Requests a credit.
     */
    public CreditResult credit(TransactionRequest transactionRequest, CreditCard creditCard, Locale userLocale) {
        throw new RuntimeException("TODO: Implement method");
        // TODO: return provider.credit(transactionRequest, creditCard, userLocale);
    }

    /**
     * Queries the provider to see if they support the secure storage of credit cards.
     *
     * @throws  IOException   when unable to contact the bank
     */
    public boolean canStoreCreditCards(Locale userLocale) throws IOException {
        return provider.canStoreCreditCards(userLocale);
    }

    /**
     * Stores a credit card securely for later reuse.  Sets the providerId, providerUniqueId, principalName, groupName, and persistenceUniqueId.
     * Upon success, clears the cardNumber and expiration date.
     *
     * @throws  IOException   when unable to contact the bank
     * @throws  SQLException  when unable to store in the persistence layer
     */
    public void storeCreditCard(Principal principal, Group group, CreditCard creditCard, Locale userLocale) throws IOException, SQLException {
        // First, store in the merchant system
        String providerUniqueId = provider.storeCreditCard(creditCard, userLocale);
        creditCard.setProviderId(provider.getProviderId());
        creditCard.setProviderUniqueId(providerUniqueId);

        // Second, store to the persistence layer (this may also choose to store the card numbers)
        creditCard.setPrincipalName(principal.getName());
        creditCard.setGroupName(group.getName());
        String persistenceUniqueId = persistenceMechanism.storeCreditCard(principal, creditCard, userLocale);
        creditCard.setPersistenceUniqueId(persistenceUniqueId);

        // Third, clear card numbers (since now stored)
        creditCard.setCardNumber(null, userLocale);
        creditCard.setExpirationMonth((byte)-1, userLocale);
        creditCard.setExpirationYear((short)-1, userLocale);
    }

    /**
     * Updates the credit card number and expiration.  If card stored to secure storage, updates that storage.
     * Otherwise updates <code>creditCard</code> directly.  In either event, the masked card number is updated.
     *
     * @throws  IOException   when unable to contact the bank
     * @throws  SQLException  when unable to store in the persistence layer
     */
    public void updateCreditCardNumberAndExpiration(Principal principal, CreditCard creditCard, String cardNumber, byte expirationMonth, short expirationYear, Locale userLocale) throws IOException, SQLException {
        cardNumber = CreditCard.numbersOnly(cardNumber);
        if(creditCard.getProviderUniqueId()!=null) {
            // Update in persistence (this also enforces security)
            String maskedCardNumber = CreditCard.maskCreditCardNumber(cardNumber);
            persistenceMechanism.updateCardNumber(principal, creditCard, maskedCardNumber, cardNumber, expirationMonth, expirationYear, userLocale);
            // Update in secure storage
            provider.updateCreditCardNumberAndExpiration(creditCard, cardNumber, expirationMonth, expirationYear, userLocale);
            // Update the masked number
            creditCard.setMaskedCardNumber(maskedCardNumber);
        } else {
            // Update directly
            creditCard.setCardNumber(cardNumber, userLocale); // This also sets the masked value
            creditCard.setExpirationMonth(expirationMonth, userLocale);
            creditCard.setExpirationYear(expirationYear, userLocale);
        }
    }

    /**
     * Updates the credit card expiration.  If card stored to secure storage, updates that storage.
     * Otherwise updates <code>creditCard</code> directly.
     *
     * @throws  IOException   when unable to contact the bank
     */
    public void updateCreditCardExpiration(Principal principal, CreditCard creditCard, byte expirationMonth, short expirationYear, Locale userLocale) throws IOException, SQLException  {
        if(creditCard.getProviderUniqueId()!=null) {
            // Update in persistence (this also enforces security)
            persistenceMechanism.updateExpiration(principal, creditCard, expirationMonth, expirationYear, userLocale);
            // Update in secure storage
            provider.updateCreditCardExpiration(creditCard, expirationMonth, expirationYear, userLocale);
        } else {
            // Update directly
            creditCard.setExpirationMonth(expirationMonth, userLocale);
            creditCard.setExpirationYear(expirationYear, userLocale);
        }
    }

    /**
     * Deletes the credit card information from the secure storage.  Clears the providerUniqueId and persistenceUniqueId on the creditCard.
     *
     * @throws  IOException   when unable to contact the bank
     * @throws  SQLException  when unable to update the persistence layer
     */
    public void deleteCreditCard(Principal principal, CreditCard creditCard, Locale userLocale) throws IOException, SQLException {
        // Delete from persistence (this also implements security)
        if(creditCard.getPersistenceUniqueId()!=null) {
            persistenceMechanism.deleteCreditCard(principal, creditCard, userLocale);
            creditCard.setPersistenceUniqueId(null);
        }
        // Delete from provider database
        if(creditCard.getProviderUniqueId()!=null) {
            provider.deleteCreditCard(creditCard, userLocale);
            creditCard.setProviderUniqueId(null);
        }
    }
}
