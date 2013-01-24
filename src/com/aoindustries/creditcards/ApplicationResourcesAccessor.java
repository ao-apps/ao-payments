/*
 * ao-credit-cards - Credit card processing API supporting multiple payment gateways.
 * Copyright (C) 2013  AO Industries, Inc.
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
 * Provides a simplified interface for obtaining localized values from the ApplicationResources.properties files.
 */
public final class ApplicationResourcesAccessor {

    /**
     * Make no instances.
     */
    private ApplicationResourcesAccessor() {
    }

    public static final com.aoindustries.util.i18n.ApplicationResourcesAccessor accessor;
    static {
        // Use correct package after any potential obfuscation
        String classname = ApplicationResourcesAccessor.class.getName();
        accessor = com.aoindustries.util.i18n.ApplicationResourcesAccessor.getInstance(
            classname.substring(0, classname.lastIndexOf('.') + 1) + "ApplicationResources"
        );
    }
}