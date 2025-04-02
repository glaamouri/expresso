package com.expresso.ast;

import com.expresso.context.Context;

/**
 * Represents a conditional (ternary) expression: condition ? trueExpr : falseExpr
 */
public class ConditionalExpression implements Expression {
    private final Expression condition;
    private final Expression trueExpression;
    private final Expression falseExpression;

    public ConditionalExpression(Expression condition, Expression trueExpression, Expression falseExpression) {
        this.condition = condition;
        this.trueExpression = trueExpression;
        this.falseExpression = falseExpression;
    }

    @Override
    public Object evaluate(Context context) {
        Object conditionValue = condition.evaluate(context);
        
        // Evaluate to boolean
        boolean conditionResult = isTruthy(conditionValue);
        
        // Return the appropriate expression based on the condition
        return conditionResult ? trueExpression.evaluate(context) : falseExpression.evaluate(context);
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