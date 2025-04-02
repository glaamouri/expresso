package com.expresso.context.functions;

import com.expresso.context.Context;
import java.util.List;
import java.util.Map;

/**
 * Provider for logical functions and operations.
 */
public class LogicFunctions implements FunctionProvider {
    
    @Override
    public void registerFunctions(Context context) {
        context.registerFunction("isNull", args -> args[0] == null);
        context.registerFunction("coalesce", args -> {
            for (Object arg : args) {
                if (arg != null) return arg;
            }
            return null;
        });
        context.registerFunction("isEmpty", args -> {
            if (args[0] == null) return true;
            if (args[0] instanceof String) return ((String) args[0]).isEmpty();
            if (args[0] instanceof List) return ((List<?>) args[0]).isEmpty();
            if (args[0] instanceof Map) return ((Map<?, ?>) args[0]).isEmpty();
            if (args[0].getClass().isArray()) return java.lang.reflect.Array.getLength(args[0]) == 0;
            return false;
        });
        context.registerFunction("isNumber", args -> args[0] instanceof Number);
        context.registerFunction("isString", args -> args[0] instanceof String);
        context.registerFunction("isBoolean", args -> args[0] instanceof Boolean);
        context.registerFunction("isList", args -> args[0] instanceof List || (args[0] != null && args[0].getClass().isArray()));
        context.registerFunction("isMap", args -> args[0] instanceof Map);
        context.registerFunction("equals", args -> {
            if (args[0] == null && args[1] == null) return true;
            if (args[0] == null || args[1] == null) return false;
            return args[0].equals(args[1]);
        });
        context.registerFunction("ifThen", args -> {
            // Handle null condition gracefully, treating it as false
            boolean condition = false;
            if (args[0] != null) {
                if (args[0] instanceof Boolean) {
                    condition = (Boolean) args[0];
                } else {
                    // Non-null, non-boolean values are considered true
                    condition = true;
                }
            }
            return condition ? args[1] : args[2];
        });
    }
} 