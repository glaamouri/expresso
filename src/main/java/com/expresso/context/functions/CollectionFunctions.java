package com.expresso.context.functions;

import com.expresso.context.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Provider for collection manipulation functions.
 */
public class CollectionFunctions implements FunctionProvider {
    
    @Override
    public void registerFunctions(Context context) {
        context.registerFunction("size", args -> {
            if (args[0] == null) return 0;
            if (args[0] instanceof String) return ((String) args[0]).length();
            if (args[0] instanceof List) return ((List<?>) args[0]).size();
            if (args[0] instanceof Map) return ((Map<?, ?>) args[0]).size();
            if (args[0].getClass().isArray()) return java.lang.reflect.Array.getLength(args[0]);
            throw new IllegalArgumentException("Cannot get size of type: " + args[0].getClass());
        });
        
        context.registerFunction("first", args -> {
            if (args[0] == null) return null;
            if (args[0] instanceof List && !((List<?>) args[0]).isEmpty()) {
                return ((List<?>) args[0]).get(0);
            }
            if (args[0].getClass().isArray() && java.lang.reflect.Array.getLength(args[0]) > 0) {
                return java.lang.reflect.Array.get(args[0], 0);
            }
            return null;
        });
        
        context.registerFunction("last", args -> {
            if (args[0] == null) return null;
            if (args[0] instanceof List) {
                List<?> list = (List<?>) args[0];
                return list.isEmpty() ? null : list.get(list.size() - 1);
            }
            if (args[0].getClass().isArray()) {
                int length = java.lang.reflect.Array.getLength(args[0]);
                return length == 0 ? null : java.lang.reflect.Array.get(args[0], length - 1);
            }
            return null;
        });
        
        context.registerFunction("subList", args -> {
            if (args[0] == null) return List.of();
            List<?> list;
            if (args[0] instanceof List) {
                list = (List<?>) args[0];
            } else if (args[0].getClass().isArray()) {
                int length = java.lang.reflect.Array.getLength(args[0]);
                List<Object> result = new ArrayList<>(length);
                for (int i = 0; i < length; i++) {
                    result.add(java.lang.reflect.Array.get(args[0], i));
                }
                list = result;
            } else {
                throw new IllegalArgumentException("Cannot get subList of type: " + args[0].getClass());
            }
            
            int start = ((Number) args[1]).intValue();
            int end = args.length > 2 ? ((Number) args[2]).intValue() : list.size();
            start = Math.max(0, Math.min(start, list.size()));
            end = Math.max(start, Math.min(end, list.size()));
            return new ArrayList<>(list.subList(start, end));
        });
    }
} 