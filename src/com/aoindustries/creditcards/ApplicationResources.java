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

import com.aoindustries.util.i18n.EditableResourceBundle;
import com.aoindustries.util.i18n.EditableResourceBundleSet;
import java.io.File;
import java.util.Arrays;
import java.util.Locale;

/**
 * Do not use directly.
 *
 * @author  AO Industries, Inc.
 */
public final class ApplicationResources extends EditableResourceBundle {

	public static final EditableResourceBundleSet bundleSet = new EditableResourceBundleSet(
		ApplicationResources.class.getName(),
		Arrays.asList(
			Locale.ROOT,
			new Locale("ar"),
			Locale.GERMAN,
			Locale.ENGLISH,
			new Locale("es"),
			Locale.FRENCH,
			Locale.ITALIAN,
			Locale.JAPANESE,
			new Locale("pt"),
			Locale.CHINESE
		)
	);

	static File[] getSourceDirectories(String filename) {
		return new File[] {
			// AO development system
			// new File(System.getProperty("user.home")+"/common/aodev/cvswork/ao-credit-cards/src/main/java/com/aoindustries/creditcards", filename),
			// AO production build
			new File(System.getProperty("user.home")+"/common/aoprod/cvswork/ao-credit-cards/src/main/java/com/aoindustries/creditcards", filename),
			// Windows development system
			// new File("C:/ao/cvswork/ao-credit-cards/src/com/aoindustries/creditcards", filename)
		};
	}

	/**
	 * Do not use directly.
	 */
	public ApplicationResources() {
		super(
			Locale.ROOT,
			bundleSet,
			getSourceDirectories("ApplicationResources.properties")
		);
	}
}
