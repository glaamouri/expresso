package com.expresso.exception;

/**
 * Exception thrown when a function is called with fewer arguments than required.
 * This occurs when a function is invoked without providing all mandatory arguments.
 */
public class MissingArgumentException extends FunctionExecutionException {
    private final int expectedCount;
    private final int actualCount;

    /**
     * Creates a new MissingArgumentException with the specified details.
     * 
     * @param functionName the name of the function
     * @param expectedCount the number of arguments expected
     * @param actualCount the number of arguments actually provided
     */
    public MissingArgumentException(String functionName, int expectedCount, int actualCount) {
        super(functionName, "Expected " + expectedCount + " arguments, but got " + actualCount);
        this.expectedCount = expectedCount;
        this.actualCount = actualCount;
    }

    /**
     * Gets the expected number of arguments.
     * 
     * @return the expected argument count
     */
    public int getExpectedCount() {
        return expectedCount;
    }

    /**
     * Gets the actual number of arguments that were provided.
     * 
     * @return the actual argument count
     */
    public int getActualCount() {
        return actualCount;
    }
} 