package com.expresso.exception;

/**
 * Base exception class for all Expresso expression-related exceptions.
 * This is the parent class for all exceptions in the expression evaluator.
 */
public class ExpressionException extends RuntimeException {
    /**
     * Creates a new ExpressionException with the specified message.
     * 
     * @param message the error message
     */
    public ExpressionException(String message) {
        super(message);
    }

    /**
     * Creates a new ExpressionException with the specified message and cause.
     * 
     * @param message the error message
     * @param cause the cause of the exception
     */
    public ExpressionException(String message, Throwable cause) {
        super(message, cause);
    }
} 