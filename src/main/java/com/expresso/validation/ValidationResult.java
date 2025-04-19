package com.expresso.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that represents the result of expression validation
 */
public class ValidationResult {
    private final boolean valid;
    private final List<ExpressionError> errors;

    private ValidationResult(boolean valid, List<ExpressionError> errors) {
        this.valid = valid;
        this.errors = errors != null ? Collections.unmodifiableList(errors) : Collections.emptyList();
    }

    /**
     * Creates a successful validation result with no errors
     * 
     * @return A successful validation result
     */
    public static ValidationResult success() {
        return new ValidationResult(true, Collections.emptyList());
    }

    /**
     * Creates a failed validation result with errors
     * 
     * @param errors The list of errors that caused validation to fail
     * @return A failed validation result
     */
    public static ValidationResult failure(List<ExpressionError> errors) {
        return new ValidationResult(false, errors);
    }

    /**
     * Creates a failed validation result with a single error
     * 
     * @param error The error that caused validation to fail
     * @return A failed validation result
     */
    public static ValidationResult failure(ExpressionError error) {
        List<ExpressionError> errors = new ArrayList<>();
        errors.add(error);
        return new ValidationResult(false, errors);
    }

    /**
     * Checks if the expression is valid
     * 
     * @return true if the expression is valid, false otherwise
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Gets the list of errors if validation failed
     * 
     * @return An unmodifiable list of validation errors
     */
    public List<ExpressionError> getErrors() {
        return errors;
    }

    /**
     * Gets the first error if validation failed
     * 
     * @return The first error, or null if there are no errors
     */
    public ExpressionError getFirstError() {
        return errors.isEmpty() ? null : errors.get(0);
    }

    /**
     * Checks if there are any errors
     * 
     * @return true if there are no errors, false otherwise
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    @Override
    public String toString() {
        if (valid) {
            return "ValidationResult{valid=true}";
        } else {
            return "ValidationResult{valid=false, errors=" + errors + "}";
        }
    }
} 