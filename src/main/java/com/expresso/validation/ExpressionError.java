package com.expresso.validation;

/**
 * Class that represents an error found during expression validation
 */
public class ExpressionError {
    private final String errorType;
    private final String message;
    private final ErrorLocation location;

    /**
     * Creates a new expression error
     * 
     * @param errorType The type of error (e.g., "SyntaxError", "VariableNotFound")
     * @param message A detailed error message
     * @param location The location of the error in the expression
     */
    public ExpressionError(String errorType, String message, ErrorLocation location) {
        this.errorType = errorType;
        this.message = message;
        this.location = location;
    }

    /**
     * Creates a new expression error without a specific location
     * 
     * @param errorType The type of error (e.g., "SyntaxError", "VariableNotFound")
     * @param message A detailed error message
     */
    public ExpressionError(String errorType, String message) {
        this(errorType, message, null);
    }

    /**
     * Gets the error type
     * 
     * @return The error type
     */
    public String getErrorType() {
        return errorType;
    }

    /**
     * Gets the error message
     * 
     * @return The error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the error location
     * 
     * @return The error location, or null if not specified
     */
    public ErrorLocation getLocation() {
        return location;
    }

    @Override
    public String toString() {
        if (location != null) {
            return errorType + " at " + location + ": " + message;
        } else {
            return errorType + ": " + message;
        }
    }

    /**
     * Class that represents the location of an error in an expression
     */
    public static class ErrorLocation {
        private final int startPosition;
        private final int endPosition;
        private final String expression;

        /**
         * Creates a new error location
         * 
         * @param startPosition The start position of the error in the expression (0-based)
         * @param endPosition The end position of the error in the expression (0-based)
         * @param expression The full expression string
         */
        public ErrorLocation(int startPosition, int endPosition, String expression) {
            this.startPosition = startPosition;
            this.endPosition = endPosition;
            this.expression = expression;
        }

        /**
         * Gets the start position of the error
         * 
         * @return The start position (0-based)
         */
        public int getStartPosition() {
            return startPosition;
        }

        /**
         * Gets the end position of the error
         * 
         * @return The end position (0-based)
         */
        public int getEndPosition() {
            return endPosition;
        }

        /**
         * Gets the expression string
         * 
         * @return The full expression string
         */
        public String getExpression() {
            return expression;
        }

        /**
         * Gets the snippet of the expression where the error occurred
         * 
         * @return The snippet containing the error
         */
        public String getErrorSnippet() {
            if (expression == null) {
                return "";
            }
            
            // Calculate safe indices
            int start = Math.max(0, startPosition);
            int end = Math.min(expression.length(), endPosition);
            
            if (start >= end || start >= expression.length()) {
                return "";
            }
            
            return expression.substring(start, end);
        }

        @Override
        public String toString() {
            return "position " + startPosition + "-" + endPosition;
        }
    }
} 