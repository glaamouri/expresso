package com.expresso.ast;

import com.expresso.context.Context;
import com.expresso.exception.EvaluationException;
import com.expresso.exception.PropertyNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * AST node for function calls
 */
public record FunctionCallExpression(String functionName, List<Expression> arguments) implements Expression {
    // List of functions that should handle null variables gracefully
    private static final Set<String> SAFE_FUNCTIONS = new HashSet<>(Arrays.asList(
            "isNull", "coalesce", "isEmpty"
    ));

    @Override
    public Object evaluate(Context context) {
        // Mark variables as being in a safe context if used with certain functions
        if (SAFE_FUNCTIONS.contains(functionName)) {
            for (Expression arg : arguments) {
                if (arg instanceof VariableExpression) {
                    ((VariableExpression) arg).setInSafeContext(true);
                }
            }
        }
        
        var function = context.getFunction(functionName);
        if (function == null) {
            throw new EvaluationException("Function not found: " + functionName);
        }

        Object[] args;
        try {
            args = arguments.stream()
                    .map(arg -> arg.evaluate(context))
                    .toArray();
        } catch (PropertyNotFoundException e) {
            // If we got here, then the safe context flag didn't prevent the exception
            // This could happen if the variable is nested in another expression type
            // Special case to handle known safe functions
            if (SAFE_FUNCTIONS.contains(functionName)) {
                if ("isNull".equals(functionName)) {
                    return true;
                } else if ("coalesce".equals(functionName) && arguments.size() > 1) {
                    // For coalesce, try to evaluate the remaining arguments
                    for (int i = 1; i < arguments.size(); i++) {
                        try {
                            Object result = arguments.get(i).evaluate(context);
                            if (result != null) {
                                return result;
                            }
                        } catch (PropertyNotFoundException ignored) {
                            // Continue to the next argument
                        }
                    }
                    return null;
                }
            }
            throw e;
        }

        try {
            return function.apply(args);
        } catch (Exception e) {
            throw new EvaluationException("Error evaluating function " + functionName, e);
        }
    }
} 