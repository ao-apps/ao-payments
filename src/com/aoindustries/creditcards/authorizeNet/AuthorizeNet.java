/*
 * Copyright 2010 by AO Industries, Inc.,
 * 7262 Bull Pen Cir, Mobile, Alabama, 36695, U.S.A.
 * All rights reserved.
 */
package com.aoindustries.creditcards.authorizeNet;

import com.aoindustries.creditcards.AuthorizationResult;
import com.aoindustries.creditcards.CaptureResult;
import com.aoindustries.creditcards.CreditCard;
import com.aoindustries.creditcards.CreditResult;
import com.aoindustries.creditcards.ErrorCodeException;
import com.aoindustries.creditcards.MerchantServicesProvider;
import com.aoindustries.creditcards.SaleResult;
import com.aoindustries.creditcards.Transaction;
import com.aoindustries.creditcards.TransactionRequest;
import com.aoindustries.creditcards.TransactionResult;
import com.aoindustries.creditcards.VoidResult;
import com.aoindustries.util.StringUtility;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Logger;

/**
 * Provider for the Authorize.Net AIM system<br>
 * <br>
 * Configuration parameters:
 * <ol>
 *   <li>x_login - The merchant's unique API Login ID</li>
 *   <li>x_tran_key - The merchant's unique Transaction Key</code>
 * </ol>
 *
 * @author  AO Industries, Inc.
 */
public class AuthorizeNet implements MerchantServicesProvider {

    private static final boolean DEBUG_REQUEST = false;
    private static final boolean DEBUG_RESPONSE = false;

    private static final char X_DELIM_CHAR = '|';
    private static final char X_ENCAP_CHAR = '`';

    private static final Logger logger = Logger.getLogger(AuthorizeNet.class.getName());

    private static final String PRODUCTION_URL = "https://secure.authorize.net/gateway/transact.dll";
    // private static final String DEV_URL = "https://test.authorize.net/gateway/transact.dll";

    private final String providerId;
    private final String x_login;
    private final String x_tran_key;

    public AuthorizeNet(String providerId, String x_login, String x_tran_key) {
        this.providerId = providerId;
        this.x_login = x_login;
        this.x_tran_key = x_tran_key;
    }

    @Override
    public String getProviderId() {
        return providerId;
    }

    public String getX_login() {
        return x_login;
    }

    public String getX_tran_key() {
        return x_tran_key;
    }

    /**
     * Combines the two street address lines into a single String.
     */
    protected static String getStreetAddress(String line1, String line2) {
        if(line1==null) {
            if(line2==null) return "";
            return line2.trim();
        } else {
            line1=line1.trim();
            if(line2==null) return line1;
            line2=line2.trim();
            return (line1+" "+line2).trim();
        }
    }

    private static BigDecimal getAmount(TransactionRequest transactionRequest) {
        BigDecimal amount = transactionRequest.getAmount();
        BigDecimal taxAmount = transactionRequest.getTaxAmount();
        if(taxAmount!=null) amount = amount.add(taxAmount);
        BigDecimal shippingAmount = transactionRequest.getShippingAmount();
        if(shippingAmount!=null) amount = amount.add(shippingAmount);
        BigDecimal dutyAmount = transactionRequest.getDutyAmount();
        if(dutyAmount!=null) amount = amount.add(dutyAmount);
        return amount;
    }

    /**
     * Strips any delimiters from a string.
     */
    private static String stripDelimiters(String value) {
        if(value==null) return null;
        int len = value.length();
        if(len==0) return value;
        StringBuilder SB = new StringBuilder(len);
        for(int i=0; i<len; i++) {
            char ch = value.charAt(i);
            if(ch!=X_DELIM_CHAR && ch!=X_ENCAP_CHAR) SB.append(ch);
        }
        return SB.length()==len ? value : SB.toString();
    }

    /**
     * Adds a parameter if the value is not null or empty.  The value is trimmed.
     */
    private static void addField(StringBuilder query, String name, String value) throws UnsupportedEncodingException {
        if(value!=null && (value = stripDelimiters(value).trim()).length()>0) {
            if(query.length()>0) query.append('&');
            else query.append(PRODUCTION_URL).append('?');
            query.append(URLEncoder.encode(name, "UTF-8")).append('=').append(URLEncoder.encode(value, "UTF-8"));
        }
    }

    private static void addField(StringBuilder query, String name, int value) throws UnsupportedEncodingException {
        addField(query, name, Integer.toString(value));
    }

    private static void addField(StringBuilder query, String name, BigDecimal value) throws UnsupportedEncodingException {
        if(value!=null) addField(query, name, value.toPlainString());
    }

    private AuthorizationResult authorizeOrSale(TransactionRequest transactionRequest, CreditCard creditCard, String x_type) {
        // Build the transaction request objects, catching ErrorCodeException after this step
        // because any of these errors will all be considered as TransactionResult.CommunicationResult.LOCAL_ERROR
        String query;
        try {
            // Must be in US dollars
            if(!transactionRequest.getCurrency().getCurrencyCode().equals("USD")) throw new ErrorCodeException(TransactionResult.ErrorCode.INVALID_CURRENCY_CODE, "TransactionResult.ErrorCode.INVALID_CURRENCY_CODE");

            StringBuilder querySB = new StringBuilder();
            // Merchant Information
            addField(querySB, "x_login", x_login);
            addField(querySB, "x_tran_key", x_tran_key);
            addField(querySB, "x_allow_partial_Auth", "False");
            // API Information
            addField(querySB, "x_version", "3.1");
            addField(querySB, "x_relay_response", "FALSE");
            addField(querySB, "x_delim_data", "TRUE");
            querySB.append('&').append(URLEncoder.encode("x_delim_char", "UTF-8")).append('=').append(URLEncoder.encode(Character.toString(X_DELIM_CHAR), "UTF-8"));
            querySB.append('&').append(URLEncoder.encode("x_encap_char", "UTF-8")).append('=').append(URLEncoder.encode(Character.toString(X_ENCAP_CHAR), "UTF-8"));
            // Transaction Information
            addField(querySB, "x_type", x_type);
            addField(querySB, "x_method", "CC");
            addField(querySB, "x_amount", getAmount(transactionRequest));
            addField(querySB, "x_card_num", CreditCard.numbersOnly(creditCard.getCardNumber()));
            addField(querySB, "x_exp_date", creditCard.getExpirationDateMMYY());
            addField(querySB, "x_card_code", creditCard.getCardCode());
            if(transactionRequest.getTestMode()) addField(querySB, "x_test_request", "TRUE");
            addField(querySB, "x_duplicate_window", transactionRequest.getDuplicateWindow());
            addField(querySB, "x_invoice_num", transactionRequest.getInvoiceNumber());
            addField(querySB, "x_description", transactionRequest.getDescription());
            // Customer Information
            addField(querySB, "x_first_name", creditCard.getFirstName());
            addField(querySB, "x_last_name", creditCard.getLastName());
            addField(querySB, "x_company", creditCard.getCompanyName());
            addField(querySB, "x_address", getStreetAddress(creditCard.getStreetAddress1(), creditCard.getStreetAddress2()));
            addField(querySB, "x_city", creditCard.getCity());
            addField(querySB, "x_state", creditCard.getState());
            addField(querySB, "x_zip", CreditCard.numbersOnly(creditCard.getPostalCode()));
            addField(querySB, "x_country", creditCard.getCountryCode());
            addField(querySB, "x_phone", creditCard.getPhone());
            addField(querySB, "x_fax", creditCard.getFax());
            addField(querySB, "x_email", creditCard.getEmail());
            addField(querySB, "x_email_customer", transactionRequest.getEmailCustomer() ? "TRUE" : "FALSE");
            addField(querySB, "x_merchant_email", transactionRequest.getMerchantEmail());
            addField(querySB, "x_cust_id", creditCard.getCustomerId());
            addField(querySB, "x_customer_ip", transactionRequest.getCustomerIp());
            // Shipping Information
            addField(querySB, "x_ship_to_first_name", transactionRequest.getShippingFirstName());
            addField(querySB, "x_ship_to_last_name", transactionRequest.getShippingLastName());
            addField(querySB, "x_ship_to_company", transactionRequest.getShippingCompanyName());
            addField(querySB, "x_ship_to_address", getStreetAddress(transactionRequest.getShippingStreetAddress1(), transactionRequest.getShippingStreetAddress2()));
            addField(querySB, "x_ship_to_city", transactionRequest.getShippingCity());
            addField(querySB, "x_ship_to_state", transactionRequest.getShippingState());
            addField(querySB, "x_ship_to_zip", CreditCard.numbersOnly(transactionRequest.getShippingPostalCode()));
            addField(querySB, "x_ship_to_country", transactionRequest.getShippingCountryCode());
            // Additional Shipping Information
            addField(querySB, "x_tax", transactionRequest.getTaxAmount());
            addField(querySB, "x_freight", transactionRequest.getShippingAmount());
            addField(querySB, "x_duty", transactionRequest.getDutyAmount());
            addField(querySB, "x_tax_exempt", transactionRequest.getTaxExempt() ? "TRUE" : "FALSE");
            addField(querySB, "x_customer_tax_id", creditCard.getCustomerTaxId());
            addField(querySB, "x_po_num", transactionRequest.getPurchaseOrderNumber());
            // Merchant-defined fields
            addField(querySB, "order_number", transactionRequest.getOrderNumber());
            addField(querySB, "card_comments", creditCard.getComments());
            query = querySB.toString();
        } catch(ErrorCodeException err) {
            return new AuthorizationResult(
                getProviderId(),
                TransactionResult.CommunicationResult.LOCAL_ERROR,
                err.getErrorCode().name(),
                err.getErrorCode(),
                err.getMessage(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            );
        } catch(Exception err) {
            return new AuthorizationResult(
                getProviderId(),
                TransactionResult.CommunicationResult.LOCAL_ERROR,
                TransactionResult.ErrorCode.UNKNOWN.name(),
                TransactionResult.ErrorCode.UNKNOWN,
                err.getMessage(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            );
        }

        // Now try to process, considering as a GATEWAY_ERROR for any ErrorCodeExceptions.
        String[] response;
        try {
            // Perform query
            if(DEBUG_REQUEST) logger.info("Query: "+query);
            String responseString;
            URL url = new URL(query);
            HttpURLConnection uc = (HttpURLConnection)url.openConnection();
            try {
                uc.setUseCaches(false);
                uc.setRequestMethod("GET");
                uc.setDoOutput(false);
                uc.setDoInput(true);

                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                InputStream in = uc.getInputStream();
                try {
                    byte[] buff = new byte[4096];
                    int ret;
                    while((ret=in.read(buff, 0, 4096))!=-1) bout.write(buff, 0, ret);
                } finally {
                    in.close();
                }
                responseString = bout.toString();
            } finally {
                uc.disconnect();
            }

            // Parse response
            if(DEBUG_RESPONSE) logger.info("Response: "+responseString);

            response = StringUtility.splitString(responseString, X_DELIM_CHAR);
            if(response.length<68) throw new Exception("Not enough fields in response");
            for(int i=0; i<response.length; i++) {
                // Must start and end with encap_char
                String value = response[i];
                if(
                    value.length()<2
                    || !value.startsWith(Character.toString(X_ENCAP_CHAR))
                    || !value.endsWith(Character.toString(X_ENCAP_CHAR))
                ) throw new Exception("Response value not encapsulated");
                response[i] = value.substring(1, value.length()-1);
            }
        } catch(Exception err) {
            return new AuthorizationResult(
                getProviderId(),
                TransactionResult.CommunicationResult.GATEWAY_ERROR,
                TransactionResult.ErrorCode.ERROR_TRY_AGAIN.name(),
                TransactionResult.ErrorCode.ERROR_TRY_AGAIN,
                err.getMessage(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            );
        }

        // Get the values from the response
        // Note: The docs are 1-based and arrays are 0-based, so these are all off by one
        final String
            responseCode = response[0],
            responseReasonCode = response[2],
            responseReasonText = response[3],
            authorizationCode = response[4],
            avsResponse = response[5],
            transactionId = response[6],
            cardCodeResponse = response[38]
        ;

        // Convert to CvvResult
        AuthorizationResult.CvvResult cvvResult;
        if("M".equals(cardCodeResponse)) cvvResult = AuthorizationResult.CvvResult.MATCH;
        else if("N".equals(cardCodeResponse)) cvvResult = AuthorizationResult.CvvResult.NO_MATCH;
        else if("P".equals(cardCodeResponse)) cvvResult = AuthorizationResult.CvvResult.NOT_PROCESSED;
        else if("S".equals(cardCodeResponse)) cvvResult = AuthorizationResult.CvvResult.CVV2_NOT_PROVIDED_BY_MERCHANT;
        else if("U".equals(cardCodeResponse)) cvvResult = AuthorizationResult.CvvResult.NOT_SUPPORTED_BY_ISSUER;
        else {
            String cvv2 = creditCard.getCardCode();
            if(cvv2!=null && cvv2.length()>0) cvvResult = AuthorizationResult.CvvResult.NOT_PROCESSED;
            else cvvResult = AuthorizationResult.CvvResult.CVV2_NOT_PROVIDED_BY_MERCHANT;
        }

        // Convert to AvsResult
        AuthorizationResult.AvsResult avsResult;
        if("A".equals(avsResponse)) avsResult = AuthorizationResult.AvsResult.ADDRESS_Y_ZIP_N;
        else if("B".equals(avsResponse)) avsResult = AuthorizationResult.AvsResult.ADDRESS_NOT_PROVIDED;
        else if("E".equals(avsResponse)) avsResult = AuthorizationResult.AvsResult.ERROR;
        else if("G".equals(avsResponse)) avsResult = AuthorizationResult.AvsResult.NON_US_CARD;
        else if("N".equals(avsResponse)) avsResult = AuthorizationResult.AvsResult.ADDRESS_N_ZIP_N;
        else if("P".equals(avsResponse)) avsResult = AuthorizationResult.AvsResult.NOT_APPLICABLE;
        else if("R".equals(avsResponse)) avsResult = AuthorizationResult.AvsResult.RETRY;
        else if("S".equals(avsResponse)) avsResult = AuthorizationResult.AvsResult.SERVICE_NOT_SUPPORTED;
        else if("U".equals(avsResponse)) avsResult = AuthorizationResult.AvsResult.UNAVAILABLE;
        else if("W".equals(avsResponse)) avsResult = AuthorizationResult.AvsResult.ADDRESS_N_ZIP_9;
        else if("X".equals(avsResponse)) avsResult = AuthorizationResult.AvsResult.ADDRESS_Y_ZIP_9;
        else if("Y".equals(avsResponse)) avsResult = AuthorizationResult.AvsResult.ADDRESS_Y_ZIP_5;
        else if("Z".equals(avsResponse)) avsResult = AuthorizationResult.AvsResult.ADDRESS_N_ZIP_5;
        else avsResult = AuthorizationResult.AvsResult.UNKNOWN;

        if(responseCode.equals("1")) {
            // Approved
            return new AuthorizationResult(
                getProviderId(),
                TransactionResult.CommunicationResult.SUCCESS,
                null,
                null,
                null,
                transactionId,
                responseReasonCode,
                AuthorizationResult.ApprovalResult.APPROVED,
                null,
                null,
                null,
                null,
                cardCodeResponse,
                cvvResult,
                avsResponse,
                avsResult,
                authorizationCode
            );
        } else if(
            responseCode.equals("2")
            // The following codes will be treated as an error
            && !"28".equals(responseReasonCode)
            && !"30".equals(responseReasonCode)
            && !"31".equals(responseReasonCode)
            && !"34".equals(responseReasonCode)
            && !"35".equals(responseReasonCode)
            && !"37".equals(responseReasonCode)
            && !"38".equals(responseReasonCode)
            && !"171".equals(responseReasonCode)
            && !"172".equals(responseReasonCode)
            && !"174".equals(responseReasonCode)
            && !"200".equals(responseReasonCode)
            && !"201".equals(responseReasonCode)
            && !"202".equals(responseReasonCode)
            && !"203".equals(responseReasonCode)
            && !"204".equals(responseReasonCode)
            && !"205".equals(responseReasonCode)
            && !"206".equals(responseReasonCode)
            && !"207".equals(responseReasonCode)
            && !"208".equals(responseReasonCode)
            && !"209".equals(responseReasonCode)
            && !"210".equals(responseReasonCode)
            && !"211".equals(responseReasonCode)
            && !"212".equals(responseReasonCode)
            && !"213".equals(responseReasonCode)
            && !"214".equals(responseReasonCode)
            && !"215".equals(responseReasonCode)
            && !"216".equals(responseReasonCode)
            && !"217".equals(responseReasonCode)
            && !"218".equals(responseReasonCode)
            && !"219".equals(responseReasonCode)
            && !"220".equals(responseReasonCode)
            && !"221".equals(responseReasonCode)
            && !"222".equals(responseReasonCode)
            && !"223".equals(responseReasonCode)
            && !"224".equals(responseReasonCode)
            && !"315".equals(responseReasonCode)
            && !"316".equals(responseReasonCode)
            && !"317".equals(responseReasonCode)
            && !"318".equals(responseReasonCode)
            && !"319".equals(responseReasonCode)
        ) {
            // Declined
            AuthorizationResult.DeclineReason declineReason;
            if("2".equals(responseReasonCode)) declineReason = AuthorizationResult.DeclineReason.NO_SPECIFIC;
            else if("3".equals(responseReasonCode)) declineReason = AuthorizationResult.DeclineReason.NO_SPECIFIC;
            else if("4".equals(responseReasonCode)) declineReason = AuthorizationResult.DeclineReason.PICK_UP_CARD;
            else if("27".equals(responseReasonCode)) declineReason = AuthorizationResult.DeclineReason.AVS_MISMATCH;
            else if("29".equals(responseReasonCode)) declineReason = AuthorizationResult.DeclineReason.UNKNOWN;
            else if("41".equals(responseReasonCode)) declineReason = AuthorizationResult.DeclineReason.FRAUD_DETECTED;
            else if("44".equals(responseReasonCode)) declineReason = AuthorizationResult.DeclineReason.CVV2_MISMATCH;
            else if("45".equals(responseReasonCode)) declineReason = AuthorizationResult.DeclineReason.NO_SPECIFIC;
            else if("65".equals(responseReasonCode)) declineReason = AuthorizationResult.DeclineReason.CVV2_MISMATCH;
            else if("127".equals(responseReasonCode)) declineReason = AuthorizationResult.DeclineReason.AVS_MISMATCH;
            else if("141".equals(responseReasonCode)) declineReason = AuthorizationResult.DeclineReason.FRAUD_DETECTED;
            else if("145".equals(responseReasonCode)) declineReason = AuthorizationResult.DeclineReason.NO_SPECIFIC;
            else if("165".equals(responseReasonCode)) declineReason = AuthorizationResult.DeclineReason.CVV2_MISMATCH;
            else if("250".equals(responseReasonCode)) declineReason = AuthorizationResult.DeclineReason.BLOCKED_IP;
            else if("251".equals(responseReasonCode)) declineReason = AuthorizationResult.DeclineReason.FRAUD_DETECTED;
            else if("254".equals(responseReasonCode)) declineReason = AuthorizationResult.DeclineReason.MANUAL_REVIEW;
            else declineReason = AuthorizationResult.DeclineReason.UNKNOWN;

            return new AuthorizationResult(
                getProviderId(),
                TransactionResult.CommunicationResult.SUCCESS,
                null,
                null,
                null,
                transactionId,
                responseReasonCode,
                AuthorizationResult.ApprovalResult.DECLINED,
                responseReasonText,
                declineReason,
                null,
                null,
                cardCodeResponse,
                cvvResult,
                avsResponse,
                avsResult,
                authorizationCode
            );
        } else if(responseCode.equals("4")) {
            // Hold
            AuthorizationResult.ReviewReason reviewReason;
            if("193".equals(responseReasonCode)) reviewReason = AuthorizationResult.ReviewReason.RISK_MANAGEMENT;
            else if("252".equals(responseReasonCode)) reviewReason = AuthorizationResult.ReviewReason.ACCEPTED_MERCHANT_REVIEW;
            else if("253".equals(responseReasonCode)) reviewReason = AuthorizationResult.ReviewReason.ACCEPTED_AUTHORIZED_MERCHANT_REVIEW;
            else if("295".equals(responseReasonCode)) reviewReason = AuthorizationResult.ReviewReason.ACCEPTED_AUTHORIZED_MERCHANT_REVIEW;
            else reviewReason = AuthorizationResult.ReviewReason.RISK_MANAGEMENT;
            return new AuthorizationResult(
                getProviderId(),
                TransactionResult.CommunicationResult.SUCCESS,
                null,
                null,
                null,
                transactionId,
                responseReasonCode,
                AuthorizationResult.ApprovalResult.HOLD,
                null,
                null,
                responseReasonText,
                reviewReason,
                cardCodeResponse,
                cvvResult,
                avsResponse,
                avsResult,
                authorizationCode
            );
        } else {
            // Other results, assume error
            TransactionResult.CommunicationResult communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
            TransactionResult.ErrorCode errorCode;
            // The following are the codes are from decline mapped to error (code 2)
            if("28".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.CARD_TYPE_NOT_SUPPORTED;
            else if("30".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            else if("31".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INVALID_MERCHANT_ID;
            else if("34".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            else if("35".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            else if("37".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INVALID_CARD_NUMBER;
            else if("38".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            else if("171".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            else if("172".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            else if("174".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INVALID_TRANSACTION_TYPE;
            else if("200".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INVALID_CARD_NUMBER;
            else if("201".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INVALID_EXPIRATION_DATE;
            else if("202".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INVALID_TRANSACTION_TYPE;
            else if("203".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INVALID_AMOUNT;
            else if("204".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            else if("205".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INVALID_MERCHANT_ID;
            else if("206".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INVALID_MERCHANT_ID;
            else if("207".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INSUFFICIENT_PERMISSIONS;
            else if("208".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INVALID_MERCHANT_ID;
            else if("209".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN;
            else if("210".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            else if("211".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INVALID_CARD_NUMBER;
            else if("212".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            else if("213".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            else if("214".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("215".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            else if("216".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            else if("217".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            else if("218".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            else if("219".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            else if("220".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN_5_MINUTES;
            else if("221".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            else if("222".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.DUPLICATE;
            else if("223".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("224".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN;
            else if("315".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INVALID_CARD_NUMBER;
            else if("316".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INVALID_EXPIRATION_DATE;
            else if("317".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.CARD_EXPIRED;
            else if("318".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.DUPLICATE;
            else if("319".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.TRANSACTION_NOT_FOUND;
            // The following are errors (code 3)
            else if("5".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INVALID_AMOUNT;
            else if("6".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INVALID_CARD_NUMBER;
            else if("7".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INVALID_EXPIRATION_DATE;
            else if("8".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.CARD_EXPIRED;
            else if("9".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("10".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("11".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.DUPLICATE;
            else if("12".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.APPROVAL_CODE_REQUIRED;
            else if("13".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INSUFFICIENT_PERMISSIONS;
            else if("14".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            else if("15".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INVALID_PROVIDER_UNIQUE_ID;
            else if("16".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.TRANSACTION_NOT_FOUND;
            else if("17".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.CARD_TYPE_NOT_SUPPORTED;
            else if("18".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            else if("19".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN_5_MINUTES;
            else if("20".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN_5_MINUTES;
            else if("21".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN_5_MINUTES;
            else if("22".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN_5_MINUTES;
            else if("23".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN_5_MINUTES;
            else if("24".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            else if("25".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN_5_MINUTES;
            else if("26".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN_5_MINUTES;
            else if("32".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("33".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN; // Could parse for separate fields if needed
            else if("36".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.APPROVED_BUT_SETTLEMENT_FAILED;
            else if("40".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.MUST_BE_ENCRYPTED;
            else if("43".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            else if("46".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.NO_SESSION;
            else if("47".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.CAPTURE_AMOUNT_GREATER_THAN_AUTHORIZATION;
            else if("48".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INVALID_AMOUNT;
            else if("49".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.AMOUNT_TOO_HIGH;
            else if("50".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.TRANSACTION_NOT_SETTLED;
            else if("51".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.SUM_OF_CREDITS_TOO_HIGH;
            else if("52".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.AUTHORIZED_NOTIFICATION_FAILED;
            else if("53".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INVALID_TRANSACTION_TYPE;
            else if("54".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.CREDIT_CRITERIA_NOT_MET;
            else if("55".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.SUM_OF_CREDITS_TOO_HIGH;
            else if("56".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.ACH_ONLY;
            else if("57".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN_5_MINUTES;
            else if("58".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN_5_MINUTES;
            else if("59".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN_5_MINUTES;
            else if("60".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN_5_MINUTES;
            else if("61".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN_5_MINUTES;
            else if("62".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN_5_MINUTES;
            else if("63".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN_5_MINUTES;
            else if("66".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.GATEWAY_SECURITY_GUIDELINES_NOT_MET;
            else if("68".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            else if("69".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INVALID_TRANSACTION_TYPE;
            else if("70".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            else if("71".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            else if("72".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INVALID_APPROVAL_CODE;
            else if("73".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("74".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INVALID_DUTY_AMOUNT;
            else if("75".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INVALID_SHIPPING_AMOUNT;
            else if("76".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INVALID_TAX_AMOUNT;
            else if("77".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INVALID_CUSTOMER_TAX_ID;
            else if("78".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INVALID_CARD_CODE;
            else if("79".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("80".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("81".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            else if("82".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            else if("83".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            else if("84".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("85".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("86".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("87".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("88".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("89".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("90".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("91".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            else if("92".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            else if("97".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("98".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("99".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("100".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("101".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("102".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.GATEWAY_SECURITY_GUIDELINES_NOT_MET;
            else if("103".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("104".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("105".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("106".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("107".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("108".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("109".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("110".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("116".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("117".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("118".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("119".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("120".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN_5_MINUTES;
            else if("121".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN_5_MINUTES;
            else if("122".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN;
            else if("123".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INSUFFICIENT_PERMISSIONS;
            else if("128".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.CUSTOMER_ACCOUNT_DISABLED;
            else if("130".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INSUFFICIENT_PERMISSIONS;
            else if("131".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INSUFFICIENT_PERMISSIONS;
            else if("132".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INSUFFICIENT_PERMISSIONS;
            else if("152".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.AUTHORIZED_NOTIFICATION_FAILED;
            else if("170".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("173".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("175".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("180".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN;
            else if("181".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN;
            else if("185".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("243".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("244".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("245".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("246".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("247".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("248".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("261".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN;
            else if("270".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("271".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("288".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INSUFFICIENT_PERMISSIONS;
            else if("289".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INVALID_AMOUNT;
            else if("290".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INVALID_CARD_ADDRESS;
            else if("296".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("297".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("300".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("301".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("302".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("303".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("304".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("305".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("306".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.UNKNOWN;
            else if("309".equals(responseReasonCode)) errorCode = TransactionResult.ErrorCode.INSUFFICIENT_PERMISSIONS;
            else errorCode = TransactionResult.ErrorCode.UNKNOWN;

            return new AuthorizationResult(
                getProviderId(),
                communicationResult,
                responseReasonCode,
                errorCode,
                responseReasonText,
                transactionId,
                null,
                null,
                null,
                null,
                null,
                null,
                cardCodeResponse,
                cvvResult,
                avsResponse,
                avsResult,
                authorizationCode
            );
        }
    }

    @Override
    public SaleResult sale(TransactionRequest transactionRequest, CreditCard creditCard) {
        AuthorizationResult authorizationResult = authorizeOrSale(transactionRequest, creditCard, "AUTH_CAPTURE");
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
        return authorizeOrSale(transactionRequest, creditCard, "AUTH_ONLY");
    }

    @Override
    public CaptureResult capture(AuthorizationResult authorizationResult) {
        throw new RuntimeException("TODO: Implement method");
    }

    @Override
    public VoidResult voidTransaction(Transaction transaction) {
        throw new RuntimeException("TODO: Implement method");
    }

    @Override
    public CreditResult credit(TransactionRequest transactionRequest, CreditCard creditCard) {
        throw new RuntimeException("TODO: Implement method");
    }

    @Override
    public boolean canStoreCreditCards() {
        return false;
    }

    @Override
    public String storeCreditCard(CreditCard creditCard) throws IOException {
        throw new RuntimeException("TODO: Credit card storage not yet implemented");
    }

    @Override
    public void updateCreditCardNumberAndExpiration(CreditCard creditCard, String cardNumber, byte expirationMonth, short expirationYear) throws IOException {
        throw new RuntimeException("TODO: Credit card storage not yet implemented");
    }

    @Override
    public void updateCreditCardExpiration(CreditCard creditCard, byte expirationMonth, short expirationYear) throws IOException {
        throw new RuntimeException("TODO: Credit card storage not yet implemented");
    }

    @Override
    public void deleteCreditCard(CreditCard creditCard) throws IOException {
        throw new RuntimeException("TODO: Credit card storage not yet implemented");
    }
}
