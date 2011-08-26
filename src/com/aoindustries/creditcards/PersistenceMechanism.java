/*
 * Copyright 2007-2011 by AO Industries, Inc.,
 * 7262 Bull Pen Cir, Mobile, Alabama, 36695, U.S.A.
 * All rights reserved.
 */
package com.aoindustries.creditcards;

import java.security.Principal;
import java.security.acl.Group;
import java.sql.SQLException;

/**
 * A persistence mechanism stores the credit card data and transaction history.
 *
 * @author  AO Industries, Inc.
 */
public interface PersistenceMechanism {

    /**
     * Stores a credit card and returns its persistenceUniqueId.
     */
    String storeCreditCard(Principal principal, CreditCard creditCard) throws SQLException;

    /**
     * Updates the stored card number and expiration for a credit card.
     */
    void updateCardNumber(Principal principal, CreditCard creditCard, String cardNumber, byte expirationMonth, short expirationYear) throws SQLException;

    /**
     * Optionally updates the expiration for a credit card.
     */
    void updateExpiration(Principal principal, CreditCard creditCard, byte expirationMonth, short expirationYear) throws SQLException;

    /**
     * Deletes the credit card from the credit card list.
     */
    void deleteCreditCard(Principal principal, CreditCard creditCard) throws SQLException;
    
    /**
     * Inserts a new transaction into the database and returns its persistenceUniqueId.
     *
     * @param  principal  <code>null</code> is acceptable
     * @param  group      <code>null</code> is acceptable
     */
    String insertTransaction(Principal principal, Group group, Transaction transaction) throws SQLException;

    /**
     * Updates a transaction in the database after the sale has completed.  The following transaction fields have been updated
     * and must be stored:
     * <ol>
     *   <li>authorizationResult</li>
     *   <li>captureTime</li>
     *   <li>capturePrincipalName</li>
     *   <li>captureResult</li>
     *   <li>status</li>
     * </ol>
     */
    void saleCompleted(Principal principal, Transaction transaction) throws SQLException;

    /**
     * Updates a transaction in the database after the authorize has completed.  The following transaction fields have been updated
     * and must be stored:
     * <ol>
     *   <li>authorizationResult</li>
     *   <li>status</li>
     * </ol>
     */
    void authorizeCompleted(Principal principal, Transaction transaction) throws SQLException;

    /**
     * Updates a transaction in the database after the void has completed.
     */
    void voidCompleted(Principal principal, Transaction transaction) throws SQLException;
}
