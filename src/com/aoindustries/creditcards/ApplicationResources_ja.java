package com.aoindustries.creditcards;

/*
 * Copyright 2009 by AO Industries, Inc.,
 * 7262 Bull Pen Cir, Mobile, Alabama, 36695, U.S.A.
 * All rights reserved.
 */
import com.aoindustries.util.EditableResourceBundle;
import java.io.File;
import java.util.Locale;

/**
 * Provides a simplified interface for obtaining localized values from the ApplicationResources.properties files.
 * Is also an editable resource bundle.
 *
 * @author  AO Industries, Inc.
 */
public final class ApplicationResources_ja extends EditableResourceBundle {

    /**
     * Do not use directly.
     */
    public ApplicationResources_ja() {
        super(
            new File(System.getProperty("user.home")+"/common/ao/cvswork/ao-credit-cards/src/com/aoindustries/creditcards/ApplicationResources_ja.properties"),
            Locale.JAPANESE,
            ApplicationResources.bundleSet
        );
    }
}
