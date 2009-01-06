package com.aoindustries.creditcards;

/*
 * Copyright 2007-2009 by AO Industries, Inc.,
 * 7262 Bull Pen Cir, Mobile, Alabama, 36695, U.S.A.
 * All rights reserved.
 */
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates instances of <code>MerchantServicesProvider</code>s based on the provided
 * parameters.
 * Will only create once instance of each unique set of parameters.
 *
 * @author  AO Industries, Inc.
 */
public class MerchantServicesProviderFactory {

    private static class ProcessorKey {
        private static boolean equals(Object O1, Object O2) {
            return O1==null?O2==null:O1.equals(O2); 
        }

        final private String providerId;
        final private String className;
        final private String param1;
        final private String param2;
        final private String param3;
        final private String param4;
        
        private ProcessorKey(
            String providerId,
            String className,
            String param1,
            String param2,
            String param3,
            String param4
        ) {
            this.providerId = providerId;
            this.className = className;
            this.param1 = param1;
            this.param2 = param2;
            this.param3 = param3;
            this.param4 = param4;
        }
        
        public int hashCode() {
            return
                providerId.hashCode()
                + className.hashCode()*7
                + (param1==null ? 0 : (param1.hashCode()*17))
                + (param2==null ? 0 : (param1.hashCode()*37))
                + (param3==null ? 0 : (param1.hashCode()*103))
                + (param4==null ? 0 : (param1.hashCode()*149))
            ;
        }
        
        public boolean equals(Object O) {
            if(O==null) return false;
            if(!(O instanceof ProcessorKey)) return false;
            ProcessorKey other = (ProcessorKey)O;
            return
                providerId.equals(other.providerId)
                && className.equals(other.className)
                && equals(param1, other.param1)
                && equals(param2, other.param2)
                && equals(param3, other.param3)
                && equals(param4, other.param4)
            ;
        }
    }

    final private static Map<ProcessorKey,MerchantServicesProvider> providers = new HashMap<ProcessorKey,MerchantServicesProvider>();

    /**
     * Gets the provider for the given parameters.<br>
     * <br>
     * Only one instance of each unique providerId, classname and all parameters will be created.<br>
     * <br>
     * Every processor will use the <code>AOServPersistenceMechanism</code> for its persistence.
     */
    public static MerchantServicesProvider getMerchantServicesProvider(
        String providerId,
        String className,
        String param1,
        String param2,
        String param3,
        String param4
    ) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        // The key in the map
        ProcessorKey processorKey = new ProcessorKey(
            providerId,
            className,
            param1,
            param2,
            param3,
            param4
        );

        // Now synchronize access to processors
        synchronized(providers) {
            // Look for existing instance
            MerchantServicesProvider provider = providers.get(processorKey);
            if(provider==null) {
                // Instantiate through reflection
                Class<? extends MerchantServicesProvider> clazz = Class.forName(className).asSubclass(MerchantServicesProvider.class);

                // Try the providerId + 4-parameter constructor
                try {
                    Constructor<? extends MerchantServicesProvider> constructor = clazz.getConstructor(String.class, String.class, String.class, String.class, String.class);
                    provider = constructor.newInstance(providerId, param1, param2, param3, param4);
                } catch(NoSuchMethodException err) {
                    // Fall through to next param set
                } catch(IllegalAccessException err) {
                    // Fall through to next param set
                } catch(InstantiationException err) {
                    // Fall through to next param set
                } catch(InvocationTargetException err) {
                    // Fall through to next param set
                }

                if(provider==null) {
                    // Try the providerId + 3-parameter constructor
                    try {
                        Constructor<? extends MerchantServicesProvider> constructor = clazz.getConstructor(String.class, String.class, String.class, String.class);
                        provider = constructor.newInstance(providerId, param1, param2, param3);
                    } catch(NoSuchMethodException err) {
                        // Fall through to next param set
                    } catch(IllegalAccessException err) {
                        // Fall through to next param set
                    } catch(InstantiationException err) {
                        // Fall through to next param set
                    } catch(InvocationTargetException err) {
                        // Fall through to next param set
                    }
                }

                if(provider==null) {
                    // Try the providerId + 2-parameter constructor
                    try {
                        Constructor<? extends MerchantServicesProvider> constructor = clazz.getConstructor(String.class, String.class, String.class);
                        provider = constructor.newInstance(providerId, param1, param2);
                    } catch(NoSuchMethodException err) {
                        // Fall through to next param set
                    } catch(IllegalAccessException err) {
                        // Fall through to next param set
                    } catch(InstantiationException err) {
                        // Fall through to next param set
                    } catch(InvocationTargetException err) {
                        // Fall through to next param set
                    }
                }

                if(provider==null) {
                    // Try the providerId + 1-parameter constructor
                    try {
                        Constructor<? extends MerchantServicesProvider> constructor = clazz.getConstructor(String.class, String.class);
                        provider = constructor.newInstance(providerId, param1);
                    } catch(NoSuchMethodException err) {
                        // Fall through to next param set
                    } catch(IllegalAccessException err) {
                        // Fall through to next param set
                    } catch(InstantiationException err) {
                        // Fall through to next param set
                    } catch(InvocationTargetException err) {
                        // Fall through to next param set
                    }
                }

                if(provider==null) {
                    // Try the providerId constructor, if fails allow exception to go out of this method
                    Constructor<? extends MerchantServicesProvider> constructor = clazz.getConstructor(String.class);
                    provider = constructor.newInstance(providerId);
                }
                
                // Create and add to cache
                providers.put(processorKey, provider);
            }
            return provider;
        }
    }

    private MerchantServicesProviderFactory() {
        // Make no instances
    }
}
