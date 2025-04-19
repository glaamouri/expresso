package com.expresso.ast;

import com.expresso.context.Context;

/**
 * Represents a unary expression (e.g., -$a, !$b).
 */
public class UnaryExpression implements Expression {
    public enum Operator {
        NEGATE,
        NOT
    }

    private final Expression operand;
    private final Operator operator;

    public UnaryExpression(Expression operand, Operator operator) {
        this.operand = operand;
        this.operator = operator;
    }

    @Override
    public Object evaluate(Context context) {
        Object value = operand.evaluate(context);

        switch (operator) {
            case NEGATE:
                if (value == null) {
                    return 0.0; // Negating null returns 0
                }
                if (value instanceof Number) {
                    return -((Number) value).doubleValue();
                }
                throw new IllegalArgumentException("Cannot negate non-numeric value: " + value);
            case NOT:
                return !isTruthy(value); // Convert to boolean and negate
            default:
                throw new IllegalArgumentException("Unknown unary operator: " + operator);
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
} 