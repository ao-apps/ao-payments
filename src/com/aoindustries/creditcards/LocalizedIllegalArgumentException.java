package com.aoindustries.creditcards;

/*
 * Copyright 2007-2009 by AO Industries, Inc.,
 * 7262 Bull Pen Cir, Mobile, Alabama, 36695, U.S.A.
 * All rights reserved.
 */
import java.util.Locale;

/**
 * Extends <code>IllegalArgumentException</code> to provide exceptions with both JVM default locale and user locale error messages.
 *
 * @author  AO Industries, Inc.
 */
public class LocalizedIllegalArgumentException extends IllegalArgumentException {

    final private String localizedMessage;

    public LocalizedIllegalArgumentException(Locale userLocale, String key) {
        super(ApplicationResourcesAccessor.getMessage(Locale.getDefault(), key));
        this.localizedMessage = ApplicationResourcesAccessor.getMessage(userLocale, key);
    }

    public LocalizedIllegalArgumentException(Locale userLocale, String key, Object arg0) {
        super(ApplicationResourcesAccessor.getMessage(Locale.getDefault(), key, arg0));
        this.localizedMessage = ApplicationResourcesAccessor.getMessage(userLocale, key, arg0);
    }

    public LocalizedIllegalArgumentException(Throwable cause, Locale userLocale, String key) {
        super(ApplicationResourcesAccessor.getMessage(Locale.getDefault(), key), cause);
        this.localizedMessage = ApplicationResourcesAccessor.getMessage(userLocale, key);
    }

    public LocalizedIllegalArgumentException(Throwable cause, Locale userLocale, String key, Object arg0) {
        super(ApplicationResourcesAccessor.getMessage(Locale.getDefault(), key, arg0), cause);
        this.localizedMessage = ApplicationResourcesAccessor.getMessage(userLocale, key, arg0);
    }

    public String getLocalizedMessage() {
        return localizedMessage;
    }
}
