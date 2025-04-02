---
id: error-handling
title: Error Handling
sidebar_position: 5
---

# Error Handling

Expresso provides robust error handling mechanisms to help you manage errors in your expressions.

## Exception Hierarchy

Expresso uses a structured exception hierarchy to provide clear and informative error messages. All exceptions extend from `ExpressionException`, which is the base class for all expression-related exceptions.

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

Understanding this hierarchy helps you catch specific types of errors and handle them appropriately.

## Syntax Errors

The `SyntaxException` is thrown when there's a syntax error in the expression, such as missing parentheses or invalid operators.

```java
try {
    evaluator.evaluate("5 + ", context);
} catch (SyntaxException e) {
    System.err.println("Syntax error: " + e.getMessage());
    // e.g. "Unexpected end of input, expected expression"
}
```

## Evaluation Errors

The `EvaluationException` is the base class for all errors that occur during expression evaluation. It has several specialized subclasses:

### Function Execution Errors

- **FunctionExecutionException**: Thrown when an error occurs within a function call
- **MissingArgumentException**: Thrown when a function is called with fewer arguments than expected
- **UnknownFunctionException**: Thrown when referencing a function that doesn't exist

```java
try {
    evaluator.evaluate("pow(2)", context); // Missing second argument
} catch (MissingArgumentException e) {
    System.err.println("Missing argument: " + e.getMessage());
    System.err.println("Function: " + e.getFunctionName());
    System.err.println("Expected: " + e.getExpectedCount() + " arguments");
    System.err.println("Actual: " + e.getActualCount() + " arguments");
}

try {
    evaluator.evaluate("unknownFunction()", context);
} catch (UnknownFunctionException e) {
    System.err.println("Unknown function: " + e.getFunctionName());
}

try {
    evaluator.evaluate("upperCase(42)", context); // Passing a number to a string function
} catch (FunctionExecutionException e) {
    System.err.println("Function error: " + e.getMessage());
    System.err.println("Function name: " + e.getFunctionName());
}
```

### Type Conversion Errors

The `TypeConversionException` is thrown when a value cannot be converted to the expected type.

```java
try {
    evaluator.evaluate("sqrt('hello')", context); // Cannot convert string to number
} catch (TypeConversionException e) {
    System.err.println("Type conversion error: " + e.getMessage());
    System.err.println("Source value: " + e.getSourceValue());
    System.err.println("Target type: " + e.getTargetType());
}
```

### Operation Errors

- **InvalidOperationException**: Thrown when an operation is invalid between two operands
- **ArithmeticExpressionException**: Specifically for arithmetic errors like division by zero

```java
try {
    evaluator.evaluate("'hello' * 5", context); // Cannot multiply string by number
} catch (InvalidOperationException e) {
    System.err.println("Invalid operation: " + e.getMessage());
    System.err.println("Operation: " + e.getOperation());
    System.err.println("Left operand: " + e.getLeftOperand());
    System.err.println("Right operand: " + e.getRightOperand());
}

try {
    evaluator.evaluate("5 / 0", context); // Division by zero
} catch (ArithmeticExpressionException e) {
    System.err.println("Arithmetic error: " + e.getMessage());
    // e.g. "Invalid operation '/' between 5 and 0: Division by zero"
}
```

## Property Access Errors

The `PropertyNotFoundException` is the base class for errors related to accessing properties that don't exist. It has several specialized subclasses:

- **VariableNotFoundException**: Thrown when referencing a variable that doesn't exist in the context
- **PropertyAccessException**: Thrown when a property exists but cannot be accessed
- **ArrayIndexOutOfBoundsException**: Thrown when an array or list index is out of bounds

```java
try {
    evaluator.evaluate("$nonExistentVar", context);
} catch (VariableNotFoundException e) {
    System.err.println("Variable not found: " + e.getVariableName());
}

try {
    evaluator.evaluate("$person.address.city", context); // When address is null
} catch (PropertyAccessException e) {
    System.err.println("Property access error: " + e.getMessage());
    System.err.println("Target object: " + e.getTarget());
    System.err.println("Property name: " + e.getProperty());
}

try {
    evaluator.evaluate("$scores[5]", context); // When scores has fewer than 6 elements
} catch (ArrayIndexOutOfBoundsException e) {
    System.err.println("Array index error: " + e.getMessage());
    System.err.println("Array: " + e.getArray());
    System.err.println("Index: " + e.getIndex());
    System.err.println("Size: " + e.getSize());
}
```

## Avoiding Errors with Null-Safe Operators

Expresso provides null-safe operators to help you avoid PropertyNotFoundException exceptions when dealing with potentially null values:

```java
// This will throw PropertyNotFoundException if person.address is null
try {
    evaluator.evaluate("$person.address.city", context);
} catch (PropertyNotFoundException e) {
    System.err.println("Property error: " + e.getMessage());
    // e.g. "Property 'address' is null for object of type Person"
}

// Using null-safe property access - returns null instead of throwing exception
Object result = evaluator.evaluate("$person?.address?.city", context);
// result will be null if person or address is null

// Using null coalescing for default values
String city = (String) evaluator.evaluate("$person?.address?.city ?? 'Unknown'", context);
// city will be "Unknown" if person or address is null

// Safer array access
try {
    evaluator.evaluate("$person.hobbies[0]", context); // Throws if hobbies is null
} catch (PropertyNotFoundException e) {
    System.err.println("Array error: " + e.getMessage());
}

// Using null-safe array access
Object hobby = evaluator.evaluate("$person?.hobbies?[0]", context);
// hobby will be null if hobbies is null or empty

// Combining null-safe array access with null coalescing
String hobby = (String) evaluator.evaluate("$person?.hobbies?[0] ?? 'No hobby'", context);
// hobby will be "No hobby" if hobbies is null or the index is out of bounds
```

## Defensive Functions

Expresso provides several built-in functions that can help with error prevention:

- `isNull(value)`: Checks if a value is null
- `isEmpty(value)`: Checks if a string, array, or collection is empty
- `coalesce(value1, value2, ...)`: Returns the first non-null value in the list of arguments

```java
// Using coalesce to handle potentially null values
Object result = evaluator.evaluate("coalesce($possiblyNullVar, $backupVar, 'default')", context);

// Checking if a value is null before using it
Object result = evaluator.evaluate("ifThen(isNull($var), 'Value is null', 'Value is: ' + $var)", context);

// Checking if a list is empty before accessing elements
Object result = evaluator.evaluate("ifThen(isEmpty($list), 'List is empty', 'First item: ' + $list[0])", context);
```

## Error Handling Best Practices

1. **Catch Specific Exceptions First**: Always catch the most specific exceptions first, followed by more general ones.

2. **Use Exception Properties**: Many exceptions provide additional information through methods like `getFunctionName()`, `getVariableName()`, etc.

3. **User-Friendly Error Messages**: Translate exceptions into user-friendly error messages that explain how to fix the issue.

4. **Default Values**: Consider using null-safe operators (`?.`, `?[]`) and null coalescing (`??`) to handle potential errors gracefully.

5. **Validate Expressions**: For critical code paths, validate expressions before evaluating them to prevent runtime errors.

6. **Properly Type-Check**: Ensure proper type checking in custom functions to avoid type conversion errors.

7. **Use Defensive Functions**: Leverage built-in functions like `isNull()`, `isEmpty()`, and `coalesce()` to handle edge cases.

Using these techniques, you can create expressions that gracefully handle errors and provide a better user experience. 