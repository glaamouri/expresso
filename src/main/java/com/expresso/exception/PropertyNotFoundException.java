package com.expresso.exception;

/** 
 * Exception thrown when a property cannot be found on an object.
 * This exception is raised when attempting to access a property or
 * variable that doesn't exist or is inaccessible.
 */
public class PropertyNotFoundException extends ExpressionException {
  /**
   * Creates a new PropertyNotFoundException with the specified message.
   * 
   * @param message details about the missing property
   */
  public PropertyNotFoundException(String message) {
    super(message);
  }

  /**
   * Creates a new PropertyNotFoundException with the specified message and cause.
   * 
   * @param message details about the missing property
   * @param cause the underlying cause of the exception
   */
  public PropertyNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
