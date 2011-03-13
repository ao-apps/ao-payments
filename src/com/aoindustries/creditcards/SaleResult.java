package com.aoindustries.creditcards;

/*
 * Copyright 2007-2011 by AO Industries, Inc.,
 * 7262 Bull Pen Cir, Mobile, Alabama, 36695, U.S.A.
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
