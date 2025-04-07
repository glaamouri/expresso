package com.expresso.context.functions;

import com.expresso.context.Context;
import java.time.LocalDate;

/**
 * Provider for comparison functions.
 */
public class ComparisonFunctions implements FunctionProvider {
    
    @Override
    public void registerFunctions(Context context) {
        context.registerFunction("greaterThan", args -> {
            if (args[0] == null || args[1] == null) return false;
            if (args[0] instanceof Number && args[1] instanceof Number) {
                return ((Number) args[0]).doubleValue() > ((Number) args[1]).doubleValue();
            }
            if (args[0] instanceof String && args[1] instanceof String) {
                return ((String) args[0]).compareTo((String) args[1]) > 0;
            }
            if (args[0] instanceof LocalDate && args[1] instanceof LocalDate) {
                return ((LocalDate) args[0]).isAfter((LocalDate) args[1]);
            }
            throw new IllegalArgumentException("Cannot compare types: " + args[0].getClass() + " and " + args[1].getClass());
        });
        
        context.registerFunction("lessThan", args -> {
            if (args[0] == null || args[1] == null) return false;
            if (args[0] instanceof Number && args[1] instanceof Number) {
                return ((Number) args[0]).doubleValue() < ((Number) args[1]).doubleValue();
            }
            if (args[0] instanceof String && args[1] instanceof String) {
                return ((String) args[0]).compareTo((String) args[1]) < 0;
            }
            if (args[0] instanceof LocalDate && args[1] instanceof LocalDate) {
                return ((LocalDate) args[0]).isBefore((LocalDate) args[1]);
            }
            throw new IllegalArgumentException("Cannot compare types: " + args[0].getClass() + " and " + args[1].getClass());
        });
        
        context.registerFunction("greaterThanOrEqual", args -> {
            if (args[0] == null || args[1] == null) return false;
            if (args[0] instanceof Number && args[1] instanceof Number) {
                return ((Number) args[0]).doubleValue() >= ((Number) args[1]).doubleValue();
            }
            if (args[0] instanceof String && args[1] instanceof String) {
                return ((String) args[0]).compareTo((String) args[1]) >= 0;
            }
            if (args[0] instanceof LocalDate && args[1] instanceof LocalDate) {
                return !((LocalDate) args[0]).isBefore((LocalDate) args[1]);
            }
            throw new IllegalArgumentException("Cannot compare types: " + args[0].getClass() + " and " + args[1].getClass());
        });
        
        context.registerFunction("lessThanOrEqual", args -> {
            if (args[0] == null || args[1] == null) return false;
            if (args[0] instanceof Number && args[1] instanceof Number) {
                return ((Number) args[0]).doubleValue() <= ((Number) args[1]).doubleValue();
            }
            if (args[0] instanceof String && args[1] instanceof String) {
                return ((String) args[0]).compareTo((String) args[1]) <= 0;
            }
            if (args[0] instanceof LocalDate && args[1] instanceof LocalDate) {
                return !((LocalDate) args[0]).isAfter((LocalDate) args[1]);
            }
            throw new IllegalArgumentException("Cannot compare types: " + args[0].getClass() + " and " + args[1].getClass());
        });
        
        context.registerFunction("strictEquals", args -> {
            if (args[0] == null && args[1] == null) return true;
            if (args[0] == null || args[1] == null) return false;
            return args[0].equals(args[1]);
        });
        
        context.registerFunction("notEquals", args -> {
            if (args[0] == null && args[1] == null) return false;
            if (args[0] == null || args[1] == null) return true;
            return !args[0].equals(args[1]);
        });
    }
} 