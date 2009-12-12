package com.aoindustries.creditcards;

/*
 * Copyright 2007-2009 by AO Industries, Inc.,
 * 7262 Bull Pen Cir, Mobile, Alabama, 36695, U.S.A.
 * All rights reserved.
 */
import com.aoindustries.io.LocalizedIOException;
import java.util.Locale;

/**
 * Extends <code>LocalizedIOException</code> to provide exceptions along with the corresponding error code.
 *
 * @author  AO Industries, Inc.
 */
public class ErrorCodeException extends LocalizedIOException {

    private static final long serialVersionUID = 1L;

    final private TransactionResult.ErrorCode errorCode;

    public ErrorCodeException(TransactionResult.ErrorCode errorCode, Locale userLocale, String key) {
        super(ApplicationResources.accessor, userLocale, key);
        this.errorCode = errorCode;
    }

    public ErrorCodeException(TransactionResult.ErrorCode errorCode, Locale userLocale, String key, Object... args) {
        super(ApplicationResources.accessor, userLocale, key, args);
        this.errorCode = errorCode;
    }

    public ErrorCodeException(Throwable cause, TransactionResult.ErrorCode errorCode, Locale userLocale, String key) {
        super(cause, ApplicationResources.accessor, userLocale, key);
        this.errorCode = errorCode;
    }

    public ErrorCodeException(Throwable cause, TransactionResult.ErrorCode errorCode, Locale userLocale, String key, Object... args) {
        super(cause, ApplicationResources.accessor, userLocale, key, args);
        this.errorCode = errorCode;
    }

    public TransactionResult.ErrorCode getErrorCode() {
        return errorCode;
    }
}
