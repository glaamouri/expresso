package com.expresso.ast;

import com.expresso.context.Context;
import com.expresso.exception.EvaluationException;

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
                    throw new EvaluationException("Cannot negate null value");
                }
                
                if (value instanceof Number) {
                    double num = ((Number) value).doubleValue();
                    return -num;
                } else {
                    throw new EvaluationException("Cannot negate non-numeric value: " + value.getClass().getSimpleName());
                }
            case NOT:
                if (value == null) {
                    // Treat null as false, so !null is true
                    return true;
                }
                
                if (value instanceof Boolean) {
                    return !(Boolean) value;
                } else {
                    throw new EvaluationException("Cannot apply logical NOT to non-boolean value: " + value.getClass().getSimpleName());
                }
            default:
                throw new EvaluationException("Unknown unary operator");
        }
    }
} 