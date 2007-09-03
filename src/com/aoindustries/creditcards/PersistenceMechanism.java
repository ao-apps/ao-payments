package com.aoindustries.creditcards;

/*
 * Copyright 2007 by AO Industries, Inc.,
 * 816 Azalea Rd, Mobile, Alabama, 36693, U.S.A.
 * All rights reserved.
 */
import java.security.Principal;
import java.security.acl.Group;
import java.sql.SQLException;
import java.util.Locale;

/**
 * A persistence mechanism stores the credit card data and transaction history.
 *
 * @author  AO Industries, Inc.
 */
public interface PersistenceMechanism {

    /**
     * Stores a credit card and returns its persistenceUniqueId.
     */
    String storeCreditCard(Principal principal, CreditCard creditCard, Locale userLocale) throws SQLException;

    /**
     * Updates the store masked card number for a credit card.  Updates the masked value on the provided creditCard.
     */
    void updateMaskedCardNumber(Principal principal, CreditCard creditCard, String maskedCardNumber, Locale userLocale) throws SQLException;

    /**
     * Deletes the credit card from the credit card list.
     */
    void deleteCreditCard(Principal principal, CreditCard creditCard, Locale userLocale) throws SQLException;
    
    /**
     * Inserts a new transaction into the database and returns its persistenceUniqueId.
     */
    String insertTransaction(Principal principal, Group group, Transaction transaction, Locale userLocale) throws SQLException;

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
    void saleCompleted(Principal principal, Transaction transaction, Locale userLocale) throws SQLException;

    /**
     * Updates a transaction in the database after the void has completed.
     */
    void voidCompleted(Principal principal, Transaction transaction, Locale userLocale) throws SQLException;
}
