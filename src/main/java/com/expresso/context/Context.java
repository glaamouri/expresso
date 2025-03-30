package com.expresso.context;

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
   * Sets a variable in the context
   *
   * @param name The variable name
   * @param value The variable value
   */
  public void setVariable(String name, Object value) {
    variables.put(name, value);
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

    // Check for null-safe array access
    if (property.contains("?[") && property.endsWith("]")) {
      int bracketIndex = property.indexOf("?[");
      String arrayProperty = bracketIndex > 0 ? property.substring(0, bracketIndex) : "";
      String indexStr = property.substring(bracketIndex + 2, property.length() - 1);
      int index = Integer.parseInt(indexStr);

      // If there's no array property, use the target directly
      Object array =
          arrayProperty.isEmpty() ? target : resolveProperty(target, arrayProperty, isNullSafe);

      if (array == null) {

        return null; // Null-safe access, return null instead of throwing
      }

      // Then access the array element
      if (array instanceof List) {
        List<?> list = (List<?>) array;

        if (index < 0 || index >= list.size()) {

          return null; // Null-safe access, return null instead of throwing
        }
        return list.get(index);
      } else if (array.getClass().isArray()) {
        int length = java.lang.reflect.Array.getLength(array);
        if (index < 0 || index >= length) {
          return null; // Null-safe access, return null instead of throwing
        }
        return java.lang.reflect.Array.get(array, index);
      } else {
        return null; // Null-safe access, return null instead of throwing
      }
    }
    // Handle regular array access
    else if (property.contains("[") && property.endsWith("]")) {
      int bracketIndex = property.indexOf("[");
      String arrayProperty = bracketIndex > 0 ? property.substring(0, bracketIndex) : "";
      String indexStr = property.substring(bracketIndex + 1, property.length() - 1);
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
      if (array instanceof List) {
        List<?> list = (List<?>) array;

        if (index < 0 || index >= list.size()) {
          if (isNullSafe) {

            return null;
          }
          throw new PropertyNotFoundException("Array index out of bounds: " + index);
        }
        return list.get(index);
      } else if (array.getClass().isArray()) {
        int length = java.lang.reflect.Array.getLength(array);
        if (index < 0 || index >= length) {
          if (isNullSafe) {
            return null;
          }
          throw new PropertyNotFoundException("Array index out of bounds: " + index);
        }
        return java.lang.reflect.Array.get(array, index);
      } else {
        if (isNullSafe) {
          return null;
        }
        throw new PropertyNotFoundException(
            "Cannot access index on non-array/list type: " + array.getClass());
      }
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
          throw new PropertyNotFoundException("Property not found: " + part, e);
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
    // String functions
    registerFunction("upperCase", args -> ((String) args[0]).toUpperCase());
    registerFunction("lowerCase", args -> ((String) args[0]).toLowerCase());
    registerFunction("length", args -> ((String) args[0]).length());
    registerFunction("trim", args -> ((String) args[0]).trim());

    // Math functions
    registerFunction("abs", args -> Math.abs(((Number) args[0]).doubleValue()));
    registerFunction("ceil", args -> Math.ceil(((Number) args[0]).doubleValue()));
    registerFunction("floor", args -> Math.floor(((Number) args[0]).doubleValue()));
    registerFunction("round", args -> Math.round(((Number) args[0]).doubleValue()));

    // Logic functions
    registerFunction("isNull", args -> args[0] == null);
    registerFunction(
        "coalesce",
        args -> {
          for (Object arg : args) {
            if (arg != null) return arg;
          }
          return null;
        });
  }
}
