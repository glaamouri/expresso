package com.expresso.exception;

/**
 * Exception thrown when an operation is invalid between two operands.
 * This occurs when trying to perform operations on incompatible types,
 * such as adding a string to a boolean.
 */
public class InvalidOperationException extends EvaluationException {
    private final String operation;
    private final Object leftOperand;
    private final Object rightOperand;

    /**
     * Creates a new InvalidOperationException with the specified details.
     * 
     * @param operation the operation that was attempted (e.g., "+", "*")
     * @param leftOperand the left operand
     * @param rightOperand the right operand
     * @param message details about the error
     */
    public InvalidOperationException(String operation, Object leftOperand, Object rightOperand, String message) {
        super("Invalid operation '" + operation + "' between " + 
              formatOperand(leftOperand) + " and " + formatOperand(rightOperand) + 
              (message != null && !message.isEmpty() ? ": " + message : ""));
        this.operation = operation;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    private static String formatOperand(Object operand) {
        if (operand == null) {
            return "null";
        }
        if (operand instanceof String) {
            return "'" + operand + "'";
        }
        return operand + " (" + operand.getClass().getSimpleName() + ")";
    }

    /**
     * Gets the operation that was attempted.
     * 
     * @return the operation
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Gets the left operand of the operation.
     * 
     * @return the left operand
     */
    public Object getLeftOperand() {
        return leftOperand;
    }

    /**
     * Gets the right operand of the operation.
     * 
     * @return the right operand
     */
    public Object getRightOperand() {
        return rightOperand;
    }
} 