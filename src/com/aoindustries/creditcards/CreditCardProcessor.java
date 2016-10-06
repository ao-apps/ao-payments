/*
 * ao-credit-cards - Credit card processing API supporting multiple payment gateways.
 * Copyright (C) 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2015, 2016  AO Industries, Inc.
 *     support@aoindustries.com
 *     7262 Bull Pen Cir
 *     Mobile, AL 36695
 *
 * This file is part of ao-credit-cards.
 *
 * ao-credit-cards is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ao-credit-cards is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ao-credit-cards.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aoindustries.creditcards;

import static com.aoindustries.creditcards.ApplicationResourcesAccessor.accessor;
import com.aoindustries.lang.LocalizedIllegalArgumentException;
import com.aoindustries.lang.NotImplementedException;
import com.aoindustries.sql.LocalizedSQLException;
import java.io.IOException;
import java.security.Principal;
import java.security.acl.Group;
import java.sql.SQLException;

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
	public Transaction sale(Principal principal, Group group, TransactionRequest transactionRequest, CreditCard creditCard) throws SQLException {
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
		String persistenceUniqueId = persistenceMechanism.insertTransaction(principal, group, transaction);
		transaction.setPersistenceUniqueId(persistenceUniqueId);

		// Perform sale
		SaleResult saleResult = provider.sale(transactionRequest, creditCard);
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
						throw new LocalizedSQLException(accessor, "CreditCardProcessor.sale.unexpectedApprovalResult", saleResult.getAuthorizationResult().getApprovalResult());
				}
				break;
			default:
				throw new LocalizedSQLException(accessor, "CreditCardProcessor.sale.unexpectedCommunicationResult", saleResult.getAuthorizationResult().getCommunicationResult());
		}
		transaction.setStatus(status);

		// Update persistence layer
		persistenceMechanism.saleCompleted(
			principal,
			transaction
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
	public Transaction authorize(Principal principal, Group group, TransactionRequest transactionRequest, CreditCard creditCard) throws SQLException {
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
			null, // capturePrincipalName
			null, // captureResult
			(long)-1, // voidTime
			null, // voidPrincipalName
			null, // voidResult
			Transaction.Status.PROCESSING
		);
		String persistenceUniqueId = persistenceMechanism.insertTransaction(principal, group, transaction);
		transaction.setPersistenceUniqueId(persistenceUniqueId);

		// Perform authorization
		AuthorizationResult authorizationResult = provider.authorize(transactionRequest, creditCard);
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
						throw new LocalizedSQLException(accessor, "CreditCardProcessor.sale.unexpectedApprovalResult", authorizationResult.getApprovalResult());
				}
				break;
			default:
				throw new LocalizedSQLException(accessor, "CreditCardProcessor.sale.unexpectedCommunicationResult", authorizationResult.getCommunicationResult());
		}
		transaction.setStatus(status);

		// Update persistence layer
		persistenceMechanism.authorizeCompleted(
			principal,
			transaction
		);

		return transaction;
	}

	/**
	 * Captures the funds from a previous call to <code>authorize</code>.
	 *
	 * @see  #authorize
	 */
	public CaptureResult capture(AuthorizationResult authorizationResult) {
		throw new NotImplementedException();
		// return provider.capture(authorizationResult);
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
	public VoidResult voidTransaction(Principal principal, Transaction transaction) throws SQLException {
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
				VoidResult voidResult = provider.voidTransaction(transaction);
				// Update the status
				transaction.setVoidResult(voidResult);
				if(voidResult.getCommunicationResult()==TransactionResult.CommunicationResult.SUCCESS) transaction.setStatus(Transaction.Status.VOID);
				persistenceMechanism.voidCompleted(principal, transaction);

				return voidResult;
			} else {
				throw new LocalizedIllegalArgumentException(accessor, "CreditCardProcessor.voidTransaction.providerUniqueId.required");
			}
		} else {
			throw new LocalizedIllegalArgumentException(accessor, "CreditCardProcessor.voidTransaction.invalidStatus", status==null ? null : status.toString());
		}
	}

	/**
	 * Requests a credit.
	 */
	public CreditResult credit(TransactionRequest transactionRequest, CreditCard creditCard) {
		throw new NotImplementedException();
		// return provider.credit(transactionRequest, creditCard);
	}

	/**
	 * Queries the provider to see if they support the secure storage of credit cards.
	 *
	 * @throws  IOException   when unable to contact the bank
	 */
	public boolean canStoreCreditCards() throws IOException {
		return provider.canStoreCreditCards();
	}

	/**
	 * Stores a credit card securely for later reuse.  Sets the providerId, providerUniqueId, principalName, groupName, and persistenceUniqueId.
	 * Upon success, clears the cardNumber and expiration date.
	 *
	 * @throws  IOException   when unable to contact the bank
	 * @throws  SQLException  when unable to store in the persistence layer
	 */
	public void storeCreditCard(Principal principal, Group group, CreditCard creditCard) throws IOException, SQLException {
		// First, store in the merchant system
		String providerUniqueId = provider.storeCreditCard(creditCard);
		creditCard.setProviderId(provider.getProviderId());
		creditCard.setProviderUniqueId(providerUniqueId);

		// Second, store to the persistence layer (this may also choose to store the card numbers)
		creditCard.setPrincipalName(principal.getName());
		creditCard.setGroupName(group.getName());
		String persistenceUniqueId = persistenceMechanism.storeCreditCard(principal, creditCard);
		creditCard.setPersistenceUniqueId(persistenceUniqueId);

		// Third, clear card numbers (since now stored)
		creditCard.setCardNumber(null);
		creditCard.setExpirationMonth((byte)-1);
		creditCard.setExpirationYear((short)-1);
	}

	/**
	 * Updates the credit card details, all except the card number and expiration.  If card stored to secure storage, updates that storage.
	 * Any desired changes must have been made to <code>creditCard</code> object preceeding this call.
	 *
	 * @throws  IOException   when unable to contact the bank
	 * @throws  SQLException  when unable to store in the persistence layer
	 */
	public void updateCreditCard(
		Principal principal,
		CreditCard creditCard
	) throws IOException, SQLException {
		if(creditCard.getProviderUniqueId()!=null) {
			// Update in persistence (this also enforces security)
			persistenceMechanism.updateCreditCard(principal, creditCard);
			// Update in secure storage
			provider.updateCreditCard(creditCard);
		}
	}

	/**
	 * Updates the credit card number, expiration, and (optionally) card code.  If card stored to secure storage, updates that storage.
	 * Otherwise updates <code>creditCard</code> directly.  In either event, the masked card number is updated.
	 *
	 * @throws  IOException   when unable to contact the bank
	 * @throws  SQLException  when unable to store in the persistence layer
	 */
	public void updateCreditCardNumberAndExpiration(Principal principal, CreditCard creditCard, String cardNumber, byte expirationMonth, short expirationYear, String cardCode) throws IOException, SQLException {
		cardNumber = CreditCard.numbersOnly(cardNumber);
		if(creditCard.getProviderUniqueId()!=null) {
			// Update in persistence (this also enforces security)
			String maskedCardNumber = CreditCard.maskCreditCardNumber(cardNumber);
			persistenceMechanism.updateCardNumber(principal, creditCard, cardNumber, expirationMonth, expirationYear);
			// Update in secure storage
			provider.updateCreditCardNumberAndExpiration(creditCard, cardNumber, expirationMonth, expirationYear, cardCode);
			// Update the masked number
			creditCard.setMaskedCardNumber(maskedCardNumber);
		} else {
			// Update directly
			creditCard.setCardNumber(cardNumber); // This also sets the masked value
			creditCard.setExpirationMonth(expirationMonth);
			creditCard.setExpirationYear(expirationYear);
			if(cardCode != null) creditCard.setCardCode(cardCode);
		}
	}

	/**
	 * Updates the credit card expiration.  If card stored to secure storage, updates that storage.
	 * Otherwise updates <code>creditCard</code> directly.
	 *
	 * @throws  IOException   when unable to contact the bank
	 */
	public void updateCreditCardExpiration(Principal principal, CreditCard creditCard, byte expirationMonth, short expirationYear) throws IOException, SQLException  {
		if(creditCard.getProviderUniqueId()!=null) {
			// Update in persistence (this also enforces security)
			persistenceMechanism.updateExpiration(principal, creditCard, expirationMonth, expirationYear);
			// Update in secure storage
			provider.updateCreditCardExpiration(creditCard, expirationMonth, expirationYear);
		} else {
			// Update directly
			creditCard.setExpirationMonth(expirationMonth);
			creditCard.setExpirationYear(expirationYear);
		}
	}

	/**
	 * Deletes the credit card information from the secure storage.  Clears the providerUniqueId and persistenceUniqueId on the creditCard.
	 *
	 * @throws  IOException   when unable to contact the bank
	 * @throws  SQLException  when unable to update the persistence layer
	 */
	public void deleteCreditCard(Principal principal, CreditCard creditCard) throws IOException, SQLException {
		// Delete from persistence (this also implements security)
		if(creditCard.getPersistenceUniqueId()!=null) {
			persistenceMechanism.deleteCreditCard(principal, creditCard);
			creditCard.setPersistenceUniqueId(null);
		}
		// Delete from provider database
		if(creditCard.getProviderUniqueId()!=null) {
			provider.deleteCreditCard(creditCard);
			creditCard.setProviderUniqueId(null);
		}
	}
}
