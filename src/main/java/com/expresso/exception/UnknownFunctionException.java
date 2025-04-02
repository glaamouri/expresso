package com.expresso.exception;

/**
 * Exception thrown when a function is referenced but cannot be found in the context.
 * This occurs when an expression attempts to call a function that has not been registered.
 */
public class UnknownFunctionException extends EvaluationException {
    private final String functionName;

    /**
     * Creates a new UnknownFunctionException with the specified function name.
     * 
     * @param functionName the name of the undefined function
     */
    public UnknownFunctionException(String functionName) {
        super("Function not found: " + functionName);
        this.functionName = functionName;
    }

    /**
     * Gets the name of the unknown function.
     * 
     * @return the function name
     */
    public String getFunctionName() {
        return functionName;
    }
} 