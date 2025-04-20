---
id: quick-reference
title: Quick Reference
sidebar_position: 10
---

# Quick Reference

This page provides concise examples of common operations in Expresso.

## Core Operations

```java
// Create evaluator
ExpressionEvaluator evaluator = new ExpressionEvaluator();

// Create context
Context context = new Context();
context.setVariable("name", "Alice");
context.setVariable("price", 100.0);

// Basic evaluation
String result1 = (String) evaluator.evaluate("$name", context); // "Alice"
Double result2 = (Double) evaluator.evaluate("$price * 1.05", context); // 105.0
```

## Operators

### Arithmetic

```java
// Addition
Double sum = (Double) evaluator.evaluate("$a + $b", Map.of("a", 10, "b", 20)); // 30.0

// Subtraction
Double diff = (Double) evaluator.evaluate("$a - $b", Map.of("a", 10, "b", 20)); // -10.0

// Multiplication
Double product = (Double) evaluator.evaluate("$a * $b", Map.of("a", 10, "b", 20)); // 200.0

// Division
Double quotient = (Double) evaluator.evaluate("$a / $b", Map.of("a", 10, "b", 20)); // 0.5

// Modulus
Double remainder = (Double) evaluator.evaluate("$a % $b", Map.of("a", 10, "b", 3)); // 1.0
```

### Comparison

```java
// Equality
Boolean equal = (Boolean) evaluator.evaluate("$a == $b", Map.of("a", 10, "b", 10)); // true

// Inequality
Boolean notEqual = (Boolean) evaluator.evaluate("$a != $b", Map.of("a", 10, "b", 20)); // true

// Greater than
Boolean greaterThan = (Boolean) evaluator.evaluate("$a > $b", Map.of("a", 10, "b", 5)); // true

// Less than
Boolean lessThan = (Boolean) evaluator.evaluate("$a < $b", Map.of("a", 10, "b", 20)); // true

// Greater than or equal
Boolean greaterEqual = (Boolean) evaluator.evaluate("$a >= $b", Map.of("a", 10, "b", 10)); // true

// Less than or equal
Boolean lessEqual = (Boolean) evaluator.evaluate("$a <= $b", Map.of("a", 10, "b", 20)); // true
```

### Logical

```java
// Logical AND
Boolean and = (Boolean) evaluator.evaluate("$a && $b", Map.of("a", true, "b", false)); // false

// Logical OR
Boolean or = (Boolean) evaluator.evaluate("$a || $b", Map.of("a", true, "b", false)); // true

// Logical NOT
Boolean not = (Boolean) evaluator.evaluate("!$a", Map.of("a", true)); // false

// Ternary conditional
String conditional = (String) evaluator.evaluate("$age >= 18 ? 'Adult' : 'Minor'", Map.of("age", 25)); // "Adult"
```

### Null-Safety

```java
// Null-safe property access
Object result1 = evaluator.evaluate("$person?.name", Map.of("person", null)); // null (no exception)

// Null-safe array access
Object result2 = evaluator.evaluate("$list?[0]", Map.of("list", null)); // null (no exception)

// Null coalescing (default values)
String result3 = (String) evaluator.evaluate("$person?.name ?? 'Unknown'", Map.of("person", null)); // "Unknown"
```

## Data Types

### Strings

```java
// String concatenation
String fullName = (String) evaluator.evaluate("$firstName + ' ' + $lastName", 
    Map.of("firstName", "John", "lastName", "Doe")); // "John Doe"

// String functions
String upper = (String) evaluator.evaluate("upperCase($name)", Map.of("name", "John")); // "JOHN"
String lower = (String) evaluator.evaluate("lowerCase($name)", Map.of("name", "John")); // "john"
Integer length = (Integer) evaluator.evaluate("length($name)", Map.of("name", "John")); // 4
```

### Numbers

```java
// Numeric functions
Double rounded = (Double) evaluator.evaluate("round($number, 2)", Map.of("number", 3.14159)); // 3.14
Integer maxValue = (Integer) evaluator.evaluate("max($a, $b)", Map.of("a", 10, "b", 20)); // 20
Double sqrtValue = (Double) evaluator.evaluate("sqrt($number)", Map.of("number", 16)); // 4.0
```

### Collections

```java
// Lists
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
context.setVariable("numbers", numbers);

// Array access
Integer item = (Integer) evaluator.evaluate("$numbers[2]", context); // 3

// Collection functions
Integer count = (Integer) evaluator.evaluate("size($numbers)", context); // 5
Integer sum = (Integer) evaluator.evaluate("sum($numbers)", context); // 15
Double avg = (Double) evaluator.evaluate("avg($numbers)", context); // 3.0
```

### Maps

```java
// Map data
Map<String, Object> user = Map.of(
    "name", "Alice",
    "profile", Map.of(
        "age", 30,
        "email", "alice@example.com"
    )
);
context.setVariable("user", user);

// Property access
String name = (String) evaluator.evaluate("$user.name", context); // "Alice"
String email = (String) evaluator.evaluate("$user.profile.email", context); // "alice@example.com"
```

## Validation

```java
// Basic validation
boolean isValid = evaluator.validate("$price * 1.05", context);

// Detailed validation
ValidationResult result = evaluator.validateWithContext("$price * 1.05", context);
if (!result.isValid()) {
    ExpressionError error = result.getFirstError();
    System.err.println(error.getMessage());
}
```

## Custom Functions

```java
// Register custom function
evaluator.registerFunction("discount", (args) -> {
    double price = ((Number) args[0]).doubleValue();
    double rate = ((Number) args[1]).doubleValue();
    return price * (1 - rate);
});

// Use custom function
Double discounted = (Double) evaluator.evaluate("discount($price, 0.1)", Map.of("price", 100.0)); // 90.0
``` 