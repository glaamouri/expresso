package com.expresso.ast;

import com.expresso.context.Context;
import com.expresso.exception.FunctionExecutionException;
import com.expresso.exception.UnknownFunctionException;
import java.util.List;
import java.util.function.Function;

/**
 * AST node for function calls
 */
public class FunctionCallExpression implements Expression {
    private final String name;
    private final List<Expression> arguments;

    public FunctionCallExpression(String name, List<Expression> arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    @Override
    public Object evaluate(Context context) {
        // Special handling for isNull to avoid throwing exceptions for non-existent variables
        if (name.equals("isNull") && arguments.size() == 1) {
            // Mark the argument as being used in a safe context if it's a variable
            if (arguments.get(0) instanceof VariableExpression) {
                ((VariableExpression) arguments.get(0)).setInSafeContext(true);
            }
            
            try {
                Object value = arguments.get(0).evaluate(context);
                return value == null;
            } catch (Exception e) {
                // For isNull function, treat exceptions as null values
                return true;
            }
        }
        
        // Special handling for coalesce to avoid throwing exceptions for null entries
        if (name.equals("coalesce")) {
            for (Expression arg : arguments) {
                // Mark variables as safe
                if (arg instanceof VariableExpression) {
                    ((VariableExpression) arg).setInSafeContext(true);
                }
                
                try {
                    Object value = arg.evaluate(context);
                    if (value != null) {
                        return value;
                    }
                } catch (Exception e) {
                    // Continue to the next argument on exception
                    continue;
                }
            }
            return null;
        }

        Function<Object[], Object> function = context.getFunction(name);
        if (function == null) {
            throw new UnknownFunctionException(name);
        }

        // Evaluate arguments
        Object[] args = new Object[arguments.size()];
        for (int i = 0; i < arguments.size(); i++) {
            args[i] = arguments.get(i).evaluate(context);
        }

        try {
            return function.apply(args);
        } catch (Exception e) {
            throw new FunctionExecutionException(name, e.getMessage(), e);
        }
    }

    public String getName() {
        return name;
    }

    public List<Expression> getArguments() {
        return arguments;
    }
} 