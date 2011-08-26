package com.aoindustries.creditcards.usaepay;

/*
 * Copyright 2008-2011 by AO Industries, Inc.,
 * 7262 Bull Pen Cir, Mobile, Alabama, 36695, U.S.A.
 * All rights reserved.
 */
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
import com.aoindustries.io.IoUtils;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * Provider for the USAePay system<br>
 * <br>
 * Configuration parameters:
 * <ol>
 *   <li>providerId - the unique name of the processor</li>
 *   <li>postUrl - the URL used for HTTP POST</li>
 *   <li>key - the source key</li>
 *   <li>pin (optional) - the pin used for MD5 hash transaction verification</li>
 * </ol>
 *
 * TODO: Support the other methods as needed.
 * TODO: Get this code listed at http://wiki.usaepay.com/developer/3rdparty
 *
 * @author  AO Industries, Inc.
 */
public class USAePay implements MerchantServicesProvider {

    private static final boolean DEBUG_REQUEST = false;
    private static final boolean DEBUG_RESPONSE = false;

    private static final String SOFTWARE_VERSION = USAePay.class.getName()+" 1.0";

    private final String providerId;
    private final String postUrl;
    private final String key;
    private final String pin;

    public USAePay(String providerId, String postUrl, String key) {
        this.providerId = providerId;
        this.postUrl = postUrl;
        this.key = key;
        this.pin = null;
    }

    public USAePay(String providerId, String postUrl, String key, String pin) {
        this.providerId = providerId;
        this.postUrl = postUrl;
        this.key = key;
        this.pin = pin;
    }

    @Override
    public String getProviderId() {
        return providerId;
    }

    /**
     * Gets the post URL
     */
    public String getPostUrl() {
        return postUrl;
    }

    /**
     * Gets the source key.
     */
    public String getKey() {
        return key;
    }
 
    /**
     * Gets the PIN used for MD5 request and response validation.
     * http://wiki.usaepay.com/developer/transactionapi#source_pin_code
     */
    public String getPin() {
        return pin;
    }

    private static String encode(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, "UTF-8");
        //return value.replace('&', '-').replace('=', '-');
    }

    private static String decode(String value) throws UnsupportedEncodingException {
        return URLDecoder.decode(value, "UTF-8");
        //return value;
    }

    /**
     * Submits a transaction (a Map of name/value pairs), and returns the result as a Map of name/value pairs.
     */
    protected Map<String,String> submitTransaction(Map<String,String> request, boolean testMode) throws ErrorCodeException {
        try {
            // Build the request string before allocating the connection to the server
            if(DEBUG_REQUEST) System.out.println("Request:");
            StringBuilder parameterSB = new StringBuilder();
            for(Map.Entry<String,String> entry : request.entrySet()) {
                String name = entry.getKey();
                String value = entry.getValue();
                if(value==null) value="";
                if(DEBUG_REQUEST) System.out.println("    "+name+"="+value);
                if(parameterSB.length()>0) parameterSB.append('&');
                parameterSB.append(name).append('=').append(encode(value));
            }

            String responseString;
            URL url = new URL(postUrl);
            HttpURLConnection uc = (HttpURLConnection)url.openConnection();
            try {
                uc.setUseCaches(false);

                uc.setRequestMethod("POST");
                uc.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                uc.setDoOutput(true);
                uc.setDoInput(true);

                DataOutputStream out = new DataOutputStream(uc.getOutputStream());
                try {
                    out.writeBytes(parameterSB.toString());
                    out.flush();
                } finally {
                    out.close();
                }

                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                InputStream in = uc.getInputStream();
                try {
                    IoUtils.copy(in, bout);
                } finally {
                    in.close();
                }
                responseString = bout.toString();
            } finally {
                uc.disconnect();
            }
            
            // Split on = and & and decode name/value pairs
            if(DEBUG_RESPONSE) System.out.println("Response:");
            Map<String,String> response = new HashMap<String,String>();
            StringTokenizer st = new StringTokenizer(responseString, "&");
            while(st.hasMoreTokens()) {
                String token = st.nextToken();
                int equalPos = token.indexOf('=');
                if(equalPos==-1) throw new IOException("No equal sign (=) found in token: "+token);
                String name = token.substring(0, equalPos);
                String value = decode(token.substring(equalPos+1));
                if(DEBUG_RESPONSE) System.out.println("    "+name+"="+value);
                response.put(name, value);
            }
            return response;
        } catch(IOException err) {
            throw new ErrorCodeException(
                err,
                TransactionResult.ErrorCode.ERROR_TRY_AGAIN,
                "TransactionResult.CommunicationResult.IO_ERROR"
            );
        }
    }

    /**
     * Adds a parameter to the request after checking its length.
     * If longer, throws an ErrorCodeException with the provided <code>TransactionResult.ErrorCode</code>, otherwise appends the value.
     */
    protected static void addMaxLengthParameter(Map<String,String> request, String name, String value, int maxLength, TransactionResult.ErrorCode errorCode) throws ErrorCodeException {
        if(value.length()>maxLength) {
            throw new ErrorCodeException(
                errorCode,
                "TransactionRequest.field.tooLong",
                name,
                maxLength
            );
        }
        request.put(name, value);
    }
    
    /**
     * Adds a parameter to the request.  If the value is too long, the first <code>maxLength</code> characters are used and the rest are discarded.
     */
    protected static void addTrimmedParameter(Map<String,String> request, String name, String value, int maxLength) {
        if(value!=null && value.length()>maxLength) value=value.substring(0, maxLength);
        request.put(name, value);
    }

    /**
     * Combines the first and last names into a single name String.
     */
    protected static String getFullName(String firstName, String lastName) {
        if(firstName==null) {
            if(lastName==null) return "";
            return lastName.trim();
        } else {
            firstName=firstName.trim();
            if(lastName==null) return firstName;
            lastName=lastName.trim();
            return (firstName+" "+lastName).trim();
        }
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

    volatile private static Random random;
    /**
     * Gets a secure random instance.  Not synchronized because multiple initialization is acceptable.
     */
    private static Random getRandom() throws NoSuchAlgorithmException {
        if(random==null) {
            random=SecureRandom.getInstance("SHA1PRNG");
        }
        return random;
    }

    /**
     * Gets a complete hexadecimal string where any leading zeros are not removed.
     * Thus, the length of the string will be twice the length of the byte[] parameter.
     */
    private static String getFullHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length*2);
        for(int c=0;c<bytes.length;c++) {
            byte nextByte = bytes[c];
            sb.append(Integer.toHexString((nextByte>>>4) & 0xf));
            sb.append(Integer.toHexString(nextByte & 0xf));
        }
        return sb.toString();
    }

    private static String createMd5Hash(String token) throws NoSuchAlgorithmException {
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(token.getBytes(), 0, token.length());
        return getFullHexString(m.digest());
    }

    private static class ConvertedError {
        
        private final TransactionResult.CommunicationResult communicationResult;
        private final TransactionResult.ErrorCode errorCode;
        private final AuthorizationResult.DeclineReason declineReason;

        private ConvertedError(
            TransactionResult.CommunicationResult communicationResult,
            TransactionResult.ErrorCode errorCode
        ) {
            this.communicationResult = communicationResult;
            this.errorCode = errorCode;
            this.declineReason = null;
        }

        private ConvertedError(
            TransactionResult.CommunicationResult communicationResult,
            TransactionResult.ErrorCode errorCode,
            AuthorizationResult.DeclineReason declineReason
        ) {
            this.communicationResult = communicationResult;
            this.errorCode = errorCode;
            this.declineReason = declineReason;
        }
    }

    private static Map<String,ConvertedError> convertedErrors = new HashMap<String,ConvertedError>();
    /*
     * http://wiki.usaepay.com/developer/errorcodes?s[]=10118
     */
    static {
        // 00001 	Password/Username Incorrect. 	Sent by login screen when the username and/or the password are incorrect.
        convertedErrors.put("00001", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INSUFFICIENT_PERMISSIONS));
        // 00002 	Access to page denied. 	The user has attempted to access a page they don't have permission to access.
        convertedErrors.put("00002", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INSUFFICIENT_PERMISSIONS));
        // 00003 	Transaction type [type] not supported. 	Please contact support. Is returned by /console/vterm.php when an unknown transaction type (sale, credit, etc) is attempted.
        convertedErrors.put("00003", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_TRANSACTION_TYPE));
        // 00004 	Processing gateway currently offline. 	Please try back in a few moments. Return by processing engine when the gateway cannot establish a connection with the processing backend.
        convertedErrors.put("00004", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 00005 	Error in verification module [module]. 	Please contact support. The given fraud module was did not load correctly. An upgrade may be in progress.
        convertedErrors.put("00005", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.UNKNOWN));
        // 00006 	Merchant not found. 	The system was not able to locate the requested merchant.
        convertedErrors.put("00006", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR));
        // 00007 	Merchant has been deactivated. 	Merchant account has been marked as deactivate. Contact USAePay customer service.
        convertedErrors.put("00007", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.CUSTOMER_ACCOUNT_DISABLED));
        // 00008 	Unable to retrieve current batch. 	Failed to get the id of the current batch. Typically this indicates that the merchant account is not active or batches are out of sync. Verify all merchant account info provided to usaepay.
        convertedErrors.put("00008", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.UNKNOWN));
        // 00009 	Unable To Create Transaction. 	Please Contact Support. Internal database error, system may be in the process of failing over to backup database server. Retry transaction.
        convertedErrors.put("00009", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 00010 	Unable To Allocate Transaction Slot. 	Please contact support. Internal database error, system may be in the process of failing over to backup database server. Retry transaction.
        convertedErrors.put("00010", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 00011 	Invalid Card Number (1) 	The cardnumber contains illegal characters. A card number may only include numbers.
        convertedErrors.put("00011", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_CARD_NUMBER));
        // 00012 	Invalid Card Number (2) 	Card Number was not between 13 and 16 digits.
        convertedErrors.put("00012", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_CARD_NUMBER));
        // 00013 	Invalid Card Number (3) 	Cardnumber failed Luhn Mod-10 Checkdigit Method (ISO 2894/ANSI 4.13)
        convertedErrors.put("00013", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_CARD_NUMBER));
        // 00014 	Invalid Credit Card Number (1) 	Cardnumber passed length, format and checkdigit tests but didn't match any of the cardnumber profiles enabled in the system. Contact USAePay to verify support of cardtype.
        convertedErrors.put("00014", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.CARD_TYPE_NOT_SUPPORTED));
        // 00015 	Invalid expiration date. 	Must be in MMYY format. Expiration contains invalid characters (nothing but numbers allowed)
        convertedErrors.put("00015", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_EXPIRATION_DATE));
        // 00016 	Invalid expiration date. 	Must be in MMYY format. Could not guess format of date. It wasn't MMYY or MMYYYY or MMDDYYYY or even MMDDYY format.
        convertedErrors.put("00016", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_EXPIRATION_DATE));
        // 00017 	Credit card has expired. 	The credit card expiration date has passed.
        convertedErrors.put("00017", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.CARD_EXPIRED));
        // 00018 	Gateway temporarily offline. 	Please try again shortly. Unable to contact processor backend. Failed bank link maybe in the process of coming back up. Retry transaction.
        convertedErrors.put("00018", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 00019 	Gateway temporarily offline for maintenance. 	Please try again in a few minutes. Processor backend is offline for maintenance. Retry transaction.
        convertedErrors.put("00019", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN_5_MINUTES));
        // 00020 	User not configured correctly, please contact support. 	User not configured correctly. Remove the user and readd.
        convertedErrors.put("00020", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR));
        // 00021 	Invalid username. 	The merchant didn't type in a valid username when adding a new user.
        convertedErrors.put("00021", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR));
        // 00022 	You do not have access to this page. 	The user tried to access a page they don't have permission to access.
        convertedErrors.put("00022", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INSUFFICIENT_PERMISSIONS));
        // 00023 	Specified source key not found. 	The source key provided did not match any of the currently active keys.
        convertedErrors.put("00023", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR));
        // 00024 	Transaction already voided. 	The transaction was already marked as voided and wasn't going to be settled anyway.
        convertedErrors.put("00024", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.UNKNOWN));
        // 00025 	Unable to find transaction in batch. 	The batchid on the transaction references a batch that doesn't exist. If there isn't a valid batch then trying to void a transaction isn't going to do much.
        convertedErrors.put("00025", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.UNKNOWN));
        // 00026 	The batch has already been closed. Please apply a credit instead. 	The specified transaction has already been settled. Once a transaction has been sent in for settlement it can not be voided.
        convertedErrors.put("00026", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.UNKNOWN));
        // 00027 	Gateway temporarily offline. Please try again shortly. (2) 	Error communicating with the processing backend. Retry transaction.
        convertedErrors.put("00027", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 00028 	Unable to verify source. 	VerifySource couldn't find the source or the source was disabled.
        convertedErrors.put("00028", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR));
        // 00029 	Unable to generate security key. 	VerifySource wasn't able to create a source on the fly. Trouble finding a key.
        convertedErrors.put("00029", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.GATEWAY_SECURITY_GUIDELINES_NOT_MET));
        // 00030 	Source has been blocked from processing transactions. 	Merchant has disabled the specified source key.
        convertedErrors.put("00030", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.CUSTOMER_ACCOUNT_DISABLED));
        // 00031 	Duplicate transaction, wait at least [minutes] minutes before trying again. 	The duplicate transaction fraud module detected a dupe.
        convertedErrors.put("00031", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.DUPLICATE));
        // 00032 	The maximum order amount is $[amount]. 	Fraud module response.
        convertedErrors.put("00032", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_AMOUNT, AuthorizationResult.DeclineReason.MAX_SALE_EXCEEDED));
        // 00033 	The minimum order amount is $[amount]. 	Fraud module response.
        convertedErrors.put("00033", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_AMOUNT, AuthorizationResult.DeclineReason.MIN_SALE_NOT_MET));
        // 00034 	Your billing information does not match your credit card. Please check with your bank. 	AVS Response fraud module blocked this transaction.
        convertedErrors.put("00034", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.UNKNOWN, AuthorizationResult.DeclineReason.AVS_MISMATCH));
        // 00035 	Unable to locate transaction. 	Was not able to find the requested transaction for voiding.
        convertedErrors.put("00035", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.TRANSACTION_NOT_FOUND));
        // 00036 	Gateway temporarily offline for maintenance. 	Please try again in a few minutes. VeriCheck link has been brought down for maintenance. Retry transaction.
        convertedErrors.put("00036", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN_5_MINUTES));
        // 00037 	Customer Name not submitted. 	Cardholder field was blank.
        convertedErrors.put("00037", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_CARD_NAME));
        // 00038 	Invalid Routing Number. 	Check Routing number did not meet requirement of 9 digits.
        convertedErrors.put("00038", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.UNKNOWN));
        // 00039 	Invalid Checking Account Number. 	Check Account number is not at least 4 digits long.
        convertedErrors.put("00039", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.UNKNOWN));
        // 00040 	Merchant does not currently support check transactions. 	The merchant doesn't have a valid tax id or password entered for check processing.
        convertedErrors.put("00040", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_TRANSACTION_TYPE));
        // 00041 	Check processing temporarily offline. Please try again shortly. 	Internal system error encountered while communicating with check processor. Please contact USAePay support.
        convertedErrors.put("00041", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 00042 	Temporarily unable to process transaction. Please try again shortly. 	A corrupted response (unparsable) was received from vericheck.
        convertedErrors.put("00042", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 00043 	Transaction Requires Voice Authentication. Please Call-In. 	Processor returned a referral.
        convertedErrors.put("00043", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.VOICE_AUTHORIZATION_REQUIRED));
        // 00044 	Merchant not configured properly (CardAuth) 	The merchant has payment authentication enabled but does not have a processorid/merchantid entered.
        convertedErrors.put("00044", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR));
        // 00045 	Auth service unavailable. 	Internal system error was encountered while connecting to authentication platform. Contact USAePay support.
        convertedErrors.put("00045", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 00046 	Auth service unavailable (6). 	A corrupted response was received from the authentication platform.
        convertedErrors.put("00046", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 00050 	Invalid SSN. 	Social Security number must be 9 digits.
        convertedErrors.put("00050", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_CUSTOMER_TAX_ID));
        // 00070 	Transaction exceeds maximum amount. 	Transaction exceeds the maximum allowable amount of $99,999.
        convertedErrors.put("00070", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_AMOUNT, AuthorizationResult.DeclineReason.MAX_SALE_EXCEEDED));
        // 00071 	Transaction out of balance. 	Transaction does not add up correctly: subtotal + tip + tax + shipping - discount must equal the amount.
        convertedErrors.put("00071", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_AMOUNT));
        // 00080 	Transaction type not allowed from this source. 	The requested command (sale, authonly, etc) was blocked by the merchant's source key. The command must be checked on source key settings screen to be accepted by the gateway.
        convertedErrors.put("00080", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_TRANSACTION_TYPE));
        // 02034 	Your billing address does not match your credit card. 	Please check with your bank. The AVS result received from the platform was blocked by the Merchant's fraud preferences. Funds were not held for this transaction.
        convertedErrors.put("02034", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.UNKNOWN, AuthorizationResult.DeclineReason.AVS_MISMATCH));
        // 10001 	Processing Error Please Try Again Error from FDMS Nashville. 	Invalid Transaction Code
        convertedErrors.put("10001", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.TRANSACTION_NOT_FOUND));
        // 10003 	Merchant does not accept this type of card (1) 	Error from FDMS Nashville: Terminal ID not setup for settlement on this Card Type.
        convertedErrors.put("10003", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.CARD_TYPE_NOT_SUPPORTED));
        // 10004 	Merchant does not accept this type of card (2) 	Error from FDMS Nashville: Terminal ID not setup for authorization on this Card Type.
        convertedErrors.put("10004", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.CARD_TYPE_NOT_SUPPORTED));
        // 10005 	Invalid Card Expiration Date Error from FDMS Nashville 	Terminal ID not setup for settlement on this Card Type.
        convertedErrors.put("10005", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_EXPIRATION_DATE));
        // 10006 	Merchant does not accept this type of card (3) Error from FDMS Nashville. 	Invalid Process Code, Authorization Type or Card Type.
        convertedErrors.put("10006", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.CARD_TYPE_NOT_SUPPORTED));
        // 10007 	Invalid amount Error from FDMS Nashville 	Invalid Transaction or Other Dollar Amount.
        convertedErrors.put("10007", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_AMOUNT));
        // 10008 	Processing Error Please Try Again (08) Error from FDMS Nashville. 	Invalid Entry Mode.
        convertedErrors.put("10008", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 10009 	Processing Error Please Try Again (09) Error from FDMS Nashville 	Invalid Card Present Flag.
        convertedErrors.put("10009", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 10010 	Processing Error Please Try Again (10) Error from FDMS Nashville 	Invalid Customer Present Flag.
        convertedErrors.put("10010", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 10011 	Processing Error Please Try Again (11) Error from FDMS Nashville 	Invalid Transaction Count Value.
        convertedErrors.put("10011", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 10012 	Processing Error Please Try Again (12) Error from FDMS Nashville 	Invalid Terminal Type.
        convertedErrors.put("10012", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 10013 	Processing Error Please Try Again (13) Error from FDMS Nashville 	Invalid Terminal Capability.
        convertedErrors.put("10013", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 10014 	Processing Error Please Try Again (14) Error from FDMS Nashville 	Invalid Source ID.
        convertedErrors.put("10014", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 10015 	Processing Error Please Try Again (15) Error from FDMS Nashville 	Invalid Summary ID.
        convertedErrors.put("10015", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 10016 	Processing Error Please Try Again (16) Error from FDMS Nashville 	Invalid Mag Strip Data.
        convertedErrors.put("10016", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 10017 	Invalid Invoice Number (17) Error from FDMS Nashville 	Invalid Invoice Number.
        convertedErrors.put("10017", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_INVOICE_NUMBER));
        // 10018 	Invalid Transaction Date or Time (18) Error from FDMS Nashville 	Invalid Transaction Date or Time.
        convertedErrors.put("10018", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 10019 	Processing Error Please Try Again (19) Error from FDMS Nashville 	Invalid bankcard merchant number in First Data database.
        convertedErrors.put("10019", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 10020 	Processing Error Please Try Again (20) Error from FDMS Nashville 	File Access Error in First Data database.
        convertedErrors.put("10020", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 10026 	Merchant has been deactivated (26) Error from FDMS Nashville 	Terminal flagged as Inactive in First Data database.
        convertedErrors.put("10026", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.CUSTOMER_ACCOUNT_DISABLED));
        // 10027 	Invalid Merchant Account (27) Error from FDMS Nashville. 	Invalid Merchant/Terminal ID combination, verify numbers are accurate.
        convertedErrors.put("10027", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_MERCHANT_ID));
        // 10030 	Processing Error Please Try Again (30) Error from FDMS Nashville. 	Unrecoverable database error from an authorization process (usually means the Merchant/Terminal ID was already in use).
        convertedErrors.put("10030", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 10031 	Processing Error Please Retry Transaction (31) Error from FDMS Nashville. 	Database access lock encountered, retry transaction.
        convertedErrors.put("10031", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 10033 	Processing Error Please Try Again (33) Error from FDMS Nashville. 	Database error in summary process, retry transaction.
        convertedErrors.put("10033", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 10043 	Sequence Error, Please Contact Support (43) Error from FDMS Nashville. 	Transaction ID invalid, incorrect or out of sequence.
        convertedErrors.put("10043", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 10051 	Merchant has been deactivated (51) Error from FDMS Nashville. 	Terminal flagged as not usable (violated) in First Data database, Call Customer Support.
        convertedErrors.put("10051", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.CUSTOMER_ACCOUNT_DISABLED));
        // 10054 	Merchant has not been setup correctly (54) Error from FDMS Nashville. 	Terminal ID not set up on First Data database for leased line access.
        convertedErrors.put("10054", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR));
        // 10057 	Merchant does not support this card type (57) Error from FDMS Nashville. 	Terminal is not programmed for this service, call customer support.
        convertedErrors.put("10057", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.CARD_TYPE_NOT_SUPPORTED));
        // 10059 	Processing Error Please Try Again (59) Error from FDMS Nashville. 	Settle Trans for Summary ID where earlier Summary ID still open.
        convertedErrors.put("10059", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 10060 	Invalid Account Number (60) Error from FDMS Nashville. 	Invalid account number found by authorization process.
        convertedErrors.put("10060", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_CARD_NUMBER));
        // 10061 	Processing Error Please Try Again (61) Error from FDMS Nashville. 	Invalid settlement data found in summary process (trans level).
        convertedErrors.put("10061", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 10062 	Processing Error Please Try Again (62) Error from FDMS Nashville. 	Invalid settlement data (i.e., 'future' date found, erroneous Pserve data found) (summary level).
        convertedErrors.put("10062", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 10080 	Processing Error Please Try Again (80) Error from FDMS Nashville. 	Invalid Payment Service data found in summary process (trans level).
        convertedErrors.put("10080", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 10098 	Processing Error Please Try Again (98) Error from FDMS Nashville. 	General System Error.
        convertedErrors.put("10098", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 10099 	Session timed out. Please re-login. 	Session timed out - (checkout timeout setting).
        convertedErrors.put("10099", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.NO_SESSION));
        // 10100 	Your account has been locked for excessive login attempts. 	The user failed login too many times. Their account has been locked for 60 minutes.
        convertedErrors.put("10100", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.CUSTOMER_ACCOUNT_DISABLED));
        // 10101 	Your username has been de-activated due to inactivity for 90 days. 	Please contact support to re-activate your account. VISA Cisp requires locking of accounts that have not been accessed in the past 90 days.
        convertedErrors.put("10101", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.CUSTOMER_ACCOUNT_DISABLED));
        // 10102 	Unable to open certificate. Unable to load required certificate. 	Contact Support.
        convertedErrors.put("10102", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.MUST_BE_ENCRYPTED));
        // 10103 	Unable to read certificate. Unable to load required certificate. 	Contact Support.
        convertedErrors.put("10103", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.MUST_BE_ENCRYPTED));
        // 10104 	Error reading certificate. Unable to load required certificate. 	Contact support.
        convertedErrors.put("10104", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.MUST_BE_ENCRYPTED));
        // 10105 	Unable to find original transaction. 	A capture or void operation was not able to locate the original transaction.
        convertedErrors.put("10105", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.TRANSACTION_NOT_FOUND));
        // 10106 	You have tried too many card numbers, please contact merchant. 	The transaction was blocked by the MultipleCardTries module.
        convertedErrors.put("10106", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.UNKNOWN, AuthorizationResult.DeclineReason.USAGE_EXCEEDED_1_DAY));
        // 10107 	Invalid billing zip code. 	The ZipCodeVerification module was not able to locate the billing zip code.
        convertedErrors.put("10107", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_CARD_POSTAL_CODE));
        // 10108 	Invalid shipping zip code. 	The ZipCodeValidation fraud module was not able to find module.
        convertedErrors.put("10108", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_SHIPPING_POSTAL_CODE));
        // 10109 	Billing state does not match billing zip code. 	ZipCodeVerification database came up with a conflict.
        convertedErrors.put("10109", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_CARD_STATE));
        // 10110 	Billing city does not match billing zip code. 	ZipCodeVerification database came up with a conflict.
        convertedErrors.put("10110", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_CARD_CITY));
        // 10111 	Billing area code does not match billing zip code. 	ZipCodeVerification database came up with a conflict.
        convertedErrors.put("10111", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_CARD_PHONE));
        // 10112 	Shipping state does not match shipping zip code. 	ZipCodeVerification database came up with a conflict.
        convertedErrors.put("10112", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_SHIPPING_STATE));
        // 10113 	Shipping city does not match shipping zip code. 	ZipCodeVerification database came up with a conflict.
        convertedErrors.put("10113", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_SHIPPING_CITY));
        // 10114 	Shipping area code does not match shipping zip code. 	ZipCodeVerification database came up with a conflict.
        convertedErrors.put("10114", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_SHIPPING_POSTAL_CODE));
        // 10115 	Merchant does not accept transactions from [country]. 	IpCountry module blocked transaction.
        convertedErrors.put("10115", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.GATEWAY_SECURITY_GUIDELINES_NOT_MET));
        // 10116 	Unable to verify card ID number. 	CVV2, CID, etc result was blocked by CVVresponse fraud module.
        convertedErrors.put("10116", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_CARD_CODE, AuthorizationResult.DeclineReason.CVV2_MISMATCH));
        // 10117 	Transaction authentication required. 	The merchant has set a pin for this transaction but the api did not receive a UMmd5hash. They need to either upgrade their software to send the hash or they need to remove the pin on the source.
        convertedErrors.put("10117", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.HASH_CHECK_FAILED));
        // 10118 	Transaction authentication failed. 	The UMmd5hash did not match the hash that was calculated for the transaction.
        convertedErrors.put("10118", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.HASH_CHECK_FAILED));
        // 10119 	Unable to parse mag stripe data. 	Could not determine the mag data format that was sent in.
        convertedErrors.put("10119", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.UNKNOWN));
        // 10120 	Unable to locate valid installation. 	Please contact support. A wireless transaction came in with an install id that wasn't found in the system.
        convertedErrors.put("10120", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR));
        // 10121 	Wireless key disabled. 	Please contact support. The install id submitted has been deleted/disabled.
        convertedErrors.put("10121", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR));
        // 10122 	Wireless key mismatch. 	The wireless key submitted does not correspond to the source id created for this installation.
        convertedErrors.put("10122", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR));
        // 10123 	Success Operation was successful. 	-
        convertedErrors.put("10123", new ConvertedError(TransactionResult.CommunicationResult.SUCCESS, TransactionResult.ErrorCode.UNKNOWN));
        // 10124 	Unsupported transaction type. 	Only authonly, sales and voids may be captured. An attempt was made to settle a transaction that can not be captured. This error will occur if you attempt to capture an echeck transaction.
        convertedErrors.put("10124", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_TRANSACTION_TYPE));
        // 10125 	Original transaction not approved. 	You are trying to capture (settle) a transaction that was declined or resulted in an error. You can only capture approved transactions.
        convertedErrors.put("10125", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_TRANSACTION_TYPE));
        // 10126 	Transactions has already been settled. 	You are trying to capture a transaction that has already been settled.
        convertedErrors.put("10126", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.INVALID_TRANSACTION_TYPE));
        // 10127 	Card Declined Hard decline from First Data. 	-
        convertedErrors.put("10127", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.UNKNOWN, AuthorizationResult.DeclineReason.NO_SPECIFIC));
        // 10128 	Processor Error ([response]) 	Unknown response code from First Data Nashville.
        convertedErrors.put("10128", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 10129 	Invalid transaction data. 	PHP Library detected missing or invalid fields.
        convertedErrors.put("10129", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
        // 10130 	Library Error. 	CURL support not found PHP Library was not able to find curl support. You must compile php with curl and openssl.
        convertedErrors.put("10130", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR));
        // 10131 	Library Error. 	Unable to initialize CURL PHP Library was unable to initialize CURL. SSL support may be missing or incorrectly configured.
        convertedErrors.put("10131", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR));
        // 10132 	Error reading from card processing gateway. 	PHP Library was received a bad response from the gateway.
        convertedErrors.put("10132", new ConvertedError(TransactionResult.CommunicationResult.GATEWAY_ERROR, TransactionResult.ErrorCode.ERROR_TRY_AGAIN));
    }

    private AuthorizationResult authorizeOrSale(TransactionRequest transactionRequest, CreditCard creditCard, String command) {
        // Build the map of request parameters, catching ErrorCodeException after this step
        // because any of these errors will all be considered as TransactionResult.CommunicationResult.LOCAL_ERROR
        Map<String,String> request = new HashMap<String,String>();
        try {
            // Build the request parameters
            request.put("UMcommand", command);
            request.put("UMkey", key);
            // Note: UMhash is added at the bottom of this block to benefit from the values that are fomatted below
            request.put("UMcard", creditCard.getCardNumber());
            request.put("UMexpir", creditCard.getExpirationDateMMYY());
            
            // Add tax, ship, and duty amounts
            BigDecimal amount = transactionRequest.getAmount();
            BigDecimal taxAmount = transactionRequest.getTaxAmount();
            if(taxAmount!=null) amount = amount.add(taxAmount);
            BigDecimal shippingAmount = transactionRequest.getShippingAmount();
            if(shippingAmount!=null) amount = amount.add(shippingAmount);
            BigDecimal dutyAmount = transactionRequest.getDutyAmount();
            if(dutyAmount!=null) amount = amount.add(dutyAmount);
            amount = amount.setScale(2, RoundingMode.UNNECESSARY);
            request.put("UMamount", amount.toString());
            
            // Currency
            String currencyCode;
            String isoCode = transactionRequest.getCurrency().getCurrencyCode();
            if(isoCode.equals("USD")) currencyCode = "840";
            else {
                // Additional currency codes are documented here: http://wiki.usaepay.com/developer/currencycode
                throw new ErrorCodeException(TransactionResult.ErrorCode.INVALID_CURRENCY_CODE, "TransactionResult.ErrorCode.INVALID_CURRENCY_CODE");
            }
            request.put("UMcurrency", currencyCode);

            // tax
            if(taxAmount!=null) {
                BigDecimal scaledTaxAmount = taxAmount.setScale(2, RoundingMode.UNNECESSARY);
                request.put("UMtax", scaledTaxAmount.toString());
            }
            if(transactionRequest.getTaxExempt()) request.put("UMnontaxable", "yes");

            // Could add tips to the CreditCard API when needed
            
            // shipping
            if(shippingAmount!=null) {
                BigDecimal scaledShippingAmount = shippingAmount.setScale(2, RoundingMode.UNNECESSARY);
                request.put("UMshipping", scaledShippingAmount.toString());
            }

            // Could add discount to the CreditCard API when needed

            // subtotal
            BigDecimal subtotal = transactionRequest.getAmount().setScale(2, RoundingMode.UNNECESSARY);
            request.put("UMsubtotal", subtotal.toString());
            
            // customer ID
            String customerId = creditCard.getCustomerId();
            if(customerId!=null && customerId.length()>0) request.put("UMcustid", customerId);
            
            // invoice_number/order_number combined to a single value
            String invoiceNumber = transactionRequest.getInvoiceNumber();
            String orderNumber = transactionRequest.getOrderNumber();
            if(orderNumber!=null && orderNumber.length()>0) {
                if(invoiceNumber!=null && invoiceNumber.length()>0) {
                    invoiceNumber = invoiceNumber+"/"+orderNumber;
                } else {
                    invoiceNumber = orderNumber;
                }
            }
            if(invoiceNumber!=null && invoiceNumber.length()>0) {
                if(invoiceNumber.length()<=10) request.put("UMinvoice", invoiceNumber);
                else {
                    addMaxLengthParameter(request, "UMorderid", invoiceNumber, 64, TransactionResult.ErrorCode.INVALID_INVOICE_NUMBER);
                }
            }
            
            // purchase order number
            String ponum = transactionRequest.getPurchaseOrderNumber();
            if(ponum!=null && ponum.length()>0) request.put("UMponum", ponum);
            
            // description
            String description = transactionRequest.getDescription();
            if(description != null && description.length()>0) request.put("UMdescription", description);

            // comments
            String comments = creditCard.getComments();
            if(comments != null && comments.length()>0) request.put("UMcomments", comments);

            // cvv2
            String cvv2 = creditCard.getCardCode();
            if(cvv2!=null && cvv2.length()>0) request.put("UMcvv2", cvv2);

            // email
            String email = creditCard.getEmail();
            if(email!=null && email.length()>0) request.put("UMcustemail", email);

            // name
            request.put("UMname", getFullName(creditCard.getFirstName(), creditCard.getLastName()));

            // street address
            String street = getStreetAddress(creditCard.getStreetAddress1(), creditCard.getStreetAddress2());
            if(street.length()>0) request.put("UMstreet", street);
            
            // ZIP
            String zip = creditCard.getPostalCode();
            if(zip!=null && zip.length()>0) request.put("UMzip", zip);

            // IP address
            String customerIp = transactionRequest.getCustomerIp();
            if(customerIp!=null && customerIp.length()>0) request.put("UMip", customerIp);

            // Software version
            request.put("UMsoftware", SOFTWARE_VERSION);

            // test mode
            if(transactionRequest.getTestMode()) request.put("UMtestmode", "1");

            // Billing Address fields
            String billfname = creditCard.getFirstName();
            if(billfname!=null && billfname.length()>0) request.put("UMbillfname", billfname);
            String billlname = creditCard.getLastName();
            if(billlname!=null && billlname.length()>0) request.put("UMbilllname", billlname);
            String billcompany = creditCard.getCompanyName();
            if(billcompany!=null && billcompany.length()>0) request.put("UMbillcompany", billcompany);
            String billstreet = creditCard.getStreetAddress1();
            if(billstreet!=null && billstreet.length()>0) request.put("UMbillstreet", billstreet);
            String billstreet2 = creditCard.getStreetAddress2();
            if(billstreet2!=null && billstreet2.length()>0) request.put("UMbillstreet2", billstreet2);
            String billcity = creditCard.getCity();
            if(billcity!=null && billcity.length()>0) request.put("UMbillcity", billcity);
            String billstate = creditCard.getState();
            if(billstate!=null && billstate.length()>0) request.put("UMbillstate", billstate);
            String billzip = creditCard.getPostalCode();
            if(billzip!=null && billzip.length()>0) request.put("UMbillzip", billzip);
            String billcountry = creditCard.getCountryCode();
            if(billcountry!=null && billcountry.length()>0) request.put("UMbillcountry", billcountry);
            String billphone = creditCard.getPhone();
            if(billphone!=null && billphone.length()>0) request.put("UMbillphone", billphone);
            
            // Shipping Address fields
            String shipfname = transactionRequest.getShippingFirstName();
            if(shipfname!=null && shipfname.length()>0) request.put("UMshipfname", shipfname);
            String shiplname = transactionRequest.getShippingLastName();
            if(shiplname!=null && shiplname.length()>0) request.put("UMshiplname", shiplname);
            String shipcompany = transactionRequest.getShippingCompanyName();
            if(shipcompany!=null && shipcompany.length()>0) request.put("UMshipcompany", shipcompany);
            String shipstreet = transactionRequest.getShippingStreetAddress1();
            if(shipstreet!=null && shipstreet.length()>0) request.put("UMshipstreet", shipstreet);
            String shipstreet2 = transactionRequest.getShippingStreetAddress2();
            if(shipstreet2!=null && shipstreet2.length()>0) request.put("UMshipstreet2", shipstreet2);
            String shipcity = transactionRequest.getShippingCity();
            if(shipcity!=null && shipcity.length()>0) request.put("UMshipcity", shipcity);
            String shipstate = transactionRequest.getShippingState();
            if(shipstate!=null && shipstate.length()>0) request.put("UMshipstate", shipstate);
            String shipzip = transactionRequest.getShippingPostalCode();
            if(shipzip!=null && shipzip.length()>0) request.put("UMshipzip", shipzip);
            String shipcountry = transactionRequest.getShippingCountryCode();
            if(shipcountry!=null && shipcountry.length()>0) request.put("UMshipcountry", shipcountry);
            // Could add shipping phone number to API when needed
            
            // Remaining Billing and Shipping Address fields
            if(email!=null && email.length()>0) request.put("UMemail", email);
            String fax = creditCard.getFax();
            if(fax!=null && fax.length()>0) request.put("UMfax", fax);

            if(pin!=null && pin.length()>0) {
                // Calculate the hash as described at
                // http://wiki.usaepay.com/developer/transactionapi#source_pin_code
                try {
                    // Generate a seed
                    String seed;
                    {
                        // It didn't like Hex seed
                        //byte[] randomBytes = new byte[64];
                        //getRandom().nextBytes(randomBytes);
                        //seed = getFullHexString(randomBytes);
                        long randomLong = getRandom().nextLong();
                        if(randomLong==Long.MIN_VALUE) randomLong = 0;
                        if(randomLong<0) randomLong = -randomLong;
                        seed = Long.toString(randomLong);
                    }
                    // Generate the MD5 hash
                    StringBuilder hashData = new StringBuilder();
                    if(command.indexOf(':')!=-1) throw new ErrorCodeException(new IllegalArgumentException("command may not contain a colon (:): "+command), TransactionResult.ErrorCode.HASH_CHECK_FAILED, "TransactionResult.ErrorCode.HASH_CHECK_FAILED");
                    if(pin.indexOf(':')!=-1) throw new ErrorCodeException(new IllegalArgumentException("pin may not contain a colon (:): "+pin), TransactionResult.ErrorCode.HASH_CHECK_FAILED, "TransactionResult.ErrorCode.HASH_CHECK_FAILED");
                    hashData.append(command).append(':').append(pin).append(':').append(amount.toString()).append(':');
                    if(invoiceNumber!=null && invoiceNumber.length()>0) hashData.append(invoiceNumber);
                    hashData.append(':').append(seed);
                    String md5 = createMd5Hash(hashData.toString());
                    // Add the hash
                    String hash = "m/"+seed+"/"+md5+"/y";
                    request.put("UMhash", hash);
                } catch(NoSuchAlgorithmException err) {
                    throw new ErrorCodeException(err, TransactionResult.ErrorCode.HASH_CHECK_FAILED, "TransactionResult.ErrorCode.HASH_CHECK_FAILED");
                }
            }
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
        }

        // Now try to process, considering as a GATEWAY_ERROR for any ErrorCodeExceptions.
        Map<String,String> results;
        try {
            // Now that the local request has been created successfully, contact the PayflowPro API.
            results = submitTransaction(request, transactionRequest.getTestMode());
        } catch(ErrorCodeException err) {
            return new AuthorizationResult(
                getProviderId(),
                TransactionResult.CommunicationResult.GATEWAY_ERROR,
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
        }

        String status = results.get("UMstatus");
        String authCode = results.get("UMauthCode");
        String refNum = results.get("UMrefNum");
        String avsResultCode = results.get("UMavsResultCode");
        String cvv2ResultCode = results.get("UMcvv2ResultCode");
        String result = results.get("UMresult");
        String error = results.get("UMerror");
        String errorcode = results.get("UMerrorcode");
        String isDuplicate = results.get("UMisDuplicate");
        String responseHash = results.get("UMresponseHash");
        
        if(pin!=null && pin.length()>0) {
            try {
                // Verify the response hash as described at
                // http://wiki.usaepay.com/developer/transactionapi#source_pin_code
                if(responseHash==null || responseHash.length()==0) {
                    System.err.println("responseHash is null or empty");
                    throw new ErrorCodeException(TransactionResult.ErrorCode.HASH_CHECK_FAILED, "TransactionResult.ErrorCode.HASH_CHECK_FAILED");
                }
                try {
                    // Parse the hash type
                    int slashPos1 = responseHash.indexOf('/');
                    if(slashPos1==-1) {
                        System.err.println("Unable to find first slash: "+responseHash);
                        throw new ErrorCodeException(TransactionResult.ErrorCode.HASH_CHECK_FAILED, "TransactionResult.ErrorCode.HASH_CHECK_FAILED");
                    }
                    String hashType = responseHash.substring(0, slashPos1);
                    if(!hashType.equals("m")) {
                        System.err.println("Unexpected hashType: "+hashType);
                        throw new ErrorCodeException(TransactionResult.ErrorCode.HASH_CHECK_FAILED, "TransactionResult.ErrorCode.HASH_CHECK_FAILED");
                    }

                    // Parse seed
                    int slashPos2 = responseHash.indexOf('/', slashPos1+1);
                    if(slashPos2==-1) {
                        System.err.println("Unable to find second slash: "+responseHash);
                        throw new ErrorCodeException(TransactionResult.ErrorCode.HASH_CHECK_FAILED, "TransactionResult.ErrorCode.HASH_CHECK_FAILED");
                    }
                    String seed = responseHash.substring(slashPos1+1, slashPos2);

                    // The rest is the MD5
                    String responseMd5 = responseHash.substring(slashPos2+1);

                    // Generate the MD5 hash
                    String hashData = pin+":"+result+":"+refNum+":"+seed;
                    String expectedMd5 = createMd5Hash(hashData);
                    if(!expectedMd5.equalsIgnoreCase(responseMd5)) {
                        System.err.println("Response MD5 != Expected MD5: "+responseMd5+" != "+expectedMd5);
                        throw new ErrorCodeException(TransactionResult.ErrorCode.HASH_CHECK_FAILED, "TransactionResult.ErrorCode.HASH_CHECK_FAILED");
                    }
                } catch(NoSuchAlgorithmException err) {
                    throw new ErrorCodeException(err, TransactionResult.ErrorCode.HASH_CHECK_FAILED, "TransactionResult.ErrorCode.HASH_CHECK_FAILED");
                }
            } catch(ErrorCodeException err) {
                return new AuthorizationResult(
                    getProviderId(),
                    TransactionResult.CommunicationResult.GATEWAY_ERROR,
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
            }
        }
        
        // Convert to CvvResult
        AuthorizationResult.CvvResult cvvResult;
        if("M".equals(cvv2ResultCode)) cvvResult = AuthorizationResult.CvvResult.MATCH;
        else if("N".equals(cvv2ResultCode)) cvvResult = AuthorizationResult.CvvResult.NO_MATCH;
        else if("P".equals(cvv2ResultCode)) cvvResult = AuthorizationResult.CvvResult.NOT_PROCESSED;
        else if("S".equals(cvv2ResultCode)) cvvResult = AuthorizationResult.CvvResult.CVV2_NOT_PROVIDED_BY_MERCHANT;
        else if("U".equals(cvv2ResultCode)) cvvResult = AuthorizationResult.CvvResult.NOT_SUPPORTED_BY_ISSUER;
        else if("X".equals(cvv2ResultCode)) cvvResult = AuthorizationResult.CvvResult.NOT_PROCESSED;
        else {
            String cvv2 = creditCard.getCardCode();
            if(cvv2!=null && cvv2.length()>0) cvvResult = AuthorizationResult.CvvResult.NOT_PROCESSED;
            else cvvResult = AuthorizationResult.CvvResult.CVV2_NOT_PROVIDED_BY_MERCHANT;
        }
        if(DEBUG_RESPONSE) System.out.println("    cvvResult="+cvvResult);

        // Convert to AvsResult
        AuthorizationResult.AvsResult avsResult;
        if(avsResultCode==null) {
            avsResult=AuthorizationResult.AvsResult.UNAVAILABLE;
        } else {
            if(
                "YYY".equals(avsResultCode)
                || "Y".equals(avsResultCode)
                || "YYA".equals(avsResultCode)
                || "YYD".equals(avsResultCode)
            ) {
                avsResult=AuthorizationResult.AvsResult.ADDRESS_Y_ZIP_5;
            } else if(
                "NYZ".equals(avsResultCode)
                || "Z".equals(avsResultCode)
            ) {
                avsResult=AuthorizationResult.AvsResult.ADDRESS_N_ZIP_5;
            } else if(
                "YNA".equals(avsResultCode)
                || "A".equals(avsResultCode)
                || "YNY".equals(avsResultCode)
            ) {
                avsResult=AuthorizationResult.AvsResult.ADDRESS_Y_ZIP_N;
            } else if(
                "NNN".equals(avsResultCode)
                || "N".equals(avsResultCode)
                || "NN".equals(avsResultCode)
            ) {
                avsResult=AuthorizationResult.AvsResult.ADDRESS_N_ZIP_N;
            } else if(
                "YYX".equals(avsResultCode)
                || "X".equals(avsResultCode)
            ) {
                avsResult=AuthorizationResult.AvsResult.ADDRESS_Y_ZIP_9;
            } else if(
                "NYW".equals(avsResultCode)
                || "W".equals(avsResultCode)
            ) {
                avsResult=AuthorizationResult.AvsResult.ADDRESS_N_ZIP_9;
            } else if(
                "XXW".equals(avsResultCode)
            ) {
                avsResult=AuthorizationResult.AvsResult.UNKNOWN;
            } else if(
                "XXU".equals(avsResultCode)
            ) {
                avsResult=AuthorizationResult.AvsResult.UNAVAILABLE;
            } else if(
                "XXR".equals(avsResultCode)
                || "R".equals(avsResultCode)
                || "U".equals(avsResultCode)
                || "E".equals(avsResultCode)
            ) {
                avsResult=AuthorizationResult.AvsResult.RETRY;
            } else if(
                "XXS".equals(avsResultCode)
                || "S".equals(avsResultCode)
            ) {
                avsResult=AuthorizationResult.AvsResult.SERVICE_NOT_SUPPORTED;
            } else if(
                "XXE".equals(avsResultCode)
            ) {
                avsResult=AuthorizationResult.AvsResult.UNKNOWN;
            } else if(
                "XXG".equals(avsResultCode)
                || "G".equals(avsResultCode)
                || "C".equals(avsResultCode)
                || "I".equals(avsResultCode)
            ) {
                avsResult=AuthorizationResult.AvsResult.UNKNOWN;
            } else if(
                "YYG".equals(avsResultCode)
                || "B".equals(avsResultCode)
                || "M".equals(avsResultCode)
            ) {
                avsResult=AuthorizationResult.AvsResult.ADDRESS_N_ZIP_N;
            } else if(
                "GGG".equals(avsResultCode)
                || "D".equals(avsResultCode)
            ) {
                avsResult=AuthorizationResult.AvsResult.ADDRESS_Y_ZIP_5;
            } else if(
                "YGG".equals(avsResultCode)
                || "P".equals(avsResultCode)
            ) {
                avsResult=AuthorizationResult.AvsResult.ADDRESS_N_ZIP_5;
            } else {
                System.err.println("Unexpected value for avsResultCode: "+avsResultCode);
                avsResult=AuthorizationResult.AvsResult.UNKNOWN;
            }
        }
        if(DEBUG_RESPONSE) System.out.println("    avsResult="+avsResult);

        if("Approved".equals(status)) {
            return new AuthorizationResult(
                getProviderId(),
                TransactionResult.CommunicationResult.SUCCESS,
                null,
                null,
                null,
                refNum,
                result,
                AuthorizationResult.ApprovalResult.APPROVED,
                null,
                null,
                null,
                null,
                cvv2ResultCode,
                cvvResult,
                avsResultCode,
                avsResult,
                authCode
            );
        } else if("Declined".equals(status)) {
            ConvertedError convertedError = convertedErrors.get(errorcode);
            AuthorizationResult.DeclineReason declineReason;
            if(convertedError!=null && convertedError.declineReason!=null) declineReason = convertedError.declineReason;
            else declineReason = AuthorizationResult.DeclineReason.UNKNOWN;
            return new AuthorizationResult(
                getProviderId(),
                TransactionResult.CommunicationResult.SUCCESS,
                null,
                null,
                null,
                refNum,
                result,
                AuthorizationResult.ApprovalResult.DECLINED,
                errorcode,
                declineReason,
                null,
                null,
                cvv2ResultCode,
                cvvResult,
                avsResultCode,
                avsResult,
                authCode
            );
        } else if("Verification".equals(status)) {
            return new AuthorizationResult(
                getProviderId(),
                TransactionResult.CommunicationResult.SUCCESS,
                null,
                null,
                null,
                refNum,
                result,
                AuthorizationResult.ApprovalResult.HOLD,
                null,
                null,
                null,
                AuthorizationResult.ReviewReason.RISK_MANAGEMENT,
                cvv2ResultCode,
                cvvResult,
                avsResultCode,
                avsResult,
                authCode
            );
        } else {
            TransactionResult.CommunicationResult communicationResult;
            TransactionResult.ErrorCode errorCode;
            ConvertedError convertedError = convertedErrors.get(errorcode);
            if(convertedError!=null) {
                communicationResult = convertedError.communicationResult;
                errorCode = convertedError.errorCode;
            } else {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.UNKNOWN;
            }
            return new AuthorizationResult(
                getProviderId(),
                communicationResult,
                result,
                errorCode,
                error,
                refNum,
                null,
                null,
                null,
                null,
                null,
                null,
                cvv2ResultCode,
                cvvResult,
                avsResultCode,
                avsResult,
                authCode
            );
        }
    }

    @Override
    public SaleResult sale(TransactionRequest transactionRequest, CreditCard creditCard) {
        AuthorizationResult authorizationResult = authorizeOrSale(transactionRequest, creditCard, "sale");
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
        return authorizeOrSale(transactionRequest, creditCard, "authonly");
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
