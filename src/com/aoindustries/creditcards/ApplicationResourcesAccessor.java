package com.aoindustries.creditcards;
/*
 * Copyright 2007 by AO Industries, Inc.,
 * 816 Azalea Rd, Mobile, Alabama, 36693, U.S.A.
 * All rights reserved.
 */
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Provides a simplified interface for obtaining localized values from the ApplicationResources.properties files.
 *
 * @author  AO Industries, Inc.
 */
public final class ApplicationResourcesAccessor {

    /**
     * Make no instances.
     */
    private ApplicationResourcesAccessor() {
    }
    
    public static String getMessage(Locale locale, String key) {
        String message = getString(locale, key);
        return message;
    }
    
    public static String getMessage(Locale locale, String key, Object arg0) {
        String message = getString(locale, key);
        message = message.replace("{0}", arg0==null ? "null" : arg0.toString());
        return message;
    }

    /**
     * TODO: If the replaced value for {0} contains {1}, that will be replaced, too.
     */
    public static String getMessage(Locale locale, String key, Object arg0, Object arg1) {
        String message = getString(locale, key);
        message = message.replace("{0}", arg0==null ? "null" : arg0.toString());
        message = message.replace("{1}", arg1==null ? "null" : arg1.toString());
        return message;
    }

    /**
     * Cache for resource lookups.
     */
    private static final Map<Locale,Map<String,String>> cache = new HashMap<Locale,Map<String,String>>();

    /**
     * Looks for a match, caches results.
     */
    private static String getString(Locale locale, String key) {
        synchronized(cache) {
            // Find the locale-specific cache
            Map<String,String> localeMap = cache.get(locale);
            if(localeMap==null) cache.put(locale, localeMap = new HashMap<String,String>());

            // Look in the cache
            String string = localeMap.get(key);
            if(string==null) {
                try {
                    ResourceBundle applicationResources = ResourceBundle.getBundle("com.aoindustries.creditcards.ApplicationResources", locale);
                    string = applicationResources.getString(key);
                } catch(MissingResourceException err) {
                    // string remains null
                }

                // Default to struts-style ??? formatting
                if(string==null) string="???"+locale.toString()+"."+key+"???";

                // Add to cache
                localeMap.put(key, string);
            }
            return string;
        }
    }
}
