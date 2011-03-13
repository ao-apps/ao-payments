/*
 * Copyright 2007-2011 by AO Industries, Inc.,
 * 7262 Bull Pen Cir, Mobile, Alabama, 36695, U.S.A.
 * All rights reserved.
 */
package com.aoindustries.creditcards;

import com.aoindustries.io.LocalizedIOException;

/**
 * Extends <code>LocalizedIOException</code> to provide exceptions along with the corresponding error code.
 *
 * @author  AO Industries, Inc.
 */
public class ErrorCodeException extends LocalizedIOException {

    private static final long serialVersionUID = 1L;

    final private TransactionResult.ErrorCode errorCode;

    public ErrorCodeException(TransactionResult.ErrorCode errorCode, String key) {
        super(ApplicationResources.accessor, key);
        this.errorCode = errorCode;
    }

    public ErrorCodeException(TransactionResult.ErrorCode errorCode, String key, Object... args) {
        super(ApplicationResources.accessor, key, args);
        this.errorCode = errorCode;
    }

    public ErrorCodeException(Throwable cause, TransactionResult.ErrorCode errorCode, String key) {
        super(cause, ApplicationResources.accessor, key);
        this.errorCode = errorCode;
    }

    public ErrorCodeException(Throwable cause, TransactionResult.ErrorCode errorCode, String key, Object... args) {
        super(cause, ApplicationResources.accessor, key, args);
        this.errorCode = errorCode;
    }

    public TransactionResult.ErrorCode getErrorCode() {
        return errorCode;
    }
}
