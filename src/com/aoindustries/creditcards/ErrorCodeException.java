package com.aoindustries.creditcards;

/*
 * Copyright 2007 by AO Industries, Inc.,
 * 816 Azalea Rd, Mobile, Alabama, 36693, U.S.A.
 * All rights reserved.
 */
import java.util.Locale;

/**
 * Extends <code>LocalizedIOException</code> to provide exceptions along with the corresponding error code.
 *
 * @author  AO Industries, Inc.
 */
public class ErrorCodeException extends LocalizedIOException {

    final private TransactionResult.ErrorCode errorCode;

    public ErrorCodeException(TransactionResult.ErrorCode errorCode, Locale userLocale, String key) {
        super(userLocale, key);
        this.errorCode = errorCode;
    }

    public ErrorCodeException(TransactionResult.ErrorCode errorCode, Locale userLocale, String key, Object arg0) {
        super(userLocale, key, arg0);
        this.errorCode = errorCode;
    }

    public ErrorCodeException(TransactionResult.ErrorCode errorCode, Locale userLocale, String key, Object arg0, Object arg1) {
        super(userLocale, key, arg0, arg1);
        this.errorCode = errorCode;
    }

    public ErrorCodeException(Throwable cause, TransactionResult.ErrorCode errorCode, Locale userLocale, String key) {
        super(cause, userLocale, key);
        this.errorCode = errorCode;
    }

    public ErrorCodeException(Throwable cause, TransactionResult.ErrorCode errorCode, Locale userLocale, String key, Object arg0) {
        super(cause, userLocale, key, arg0);
        this.errorCode = errorCode;
    }

    public ErrorCodeException(Throwable cause, TransactionResult.ErrorCode errorCode, Locale userLocale, String key, Object arg0, Object arg1) {
        super(cause, userLocale, key, arg0, arg1);
        this.errorCode = errorCode;
    }

    public TransactionResult.ErrorCode getErrorCode() {
        return errorCode;
    }
}
