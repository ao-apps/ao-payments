package com.aoindustries.creditcards;

/*
 * Copyright 2007 by AO Industries, Inc.,
 * 816 Azalea Rd, Mobile, Alabama, 36693, U.S.A.
 * All rights reserved.
 */

/**
 * Encapsulates the results of a capture.
 *
 * @see  MerchantServicesProvider#capture
 *
 * @author  AO Industries, Inc.
 */
public class CaptureResult extends TransactionResult implements Cloneable {

    public CaptureResult(
        String providerId,
        CommunicationResult communicationResult,
        String providerErrorCode,
        ErrorCode errorCode,
        String providerErrorMessage,
        String providerUniqueId
    ) {
        super(
            providerId,
            communicationResult,
            providerErrorCode,
            errorCode,
            providerErrorMessage,
            providerUniqueId
        );
    }

    public CaptureResult clone() {
        try {
            return (CaptureResult)super.clone();
        } catch(CloneNotSupportedException err) {
            throw new RuntimeException(err);
        }
    }
}
