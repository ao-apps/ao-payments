package com.aoindustries.creditcards;

/*
 * Copyright 2007-2009 by AO Industries, Inc.,
 * 7262 Bull Pen Cir, Mobile, Alabama, 36695, U.S.A.
 * All rights reserved.
 */

/**
 * Encapsulates the results of a void.
 *
 * @see  MerchantServicesProvider#voidTransaction
 *
 * @author  AO Industries, Inc.
 */
public class VoidResult extends TransactionResult implements Cloneable {

    public VoidResult(
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

    public VoidResult clone() {
        try {
            return (VoidResult)super.clone();
        } catch(CloneNotSupportedException err) {
            throw new RuntimeException(err);
        }
    }
}
