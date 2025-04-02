package com.expresso.context.functions;

import com.expresso.context.Context;
import java.util.Arrays;
import java.util.List;

/**
 * Registry for all function providers.
 * Acts as a central place to register all function providers.
 */
public class FunctionRegistry {
    
    // List of all function providers
    private static final List<FunctionProvider> PROVIDERS = Arrays.asList(
        new StringFunctions(),
        new MathFunctions(),
        new LogicFunctions(),
        new ComparisonFunctions(),
        new DateFunctions(),
        new CollectionFunctions(),
        new UtilityFunctions()
    );
    
    /**
     * Registers all built-in functions in the given context.
     * 
     * @param context The context to register functions in
     */
    public static void registerAllFunctions(Context context) {
        PROVIDERS.forEach(provider -> provider.registerFunctions(context));
    }
} 