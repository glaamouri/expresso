package com.expresso.context.functions;

import com.expresso.context.Context;
import java.util.List;

/**
 * Provider for string manipulation functions.
 */
public class StringFunctions implements FunctionProvider {
    
    @Override
    public void registerFunctions(Context context) {
        context.registerFunction("upperCase", args -> ((String) args[0]).toUpperCase());
        context.registerFunction("lowerCase", args -> ((String) args[0]).toLowerCase());
        context.registerFunction("length", args -> ((String) args[0]).length());
        context.registerFunction("trim", args -> ((String) args[0]).trim());
        context.registerFunction("substring", args -> {
            String str = (String) args[0];
            int start = ((Number) args[1]).intValue();
            int length = args.length > 2 ? ((Number) args[2]).intValue() : str.length() - start;
            return str.substring(start, Math.min(start + length, str.length()));
        });
        context.registerFunction("replace", args -> {
            String str = (String) args[0];
            String oldStr = (String) args[1];
            String newStr = (String) args[2];
            return str.replace(oldStr, newStr);
        });
        context.registerFunction("contains", args -> {
            // Handle null container
            if (args[0] == null) {
                return false;
            }
            
            Object container = args[0];
            Object searchValue = args[1];
            
            // String contains
            if (container instanceof String) {
                if (searchValue instanceof String) {
                    return ((String) container).contains((String) searchValue);
                }
                return false; // String can only contain other strings
            }
            // List contains
            else if (container instanceof List) {
                List<?> list = (List<?>) container;
                
                // Handle null search value specifically to avoid NPE
                if (searchValue == null) {
                    // Check if list contains null element
                    for (Object item : list) {
                        if (item == null) {
                            return true;
                        }
                    }
                    return false;
                }
                
                // Handle numeric comparison specially
                if (searchValue instanceof Number) {
                    Number searchNumber = (Number) searchValue;
                    for (Object item : list) {
                        if (item instanceof Number && 
                            ((Number) item).doubleValue() == searchNumber.doubleValue()) {
                            return true;
                        }
                    }
                }
                
                // Default list contains check
                return list.contains(searchValue);
            } 
            // Array contains
            else if (container.getClass().isArray()) {
                int length = java.lang.reflect.Array.getLength(container);
                
                // Handle null search value
                if (searchValue == null) {
                    for (int i = 0; i < length; i++) {
                        if (java.lang.reflect.Array.get(container, i) == null) {
                            return true;
                        }
                    }
                    return false;
                }
                
                for (int i = 0; i < length; i++) {
                    Object element = java.lang.reflect.Array.get(container, i);
                    
                    // Skip null elements for non-null search values
                    if (element == null) {
                        continue;
                    }
                    
                    // Handle numeric equality for arrays when comparing with numbers
                    if (element instanceof Number && searchValue instanceof Number) {
                        if (((Number) element).doubleValue() == ((Number) searchValue).doubleValue()) {
                            return true;
                        }
                    }
                    // Otherwise use equals
                    else if (element.equals(searchValue) || searchValue.equals(element)) {
                        return true;
                    }
                }
                return false;
            }
            // Default not found
            return false;
        });
        context.registerFunction("startsWith", args -> {
            String str = (String) args[0];
            String prefix = (String) args[1];
            return str.startsWith(prefix);
        });
        context.registerFunction("endsWith", args -> {
            String str = (String) args[0];
            String suffix = (String) args[1];
            return str.endsWith(suffix);
        });
        context.registerFunction("split", args -> {
            String str = (String) args[0];
            String delimiter = (String) args[1];
            return List.of(str.split(delimiter));
        });
        context.registerFunction("join", args -> {
            String delimiter = (String) args[0];
            List<?> elements = (List<?>) args[1];
            return String.join(delimiter, elements.stream().map(Object::toString).toList());
        });
        context.registerFunction("charAt", args -> {
            String str = (String) args[0];
            int index = ((Number) args[1]).intValue();
            return String.valueOf(str.charAt(index));
        });
        context.registerFunction("indexOf", args -> {
            String str = (String) args[0];
            String search = (String) args[1];
            return str.indexOf(search);
        });
    }
} 