package com.aoindustries.creditcards;

/*
 * Copyright 2007 by AO Industries, Inc.,
 * 816 Azalea Rd, Mobile, Alabama, 36693, U.S.A.
 * All rights reserved.
 */
import java.math.BigDecimal;
import java.util.Locale;
import org.apache.commons.validator.GenericValidator;

/**
 * Encapsulates the details about a specific transaction request, which will be
 * sent to either <code>sale</code> or <code>authorize</code>.
 *
 * @see  MerchantServicesProvider#sale
 * @see  MerchantServicesProvider#authorize
 *
 * @author  AO Industries, Inc.
 */
public class TransactionRequest implements Cloneable {

    /**
     * These are obtained from {@link http://en.wikipedia.org/wiki/ISO_4217}
     *
     * Note: Add more as needed
     */
    public enum CurrencyCode {
        CAD,
        CLP,
        EUR,
        GBP,
        JPY,
        USD,
        VEB;

        /**
         * Gets the display value in the default locale.
         */
        public String toString() {
            return toString(Locale.getDefault());
        }

        /**
         * Gets the display value in the provided locale.
         */
        public String toString(Locale userLocale) {
            return ApplicationResourcesAccessor.getMessage(userLocale, "TransactionRequest.CurrencyCode."+name());
        }
    }

    /**
     * The default number of seconds for duplication detection.
     */
    private static final int DEFAULT_DUPLICATE_WINDOW = 120;

    private boolean testMode;
    private int duplicateWindow = DEFAULT_DUPLICATE_WINDOW;
    private String orderNumber;
    private CurrencyCode currencyCode;
    private BigDecimal amount;
    private BigDecimal taxAmount;
    private boolean taxExempt;
    private BigDecimal shippingAmount;
    private BigDecimal dutyAmount;
    private String shippingFirstName;
    private String shippingLastName;
    private String shippingCompanyName;
    private String shippingStreetAddress1;
    private String shippingStreetAddress2;
    private String shippingCity;
    private String shippingState;
    private String shippingPostalCode;
    private String shippingCountryCode;
    private boolean emailCustomer;
    private String merchantEmail;
    private String invoiceNumber;
    private String purchaseOrderNumber;
    private String description;

    /**
     * Creates an empty TransactionRequest.  The values should be set using the appropriate setter methods.
     */
    public TransactionRequest(Locale userLocale) {
        setCurrencyCode(CurrencyCode.valueOf(ApplicationResourcesAccessor.getMessage(userLocale, "TransactionRequest.currencyCode.default")));
    }

    /**
     * Creates a TransactionRequest providing all of the details.
     *
     * @throws  IllegalArgumentException  if anything not valid
     */
    public TransactionRequest(
        Locale userLocale,
        boolean testMode,
        int duplicateWindow,
        String orderNumber,
        CurrencyCode currencyCode,
        BigDecimal amount,
        BigDecimal taxAmount,
        boolean taxExempt,
        BigDecimal shippingAmount,
        BigDecimal dutyAmount,
        String shippingFirstName,
        String shippingLastName,
        String shippingCompanyName,
        String shippingStreetAddress1,
        String shippingStreetAddress2,
        String shippingCity,
        String shippingState,
        String shippingPostalCode,
        String shippingCountryCode,
        boolean emailCustomer,
        String merchantEmail,
        String invoiceNumber,
        String purchaseOrderNumber,
        String description
    ) {
        setTestMode(testMode);
        setDuplicateWindow(duplicateWindow);
        setOrderNumber(orderNumber);
        if(currencyCode==null) setCurrencyCode(CurrencyCode.valueOf(ApplicationResourcesAccessor.getMessage(userLocale, "TransactionRequest.currencyCode.default")));
        else setCurrencyCode(currencyCode);
        setAmount(amount, userLocale);
        setTaxAmount(taxAmount, userLocale);
        setTaxExempt(taxExempt);
        setShippingAmount(shippingAmount, userLocale);
        setDutyAmount(dutyAmount, userLocale);
        setShippingFirstName(shippingFirstName);
        setShippingLastName(shippingLastName);
        setShippingCompanyName(shippingCompanyName);
        setShippingStreetAddress1(shippingStreetAddress1);
        setShippingStreetAddress2(shippingStreetAddress2);
        setShippingCity(shippingCity);
        setShippingState(shippingState);
        setShippingPostalCode(shippingPostalCode);
        setShippingCountryCode(shippingCountryCode, userLocale);
        setEmailCustomer(emailCustomer);
        setMerchantEmail(merchantEmail, userLocale);
        setInvoiceNumber(invoiceNumber);
        setPurchaseOrderNumber(purchaseOrderNumber);
        setDescription(description);
    }

    public TransactionRequest clone() {
        try {
            return (TransactionRequest)super.clone();
        } catch(CloneNotSupportedException err) {
            throw new RuntimeException(err);
        }
    }
    /**
     * Gets the test mode for this transaction.
     */
    public boolean getTestMode() {
        return testMode;
    }
    
    /**
     * Sets the test mode for this transaction.
     */
    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

    /**
     * Gets the duplicate detection window in seconds.  Defaults to <code>DEFAULT_DUPLICATE_WINDOW</code> seconds.
     */
    public int getDuplicateWindow() {
        return duplicateWindow;
    }
    
    /**
     * Sets the duplicate detenction window in seconds.
     */
    public void setDuplicateWindow(int duplicateWindow) {
        this.duplicateWindow = duplicateWindow;
    }

    /**
     * Gets the unique order number.
     */
    public String getOrderNumber() {
        return orderNumber;
    }
    
    /**
     * Sets the unique order number.
     */
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * Gets the currency code for this transaction.
     */
    public CurrencyCode getCurrencyCode() {
        return currencyCode;
    }

    /**
     * Sets the currency code for this transaction.
     */
    public void setCurrencyCode(CurrencyCode currencyCode) {
        this.currencyCode = currencyCode;
    }

    /**
     * Gets the amount of the transaction.  This amount should not include any tax, shipping charges, or duty.
     * Thus the total amount of the transaction is the amount + taxAmount + shippingAmount + dutyAmount.
     *
     * @see  #setTaxAmount
     * @see  #setShippingAmount
     * @see  #setDutyAmount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the transaction.  This amount should not include any tax, shipping charges, or duty.
     * Thus the total amount of the transaction is the amount + taxAmount + shippingAmount + dutyAmount.
     *
     * @throws  IllegalArgumentException  if amount <= 0
     */
    public void setAmount(BigDecimal amount, Locale userLocale) {
        if(amount==null) {
            this.amount = null;
        } else {
            if(amount.compareTo(BigDecimal.ZERO)<=0) throw new LocalizedIllegalArgumentException(userLocale, "TransactionRequest.setAmount.amount.lessThanEqualZero");
            this.amount = amount;
        }
    }

    /**
     * Sets the tax amount of the transaction.
     */
    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    /**
     * Sets the tax amount of the transaction.
     *
     * @throws  IllegalArgumentException  if amount < 0
     */
    public void setTaxAmount(BigDecimal taxAmount, Locale userLocale) {
        if(taxAmount==null) {
            this.taxAmount = null;
        } else {
            if(taxAmount.compareTo(BigDecimal.ZERO)<=0) throw new LocalizedIllegalArgumentException(userLocale, "TransactionRequest.setTaxAmount.taxAmount.lessThanEqualZero");
            this.taxAmount = taxAmount;
        }
    }

    /**
     * Gets the tax exempt flag.
     */
    public boolean getTaxExempt() {
        return taxExempt;
    }

    /**
     * Set the tax exempt flag.
     */
    public void setTaxExempt(boolean taxExempt) {
        this.taxExempt = taxExempt;
    }

    /**
     * Sets the shipping amount of the transaction.
     */
    public BigDecimal getShippingAmount() {
        return shippingAmount;
    }

    /**
     * Sets the shipping amount of the transaction.
     *
     * @throws  IllegalArgumentException  if shippingAmount < 0
     */
    public void setShippingAmount(BigDecimal shippingAmount, Locale userLocale) {
        if(shippingAmount==null) {
            this.shippingAmount = null;
        } else {
            if(shippingAmount.compareTo(BigDecimal.ZERO)<=0) throw new LocalizedIllegalArgumentException(userLocale, "TransactionRequest.setShippingAmount.shippingAmount.lessThanEqualZero");
            this.shippingAmount = shippingAmount;
        }
    }

    /**
     * Gets the duty charges of the transaction.
     */
    public BigDecimal getDutyAmount() {
        return dutyAmount;
    }
    
    /**
     * Sets the duty charges of the transaction.
     *
     * @throws  IllegalArgumentException  if dutyAmount < 0
     */
    public void setDutyAmount(BigDecimal dutyAmount, Locale userLocale) {
        if(dutyAmount==null) {
            this.dutyAmount = null;
        } else {
            if(dutyAmount.compareTo(BigDecimal.ZERO)<=0) throw new LocalizedIllegalArgumentException(userLocale, "TransactionRequest.setDutyAmount.dutyAmount.lessThanEqualZero");
            this.dutyAmount = dutyAmount;
        }
    }

    /**
     * Gets the first name of the recipient.
     */
    public String getShippingFirstName() {
        return shippingFirstName;
    }

    /**
     * Trims and sets the first name of the recipient.
     */
    public void setShippingFirstName(String shippingFirstName) {
        this.shippingFirstName = shippingFirstName==null ? null : shippingFirstName.trim();
    }

    /**
     * Gets the last name of the recipient.
     */
    public String getShippingLastName() {
        return shippingLastName;
    }

    /**
     * Trims and sets the last name of the recipient.
     */
    public void setShippingLastName(String shippingLastName) {
        this.shippingLastName = shippingLastName==null ? null : shippingLastName.trim();
    }

    /**
     * Gets the company name of the recipient.
     */
    public String getShippingCompanyName() {
        return shippingCompanyName;
    }

    /**
     * Trims and sets the company name of the recipient.
     */
    public void setShippingCompanyName(String shippingCompanyName) {
        this.shippingCompanyName = shippingCompanyName==null ? null : shippingCompanyName.trim();
    }

    /**
     * Gets the shipping street address (first line).
     */
    public String getShippingStreetAddress1() {
        return shippingStreetAddress1;
    }

    /**
     * Trims and sets the shipping street address (first line).
     */
    public void setShippingStreetAddress1(String shippingStreetAddress1) {
        this.shippingStreetAddress1 = shippingStreetAddress1==null ? null : shippingStreetAddress1.trim();
    }

    /**
     * Gets the shipping street address (second line).
     */
    public String getShippingStreetAddress2() {
        return shippingStreetAddress2;
    }

    /**
     * Trims and sets the shipping street address (second line).
     */
    public void setShippingStreetAddress2(String shippingStreetAddress2) {
        this.shippingStreetAddress2 = shippingStreetAddress2==null ? null : shippingStreetAddress2.trim();
    }

    /**
     * Gets the shipping city.
     */
    public String getShippingCity() {
        return shippingCity;
    }

    /**
     * Trims and sets the shipping city.
     */
    public void setShippingCity(String shippingCity) {
        this.shippingCity = shippingCity==null ? null : shippingCity.trim();
    }

    /**
     * Gets the shipping state/province/prefecture.
     */
    public String getShippingState() {
        return shippingState;
    }

    /**
     * Trims and sets the shipping state/province/prefecture.
     */
    public void setShippingState(String shippingState) {
        this.shippingState = shippingState==null ? null : shippingState.trim();
    }

    /**
     * Gets the shipping postal code.
     */
    public String getShippingPostalCode() {
        return shippingPostalCode;
    }

    /**
     * Trims and sets the shipping postal code.
     */
    public void setShippingPostalCode(String shippingPostalCode) {
        this.shippingPostalCode = shippingPostalCode==null ? null : shippingPostalCode.trim();
    }

    /**
     * Gets the shipping two-digit ISO 3166-1 alpha-2 country code.
     *
     * See {@link http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2}
     */
    public String getShippingCountryCode() {
        return shippingCountryCode;
    }

    /**
     * Trims, converts to upper case, and sets the shipping two-digit ISO 3166-1 alpha-2 country code.
     *
     * See {@link http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2}
     *
     * @throws  IllegalArgumentException  if not a two-character code (after trimming).
     */
    public void setShippingCountryCode(String shippingCountryCode, Locale userLocale) {
         if(shippingCountryCode==null) {
            this.shippingCountryCode = null;
        } else {
            shippingCountryCode = shippingCountryCode.trim().toUpperCase(Locale.ENGLISH);
            if(shippingCountryCode.length()!=2) throw new LocalizedIllegalArgumentException(userLocale, "TransactionRequest.setShippingCountryCode.shippingCountryCode.mustBe2Digits");
            this.shippingCountryCode = shippingCountryCode;
        }
   }
    
    /**
     * Gets the flag indicating if the customer will be emailed directly by the payment gateway.
     */
    public boolean getEmailCustomer() {
        return emailCustomer;
    }
    
    /**
     * Sets the flag indicating if the customer will be emailed directly by the payment gateway.
     */
    public void setEmailCustomer(boolean emailCustomer) {
        this.emailCustomer = emailCustomer;
    }
    
    /**
     * Gets the merchant email address that will be emailed a copy of the customer email directly
     * by the payment gateway.
     */
    public String getMerchantEmail() {
        return merchantEmail;
    }
    
    /**
     * Trims and sets the merchant email address that will be emailed a copy of the customer email directly
     * by the payment gateway.
     *
     * @throws  IllegalArgumentException  if the address does is not in the proper format
     */
    public void setMerchantEmail(String merchantEmail, Locale userLocale) {
        if(merchantEmail==null) {
            this.merchantEmail = null;
        } else {
            merchantEmail = merchantEmail.trim();
            if(!GenericValidator.isEmail(merchantEmail)) throw new LocalizedIllegalArgumentException(userLocale, "TransactionRequest.setMerchantEmail.merchantEmail.invalid");
            this.merchantEmail = merchantEmail;
        }
    }

    /**
     * Gets the merchant-assigned invoice number.
     */
    public String getInvoiceNumber() {
        return invoiceNumber;
    }
    
    /**
     * Sets the merchant-assigned invoice number.
     */
    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    /**
     * Gets the purchase order number.
     */
    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }
    
    /**
     * Sets the purchase order number.
     */
    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    /**
     * Gets the merchant-assigned description.
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Sets the merchant-assigned description.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
