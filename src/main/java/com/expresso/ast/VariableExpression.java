package com.expresso.ast;

import com.expresso.context.Context;
import com.expresso.exception.PropertyNotFoundException;

/** AST node for variable references and property access */
public class VariableExpression implements Expression {
  private final String name;
  private final String propertyPath;
  private final boolean isNullSafe;

  public VariableExpression(String name) {
    this(name, null, false);
  }

  public VariableExpression(String name, String propertyPath) {
    this(name, propertyPath, false);
  }

  public VariableExpression(String name, String propertyPath, boolean isNullSafe) {
    this.name = name;
    this.propertyPath = propertyPath;
    this.isNullSafe = isNullSafe;
  }

  @Override
  public Object evaluate(Context context) {
    Object value = context.getVariable(name);

    // If there's no property path, handle the variable value directly
    if (propertyPath == null) {
      if (value == null && !isNullSafe) {
        throw new PropertyNotFoundException("Variable not found: " + name);
      }
      return value;
    }

    // If there is a property path, resolve it with null-safe handling
    try {
      if (value == null && !isNullSafe) {
        throw new PropertyNotFoundException("Variable not found: " + name);
      }

        return context.resolveProperty(value, propertyPath, isNullSafe);
    } catch (PropertyNotFoundException e) {

      if (isNullSafe) {

        return null;
      }
      throw new PropertyNotFoundException("Property not found: " + name + "." + propertyPath, e);
    }
  }

  public String getName() {
    return name;
  }

  public String getPropertyPath() {
    return propertyPath;
  }

  public boolean isNullSafe() {
    return isNullSafe;
  }
}
