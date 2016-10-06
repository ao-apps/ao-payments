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
		super(accessor, key);
		this.errorCode = errorCode;
	}

	public ErrorCodeException(TransactionResult.ErrorCode errorCode, String key, Object... args) {
		super(accessor, key, args);
		this.errorCode = errorCode;
	}

	public ErrorCodeException(Throwable cause, TransactionResult.ErrorCode errorCode, String key) {
		super(cause, accessor, key);
		this.errorCode = errorCode;
	}

	public ErrorCodeException(Throwable cause, TransactionResult.ErrorCode errorCode, String key, Object... args) {
		super(cause, accessor, key, args);
		this.errorCode = errorCode;
	}

	public TransactionResult.ErrorCode getErrorCode() {
		return errorCode;
	}
}
