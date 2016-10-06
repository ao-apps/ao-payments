/*
 * ao-credit-cards - Credit card processing API supporting multiple payment gateways.
 * Copyright (C) 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2016  AO Industries, Inc.
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

/**
 * Encapsulates the results of an authorization.
 *
 * @see  MerchantServicesProvider#authorize
 *
 * @author  AO Industries, Inc.
 */
public class AuthorizationResult extends TransactionResult implements Cloneable {

	public enum ApprovalResult {
		APPROVED,
		DECLINED,
		HOLD;

		/**
		 * Gets the display value.
		 */
		@Override
		public String toString() {
			return accessor.getMessage("AuthorizationResult.ApprovalResult."+name());
		}
	}

	public enum DeclineReason {
		NO_SPECIFIC,
		EXPIRED_CARD,
		PICK_UP_CARD,
		AVS_MISMATCH,
		CVV2_MISMATCH,
		FRAUD_DETECTED,
		BLOCKED_IP,
		MANUAL_REVIEW,
		INSUFFICIENT_FUNDS,
		MAX_SALE_EXCEEDED,
		MIN_SALE_NOT_MET,
		VOLUME_EXCEEDED_1_DAY,
		USAGE_EXCEEDED_1_DAY,
		VOLUME_EXCEEDED_3_DAYS,
		USAGE_EXCEEDED_3_DAYS,
		VOLUME_EXCEEDED_15_DAYS,
		USAGE_EXCEEDED_15_DAYS,
		VOLUME_EXCEEDED_30_DAYS,
		USAGE_EXCEEDED_30_DAYS,
		STOLEN_OR_LOST_CARD,
		AVS_FAILURE,
		NOT_PROVIDED,
		UNKNOWN;

		/**
		 * Gets the display value.
		 */
		@Override
		public String toString() {
			return accessor.getMessage("AuthorizationResult.DeclineReason."+name());
		}
	}

	public enum ReviewReason {
		RISK_MANAGEMENT,
		ACCEPTED_MERCHANT_REVIEW,
		ACCEPTED_AUTHORIZED_MERCHANT_REVIEW;

		/**
		 * Gets the display value.
		 */
		@Override
		public String toString() {
			return accessor.getMessage("AuthorizationResult.ReviewReason."+name());
		}
	}

	public enum CvvResult {
		MATCH,
		NO_MATCH,
		NOT_PROCESSED,
		CVV2_NOT_PROVIDED_BY_MERCHANT,
		NOT_SUPPORTED_BY_ISSUER,
		UNKNOWN;

		/**
		 * Gets the display value.
		 */
		@Override
		public String toString() {
			return accessor.getMessage("AuthorizationResult.CvvResult."+name());
		}
	}

	public enum AvsResult {
		ADDRESS_NOT_PROVIDED,
		ADDRESS_Y_ZIP_9,
		ADDRESS_Y_ZIP_5,
		ADDRESS_Y_ZIP_N,
		ADDRESS_N_ZIP_9,
		ADDRESS_N_ZIP_5,
		ADDRESS_N_ZIP_N,
		UNAVAILABLE,
		RETRY,
		ERROR,
		SERVICE_NOT_SUPPORTED,
		NON_US_CARD,
		NOT_APPLICABLE,
		UNKNOWN;

		/**
		 * Gets the display value.
		 */
		@Override
		public String toString() {
			return accessor.getMessage("AuthorizationResult.AvsResult."+name());
		}
	}

	private final String providerApprovalResult;
	private final ApprovalResult approvalResult;
	private final String providerDeclineReason;
	private final DeclineReason declineReason;
	private final String providerReviewReason;
	private final ReviewReason reviewReason;
	private final String providerCvvResult;
	private final CvvResult cvvResult;
	private final String providerAvsResult;
	private final AvsResult avsResult;
	private final String approvalCode;

	public AuthorizationResult(
		String providerId,
		CommunicationResult communicationResult,
		String providerErrorCode,
		ErrorCode errorCode,
		String providerErrorMessage,
		String providerUniqueId,
		String providerApprovalResult,
		ApprovalResult approvalResult,
		String providerDeclineReason,
		DeclineReason declineReason,
		String providerReviewReason,
		ReviewReason reviewReason,
		String providerCvvResult,
		CvvResult cvvResult,
		String providerAvsResult,
		AvsResult avsResult,
		String approvalCode
	) {
		super(
			providerId,
			communicationResult,
			providerErrorCode,
			errorCode,
			providerErrorMessage,
			providerUniqueId
		);
		this.providerApprovalResult = providerApprovalResult;
		this.approvalResult = approvalResult;
		this.providerDeclineReason = providerDeclineReason;
		this.declineReason = declineReason;
		this.providerReviewReason = providerReviewReason;
		this.reviewReason = reviewReason;
		this.providerCvvResult = providerCvvResult;
		this.cvvResult = cvvResult;
		this.providerAvsResult = providerAvsResult;
		this.avsResult = avsResult;
		this.approvalCode = approvalCode;
	}

	@Override
	public AuthorizationResult clone() {
		try {
			return (AuthorizationResult)super.clone();
		} catch(CloneNotSupportedException err) {
			throw new RuntimeException(err);
		}
	}

	/**
	 * Gets the provider-specific approval response.
	 */
	public String getProviderApprovalResult() {
		return providerApprovalResult;
	}

	/**
	 * Gets the provider-neutral approval response code.
	 */
	public ApprovalResult getApprovalResult() {
		return approvalResult;
	}

	/**
	 * Gets the provider-specific decline reason.
	 */
	public String getProviderDeclineReason() {
		return providerDeclineReason;
	}

	/**
	 * Gets the provider-neutral decline reason.
	 */
	public DeclineReason getDeclineReason() {
		return declineReason;
	}

	/**
	 * Gets the provider-specific review reason.
	 */
	public String getProviderReviewReason() {
		return providerReviewReason;
	}

	/**
	 * Gets the provider-neutral review reason.
	 */
	public ReviewReason getReviewReason() {
		return reviewReason;
	}

	/**
	 * Gets the provider-specific CVV result.
	 */
	public String getProviderCvvResult() {
		return providerCvvResult;
	}

	/**
	 * Gets the provider-neutral CVV result.
	 */
	public CvvResult getCvvResult() {
		return cvvResult;
	}

	/**
	 * Gets the provider-specific AVS result.
	 */
	public String getProviderAvsResult() {
		return providerAvsResult;
	}

	/**
	 * Gets the provider-neutral AVS result.
	 */
	public AvsResult getAvsResult() {
		return avsResult;
	}

	/**
	 * Gets the approval code.
	 */
	public String getApprovalCode() {
		return approvalCode;
	}
}
