package com.expresso.exception;

/** 
 * Exception thrown when there is an error during expression evaluation.
 * This covers errors that occur during the evaluation of a syntactically
 * valid expression, such as undefined functions, type errors, or
 * invalid operations.
 */
public class EvaluationException extends ExpressionException {
  /**
   * Creates a new EvaluationException with the specified message.
   * 
   * @param message details about the evaluation error
   */
  public EvaluationException(String message) {
    super(message);
  }

  /**
   * Creates a new EvaluationException with the specified message and cause.
   * 
   * @param message details about the evaluation error
   * @param cause the underlying cause of the exception
   */
  public EvaluationException(String message, Throwable cause) {
    super(message, cause);
  }
}
