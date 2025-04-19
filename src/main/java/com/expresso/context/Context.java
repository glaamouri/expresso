package com.expresso.context;

import com.expresso.context.functions.FunctionRegistry;
import com.expresso.exception.ArrayIndexOutOfBoundsException;
import com.expresso.exception.PropertyAccessException;
import com.expresso.exception.PropertyNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/** Context class that holds variables and functions for expression evaluation. */
public class Context {
  private final Map<String, Object> variables;
  private final Map<String, Function<Object[], Object>> functions;

  public Context() {
    this.variables = new HashMap<>();
    this.functions = new HashMap<>();
    registerBuiltInFunctions();
  }
  
  /**
   * Creates a new Context with a single variable
   * 
   * @param name The variable name
   * @param value The variable value
   * @return A new Context with the specified variable
   */
  public static Context of(String name, Object value) {
    Context context = new Context();
    context.setVariable(name, value);
    return context;
  }
  
  /**
   * Creates a new Context with variables from a map
   * 
   * @param variables Map of variable names to values
   * @return A new Context with the specified variables
   */
  public static Context of(Map<String, Object> variables) {
    Context context = new Context();
    variables.forEach(context::setVariable);
    return context;
  }

  /**
   * Sets a variable in the context
   *
   * @param name The variable name
   * @param value The variable value
   */
  public void setVariable(String name, Object value) {
    variables.put(name, value);
  }
  
  /**
   * Sets a variable in the context with a fluent interface
   * 
   * @param name The variable name
   * @param value The variable value
   * @return This context for method chaining
   */
  public Context with(String name, Object value) {
    variables.put(name, value);
    return this;
  }
  
  /**
   * Sets multiple variables from a map with a fluent interface
   * 
   * @param variables Map of variable names to values
   * @return This context for method chaining
   */
  public Context withAll(Map<String, Object> variables) {
    variables.forEach(this::setVariable);
    return this;
  }

  /**
   * Gets a variable from the context
   *
   * @param name The variable name
   * @return The variable value
   */
  public Object getVariable(String name) {
    return variables.get(name);
  }

  /**
   * Checks if a variable exists in the context
   *
   * @param name The variable name
   * @return true if the variable exists, false otherwise
   */
  public boolean variableExists(String name) {
    return variables.containsKey(name);
  }

  /**
   * Registers a function in the context
   *
   * @param name The function name
   * @param function The function implementation
   */
  public void registerFunction(String name, Function<Object[], Object> function) {
    functions.put(name, function);
  }

  /**
   * Gets a function from the context
   *
   * @param name The function name
   * @return The function implementation
   */
  public Function<Object[], Object> getFunction(String name) {
    return functions.get(name);
  }

  /**
   * Resolves a property path on an object
   *
   * @param target The target object
   * @param property The property path (e.g., "user.address.city")
   * @param isNullSafe Whether to use null-safe property access
   * @return The resolved property value
   */
  public Object resolveProperty(Object target, String property, boolean isNullSafe) {

    if (target == null) {
      if (isNullSafe) {
        return null;
      }
      throw new PropertyNotFoundException("Cannot access property on null value");
    }

    // Handle array access notation
    // Check for null-safe array access
    if (property.contains("?[") && property.contains("]")) {
      int bracketIndex = property.indexOf("?[");
      String arrayProperty = bracketIndex > 0 ? property.substring(0, bracketIndex) : "";
      int closingBracketIndex = property.indexOf("]", bracketIndex);
      String indexStr = property.substring(bracketIndex + 2, closingBracketIndex);
      int index = Integer.parseInt(indexStr);

      // If there's no array property, use the target directly
      Object array =
          arrayProperty.isEmpty() ? target : resolveProperty(target, arrayProperty, isNullSafe);

      if (array == null) {
        return null; // Null-safe access, return null instead of throwing
      }

      // Then access the array element
      Object element = null;
      if (array instanceof List) {
        List<?> list = (List<?>) array;

        if (index < 0 || index >= list.size()) {
          return null; // Null-safe access, return null instead of throwing
        }
        element = list.get(index);
      } else if (array.getClass().isArray()) {
        int length = java.lang.reflect.Array.getLength(array);
        if (index < 0 || index >= length) {
          return null; // Null-safe access, return null instead of throwing
        }
        element = java.lang.reflect.Array.get(array, index);
      } else {
        return null; // Null-safe access, return null instead of throwing
      }
      
      // Check if there are more properties to resolve after the array access
      if (closingBracketIndex < property.length() - 1) {
        String remainingProperty = property.substring(closingBracketIndex + 1);
        if (remainingProperty.startsWith(".")) {
          remainingProperty = remainingProperty.substring(1);
        }
        return resolveProperty(element, remainingProperty, isNullSafe);
      }
      
      return element;
    }
    // Handle regular array access
    else if (property.contains("[") && property.contains("]")) {
      int bracketIndex = property.indexOf("[");
      String arrayProperty = bracketIndex > 0 ? property.substring(0, bracketIndex) : "";
      int closingBracketIndex = property.indexOf("]", bracketIndex);
      String indexStr = property.substring(bracketIndex + 1, closingBracketIndex);
      int index = Integer.parseInt(indexStr);

      // If there's no array property, use the target directly
      Object array =
          arrayProperty.isEmpty() ? target : resolveProperty(target, arrayProperty, isNullSafe);

      if (array == null) {
        if (isNullSafe) {
          return null;
        }
        throw new PropertyNotFoundException("Cannot access array on null value");
      }

      // Then access the array element
      Object element = null;
      if (array instanceof List) {
        List<?> list = (List<?>) array;

        if (index < 0 || index >= list.size()) {
          if (isNullSafe) {
            return null;
          }
          throw new ArrayIndexOutOfBoundsException(list, index, list.size());
        }
        element = list.get(index);
      } else if (array.getClass().isArray()) {
        int length = java.lang.reflect.Array.getLength(array);
        if (index < 0 || index >= length) {
          if (isNullSafe) {
            return null;
          }
          throw new ArrayIndexOutOfBoundsException(array, index, length);
        }
        element = java.lang.reflect.Array.get(array, index);
      } else {
        if (isNullSafe) {
          return null;
        }
        throw new PropertyAccessException(array, property, "Cannot access index on non-array/list type");
      }
      
      // Check if there are more properties to resolve after the array access
      if (closingBracketIndex < property.length() - 1) {
        String remainingProperty = property.substring(closingBracketIndex + 1);
        if (remainingProperty.startsWith(".")) {
          remainingProperty = remainingProperty.substring(1);
        }
        return resolveProperty(element, remainingProperty, isNullSafe);
      }
      
      return element;
    }

    // Handle regular property access
    String[] parts = property.split("\\.");
    Object current = target;

    for (String part : parts) {
      if (current == null) {
        if (isNullSafe) {
          return null;
        }
        throw new PropertyNotFoundException("Cannot access property on null value");
      }

      if (current instanceof Map) {
        current = ((Map<?, ?>) current).get(part);
      } else {
        try {
          current =
              current
                  .getClass()
                  .getMethod("get" + Character.toUpperCase(part.charAt(0)) + part.substring(1))
                  .invoke(current);
        } catch (Exception e) {
          if (isNullSafe) {
            return null;
          }
          throw new PropertyAccessException(current, part, "Property not found", e);
        }
      }
    }

    return current;
  }

  /**
   * Resolves a property path on an object (non-null-safe version)
   *
   * @param target The target object
   * @param property The property path (e.g., "user.address.city")
   * @return The resolved property value
   */
  public Object resolveProperty(Object target, String property) {
    return resolveProperty(target, property, false);
  }

  private void registerBuiltInFunctions() {
    // Register all built-in functions from the function registry
    FunctionRegistry.registerAllFunctions(this);
  }
}
