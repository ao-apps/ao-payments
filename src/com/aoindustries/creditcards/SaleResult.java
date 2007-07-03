package com.aoindustries.creditcards;

/*
 * Copyright 2007 by AO Industries, Inc.,
 * 816 Azalea Rd, Mobile, Alabama, 36693, U.S.A.
 * All rights reserved.
 */

/**
 * Encapsulates the results of a sale.
 *
 * @see  MerchantServicesProvider#sale
 *
 * @author  AO Industries, Inc.
 */
public class SaleResult {

    private AuthorizationResult authorizationResult;
    private CaptureResult captureResult;

    public SaleResult(
        AuthorizationResult authorizationResult,
        CaptureResult captureResult
    ) {
        this.authorizationResult = authorizationResult;
        this.captureResult = captureResult;
    }

    public AuthorizationResult getAuthorizationResult() {
        return authorizationResult;
    }
    
    public CaptureResult getCaptureResult() {
        return captureResult;
    }
}
