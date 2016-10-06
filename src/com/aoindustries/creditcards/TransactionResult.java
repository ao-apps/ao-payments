/*
 * ao-credit-cards - Credit card processing API supporting multiple payment gateways.
 * Copyright (C) 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2016  AO Industries, Inc.
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

/**
 * Encapsulates the results of any type of transaction with the merchant services provider.
 *
 * @author  AO Industries, Inc.
 */
abstract public class TransactionResult {

	public enum CommunicationResult {
		LOCAL_ERROR,
		IO_ERROR,
		GATEWAY_ERROR,
		SUCCESS;

		@Override
		public String toString() {
			return accessor.getMessage("TransactionResult.CommunicationResult."+name());
		}
	}

	public enum ErrorCode {
		// ALL
		UNKNOWN,
		// LOCAL_ERROR
		HASH_CHECK_FAILED,
		// IO_ERROR
		// GATEWAY_ERROR
		INVALID_TRANSACTION_TYPE,
		VOICE_AUTHORIZATION_REQUIRED,
		INSUFFICIENT_PERMISSIONS,
		INVALID_AMOUNT,
		INVALID_CARD_NUMBER,
		INVALID_EXPIRATION_DATE,
		CARD_EXPIRED,
		DUPLICATE,
		APPROVAL_CODE_REQUIRED,
		INVALID_MERCHANT_ID,
		INVALID_PARTNER,
		INVALID_PROVIDER_UNIQUE_ID,
		TRANSACTION_NOT_FOUND,
		CARD_TYPE_NOT_SUPPORTED,
		ERROR_TRY_AGAIN,
		ERROR_TRY_AGAIN_5_MINUTES,
		PROVIDER_CONFIGURATION_ERROR,
		APPROVED_BUT_SETTLEMENT_FAILED,
		INVALID_CURRENCY_CODE,
		MUST_BE_ENCRYPTED,
		NO_SESSION,
		CAPTURE_AMOUNT_LESS_THAN_AUTHORIZATION,
		CAPTURE_AMOUNT_GREATER_THAN_AUTHORIZATION,
		AMOUNT_TOO_HIGH,
		TRANSACTION_NOT_SETTLED,
		SUM_OF_CREDITS_TOO_HIGH,
		AUTHORIZED_NOTIFICATION_FAILED,
		CREDIT_CRITERIA_NOT_MET,
		ACH_ONLY,
		GATEWAY_SECURITY_GUIDELINES_NOT_MET,
		INVALID_APPROVAL_CODE,
		INVALID_DUTY_AMOUNT,
		INVALID_SHIPPING_AMOUNT,
		INVALID_TAX_AMOUNT,
		INVALID_CUSTOMER_TAX_ID,
		INVALID_CARD_CODE,
		CUSTOMER_ACCOUNT_DISABLED,
		INVALID_INVOICE_NUMBER,
		INVALID_ORDER_NUMBER,
		INVALID_CARD_NAME,
		INVALID_CARD_ADDRESS,
		INVALID_CARD_CITY,
		INVALID_CARD_STATE,
		INVALID_CARD_POSTAL_CODE,
		INVALID_CARD_COUNTRY_CODE,
		INVALID_CARD_PHONE,
		INVALID_CARD_FAX,
		INVALID_CARD_EMAIL,
		INVALID_SHIPPING_NAME,
		INVALID_SHIPPING_ADDRESS,
		INVALID_SHIPPING_CITY,
		INVALID_SHIPPING_STATE,
		INVALID_SHIPPING_POSTAL_CODE,
		INVALID_SHIPPING_COUNTRY_CODE,
		CURRENCY_NOT_SUPPORTED;

		@Override
		public String toString() {
			return accessor.getMessage("TransactionResult.ErrorCode."+name());
		}
	}

	private final String providerId;
	private final CommunicationResult communicationResult;
	private final String providerErrorCode;
	private final ErrorCode errorCode;
	private final String providerErrorMessage;
	private final String providerUniqueId;

	protected TransactionResult(
		String providerId,
		CommunicationResult communicationResult,
		String providerErrorCode,
		ErrorCode errorCode,
		String providerErrorMessage,
		String providerUniqueId
	) {
		this.providerId = providerId;
		this.communicationResult = communicationResult;
		this.providerErrorCode = providerErrorCode;
		this.errorCode = errorCode;
		this.providerErrorMessage = providerErrorMessage;
		this.providerUniqueId = providerUniqueId;
	}

	/**
	 * Gets the id of the provider that processed this transaction.
	 */
	public String getProviderId() {
		return providerId;
	}

	/**
	 * Gets the communication result.  This should be the first thing checked after any
	 * attempt to access the <code>MerchantServicesProvider</code>.
	 */
	public CommunicationResult getCommunicationResult() {
		return communicationResult;
	}

	/**
	 * Gets the provider-specific error code.
	 *
	 * @see  #getErrorCode()
	 */
	public String getProviderErrorCode() {
		return providerErrorCode;
	}

	/**
	 * Gets the provider-neutral error code.
	 */
	public ErrorCode getErrorCode() {
		return errorCode;
	}

	/**
	 * Gets the provider-specific error message.
	 */
	public String getProviderErrorMessage() {
		return providerErrorMessage;
	}

	/**
	 * Gets the provider-specific unique identifier.
	 */
	public String getProviderUniqueId() {
		return providerUniqueId;
	}
}
