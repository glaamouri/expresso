package com.expresso.ast;

import com.expresso.context.Context;
import com.expresso.exception.EvaluationException;

/**
 * Represents a binary expression (e.g., $a + $b).
 */
public class BinaryExpression implements Expression {
    public enum Operator {
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE,
        MODULO,
        AND,
        OR
    }

    private final Expression left;
    private final Expression right;
    private final Operator operator;

    public BinaryExpression(Expression left, Expression right, Operator operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public Object evaluate(Context context) {
        Object leftValue = left.evaluate(context);
        
        // Special handling for logical operators
        if (operator == Operator.AND || operator == Operator.OR) {
            // For AND: If left is null or false, return false (short-circuit)
            if (operator == Operator.AND) {
                boolean leftBool = isTruthy(leftValue);
                if (!leftBool) {
                    return false;
                }
            }
            
            // For OR: If left is truthy (not null and not false), return true (short-circuit)
            if (operator == Operator.OR) {
                boolean leftBool = isTruthy(leftValue);
                if (leftBool) {
                    return true;
                }
            }
            
            // Evaluate right side
            Object rightValue = right.evaluate(context);
            boolean rightBool = isTruthy(rightValue);
            
            return operator == Operator.AND ? (isTruthy(leftValue) && rightBool) : (isTruthy(leftValue) || rightBool);
        }
        
        // For non-logical operators, proceed as before
        Object rightValue = right.evaluate(context);
        
        if (leftValue == null || rightValue == null) {
            throw new EvaluationException("Cannot perform operation on null value");
        }

        if (leftValue instanceof Number && rightValue instanceof Number) {
            double leftNum = ((Number) leftValue).doubleValue();
            double rightNum = ((Number) rightValue).doubleValue();

            switch (operator) {
                case ADD:
                    return leftNum + rightNum;
                case SUBTRACT:
                    return leftNum - rightNum;
                case MULTIPLY:
                    return leftNum * rightNum;
                case DIVIDE:
                    if (rightNum == 0) {
                        throw new EvaluationException("Division by zero");
                    }
                    return leftNum / rightNum;
                case MODULO:
                    if (rightNum == 0) {
                        throw new EvaluationException("Modulo by zero");
                    }
                    return leftNum % rightNum;
                default:
                    throw new EvaluationException("Unknown operator");
            }
        } else if (operator == Operator.ADD && (leftValue instanceof String || rightValue instanceof String)) {
            // String concatenation
            return String.valueOf(leftValue) + String.valueOf(rightValue);
        } else {
            throw new EvaluationException("Incompatible types for operation: " + leftValue.getClass().getSimpleName() + " and " + rightValue.getClass().getSimpleName());
        }
    }
    
    /**
     * Determines if a value should be considered "truthy" in a boolean context.
     * - null is falsy
     * - Boolean false is falsy
     * - Boolean true is truthy
     * - For other types, we throw an exception as they're not valid in logical expressions
     */
    private boolean isTruthy(Object value) {
        if (value == null) {
            return false;
        }
        
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        
        throw new EvaluationException("Logical operators require boolean operands or null values");
    }
} 