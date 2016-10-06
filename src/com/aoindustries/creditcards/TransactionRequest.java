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
import java.math.BigDecimal;
import java.util.Currency;
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
	 * The default number of seconds for duplication detection.
	 */
	private static final int DEFAULT_DUPLICATE_WINDOW = 120;

	/**
	 * Adds-up all amounts into a total amount.
	 * @return   amount + tax + shipping + duty
	 */
	public static BigDecimal getTotalAmount(
		BigDecimal amount,
		BigDecimal taxAmount,
		BigDecimal shippingAmount,
		BigDecimal dutyAmount
	) {
		if(taxAmount!=null) amount = amount.add(taxAmount);
		if(shippingAmount!=null) amount = amount.add(shippingAmount);
		if(dutyAmount!=null) amount = amount.add(dutyAmount);
		return amount;
	}

	private boolean testMode;
	private String customerIp;
	private int duplicateWindow = DEFAULT_DUPLICATE_WINDOW;
	private String orderNumber;
	private Currency currency;
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
	public TransactionRequest() {
		setCurrency(Currency.getInstance(accessor.getMessage("TransactionRequest.currency.default")));
	}

	/**
	 * Creates a TransactionRequest providing all of the details.
	 *
	 * @throws  IllegalArgumentException  if anything not valid
	 */
	public TransactionRequest(
		boolean testMode,
		String customerIp,
		int duplicateWindow,
		String orderNumber,
		Currency currency,
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
		setCustomerIp(customerIp);
		setDuplicateWindow(duplicateWindow);
		setOrderNumber(orderNumber);
		if(currency==null) setCurrency(Currency.getInstance(accessor.getMessage("TransactionRequest.currency.default")));
		else setCurrency(currency);
		setAmount(amount);
		setTaxAmount(taxAmount);
		setTaxExempt(taxExempt);
		setShippingAmount(shippingAmount);
		setDutyAmount(dutyAmount);
		setShippingFirstName(shippingFirstName);
		setShippingLastName(shippingLastName);
		setShippingCompanyName(shippingCompanyName);
		setShippingStreetAddress1(shippingStreetAddress1);
		setShippingStreetAddress2(shippingStreetAddress2);
		setShippingCity(shippingCity);
		setShippingState(shippingState);
		setShippingPostalCode(shippingPostalCode);
		setShippingCountryCode(shippingCountryCode);
		setEmailCustomer(emailCustomer);
		setMerchantEmail(merchantEmail);
		setInvoiceNumber(invoiceNumber);
		setPurchaseOrderNumber(purchaseOrderNumber);
		setDescription(description);
	}

	@Override
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
	 * Gets the customer IP
	 */
	public String getCustomerIp() {
		return customerIp;
	}

	/**
	 * Sets the customer IP
	 */
	public void setCustomerIp(String customerIp) {
		this.customerIp = customerIp;
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
	 * Gets the currency for this transaction.
	 */
	public Currency getCurrency() {
		return currency;
	}

	/**
	 * Sets the currency code for this transaction.  The currency should be set
	 * before the monetary amounts in order to properly scale the amounts.  If
	 * the currency is reset, the amounts will be scaled.  This may result
	 * in an ArithmentException.
	 */
	public void setCurrency(Currency currency) {
		this.currency = currency;
		int scale = currency.getDefaultFractionDigits();
		if(amount!=null) amount = amount.setScale(scale);
		if(taxAmount!=null) taxAmount = taxAmount.setScale(scale);
		if(shippingAmount!=null) shippingAmount = shippingAmount.setScale(scale);
		if(dutyAmount!=null) dutyAmount = dutyAmount.setScale(scale);
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
	 * The amount is normalized to the proper number of decimal places for the selected currency.
	 *
	 * @throws  IllegalArgumentException  if amount <= 0 or is incorrectly formatted for the currency.
	 */
	public void setAmount(BigDecimal amount) throws IllegalArgumentException {
		if(amount==null) {
			this.amount = null;
		} else {
			if(amount.compareTo(BigDecimal.ZERO)<=0) throw new LocalizedIllegalArgumentException(accessor, "TransactionRequest.setAmount.amount.lessThanEqualZero");
			try {
				this.amount = amount.setScale(currency.getDefaultFractionDigits());
			} catch(ArithmeticException err) {
				throw new LocalizedIllegalArgumentException(err, accessor, "TransactionRequest.setAmount.amount.cannotNormalize");
			}
		}
	}

	/**
	 * Gets the tax amount of the transaction.
	 */
	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	/**
	 * Sets the tax amount of the transaction.
	 *
	 * The amount is normalized to the proper number of decimal places for the selected currency.
	 *
	 * @throws  IllegalArgumentException  if taxAmount < 0 or is incorrectly formatted for the currency.
	 */
	public void setTaxAmount(BigDecimal taxAmount) {
		if(taxAmount==null) {
			this.taxAmount = null;
		} else {
			if(taxAmount.compareTo(BigDecimal.ZERO)<0) throw new LocalizedIllegalArgumentException(accessor, "TransactionRequest.setTaxAmount.taxAmount.lessThanZero");
			try {
				this.taxAmount = taxAmount.setScale(currency.getDefaultFractionDigits());
			} catch(ArithmeticException err) {
				throw new LocalizedIllegalArgumentException(err, accessor, "TransactionRequest.setTaxAmount.taxAmount.cannotNormalize");
			}
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
	 * Gets the shipping amount of the transaction.
	 */
	public BigDecimal getShippingAmount() {
		return shippingAmount;
	}

	/**
	 * Sets the shipping amount of the transaction.
	 *
	 * The amount is normalized to the proper number of decimal places for the selected currency.
	 *
	 * @throws  IllegalArgumentException  if shippingAmount < 0 or is incorrectly formatted for the currency.
	 */
	public void setShippingAmount(BigDecimal shippingAmount) {
		if(shippingAmount==null) {
			this.shippingAmount = null;
		} else {
			if(shippingAmount.compareTo(BigDecimal.ZERO)<0) throw new LocalizedIllegalArgumentException(accessor, "TransactionRequest.setShippingAmount.shippingAmount.lessThanZero");
			try {
				this.shippingAmount = shippingAmount.setScale(currency.getDefaultFractionDigits());
			} catch(ArithmeticException err) {
				throw new LocalizedIllegalArgumentException(err, accessor, "TransactionRequest.setShippingAmount.shippingAmount.cannotNormalize");
			}
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
	 * The amount is normalized to the proper number of decimal places for the selected currency.
	 *
	 * @throws  IllegalArgumentException  if dutyAmount < 0 or is incorrectly formatted for the currency.
	 */
	public void setDutyAmount(BigDecimal dutyAmount) {
		if(dutyAmount==null) {
			this.dutyAmount = null;
		} else {
			if(dutyAmount.compareTo(BigDecimal.ZERO)<0) throw new LocalizedIllegalArgumentException(accessor, "TransactionRequest.setDutyAmount.dutyAmount.lessThanZero");
			try {
				this.dutyAmount = dutyAmount.setScale(currency.getDefaultFractionDigits());
			} catch(ArithmeticException err) {
				throw new LocalizedIllegalArgumentException(err, accessor, "TransactionRequest.setDutyAmount.dutyAmount.cannotNormalize");
			}
		}
	}

	/**
	 * Adds-up all amounts into a total amount.
	 * @return   amount + tax + shipping + duty
	 */
	public BigDecimal getTotalAmount() {
		return getTotalAmount(amount, taxAmount, shippingAmount, dutyAmount);
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
	 * See <a href="http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2">http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2</a>
	 */
	public String getShippingCountryCode() {
		return shippingCountryCode;
	}

	/**
	 * Trims, converts to upper case, and sets the shipping two-digit ISO 3166-1 alpha-2 country code.
	 *
	 * See <a href="http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2">http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2</a>
	 *
	 * @throws  IllegalArgumentException  if not a two-character code (after trimming).
	 */
	public void setShippingCountryCode(String shippingCountryCode) {
		 if(shippingCountryCode==null) {
			this.shippingCountryCode = null;
		} else {
			shippingCountryCode = shippingCountryCode.trim().toUpperCase(Locale.ENGLISH);
			if(shippingCountryCode.length()!=2) throw new LocalizedIllegalArgumentException(accessor, "TransactionRequest.setShippingCountryCode.shippingCountryCode.mustBe2Digits");
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
	public void setMerchantEmail(String merchantEmail) {
		if(merchantEmail==null) {
			this.merchantEmail = null;
		} else {
			merchantEmail = merchantEmail.trim();
			if(!GenericValidator.isEmail(merchantEmail)) throw new LocalizedIllegalArgumentException(accessor, "TransactionRequest.setMerchantEmail.merchantEmail.invalid");
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
