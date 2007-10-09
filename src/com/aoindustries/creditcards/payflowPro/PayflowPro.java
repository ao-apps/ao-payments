package com.aoindustries.creditcards.payflowPro;

/*
 * Copyright 2007 by AO Industries, Inc.,
 * 816 Azalea Rd, Mobile, Alabama, 36693, U.S.A.
 * All rights reserved.
 */
import com.Verisign.payment.PFProAPI;
import com.Verisign.payment.ParameterList;
import com.aoindustries.creditcards.ApplicationResourcesAccessor;
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
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Provider for the PayflowPro_XMLPay system<br>
 * <br>
 * Configuration parameters:
 * <ol>
 *   <li>user -
 *     If you set up one or more additional users on the account, this value is the ID of the user authorized to process transactions.
 *     If, however, you have not sest up additional users on the account, user has the same value as vendor.
 *   </li>
 *   <li>vendor - Your merchant login ID that you created when you registered for the Payflow Pro account.  This value is case sensitive.</code>
 *   <li>partner -
 *     The ID provided to you by the authorized PayPal Reseller who registered you for the Payflow Pro service.
 *     If you purchased your account directly from PayPal, use PayPal.  This values is case-sensitive.</li>
 *   <li>password - User's password (string).</li>
 *   <li>certPath - The path to the directory containing the <code>f73e89fd.0</code> file.
 * </ol>
 *
 * TODO: Should we support the GetStatus call???
 * TODO: Should we support Buyer Authentication Service?
 * TODO: Should we support separate Freight and Handling amounts instead of just
 *       the one shipping?  We would need to automatically combine them on
 *       processors that don't support the distinction.
 * TODO: Is Prenote used for storing cards?
 * TODO: Use AMEXID?
 * TODO: Support SettleDate?
 * TODO: Support SHIPTOPHONENUM?
 * TODO: Possible better rates if we provide the merchant details and ship from details.
 *
 * @author  AO Industries, Inc.
 */
public class PayflowPro implements MerchantServicesProvider {

    private final String providerId;
    private final String user;
    private final String vendor;
    private final String partner;
    private final String password;
    private final String certPath;

    public PayflowPro(String providerId, String user, String vendor, String partner, String password, String certPath) {
        this.providerId = providerId;
        this.user = user;
        this.vendor = vendor;
        this.partner = partner;
        this.password = password;
        this.certPath = certPath;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getUser() {
        return user;
    }
    
    public String getVendor() {
        return vendor;
    }

    public String getPartner() {
        return partner;
    }

    public String getPassword() {
        return password;
    }

    public String getCertPath() {
        return certPath;
    }

    /**
     * Submits a transaction (a Map of name/value pairs), and returns the result as a Map of name/value pairs.
     */
    protected Map<String,String> submitTransaction(Locale userLocale, Map<String,String> request, boolean testMode) throws ErrorCodeException {
        // Build the request string before allocating the connection to payflow
        StringBuilder parameterSB = new StringBuilder();
        for(Map.Entry<String,String> entry : request.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            if(value==null) value="";
            else value = value.replace('"', '\'');

            if(parameterSB.length()>0) parameterSB.append('&');
            parameterSB.append(name);
            if(value.indexOf('&')!=-1 || value.indexOf('=')!=-1) {
                // Provide length due to special character
                parameterSB.append('[').append(value.length()).append("]=");
            } else {
                // No length required
                parameterSB.append('=');
            }
            parameterSB.append(value);
        }

        // Perform the transaction
        String response;
        PFProAPI pfPro = new PFProAPI();
        pfPro.SetCertPath(certPath);
        pfPro.CreateContext(
            testMode ? "test-payflow.verisign.com" : "payflow.verisign.com",
            443,
            60,
            "",
            0,
            "",
            ""
        );
        try {
            response = pfPro.SubmitTransaction(parameterSB.toString());
        } finally {
            pfPro.DestroyContext();
        }

        // Parse the response after releasing the resources
        ParameterList plist = new ParameterList(response);
        Map<String,String> results = new HashMap<String,String>();
        while(plist.parseNextNameValuePair()) {
            String name = plist.getCurrentName();
            String value = plist.getCurrentValue();
            if(results.put(name, value)!=null) {
                throw new ErrorCodeException(
                    TransactionResult.ErrorCode.UNKNOWN,
                    userLocale,
                    "TransactionResult.duplicateResponseName",
                    name
                );
            }
        }
        return results;
    }

    /**
     * Adds a parameter to the request after checking its length.
     * If longer, throws an ErrorCodeException with the provided <code>TransactionResult.ErrorCode</code>, otherwise appends the value.
     *
     * @see  TransactionResult.ErrorCode
     */
    protected static void addMaxLengthParameter(Locale userLocale, Map<String,String> request, String name, String value, int maxLength, TransactionResult.ErrorCode errorCode) throws ErrorCodeException {
        if(value.length()>maxLength) {
            throw new ErrorCodeException(
                errorCode,
                userLocale,
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

    private AuthorizationResult authorizeOrSale(TransactionRequest transactionRequest, CreditCard creditCard, Locale userLocale, String trxType) {
        // Build the list of request parameters, catching ErrorCodeException after this step
        // because any of these errors will all be considered as TransactionResult.CommunicationResult.LOCAL_ERROR
        Map<String,String> request = new HashMap<String,String>();
        try {
            // Build the request parameters
            addMaxLengthParameter(userLocale, request, "ACCT", creditCard.getCardNumber(), 19, TransactionResult.ErrorCode.INVALID_CARD_NUMBER);

            // Add tax, ship, and duty amounts
            BigDecimal amount = transactionRequest.getAmount();
            BigDecimal taxAmount = transactionRequest.getTaxAmount();
            if(taxAmount!=null) amount = amount.add(taxAmount);
            BigDecimal shippingAmount = transactionRequest.getShippingAmount();
            if(shippingAmount!=null) amount = amount.add(shippingAmount);
            BigDecimal dutyAmount = transactionRequest.getDutyAmount();
            if(dutyAmount!=null) amount = amount.add(dutyAmount);
            
            addMaxLengthParameter(userLocale, request, "AMT", amount.toString(), 10, TransactionResult.ErrorCode.INVALID_AMOUNT);

            String comment1 = transactionRequest.getDescription();
            if(comment1 != null && comment1.length()>0) addTrimmedParameter(request, "COMMENT1", "Transaction Description: "+comment1, 128);
            String comment2 = creditCard.getComments();
            if(comment2 != null && comment2.length()>0) addTrimmedParameter(request, "COMMENT2", "Credit Card Comments: "+comment2, 128);

            addMaxLengthParameter(userLocale, request, "CURRENCY", transactionRequest.getCurrencyCode().name(), 3, TransactionResult.ErrorCode.INVALID_CURRENCY_CODE);
            
            String orderNumber = transactionRequest.getOrderNumber();
            if(orderNumber!=null && orderNumber.length()>0) addMaxLengthParameter(userLocale, request, "CUSTREF", orderNumber, 12, TransactionResult.ErrorCode.INVALID_ORDER_NUMBER);
            
            String cvv2 = creditCard.getCardCode();
            if(cvv2!=null && cvv2.length()>0) addMaxLengthParameter(userLocale, request, "CVV2", cvv2, 4, TransactionResult.ErrorCode.INVALID_CARD_CODE);
            
            addMaxLengthParameter(userLocale, request, "EXPDATE", creditCard.getExpirationDateMMYY(userLocale), 4, TransactionResult.ErrorCode.INVALID_EXPIRATION_DATE);
            addTrimmedParameter(request, "FIRSTNAME", creditCard.getFirstName(), 30);
            addMaxLengthParameter(userLocale, request, "PARTNER", partner, 12, TransactionResult.ErrorCode.INVALID_PARTNER);
            addMaxLengthParameter(userLocale, request, "PWD", password, 32, TransactionResult.ErrorCode.GATEWAY_SECURITY_GUIDELINES_NOT_MET);
            String street = getStreetAddress(creditCard.getStreetAddress1(), creditCard.getStreetAddress2());
            if(street.length()>0) addTrimmedParameter(request, "STREET", street, 30);
            request.put("TENDER", "C");
            request.put("TRXTYPE", trxType);
            addMaxLengthParameter(userLocale, request, "USER", user, 64, TransactionResult.ErrorCode.GATEWAY_SECURITY_GUIDELINES_NOT_MET);
            addMaxLengthParameter(userLocale, request, "VENDOR", vendor==null ? "" : vendor, 64, TransactionResult.ErrorCode.GATEWAY_SECURITY_GUIDELINES_NOT_MET);
            String zip = creditCard.getPostalCode();
            if(zip!=null && zip.length()>0) addTrimmedParameter(request, "ZIP", CreditCard.numbersOnly(zip), 9);

            String email = creditCard.getEmail();
            if(email!=null && email.length()>0) addMaxLengthParameter(userLocale, request, "EMAIL", email, 64, TransactionResult.ErrorCode.INVALID_CARD_EMAIL);
            
            String phone = creditCard.getPhone();
            if(phone!=null && phone.length()>0) addMaxLengthParameter(userLocale, request, "PHONENUM", phone, 20, TransactionResult.ErrorCode.INVALID_CARD_PHONE);

            try {
                addTrimmedParameter(request, "CUSTHOSTNAME", InetAddress.getLocalHost().getHostName(), 60);
            } catch(IOException err) {
                err.printStackTrace();
            }
 
            String customerIp = transactionRequest.getCustomerIp();
            if(customerIp!=null && customerIp.length()>0) addTrimmedParameter(request, "CUSTIP", customerIp, 15);

            String shippingCountry = transactionRequest.getShippingCountryCode();
            if(shippingCountry!=null && shippingCountry.length()>0) {
                if("US".equals(shippingCountry)) request.put("SHIPTOCOUNTRY", "840");
                else System.err.println("WARNING: PayflowPro: shippingCountry unknown, not sending SHIPTOCOUNTRY: "+shippingCountry);
            }

            String shippingFirstName = transactionRequest.getShippingFirstName();
            if(shippingFirstName!=null && shippingFirstName.length()>0) addTrimmedParameter(request, "SHIPTOFIRSTNAME", shippingFirstName, 30);

            String shippingLastName = transactionRequest.getShippingLastName();
            if(shippingLastName!=null && shippingLastName.length()>0) addTrimmedParameter(request, "SHIPTOLASTNAME", shippingLastName, 30);

            String shippingStreet = getStreetAddress(transactionRequest.getShippingStreetAddress1(), transactionRequest.getShippingStreetAddress2());
            if(shippingStreet.length()>0) addTrimmedParameter(request, "SHIPTOSTREET", shippingStreet, 30);

            String shippingPostalCode = transactionRequest.getShippingPostalCode();
            if(shippingPostalCode!=null && shippingPostalCode.length()>0) addTrimmedParameter(request, "SHIPTOZIP", CreditCard.numbersOnly(shippingPostalCode), 9);

            String invnum = transactionRequest.getInvoiceNumber();
            if(invnum!=null && invnum.length()>0) addMaxLengthParameter(userLocale, request, "INVNUM", invnum, 9, TransactionResult.ErrorCode.INVALID_INVOICE_NUMBER);
            
            String city = creditCard.getCity();
            if(city!=null && city.length()>0) addTrimmedParameter(request, "CITY", city, 20);

            String state = creditCard.getState();
            if(state!=null && state.length()>0) {
                if(state.length()==2) request.put("STATE", state);
                else System.err.println("WARNING: PayflowPro: state is not two-digits, and no automatic conversion has been implemented, not sending STATE: "+state);
            }

            addTrimmedParameter(request, "NAME", getFullName(creditCard.getFirstName(), creditCard.getLastName()), 30);

            String ss = creditCard.getCustomerTaxId();
            if(ss!=null && ss.length()>0) addTrimmedParameter(request, "SS", ss, 35);

            String ponum = transactionRequest.getPurchaseOrderNumber();
            if(ponum!=null && ponum.length()>0) addTrimmedParameter(request, "PONUM", ponum, 15);

            if(taxAmount!=null) addMaxLengthParameter(userLocale, request, "TAXAMT", taxAmount.toString(), 10, TransactionResult.ErrorCode.INVALID_TAX_AMOUNT);
            request.put("TAXEXEMPT", transactionRequest.getTaxExempt() ? "Y" : "N");
            if(dutyAmount!=null) addMaxLengthParameter(userLocale, request, "DUTYAMT", dutyAmount.toString(), 10, TransactionResult.ErrorCode.INVALID_DUTY_AMOUNT);
            if(shippingAmount!=null) addMaxLengthParameter(userLocale, request, "FREIGHTAMT", shippingAmount.toString(), 10, TransactionResult.ErrorCode.INVALID_SHIPPING_AMOUNT);

            String cardCountryCode = creditCard.getCountryCode();
            if(cardCountryCode!=null && cardCountryCode.length()>0) addTrimmedParameter(request, "BILLTOCOUNTRY", cardCountryCode, 30);

            String customerId = creditCard.getCustomerId();
            if(customerId!=null && customerId.length()>0) request.put("CUSTCODE", customerId); //addTrimmedParameter(request, "CUSTCODE", customerId, 4);

            String shippingCity = transactionRequest.getShippingCity();
            if(shippingCity!=null && shippingCity.length()>0) addTrimmedParameter(request, "SHIPTOCITY", shippingCity, 30);

            String shippingState = transactionRequest.getShippingState();
            if(shippingState!=null && shippingState.length()>0) addTrimmedParameter(request, "SHIPTOSTATE", shippingState, 10);

            // No length constraint specified in documentation
            String companyName = creditCard.getCompanyName();
            if(companyName!=null && companyName.length()>0) request.put("COMPANYNAME", companyName);
        } catch(ErrorCodeException err) {
            new AuthorizationResult(
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
            results = submitTransaction(userLocale, request, transactionRequest.getTestMode());
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
        
        String pnref = results.get("PNREF");
        String result = results.get("RESULT");
        String cvv2Match = results.get("CVV2MATCH");  // Y, N, X, or null
        String respMsg = results.get("RESPMSG");
        String authCode = results.get("AUTHCODE");
        String avsAddr = results.get("AVSADDR"); // Y, N, X, or null
        if(avsAddr==null || avsAddr.length()==0) avsAddr="?";
        String avsZip = results.get("AVSZIP"); // Y, N, X, or null
        if(avsZip==null || avsZip.length()==0) avsZip="?";
        String iavs = results.get("IAVS"); // International AVS.  Y, N, X, or null
        if(iavs==null || iavs.length()==0) iavs="?";
        String cardSecure = results.get("CARDSECURE"); // ???

        // Convert to CvvResult
        AuthorizationResult.CvvResult cvvResult;
        if("Y".equals(cvv2Match)) cvvResult = AuthorizationResult.CvvResult.MATCH;
        else if("N".equals(cvv2Match)) cvvResult = AuthorizationResult.CvvResult.NO_MATCH;
        else if("X".equals(cvv2Match)) cvvResult = AuthorizationResult.CvvResult.NOT_SUPPORTED_BY_ISSUER;
        else {
            String cvv2 = creditCard.getCardCode();
            if(cvv2!=null && cvv2.length()>0) cvvResult = AuthorizationResult.CvvResult.NOT_PROCESSED;
            else cvvResult = AuthorizationResult.CvvResult.CVV2_NOT_PROVIDED_BY_MERCHANT;
        }

        // Convert to AvsResult
        AuthorizationResult.AvsResult avsResult;
        if("Y".equals(avsAddr)) {
            if("Y".equals(avsZip)) avsResult=AuthorizationResult.AvsResult.ADDRESS_Y_ZIP_5;
            else if("N".equals(avsZip)) avsResult=AuthorizationResult.AvsResult.ADDRESS_Y_ZIP_N;
            else if("X".equals(avsZip)) avsResult=AuthorizationResult.AvsResult.SERVICE_NOT_SUPPORTED;
            else avsResult=AuthorizationResult.AvsResult.UNAVAILABLE;
        } else if("N".equals(avsAddr)) {
            if("Y".equals(avsZip)) avsResult=AuthorizationResult.AvsResult.ADDRESS_N_ZIP_5;
            else if("N".equals(avsZip)) avsResult=AuthorizationResult.AvsResult.ADDRESS_N_ZIP_N;
            else if("X".equals(avsZip)) avsResult=AuthorizationResult.AvsResult.SERVICE_NOT_SUPPORTED;
            else avsResult=AuthorizationResult.AvsResult.UNAVAILABLE;
        } else if("X".equals(avsAddr)) {
            avsResult=AuthorizationResult.AvsResult.SERVICE_NOT_SUPPORTED;
        } else {
            avsResult=AuthorizationResult.AvsResult.UNAVAILABLE;
        }

        if("0".equals(result)) {
            // Approved
            return new AuthorizationResult(
                getProviderId(),
                TransactionResult.CommunicationResult.SUCCESS,
                null,
                null,
                null,
                pnref,
                result,
                AuthorizationResult.ApprovalResult.APPROVED,
                null,
                null,
                null,
                null,
                cvv2Match,
                cvvResult,
                avsAddr+avsZip+iavs,
                avsResult,
                authCode
            );
        } else if(
            "12".equals(result) // Not specific
            || "50".equals(result) // Insufficient funds
            || "51".equals(result) // Exceeds per transaction limit
            || "112".equals(result) // Failed AVS check
            || "114".equals(result) // Card security code mismatch
            || "117".equals(result) // Failed merchant rule check (parse RESPMSG)
            || "125".equals(result) // Declined by Fraud Protection Services Filter
            || "128".equals(result) // Declined by merchant after flagged for review by Fraud Protection Services Filter
        ) {
            // Declined
            AuthorizationResult.DeclineReason declineReason;
            if("12".equals(result)) declineReason = AuthorizationResult.DeclineReason.NO_SPECIFIC;
            else if("50".equals(result)) declineReason = AuthorizationResult.DeclineReason.INSUFFICIENT_FUNDS;
            else if("51".equals(result)) declineReason = AuthorizationResult.DeclineReason.MAX_SALE_EXCEEDED;
            else if("112".equals(result)) declineReason = AuthorizationResult.DeclineReason.AVS_MISMATCH;
            else if("114".equals(result)) declineReason = AuthorizationResult.DeclineReason.CVV2_MISMATCH;
            else if("117".equals(result)) {
                // TODO: Parse respMsg for more details
                declineReason = AuthorizationResult.DeclineReason.UNKNOWN;
            } else if("125".equals(result)) declineReason = AuthorizationResult.DeclineReason.FRAUD_DETECTED;
            else if("128".equals(result)) declineReason = AuthorizationResult.DeclineReason.MANUAL_REVIEW;
            else declineReason = AuthorizationResult.DeclineReason.UNKNOWN;

            return new AuthorizationResult(
                getProviderId(),
                TransactionResult.CommunicationResult.SUCCESS,
                null,
                null,
                null,
                pnref,
                result,
                AuthorizationResult.ApprovalResult.DECLINED,
                respMsg,
                declineReason,
                null,
                null,
                cvv2Match,
                cvvResult,
                avsAddr+avsZip+iavs,
                avsResult,
                authCode
            );
        } else if("126".equals(result)) {
            // Hold
            return new AuthorizationResult(
                getProviderId(),
                TransactionResult.CommunicationResult.SUCCESS,
                null,
                null,
                null,
                pnref,
                result,
                AuthorizationResult.ApprovalResult.HOLD,
                null,
                null,
                respMsg,
                AuthorizationResult.ReviewReason.RISK_MANAGEMENT,
                cvv2Match,
                cvvResult,
                avsAddr+avsZip+iavs,
                avsResult,
                authCode
            );
        } else {
            // Other results
            TransactionResult.CommunicationResult communicationResult;
            TransactionResult.ErrorCode errorCode;
            if("1".equals(result)) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.GATEWAY_SECURITY_GUIDELINES_NOT_MET;
            } else if(
                "2".equals(result)
                || "25".equals(result)
                || "1021".equals(result)
            ) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.CARD_TYPE_NOT_SUPPORTED;
            } else if("3".equals(result)) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.INVALID_TRANSACTION_TYPE;
            } else if(
                "4".equals(result)
                || "1045".equals(result)
                || "-113".equals(result)
            ) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.INVALID_AMOUNT;
            } else if(
                "5".equals(result)
                || "1047".equals(result)
            ) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.INVALID_MERCHANT_ID;
            } else if("6".equals(result)) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                // We choose currency_not_supported instead of invalid_currency because going through the enum ensures
                // the currency is one of the acceptable values.
                errorCode = TransactionResult.ErrorCode.CURRENCY_NOT_SUPPORTED;
            } else if("7".equals(result)) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                // TODO: Parse RESPMSG for more info
                errorCode = TransactionResult.ErrorCode.UNKNOWN;
            } else if(
                "11".equals(result)
                || "36".equals(result)
                || "102".equals(result)
                || "107".equals(result)
                || "116".equals(result)
                || "119".equals(result)
                || "132".equals(result)
            ) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN;
            } else if("13".equals(result)) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.VOICE_AUTHORIZATION_REQUIRED;
            } else if(
                "14".equals(result)
                || "26".equals(result)
            ) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            } else if(
                "19".equals(result)
                || "20".equals(result)
            ) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.TRANSACTION_NOT_FOUND;
            } else if(
                "23".equals(result)
                || "1048".equals(result)
            ) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.INVALID_CARD_NUMBER;
            } else if("24".equals(result)) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.INVALID_EXPIRATION_DATE;
            } else if(
                "27".equals(result)
                || "28".equals(result)
            ) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.INSUFFICIENT_PERMISSIONS;
            } else if("30".equals(result)) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.DUPLICATE;
            } else if(
                "103".equals(result)
                || "104".equals(result)
                || "106".equals(result)
                || "109".equals(result)
                || "133".equals(result)
                || "150".equals(result)
            ) {
                communicationResult = TransactionResult.CommunicationResult.IO_ERROR;
                errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN;
            } else if("115".equals(result)) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN_5_MINUTES;
            } else if(
                "151".equals(result)
                || "-14".equals(result)
            ) {
                communicationResult = TransactionResult.CommunicationResult.IO_ERROR;
                errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN_5_MINUTES;
            } else if(
                "1022".equals(result)
                || "1049".equals(result)
            ) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.INVALID_CURRENCY_CODE;
            } else if("3".equals(result)) {
                communicationResult = TransactionResult.CommunicationResult.IO_ERROR;
                errorCode = TransactionResult.ErrorCode.INVALID_TRANSACTION_TYPE;
            } else if(
                "101".equals(result)
                || "-23".equals(result)
                || "-30".equals(result)
                || "-31".equals(result)
                || "-32".equals(result)
                || "-108".equals(result)
            ) {
                communicationResult = TransactionResult.CommunicationResult.LOCAL_ERROR;
                errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            } else if("-99".equals(result)) {
                communicationResult = TransactionResult.CommunicationResult.LOCAL_ERROR;
                errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN_5_MINUTES;
            } else if(result.startsWith("-")) {
                // Negative indicates communication error
                communicationResult = TransactionResult.CommunicationResult.IO_ERROR;
                errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN;
            } else {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.UNKNOWN;
            }

            return new AuthorizationResult(
                getProviderId(),
                communicationResult,
                result,
                errorCode,
                respMsg,
                pnref,
                null,
                null,
                null,
                null,
                null,
                null,
                cvv2Match,
                cvvResult,
                avsAddr+avsZip+iavs,
                avsResult,
                authCode
            );
        }
    }

    public SaleResult sale(TransactionRequest transactionRequest, CreditCard creditCard, Locale userLocale) {
        AuthorizationResult authorizationResult = authorizeOrSale(transactionRequest, creditCard, userLocale, "S");
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

    public AuthorizationResult authorize(TransactionRequest transactionRequest, CreditCard creditCard, Locale userLocale) {
        return authorizeOrSale(transactionRequest, creditCard, userLocale, "A");
    }

    public CaptureResult capture(AuthorizationResult authorizationResult, Locale userLocale) {
        throw new RuntimeException("TODO: Implement method");
    }

    public VoidResult voidTransaction(Transaction transaction, Locale userLocale) {
        throw new RuntimeException("TODO: Implement method");
    }

    public CreditResult credit(TransactionRequest transactionRequest, CreditCard creditCard, Locale userLocale) {
        throw new RuntimeException("TODO: Implement method");
    }

    public boolean canStoreCreditCards(Locale userLocale) {
        return false;
    }

    public String storeCreditCard(CreditCard creditCard, Locale userLocale) throws IOException {
        throw new RuntimeException("TODO: Credit card storage not yet implemented");
    }

    public void updateCreditCardNumberAndExpiration(CreditCard creditCard, String cardNumber, byte expirationMonth, short expirationYear, Locale userLocale) throws IOException {
        throw new RuntimeException("TODO: Credit card storage not yet implemented");
    }

    public void updateCreditCardExpiration(CreditCard creditCard, byte expirationMonth, short expirationYear, Locale userLocale) throws IOException {
        throw new RuntimeException("TODO: Credit card storage not yet implemented");
    }

    public void deleteCreditCard(CreditCard creditCard, Locale userLocale) throws IOException {
        throw new RuntimeException("TODO: Credit card storage not yet implemented");
    }
}
