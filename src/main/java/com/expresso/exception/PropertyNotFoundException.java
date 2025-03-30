package com.expresso.exception;

/** Exception thrown when a property cannot be found on an object. */
public class PropertyNotFoundException extends RuntimeException {
  public PropertyNotFoundException(String message) {
    super(message);
  }

  public PropertyNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
