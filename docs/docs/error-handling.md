---
id: error-handling
title: Error Handling
sidebar_position: 5
---

# Error Handling

Expresso provides robust error handling mechanisms to help you manage errors in your expressions.

## Exception Types

Expresso provides several exception types to help you handle errors effectively:

- **SyntaxException**: Thrown when there's a syntax error in the expression
- **PropertyNotFoundException**: Thrown when a property or variable doesn't exist
- **EvaluationException**: Thrown when there's an error during expression evaluation

## Syntax Errors

```java
try {
    evaluator.evaluate("invalid expression", context);
} catch (SyntaxException e) {
    System.err.println("Syntax error: " + e.getMessage());
    // e.g. "Unexpected token 'expression' at position 8"
}
```

## Property Not Found Errors

```java
try {
    evaluator.evaluate("$nonExistentVariable", context);
} catch (PropertyNotFoundException e) {
    System.err.println("Property error: " + e.getMessage());
    // e.g. "Property 'nonExistentVariable' not found in context"
}

try {
    evaluator.evaluate("$user.nonExistentProperty", context);
} catch (PropertyNotFoundException e) {
    System.err.println("Property error: " + e.getMessage());
    // e.g. "Property 'nonExistentProperty' not found on object of type User"
}

try {
    evaluator.evaluate("$scores[10]", context); // When scores has fewer than 10 elements
} catch (PropertyNotFoundException e) {
    System.err.println("Array error: " + e.getMessage());
    // e.g. "Index out of bounds: 10"
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

Using these null-safe operators can significantly reduce the need for try-catch blocks in your code when dealing with optional properties or nested structures.

## Evaluation Errors

```java
try {
    evaluator.evaluate("upperCase(42)", context); // Passing a number to a string function
} catch (EvaluationException e) {
    System.err.println("Evaluation error: " + e.getMessage());
    // e.g. "Cannot apply function 'upperCase' to argument of type Integer"
}
```

## Error Handling Best Practices

- Always catch specific exceptions rather than generic ones
- Use null-safe operators (`?.`, `?[]`) for properties that might be null
- Use null coalescing operator (`??`) to provide default values
- Validate expressions before evaluating them in critical code paths
- Provide meaningful error messages to users when expression evaluation fails
- Properly type-check arguments in custom functions 