---
id: error-handling
title: Error Handling
sidebar_position: 5
---

# Error Handling

Expresso provides robust error handling mechanisms to help you manage errors in your expressions.

## Exception Hierarchy

Expresso uses a comprehensive exception hierarchy to provide clear and informative error messages. All exceptions extend from the base `ExpressionException` class.

```
ExpressionException
├── SyntaxException                // For parsing and syntax errors
├── EvaluationException            // For errors during expression evaluation
│   ├── FunctionExecutionException // For errors in function execution
│   │   └── MissingArgumentException // When function arguments are missing
│   ├── TypeConversionException    // For type conversion errors
│   ├── InvalidOperationException  // For invalid operations between operands
│   │   └── ArithmeticExpressionException // For arithmetic errors like division by zero
│   └── UnknownFunctionException   // When a referenced function doesn't exist
└── PropertyNotFoundException      // For property access errors
    ├── VariableNotFoundException  // When a variable doesn't exist
    ├── PropertyAccessException    // When a property exists but can't be accessed
    └── ArrayIndexOutOfBoundsException // When an array index is out of bounds
```

Understanding this hierarchy helps you catch specific types of errors and handle them appropriately.

## Syntax Errors

The `SyntaxException` is thrown when there's a syntax error in the expression, such as missing parentheses or invalid operators.

```java
try {
    evaluator.evaluate("5 + ", context);
} catch (SyntaxException e) {
    System.err.println("Syntax error: " + e.getMessage());
    // e.g. "Unexpected end of expression"
}
```

Common syntax errors include:
- Unclosed parentheses or string literals
- Missing operators between values
- Invalid array indices
- Malformed expressions

## Evaluation Errors

The `EvaluationException` is the base class for all errors that occur during expression evaluation.

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

The `PropertyNotFoundException` is the base class for errors related to accessing properties that don't exist.

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

## Known Limitations with Complex Property Paths

When working with complex property paths, especially those involving arrays and nested objects, there are some limitations to be aware of:

### Complex Path Limitations

The evaluator may encounter difficulties when evaluating complex property paths with multiple array accesses or deeply nested structures. For example:

```java
// This may throw PropertyNotFoundException even when the objects exist
try {
    evaluator.evaluate("$company.departments[0].employees[0].name", context);
} catch (PropertyNotFoundException e) {
    System.err.println("Complex path error: " + e.getMessage());
}
```

### Best Practices for Handling Complex Paths

To work around limitations with complex property paths, consider breaking down complex expressions into simpler steps:

```java
// Instead of accessing a deep path in one go:
// $company.departments[0].employees[0].name

// Break it down into steps:
Object department = evaluator.evaluate("$company.departments[0]", context);
context.setVariable("department", department);

Object employee = evaluator.evaluate("$department.employees[0]", context);
context.setVariable("employee", employee);

Object name = evaluator.evaluate("$employee.name", context);
```

Or use temporary variables in your expressions:

```java
// Using temporary variables in expressions
Object result = evaluator.evaluate("{ " +
    "let dept = $company.departments[0]; " +
    "let emp = dept.employees[0]; " +
    "return emp.name; " +
    "}", context);
```

### Using Null-Safe Access with Complex Paths

When working with complex paths, always use null-safe operators to prevent exceptions:

```java
// Using null-safe access for complex paths
Object result = evaluator.evaluate("$company?.departments?[0]?.employees?[0]?.name", context);

// With a default value
Object result = evaluator.evaluate("$company?.departments?[0]?.employees?[0]?.name ?? 'Unknown'", context);
```

### Testing Complex Paths

When testing expressions that use complex property paths:

1. First verify that individual parts of the path exist and are not null
2. Check the types of intermediate objects to ensure they match your expectations
3. Use defensive programming with null checks and type validations

```java
// Verify each part of a complex path
boolean hasDepartments = (boolean) evaluator.evaluate("$company != null && $company.departments != null", context);
boolean hasFirstDept = hasDepartments && (boolean) evaluator.evaluate("$company.departments.size() > 0", context);
boolean hasEmployees = hasFirstDept && (boolean) evaluator.evaluate("$company.departments[0].employees != null", context);

// Only attempt to access the full path if all parts exist
if (hasEmployees) {
    Object name = evaluator.evaluate("$company.departments[0].employees[0].name", context);
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
    // e.g. "Cannot access property on null value"
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
Object result = evaluator.evaluate("$var == null ? 'Value is null' : 'Value is: ' + $var", context);

// Checking if a list is empty before accessing elements
Object result = evaluator.evaluate("isEmpty($list) ? 'List is empty' : 'First item: ' + $list[0]", context);
```

## Error Handling Best Practices

1. **Catch Specific Exceptions First**: Always catch the most specific exceptions first, followed by more general ones.

```java
try {
    evaluator.evaluate(expression, context);
} catch (SyntaxException e) {
    // Handle syntax errors
} catch (MissingArgumentException e) {
    // Handle missing arguments in functions
} catch (UnknownFunctionException e) {
    // Handle unknown functions
} catch (FunctionExecutionException e) {
    // Handle other function execution errors
} catch (TypeConversionException e) {
    // Handle type conversion errors
} catch (ArithmeticExpressionException e) {
    // Handle arithmetic errors
} catch (InvalidOperationException e) {
    // Handle other operation errors
} catch (ArrayIndexOutOfBoundsException e) {
    // Handle array index errors
} catch (VariableNotFoundException e) {
    // Handle variable not found errors
} catch (PropertyAccessException e) {
    // Handle property access errors
} catch (PropertyNotFoundException e) {
    // Handle other property errors
} catch (EvaluationException e) {
    // Handle other evaluation errors
} catch (ExpressionException e) {
    // Handle any other expression errors
} catch (Exception e) {
    // Handle unexpected errors
}
```

2. **Use Exception Properties**: Many exceptions provide additional information through methods like `getFunctionName()`, `getVariableName()`, etc.

3. **User-Friendly Error Messages**: Translate exceptions into user-friendly error messages that explain how to fix the issue.

4. **Default Values**: Consider using null-safe operators (`?.`, `?[]`) and null coalescing (`??`) to handle potential errors gracefully.

5. **Validate Expressions**: For critical code paths, validate expressions before evaluating them to prevent runtime errors.

6. **Properly Type-Check**: Ensure proper type checking in custom functions to avoid type conversion errors.

7. **Use Defensive Functions**: Leverage built-in functions like `isNull()`, `isEmpty()`, and `coalesce()` to handle edge cases.

Using these techniques, you can create expressions that gracefully handle errors and provide a better user experience. 