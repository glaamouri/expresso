package com.expresso.exception;

/**
 * Exception thrown when a variable is referenced but cannot be found in the context.
 * This occurs when an expression attempts to use a variable that has not been defined.
 */
public class VariableNotFoundException extends PropertyNotFoundException {
    private final String variableName;

    /**
     * Creates a new VariableNotFoundException with the specified variable name.
     * 
     * @param variableName the name of the undefined variable
     */
    public VariableNotFoundException(String variableName) {
        super("Variable not found: " + variableName);
        this.variableName = variableName;
    }

    /**
     * Gets the name of the undefined variable.
     * 
     * @return the variable name
     */
    public String getVariableName() {
        return variableName;
    }
} 