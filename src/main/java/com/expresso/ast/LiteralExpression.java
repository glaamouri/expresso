package com.expresso.ast;

import com.expresso.context.Context;

/** AST node for literal values (strings, numbers, booleans, etc.) */
public record LiteralExpression(Object value) implements Expression {

  @Override
  public Object evaluate(Context context) {
    return value;
  }
}
