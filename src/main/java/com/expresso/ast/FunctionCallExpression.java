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
        // Special handling for isNull to avoid throwing exceptions for non-existent
        // variables
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

        // Special handling for filter and map
        if (name.equals("filter") || name.equals("map")) {
            if (arguments.size() != 2) {
                throw new FunctionExecutionException(name, "Function requires exactly 2 arguments");
            }

            Object collection = arguments.get(0).evaluate(context);
            Expression lambda = arguments.get(1);

            List<Object> result = new java.util.ArrayList<>();

            // Save current item value if it exists
            Object previousItem = context.getVariable("item");
            boolean hadItem = context.variableExists("item");

            try {
                if (collection instanceof List) {
                    for (Object item : (List<?>) collection) {
                        context.setVariable("item", item);
                        Object lambdaResult = lambda.evaluate(context);

                        if (name.equals("filter")) {
                            if (Boolean.TRUE.equals(lambdaResult)) {
                                result.add(item);
                            }
                        } else {
                            result.add(lambdaResult);
                        }
                    }
                } else if (collection != null && collection.getClass().isArray()) {
                    int length = java.lang.reflect.Array.getLength(collection);
                    for (int i = 0; i < length; i++) {
                        Object item = java.lang.reflect.Array.get(collection, i);
                        context.setVariable("item", item);
                        Object lambdaResult = lambda.evaluate(context);

                        if (name.equals("filter")) {
                            if (Boolean.TRUE.equals(lambdaResult)) {
                                result.add(item);
                            }
                        } else {
                            result.add(lambdaResult);
                        }
                    }
                }
            } finally {
                // Restore previous item value
                if (hadItem) {
                    context.setVariable("item", previousItem);
                } else {
                    context.removeVariable("item");
                }
            }

            return result;
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