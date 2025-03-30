---
id: examples
title: Examples
sidebar_position: 6
---

# Examples

This page contains various examples to help you understand how to use Expresso in different scenarios.

## Basic Examples

```java
// Simple string concatenation
String expr = "$name + ' ' + $surname";
Map<String, Object> vars = Map.of(
    "name", "John",
    "surname", "Doe"
);
// Result: "John Doe"

// Mathematical operations
String mathExpr = "$x * ($y + 5)";
Map<String, Object> mathVars = Map.of(
    "x", 10,
    "y", 3
);
// Result: 80

// Boolean expressions
String boolExpr = "$age >= 18 && $score > 80";
Map<String, Object> boolVars = Map.of(
    "age", 20,
    "score", 85
);
// Result: true
```

## Object Examples

```java
// Access object properties
String propExpr = "$person.name + ' ' + $person.address.city";
Map<String, Object> objVars = Map.of(
    "person", Map.of(
        "name", "John",
        "address", Map.of("city", "New York")
    )
);
// Result: "John New York"

// Work with collections
String listExpr = "$scores[0] + $scores[1]";
Map<String, Object> listVars = Map.of(
    "scores", List.of(85, 92, 78)
);
// Result: 177
```

## Null Safety Examples

Expresso provides robust null handling with null-safe operators:

```java
// Person with null address
Person personWithNullAddress = new Person("Alice", 25, null, Arrays.asList("reading"));
context.setVariable("person", personWithNullAddress);

// Unsafe property access (throws PropertyNotFoundException)
try {
    evaluator.evaluate("$person.address.city", context);
} catch (PropertyNotFoundException e) {
    // Will throw exception: "Property 'address' not found on object of type Person"
}

// Safe property access with null-safe operator
Object result = evaluator.evaluate("$person?.address?.city", context);
// Result: null (instead of throwing exception)

// Using null coalescing for default values
String city = (String) evaluator.evaluate("$person?.address?.city ?? 'Unknown'", context);
// Result: "Unknown"

// Person with valid address
Person personWithAddress = new Person("Bob", 30, 
    new Address("Paris", "France", 75000), 
    Arrays.asList("gaming"));
context.setVariable("person", personWithAddress);

// Null-safe property access returns actual value when not null
String city = (String) evaluator.evaluate("$person?.address?.city ?? 'Unknown'", context);
// Result: "Paris"

// Null-safe array access
Person personWithNullHobbies = new Person("Charlie", 35, 
    new Address("London", "UK", 10000), 
    null);
context.setVariable("person", personWithNullHobbies);

// Regular array access would throw an exception
try {
    evaluator.evaluate("$person.hobbies[0]", context);
} catch (PropertyNotFoundException e) {
    // Will throw exception: "Property 'hobbies' is null or not an array"
}

// Null-safe array access with null coalescing
String hobby = (String) evaluator.evaluate("$person?.hobbies?[0] ?? 'Unknown'", context);
// Result: "Unknown"

// Person with valid hobbies
Person personWithHobbies = new Person("Dave", 40, 
    new Address("Berlin", "Germany", 10115), 
    Arrays.asList("cooking", "hiking"));
context.setVariable("person", personWithHobbies);

// Null-safe array access on existing array
String hobby = (String) evaluator.evaluate("$person?.hobbies?[0]", context);
// Result: "cooking"

// Out-of-bounds index with null coalescing
String hobby = (String) evaluator.evaluate("$person?.hobbies?[10] ?? 'No hobby'", context);
// Result: "No hobby" (index 10 doesn't exist)
```

## Function Examples

```java
// String functions
String strExpr = "upperCase(substring($name, 0, 3))";
Map<String, Object> strVars = Map.of(
    "name", "John Doe"
);
// Result: "JOH"

// Math functions
String mathFuncExpr = "round($pi, 2) + abs($negative)";
Map<String, Object> mathFuncVars = Map.of(
    "pi", 3.14159,
    "negative", -42
);
// Result: 45.14

// Date functions
String dateExpr = "format($date, 'yyyy-MM-dd')";
Map<String, Object> dateVars = Map.of(
    "date", LocalDate.now()
);
// Result: "2024-03-29"
```

## Advanced Examples

```java
// Conditional expressions
String condExpr = "$score >= 90 ? 'A' : ($score >= 80 ? 'B' : 'C')";
Map<String, Object> condVars = Map.of(
    "score", 85
);
// Result: "B"

// Complex calculations
String calcExpr = "round(sqrt(pow($x, 2) + pow($y, 2)), 2)";
Map<String, Object> calcVars = Map.of(
    "x", 3,
    "y", 4
);
// Result: 5.0

// Custom functions
evaluator.registerFunction("calculate", (args) -> {
    double x = ((Number) args[0]).doubleValue();
    double y = ((Number) args[1]).doubleValue();
    return x * y + 10;
});

String customExpr = "calculate($x, $y)";
Map<String, Object> customVars = Map.of(
    "x", 5,
    "y", 3
);
// Result: 25
``` 