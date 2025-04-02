package com.expresso.exception;

/** 
 * Exception thrown when there is a syntax error in the expression.
 * This exception indicates problems with the parsing phase, such as
 * malformed expressions, unexpected tokens, or invalid syntax.
 */
public class SyntaxException extends ExpressionException {
  /**
   * Creates a new SyntaxException with the specified message.
   * 
   * @param message details about the syntax error
   */
  public SyntaxException(String message) {
    super(message);
  }

  /**
   * Creates a new SyntaxException with the specified message and cause.
   * 
   * @param message details about the syntax error
   * @param cause the underlying cause of the exception
   */
  public SyntaxException(String message, Throwable cause) {
    super(message, cause);
  }
}
