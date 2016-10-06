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
import java.util.Calendar;
import java.util.Locale;
import org.apache.commons.validator.GenericValidator;

/**
 * Encapsulates the credit card details that are sent to the bank, retrieved from the database, or manipulated in any way.
 * The credit card details include card numbers and related billing address details.
 *
 * If this card is retrieved from an encrypted/protected source, not all the information will necessarily be available.
 *
 * @author  AO Industries, Inc.
 */
public class CreditCard implements Cloneable {

	/**
	 * Only shows the first four and last four digits of a card number.
	 * If the number is <code>null</code>, returns an empty string.
	 */
	public static String maskCreditCardNumber(String cardNumber) {
		if(cardNumber==null) return "";
		cardNumber=cardNumber.trim();
		int len=cardNumber.length();
		if(len==0) return "";
		StringBuilder SB=new StringBuilder(len);
		for(int c=0;c<len;c++) {
			char ch=cardNumber.charAt(c);
			if(
				ch<'0'
				|| ch>'9'
				|| c<4
				|| c>=(len-4)
			) SB.append(cardNumber.charAt(c));
			else SB.append('X');
		}
		return SB.toString();
	}

	/**
	 * Gets the numbers out of a String.
	 */
	public static String numbersOnly(String S) {
		if(S==null) return null;
		int len = S.length();
		if(len==0) return S;
		StringBuilder SB = new StringBuilder(len);
		for(int c=0;c<len;c++) {
			char ch = S.charAt(c);
			if(ch>='0' && ch<='9') SB.append(ch);
		}
		return SB.length() == len ? S : SB.toString();
	}

	/**
	 * Gets an expiration date in MMYY format.
	 *
	 * @throws  IllegalArgumentException  if invalid date
	 */
	public static String getExpirationDateMMYY(byte month, short year) {
		if(month==-1) throw new LocalizedIllegalArgumentException(accessor, "CreditCard.setExpirationMonth.expirationMonth.invalid");
		if(year==-1) throw new LocalizedIllegalArgumentException(accessor, "CreditCard.setExpirationYear.expirationYear.invalid");
		StringBuilder SB = new StringBuilder(4);
		if(month<10) SB.append('0');
		SB.append(month);
		int modYear = year%100;
		if(modYear<10) SB.append('0');
		SB.append(modYear);
		return SB.toString();
	}

	/**
	 * Combines the first and last names into a single name String.
	 */
	public static String getFullName(String firstName, String lastName) {
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

	private String persistenceUniqueId;
	private String principalName;
	private String groupName;
	private String providerId;
	private String providerUniqueId;
	private String cardNumber;
	private String maskedCardNumber;
	private byte expirationMonth = -1;
	private short expirationYear = -1;
	private String cardCode;
	private String firstName;
	private String lastName;
	private String companyName;
	private String email;
	private String phone;
	private String fax;
	private String customerId;
	private String customerTaxId;
	private String streetAddress1;
	private String streetAddress2;
	private String city;
	private String state;
	private String postalCode;
	private String countryCode;
	private String comments;

	/**
	 * Creates an empty CreditCard.  The values should be set using the appropriate setter methods.
	 */
	public CreditCard() {
	}

	/**
	 * Creates a CreditCard providing all of the details.
	 *
	 * @throws  IllegalArgumentException  if anything not valid
	 */
	public CreditCard(
		String persistenceUniqueId,
		String principalName,
		String groupName,
		String providerId,
		String providerUniqueId,
		String cardNumber,
		String maskedCardNumber,
		byte expirationMonth,
		short expirationYear,
		String cardCode,
		String firstName,
		String lastName,
		String companyName,
		String email,
		String phone,
		String fax,
		String customerId,
		String customerTaxId,
		String streetAddress1,
		String streetAddress2,
		String city,
		String state,
		String postalCode,
		String countryCode,
		String comments
	) {
		setPersistenceUniqueId(persistenceUniqueId);
		setPrincipalName(principalName);
		setGroupName(groupName);
		setProviderId(providerId);
		setProviderUniqueId(providerUniqueId);
		setCardNumber(cardNumber);
		if(cardNumber==null) setMaskedCardNumber(maskedCardNumber);
		setExpirationMonth(expirationMonth);
		setExpirationYear(expirationYear);
		setCardCode(cardCode);
		setFirstName(firstName);
		setLastName(lastName);
		setCompanyName(companyName);
		setEmail(email);
		setPhone(phone);
		setFax(fax);
		setCustomerId(customerId);
		setCustomerTaxId(customerTaxId);
		setStreetAddress1(streetAddress1);
		setStreetAddress2(streetAddress2);
		setCity(city);
		setState(state);
		setPostalCode(postalCode);
		setCountryCode(countryCode);
		setComments(comments);
	}

	@Override
	public CreditCard clone() {
		try {
			return (CreditCard)super.clone();
		} catch(CloneNotSupportedException err) {
			throw new RuntimeException(err);
		}
	}

	/**
	 * Gets the persistence unique identifier.
	 */
	public String getPersistenceUniqueId() {
		return persistenceUniqueId;
	}

	/**
	 * Sets the persistence unique identifier.
	 */
	public void setPersistenceUniqueId(String persistenceUniqueId) {
		this.persistenceUniqueId = persistenceUniqueId;
	}

	/**
	 * Gets the name of the principal who added the card.
	 */
	public String getPrincipalName() {
		return principalName;
	}

	/**
	 * Sets the name of the principal who added the card.
	 */
	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}

	/**
	 * Gets the name of the group this card belongs to.
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * Sets the name of the group this card belongs to.
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * Gets the id of the merchant services provider that is storing this card.
	 */
	public String getProviderId() {
		return providerId;
	}

	/**
	 * Sets the id of the merchant services provider that is storing this card.
	 */
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	/**
	 * Gets the provider-specific unique id representing this card.
	 */
	public String getProviderUniqueId() {
		return providerUniqueId;
	}

	/**
	 * Sets the provider-specific unique id representing this card.
	 */
	public void setProviderUniqueId(String providerUniqueId) {
		this.providerUniqueId = providerUniqueId;
	}

	/**
	 * Gets the full credit card number.  This may return <code>null</code> if the full card number is not available.
	 */
	public String getCardNumber() {
		return cardNumber;
	}

	/**
	 * Trims and sets the full credit card number.  Also sets the masked card number if cardNumber is not null.
	 *
	 * @throws  IllegalArgumentException  if invalid credit card number
	 */
	public void setCardNumber(String cardNumber) {
		if(cardNumber!=null && cardNumber.length()>0) {
			cardNumber = numbersOnly(cardNumber);
			String numbersOnly = numbersOnly(cardNumber);
			if(
				//!"4222222222222222".equals(cardNumber)
				!GenericValidator.isCreditCard(numbersOnly)
			) throw new LocalizedIllegalArgumentException(accessor, "CreditCard.setCardNumber.cardNumber.invalid");
			String trimmed = cardNumber.trim();
			this.cardNumber = trimmed;
			this.maskedCardNumber = maskCreditCardNumber(trimmed);
		} else {
			this.cardNumber = null;
		}
	}

	/**
	 * Gets the masked card number.  This contains the first two and last four digits of the card number.
	 * This value will usually be available while the card number will is only available for new transactions.
	 */
	public String getMaskedCardNumber() {
		return maskedCardNumber;
	}

	/**
	 * Sets the masked card number.
	 *
	 * For internal API use only.
	 */
	void setMaskedCardNumber(String maskedCardNumber) {
		this.maskedCardNumber = maskedCardNumber;
	}

	/**
	 * Gets the expiration month, where 1 is January and 12 is December.
	 */
	public byte getExpirationMonth() {
		return expirationMonth;
	}

	/**
	 * Sets the expiration month, where 1 is January and 12 is December.
	 *
	 * @throws  IllegalArgumentException  if out of range.
	 */
	public void setExpirationMonth(byte expirationMonth) {
		if(expirationMonth!=-1 && (expirationMonth<1 || expirationMonth>12)) throw new LocalizedIllegalArgumentException(accessor, "CreditCard.setExpirationMonth.expirationMonth.invalid");
		this.expirationMonth = expirationMonth;
	}

	/**
	 * Gets the expiration year, such as <code>2007</code>.
	 */
	public short getExpirationYear() {
		return expirationYear;
	}

	/**
	 * Sets the expiration year, such as <code>2007</code>.
	 * It also accepts values 0 <= year <= 99.  These values will be automatically
	 * added to the current century.
	 *
	 * @throws  IllegalArgumentException  if the resolved year is < 1977 or > (current year + 12)
	 */
	public void setExpirationYear(short expirationYear) {
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		if(expirationYear>=0 && expirationYear<=99) {
			expirationYear = (short)((currentYear/100)*100 + expirationYear);
		}
		if(expirationYear!=-1 && (expirationYear<1977 || expirationYear>(currentYear+12))) throw new LocalizedIllegalArgumentException(accessor, "CreditCard.setExpirationYear.expirationYear.invalid");
		this.expirationYear = expirationYear;
	}

	/**
	 * Gets the expiration date in MMYY format.
	 *
	 * @throws  IllegalArgumentException  if invalid date
	 */
	public String getExpirationDateMMYY() {
		return getExpirationDateMMYY(getExpirationMonth(), getExpirationYear());
	}

	/**
	 * Gets the three or four digit card security code.  This value is never stored and is therefore only
	 * available for a new card.
	 */
	public String getCardCode() {
		return cardCode;
	}

	/**
	 * Checks a card code format.
	 * 
	 * @param cardCode The card code to check
	 *
	 * @return The card code to use
	 * 
	 * @throws LocalizedIllegalArgumentException if card code invalid
	 */
	public static String validateCardCode(String cardCode) throws LocalizedIllegalArgumentException {
		if(cardCode==null) return null;
		cardCode = cardCode.trim();
		if(cardCode.length()==0) return null;

		if(cardCode.length()!=3 && cardCode.length()!=4) throw new LocalizedIllegalArgumentException(accessor, "CreditCard.validateCardCode.cardCode.mustBe3Or4Digits");
		// Each digit must be a number
		for(int c=0;c<cardCode.length();c++) {
			char ch = cardCode.charAt(c);
			if(ch<'0' || ch>'9') throw new LocalizedIllegalArgumentException(accessor, "CreditCard.validateCardCode.cardCode.mustBeDigits");
		}
		return cardCode;
	}

	/**
	 * Trims and sets the three or four digit card security code.
	 *
	 * @throws  IllegalArgumentException  if the value is not either <code>null</code> or purely digits (after trimming) and 3 or 4 digits long.
	 */
	public void setCardCode(String cardCode) throws LocalizedIllegalArgumentException {
		this.cardCode = validateCardCode(cardCode);
	}

	/**
	 * Gets the first name of the card holder.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Trims and sets the first name of the card holder.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName==null || firstName.length()==0 ? null : firstName.trim();
	}

	/**
	 * Gets the last name of the card holder.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Trims and sets the last name of the card holder.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName==null || lastName.length()==0 ? null : lastName.trim();
	}

	/**
	 * Gets the company name of the card holder.
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * Trims and sets the company name of the card holder.
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName==null || companyName.length()==0 ? null : companyName.trim();
	}

	/**
	 * Gets the card holder's email address.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Trims and sets the card holder's email address.
	 *
	 * @throws  IllegalArgumentException  if the address does is not in the proper format
	 */
	public void setEmail(String email) {
		if(email==null || email.length()==0) {
			this.email = null;
		} else {
			email = email.trim();
			if(!GenericValidator.isEmail(email)) throw new LocalizedIllegalArgumentException(accessor, "CreditCard.setEmail.email.invalid");
			this.email = email;
		}
	}

	/**
	 * Gets the card holder's telephone number.
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Trims and sets the card holder's telephone number.
	 */
	public void setPhone(String phone) {
		this.phone = phone==null || phone.length()==0 ? null : phone.trim();
	}

	/**
	 * Gets the card holder's fax number.
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * Trims and sets the card holder's fax number.
	 */
	public void setFax(String fax) {
		this.fax = fax==null || fax.length()==0 ? null : fax.trim();
	}

	/**
	 * Gets the merchant-specific unique customer ID.
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * Sets the merchant-specific unique customer ID.
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId==null || customerId.length()==0 ? null : customerId;
	}

	/**
	 * Gets the customer SSN or Tax ID.
	 */
	public String getCustomerTaxId() {
		return customerTaxId;
	}

	/**
	 * Trims and sets the merchant-specific unique customer ID, removes any spaces and hyphens.
	 *
	 * @throws  IllegalArgumentException  if not a nine digit number after trimming.
	 */
	public void setCustomerTaxId(String customerTaxId) {
		if(customerTaxId==null || customerTaxId.length()==0) {
			this.customerTaxId = null;
		} else {
			customerTaxId = numbersOnly(customerTaxId.trim());
			this.customerTaxId = customerTaxId;
		}
	}

	/**
	 * Gets the card holder's street address (first line).
	 */
	public String getStreetAddress1() {
		return streetAddress1;
	}

	/**
	 * Trims and sets the card holder's street address (first line).
	 */
	public void setStreetAddress1(String streetAddress1) {
		this.streetAddress1 = streetAddress1==null || streetAddress1.length()==0 ? null : streetAddress1.trim();
	}

	/**
	 * Gets the card holder's street address (second line).
	 */
	public String getStreetAddress2() {
		return streetAddress2;
	}

	/**
	 * Trims and sets the card holder's street address (second line).
	 */
	public void setStreetAddress2(String streetAddress2) {
		this.streetAddress2 = streetAddress2==null || streetAddress2.length()==0 ? null : streetAddress2.trim();
	}

	/**
	 * Gets the card holder's city.
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Trims and sets the card holder's city.
	 */
	public void setCity(String city) {
		this.city = city==null || city.length()==0 ? null : city.trim();
	}

	/**
	 * Gets the card holder's state/province/prefecture.
	 */
	public String getState() {
		return state;
	}

	/**
	 * Trims and sets the card holder's state/province/prefecture.
	 */
	public void setState(String state) {
		this.state = state==null || state.length()==0 ? null : state.trim();
	}

	/**
	 * Gets the card holder's postal code.
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * Trims and sets the card holder's postal code.
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode==null || postalCode.length()==0 ? null : postalCode.trim();
	}

	/**
	 * Gets the card holder's two-digit ISO 3166-1 alpha-2 country code.
	 *
	 * See <a href="http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2">http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2</a>
	 */
	public String getCountryCode() {
		return countryCode;
	}

	/**
	 * Trims, converts to upper case, and sets the card holder's two-digit ISO 3166-1 alpha-2 country code.
	 *
	 * See <a href="http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2">http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2</a>
	 *
	 * @throws  IllegalArgumentException  if not a two-character code (after trimming).
	 */
	public void setCountryCode(String countryCode) {
		if(countryCode==null || countryCode.length()==0) {
			this.countryCode = null;
		} else {
			countryCode = countryCode.trim().toUpperCase(Locale.ENGLISH);
			if(countryCode.length()!=2) throw new LocalizedIllegalArgumentException(accessor, "CreditCard.setCountryCode.countryCode.mustBe2Digits");
			this.countryCode = countryCode;
		}
	}

	/**
	 * Gets the comments associated with this card.
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * Sets the comments associated with this card.
	 */
	public void setComments(String comments) {
		this.comments = comments==null || comments.length()==0 ? null : comments;
	}
}
