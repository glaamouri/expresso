package com.expresso.context.functions;

import com.expresso.context.Context;
import java.time.LocalDate;
import java.util.List;

/**
 * Provider for comparison functions.
 */
public class ComparisonFunctions implements FunctionProvider {

    @Override
    public void registerFunctions(Context context) {
        context.registerFunction("greaterThan", args -> {
            if (args[0] == null || args[1] == null)
                return false;
            if (args[0] instanceof Number && args[1] instanceof Number) {
                return ((Number) args[0]).doubleValue() > ((Number) args[1]).doubleValue();
            }
            if (args[0] instanceof String && args[1] instanceof String) {
                return ((String) args[0]).compareTo((String) args[1]) > 0;
            }
            if (args[0] instanceof LocalDate && args[1] instanceof LocalDate) {
                return ((LocalDate) args[0]).isAfter((LocalDate) args[1]);
            }
            throw new IllegalArgumentException(
                    "Cannot compare types: " + args[0].getClass() + " and " + args[1].getClass());
        });

        context.registerFunction("lessThan", args -> {
            if (args[0] == null || args[1] == null)
                return false;
            if (args[0] instanceof Number && args[1] instanceof Number) {
                return ((Number) args[0]).doubleValue() < ((Number) args[1]).doubleValue();
            }
            if (args[0] instanceof String && args[1] instanceof String) {
                return ((String) args[0]).compareTo((String) args[1]) < 0;
            }
            if (args[0] instanceof LocalDate && args[1] instanceof LocalDate) {
                return ((LocalDate) args[0]).isBefore((LocalDate) args[1]);
            }
            throw new IllegalArgumentException(
                    "Cannot compare types: " + args[0].getClass() + " and " + args[1].getClass());
        });

        context.registerFunction("greaterThanOrEqual", args -> {
            if (args[0] == null || args[1] == null)
                return false;
            if (args[0] instanceof Number && args[1] instanceof Number) {
                return ((Number) args[0]).doubleValue() >= ((Number) args[1]).doubleValue();
            }
            if (args[0] instanceof String && args[1] instanceof String) {
                return ((String) args[0]).compareTo((String) args[1]) >= 0;
            }
            if (args[0] instanceof LocalDate && args[1] instanceof LocalDate) {
                return !((LocalDate) args[0]).isBefore((LocalDate) args[1]);
            }
            throw new IllegalArgumentException(
                    "Cannot compare types: " + args[0].getClass() + " and " + args[1].getClass());
        });

        context.registerFunction("lessThanOrEqual", args -> {
            if (args[0] == null || args[1] == null)
                return false;
            if (args[0] instanceof Number && args[1] instanceof Number) {
                return ((Number) args[0]).doubleValue() <= ((Number) args[1]).doubleValue();
            }
            if (args[0] instanceof String && args[1] instanceof String) {
                return ((String) args[0]).compareTo((String) args[1]) <= 0;
            }
            if (args[0] instanceof LocalDate && args[1] instanceof LocalDate) {
                return !((LocalDate) args[0]).isAfter((LocalDate) args[1]);
            }
            throw new IllegalArgumentException(
                    "Cannot compare types: " + args[0].getClass() + " and " + args[1].getClass());
        });

        context.registerFunction("strictEquals", args -> {
            if (args[0] == null && args[1] == null)
                return true;
            if (args[0] == null || args[1] == null)
                return false;
            return args[0].equals(args[1]);
        });

        context.registerFunction("notEquals", args -> {
            if (args[0] == null && args[1] == null)
                return false;
            if (args[0] == null || args[1] == null)
                return true;
            return !args[0].equals(args[1]);
        });
    }

    @Override
    public List<FunctionInfo> getFunctionInfo() {
        return List.of(
                FunctionInfo.builder("greaterThan")
                        .description("Checks if first value is greater than second value")
                        .parameter("a", "Number|String|Date", "First value")
                        .parameter("b", "Number|String|Date", "Second value")
                        .returnType("Boolean")
                        .build(),

                FunctionInfo.builder("lessThan")
                        .description("Checks if first value is less than second value")
                        .parameter("a", "Number|String|Date", "First value")
                        .parameter("b", "Number|String|Date", "Second value")
                        .returnType("Boolean")
                        .build(),

                FunctionInfo.builder("greaterThanOrEqual")
                        .description("Checks if first value is greater than or equal to second value")
                        .parameter("a", "Number|String|Date", "First value")
                        .parameter("b", "Number|String|Date", "Second value")
                        .returnType("Boolean")
                        .build(),

                FunctionInfo.builder("lessThanOrEqual")
                        .description("Checks if first value is less than or equal to second value")
                        .parameter("a", "Number|String|Date", "First value")
                        .parameter("b", "Number|String|Date", "Second value")
                        .returnType("Boolean")
                        .build(),

                FunctionInfo.builder("strictEquals")
                        .description("Checks if two values are strictly equal")
                        .parameter("a", "Object", "First value")
                        .parameter("b", "Object", "Second value")
                        .returnType("Boolean")
                        .build(),

                FunctionInfo.builder("notEquals")
                        .description("Checks if two values are not equal")
                        .parameter("a", "Object", "First value")
                        .parameter("b", "Object", "Second value")
                        .returnType("Boolean")
                        .build());
    }
}