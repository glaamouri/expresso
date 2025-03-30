package com.expresso.ast;

import com.expresso.context.Context;

/** AST node for null coalescing expressions (?? operator) */
public record NullCoalescingExpression(Expression left, Expression right) implements Expression {

  @Override
  public Object evaluate(Context context) {
    Object value = left.evaluate(context);

    if (value == null) {

      Object defaultValue = right.evaluate(context);

      if (defaultValue instanceof String strValue) {
          if ((strValue.startsWith("'") && strValue.endsWith("'"))
            || (strValue.startsWith("\"") && strValue.endsWith("\""))) {
          return strValue.substring(1, strValue.length() - 1);
        }
        return strValue;
      }
      return defaultValue;
    } else {
      return value;
    }
  }
}
