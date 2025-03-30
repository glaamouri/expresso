package com.expresso.exception;

/** Exception thrown when there is an error during expression evaluation. */
public class EvaluationException extends RuntimeException {
  public EvaluationException(String message) {
    super(message);
  }

  public EvaluationException(String message, Throwable cause) {
    super(message, cause);
  }
}
