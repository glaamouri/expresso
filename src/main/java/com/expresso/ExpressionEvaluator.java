package com.expresso;

import com.expresso.ast.Expression;
import com.expresso.context.Context;
import com.expresso.exception.EvaluationException;
import com.expresso.exception.PropertyNotFoundException;
import com.expresso.exception.SyntaxException;
import com.expresso.exception.VariableNotFoundException;
import com.expresso.parser.Parser;
import com.expresso.validation.ExpressionError;
import com.expresso.validation.ValidationResult;

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
  
  /**
   * Validates if an expression has valid syntax, without checking variable existence.
   * 
   * @param expression The expression string to validate
   * @return true if the expression has valid syntax, false otherwise
   * @deprecated Use {@link #validateSyntax(String)} instead for more detailed error information
   */
  @Deprecated
  public boolean validate(String expression) {
    ValidationResult result = validateSyntax(expression);
    return result.isValid();
  }
  
  /**
   * Validates if an expression is valid with the given context.
   * This checks both syntax and variable/function references.
   *
   * @param expression The expression string to validate
   * @param context The context containing variables and functions to check against
   * @return true if the expression is valid with the given context, false otherwise
   * @deprecated Use {@link #validateWithContext(String, Context)} instead for more detailed error information
   */
  @Deprecated
  public boolean validate(String expression, Context context) {
    ValidationResult result = validateWithContext(expression, context);
    return result.isValid();
  }
  
  /**
   * Validates the syntax of an expression without checking variable existence.
   * 
   * @param expression The expression string to validate
   * @return A ValidationResult object containing the result of the validation
   */
  public ValidationResult validateSyntax(String expression) {
    try {
      parse(expression);
      return ValidationResult.success();
    } catch (SyntaxException e) {
      // Create a syntax error with position information if available
      ExpressionError error;
      if (e.getPosition() >= 0) {
        ExpressionError.ErrorLocation location = new ExpressionError.ErrorLocation(
            e.getPosition(), 
            Math.min(e.getPosition() + 10, expression.length()), 
            expression);
        error = new ExpressionError("SyntaxError", e.getMessage(), location);
      } else {
        error = new ExpressionError("SyntaxError", e.getMessage());
      }
      return ValidationResult.failure(error);
    } catch (Exception e) {
      // Other unexpected exceptions are treated as general validation errors
      ExpressionError error = new ExpressionError("ValidationError", e.getMessage());
      return ValidationResult.failure(error);
    }
  }
  
  /**
   * Validates if an expression is valid with the given context.
   * This checks both syntax and variable/function references.
   *
   * @param expression The expression string to validate
   * @param context The context containing variables and functions to check against
   * @return A ValidationResult object containing the result of the validation
   */
  public ValidationResult validateWithContext(String expression, Context context) {
    // First check syntax
    ValidationResult syntaxResult = validateSyntax(expression);
    if (!syntaxResult.isValid()) {
      return syntaxResult;
    }
    
    try {
      // Parse the expression
      Expression parsed = parse(expression);
      
      // Register custom functions with the context for validation
      customFunctions.forEach(context::registerFunction);
      
      try {
        // Try to evaluate the expression
        parsed.evaluate(context);
        return ValidationResult.success();
      } catch (VariableNotFoundException e) {
        // Special handling for null-safe access - check if the expression uses ?. or ?[ 
        if (expression.contains("?.") || expression.contains("?[")) {
          String varName = e.getVariableName();
          // If the expression has null-safe operators followed by the variable, consider it valid
          if (expression.contains("?." + varName) || expression.contains("?[" + varName + "]")) {
            return ValidationResult.success();
          }
        }
        
        ExpressionError error = new ExpressionError(
            "VariableNotFoundError", 
            "Variable '" + e.getVariableName() + "' not found", 
            createLocationFromVariableName(expression, e.getVariableName()));
        return ValidationResult.failure(error);
      } catch (PropertyNotFoundException e) {
        // If using null-safe access, property not found exceptions should not be considered errors
        if (expression.contains("?.") || expression.contains("?[")) {
          return ValidationResult.success();
        }
        
        ExpressionError error = new ExpressionError(
            "PropertyNotFoundError", 
            e.getMessage());
        return ValidationResult.failure(error);
      } 
    } catch (Exception e) {
      ExpressionError error = new ExpressionError(
          "EvaluationError", 
          e.getMessage());
      return ValidationResult.failure(error);
    }
  }
  
  /**
   * Creates an error location for a variable name in an expression
   * 
   * @param expression The full expression string
   * @param variableName The variable name to locate
   * @return An ErrorLocation object, or null if the variable name couldn't be found
   */
  private ExpressionError.ErrorLocation createLocationFromVariableName(String expression, String variableName) {
    if (expression == null || variableName == null) {
      return null;
    }
    
    // Look for the variable in the expression (prefixed with $)
    int startPos = expression.indexOf("$" + variableName);
    
    if (startPos >= 0) {
      return new ExpressionError.ErrorLocation(
          startPos, 
          startPos + variableName.length() + 1, // +1 for the $
          expression);
    }
    
    return null;
  }
}
