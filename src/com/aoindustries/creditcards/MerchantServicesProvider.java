/*
 * Copyright 2007-2010 by AO Industries, Inc.,
 * 7262 Bull Pen Cir, Mobile, Alabama, 36695, U.S.A.
 * All rights reserved.
 */
package com.aoindustries.creditcards;

import java.io.IOException;

/**
 * This set of functions may be provided by a specific merchant services provider/bank.
 * Not all of the functions will be supported by all providers.
 *
 * @author  AO Industries, Inc.
 */
public interface MerchantServicesProvider {

    /**
     * Gets the unique identifier for this provider.  The persistence layer may enforce unique
     * IDs per each provider.  Also, each credit, void, capture, and stored credit card transaction
     * must be performed by the same provider.  The identifier is compared to test this equality.
     */
    String getProviderId();

    /**
     * Performs an immediate sale, effectively a combination of <code>authorize</code> and <code>capture</code>.
     * No exceptions should ever be thrown, the CommunicationResult should be set to the appropriate error instead.
     *
     * @see  #authorize
     * @see  #capture
     */
    SaleResult sale(TransactionRequest transactionRequest, CreditCard creditCard);

    /**
     * Authorizes a sale.  The funds are reserved but not captured until a later call to capture.
     *
     * @see  #capture
     * @see  #voidTransaction
     */
    AuthorizationResult authorize(TransactionRequest transactionRequest, CreditCard creditCard);

    /**
     * Captures the funds from a previous call to <code>authorize</code>.
     *
     * @see  #authorize
     */
    CaptureResult capture(AuthorizationResult authorizationResult);

    /**
     * Voids a previous transaction.
     *
     * @throws  IOException   when unable to contact the bank
     */
    VoidResult voidTransaction(Transaction transaction);

    /**
     * Requests a credit.
     */
    CreditResult credit(TransactionRequest transactionRequest, CreditCard creditCard);

    /**
     * Queries the provider to see if they support the secure storage of credit cards.
     *
     * @throws  IOException   when unable to contact the bank
     */
    boolean canStoreCreditCards() throws IOException;

    /**
     * Stores a credit card securely for later reuse, returning its providerUniqueId.
     *
     * @throws  IOException   when unable to contact the bank
     */
    String storeCreditCard(CreditCard creditCard) throws IOException;

    /**
     * Updates the credit card number and expiration in the secure storage, card number and expiration on <code>creditCard</code> are not changed.
     *
     * @throws  IOException   when unable to contact the bank
     */
    void updateCreditCardNumberAndExpiration(CreditCard creditCard, String cardNumber, byte expirationMonth, short expirationYear) throws IOException;
    
    /**
     * Updates the credit card expiration in the secure storage, card expiration on <code>creditCard</code> are not changed.
     *
     * @throws  IOException   when unable to contact the bank
     */
    void updateCreditCardExpiration(CreditCard creditCard, byte expirationMonth, short expirationYear) throws IOException;

    /**
     * Deleted the credit card from the provider's secure storage.
     *
     * @throws  IOException   when unable to contact the bank
     */
    void deleteCreditCard(CreditCard creditCard) throws IOException;
}
