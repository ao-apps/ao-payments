/*
 * Copyright 2007-2009 by AO Industries, Inc.,
 * 7262 Bull Pen Cir, Mobile, Alabama, 36695, U.S.A.
 * All rights reserved.
 */
package com.aoindustries.creditcards.payflowPro;

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
import java.util.logging.Level;
import java.util.logging.Logger;
import paypal.payflow.AuthorizationTransaction;
import paypal.payflow.BaseTransaction;
import paypal.payflow.BillTo;
import paypal.payflow.CardTender;
import paypal.payflow.Currency;
import paypal.payflow.CustomerInfo;
import paypal.payflow.Invoice;
import paypal.payflow.PayflowConnectionData;
import paypal.payflow.PayflowUtility;
import paypal.payflow.Response;
import paypal.payflow.SDKProperties;
import paypal.payflow.SaleTransaction;
import paypal.payflow.ShipTo;
import paypal.payflow.TransactionResponse;
import paypal.payflow.UserInfo;

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
 * </ol>
 *
 * TODO: Should we support the GetStatus call???<br />
 * TODO: Should we support Buyer Authentication Service?<br />
 * TODO: Should we support separate Freight and Handling amounts instead of just
 *       the one shipping?  We would need to automatically combine them on
 *       processors that don't support the distinction.<br />
 * TODO: Is Prenote used for storing cards?<br />
 * TODO: Use AMEXID?<br />
 * TODO: Support SettleDate?<br />
 * TODO: Support SHIPTOPHONENUM?<br />
 * TODO: Possible better rates if we provide the merchant details and ship from details.
 *
 * @author  AO Industries, Inc.
 */
public class PayflowPro implements MerchantServicesProvider {

    private static final Logger logger = Logger.getLogger(PayflowPro.class.getName());

    private static final String TEST_HOST_ADDRESS = "pilot-payflowpro.paypal.com";
    private static final String LIVE_HOST_ADDRESS = "payflowpro.paypal.com";
    private static final int HOST_PORT = 443;
    private static final int TIMEOUT = 60;

    private final String providerId;
    private final String user;
    private final String vendor;
    private final String partner;
    private final String password;

    public PayflowPro(String providerId, String user, String vendor, String partner, String password) {
        this.providerId = providerId;
        this.user = user;
        this.vendor = vendor;
        this.partner = partner;
        this.password = password;
    }

    /**
     * @deprecated  certPath is no longer used
     */
    @Deprecated
    public PayflowPro(String providerId, String user, String vendor, String partner, String password, String certPath) {
        this.providerId = providerId;
        this.user = user;
        this.vendor = vendor;
        this.partner = partner;
        this.password = password;
    }

    @Override
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

    private AuthorizationResult authorizeOrSale(TransactionRequest transactionRequest, CreditCard creditCard, String trxType) {
        // Build the transaction request objects, catching ErrorCodeException after this step
        // because any of these errors will all be considered as TransactionResult.CommunicationResult.LOCAL_ERROR
        BaseTransaction transaction;
        try {
            // These SDKProperties may not be necessary because PayflowConnectionData is used below, but they can't hurt.
            SDKProperties.setHostAddress(transactionRequest.getTestMode() ? TEST_HOST_ADDRESS : LIVE_HOST_ADDRESS);
            SDKProperties.setHostPort(HOST_PORT);
            SDKProperties.setTimeOut(TIMEOUT);

            UserInfo userInfo = new UserInfo(
                user,
                vendor==null ? "" : vendor,
                partner,
                password
            );
            PayflowConnectionData connectionData = new PayflowConnectionData(
                transactionRequest.getTestMode() ? TEST_HOST_ADDRESS : LIVE_HOST_ADDRESS,
                HOST_PORT,
                TIMEOUT
            );

            // Invoice
            Invoice invoice = new Invoice();
            String ponum = transactionRequest.getPurchaseOrderNumber();
            if(ponum!=null && ponum.length()>0) invoice.setPoNum(ponum);
            String invnum = transactionRequest.getInvoiceNumber();
            if(invnum!=null && invnum.length()>0) invoice.setInvNum(invnum);
            String comment1 = transactionRequest.getDescription();
            if(comment1 != null && comment1.length()>0) invoice.setComment1("Transaction Description: "+comment1);
            String comment2 = creditCard.getComments();
            if(comment2 != null && comment2.length()>0) invoice.setComment2("Credit Card Comments: "+comment2);
            String orderNumber = transactionRequest.getOrderNumber();
            if(orderNumber!=null && orderNumber.length()>0) invoice.setCustRef(orderNumber);

            // Add tax, ship, and duty amounts to invoice
            BigDecimal amount = transactionRequest.getAmount();
            BigDecimal taxAmount = transactionRequest.getTaxAmount();
            if(taxAmount!=null) amount = amount.add(taxAmount);
            BigDecimal shippingAmount = transactionRequest.getShippingAmount();
            if(shippingAmount!=null) amount = amount.add(shippingAmount);
            BigDecimal dutyAmount = transactionRequest.getDutyAmount();
            if(dutyAmount!=null) amount = amount.add(dutyAmount);
            invoice.setAmt(new Currency(amount.doubleValue(), transactionRequest.getCurrencyCode().name()));
            if(taxAmount!=null) invoice.setTaxAmt(new Currency(taxAmount.doubleValue(), transactionRequest.getCurrencyCode().name()));
            invoice.setTaxExempt(transactionRequest.getTaxExempt() ? "Y" : "N");
            if(shippingAmount!=null) invoice.setFreightAmt(new Currency(shippingAmount.doubleValue(), transactionRequest.getCurrencyCode().name()));
            if(dutyAmount!=null) invoice.setDutyAmt(new Currency(dutyAmount.doubleValue(), transactionRequest.getCurrencyCode().name()));

            // BillTo
            BillTo billTo = new BillTo();
            billTo.setFirstName(creditCard.getFirstName());
            billTo.setLastName(creditCard.getLastName());
            String email = creditCard.getEmail();
            if(email!=null && email.length()>0) billTo.setEmail(email);
            String phone = creditCard.getPhone();
            if(phone!=null && phone.length()>0) billTo.setPhoneNum(phone);
            String street = getStreetAddress(creditCard.getStreetAddress1(), creditCard.getStreetAddress2());
            if(street.length()>0) billTo.setStreet(street);
            String city = creditCard.getCity();
            if(city!=null && city.length()>0) billTo.setCity(city);
            String state = creditCard.getState();
            if(state!=null && state.length()>0) {
                if(state.length()==2) billTo.setState(state);
                else logger.log(Level.WARNING, "PayflowPro: state is not two-digits, and no automatic conversion has been implemented, not sending STATE", state);
            }
            String zip = creditCard.getPostalCode();
            if(zip!=null && zip.length()>0) billTo.setZip(CreditCard.numbersOnly(zip));
            String cardCountryCode = creditCard.getCountryCode();
            if(cardCountryCode!=null && cardCountryCode.length()>0) billTo.setBillToCountry(cardCountryCode);
            String companyName = creditCard.getCompanyName();
            if(companyName!=null && companyName.length()>0) billTo.setCompanyName(companyName);
            invoice.setBillTo(billTo);

            // ShipTo
            ShipTo shipTo = new ShipTo();
            String shippingFirstName = transactionRequest.getShippingFirstName();
            if(shippingFirstName!=null && shippingFirstName.length()>0) shipTo.setShipToFirstName(shippingFirstName);
            String shippingLastName = transactionRequest.getShippingLastName();
            if(shippingLastName!=null && shippingLastName.length()>0) shipTo.setShipToLastName(shippingLastName);
            String shippingStreetAddress1 = transactionRequest.getShippingStreetAddress1();
            if(shippingStreetAddress1!=null && shippingStreetAddress1.length()>0) shipTo.setShipToStreet(shippingStreetAddress1);
            String shippingStreetAddress2 = transactionRequest.getShippingStreetAddress2();
            if(shippingStreetAddress2!=null && shippingStreetAddress2.length()>0) {
                if(shippingStreetAddress1!=null && shippingStreetAddress1.length()>0) shipTo.setShipToStreet2(shippingStreetAddress2);
                else shipTo.setShipToStreet(shippingStreetAddress2);
            }
            String shippingCity = transactionRequest.getShippingCity();
            if(shippingCity!=null && shippingCity.length()>0) shipTo.setShipToCity(shippingCity);
            String shippingState = transactionRequest.getShippingState();
            if(shippingState!=null && shippingState.length()>0) shipTo.setShipToState(shippingState);
            String shippingPostalCode = transactionRequest.getShippingPostalCode();
            if(shippingPostalCode!=null && shippingPostalCode.length()>0) shipTo.setShipToZip(CreditCard.numbersOnly(shippingPostalCode));
            String shippingCountry = transactionRequest.getShippingCountryCode();
            if(shippingCountry!=null && shippingCountry.length()>0) {
                if("US".equals(shippingCountry)) shipTo.setShipToCountry("840");
                else System.err.println("WARNING: PayflowPro: shippingCountry unknown, not sending SHIPTOCOUNTRY: "+shippingCountry);
            }
            invoice.setShipTo(shipTo);

            // CustomerInfo
            CustomerInfo customerInfo = new CustomerInfo();
            String customerIp = transactionRequest.getCustomerIp();
            if(customerIp!=null && customerIp.length()>0) customerInfo.setCustIP(customerIp);
            String customerId = creditCard.getCustomerId();
            if(customerId!=null && customerId.length()>0) customerInfo.setCustCode(customerId);
            invoice.setCustomerInfo(customerInfo);

            // CreditCard
            paypal.payflow.CreditCard ppCreditCard = new paypal.payflow.CreditCard(creditCard.getCardNumber(), creditCard.getExpirationDateMMYY());
            String cvv2 = creditCard.getCardCode();
            if(cvv2!=null && cvv2.length()>0) ppCreditCard.setCvv2(cvv2);
            ppCreditCard.setName(getFullName(creditCard.getFirstName(), creditCard.getLastName()));
            CardTender cardTender = new CardTender(ppCreditCard);

            // ClientInfo
            //ClientInfo clientInfo = new ClientInfo();

            // RequestID: Use orderNumber if available, fall-back to PayflowUtility
            String requestId;
            if(orderNumber!=null && orderNumber.length()>0) requestId = orderNumber;
            else requestId = PayflowUtility.getRequestId();

            if("A".equals(trxType)) {
                transaction = new AuthorizationTransaction(userInfo, connectionData, invoice, cardTender, requestId);
            } else if("S".equals(trxType)) {
                transaction = new SaleTransaction(userInfo, connectionData, invoice, cardTender, requestId);
            } else throw new AssertionError("Unexpected value for trxType: "+trxType);
            //transaction.setClientInfo(clientInfo);

            /* TODO: Don't know what to do with these:
            try {
                addTrimmedParameter(request, "CUSTHOSTNAME", InetAddress.getLocalHost().getHostName(), 60);
            } catch(IOException err) {
                logger.log(Level.WARNING, null, err);
            }
            String ss = creditCard.getCustomerTaxId();
            if(ss!=null && ss.length()>0) addTrimmedParameter(request, "SS", ss, 35);
             */
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
        Response response;
        try {
            // Now that the local request has been created successfully, contact the PayflowPro API.
            response = transaction.submitTransaction();
            // TODO: Check for duplicates?
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
        TransactionResponse transactionResponse = response.getTransactionResponse();
        // FraudResponse fraudResponse = response.getFraudResponse();
        
        String pnref = transactionResponse.getPnref();
        int result = transactionResponse.getResult();
        String cvv2Match = transactionResponse.getCvv2Match();  // Y, N, X, or null
        String respMsg = transactionResponse.getRespMsg();
        String authCode = transactionResponse.getAuthCode();
        String avsAddr = transactionResponse.getAvsAddr(); // Y, N, X, or null
        if(avsAddr==null || avsAddr.length()==0) avsAddr="?";
        String avsZip = transactionResponse.getAvsZip(); // Y, N, X, or null
        if(avsZip==null || avsZip.length()==0) avsZip="?";
        String iavs = transactionResponse.getIavs(); // International AVS.  Y, N, X, or null
        if(iavs==null || iavs.length()==0) iavs="?";
        // String cardSecure = transactionResponse.getCardSecure(); // ???

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

        if(result==0) {
            // Approved
            return new AuthorizationResult(
                getProviderId(),
                TransactionResult.CommunicationResult.SUCCESS,
                null,
                null,
                null,
                pnref,
                Integer.toString(result),
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
            result==12 // Not specific
            || result==50 // Insufficient funds
            || result==51 // Exceeds per transaction limit
            || result==112 // Failed AVS check
            || result==114 // Card security code mismatch
            || result==117 // Failed merchant rule check (parse RESPMSG)
            || result==125 // Declined by Fraud Protection Services Filter
            || result==128 // Declined by merchant after flagged for review by Fraud Protection Services Filter
        ) {
            // Declined
            AuthorizationResult.DeclineReason declineReason;
            if(result==12) declineReason = AuthorizationResult.DeclineReason.NO_SPECIFIC;
            else if(result==50) declineReason = AuthorizationResult.DeclineReason.INSUFFICIENT_FUNDS;
            else if(result==51) declineReason = AuthorizationResult.DeclineReason.MAX_SALE_EXCEEDED;
            else if(result==112) declineReason = AuthorizationResult.DeclineReason.AVS_MISMATCH;
            else if(result==114) declineReason = AuthorizationResult.DeclineReason.CVV2_MISMATCH;
            else if(result==117) {
                // TODO: Parse respMsg for more details
                declineReason = AuthorizationResult.DeclineReason.UNKNOWN;
            } else if(result==125) declineReason = AuthorizationResult.DeclineReason.FRAUD_DETECTED;
            else if(result==128) declineReason = AuthorizationResult.DeclineReason.MANUAL_REVIEW;
            else declineReason = AuthorizationResult.DeclineReason.UNKNOWN;

            return new AuthorizationResult(
                getProviderId(),
                TransactionResult.CommunicationResult.SUCCESS,
                null,
                null,
                null,
                pnref,
                Integer.toString(result),
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
        } else if(result==126) {
            // Hold
            return new AuthorizationResult(
                getProviderId(),
                TransactionResult.CommunicationResult.SUCCESS,
                null,
                null,
                null,
                pnref,
                Integer.toString(result),
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
            if(result==1) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.GATEWAY_SECURITY_GUIDELINES_NOT_MET;
            } else if(
                result==2
                || result==25
                || result==1021
            ) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.CARD_TYPE_NOT_SUPPORTED;
            } else if(result==3) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.INVALID_TRANSACTION_TYPE;
            } else if(
                result==4
                || result==1045
                || result==-113
            ) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.INVALID_AMOUNT;
            } else if(
                result==5
                || result==1047
            ) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.INVALID_MERCHANT_ID;
            } else if(result==6) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                // We choose currency_not_supported instead of invalid_currency because going through the enum ensures
                // the currency is one of the acceptable values.
                errorCode = TransactionResult.ErrorCode.CURRENCY_NOT_SUPPORTED;
            } else if(result==7) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                // TODO: Parse RESPMSG for more info
                errorCode = TransactionResult.ErrorCode.UNKNOWN;
            } else if(
                result==11
                || result==36
                || result==102
                || result==107
                || result==116
                || result==119
                || result==132
            ) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN;
            } else if(result==13) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.VOICE_AUTHORIZATION_REQUIRED;
            } else if(
                result==14
                || result==26
            ) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            } else if(
                result==19
                || result==20
            ) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.TRANSACTION_NOT_FOUND;
            } else if(
                result==23
                || result==1048
            ) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.INVALID_CARD_NUMBER;
            } else if(result==24) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.INVALID_EXPIRATION_DATE;
            } else if(
                result==27
                || result==28
            ) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.INSUFFICIENT_PERMISSIONS;
            } else if(result==30) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.DUPLICATE;
            } else if(
                result==103
                || result==104
                || result==106
                || result==109
                || result==133
                || result==150
            ) {
                communicationResult = TransactionResult.CommunicationResult.IO_ERROR;
                errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN;
            } else if(result==115) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN_5_MINUTES;
            } else if(
                result==151
                || result==-14
            ) {
                communicationResult = TransactionResult.CommunicationResult.IO_ERROR;
                errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN_5_MINUTES;
            } else if(
                result==1022
                || result==1049
            ) {
                communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
                errorCode = TransactionResult.ErrorCode.INVALID_CURRENCY_CODE;
            } else if(result==3) {
                communicationResult = TransactionResult.CommunicationResult.IO_ERROR;
                errorCode = TransactionResult.ErrorCode.INVALID_TRANSACTION_TYPE;
            } else if(
                result==101
                || result==-23
                || result==-30
                || result==-31
                || result==-32
                || result==-108
            ) {
                communicationResult = TransactionResult.CommunicationResult.LOCAL_ERROR;
                errorCode = TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
            } else if(result==-99) {
                communicationResult = TransactionResult.CommunicationResult.LOCAL_ERROR;
                errorCode = TransactionResult.ErrorCode.ERROR_TRY_AGAIN_5_MINUTES;
            } else if(result<0) {
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
                Integer.toString(result),
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

    @Override
    public SaleResult sale(TransactionRequest transactionRequest, CreditCard creditCard) {
        AuthorizationResult authorizationResult = authorizeOrSale(transactionRequest, creditCard, "S");
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
        return authorizeOrSale(transactionRequest, creditCard, "A");
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
