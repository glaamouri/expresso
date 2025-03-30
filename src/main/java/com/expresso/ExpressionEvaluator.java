package com.expresso;

import com.expresso.ast.Expression;
import com.expresso.context.Context;
import com.expresso.exception.EvaluationException;
import com.expresso.exception.SyntaxException;
import com.expresso.parser.Parser;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Main entry point for the expression evaluator library. Provides methods to parse and evaluate
 * expressions.
 */
public class ExpressionEvaluator {
  private final Parser parser;
  private final Map<String, Function<Object[], Object>> customFunctions = new HashMap<>();

  public ExpressionEvaluator() {
    this.parser = new Parser();
  }

  /**
   * Parses an expression string into an AST
   *
   * @param expression The expression string to parse
   * @return The parsed Expression AST
   * @throws SyntaxException if the expression is malformed
   */
  public Expression parse(String expression) {
    return parser.parse(expression);
  }

  /**
   * Registers a custom function that can be used in expressions.
   *
   * @param name The name of the function as it will be used in expressions
   * @param function The function implementation that takes an array of arguments and returns a
   *     result
   */
  public void registerFunction(String name, Function<Object[], Object> function) {
    customFunctions.put(name, function);
  }

  /**
   * Directly evaluates an expression string with the given context
   *
   * @param expression The expression string to evaluate
   * @param context The context containing variables and functions
   * @return The evaluation result
   * @throws EvaluationException if evaluation fails
   */
  public Object evaluate(String expression, Context context) {
    // Register custom functions with the context
    customFunctions.forEach(context::registerFunction);

    Expression parsed = parse(expression);
    return evaluate(parsed, context);
  }

  /**
   * Evaluates a previously parsed expression with the given context
   *
   * @param expression The parsed Expression AST
   * @param context The context containing variables and functions
   * @return The evaluation result
   * @throws EvaluationException if evaluation fails
   */
  public Object evaluate(Expression expression, Context context) {
    // Register custom functions with the context
    customFunctions.forEach(context::registerFunction);

    return expression.evaluate(context);
  }
}
