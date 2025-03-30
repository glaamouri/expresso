package com.expresso.ast;

import com.expresso.context.Context;

/** Base interface for all AST nodes in the expression evaluator. */
public interface Expression {
  /**
   * Evaluates this expression in the given context
   *
   * @param context The evaluation context
   * @return The evaluation result
   */
  Object evaluate(Context context);
}
