package com.expresso.exception;

/**
 * Exception thrown when there is an error during function execution.
 * This can occur due to issues like wrong argument types, invalid operations
 * within a function, or runtime errors during function evaluation.
 */
public class FunctionExecutionException extends EvaluationException {
    private final String functionName;

    /**
     * Creates a new FunctionExecutionException with the specified details.
     * 
     * @param functionName the name of the function that failed
     * @param message details about the function execution error
     */
    public FunctionExecutionException(String functionName, String message) {
        super("Error in function '" + functionName + "': " + message);
        this.functionName = functionName;
    }

    /**
     * Creates a new FunctionExecutionException with the specified details and cause.
     * 
     * @param functionName the name of the function that failed
     * @param message details about the function execution error
     * @param cause the underlying cause of the exception
     */
    public FunctionExecutionException(String functionName, String message, Throwable cause) {
        super("Error in function '" + functionName + "': " + message, cause);
        this.functionName = functionName;
    }

    /**
     * Gets the name of the function that caused the error.
     * 
     * @return the function name
     */
    public String getFunctionName() {
        return functionName;
    }
} 