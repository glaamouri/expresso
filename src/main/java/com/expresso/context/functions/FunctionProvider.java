package com.expresso.context.functions;

import com.expresso.context.Context;

/**
 * Interface for classes that provide function implementations to the Context.
 */
public interface FunctionProvider {
    
    /**
     * Registers functions in the context
     * 
     * @param context The context to register functions in
     */
    void registerFunctions(Context context);
} 