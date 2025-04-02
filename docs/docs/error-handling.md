---
id: error-handling
title: Error Handling
sidebar_position: 5
---

# Error Handling

Expresso provides robust error handling mechanisms to help you manage errors in your expressions.

## Exception Hierarchy

Expresso uses a structured exception hierarchy to provide clear and informative error messages. The library includes these primary exception types:

```
RuntimeException
├── SyntaxException              // For parsing and syntax errors
├── EvaluationException          // For errors during expression evaluation
└── PropertyNotFoundException     // For property access errors
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

Common syntax errors include:
- Unclosed parentheses or string literals
- Missing operators between values
- Invalid array indices
- Malformed expressions

## Evaluation Errors

The `EvaluationException` is thrown when errors occur during the evaluation of a syntactically valid expression:

```java
try {
    evaluator.evaluate("unknownFunction()", context);
} catch (EvaluationException e) {
    System.err.println("Evaluation error: " + e.getMessage());
    // e.g. "Function not found: unknownFunction"
}
```

Common evaluation errors include:
- Referencing undefined functions
- Passing incorrect number of arguments to functions
- Type mismatches in operations
- Division by zero and other arithmetic errors

### Function Errors

Function-related errors are a common type of evaluation error:

```java
try {
    evaluator.evaluate("upperCase()", context); // Missing argument
} catch (EvaluationException e) {
    System.err.println("Function error: " + e.getMessage());
}

try {
    evaluator.evaluate("upperCase(42)", context); // Wrong type of argument
} catch (EvaluationException e) {
    System.err.println("Type error: " + e.getMessage());
}
```

### Type Errors and Invalid Operations

The library also throws appropriate exceptions for type errors and invalid operations:

```java
try {
    evaluator.evaluate("'hello' * 5", context); // Cannot multiply string by number
} catch (EvaluationException e) {
    System.err.println("Invalid operation: " + e.getMessage());
}

try {
    evaluator.evaluate("5 / 0", context); // Division by zero
} catch (EvaluationException e) {
    System.err.println("Arithmetic error: " + e.getMessage());
}
```

## Property Access Errors

The `PropertyNotFoundException` is thrown when errors occur during property access:

```java
try {
    evaluator.evaluate("$nonExistentVar", context);
} catch (PropertyNotFoundException e) {
    System.err.println("Variable not found: " + e.getMessage());
    // e.g. "Variable not found: nonExistentVar"
}

try {
    evaluator.evaluate("$person.address.city", context); // When address is null
} catch (PropertyNotFoundException e) {
    System.err.println("Property access error: " + e.getMessage());
    // e.g. "Cannot access property on null value"
}

try {
    evaluator.evaluate("$scores[5]", context); // When scores has fewer than 6 elements
} catch (PropertyNotFoundException e) {
    System.err.println("Array index error: " + e.getMessage());
    // e.g. "Array index out of bounds: 5"
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
} catch (PropertyNotFoundException e) {
    // Handle property not found errors
} catch (EvaluationException e) {
    // Handle other evaluation errors
} catch (Exception e) {
    // Handle unexpected errors
}
```

2. **Use Null-Safe Operators**: When accessing properties that might be null, use the null-safe operators (`?.`, `?[]`) to avoid exceptions.

3. **Provide Default Values**: Use the null coalescing operator (`??`) to provide default values for potentially null results.

4. **Validate Expressions**: For critical code paths, validate expressions before evaluating them to prevent runtime errors.

5. **Custom Error Messages**: Provide clear error messages when catching exceptions to help with debugging.

6. **Defensive Programming**: Use defensive functions like `isNull()`, `isEmpty()`, and `coalesce()` to handle edge cases.

7. **Test with Edge Cases**: Test your expressions with edge cases like null values, empty collections, and invalid inputs to ensure they handle errors gracefully.

Using these techniques, you can create expressions that gracefully handle errors and provide a better user experience. 