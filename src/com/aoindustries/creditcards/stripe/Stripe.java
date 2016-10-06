/*
 * ao-credit-cards - Credit card processing API supporting multiple payment gateways.
 * Copyright (C) 2015, 2016  AO Industries, Inc.
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
package com.aoindustries.creditcards.stripe;

import static com.aoindustries.creditcards.ApplicationResourcesAccessor.accessor;
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
import com.aoindustries.io.LocalizedIOException;
import com.aoindustries.lang.NotImplementedException;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.Card;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCardCollection;
import com.stripe.model.DeletedCustomer;
import com.stripe.net.RequestOptions;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Currency;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Provider for Stripe<br>
 * <br>
 * Configuration parameters:
 * <ol>
 *   <li>apiKey - the Stripe account secret key</li>
 * </ol>
 *
 * TODO: Support testMode with optional testApiKey
 * TODO: Support Stripe.js
 *
 * @author  AO Industries, Inc.
 */
public class Stripe implements MerchantServicesProvider {

	private static final String STRIPE_API_VERSION = "2015-01-11";

	// From https://stripe.com/docs/api/java#metadata
	private static final int MAX_METADATA_KEYS = 20;
	private static final int MAX_METADATA_KEY_LENGTH = 40;
	private static final int MAX_METADATA_VALUE_LENGTH = 500;

	/**
	 * The maximum allowed statement descriptor.
	 * From https://stripe.com/docs/api#create_charge
	 */
	private static final int MAX_STATEMENT_DESCRIPTOR_LEN = 22;

	/**
	 * The characters on the statement before the order number.
	 */
	private static final String STATEMENT_DESCRIPTOR_PREFIX = "AO#";

	private final String providerId;
	private final String apiKey;

	private final RequestOptions options;

	public Stripe(String providerId, String apiKey) {
		this.providerId = providerId;
		this.apiKey = apiKey;
		this.options = RequestOptions
			.builder()
			.setApiKey(apiKey)
			.setStripeVersion(STRIPE_API_VERSION)
			.build();
	}

	@Override
	public String getProviderId() {
		return providerId;
	}

	/**
	 * Gets the API secret key.
	 */
	public String getApiKey() {
		return apiKey;
	}

	// <editor-fold defaultstate="collapsed" desc="API parameters">
	private static void addParam(boolean update, Map<String,Object> params, String name, String value) {
		if(value != null) {
			value = value.trim();
			if(!value.isEmpty()) {
				params.put(name, value);
				return;
			}
		}
		if(update) params.put(name, null);
	}

	private static void addParam(boolean update, Map<String,Object> params, String name, Object value) {
		if(value != null) {
			params.put(name, value);
			return;
		}
		if(update) params.put(name, null);
	}

	private static void addParam(boolean update, Map<String,Object> params, String name, Map<String,Object> map) {
		if(map != null && !map.isEmpty()) {
			params.put(name, map);
			return;
		}
		if(update) params.put(name, null);
	}

	private static void addMetaData(boolean update, Map<String,Object> metadata, String key, String value, boolean allowTrimValue) {
		if(key.length() > MAX_METADATA_KEY_LENGTH) throw new IllegalArgumentException("Meta data key too long: " + key);
		if(value != null) {
			value = value.trim();
			if(!value.isEmpty()) {
				if(value.length() > MAX_METADATA_VALUE_LENGTH) {
					if(allowTrimValue) value = value.substring(0, MAX_METADATA_VALUE_LENGTH);
					else throw new IllegalArgumentException("Meta data value too long: " + value);
				}
				if(!metadata.containsKey(key) && metadata.size() >= MAX_METADATA_KEYS) throw new IllegalStateException("Too many meta data keys");
				metadata.put(key, value);
				return;
			}
		}
		if(update) metadata.put(key, null);
	}

	private static void addMetaData(boolean update, Map<String,Object> metadata, String key, Object value, boolean allowTrimValue) {
		addMetaData(
			update,
			metadata,
			key,
			value==null ? (String)value : value.toString(),
			allowTrimValue
		);
	}

	/** https://stripe.com/docs/api#metadata */
	private static Map<String,Object> makeMetadata(CreditCard creditCard, boolean update) {
		Map<String,Object> metadata = new LinkedHashMap<String,Object>();
		addMetaData(update, metadata, "company_name", creditCard.getCompanyName(), true);
		addMetaData(update, metadata, "phone", creditCard.getPhone(), true);
		addMetaData(update, metadata, "fax", creditCard.getFax(), true);
		addMetaData(update, metadata, "customer_id", creditCard.getCustomerId(), true);
		addMetaData(update, metadata, "customer_tax_id", creditCard.getCustomerTaxId(), true);
		return metadata;
	}

	/**
	 * Meta data contains both card meta data (also associated with "customer" for stored cards) and transaction meta data.
	 * https://stripe.com/docs/api#create_charge
	 */
	private static Map<String,Object> makeMetadata(TransactionRequest transactionRequest, CreditCard creditCard, boolean update) {
		Map<String,Object> metadata = makeMetadata(creditCard, update);
		// Additional customer meta data
		addMetaData(update, metadata, "customer_description", creditCard.getComments(), true);
		addMetaData(update, metadata, "customer_email", creditCard.getEmail(), false);
		// Transaction meta data
		addMetaData(update, metadata, "customer_ip", transactionRequest.getCustomerIp(), false);
		addMetaData(update, metadata, "order_number", transactionRequest.getOrderNumber(), false);
		addMetaData(update, metadata, "amount", transactionRequest.getAmount(), false);
		addMetaData(update, metadata, "tax_amount", transactionRequest.getTaxAmount(), false);
		addMetaData(update, metadata, "tax_exempt", transactionRequest.getTaxExempt(), false);
		addMetaData(update, metadata, "shipping_amount", transactionRequest.getShippingAmount(), false);
		addMetaData(update, metadata, "duty_amount", transactionRequest.getDutyAmount(), false);
		addMetaData(update, metadata, "shipping_company_name", transactionRequest.getShippingCompanyName(), true);
		addMetaData(update, metadata, "invoice_number", transactionRequest.getInvoiceNumber(), false);
		addMetaData(update, metadata, "purchase_order_number", transactionRequest.getPurchaseOrderNumber(), false);
		return metadata;
	}

	/** https://stripe.com/docs/api#update_customer */
	private static void addCustomerParams(
		CreditCard creditCard,
		boolean update,
		Map<String,Object> customerParams
	) {
		addParam(update, customerParams, "description", creditCard.getComments());
		addParam(update, customerParams, "email", creditCard.getEmail());
		addParam(update, customerParams, "metadata", makeMetadata(creditCard, update));
	}

	/** https://stripe.com/docs/api#update_card */
	private static void addCardParams(
		CreditCard creditCard,
		boolean update,
		Map<String,Object> cardParams
	) {
		addParam(update, cardParams, "name", CreditCard.getFullName(creditCard.getFirstName(), creditCard.getLastName()));
		addParam(update, cardParams, "address_line1", creditCard.getStreetAddress1());
		addParam(update, cardParams, "address_line2", creditCard.getStreetAddress2());
		addParam(update, cardParams, "address_city", creditCard.getCity());
		addParam(update, cardParams, "address_zip", creditCard.getPostalCode());
		addParam(update, cardParams, "address_state", creditCard.getState());
		addParam(update, cardParams, "address_country", creditCard.getCountryCode());
	}

	private static Map<String,Object> makeCardParams(
		CreditCard creditCard,
		boolean update,
		String cardNumber,
		byte expirationMonth,
		short expirationYear,
		String cardCode
	) {
		Map<String,Object> cardParams = new HashMap<String,Object>();
		addParam(update, cardParams, "number", CreditCard.numbersOnly(cardNumber));
		addParam(update, cardParams, "exp_month", expirationMonth);
		addParam(update, cardParams, "exp_year", expirationYear);
		addParam(update, cardParams, "cvc", cardCode);
		addCardParams(creditCard, update, cardParams);
		return cardParams;
	}

	private static Map<String,Object> makeCardParams(CreditCard creditCard, boolean update) {
		return makeCardParams(
			creditCard,
			update,
			creditCard.getCardNumber(),
			creditCard.getExpirationMonth(),
			creditCard.getExpirationYear(),
			creditCard.getCardCode()
		);
	}

	/** https://stripe.com/docs/api#create_charge */
	private static Map<String,Object> makeShippingAddressParams(TransactionRequest transactionRequest, boolean update) {
		Map<String,Object> shippingAddressParams = new HashMap<String,Object>();
		addParam(update, shippingAddressParams, "line1", transactionRequest.getShippingStreetAddress1());
		addParam(update, shippingAddressParams, "line2", transactionRequest.getShippingStreetAddress2());
		addParam(update, shippingAddressParams, "city", transactionRequest.getShippingCity());
		addParam(update, shippingAddressParams, "state", transactionRequest.getShippingState());
		addParam(update, shippingAddressParams, "postal_code", transactionRequest.getShippingPostalCode());
		addParam(update, shippingAddressParams, "country", transactionRequest.getShippingCountryCode());
		return shippingAddressParams;
	}

	/** https://stripe.com/docs/api#create_charge */
	private static Map<String,Object> makeShippingParams(TransactionRequest transactionRequest, CreditCard creditCard, boolean update) {
		Map<String,Object> shippingParams = new HashMap<String,Object>();
		addParam(update, shippingParams, "address", makeShippingAddressParams(transactionRequest, update));
		addParam(update, shippingParams, "name", CreditCard.getFullName(transactionRequest.getShippingFirstName(), transactionRequest.getShippingLastName()));
		// Phone cannot be in the shipping by itself
		if(!shippingParams.isEmpty()) addParam(update, shippingParams, "phone", creditCard.getPhone());
		// Unused: tracking_number
		return shippingParams;
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Error code conversion">
	private static class ConvertedError {

		private final TransactionResult.CommunicationResult communicationResult;
		private final String providerErrorCode;
		private final TransactionResult.ErrorCode errorCode;
		private final String providerErrorMessage;
		private final AuthorizationResult.DeclineReason declineReason;

		private ConvertedError(
			TransactionResult.CommunicationResult communicationResult,
			String providerErrorCode,
			TransactionResult.ErrorCode errorCode,
			String providerErrorMessage,
			AuthorizationResult.DeclineReason declineReason
		) {
			this.communicationResult = communicationResult;
			this.providerErrorCode = providerErrorCode;
			this.errorCode = errorCode;
			this.providerErrorMessage = providerErrorMessage;
			this.declineReason = declineReason;
		}
	}

	/**
	 * Gets an exception message.
	 */
	private static String getMessage(Throwable t) {
		String message = t.getMessage();
		if(message==null || message.trim().isEmpty()) message = t.toString();
		return message;
	}

	private static ConvertedError convertError(StripeException e) {
		if(e instanceof AuthenticationException) {
			return new ConvertedError(
				TransactionResult.CommunicationResult.GATEWAY_ERROR,
				null,
				TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR,
				getMessage(e),
				null
			);
		}
		if(e instanceof InvalidRequestException) {
			InvalidRequestException ire = (InvalidRequestException)e;
			String param = ire.getParam();
			return new ConvertedError(
				TransactionResult.CommunicationResult.GATEWAY_ERROR,
				param,
				TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR,
				getMessage(e),
				null
			);
		}
		if(e instanceof APIConnectionException) {
			return new ConvertedError(
				TransactionResult.CommunicationResult.IO_ERROR,
				null,
				TransactionResult.ErrorCode.ERROR_TRY_AGAIN,
				getMessage(e),
				null
			);
		}
		if(e instanceof CardException) {
			CardException ce = (CardException)e;
			String code = ce.getCode();
			String param = ce.getParam();
			// Convert to ErrorCode, see https://stripe.com/docs/api/java#errors
			final TransactionResult.CommunicationResult communicationResult;
			final TransactionResult.ErrorCode errorCode;
			final AuthorizationResult.DeclineReason declineReason;
			if(
				"incorrect_number".equals(code)
				|| "invalid_number".equals(code)
			) {
				communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
				errorCode = TransactionResult.ErrorCode.INVALID_CARD_NUMBER;
				declineReason = null;
			} else if(
				"invalid_expiry_month".equals(code)
				|| "invalid_expiry_year".equals(code)
			) {
				communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
				errorCode = TransactionResult.ErrorCode.INVALID_EXPIRATION_DATE;
				declineReason = null;
			} else if("invalid_cvc".equals(code)) {
				communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
				errorCode = TransactionResult.ErrorCode.INVALID_CARD_CODE;
				declineReason = null;
			} else if("expired_card".equals(code)) {
				communicationResult = TransactionResult.CommunicationResult.SUCCESS;
				errorCode = TransactionResult.ErrorCode.CARD_EXPIRED;
				declineReason = AuthorizationResult.DeclineReason.EXPIRED_CARD;
			} else if("incorrect_cvc".equals(code)) {
				communicationResult = TransactionResult.CommunicationResult.SUCCESS;
				errorCode = TransactionResult.ErrorCode.INVALID_CARD_CODE;
				declineReason = AuthorizationResult.DeclineReason.CVV2_MISMATCH;
			} else if("incorrect_zip".equals(code)) {
				communicationResult = TransactionResult.CommunicationResult.SUCCESS;
				errorCode = TransactionResult.ErrorCode.UNKNOWN;
				declineReason = AuthorizationResult.DeclineReason.AVS_MISMATCH;
			} else if("card_declined".equals(code)) {
				communicationResult = TransactionResult.CommunicationResult.SUCCESS;
				errorCode = TransactionResult.ErrorCode.UNKNOWN;
				declineReason = AuthorizationResult.DeclineReason.UNKNOWN;
			} else if("missing".equals(code)) {
				communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
				errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
				declineReason = null;
			} else if("processing_error".equals(code)) {
				communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
				errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN;
				declineReason = null;
			} else if("rate_limit".equals(code)) {
				communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
				errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN_5_MINUTES;
				declineReason = null;
			} else {
				communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
				errorCode = TransactionResult.ErrorCode.UNKNOWN;
				declineReason = null;
			}
			if(communicationResult == TransactionResult.CommunicationResult.SUCCESS) {
				if(declineReason == null) throw new AssertionError("declineReason required when communicationResult is SUCCESS");
			} else if(communicationResult != TransactionResult.CommunicationResult.SUCCESS) {
				if(declineReason != null) throw new AssertionError("declineReason not allowed when communicationResult is not SUCCESS");
			}
			return new ConvertedError(
				communicationResult,
				code,
				errorCode,
				getMessage(e),
				declineReason
			);
		}
		if(e instanceof APIException) {
			return new ConvertedError(
				TransactionResult.CommunicationResult.IO_ERROR,
				null,
				TransactionResult.ErrorCode.ERROR_TRY_AGAIN,
				getMessage(e),
				null
			);
		}
		// Note: This will not happen unless a new subclass of StripeException is introduced.
		return new ConvertedError(
			TransactionResult.CommunicationResult.GATEWAY_ERROR,
			null,
			TransactionResult.ErrorCode.UNKNOWN,
			getMessage(e),
			null
		);
	}
	// </editor-fold>

	@Override
	public SaleResult sale(TransactionRequest transactionRequest, CreditCard creditCard) {
		AuthorizationResult authorizationResult = saleOrAuthorize(transactionRequest, creditCard, true);
		return new SaleResult(
			authorizationResult,
			new CaptureResult(
				authorizationResult.getProviderId(),
				authorizationResult.getCommunicationResult(),
				authorizationResult.getProviderErrorCode(),
				authorizationResult.getErrorCode(),
				authorizationResult.getProviderErrorMessage(),
				authorizationResult.getProviderUniqueId()
			)
		);
	}

	@Override
	public AuthorizationResult authorize(TransactionRequest transactionRequest, CreditCard creditCard) {
		return saleOrAuthorize(transactionRequest, creditCard, false);
	}

	private AuthorizationResult saleOrAuthorize(TransactionRequest transactionRequest, CreditCard creditCard, boolean capture) {
		// Test mode not currently supported
		if(transactionRequest.getTestMode()) {
			throw new UnsupportedOperationException("Test mode not currently supported");
		}
		// Convert amount into smallest unit
		BigDecimal totalAmount = transactionRequest.getTotalAmount();
		Currency currency = transactionRequest.getCurrency();
		int currencyDigits = currency.getDefaultFractionDigits();
		if(currencyDigits < 0) throw new AssertionError("currencyDigits < 0: " + currencyDigits);
		BigInteger amount = totalAmount.scaleByPowerOfTen(currencyDigits).toBigIntegerExact();
		// Create the Charge
		// https://stripe.com/docs/api#create_charge
		Map<String,Object> chargeParams = new HashMap<String,Object>();
		addParam(false, chargeParams, "amount", amount);
		addParam(false, chargeParams, "currency", currency.getCurrencyCode());
		if(creditCard.getProviderUniqueId() != null) {
			// Is a stored card
			addParam(false, chargeParams, "customer", creditCard.getProviderUniqueId());
		} else {
			// Is a new card
			addParam(false, chargeParams, "card", makeCardParams(creditCard, false));
		}
		addParam(false, chargeParams, "description", transactionRequest.getDescription());
		addParam(false, chargeParams, "metadata", makeMetadata(transactionRequest, creditCard, false));
		addParam(false, chargeParams, "capture", capture);
		if(transactionRequest.getOrderNumber() != null) {
			String combined = STATEMENT_DESCRIPTOR_PREFIX + transactionRequest.getOrderNumber();
			if(combined.length() <= MAX_STATEMENT_DESCRIPTOR_LEN) addParam(false, chargeParams, "statement_descriptor", combined);
		}
		if(transactionRequest.getEmailCustomer()) {
			addParam(false, chargeParams, "receipt_email", creditCard.getEmail());
		}
		// Unused: application_fee
		addParam(false, chargeParams, "shipping", makeShippingParams(transactionRequest, creditCard, false));
		try {
			Charge charge = Charge.create(chargeParams, options);
			Card card = charge.getCard();
			// AVS
			final String providerAvsResult;
			final AuthorizationResult.AvsResult avsResult;
			// <editor-fold defaultstate="collapsed" desc="AVS conversion">
			{
				String addressResult = card.getAddressLine1Check();
				String zipResult = card.getAddressZipCheck();
				if(addressResult != null) {
					if(zipResult != null) {
						// Both address and ZIP
						providerAvsResult = addressResult + "," + zipResult;
						// ADDRESS_Y_ZIP_5
						if(addressResult.equals("pass") && zipResult.equals("pass")) {
							avsResult = AuthorizationResult.AvsResult.ADDRESS_Y_ZIP_5;
						}
						// ADDRESS_Y_ZIP_N
						else if(addressResult.equals("pass")) {
							avsResult = AuthorizationResult.AvsResult.ADDRESS_Y_ZIP_N;
						}
						// ADDRESS_N_ZIP_5
						else if(zipResult.equals("pass")) {
							avsResult = AuthorizationResult.AvsResult.ADDRESS_N_ZIP_5;
						}
						// ADDRESS_N_ZIP_N
						else if(addressResult.equals("fail") && zipResult.equals("fail")) {
							avsResult = AuthorizationResult.AvsResult.ADDRESS_N_ZIP_N;
						}
						// UNAVAILABLE
						else if(addressResult.equals("unavailable") && zipResult.equals("unavailable")) {
							avsResult = AuthorizationResult.AvsResult.UNAVAILABLE;
						}
						// SERVICE_NOT_SUPPORTED
						else if(addressResult.equals("unchecked") && zipResult.equals("unchecked")) {
							avsResult = AuthorizationResult.AvsResult.UNAVAILABLE;
						} else {
							avsResult = AuthorizationResult.AvsResult.UNKNOWN;
						}
					} else {
						// Address only
						providerAvsResult = addressResult + ",";
						if("pass".equals(addressResult)) {
							avsResult = AuthorizationResult.AvsResult.ADDRESS_Y_ZIP_N;
						} else if("fail".equals(addressResult)) {
							avsResult = AuthorizationResult.AvsResult.ADDRESS_N_ZIP_N;
						} else if("unavailable".equals(addressResult)) {
							avsResult = AuthorizationResult.AvsResult.UNAVAILABLE;
						} else if("unchecked".equals(addressResult)) {
							avsResult = AuthorizationResult.AvsResult.SERVICE_NOT_SUPPORTED;
						} else {
							avsResult = AuthorizationResult.AvsResult.UNKNOWN;
						}
					}
				} else {
					if(zipResult != null) {
						// ZIP only
						providerAvsResult = "," + zipResult;
						if("pass".equals(zipResult)) {
							avsResult = AuthorizationResult.AvsResult.ADDRESS_N_ZIP_5;
						} else if("fail".equals(zipResult)) {
							avsResult = AuthorizationResult.AvsResult.ADDRESS_N_ZIP_N;
						} else if("unavailable".equals(zipResult)) {
							avsResult = AuthorizationResult.AvsResult.UNAVAILABLE;
						} else if("unchecked".equals(zipResult)) {
							avsResult = AuthorizationResult.AvsResult.SERVICE_NOT_SUPPORTED;
						} else {
							avsResult = AuthorizationResult.AvsResult.UNKNOWN;
						}
					} else {
						providerAvsResult = ",";
						avsResult = AuthorizationResult.AvsResult.ADDRESS_NOT_PROVIDED;
					}
				}
			}
			// </editor-fold>
			// CVC
			final String providerCvvResult = card.getCvcCheck();
			final AuthorizationResult.CvvResult cvvResult;
			// <editor-fold defaultstate="collapsed" desc="CVC conversion">
			if(providerCvvResult == null) {
				cvvResult = AuthorizationResult.CvvResult.CVV2_NOT_PROVIDED_BY_MERCHANT;
			} else {
				if("pass".equals(providerCvvResult)) {
					cvvResult = AuthorizationResult.CvvResult.MATCH;
				} else if("fail".equals(providerCvvResult)) {
					cvvResult = AuthorizationResult.CvvResult.NO_MATCH;
				} else if("unavailable".equals(providerCvvResult)) {
					cvvResult = AuthorizationResult.CvvResult.NOT_PROCESSED;
				} else if("unchecked".equals(providerCvvResult)) {
					cvvResult = AuthorizationResult.CvvResult.NOT_SUPPORTED_BY_ISSUER;
				} else {
					cvvResult = AuthorizationResult.CvvResult.UNKNOWN;
				}
			}
			// </editor-fold>
			// TODO: FraudDetails fraudDetails = charge.getFraudDetails();
			// Trip "ch_" from charge ID from approval code
			String approvalCode = charge.getId();
			if(approvalCode != null && approvalCode.startsWith("ch_")) approvalCode = approvalCode.substring(3);
			return new AuthorizationResult(
				getProviderId(),
				TransactionResult.CommunicationResult.SUCCESS,
				null, // providerErrorCode
				null, // errorCode
				null, // providerErrorMessage
				charge.getId(),
				null, // providerApprovalResult
				AuthorizationResult.ApprovalResult.APPROVED,
				null, // providerDeclineReason
				null, // declineReason
				null, // providerReviewReason
				null, // reviewReason
				providerCvvResult,
				cvvResult,
				providerAvsResult,
				avsResult,
				approvalCode // approvalCode
			);
		} catch(StripeException e) {
			ConvertedError converted = convertError(e);
			if(converted.declineReason == null) {
				return new AuthorizationResult(
					getProviderId(),
					converted.communicationResult,
					converted.providerErrorCode,
					converted.errorCode,
					converted.providerErrorMessage,
					null, // providerUniqueId
					null, // providerApprovalResult
					null, // approvalResult
					null, // providerDeclineReason
					null, // declineReason
					null, // providerReviewReason
					null, // reviewReason
					null, // providerCvvResult
					null, // cvvResult
					null, // providerAvsResult
					null, // avsResult
					null  // approvalCode
				);
			} else {
				// Declined
				return new AuthorizationResult(
					getProviderId(),
					converted.communicationResult,
					null, // providerErrorCode
					null, // errorCode
					converted.providerErrorMessage,
					null, // providerUniqueId
					null, // providerApprovalResult
					AuthorizationResult.ApprovalResult.DECLINED, // approvalResult
					converted.providerErrorCode, // providerDeclineReason
					converted.declineReason,
					null, // providerReviewReason
					null, // reviewReason
					null, // providerCvvResult
					null, // cvvResult
					null, // providerAvsResult
					null, // avsResult
					null  // approvalCode
				);
			}
		}
	}

	@Override
	public CaptureResult capture(AuthorizationResult authorizationResult) {
		String id = authorizationResult.getProviderUniqueId();
		try {
			authorizationResult.getProviderUniqueId();
			Charge ch = Charge.retrieve(id, options);
			ch.capture(options);
			return new CaptureResult(
				providerId,
				TransactionResult.CommunicationResult.SUCCESS,
				null,
				null,
				null,
				id
			);
		} catch(StripeException e) {
			ConvertedError converted = convertError(e);
			return new CaptureResult(
				providerId,
				converted.communicationResult,
				converted.providerErrorCode,
				converted.errorCode,
				converted.providerErrorMessage,
				id
			);
		}
	}

	@Override
	public VoidResult voidTransaction(Transaction transaction) {
		throw new NotImplementedException();
	}

	@Override
	public CreditResult credit(TransactionRequest transactionRequest, CreditCard creditCard) {
		throw new NotImplementedException();
	}

	@Override
	public boolean canStoreCreditCards() {
		return true;
	}

	@Override
	public String storeCreditCard(CreditCard creditCard) throws IOException {
		// Create the Customer
		Map<String,Object> customerParams = new HashMap<String,Object>();
		addParam(false, customerParams, "card", makeCardParams(creditCard, false));
		addCustomerParams(creditCard, false, customerParams);
		// Make API call
		try {
			Customer customer = Customer.create(customerParams, options);
			return customer.getId();
		} catch(StripeException e) {
			ConvertedError converted = convertError(e);
			// TODO: Throw ErrorCodeException to provide more details
			throw new LocalizedIOException(e, accessor, "MerchantServicesProvider.storeCreditCard.notSuccessful");
		}
	}

	@Override
	public void updateCreditCard(CreditCard creditCard) throws IOException {
		// Update the Customer
		Map<String,Object> updateCustomerParams = new HashMap<String,Object>();
		addCustomerParams(creditCard, true, updateCustomerParams);
		// Update the default Card
		Map<String,Object> updateCardParams = new HashMap<String,Object>();
		addCardParams(creditCard, true, updateCardParams);
		try {
			Customer customer = Customer.retrieve(creditCard.getProviderUniqueId(), options);
			customer.update(updateCustomerParams, options);
			CustomerCardCollection cards = customer.getCards();
			Card defaultCard = cards.retrieve(customer.getDefaultCard(), options);
			defaultCard.update(updateCardParams, options);
		} catch(StripeException e) {
			ConvertedError converted = convertError(e);
			// TODO: Throw ErrorCodeException to provide more details
			throw new LocalizedIOException(e, accessor, "MerchantServicesProvider.updateCreditCardNumberAndExpiration.notSuccessful");
		}

	}

	@Override
	public void updateCreditCardNumberAndExpiration(
		CreditCard creditCard,
		String cardNumber,
		byte expirationMonth,
		short expirationYear,
		String cardCode
	) throws IOException {
		// Replace the default Card
		Map<String,Object> cardParams = makeCardParams(
			creditCard,
			true,
			cardNumber,
			expirationMonth,
			expirationYear,
			cardCode!=null ? CreditCard.numbersOnly(cardCode) : creditCard.getCardCode()
		);
		Map<String,Object> updateParams = new HashMap<String,Object>();
		addParam(true, updateParams, "card", cardParams);
		try {
			Customer customer = Customer.retrieve(creditCard.getProviderUniqueId(), options);
			customer.update(updateParams, options);
		} catch(StripeException e) {
			ConvertedError converted = convertError(e);
			// TODO: Throw ErrorCodeException to provide more details
			throw new LocalizedIOException(e, accessor, "MerchantServicesProvider.updateCreditCardNumberAndExpiration.notSuccessful");
		}
	}

	@Override
	public void updateCreditCardExpiration(
		CreditCard creditCard,
		byte expirationMonth,
		short expirationYear
	) throws IOException {
		// Update the default Card
		Map<String,Object> updateParams = new HashMap<String,Object>();
		addParam(true, updateParams, "exp_month", expirationMonth);
		addParam(true, updateParams, "exp_year", expirationYear);
		try {
			Customer customer = Customer.retrieve(creditCard.getProviderUniqueId(), options);
			CustomerCardCollection cards = customer.getCards();
			Card defaultCard = cards.retrieve(customer.getDefaultCard(), options);
			defaultCard.update(updateParams, options);
		} catch(StripeException e) {
			ConvertedError converted = convertError(e);
			// TODO: Throw ErrorCodeException to provide more details
			throw new LocalizedIOException(e, accessor, "MerchantServicesProvider.updateCreditCardExpiration.notSuccessful");
		}
	}

	@Override
	public void deleteCreditCard(CreditCard creditCard) throws IOException {
		try {
			Customer customer = Customer.retrieve(creditCard.getProviderUniqueId(), options);
			if(customer.getDeleted() == null || !customer.getDeleted()) {
				DeletedCustomer deletedCustomer = customer.delete(options);
			}
		} catch(StripeException e) {
			ConvertedError converted = convertError(e);
			// TODO: Throw ErrorCodeException to provide more details
			throw new LocalizedIOException(e, accessor, "MerchantServicesProvider.deleteCreditCard.notSuccessful");
		}
	}
}
