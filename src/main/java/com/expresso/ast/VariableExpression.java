package com.expresso.ast;

import com.expresso.context.Context;
import com.expresso.exception.PropertyNotFoundException;
import com.expresso.exception.VariableNotFoundException;

/** AST node for variable references and property access */
public class VariableExpression implements Expression {
  private final String name;
  private final String propertyPath;
  private final boolean isNullSafe;
  // A flag to indicate if this variable is being used in a safe context
  private boolean inSafeContext = false;

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

  /**
   * Mark this variable as being used in a context where non-existence should return null rather than throwing.
   * This is used for functions like isNull and coalesce.
   */
  public void setInSafeContext(boolean inSafeContext) {
    this.inSafeContext = inSafeContext;
  }

  @Override
  public Object evaluate(Context context) {
    // Check if the variable exists in the context
    boolean exists = context.variableExists(name);
    Object value = context.getVariable(name);

    // If there's no property path, handle the variable value directly
    if (propertyPath == null) {
      if (value == null && !exists && !isNullSafe && !inSafeContext) {
        throw new VariableNotFoundException(name);
      }
      return value;
    }

    // If there is a property path, resolve it with null-safe handling
    try {
      if (value == null && !exists && !isNullSafe && !inSafeContext) {
        throw new VariableNotFoundException(name);
      }

      return context.resolveProperty(value, propertyPath, isNullSafe);
    } catch (PropertyNotFoundException e) {
      if (isNullSafe || inSafeContext) {
        return null;
      }
      throw e;
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
