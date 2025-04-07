package com.expresso.exception;

/**
 * Exception thrown for arithmetic errors during expression evaluation.
 * This includes operations like division by zero or invalid mathematical operations.
 */
public class ArithmeticExpressionException extends InvalidOperationException {
    /**
     * Creates a new ArithmeticExpressionException for a division by zero error.
     * 
     * @param left the left operand (dividend)
     * @param right the right operand (divisor, which is zero)
     * @return the exception
     */
    public static ArithmeticExpressionException divisionByZero(Object left, Object right) {
        return new ArithmeticExpressionException("/", left, right, "Division by zero");
    }

    /**
     * Creates a new ArithmeticExpressionException for a modulo by zero error.
     * 
     * @param left the left operand (dividend)
     * @param right the right operand (divisor, which is zero)
     * @return the exception
     */
    public static ArithmeticExpressionException moduloByZero(Object left, Object right) {
        return new ArithmeticExpressionException("%", left, right, "Modulo by zero");
    }

    /**
     * Creates a new ArithmeticExpressionException with the specified details.
     * 
     * @param operation the arithmetic operation that failed
     * @param leftOperand the left operand
     * @param rightOperand the right operand
     * @param message details about the arithmetic error
     */
    public ArithmeticExpressionException(
            String operation, Object leftOperand, Object rightOperand, String message) {
        super(operation, leftOperand, rightOperand, message);
    }
} 