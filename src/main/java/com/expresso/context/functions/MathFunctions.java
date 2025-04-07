package com.expresso.context.functions;

import com.expresso.context.Context;

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
} 