/*
 * ao-credit-cards - Credit card processing API supporting multiple payment gateways.
 * Copyright (C) 2015  AO Industries, Inc.
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
import java.util.HashMap;
import java.util.Map;

/**
 * Provider for Stripe<br>
 * <br>
 * Configuration parameters:
 * <ol>
 *   <li>apiKey - the Stripe account secret key</li>
 * </ol>
 *
 * @author  AO Industries, Inc.
 */
public class Stripe implements MerchantServicesProvider {

	// TODO: Statement descriptor with Accounting code and transid? "NMW #3443"

	private static final String STRIPE_API_VERSION = "2015-01-11";

	// From https://stripe.com/docs/api/java#metadata
	private static final int MAX_METADATA_KEYS = 20;
	private static final int MAX_METADATA_KEY_LENGTH = 40;
	private static final int MAX_METADATA_VALUE_LENGTH = 500;

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

	private static void addMetaData(boolean update, Map<String,Object> metadata, String key, String value) {
		if(key.length() > MAX_METADATA_KEY_LENGTH) throw new IllegalArgumentException("Meta data key too long: " + key);
		if(value != null) {
			value = value.trim();
			if(!value.isEmpty()) {
				if(value.length() > MAX_METADATA_VALUE_LENGTH) throw new IllegalArgumentException("Meta data value too long: " + value);
				if(!metadata.containsKey(key) && metadata.size() >= MAX_METADATA_KEYS) throw new IllegalStateException("Too many meta data keys");
				metadata.put(key, value);
				return;
			}
		}
		if(update) metadata.put(key, null);
	}

	/** https://stripe.com/docs/api#metadata */
	private static Map<String,Object> makeMetadata(CreditCard creditCard, boolean update) {
		Map<String,Object> metadata = new HashMap<>();
		addMetaData(update, metadata, "company_name", creditCard.getCompanyName());
		addMetaData(update, metadata, "phone", creditCard.getPhone());
		addMetaData(update, metadata, "fax", creditCard.getFax());
		addMetaData(update, metadata, "customer_id", creditCard.getCustomerId());
		addMetaData(update, metadata, "customer_tax_id", creditCard.getCustomerTaxId());
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
		addParam(update, cardParams, "name", creditCard.getFirstName());
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
		short expirationYear
	) {
		Map<String,Object> cardParams = new HashMap<>();
		addParam(update, cardParams, "number", CreditCard.numbersOnly(cardNumber));
		addParam(update, cardParams, "exp_month", expirationMonth);
		addParam(update, cardParams, "exp_year", expirationYear);
		addParam(update, cardParams, "cvc", creditCard.getCardCode());
		addCardParams(creditCard, update, cardParams);
		return cardParams;
	}

	private static Map<String,Object> makeCardParams(CreditCard creditCard, boolean update) {
		return makeCardParams(
			creditCard,
			update,
			creditCard.getCardNumber(),
			creditCard.getExpirationMonth(),
			creditCard.getExpirationYear()
		);
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
			TransactionResult.ErrorCode errorCode;
			AuthorizationResult.DeclineReason declineReason;
			switch(code) {
				case "incorrect_number" :
				case "invalid_number" :
					errorCode = TransactionResult.ErrorCode.INVALID_CARD_NUMBER;
					declineReason = null;
					break;
				case "invalid_expiry_month" :
				case "invalid_expiry_year" :
					errorCode = TransactionResult.ErrorCode.INVALID_EXPIRATION_DATE;
					declineReason = null;
					break;
				case "invalid_cvc" :
					errorCode = TransactionResult.ErrorCode.INVALID_CARD_CODE;
					declineReason = null;
					break;
				case "expired_card" :
					errorCode = TransactionResult.ErrorCode.CARD_EXPIRED;
					declineReason = AuthorizationResult.DeclineReason.EXPIRED_CARD;
					break;
				case "incorrect_cvc" :
					errorCode = TransactionResult.ErrorCode.INVALID_CARD_CODE;
					declineReason = AuthorizationResult.DeclineReason.CVV2_MISMATCH;
					break;
				case "incorrect_zip" :
					errorCode = TransactionResult.ErrorCode.UNKNOWN;
					declineReason = AuthorizationResult.DeclineReason.AVS_MISMATCH;
					break;
				case "card_declined" :
					errorCode = TransactionResult.ErrorCode.UNKNOWN;
					declineReason = AuthorizationResult.DeclineReason.UNKNOWN;
					break;
				case "missing" :
					errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
					declineReason = null;
					break;
				case "processing_error" :
					errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN;
					declineReason = null;
					break;
				case "rate_limit" :
					errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN_5_MINUTES;
					declineReason = null;
					break;
				default :
					errorCode = TransactionResult.ErrorCode.UNKNOWN;
					declineReason = null;
			}
			return new ConvertedError(
				TransactionResult.CommunicationResult.SUCCESS,
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
        throw new NotImplementedException();
    }

    @Override
    public AuthorizationResult authorize(TransactionRequest transactionRequest, CreditCard creditCard) {
        throw new NotImplementedException();
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
		// Create the Card
		Map<String,Object> cardParams = makeCardParams(creditCard, false);
		// Create the Customer
		Map<String,Object> customerParams = new HashMap<>();
		addParam(false, customerParams, "card", cardParams);
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
		Map<String,Object> updateCustomerParams = new HashMap<>();
		addCustomerParams(creditCard, true, updateCustomerParams);
		// Update the default Card
		Map<String,Object> updateCardParams = new HashMap<>();
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
		short expirationYear
	) throws IOException {
		// Replace the default Card
		Map<String,Object> cardParams = makeCardParams(creditCard, true, cardNumber, expirationMonth, expirationYear);
		Map<String,Object> updateParams = new HashMap<>();
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
		Map<String,Object> updateParams = new HashMap<>();
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
			if(customer.getDeleted() != Boolean.TRUE) {
				DeletedCustomer deletedCustomer = customer.delete(options);
			}
		} catch(StripeException e) {
			ConvertedError converted = convertError(e);
			// TODO: Throw ErrorCodeException to provide more details
			throw new LocalizedIOException(e, accessor, "MerchantServicesProvider.deleteCreditCard.notSuccessful");
		}
    }
}
