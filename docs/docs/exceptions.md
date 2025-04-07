# Expresso Exception Handling

Expresso uses a structured exception hierarchy to provide clear and informative error messages when problems occur during expression parsing or evaluation. This document outlines the different types of exceptions you might encounter and how to handle them.

## Exception Hierarchy

All Expresso exceptions extend from `ExpressionException`, which is the base class for all expression-related exceptions. Understanding this hierarchy helps you catch specific types of errors and handle them appropriately.

```
ExpressionException
├── SyntaxException
├── EvaluationException
│   ├── FunctionExecutionException
│   │   └── MissingArgumentException
│   ├── TypeConversionException
│   ├── InvalidOperationException
│   │   └── ArithmeticExpressionException
│   └── UnknownFunctionException
└── PropertyNotFoundException
    ├── VariableNotFoundException
    ├── PropertyAccessException
    └── ArrayIndexOutOfBoundsException
```

## Common Exception Types

### Syntax Errors

- **SyntaxException**: Thrown when there is a syntax error in the expression, such as missing parentheses or invalid operators.

Example:
```java
try {
    evaluator.evaluate("5 + ", context);
} catch (SyntaxException e) {
    // Handle syntax error
    System.err.println("Syntax error: " + e.getMessage());
}
```

### Evaluation Errors

- **EvaluationException**: The base class for all errors that occur during expression evaluation.

- **FunctionExecutionException**: Thrown when an error occurs within a function call.
  - **MissingArgumentException**: Thrown when a function is called with fewer arguments than expected.

- **TypeConversionException**: Thrown when a value cannot be converted to the expected type.

- **InvalidOperationException**: Thrown when an operation is invalid between two operands.
  - **ArithmeticExpressionException**: Specifically for arithmetic errors like division by zero.

- **UnknownFunctionException**: Thrown when referencing a function that doesn't exist.

Example:
```java
try {
    evaluator.evaluate("5 / 0", context);
} catch (ArithmeticExpressionException e) {
    // Handle division by zero
    System.err.println("Arithmetic error: " + e.getMessage());
} catch (InvalidOperationException e) {
    // Handle other invalid operations
    System.err.println("Invalid operation: " + e.getMessage());
} catch (EvaluationException e) {
    // Handle generic evaluation errors
    System.err.println("Evaluation error: " + e.getMessage());
}
```

### Property Access Errors

- **PropertyNotFoundException**: The base class for errors related to accessing properties that don't exist.

- **VariableNotFoundException**: Thrown when referencing a variable that doesn't exist in the context.

- **PropertyAccessException**: Thrown when a property exists but cannot be accessed.

- **ArrayIndexOutOfBoundsException**: Thrown when an array or list index is out of bounds.

Example:
```java
try {
    evaluator.evaluate("$nonExistentVar", context);
} catch (VariableNotFoundException e) {
    // Handle missing variable
    System.err.println("Variable not found: " + e.getVariableName());
} catch (ArrayIndexOutOfBoundsException e) {
    // Handle array index out of bounds
    System.err.println("Array index out of bounds: " + e.getMessage());
} catch (PropertyNotFoundException e) {
    // Handle generic property not found
    System.err.println("Property error: " + e.getMessage());
}
```

## Best Practices

1. **Catch Specific Exceptions First**: Always catch the most specific exceptions first, followed by more general ones.

2. **Use Exception Properties**: Many exceptions provide additional information through methods like `getFunctionName()`, `getVariableName()`, etc.

3. **User-Friendly Error Messages**: Translate exceptions into user-friendly error messages that explain how to fix the issue.

4. **Default Values**: Consider using null-safe operators (`?.`) and null coalescing (`??`) to handle potential errors gracefully.

## Null Safety and Error Prevention

Expresso provides several features to help prevent exceptions:

1. **Null-safe property access**: Use the `?.` operator
   ```
   $user?.address?.city
   ```

2. **Null coalescing**: Provide default values with the `??` operator
   ```
   $user?.address?.city ?? "Unknown"
   ```

3. **Defensive functions**: Built-in functions like `isNull()`, `isEmpty()`, and `coalesce()`
   ```
   coalesce($possiblyNullVar, "default value")
   ```

These features can help reduce the need for extensive exception handling in many cases. 