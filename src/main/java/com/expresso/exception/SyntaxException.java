package com.expresso.exception;

/** 
 * Exception thrown when there is a syntax error in the expression.
 * This exception indicates problems with the parsing phase, such as
 * malformed expressions, unexpected tokens, or invalid syntax.
 */
public class SyntaxException extends ExpressionException {
  private final int position;

  /**
   * Creates a new SyntaxException with the specified message and position.
   * 
   * @param message details about the syntax error
   * @param position the position in the input where the error occurred (0-based)
   */
  public SyntaxException(String message, int position) {
    super(message);
    this.position = position;
  }

  /**
   * Creates a new SyntaxException with just a message, with an unknown position.
   * 
   * @param message details about the syntax error
   */
  public SyntaxException(String message) {
    this(message, -1);
  }

  /**
   * Creates a new SyntaxException with the specified message, position, and cause.
   * 
   * @param message details about the syntax error
   * @param position the position in the input where the error occurred (0-based)
   * @param cause the underlying cause of the exception
   */
  public SyntaxException(String message, int position, Throwable cause) {
    super(message, cause);
    this.position = position;
  }

  /**
   * Creates a new SyntaxException with the specified message and cause, with an unknown position.
   * 
   * @param message details about the syntax error
   * @param cause the underlying cause of the exception
   */
  public SyntaxException(String message, Throwable cause) {
    this(message, -1, cause);
  }

  /**
   * Gets the position in the input where the error occurred.
   * 
   * @return the position (0-based), or -1 if the position is unknown
   */
  public int getPosition() {
    return position;
  }
}
