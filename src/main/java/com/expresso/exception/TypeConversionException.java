package com.expresso.exception;

/**
 * Exception thrown when a value cannot be converted to the expected type.
 * This occurs when type conversion fails, such as when trying to apply
 * numeric operations to non-numeric values.
 */
public class TypeConversionException extends EvaluationException {
    private final Object sourceValue;
    private final Class<?> targetType;

    /**
     * Creates a new TypeConversionException with the specified details.
     * 
     * @param sourceValue the value that could not be converted
     * @param targetType the type to which conversion was attempted
     * @param message details about the conversion error
     */
    public TypeConversionException(Object sourceValue, Class<?> targetType, String message) {
        super("Cannot convert " + (sourceValue == null ? "null" : sourceValue.toString()) + 
              " to " + targetType.getSimpleName() + ": " + message);
        this.sourceValue = sourceValue;
        this.targetType = targetType;
    }

    /**
     * Gets the source value that could not be converted.
     * 
     * @return the source value
     */
    public Object getSourceValue() {
        return sourceValue;
    }

    /**
     * Gets the target type to which conversion was attempted.
     * 
     * @return the target type
     */
    public Class<?> getTargetType() {
        return targetType;
    }
} 