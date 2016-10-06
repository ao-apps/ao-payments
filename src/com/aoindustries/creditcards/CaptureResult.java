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

	@Override
	public CaptureResult clone() {
		try {
			return (CaptureResult)super.clone();
		} catch(CloneNotSupportedException err) {
			throw new RuntimeException(err);
		}
	}
}
