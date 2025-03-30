package com.expresso.exception;

/** Exception thrown when there is a syntax error in the expression. */
public class SyntaxException extends RuntimeException {
  public SyntaxException(String message) {
    super(message);
  }

  public SyntaxException(String message, Throwable cause) {
    super(message, cause);
  }
}
