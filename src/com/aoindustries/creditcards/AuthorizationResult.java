package com.aoindustries.creditcards;

/*
 * Copyright 2007 by AO Industries, Inc.,
 * 816 Azalea Rd, Mobile, Alabama, 36693, U.S.A.
 * All rights reserved.
 */
import java.util.Locale;

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
         * Gets the display value in the default locale.
         */
        public String toString() {
            return toString(Locale.getDefault());
        }

        /**
         * Gets the display value in the provided locale.
         */
        public String toString(Locale userLocale) {
            return ApplicationResourcesAccessor.getMessage(userLocale, "AuthorizationResult.ApprovalResult."+name());
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
         * Gets the display value in the default locale.
         */
        public String toString() {
            return toString(Locale.getDefault());
        }

        /**
         * Gets the display value in the provided locale.
         */
        public String toString(Locale userLocale) {
            return ApplicationResourcesAccessor.getMessage(userLocale, "AuthorizationResult.DeclineReason."+name());
        }
    }

    public enum ReviewReason {
        RISK_MANAGEMENT,
        ACCEPTED_MERCHANT_REVIEW,
        ACCEPTED_AUTHORIZED_MERCHANT_REVIEW;

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
            return ApplicationResourcesAccessor.getMessage(userLocale, "AuthorizationResult.ReviewReason."+name());
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
         * Gets the display value in the default locale.
         */
        public String toString() {
            return toString(Locale.getDefault());
        }

        /**
         * Gets the display value in the provided locale.
         */
        public String toString(Locale userLocale) {
            return ApplicationResourcesAccessor.getMessage(userLocale, "AuthorizationResult.CvvResult."+name());
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
         * Gets the display value in the default locale.
         */
        public String toString() {
            return toString(Locale.getDefault());
        }

        /**
         * Gets the display value in the provided locale.
         */
        public String toString(Locale userLocale) {
            return ApplicationResourcesAccessor.getMessage(userLocale, "AuthorizationResult.AvsResult."+name());
        }
    }

    private String providerApprovalResult;
    private ApprovalResult approvalResult;
    private String providerDeclineReason;
    private DeclineReason declineReason;
    private String providerReviewReason;
    private ReviewReason reviewReason;
    private String providerCvvResult;
    private CvvResult cvvResult;
    private String providerAvsResult;
    private AvsResult avsResult;
    private String approvalCode;

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
