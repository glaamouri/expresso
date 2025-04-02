package com.expresso.context.functions;

import com.expresso.context.Context;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Provider for utility functions.
 */
public class UtilityFunctions implements FunctionProvider {
    
    @Override
    public void registerFunctions(Context context) {
        context.registerFunction("typeof", args -> {
            if (args[0] == null) return "null";
            if (args[0] instanceof String) return "string";
            if (args[0] instanceof Number) return "number";
            if (args[0] instanceof Boolean) return "boolean";
            if (args[0] instanceof List || args[0].getClass().isArray()) return "list";
            if (args[0] instanceof Map) return "map";
            if (args[0] instanceof LocalDate) return "date";
            return args[0].getClass().getSimpleName().toLowerCase();
        });
        
        context.registerFunction("toString", args -> args[0] == null ? "null" : args[0].toString());
        
        context.registerFunction("toNumber", args -> {
            if (args[0] == null) return 0.0;
            if (args[0] instanceof Number) return ((Number) args[0]).doubleValue();
            if (args[0] instanceof String) {
                try {
                    return Double.parseDouble((String) args[0]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Cannot convert string to number: " + args[0]);
                }
            }
            if (args[0] instanceof Boolean) return ((Boolean) args[0]) ? 1.0 : 0.0;
            throw new IllegalArgumentException("Cannot convert to number: " + args[0]);
        });
        
        context.registerFunction("toBoolean", args -> {
            if (args[0] == null) return false;
            if (args[0] instanceof Boolean) return args[0];
            if (args[0] instanceof String) {
                String str = ((String) args[0]).toLowerCase();
                if ("true".equals(str) || "yes".equals(str) || "1".equals(str)) {
                    return true;
                }
                if ("false".equals(str) || "no".equals(str) || "0".equals(str)) {
                    return false;
                }
                // Non-specific strings are considered true if not empty
                return !str.isEmpty();
            }
            if (args[0] instanceof Number) return ((Number) args[0]).doubleValue() != 0;
            return true; // Non-null object is considered true
        });
    }
} 