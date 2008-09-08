package com.aoindustries.creditcards;

/*
 * Copyright 2007-2008 by AO Industries, Inc.,
 * 7262 Bull Pen Cir, Mobile, Alabama, 36695, U.S.A.
 * All rights reserved.
 */

/**
 * Encapsulates the results of a credit.
 *
 * @see  MerchantServicesProvider#credit
 *
 * @author  AO Industries, Inc.
 */
public class CreditResult extends TransactionResult {

    public CreditResult(
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
}
