package com.expresso.context.functions;

import com.expresso.context.Context;
import java.util.List;

/**
 * Provider for mathematical functions.
 */
public class MathFunctions implements FunctionProvider {

    @Override
    public void registerFunctions(Context context) {
        context.registerFunction("abs", args -> Math.abs(((Number) args[0]).doubleValue()));
        context.registerFunction("ceil", args -> Math.ceil(((Number) args[0]).doubleValue()));
        context.registerFunction("floor", args -> Math.floor(((Number) args[0]).doubleValue()));
        context.registerFunction("round", args -> {
            double num = ((Number) args[0]).doubleValue();
            if (args.length > 1) {
                int decimals = ((Number) args[1]).intValue();
                double factor = Math.pow(10, decimals);
                return Math.round(num * factor) / factor;
            }
            return (long) Math.round(num);
        });
        context.registerFunction("max", args -> {
            double a = ((Number) args[0]).doubleValue();
            double b = ((Number) args[1]).doubleValue();
            return Math.max(a, b);
        });
        context.registerFunction("min", args -> {
            double a = ((Number) args[0]).doubleValue();
            double b = ((Number) args[1]).doubleValue();
            return Math.min(a, b);
        });
        context.registerFunction("pow", args -> {
            double base = ((Number) args[0]).doubleValue();
            double exponent = ((Number) args[1]).doubleValue();
            return Math.pow(base, exponent);
        });
        context.registerFunction("sqrt", args -> {
            double num = ((Number) args[0]).doubleValue();
            return Math.sqrt(num);
        });
        context.registerFunction("random", args -> Math.random());
        context.registerFunction("sin", args -> Math.sin(((Number) args[0]).doubleValue()));
        context.registerFunction("cos", args -> Math.cos(((Number) args[0]).doubleValue()));
        context.registerFunction("tan", args -> Math.tan(((Number) args[0]).doubleValue()));
        context.registerFunction("log", args -> Math.log(((Number) args[0]).doubleValue()));
        context.registerFunction("log10", args -> Math.log10(((Number) args[0]).doubleValue()));
        context.registerFunction("exp", args -> Math.exp(((Number) args[0]).doubleValue()));
    }

    @Override
    public List<FunctionInfo> getFunctionInfo() {
        return List.of(
                FunctionInfo.builder("abs")
                        .description("Returns the absolute value of a number")
                        .parameter("num", "Number", "The number to process")
                        .returnType("Number")
                        .build(),

                FunctionInfo.builder("ceil")
                        .description("Returns the smallest integer greater than or equal to a number")
                        .parameter("num", "Number", "The number to round up")
                        .returnType("Number")
                        .build(),

                FunctionInfo.builder("floor")
                        .description("Returns the largest integer less than or equal to a number")
                        .parameter("num", "Number", "The number to round down")
                        .returnType("Number")
                        .build(),

                FunctionInfo.builder("round")
                        .description("Rounds a number to the specified number of decimal places")
                        .parameter("num", "Number", "The number to round")
                        .parameter("decimals", "Integer", "The number of decimal places (optional)")
                        .returnType("Number")
                        .build(),

                FunctionInfo.builder("max")
                        .description("Returns the larger of two numbers")
                        .parameter("a", "Number", "First number")
                        .parameter("b", "Number", "Second number")
                        .returnType("Number")
                        .build(),

                FunctionInfo.builder("min")
                        .description("Returns the smaller of two numbers")
                        .parameter("a", "Number", "First number")
                        .parameter("b", "Number", "Second number")
                        .returnType("Number")
                        .build(),

                FunctionInfo.builder("pow")
                        .description("Returns base raised to the power of exponent")
                        .parameter("base", "Number", "The base number")
                        .parameter("exponent", "Number", "The exponent")
                        .returnType("Number")
                        .build(),

                FunctionInfo.builder("sqrt")
                        .description("Returns the square root of a number")
                        .parameter("num", "Number", "The number")
                        .returnType("Number")
                        .build(),

                FunctionInfo.builder("random")
                        .description("Returns a random number between 0 and 1")
                        .returnType("Number")
                        .build(),

                FunctionInfo.builder("sin")
                        .description("Returns the sine of a number (angle in radians)")
                        .parameter("angle", "Number", "The angle in radians")
                        .returnType("Number")
                        .build(),

                FunctionInfo.builder("cos")
                        .description("Returns the cosine of a number (angle in radians)")
                        .parameter("angle", "Number", "The angle in radians")
                        .returnType("Number")
                        .build(),

                FunctionInfo.builder("tan")
                        .description("Returns the tangent of a number (angle in radians)")
                        .parameter("angle", "Number", "The angle in radians")
                        .returnType("Number")
                        .build(),

                FunctionInfo.builder("log")
                        .description("Returns the natural logarithm (base e) of a number")
                        .parameter("num", "Number", "The number")
                        .returnType("Number")
                        .build(),

                FunctionInfo.builder("log10")
                        .description("Returns the base 10 logarithm of a number")
                        .parameter("num", "Number", "The number")
                        .returnType("Number")
                        .build(),

                FunctionInfo.builder("exp")
                        .description("Returns e raised to the power of a number")
                        .parameter("num", "Number", "The exponent")
                        .returnType("Number")
                        .build());
    }
}