package com.expresso.ast;

import com.expresso.context.Context;
import com.expresso.exception.EvaluationException;
import java.util.List;

/**
 * AST node for function calls
 */
public record FunctionCallExpression(String functionName, List<Expression> arguments) implements Expression {

    @Override
    public Object evaluate(Context context) {
        var function = context.getFunction(functionName);
        if (function == null) {
            throw new EvaluationException("Function not found: " + functionName);
        }

        Object[] args = arguments.stream()
                .map(arg -> arg.evaluate(context))
                .toArray();

        try {
            return function.apply(args);
        } catch (Exception e) {
            throw new EvaluationException("Error evaluating function " + functionName, e);
        }
    }
} 