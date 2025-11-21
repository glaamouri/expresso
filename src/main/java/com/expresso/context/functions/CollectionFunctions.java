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
            if (args[0] == null)
                return 0;
            if (args[0] instanceof String)
                return ((String) args[0]).length();
            if (args[0] instanceof List)
                return ((List<?>) args[0]).size();
            if (args[0] instanceof Map)
                return ((Map<?, ?>) args[0]).size();
            if (args[0].getClass().isArray())
                return java.lang.reflect.Array.getLength(args[0]);
            throw new IllegalArgumentException("Cannot get size of type: " + args[0].getClass());
        });

        context.registerFunction("first", args -> {
            if (args[0] == null)
                return null;
            if (args[0] instanceof List && !((List<?>) args[0]).isEmpty()) {
                return ((List<?>) args[0]).get(0);
            }
            if (args[0].getClass().isArray() && java.lang.reflect.Array.getLength(args[0]) > 0) {
                return java.lang.reflect.Array.get(args[0], 0);
            }
            return null;
        });

        context.registerFunction("last", args -> {
            if (args[0] == null)
                return null;
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
            if (args[0] == null)
                return List.of();
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

        context.registerFunction("sum", args -> {
            if (args[0] == null)
                return 0.0;
            double sum = 0;
            if (args[0] instanceof List) {
                for (Object item : (List<?>) args[0]) {
                    if (item instanceof Number) {
                        sum += ((Number) item).doubleValue();
                    }
                }
            } else if (args[0].getClass().isArray()) {
                int length = java.lang.reflect.Array.getLength(args[0]);
                for (int i = 0; i < length; i++) {
                    Object item = java.lang.reflect.Array.get(args[0], i);
                    if (item instanceof Number) {
                        sum += ((Number) item).doubleValue();
                    }
                }
            }
            return sum;
        });

        context.registerFunction("avg", args -> {
            if (args[0] == null)
                return 0.0;
            double sum = 0;
            int count = 0;
            if (args[0] instanceof List) {
                List<?> list = (List<?>) args[0];
                count = list.size();
                for (Object item : list) {
                    if (item instanceof Number) {
                        sum += ((Number) item).doubleValue();
                    }
                }
            } else if (args[0].getClass().isArray()) {
                count = java.lang.reflect.Array.getLength(args[0]);
                for (int i = 0; i < count; i++) {
                    Object item = java.lang.reflect.Array.get(args[0], i);
                    if (item instanceof Number) {
                        sum += ((Number) item).doubleValue();
                    }
                }
            }
            return count == 0 ? 0.0 : sum / count;
        });

        context.registerFunction("sort", args -> {
            if (args[0] == null)
                return null;
            List<Object> list = new ArrayList<>();
            if (args[0] instanceof List) {
                list.addAll((List<?>) args[0]);
            } else if (args[0].getClass().isArray()) {
                int length = java.lang.reflect.Array.getLength(args[0]);
                for (int i = 0; i < length; i++) {
                    list.add(java.lang.reflect.Array.get(args[0], i));
                }
            } else {
                return args[0];
            }

            list.sort((a, b) -> {
                if (a == null && b == null)
                    return 0;
                if (a == null)
                    return -1;
                if (b == null)
                    return 1;
                if (a instanceof Comparable && b instanceof Comparable) {
                    try {
                        @SuppressWarnings({ "unchecked", "rawtypes" })
                        int result = ((Comparable) a).compareTo(b);
                        return result;
                    } catch (Exception e) {
                        return 0;
                    }
                }
                return a.toString().compareTo(b.toString());
            });
            return list;
        });

        context.registerFunction("reverse", args -> {
            if (args[0] == null)
                return null;
            List<Object> list = new ArrayList<>();
            if (args[0] instanceof List) {
                list.addAll((List<?>) args[0]);
            } else if (args[0].getClass().isArray()) {
                int length = java.lang.reflect.Array.getLength(args[0]);
                for (int i = 0; i < length; i++) {
                    list.add(java.lang.reflect.Array.get(args[0], i));
                }
            } else {
                return args[0];
            }
            java.util.Collections.reverse(list);
            return list;
        });
    }
}