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
package com.aoindustries.creditcards.sagePayments;

import static com.aoindustries.creditcards.ApplicationResourcesAccessor.accessor;
import com.aoindustries.creditcards.AuthorizationResult;
import com.aoindustries.creditcards.CaptureResult;
import com.aoindustries.creditcards.CreditCard;
import com.aoindustries.creditcards.CreditResult;
import com.aoindustries.io.LocalizedIOException;
import com.aoindustries.creditcards.MerchantServicesProvider;
import com.aoindustries.creditcards.SaleResult;
import com.aoindustries.creditcards.Transaction;
import com.aoindustries.creditcards.TransactionRequest;
import com.aoindustries.creditcards.TransactionResult;
import com.aoindustries.creditcards.VoidResult;
import com.aoindustries.creditcards.sagePayments.transaction_processing.TRANSACTION_PROCESSINGLocator;
import com.aoindustries.creditcards.sagePayments.wsVault.WsVaultLocator;
import com.aoindustries.creditcards.sagePayments.wsVaultBankcard.WsVaultBankcardLocator;
import com.aoindustries.lang.NotImplementedException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.rpc.ServiceException;
import org.apache.axis.message.MessageElement;
import org.w3c.dom.Node;

/**
 * Provider for <a href="http://www.sagepayments.com/">Sage Payment Solutions</a><br>
 * <br>
 * Configuration parameters:
 * <ol>
 *   <li>merchantId - Merchant ID</li>
 *   <li>merchantKey - Merchant Key</li>
 * </ol>
 *
 * Note: Commands to build .java files from WSDL:
 * <ol>
 *   <li>cd /path/to/project/src</li>
 *   <li>java -classpath ../lib/saaj.jar:../lib/jaxrpc.jar:../lib/commons-discovery-0.2.jar:../lib/commons-logging-1.0.4.jar:../lib/wsdl4j-1.5.1.jar:../lib/axis.jar org.apache.axis.wsdl.WSDL2Java --package com.aoindustries.creditcards.sagePayments.wsVault https://www.sagepayments.net/web_services/wsVault/wsVault.asmx?WSDL</li>
 *   <li>java -classpath ../lib/saaj.jar:../lib/jaxrpc.jar:../lib/commons-discovery-0.2.jar:../lib/commons-logging-1.0.4.jar:../lib/wsdl4j-1.5.1.jar:../lib/axis.jar org.apache.axis.wsdl.WSDL2Java --package com.aoindustries.creditcards.sagePayments.transaction_processing https://www.sagepayments.net/web_services/vterm_extensions/transaction_processing.asmx?WSDL</li>
 *   <li>java -classpath ../lib/saaj.jar:../lib/jaxrpc.jar:../lib/commons-discovery-0.2.jar:../lib/commons-logging-1.0.4.jar:../lib/wsdl4j-1.5.1.jar:../lib/axis.jar org.apache.axis.wsdl.WSDL2Java --package com.aoindustries.creditcards.sagePayments.wsVaultBankcard https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard.asmx?WSDL</li>
 * </ol>
 *
 * TODO: invalid expiration with valid CVV causes CVV2 failure.  I notified Sage on 2007-06-20
 *       David Landrum ext 3025
 * TODO: Check for test mode and don't allow transaction because test mode not supported (all calls)
 *
 * @author  AO Industries, Inc.
 */
public class SagePayments implements MerchantServicesProvider {

	private static final Logger logger = Logger.getLogger(SagePayments.class.getName());

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

	/**
	 * Returns "" is the String is null.
	 */
	protected static String emptyStringIfNull(String S) {
		return S==null ? "" : S;
	}

	private static String getFirstChildNodeValue(Node child) {
		Node firstChild = child.getFirstChild();
		if(firstChild==null) return null;
		return firstChild.getNodeValue();
	}

	private static TransactionResult.ErrorCode convertErrorCode(String code) {
		if(code==null) return null;
		// Not documented, from testing
		if("000014".equals(code)) return TransactionResult.ErrorCode.INVALID_CARD_NUMBER;
		if("000015".equals(code)) return TransactionResult.ErrorCode.INVALID_CARD_NUMBER; // NO SUCH ISSUER (when using 4111 1111 1111 1111)
		// Documented
		if("000000".equals(code)) return TransactionResult.ErrorCode.ERROR_TRY_AGAIN;
		if("900000".equals(code)) return TransactionResult.ErrorCode.INVALID_ORDER_NUMBER;
		if("900001".equals(code)) return TransactionResult.ErrorCode.INVALID_CARD_NAME;
		if("900002".equals(code)) return TransactionResult.ErrorCode.INVALID_CARD_ADDRESS;
		if("900003".equals(code)) return TransactionResult.ErrorCode.INVALID_CARD_CITY;
		if("900004".equals(code)) return TransactionResult.ErrorCode.INVALID_CARD_STATE;
		if("900005".equals(code)) return TransactionResult.ErrorCode.INVALID_CARD_POSTAL_CODE;
		if("900006".equals(code)) return TransactionResult.ErrorCode.INVALID_CARD_COUNTRY_CODE;
		if("900007".equals(code)) return TransactionResult.ErrorCode.INVALID_CARD_PHONE;
		if("900008".equals(code)) return TransactionResult.ErrorCode.INVALID_CARD_FAX;
		if("900009".equals(code)) return TransactionResult.ErrorCode.INVALID_CARD_EMAIL;
		if("900010".equals(code)) return TransactionResult.ErrorCode.INVALID_SHIPPING_NAME;
		if("900011".equals(code)) return TransactionResult.ErrorCode.INVALID_SHIPPING_ADDRESS;
		if("900012".equals(code)) return TransactionResult.ErrorCode.INVALID_SHIPPING_CITY;
		if("900013".equals(code)) return TransactionResult.ErrorCode.INVALID_SHIPPING_STATE;
		if("900014".equals(code)) return TransactionResult.ErrorCode.INVALID_SHIPPING_POSTAL_CODE;
		if("900015".equals(code)) return TransactionResult.ErrorCode.INVALID_SHIPPING_COUNTRY_CODE;
		if("900016".equals(code)) return TransactionResult.ErrorCode.INVALID_CARD_NUMBER;
		if("900017".equals(code)) return TransactionResult.ErrorCode.INVALID_EXPIRATION_DATE;
		if("900018".equals(code)) return TransactionResult.ErrorCode.INVALID_CARD_CODE;
		if("900019".equals(code)) return TransactionResult.ErrorCode.INVALID_AMOUNT;
		if("900020".equals(code)) return TransactionResult.ErrorCode.ERROR_TRY_AGAIN;
		if("900021".equals(code)) return TransactionResult.ErrorCode.ERROR_TRY_AGAIN;
		if("900022".equals(code)) return TransactionResult.ErrorCode.ERROR_TRY_AGAIN;
		if("900023".equals(code)) return TransactionResult.ErrorCode.ERROR_TRY_AGAIN;
		if("900024".equals(code)) return TransactionResult.ErrorCode.ERROR_TRY_AGAIN;
		if("900025".equals(code)) return TransactionResult.ErrorCode.ERROR_TRY_AGAIN;
		if("910000".equals(code)) return TransactionResult.ErrorCode.PROVIDER_CONFIGURATION_ERROR;
		if("910001".equals(code)) return TransactionResult.ErrorCode.CARD_TYPE_NOT_SUPPORTED;
		if("910002".equals(code)) return TransactionResult.ErrorCode.CARD_TYPE_NOT_SUPPORTED;
		if("910003".equals(code)) return TransactionResult.ErrorCode.CARD_TYPE_NOT_SUPPORTED;
		if("910004".equals(code)) return TransactionResult.ErrorCode.CARD_TYPE_NOT_SUPPORTED;
		if("910005".equals(code)) return TransactionResult.ErrorCode.CARD_TYPE_NOT_SUPPORTED;
		if("911911".equals(code)) return TransactionResult.ErrorCode.GATEWAY_SECURITY_GUIDELINES_NOT_MET;
		if("920000".equals(code)) return TransactionResult.ErrorCode.TRANSACTION_NOT_FOUND;
		if("920001".equals(code)) return TransactionResult.ErrorCode.SUM_OF_CREDITS_TOO_HIGH;
		if("920002".equals(code)) return TransactionResult.ErrorCode.ERROR_TRY_AGAIN;
		if("999999".equals(code)) return TransactionResult.ErrorCode.ERROR_TRY_AGAIN;
		return TransactionResult.ErrorCode.UNKNOWN;
	}

	private final String providerId;
	private final String merchantId;
	private final String merchantKey;

	public SagePayments(String providerId, String merchantId, String merchantKey) {
		this.providerId = providerId;
		this.merchantId = merchantId;
		this.merchantKey = merchantKey;
	}

	@Override
	public String getProviderId() {
		return providerId;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public String getMerchantKey() {
		return merchantKey;
	}

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
		// Only supports USD
		if(!transactionRequest.getCurrency().getCurrencyCode().equals("USD")) {
			// The default locale is used because that represents the locale of the system admin, and they are the ones who need to
			// use this message (processor-specific, behind-the-scenes value)
			String message = accessor.getMessage("TransactionRequest.currency.onlyOneSupported", "USD");
			return new AuthorizationResult(
				getProviderId(),
				TransactionResult.CommunicationResult.LOCAL_ERROR,
				TransactionResult.ErrorCode.CURRENCY_NOT_SUPPORTED.name(),
				TransactionResult.ErrorCode.CURRENCY_NOT_SUPPORTED,
				message,
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
		try {
			String approvalIndicator = null;
			String code = null;
			String message = null;
			//String frontEndIndicator = null;
			String cvvIndicator = null;
			String avsIndicator = null;
			String riskIndicator = null;
			String reference = null;
			String orderNumber = null;

			{
				MessageElement[] results;
				if(creditCard.getProviderUniqueId() != null) {
					// Is a stored card
					//System.out.println("sale, creditCard.providerUniqueId="+creditCard.getProviderUniqueId());
					results =
						capture
						? new WsVaultBankcardLocator().getwsVaultBankcardSoap().VAULT_BANKCARD_SALE(
							emptyStringIfNull(merchantId),
							emptyStringIfNull(merchantKey),
							emptyStringIfNull(CreditCard.getFullName(creditCard.getFirstName(), creditCard.getLastName())),
							emptyStringIfNull(getStreetAddress(creditCard.getStreetAddress1(), creditCard.getStreetAddress2())),
							emptyStringIfNull(creditCard.getCity()),
							emptyStringIfNull(creditCard.getState()),
							emptyStringIfNull(creditCard.getPostalCode()),
							emptyStringIfNull(creditCard.getCountryCode()),
							emptyStringIfNull(creditCard.getEmail()),
							emptyStringIfNull(creditCard.getProviderUniqueId()),
							emptyStringIfNull(null),
							// TODO: Should amount be the total, or just the part before adding shipping, tax, duty???
							emptyStringIfNull(transactionRequest.getAmount().toString()),
							emptyStringIfNull(transactionRequest.getShippingAmount()==null ? null : transactionRequest.getShippingAmount().toString()),
							emptyStringIfNull(transactionRequest.getTaxAmount()==null ? null : transactionRequest.getTaxAmount().toString()),
							emptyStringIfNull(transactionRequest.getOrderNumber()),
							emptyStringIfNull(creditCard.getPhone()),
							emptyStringIfNull(creditCard.getFax()),
							emptyStringIfNull(CreditCard.getFullName(transactionRequest.getShippingFirstName(), transactionRequest.getShippingLastName())),
							emptyStringIfNull(getStreetAddress(transactionRequest.getShippingStreetAddress1(), transactionRequest.getShippingStreetAddress2())),
							emptyStringIfNull(transactionRequest.getShippingCity()),
							emptyStringIfNull(transactionRequest.getShippingState()),
							emptyStringIfNull(transactionRequest.getShippingPostalCode()),
							emptyStringIfNull(transactionRequest.getShippingCountryCode())
						).get_any()
						: new WsVaultBankcardLocator().getwsVaultBankcardSoap().VAULT_BANKCARD_AUTHONLY(
							emptyStringIfNull(merchantId),
							emptyStringIfNull(merchantKey),
							emptyStringIfNull(CreditCard.getFullName(creditCard.getFirstName(), creditCard.getLastName())),
							emptyStringIfNull(getStreetAddress(creditCard.getStreetAddress1(), creditCard.getStreetAddress2())),
							emptyStringIfNull(creditCard.getCity()),
							emptyStringIfNull(creditCard.getState()),
							emptyStringIfNull(creditCard.getPostalCode()),
							emptyStringIfNull(creditCard.getCountryCode()),
							emptyStringIfNull(creditCard.getEmail()),
							emptyStringIfNull(creditCard.getProviderUniqueId()),
							emptyStringIfNull(null),
							// TODO: Should amount be the total, or just the part before adding shipping, tax, duty???
							emptyStringIfNull(transactionRequest.getAmount().toString()),
							emptyStringIfNull(transactionRequest.getShippingAmount()==null ? null : transactionRequest.getShippingAmount().toString()),
							emptyStringIfNull(transactionRequest.getTaxAmount()==null ? null : transactionRequest.getTaxAmount().toString()),
							emptyStringIfNull(transactionRequest.getOrderNumber()),
							emptyStringIfNull(creditCard.getPhone()),
							emptyStringIfNull(creditCard.getFax()),
							emptyStringIfNull(CreditCard.getFullName(transactionRequest.getShippingFirstName(), transactionRequest.getShippingLastName())),
							emptyStringIfNull(getStreetAddress(transactionRequest.getShippingStreetAddress1(), transactionRequest.getShippingStreetAddress2())),
							emptyStringIfNull(transactionRequest.getShippingCity()),
							emptyStringIfNull(transactionRequest.getShippingState()),
							emptyStringIfNull(transactionRequest.getShippingPostalCode()),
							emptyStringIfNull(transactionRequest.getShippingCountryCode())
						).get_any()
					;
				} else {
					// Is a new card
					results =
						capture
						 ? new TRANSACTION_PROCESSINGLocator().getTRANSACTION_PROCESSINGSoap().BANKCARD_SALE(
							emptyStringIfNull(merchantId),
							emptyStringIfNull(merchantKey),
							emptyStringIfNull(CreditCard.getFullName(creditCard.getFirstName(), creditCard.getLastName())),
							emptyStringIfNull(getStreetAddress(creditCard.getStreetAddress1(), creditCard.getStreetAddress2())),
							emptyStringIfNull(creditCard.getCity()),
							emptyStringIfNull(creditCard.getState()),
							emptyStringIfNull(creditCard.getPostalCode()),
							emptyStringIfNull(creditCard.getCountryCode()),
							emptyStringIfNull(creditCard.getEmail()),
							emptyStringIfNull(creditCard.getCardNumber()),
							emptyStringIfNull(creditCard.getExpirationDateMMYY()),
							emptyStringIfNull(creditCard.getCardCode()),
							emptyStringIfNull(null),
							// TODO: Should amount be the total, or just the part before adding shipping, tax, duty???
							emptyStringIfNull(transactionRequest.getAmount().toString()),
							emptyStringIfNull(transactionRequest.getShippingAmount()==null ? null : transactionRequest.getShippingAmount().toString()),
							emptyStringIfNull(transactionRequest.getTaxAmount()==null ? null : transactionRequest.getTaxAmount().toString()),
							emptyStringIfNull(transactionRequest.getOrderNumber()),
							emptyStringIfNull(creditCard.getPhone()),
							emptyStringIfNull(creditCard.getFax()),
							emptyStringIfNull(CreditCard.getFullName(transactionRequest.getShippingFirstName(), transactionRequest.getShippingLastName())),
							emptyStringIfNull(getStreetAddress(transactionRequest.getShippingStreetAddress1(), transactionRequest.getShippingStreetAddress2())),
							emptyStringIfNull(transactionRequest.getShippingCity()),
							emptyStringIfNull(transactionRequest.getShippingState()),
							emptyStringIfNull(transactionRequest.getShippingPostalCode()),
							emptyStringIfNull(transactionRequest.getShippingCountryCode())
						).get_any()
						: new TRANSACTION_PROCESSINGLocator().getTRANSACTION_PROCESSINGSoap().BANKCARD_AUTHONLY(
							emptyStringIfNull(merchantId),
							emptyStringIfNull(merchantKey),
							emptyStringIfNull(CreditCard.getFullName(creditCard.getFirstName(), creditCard.getLastName())),
							emptyStringIfNull(getStreetAddress(creditCard.getStreetAddress1(), creditCard.getStreetAddress2())),
							emptyStringIfNull(creditCard.getCity()),
							emptyStringIfNull(creditCard.getState()),
							emptyStringIfNull(creditCard.getPostalCode()),
							emptyStringIfNull(creditCard.getCountryCode()),
							emptyStringIfNull(creditCard.getEmail()),
							emptyStringIfNull(creditCard.getCardNumber()),
							emptyStringIfNull(creditCard.getExpirationDateMMYY()),
							emptyStringIfNull(creditCard.getCardCode()),
							emptyStringIfNull(null),
							// TODO: Should amount be the total, or just the part before adding shipping, tax, duty???
							emptyStringIfNull(transactionRequest.getAmount().toString()),
							emptyStringIfNull(transactionRequest.getShippingAmount()==null ? null : transactionRequest.getShippingAmount().toString()),
							emptyStringIfNull(transactionRequest.getTaxAmount()==null ? null : transactionRequest.getTaxAmount().toString()),
							emptyStringIfNull(transactionRequest.getOrderNumber()),
							emptyStringIfNull(creditCard.getPhone()),
							emptyStringIfNull(creditCard.getFax()),
							emptyStringIfNull(CreditCard.getFullName(transactionRequest.getShippingFirstName(), transactionRequest.getShippingLastName())),
							emptyStringIfNull(getStreetAddress(transactionRequest.getShippingStreetAddress1(), transactionRequest.getShippingStreetAddress2())),
							emptyStringIfNull(transactionRequest.getShippingCity()),
							emptyStringIfNull(transactionRequest.getShippingState()),
							emptyStringIfNull(transactionRequest.getShippingPostalCode()),
							emptyStringIfNull(transactionRequest.getShippingCountryCode())
						).get_any()
					;
				}

				Node child = null;
				MessageElement me = results[results.length-1];
				if(me!=null) {
					Node meChild = me.getFirstChild();
					if(meChild!=null) {
						Node table1 = meChild.getFirstChild();
						if(table1!=null) child = table1.getFirstChild();
					}
				}
				while(child!=null) {
					if("APPROVAL_INDICATOR".equals(child.getNodeName())) approvalIndicator = getFirstChildNodeValue(child);
					if("CODE".equals(child.getNodeName())) code = getFirstChildNodeValue(child);
					if("MESSAGE".equals(child.getNodeName())) message = getFirstChildNodeValue(child);
					//if("FRONT_END_INDICATOR".equals(child.getNodeName())) frontEndIndicator = getFirstChildNodeValue(child);
					if("CVV_INDICATOR".equals(child.getNodeName())) cvvIndicator = getFirstChildNodeValue(child);
					if("AVS_INDICATOR".equals(child.getNodeName())) avsIndicator = getFirstChildNodeValue(child);
					if("RISK_INDICATOR".equals(child.getNodeName())) riskIndicator = getFirstChildNodeValue(child);
					if("REFERENCE".equals(child.getNodeName())) reference = getFirstChildNodeValue(child);
					if("ORDER_NUMBER".equals(child.getNodeName())) orderNumber = getFirstChildNodeValue(child);
					child = child.getNextSibling();
				}

				//System.out.println("approvalIndicator="+approvalIndicator);
				//System.out.println("code="+code);
				//System.out.println("message="+message);
				//System.out.println("frontEndIndicator="+frontEndIndicator);
				//System.out.println("cvvIndicator="+cvvIndicator);
				//System.out.println("avsIndicator="+avsIndicator);
				//System.out.println("riskIndicator="+riskIndicator);
				//System.out.println("reference="+reference);
				//System.out.println("orderNumber="+orderNumber);
			}

			// Correct output for CVV2 failure not setting cvvIndicator and setting error instead
			boolean didCvvFix = false;
			if("P".equals(cvvIndicator) && "0000N7".equals(code) && "CVV2 MISMATCH".equals(message)) {
				code = null;
				cvvIndicator = "N";
				didCvvFix = true;
			} else if("P".equals(cvvIndicator) && (creditCard.getCardCode()==null || creditCard.getCardCode().length()==0)) {
				// Correct when no CardCode is provided and it returns "P"
				cvvIndicator = "S";
			}

			String providerUniqueId = "0000000000".equals(reference) || "".equals(reference) ? null : reference;
			String providerApprovalResult = approvalIndicator;
			String providerReviewReason = null;
			AuthorizationResult.ReviewReason reviewReason = null;
			// CVV
			String providerCvvResult = cvvIndicator;
			AuthorizationResult.CvvResult cvvResult;
			if("M".equals(cvvIndicator)) cvvResult = AuthorizationResult.CvvResult.MATCH;
			else if("N".equals(cvvIndicator)) cvvResult = AuthorizationResult.CvvResult.NO_MATCH;
			else if("P".equals(cvvIndicator)) cvvResult = AuthorizationResult.CvvResult.NOT_PROCESSED;
			else if("S".equals(cvvIndicator)) cvvResult = AuthorizationResult.CvvResult.CVV2_NOT_PROVIDED_BY_MERCHANT;
			else if("U".equals(cvvIndicator)) cvvResult = AuthorizationResult.CvvResult.NOT_SUPPORTED_BY_ISSUER;
			else if(cvvIndicator==null || "".equals(cvvIndicator)) cvvResult = null;
			else cvvResult = AuthorizationResult.CvvResult.UNKNOWN;
			// AVS
			String providerAvsResult = avsIndicator;
			AuthorizationResult.AvsResult avsResult;
			if("X".equals(avsIndicator)) avsResult = AuthorizationResult.AvsResult.ADDRESS_Y_ZIP_9;
			else if("Y".equals(avsIndicator)) avsResult = AuthorizationResult.AvsResult.ADDRESS_Y_ZIP_5;
			else if("A".equals(avsIndicator)) avsResult = AuthorizationResult.AvsResult.ADDRESS_Y_ZIP_N;
			else if("W".equals(avsIndicator)) avsResult = AuthorizationResult.AvsResult.ADDRESS_N_ZIP_9;
			else if("Z".equals(avsIndicator)) avsResult = AuthorizationResult.AvsResult.ADDRESS_N_ZIP_5;
			else if("N".equals(avsIndicator)) avsResult = AuthorizationResult.AvsResult.ADDRESS_N_ZIP_N;
			else if("U".equals(avsIndicator)) avsResult = AuthorizationResult.AvsResult.UNAVAILABLE;
			else if("R".equals(avsIndicator)) avsResult = AuthorizationResult.AvsResult.RETRY;
			else if("E".equals(avsIndicator)) avsResult = AuthorizationResult.AvsResult.ERROR;
			else if("S".equals(avsIndicator)) avsResult = AuthorizationResult.AvsResult.SERVICE_NOT_SUPPORTED;
			else if("".equals(avsIndicator)) avsResult = AuthorizationResult.AvsResult.SERVICE_NOT_SUPPORTED;
			else if("0".equals(avsIndicator) || avsIndicator==null) avsResult = null;
			else avsResult = AuthorizationResult.AvsResult.UNKNOWN;
			// Decline Reason
			String providerDeclineReason = riskIndicator;
			AuthorizationResult.DeclineReason declineReason;
			// The 000054 is undocumented
			if("000054".equals(code)) declineReason = AuthorizationResult.DeclineReason.EXPIRED_CARD;
			else if(riskIndicator==null || "".equals(riskIndicator) || "00".equals(riskIndicator)) declineReason = null;
			else if("01".equals(riskIndicator)) declineReason = AuthorizationResult.DeclineReason.MAX_SALE_EXCEEDED;
			else if("02".equals(riskIndicator)) declineReason = AuthorizationResult.DeclineReason.MIN_SALE_NOT_MET;
			else if("03".equals(riskIndicator)) declineReason = AuthorizationResult.DeclineReason.VOLUME_EXCEEDED_1_DAY;
			else if("04".equals(riskIndicator)) declineReason = AuthorizationResult.DeclineReason.USAGE_EXCEEDED_1_DAY;
			else if("05".equals(riskIndicator)) declineReason = AuthorizationResult.DeclineReason.VOLUME_EXCEEDED_3_DAYS;
			else if("06".equals(riskIndicator)) declineReason = AuthorizationResult.DeclineReason.USAGE_EXCEEDED_3_DAYS;
			else if("07".equals(riskIndicator)) declineReason = AuthorizationResult.DeclineReason.VOLUME_EXCEEDED_15_DAYS;
			else if("08".equals(riskIndicator)) declineReason = AuthorizationResult.DeclineReason.USAGE_EXCEEDED_15_DAYS;
			else if("09".equals(riskIndicator)) declineReason = AuthorizationResult.DeclineReason.VOLUME_EXCEEDED_30_DAYS;
			else if("10".equals(riskIndicator)) declineReason = AuthorizationResult.DeclineReason.USAGE_EXCEEDED_30_DAYS;
			else if("11".equals(riskIndicator)) declineReason = AuthorizationResult.DeclineReason.STOLEN_OR_LOST_CARD;
			else if("12".equals(riskIndicator)) declineReason = AuthorizationResult.DeclineReason.AVS_FAILURE;
			else declineReason = AuthorizationResult.DeclineReason.UNKNOWN;
			TransactionResult.CommunicationResult communicationResult;
			String providerErrorCode;
			TransactionResult.ErrorCode errorCode;
			String providerErrorMessage;
			AuthorizationResult.ApprovalResult approvalResult;
			String approvalCode;
			if("A".equals(approvalIndicator)) {
				// Approved
				communicationResult = TransactionResult.CommunicationResult.SUCCESS;
				providerErrorCode = null;
				errorCode = null;
				providerErrorMessage = null;
				approvalResult = AuthorizationResult.ApprovalResult.APPROVED;
				approvalCode = code;
			} else if("E".equals(approvalIndicator) || "X".equals(approvalIndicator)) {
				if("E".equals(approvalIndicator) && didCvvFix) {
					// Gateway Error
					communicationResult = TransactionResult.CommunicationResult.SUCCESS;
					providerErrorCode = code;
					errorCode = null;
					providerErrorMessage = message;
					approvalResult = AuthorizationResult.ApprovalResult.DECLINED;
					approvalCode = null;
					if(declineReason==null) declineReason = AuthorizationResult.DeclineReason.CVV2_MISMATCH;
				} else {
					// Gateway Error
					communicationResult = declineReason==null ? TransactionResult.CommunicationResult.GATEWAY_ERROR : TransactionResult.CommunicationResult.SUCCESS;
					providerErrorCode = code;
					errorCode = declineReason==null ? convertErrorCode(code) : null;
					providerErrorMessage = message;
					approvalResult = declineReason==null ? null : AuthorizationResult.ApprovalResult.DECLINED;
					approvalCode = null;
				}
			} else {
				// Unknown response
				communicationResult = TransactionResult.CommunicationResult.LOCAL_ERROR;
				providerErrorCode = code;
				errorCode = convertErrorCode(code);
				providerErrorMessage = message;
				approvalResult = null;
				approvalCode = null;
			}
			return new AuthorizationResult(
				getProviderId(),
				communicationResult,
				providerErrorCode,
				errorCode,
				providerErrorMessage,
				providerUniqueId,
				providerApprovalResult,
				approvalResult,
				providerDeclineReason,
				declineReason,
				providerReviewReason,
				reviewReason,
				providerCvvResult,
				cvvResult,
				providerAvsResult,
				avsResult,
				approvalCode
			);
		} catch(ServiceException err) {
			logger.log(Level.SEVERE, null, err);
			return new AuthorizationResult(
				getProviderId(),
				TransactionResult.CommunicationResult.LOCAL_ERROR,
				err.getClass().getName(),
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
		} catch(RemoteException err) {
			logger.log(Level.SEVERE, null, err);
			return new AuthorizationResult(
				getProviderId(),
				TransactionResult.CommunicationResult.IO_ERROR,
				err.getClass().getName(),
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
	}

	@Override
	public CaptureResult capture(AuthorizationResult authorizationResult) {
		throw new NotImplementedException();
	}

	@Override
	public VoidResult voidTransaction(Transaction transaction) {
		try {
			String approvalIndicator = null;
			String code = null;
			String message = null;
			//String frontEndIndicator = null;
			String cvvIndicator = null;
			String avsIndicator = null;
			String riskIndicator = null;
			String reference = null;
			String orderNumber = null;

			{
				MessageElement[] results = new TRANSACTION_PROCESSINGLocator().getTRANSACTION_PROCESSINGSoap().BANKCARD_VOID(
					emptyStringIfNull(merchantId),
					emptyStringIfNull(merchantKey),
					emptyStringIfNull(transaction.getAuthorizationResult().getProviderUniqueId())
				).get_any();

				Node table1 = results[results.length-1].getFirstChild().getFirstChild();
				Node child = table1.getFirstChild();
				while(child!=null) {
					if("APPROVAL_INDICATOR".equals(child.getNodeName())) approvalIndicator = getFirstChildNodeValue(child);
					if("CODE".equals(child.getNodeName())) code = getFirstChildNodeValue(child);
					if("MESSAGE".equals(child.getNodeName())) message = getFirstChildNodeValue(child);
					//if("FRONT_END_INDICATOR".equals(child.getNodeName())) frontEndIndicator = getFirstChildNodeValue(child);
					if("CVV_INDICATOR".equals(child.getNodeName())) cvvIndicator = getFirstChildNodeValue(child);
					if("AVS_INDICATOR".equals(child.getNodeName())) avsIndicator = getFirstChildNodeValue(child);
					if("RISK_INDICATOR".equals(child.getNodeName())) riskIndicator = getFirstChildNodeValue(child);
					if("REFERENCE".equals(child.getNodeName())) reference = getFirstChildNodeValue(child);
					if("ORDER_NUMBER".equals(child.getNodeName())) orderNumber = getFirstChildNodeValue(child);
					child = child.getNextSibling();
				}

				//System.out.println("approvalIndicator="+approvalIndicator);
				//System.out.println("code="+code);
				//System.out.println("message="+message);
				//System.out.println("frontEndIndicator="+frontEndIndicator);
				//System.out.println("cvvIndicator="+cvvIndicator);
				//System.out.println("avsIndicator="+avsIndicator);
				//System.out.println("riskIndicator="+riskIndicator);
				//System.out.println("reference="+reference);
				//System.out.println("orderNumber="+orderNumber);
			}

			TransactionResult.CommunicationResult communicationResult;
			String providerErrorCode;
			TransactionResult.ErrorCode errorCode;
			String providerErrorMessage;

			if("A".equals(approvalIndicator)) {
				// Void approved
				communicationResult = TransactionResult.CommunicationResult.SUCCESS;
				providerErrorCode = null;
				errorCode = null;
				providerErrorMessage = null;
			} else if("E".equals(approvalIndicator) || "X".equals(approvalIndicator)) {
				// Gateway Error
				communicationResult = TransactionResult.CommunicationResult.GATEWAY_ERROR;
				providerErrorCode = code;
				errorCode = convertErrorCode(code);
				providerErrorMessage = message;
			} else {
				// Unknown response
				communicationResult = TransactionResult.CommunicationResult.LOCAL_ERROR;
				providerErrorCode = code;
				errorCode = convertErrorCode(code);
				providerErrorMessage = message;
			}

			return new VoidResult(
				transaction.getProviderId(),
				communicationResult,
				providerErrorCode,
				errorCode,
				providerErrorMessage,
				reference
			);
		} catch(ServiceException err) {
			return new VoidResult(
				transaction.getProviderId(),
				TransactionResult.CommunicationResult.LOCAL_ERROR,
				err.getClass().getName(),
				TransactionResult.ErrorCode.ERROR_TRY_AGAIN,
				err.getMessage(),
				null
			);
		} catch(RemoteException err) {
			return new VoidResult(
				transaction.getProviderId(),
				TransactionResult.CommunicationResult.LOCAL_ERROR,
				err.getClass().getName(),
				TransactionResult.ErrorCode.ERROR_TRY_AGAIN,
				err.getMessage(),
				null
			);
		}
	}

	@Override
	public CreditResult credit(TransactionRequest transactionRequest, CreditCard creditCard) {
		throw new NotImplementedException();
	}

	@Override
	public boolean canStoreCreditCards() throws IOException {
		try {
			return new WsVaultLocator().getwsVaultSoap().VERIFY_SERVICE(
				emptyStringIfNull(merchantId),
				emptyStringIfNull(merchantKey)
			);
		} catch(ServiceException err) {
			throw new LocalizedIOException(err, accessor, "MerchantServicesProvider.canStoreCreditCards.ioException");
		} catch(RemoteException err) {
			throw new LocalizedIOException(err, accessor, "MerchantServicesProvider.canStoreCreditCards.ioException");
		}
	}

	@Override
	public String storeCreditCard(CreditCard creditCard) throws IOException {
		try {
			String success = null;
			String guid = null;
			String message = null;

			{
				MessageElement[] results = new WsVaultLocator().getwsVaultSoap().INSERT_CREDIT_CARD_DATA(
					emptyStringIfNull(merchantId),
					emptyStringIfNull(merchantKey),
					emptyStringIfNull(creditCard.getCardNumber()),
					emptyStringIfNull(creditCard.getExpirationDateMMYY())
				).get_any();

				Node table1 = results[results.length-1].getFirstChild().getFirstChild();
				Node child = table1.getFirstChild();
				while(child!=null) {
					if("SUCCESS".equals(child.getNodeName())) success = getFirstChildNodeValue(child);
					if("GUID".equals(child.getNodeName())) guid = getFirstChildNodeValue(child);
					if("MESSAGE".equals(child.getNodeName())) message = child.getFirstChild().getNodeValue();
					child = child.getNextSibling();
				}

				//System.out.println("success="+success);
				//System.out.println("guid="+guid);
				//System.out.println("message="+message);
			}

			if(!"true".equals(success)) throw new LocalizedIOException(accessor, "MerchantServicesProvider.storeCreditCard.notSuccessful");
			if(guid==null) {
				if("UNABLE TO VERIFY VAULT SERVICE".equals(message)) throw new LocalizedIOException(accessor, "MerchantServicesProvider.storeCreditCard.notSupported");
				else throw new LocalizedIOException(accessor, "MerchantServicesProvider.storeCreditCard.missingProviderUniqueId");
			}
			return guid;
		} catch(ServiceException err) {
			throw new IOException(err);
		} catch(RemoteException err) {
			throw new IOException(err);
		}
	}

	@Override
	public void updateCreditCard(CreditCard creditCard) {
		// Nothing to do, only local persistence used for these values
	}

	/**
	 * @param cardCode Note: Sage Payments does not update stored card code
	 */
	@Override
	public void updateCreditCardNumberAndExpiration(
		CreditCard creditCard,
		String cardNumber,
		byte expirationMonth,
		short expirationYear,
		String cardCode
	) throws IOException {
		try {
			String success = null;
			String guid = null;
			String message = null;

			{
				MessageElement[] results = new WsVaultLocator().getwsVaultSoap().UPDATE_CREDIT_CARD_DATA(
					emptyStringIfNull(merchantId),
					emptyStringIfNull(merchantKey),
					emptyStringIfNull(creditCard.getProviderUniqueId()),
					emptyStringIfNull(cardNumber),
					emptyStringIfNull(CreditCard.getExpirationDateMMYY(expirationMonth, expirationYear))
				).get_any();
				Node table1 = results[results.length-1].getFirstChild().getFirstChild();
				Node child = table1.getFirstChild();
				while(child!=null) {
					if("SUCCESS".equals(child.getNodeName())) success = getFirstChildNodeValue(child);
					if("GUID".equals(child.getNodeName())) guid = getFirstChildNodeValue(child);
					if("MESSAGE".equals(child.getNodeName())) message = getFirstChildNodeValue(child);
					child = child.getNextSibling();
				}

				//System.out.println("success="+success);
				//System.out.println("guid="+guid);
				//System.out.println("message="+message);
			}

			if(!"true".equals(success)) throw new LocalizedIOException(accessor, "MerchantServicesProvider.updateCreditCardNumberAndExpiration.notSuccessful");
			if("UNABLE TO VERIFY VAULT SERVICE".equals(message)) throw new LocalizedIOException(accessor, "MerchantServicesProvider.updateCreditCardNumberAndExpiration.notSupported");
			if("INVALID CARDNUMBER".equals(message)) throw new LocalizedIOException(accessor, "MerchantServicesProvider.updateCreditCardNumberAndExpiration.invalidCardNumber");
			if(!"SUCCESS".equals(message)) throw new LocalizedIOException(accessor, "MerchantServicesProvider.updateCreditCardNumberAndExpiration.unexpectedResponse", message);
		} catch(ServiceException err) {
			throw new IOException(err);
		} catch(RemoteException err) {
			throw new IOException(err);
		}
	}

	@Override
	public void updateCreditCardExpiration(
		CreditCard creditCard,
		byte expirationMonth,
		short expirationYear
	) throws IOException {
		try {
			String success = null;
			String guid = null;
			String message = null;

			{
				MessageElement[] results = new WsVaultLocator().getwsVaultSoap().UPDATE_CREDIT_CARD_EXPIRATION_DATE(
					emptyStringIfNull(merchantId),
					emptyStringIfNull(merchantKey),
					emptyStringIfNull(creditCard.getProviderUniqueId()),
					emptyStringIfNull(CreditCard.getExpirationDateMMYY(expirationMonth, expirationYear))
				).get_any();
				Node table1 = results[results.length-1].getFirstChild().getFirstChild();
				Node child = table1.getFirstChild();
				while(child!=null) {
					if("SUCCESS".equals(child.getNodeName())) success = getFirstChildNodeValue(child);
					if("GUID".equals(child.getNodeName())) guid = getFirstChildNodeValue(child);
					if("MESSAGE".equals(child.getNodeName())) message = getFirstChildNodeValue(child);
					child = child.getNextSibling();
				}

				//System.out.println("success="+success);
				//System.out.println("guid="+guid);
				//System.out.println("message="+message);
			}

			if(!"true".equals(success)) throw new LocalizedIOException(accessor, "MerchantServicesProvider.updateCreditCardExpiration.notSuccessful");
			if("UNABLE TO LOCATE".equals(message)) throw new LocalizedIOException(accessor, "MerchantServicesProvider.updateCreditCardExpiration.unableToLocate");
			if(!"SUCCESS".equals(message)) throw new LocalizedIOException(accessor, "MerchantServicesProvider.updateCreditCardExpiration.unexpectedResponse", message);
		} catch(ServiceException err) {
			throw new IOException(err);
		} catch(RemoteException err) {
			throw new IOException(err);
		}
	}

	@Override
	public void deleteCreditCard(CreditCard creditCard) throws IOException {
		try {
			boolean success = new WsVaultLocator().getwsVaultSoap().DELETE_DATA(
				emptyStringIfNull(merchantId),
				emptyStringIfNull(merchantKey),
				emptyStringIfNull(creditCard.getProviderUniqueId())
			);
			if(!success) throw new LocalizedIOException(accessor, "MerchantServicesProvider.deleteCreditCard.notSuccessful");
		} catch(ServiceException err) {
			throw new IOException(err);
		} catch(RemoteException err) {
			throw new IOException(err);
		}
	}

	/*
	public static void main(String[] args) {
		try {
			// Test expiration date update
			CreditCard creditCard = new CreditCard();
			creditCard.setProviderUniqueId("d7e61c0226014cd3a7eb911ca5545b5d");
			sagePayments.updateCreditCardExpiration(creditCard, (byte)2, (byte)9);

			// Test number and expiration date update
			CreditCard creditCard = new CreditCard();
			creditCard.setProviderUniqueId("d7e61c0226014cd3a7eb911ca5545b5d");
			creditCard.setPersistenceUniqueId("10");
			sagePayments.updateCreditCardNumberAndExpiration(principal, creditCard, "5212345678901234", (byte)2, (byte)9);
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	 */
}
