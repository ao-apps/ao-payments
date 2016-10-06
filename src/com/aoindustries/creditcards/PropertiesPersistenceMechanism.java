/*
 * ao-credit-cards - Credit card processing API supporting multiple payment gateways.
 * Copyright (C) 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2014, 2015, 2016  AO Industries, Inc.
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
import com.aoindustries.io.FileUtils;
import com.aoindustries.sql.LocalizedSQLException;
import com.aoindustries.util.PropertiesUtils;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.security.Principal;
import java.security.acl.Group;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Stores everything in a simple on-disk properties file.  This is not a scalable solution
 * and is intended for debugging purposes only.
 *
 * @author  AO Industries, Inc.
 */
public class PropertiesPersistenceMechanism implements PersistenceMechanism {

	private static final Logger logger = Logger.getLogger(PropertiesPersistenceMechanism.class.getName());

	private static final Map<String,PropertiesPersistenceMechanism> ppms = new HashMap<String,PropertiesPersistenceMechanism>();

	/**
	 * For intra-JVM reusability, only one instance is made per unique path.
	 * However, different paths resolving to the same file (symlink in Unix
	 * is one example) or multiple processes accessing the same file are
	 * not guaranteed to interoperate properly.  In fact, there are no mechanisms
	 * in this code to control that in any way.  Once again, this is intended
	 * for development and debugging use only.
	 */
	public static PropertiesPersistenceMechanism getInstance(String propertiesFilePath) {
		synchronized(ppms) {
			PropertiesPersistenceMechanism ppm = ppms.get(propertiesFilePath);
			if(ppm==null) {
				ppm = new PropertiesPersistenceMechanism(propertiesFilePath);
				ppms.put(propertiesFilePath, ppm);
			}
			return ppm;
		}
	}

	/**
	 * The properties file path is obtained at creation time just in case the configuration mapping
	 * changes values.  The properties file will remain the same.
	 */
	final protected String propertiesFilePath;

	/**
	 * The list of credit cards in the database.
	 */
	protected List<CreditCard> internalCreditCards;

	/**
	 * The list of transactions in the database.
	 */
	protected List<Transaction> internalTransactions;

	/**
	 * Creates a new properties persistence mechanism.
	 *
	 * @throws  IllegalArgumentException  if not properly configured
	 */
	private PropertiesPersistenceMechanism(String propertiesFilePath) {
		this.propertiesFilePath = propertiesFilePath;
	}

	private synchronized void loadIfNeeded() throws SQLException {
		if(internalCreditCards==null || internalTransactions==null) {
			try {
				File file = new File(propertiesFilePath);
				if(file.exists()) {
					List<CreditCard> newCreditCards = new ArrayList<CreditCard>();
					List<Transaction> newTransactions = new ArrayList<Transaction>();
					Properties props = PropertiesUtils.loadFromFile(file);
					for(long counter=1; counter<Long.MAX_VALUE; counter++) {
						String persistenceUniqueId = props.getProperty("creditCards."+counter+".persistenceUniqueId");
						if(persistenceUniqueId==null) break;
						CreditCard newCreditCard = new CreditCard(
							persistenceUniqueId,
							props.getProperty("creditCards."+counter+".principalName"),
							props.getProperty("creditCards."+counter+".groupName"),
							props.getProperty("creditCards."+counter+".providerId"),
							props.getProperty("creditCards."+counter+".providerUniqueId"),
							null,
							props.getProperty("creditCards."+counter+".maskedCardNumber"),
							(byte)-1,
							(short)-1,
							null,
							props.getProperty("creditCards."+counter+".firstName"),
							props.getProperty("creditCards."+counter+".lastName"),
							props.getProperty("creditCards."+counter+".companyName"),
							props.getProperty("creditCards."+counter+".email"),
							props.getProperty("creditCards."+counter+".phone"),
							props.getProperty("creditCards."+counter+".fax"),
							props.getProperty("creditCards."+counter+".customerId"),
							props.getProperty("creditCards."+counter+".customerTaxId"),
							props.getProperty("creditCards."+counter+".streetAddress1"),
							props.getProperty("creditCards."+counter+".streetAddress2"),
							props.getProperty("creditCards."+counter+".city"),
							props.getProperty("creditCards."+counter+".state"),
							props.getProperty("creditCards."+counter+".postalCode"),
							props.getProperty("creditCards."+counter+".countryCode"),
							props.getProperty("creditCards."+counter+".comments")
						);
						newCreditCards.add(newCreditCard);
					}
					for(long counter=1; counter<Long.MAX_VALUE; counter++) {
						String persistenceUniqueId = props.getProperty("transactions."+counter+".persistenceUniqueId");
						if(persistenceUniqueId==null) break;
						String currencyCode = props.getProperty("transactions."+counter+".transactionRequest.currencyCode");
						Currency currency = currencyCode==null ? null : Currency.getInstance(currencyCode);
						String amountString = props.getProperty("transactions."+counter+".transactionRequest.amount");
						BigDecimal amount = amountString==null ? null : new BigDecimal(amountString);
						String taxAmountString = props.getProperty("transactions."+counter+".transactionRequest.taxAmount");
						BigDecimal taxAmount = taxAmountString==null ? null : new BigDecimal(taxAmountString);
						String shippingAmountString = props.getProperty("transactions."+counter+".transactionRequest.shippingAmount");
						BigDecimal shippingAmount = shippingAmountString==null ? null : new BigDecimal(shippingAmountString);
						String dutyAmountString = props.getProperty("transactions."+counter+".transactionRequest.dutyAmount");
						BigDecimal dutyAmount = dutyAmountString==null ? null : new BigDecimal(dutyAmountString);
						String authorizationCommunicationResultString = props.getProperty("transactions."+counter+".authorizationResult.communicationResult");
						TransactionResult.CommunicationResult authorizationCommunicationResult = authorizationCommunicationResultString==null ? null : TransactionResult.CommunicationResult.valueOf(authorizationCommunicationResultString);
						String authorizationErrorCodeString = props.getProperty("transactions."+counter+".authorizationResult.errorCode");
						TransactionResult.ErrorCode authorizationErrorCode = authorizationErrorCodeString==null ? null : TransactionResult.ErrorCode.valueOf(authorizationErrorCodeString);
						String approvalResultString = props.getProperty("transactions."+counter+".authorizationResult.approvalResult");
						AuthorizationResult.ApprovalResult approvalResult = approvalResultString==null ? null : AuthorizationResult.ApprovalResult.valueOf(approvalResultString);
						String declineReasonString = props.getProperty("transactions."+counter+".authorizationResult.declineReason");
						AuthorizationResult.DeclineReason declineReason = declineReasonString==null ? null : AuthorizationResult.DeclineReason.valueOf(declineReasonString);
						String reviewReasonString = props.getProperty("transactions."+counter+".authorizationResult.reviewReason");
						AuthorizationResult.ReviewReason reviewReason = reviewReasonString==null ? null : AuthorizationResult.ReviewReason.valueOf(reviewReasonString);
						String cvvResultString = props.getProperty("transactions."+counter+".authorizationResult.cvvResult");
						AuthorizationResult.CvvResult cvvResult = cvvResultString==null ? null : AuthorizationResult.CvvResult.valueOf(cvvResultString);
						String avsResultString = props.getProperty("transactions."+counter+".authorizationResult.avsResult");
						AuthorizationResult.AvsResult avsResult = avsResultString==null ? null : AuthorizationResult.AvsResult.valueOf(avsResultString);
						String captureTimeString = props.getProperty("transactions."+counter+".captureTime");
						long captureTime = captureTimeString==null ? (long)-1 : Long.parseLong(captureTimeString);
						String captureCommunicationResultString = props.getProperty("transactions."+counter+".captureResult.communicationResult");
						TransactionResult.CommunicationResult captureCommunicationResult = captureCommunicationResultString==null ? null : TransactionResult.CommunicationResult.valueOf(captureCommunicationResultString);
						String captureErrorCodeString = props.getProperty("transactions."+counter+".captureResult.errorCode");
						TransactionResult.ErrorCode captureErrorCode = captureErrorCodeString==null ? null : TransactionResult.ErrorCode.valueOf(captureErrorCodeString);
						String voidTimeString = props.getProperty("transactions."+counter+".voidTime");
						long voidTime = voidTimeString==null ? (long)-1 : Long.parseLong(voidTimeString);
						String voidCommunicationResultString = props.getProperty("transactions."+counter+".voidResult.communicationResult");
						TransactionResult.CommunicationResult voidCommunicationResult = voidCommunicationResultString==null ? null : TransactionResult.CommunicationResult.valueOf(voidCommunicationResultString);
						String voidErrorCodeString = props.getProperty("transactions."+counter+".voidResult.errorCode");
						TransactionResult.ErrorCode voidErrorCode = voidErrorCodeString==null ? null : TransactionResult.ErrorCode.valueOf(voidErrorCodeString);
						String statusString = props.getProperty("transactions."+counter+".status");
						Transaction.Status status = statusString==null ? null : Transaction.Status.valueOf(statusString);
						Transaction newTransaction = new Transaction(
							props.getProperty("transactions."+counter+".providerId"),
							persistenceUniqueId,
							props.getProperty("transactions."+counter+".groupName"),
							new TransactionRequest(
								"true".equals(props.getProperty("transactions."+counter+".transactionRequest.testMode")),
								props.getProperty("transactions."+counter+".transactionRequest.customerIp"),
								Integer.parseInt(props.getProperty("transactions."+counter+".transactionRequest.duplicateWindow")),
								props.getProperty("transactions."+counter+".transactionRequest.orderNumber"),
								currency,
								amount,
								taxAmount,
								"true".equals(props.getProperty("transactions."+counter+".transactionRequest.taxExempt")),
								shippingAmount,
								dutyAmount,
								props.getProperty("transactions."+counter+".transactionRequest.shippingFirstName"),
								props.getProperty("transactions."+counter+".transactionRequest.shippingLastName"),
								props.getProperty("transactions."+counter+".transactionRequest.shippingCompanyName"),
								props.getProperty("transactions."+counter+".transactionRequest.shippingStreetAddress1"),
								props.getProperty("transactions."+counter+".transactionRequest.shippingStreetAddress2"),
								props.getProperty("transactions."+counter+".transactionRequest.shippingCity"),
								props.getProperty("transactions."+counter+".transactionRequest.shippingState"),
								props.getProperty("transactions."+counter+".transactionRequest.shippingPostalCode"),
								props.getProperty("transactions."+counter+".transactionRequest.shippingCountryCode"),
								"true".equals(props.getProperty("transactions."+counter+".transactionRequest.emailCustomer")),
								props.getProperty("transactions."+counter+".transactionRequest.merchantEmail"),
								props.getProperty("transactions."+counter+".transactionRequest.invoiceNumber"),
								props.getProperty("transactions."+counter+".transactionRequest.purchaseOrderNumber"),
								props.getProperty("transactions."+counter+".transactionRequest.description")
							),
							new CreditCard(
								null,
								props.getProperty("transactions."+counter+".creditCard.principalName"),
								props.getProperty("transactions."+counter+".creditCard.groupName"),
								props.getProperty("transactions."+counter+".providerId"),
								props.getProperty("transactions."+counter+".creditCard.providerUniqueId"),
								null,
								props.getProperty("transactions."+counter+".creditCard.maskedCardNumber"),
								(byte)-1,
								(short)-1,
								null,
								props.getProperty("transactions."+counter+".creditCard.firstName"),
								props.getProperty("transactions."+counter+".creditCard.lastName"),
								props.getProperty("transactions."+counter+".creditCard.companyName"),
								props.getProperty("transactions."+counter+".creditCard.email"),
								props.getProperty("transactions."+counter+".creditCard.phone"),
								props.getProperty("transactions."+counter+".creditCard.fax"),
								props.getProperty("transactions."+counter+".creditCard.customerId"),
								props.getProperty("transactions."+counter+".creditCard.customerTaxId"),
								props.getProperty("transactions."+counter+".creditCard.streetAddress1"),
								props.getProperty("transactions."+counter+".creditCard.streetAddress2"),
								props.getProperty("transactions."+counter+".creditCard.city"),
								props.getProperty("transactions."+counter+".creditCard.state"),
								props.getProperty("transactions."+counter+".creditCard.postalCode"),
								props.getProperty("transactions."+counter+".creditCard.countryCode"),
								props.getProperty("transactions."+counter+".creditCard.comments")
							),
							Long.parseLong(props.getProperty("transactions."+counter+".authorizationTime")),
							props.getProperty("transactions."+counter+".authorizationPrincipalName"),
							new AuthorizationResult(
								props.getProperty("transactions."+counter+".providerId"),
								authorizationCommunicationResult,
								props.getProperty("transactions."+counter+".authorizationResult.providerErrorCode"),
								authorizationErrorCode,
								props.getProperty("transactions."+counter+".authorizationResult.providerErrorMessage"),
								props.getProperty("transactions."+counter+".authorizationResult.providerUniqueId"),
								props.getProperty("transactions."+counter+".authorizationResult.providerApprovalResult"),
								approvalResult,
								props.getProperty("transactions."+counter+".authorizationResult.providerDeclineReason"),
								declineReason,
								props.getProperty("transactions."+counter+".authorizationResult.providerReviewReason"),
								reviewReason,
								props.getProperty("transactions."+counter+".authorizationResult.providerCvvResult"),
								cvvResult,
								props.getProperty("transactions."+counter+".authorizationResult.providerAvsResult"),
								avsResult,
								props.getProperty("transactions."+counter+".authorizationResult.approvalCode")
							),
							captureTime,
							props.getProperty("transactions."+counter+".capturePrincipalName"),
							new CaptureResult(
								props.getProperty("transactions."+counter+".providerId"),
								captureCommunicationResult,
								props.getProperty("transactions."+counter+".captureResult.providerErrorCode"),
								captureErrorCode,
								props.getProperty("transactions."+counter+".captureResult.providerErrorMessage"),
								props.getProperty("transactions."+counter+".captureResult.providerUniqueId")
							),
							voidTime,
							props.getProperty("transactions."+counter+".voidPrincipalName"),
							new VoidResult(
								props.getProperty("transactions."+counter+".providerId"),
								voidCommunicationResult,
								props.getProperty("transactions."+counter+".voidResult.providerErrorCode"),
								voidErrorCode,
								props.getProperty("transactions."+counter+".voidResult.providerErrorMessage"),
								props.getProperty("transactions."+counter+".voidResult.providerUniqueId")
							),
							status
						);
						newTransactions.add(newTransaction);
					}
					internalCreditCards = newCreditCards;
					internalTransactions = newTransactions;
				} else {
					internalCreditCards = new ArrayList<CreditCard>();
					internalTransactions = new ArrayList<Transaction>();
				}
			} catch(IOException err) {
				throw new SQLException(err);
			}
		}
	}

	private synchronized void save() throws SQLException {
		try {
			File newFile = new File(propertiesFilePath+".new");
			File file = new File(propertiesFilePath);
			File backupFile = new File(propertiesFilePath+".backup");
			Properties props = new Properties();
			// Add the credit cards
			long counter=1;
			for(CreditCard internalCreditCard : internalCreditCards) {
				props.setProperty("creditCards."+counter+".persistenceUniqueId", internalCreditCard.getPersistenceUniqueId());
				if(internalCreditCard.getPrincipalName()!=null) props.setProperty("creditCards."+counter+".principalName", internalCreditCard.getPrincipalName());
				if(internalCreditCard.getGroupName()!=null) props.setProperty("creditCards."+counter+".groupName", internalCreditCard.getGroupName());
				if(internalCreditCard.getProviderId()!=null) props.setProperty("creditCards."+counter+".providerId", internalCreditCard.getProviderId());
				if(internalCreditCard.getProviderUniqueId()!=null) props.setProperty("creditCards."+counter+".providerUniqueId", internalCreditCard.getProviderUniqueId());
				if(internalCreditCard.getMaskedCardNumber()!=null) props.setProperty("creditCards."+counter+".maskedCardNumber", internalCreditCard.getMaskedCardNumber());
				if(internalCreditCard.getFirstName()!=null) props.setProperty("creditCards."+counter+".firstName", internalCreditCard.getFirstName());
				if(internalCreditCard.getLastName()!=null) props.setProperty("creditCards."+counter+".lastName", internalCreditCard.getLastName());
				if(internalCreditCard.getCompanyName()!=null) props.setProperty("creditCards."+counter+".companyName", internalCreditCard.getCompanyName());
				if(internalCreditCard.getEmail()!=null) props.setProperty("creditCards."+counter+".email", internalCreditCard.getEmail());
				if(internalCreditCard.getPhone()!=null) props.setProperty("creditCards."+counter+".phone", internalCreditCard.getPhone());
				if(internalCreditCard.getFax()!=null) props.setProperty("creditCards."+counter+".fax", internalCreditCard.getFax());
				if(internalCreditCard.getCustomerId()!=null) props.setProperty("creditCards."+counter+".customerId", internalCreditCard.getCustomerId());
				if(internalCreditCard.getCustomerTaxId()!=null) props.setProperty("creditCards."+counter+".customerTaxId", internalCreditCard.getCustomerTaxId());
				if(internalCreditCard.getStreetAddress1()!=null) props.setProperty("creditCards."+counter+".streetAddress1", internalCreditCard.getStreetAddress1());
				if(internalCreditCard.getStreetAddress2()!=null) props.setProperty("creditCards."+counter+".streetAddress2", internalCreditCard.getStreetAddress2());
				if(internalCreditCard.getCity()!=null) props.setProperty("creditCards."+counter+".city", internalCreditCard.getCity());
				if(internalCreditCard.getState()!=null) props.setProperty("creditCards."+counter+".state", internalCreditCard.getState());
				if(internalCreditCard.getPostalCode()!=null) props.setProperty("creditCards."+counter+".postalCode", internalCreditCard.getPostalCode());
				if(internalCreditCard.getCountryCode()!=null) props.setProperty("creditCards."+counter+".countryCode", internalCreditCard.getCountryCode());
				if(internalCreditCard.getComments()!=null) props.setProperty("creditCards."+counter+".comments", internalCreditCard.getComments());
				counter++;
			}
			// Add the transactions
			counter=1;
			for(Transaction internalTransaction : internalTransactions) {
				props.setProperty("transactions."+counter+".providerId", internalTransaction.getProviderId());
				props.setProperty("transactions."+counter+".persistenceUniqueId", internalTransaction.getPersistenceUniqueId());
				if(internalTransaction.getGroupName()!=null) props.setProperty("transactions."+counter+".groupName", internalTransaction.getGroupName());
				TransactionRequest transactionRequest = internalTransaction.getTransactionRequest();
				if(transactionRequest!=null) {
					props.setProperty("transactions."+counter+".transactionRequest.testMode", transactionRequest.getTestMode() ? "true" : "false");
					if(transactionRequest.getCustomerIp()!=null) props.setProperty("transactions."+counter+".transactionRequest.customerIp", transactionRequest.getCustomerIp());
					props.setProperty("transactions."+counter+".transactionRequest.duplicateWindow", Integer.toString(transactionRequest.getDuplicateWindow()));
					if(transactionRequest.getOrderNumber()!=null) props.setProperty("transactions."+counter+".transactionRequest.orderNumber", transactionRequest.getOrderNumber());
					if(transactionRequest.getCurrency()!=null) props.setProperty("transactions."+counter+".transactionRequest.currencyCode", transactionRequest.getCurrency().getCurrencyCode());
					if(transactionRequest.getAmount()!=null) props.setProperty("transactions."+counter+".transactionRequest.amount", transactionRequest.getAmount().toString());
					if(transactionRequest.getTaxAmount()!=null) props.setProperty("transactions."+counter+".transactionRequest.taxAmount", transactionRequest.getTaxAmount().toString());
					props.setProperty("transactions."+counter+".transactionRequest.taxExempt", transactionRequest.getTaxExempt() ? "true" : "false");
					if(transactionRequest.getShippingAmount()!=null) props.setProperty("transactions."+counter+".transactionRequest.shippingAmount", transactionRequest.getShippingAmount().toString());
					if(transactionRequest.getDutyAmount()!=null) props.setProperty("transactions."+counter+".transactionRequest.dutyAmount", transactionRequest.getDutyAmount().toString());
					if(transactionRequest.getShippingFirstName()!=null) props.setProperty("transactions."+counter+".transactionRequest.shippingFirstName", transactionRequest.getShippingFirstName());
					if(transactionRequest.getShippingLastName()!=null) props.setProperty("transactions."+counter+".transactionRequest.shippingLastName", transactionRequest.getShippingLastName());
					if(transactionRequest.getShippingCompanyName()!=null) props.setProperty("transactions."+counter+".transactionRequest.shippingCompanyName", transactionRequest.getShippingCompanyName());
					if(transactionRequest.getShippingStreetAddress1()!=null) props.setProperty("transactions."+counter+".transactionRequest.shippingStreetAddress1", transactionRequest.getShippingStreetAddress1());
					if(transactionRequest.getShippingStreetAddress2()!=null) props.setProperty("transactions."+counter+".transactionRequest.shippingStreetAddress2", transactionRequest.getShippingStreetAddress2());
					if(transactionRequest.getShippingCity()!=null) props.setProperty("transactions."+counter+".transactionRequest.shippingCity", transactionRequest.getShippingCity());
					if(transactionRequest.getShippingState()!=null) props.setProperty("transactions."+counter+".transactionRequest.shippingState", transactionRequest.getShippingState());
					if(transactionRequest.getShippingPostalCode()!=null) props.setProperty("transactions."+counter+".transactionRequest.shippingPostalCode", transactionRequest.getShippingPostalCode());
					if(transactionRequest.getShippingCountryCode()!=null) props.setProperty("transactions."+counter+".transactionRequest.shippingCountryCode", transactionRequest.getShippingCountryCode());
					props.setProperty("transactions."+counter+".transactionRequest.emailCustomer", transactionRequest.getEmailCustomer() ? "true" : "false");
					if(transactionRequest.getMerchantEmail()!=null) props.setProperty("transactions."+counter+".transactionRequest.merchantEmail", transactionRequest.getMerchantEmail());
					if(transactionRequest.getInvoiceNumber()!=null) props.setProperty("transactions."+counter+".transactionRequest.invoiceNumber", transactionRequest.getInvoiceNumber());
					if(transactionRequest.getPurchaseOrderNumber()!=null) props.setProperty("transactions."+counter+".transactionRequest.purchaseOrderNumber", transactionRequest.getPurchaseOrderNumber());
					if(transactionRequest.getDescription()!=null) props.setProperty("transactions."+counter+".transactionRequest.description", transactionRequest.getDescription());
				}
				CreditCard creditCard = internalTransaction.getCreditCard();
				if(creditCard!=null) {
					if(creditCard.getPrincipalName()!=null) props.setProperty("transactions."+counter+".creditCard.principalName", creditCard.getPrincipalName());
					if(creditCard.getGroupName()!=null) props.setProperty("transactions."+counter+".creditCard.groupName", creditCard.getGroupName());
					if(creditCard.getProviderUniqueId()!=null) props.setProperty("transactions."+counter+".creditCard.providerUniqueId", creditCard.getProviderUniqueId());
					if(creditCard.getMaskedCardNumber()!=null) props.setProperty("transactions."+counter+".creditCard.maskedCardNumber", creditCard.getMaskedCardNumber());
					if(creditCard.getFirstName()!=null) props.setProperty("transactions."+counter+".creditCard.firstName", creditCard.getFirstName());
					if(creditCard.getLastName()!=null) props.setProperty("transactions."+counter+".creditCard.lastName", creditCard.getLastName());
					if(creditCard.getCompanyName()!=null) props.setProperty("transactions."+counter+".creditCard.companyName", creditCard.getCompanyName());
					if(creditCard.getEmail()!=null) props.setProperty("transactions."+counter+".creditCard.email", creditCard.getEmail());
					if(creditCard.getPhone()!=null) props.setProperty("transactions."+counter+".creditCard.phone", creditCard.getPhone());
					if(creditCard.getFax()!=null) props.setProperty("transactions."+counter+".creditCard.fax", creditCard.getFax());
					if(creditCard.getCustomerId()!=null) props.setProperty("transactions."+counter+".creditCard.customerId", creditCard.getCustomerId());
					if(creditCard.getCustomerTaxId()!=null) props.setProperty("transactions."+counter+".creditCard.customerTaxId", creditCard.getCustomerTaxId());
					if(creditCard.getStreetAddress1()!=null) props.setProperty("transactions."+counter+".creditCard.streetAddress1", creditCard.getStreetAddress1());
					if(creditCard.getStreetAddress2()!=null) props.setProperty("transactions."+counter+".creditCard.streetAddress2", creditCard.getStreetAddress2());
					if(creditCard.getCity()!=null) props.setProperty("transactions."+counter+".creditCard.city", creditCard.getCity());
					if(creditCard.getState()!=null) props.setProperty("transactions."+counter+".creditCard.state", creditCard.getState());
					if(creditCard.getPostalCode()!=null) props.setProperty("transactions."+counter+".creditCard.postalCode", creditCard.getPostalCode());
					if(creditCard.getCountryCode()!=null) props.setProperty("transactions."+counter+".creditCard.countryCode", creditCard.getCountryCode());
					if(creditCard.getComments()!=null) props.setProperty("transactions."+counter+".creditCard.comments", creditCard.getComments());
				}
				props.setProperty("transactions."+counter+".authorizationTime", Long.toString(internalTransaction.getAuthorizationTime()));
				if(internalTransaction.getAuthorizationPrincipalName()!=null) props.setProperty("transactions."+counter+".authorizationPrincipalName", internalTransaction.getAuthorizationPrincipalName());
				AuthorizationResult authorizationResult = internalTransaction.getAuthorizationResult();
				if(authorizationResult!=null) {
					if(authorizationResult.getCommunicationResult()!=null) props.setProperty("transactions."+counter+".authorizationResult.communicationResult", authorizationResult.getCommunicationResult().name());
					if(authorizationResult.getProviderErrorCode()!=null) props.setProperty("transactions."+counter+".authorizationResult.providerErrorCode", authorizationResult.getProviderErrorCode());
					if(authorizationResult.getErrorCode()!=null) props.setProperty("transactions."+counter+".authorizationResult.errorCode", authorizationResult.getErrorCode().name());
					if(authorizationResult.getProviderErrorMessage()!=null) props.setProperty("transactions."+counter+".authorizationResult.providerErrorMessage", authorizationResult.getProviderErrorMessage());
					if(authorizationResult.getProviderUniqueId()!=null) props.setProperty("transactions."+counter+".authorizationResult.providerUniqueId", authorizationResult.getProviderUniqueId());
					if(authorizationResult.getProviderApprovalResult()!=null) props.setProperty("transactions."+counter+".authorizationResult.providerApprovalResult", authorizationResult.getProviderApprovalResult());
					if(authorizationResult.getApprovalResult()!=null) props.setProperty("transactions."+counter+".authorizationResult.approvalResult", authorizationResult.getApprovalResult().name());
					if(authorizationResult.getProviderDeclineReason()!=null) props.setProperty("transactions."+counter+".authorizationResult.providerDeclineReason", authorizationResult.getProviderDeclineReason());
					if(authorizationResult.getDeclineReason()!=null) props.setProperty("transactions."+counter+".authorizationResult.declineReason", authorizationResult.getDeclineReason().name());
					if(authorizationResult.getProviderReviewReason()!=null) props.setProperty("transactions."+counter+".authorizationResult.providerReviewReason", authorizationResult.getProviderReviewReason());
					if(authorizationResult.getReviewReason()!=null) props.setProperty("transactions."+counter+".authorizationResult.reviewReason", authorizationResult.getReviewReason().name());
					if(authorizationResult.getProviderCvvResult()!=null) props.setProperty("transactions."+counter+".authorizationResult.providerCvvResult", authorizationResult.getProviderCvvResult());
					if(authorizationResult.getCvvResult()!=null) props.setProperty("transactions."+counter+".authorizationResult.cvvResult", authorizationResult.getCvvResult().name());
					if(authorizationResult.getProviderAvsResult()!=null) props.setProperty("transactions."+counter+".authorizationResult.providerAvsResult", authorizationResult.getProviderAvsResult());
					if(authorizationResult.getAvsResult()!=null) props.setProperty("transactions."+counter+".authorizationResult.avsResult", authorizationResult.getAvsResult().name());
					if(authorizationResult.getApprovalCode()!=null) props.setProperty("transactions."+counter+".authorizationResult.approvalCode", authorizationResult.getApprovalCode());
				}
				props.setProperty("transactions."+counter+".captureTime", Long.toString(internalTransaction.getCaptureTime()));
				if(internalTransaction.getCapturePrincipalName()!=null) props.setProperty("transactions."+counter+".capturePrincipalName", internalTransaction.getCapturePrincipalName());
				CaptureResult captureResult = internalTransaction.getCaptureResult();
				if(captureResult!=null) {
					if(captureResult.getCommunicationResult()!=null) props.setProperty("transactions."+counter+".captureResult.communicationResult", captureResult.getCommunicationResult().name());
					if(captureResult.getProviderErrorCode()!=null) props.setProperty("transactions."+counter+".captureResult.providerErrorCode", captureResult.getProviderErrorCode());
					if(captureResult.getErrorCode()!=null) props.setProperty("transactions."+counter+".captureResult.errorCode", captureResult.getErrorCode().name());
					if(captureResult.getProviderErrorMessage()!=null) props.setProperty("transactions."+counter+".captureResult.providerErrorMessage", captureResult.getProviderErrorMessage());
					if(captureResult.getProviderUniqueId()!=null) props.setProperty("transactions."+counter+".captureResult.providerUniqueId", captureResult.getProviderUniqueId());
				}
				props.setProperty("transactions."+counter+".voidTime", Long.toString(internalTransaction.getVoidTime()));
				if(internalTransaction.getVoidPrincipalName()!=null) props.setProperty("transactions."+counter+".voidPrincipalName", internalTransaction.getVoidPrincipalName());
				VoidResult voidResult = internalTransaction.getVoidResult();
				if(voidResult!=null) {
					if(voidResult.getCommunicationResult()!=null) props.setProperty("transactions."+counter+".voidResult.communicationResult", voidResult.getCommunicationResult().name());
					if(voidResult.getProviderErrorCode()!=null) props.setProperty("transactions."+counter+".voidResult.providerErrorCode", voidResult.getProviderErrorCode());
					if(voidResult.getErrorCode()!=null) props.setProperty("transactions."+counter+".voidResult.errorCode", voidResult.getErrorCode().name());
					if(voidResult.getProviderErrorMessage()!=null) props.setProperty("transactions."+counter+".voidResult.providerErrorMessage", voidResult.getProviderErrorMessage());
					if(voidResult.getProviderUniqueId()!=null) props.setProperty("transactions."+counter+".voidResult.providerUniqueId", voidResult.getProviderUniqueId());
				}
				if(internalTransaction.getStatus()!=null) props.setProperty("transactions."+counter+".status", internalTransaction.getStatus().name());
				counter++;
			}
			// Store the a new file
			if(newFile.exists()) FileUtils.delete(newFile);
			OutputStream out = new BufferedOutputStream(new FileOutputStream(newFile));
			try {
				props.store(out, "Generated by "+PropertiesPersistenceMechanism.class.getName());
			} finally {
				out.close();
			}
			// Move the file into place
			if(backupFile.exists()) FileUtils.delete(backupFile);
			if(file.exists()) FileUtils.rename(file, backupFile);
			FileUtils.rename(newFile, file);
		} catch(IOException err) {
			throw new SQLException(err);
		}
	}

	@Override
	public synchronized String storeCreditCard(Principal principal, CreditCard creditCard) throws SQLException {
		loadIfNeeded();
		long highest = 0;
		for(CreditCard internalCreditCard : internalCreditCards) {
			try {
				long id = Long.parseLong(internalCreditCard.getPersistenceUniqueId());
				if(id>highest) highest = id;
			} catch(NumberFormatException err) {
				// This should not happen, but is not critical
				logger.log(Level.WARNING, null, err);
			}
		}
		String uniqueId = Long.toString(highest+1);
		CreditCard internalCreditCard = creditCard.clone();
		internalCreditCard.setPersistenceUniqueId(uniqueId);
		internalCreditCards.add(internalCreditCard);
		save();
		return uniqueId;
	}

	/**
	 * Card numbers and expiration dates are not persisted to the properties files - encrypted local storage not supported.
	 */
	@Override
	synchronized public void updateCreditCard(Principal principal, CreditCard creditCard) throws SQLException {
		loadIfNeeded();
		// Find the card with matching persistence id
		CreditCard internalCreditCard = getCreditCard(creditCard.getPersistenceUniqueId());
		if(internalCreditCard==null) throw new LocalizedSQLException(accessor, "PersistenceMechanism.updateCardNumber.unableToFindCard", creditCard.getPersistenceUniqueId());
		internalCreditCard.setFirstName(creditCard.getFirstName());
		internalCreditCard.setLastName(creditCard.getLastName());
		internalCreditCard.setCompanyName(creditCard.getCompanyName());
		internalCreditCard.setEmail(creditCard.getEmail());
		internalCreditCard.setPhone(creditCard.getPhone());
		internalCreditCard.setFax(creditCard.getFax());
		internalCreditCard.setCustomerId(creditCard.getCustomerId());
		internalCreditCard.setCustomerTaxId(creditCard.getCustomerTaxId());
		internalCreditCard.setStreetAddress1(creditCard.getStreetAddress1());
		internalCreditCard.setStreetAddress2(creditCard.getStreetAddress2());
		internalCreditCard.setCity(creditCard.getCity());
		internalCreditCard.setState(creditCard.getState());
		internalCreditCard.setPostalCode(creditCard.getPostalCode());
		internalCreditCard.setCountryCode(creditCard.getCountryCode());
		internalCreditCard.setComments(creditCard.getComments());
		save();
	}

	/**
	 * Card numbers and expiration dates are not persisted to the properties files - encrypted local storage not supported.
	 */
	@Override
	synchronized public void updateCardNumber(
		Principal principal,
		CreditCard creditCard,
		String cardNumber,
		byte expirationMonth,
		short expirationYear
	) throws SQLException {
		loadIfNeeded();
		// Find the card with matching persistence id
		CreditCard internalCreditCard = getCreditCard(creditCard.getPersistenceUniqueId());
		if(internalCreditCard==null) throw new LocalizedSQLException(accessor, "PersistenceMechanism.updateCardNumber.unableToFindCard", creditCard.getPersistenceUniqueId());
		internalCreditCard.setMaskedCardNumber(CreditCard.maskCreditCardNumber(cardNumber));
		save();
	}

	/**
	 * Expiration dates are not persisted to the properties files - encrypted local storage not supported.
	 */
	@Override
	public void updateExpiration(
		Principal principal,
		CreditCard creditCard,
		byte expirationMonth,
		short expirationYear
	) throws SQLException {
	}

	synchronized private CreditCard getCreditCard(String persistenceUniqueId) throws SQLException {
		loadIfNeeded();
		for(CreditCard internalCreditCard : internalCreditCards) {
			if(persistenceUniqueId.equals(internalCreditCard.getPersistenceUniqueId())) return internalCreditCard.clone();
		}
		return null;
	}

	@Override
	synchronized public void deleteCreditCard(Principal principal, CreditCard creditCard) throws SQLException {
		if(creditCard.getPersistenceUniqueId()!=null) {
			loadIfNeeded();
			boolean modified=false;
			Iterator<CreditCard> I = internalCreditCards.iterator();
			while(I.hasNext()) {
				CreditCard internalCreditCard = I.next();
				if(creditCard.getPersistenceUniqueId().equals(internalCreditCard.getPersistenceUniqueId())) {
					I.remove();
					modified = true;
				}
			}
			if(modified) save();
		}
	}

	@Override
	synchronized public String insertTransaction(Principal principal, Group group, Transaction transaction) throws SQLException {
		loadIfNeeded();
		long highest = 0;
		for(Transaction internalTransaction : internalTransactions) {
			try {
				long id = Long.parseLong(internalTransaction.getPersistenceUniqueId());
				if(id>highest) highest = id;
			} catch(NumberFormatException err) {
				// This should not happen, but is not critical
				logger.log(Level.WARNING, null, err);
			}
		}
		String uniqueId = Long.toString(highest+1);
		Transaction internalTransaction = transaction.clone();
		internalTransaction.setPersistenceUniqueId(uniqueId);
		internalTransactions.add(internalTransaction);
		save();
		return uniqueId;
	}

	@Override
	public void saleCompleted(Principal principal, Transaction transaction) throws SQLException {
		updateTransaction(principal, transaction);
	}

	@Override
	public void authorizeCompleted(Principal principal, Transaction transaction) throws SQLException {
		updateTransaction(principal, transaction);
	}

	@Override
	public void voidCompleted(Principal principal, Transaction transaction) throws SQLException {
		updateTransaction(principal, transaction);
	}

	synchronized private void updateTransaction(Principal principal, Transaction transaction) throws SQLException {
		loadIfNeeded();
		// Find the transaction with the matching persistence unique ID
		for(int c=0;c<internalTransactions.size();c++) {
			Transaction internalTransaction = internalTransactions.get(c);
			if(internalTransaction.getPersistenceUniqueId().equals(transaction.getPersistenceUniqueId())) {
				internalTransactions.set(c, transaction.clone());
				save();
				return;
			}
		}
		throw new LocalizedSQLException(accessor, "PersistenceMechanism.updateTransaction.unableToFindTransaction", transaction.getPersistenceUniqueId());
	}
}
