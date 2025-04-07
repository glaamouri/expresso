package com.expresso.ast;

import com.expresso.context.Context;
import com.expresso.exception.ArithmeticExpressionException;
import com.expresso.exception.InvalidOperationException;

/**
 * AST node for binary expressions (e.g., a + b, a * b)
 */
public class BinaryExpression implements Expression {
    public enum Operator {
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE,
        MODULO,
        AND,
        OR,
        EQUALS,
        NOT_EQUALS,
        GREATER_THAN,
        LESS_THAN,
        GREATER_THAN_OR_EQUAL,
        LESS_THAN_OR_EQUAL
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
        
        // Short-circuit evaluation for logical operators
        if (operator == Operator.AND) {
            // If left is falsy (false or null), return false without evaluating right
            if (!isTruthy(leftValue)) {
                return false;
            }
            // Otherwise, evaluate right and check truthiness
            Object rightValue = right.evaluate(context);
            return isTruthy(rightValue);
        }
        
        if (operator == Operator.OR) {
            // If left is truthy (true and not null), return true without evaluating right
            if (isTruthy(leftValue)) {
                return true;
            }
            // Otherwise, evaluate right and check truthiness
            Object rightValue = right.evaluate(context);
            return isTruthy(rightValue);
        }
        
        // For non-short-circuit operators, evaluate both sides
        Object rightValue = right.evaluate(context);
        
        switch (operator) {
            case ADD:
                if (leftValue instanceof String || rightValue instanceof String) {
                    return String.valueOf(leftValue) + String.valueOf(rightValue);
                } else if (leftValue instanceof Number && rightValue instanceof Number) {
                    return ((Number) leftValue).doubleValue() + ((Number) rightValue).doubleValue();
                }
                throw new InvalidOperationException("+", leftValue, rightValue, "Cannot add these types");
            case SUBTRACT:
                if (leftValue instanceof Number && rightValue instanceof Number) {
                    return ((Number) leftValue).doubleValue() - ((Number) rightValue).doubleValue();
                }
                throw new InvalidOperationException("-", leftValue, rightValue, "Cannot subtract non-numeric values");
            case MULTIPLY:
                if (leftValue instanceof Number && rightValue instanceof Number) {
                    return ((Number) leftValue).doubleValue() * ((Number) rightValue).doubleValue();
                }
                throw new InvalidOperationException("*", leftValue, rightValue, "Cannot multiply non-numeric values");
            case DIVIDE:
                if (leftValue instanceof Number && rightValue instanceof Number) {
                    double leftNum = ((Number) leftValue).doubleValue();
                    double rightNum = ((Number) rightValue).doubleValue();
                    if (rightNum == 0) {
                        throw ArithmeticExpressionException.divisionByZero(leftValue, rightValue);
                    }
                    return leftNum / rightNum;
                }
                throw new InvalidOperationException("/", leftValue, rightValue, "Cannot divide non-numeric values");
            case MODULO:
                if (leftValue instanceof Number && rightValue instanceof Number) {
                    double leftNum = ((Number) leftValue).doubleValue();
                    double rightNum = ((Number) rightValue).doubleValue();
                    if (rightNum == 0) {
                        throw ArithmeticExpressionException.moduloByZero(leftValue, rightValue);
                    }
                    return leftNum % rightNum;
                }
                throw new InvalidOperationException("%", leftValue, rightValue, "Cannot perform modulo with non-numeric values");
            case EQUALS:
                if (leftValue == null && rightValue == null) {
                    return true;
                }
                if (leftValue == null || rightValue == null) {
                    return false;
                }
                // Special handling for numeric comparisons
                if (leftValue instanceof Number && rightValue instanceof Number) {
                    return Double.compare(((Number) leftValue).doubleValue(), ((Number) rightValue).doubleValue()) == 0;
                }
                return leftValue.equals(rightValue);
            case NOT_EQUALS:
                if (leftValue == null && rightValue == null) {
                    return false;
                }
                if (leftValue == null || rightValue == null) {
                    return true;
                }
                // Special handling for numeric comparisons
                if (leftValue instanceof Number && rightValue instanceof Number) {
                    return Double.compare(((Number) leftValue).doubleValue(), ((Number) rightValue).doubleValue()) != 0;
                }
                return !leftValue.equals(rightValue);
            case GREATER_THAN:
                if (leftValue == null || rightValue == null) {
                    return false; // Null values cannot be compared
                }
                return compareValues(leftValue, rightValue) > 0 ? true : false;
            case LESS_THAN:
                if (leftValue == null || rightValue == null) {
                    return false; // Null values cannot be compared
                }
                return compareValues(leftValue, rightValue) < 0 ? true : false;
            case GREATER_THAN_OR_EQUAL:
                if (leftValue == null || rightValue == null) {
                    return false; // Null values cannot be compared
                }
                return compareValues(leftValue, rightValue) >= 0 ? true : false;
            case LESS_THAN_OR_EQUAL:
                if (leftValue == null || rightValue == null) {
                    return false; // Null values cannot be compared
                }
                return compareValues(leftValue, rightValue) <= 0 ? true : false;
            default:
                throw new InvalidOperationException(operator.toString(), leftValue, rightValue, "Unsupported operation");
        }
    }
    
    /**
     * Determines if a value is truthy (true if not null and not false)
     * @param value The value to check
     * @return true if the value is truthy, false otherwise
     */
    private boolean isTruthy(Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return true; // Non-null, non-boolean values are considered truthy
    }
    
    /**
     * Compares two values and returns an integer indicating their relationship
     * @param left The left value
     * @param right The right value
     * @return Negative if left < right, 0 if left == right, positive if left > right
     */
    @SuppressWarnings("unchecked")
    private int compareValues(Object left, Object right) {
        if (left instanceof Number && right instanceof Number) {
            double leftDouble = ((Number) left).doubleValue();
            double rightDouble = ((Number) right).doubleValue();
            return Double.compare(leftDouble, rightDouble);
        }
        
        if (left instanceof String && right instanceof String) {
            return ((String) left).compareTo((String) right);
        }
        
        if (left instanceof Comparable && right.getClass().isAssignableFrom(left.getClass())) {
            return ((Comparable<Object>) left).compareTo(right);
        }
        
        throw new InvalidOperationException("compare", left, right, "Cannot compare these types");
    }
} 