package com.expresso.exception;

/**
 * Exception thrown when a property exists but cannot be accessed due to reflection or access errors.
 * This occurs when a property exists on an object but cannot be read due to 
 * Java access controls or other reflection-related issues.
 */
public class PropertyAccessException extends PropertyNotFoundException {
    private final Object target;
    private final String property;

    /**
     * Creates a new PropertyAccessException with the specified details.
     * 
     * @param target the target object on which property access failed
     * @param property the name of the property that could not be accessed
     * @param message details about the access error
     */
    public PropertyAccessException(Object target, String property, String message) {
        super("Cannot access property '" + property + "' on " + 
              (target == null ? "null" : target.getClass().getSimpleName()) + 
              (message != null && !message.isEmpty() ? ": " + message : ""));
        this.target = target;
        this.property = property;
    }

    /**
     * Creates a new PropertyAccessException with the specified details and cause.
     * 
     * @param target the target object on which property access failed
     * @param property the name of the property that could not be accessed
     * @param message details about the access error
     * @param cause the underlying cause of the exception
     */
    public PropertyAccessException(Object target, String property, String message, Throwable cause) {
        super("Cannot access property '" + property + "' on " + 
              (target == null ? "null" : target.getClass().getSimpleName()) + 
              (message != null && !message.isEmpty() ? ": " + message : ""), cause);
        this.target = target;
        this.property = property;
    }

    /**
     * Gets the target object on which property access failed.
     * 
     * @return the target object
     */
    public Object getTarget() {
        return target;
    }

    /**
     * Gets the name of the property that could not be accessed.
     * 
     * @return the property name
     */
    public String getProperty() {
        return property;
    }
} 