package com.expresso.context.functions;

import com.expresso.context.Context;
import java.util.List;

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

    /**
     * Returns metadata information about the functions provided by this provider
     * 
     * @return List of FunctionInfo objects describing each function
     */
    List<FunctionInfo> getFunctionInfo();
}