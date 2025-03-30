---
id: getting-started
title: Getting Started
sidebar_position: 2
---

# Getting Started with Expresso

## Key Features

- **Simple Syntax**: Easy to learn and use
- **Type-Safe**: Evaluate expressions with proper type checking
- **Variable Support**: Inject variables into expressions
- **Property Access**: Access object properties and array elements
- **Null Safety**: Null-safe property access and array access with the ?. and ?[] operators
- **Default Values**: Provide default values for null results with the ?? operator
- **Built-in Functions**: String, math, and logic functions included
- **Custom Functions**: Define your own functions easily
- **Error Handling**: Comprehensive error reporting
- **Lightweight**: Zero external dependencies

## Installation

Add Expresso to your project using Maven:

```xml
<dependency>
    <groupId>work.ghassen</groupId>
    <artifactId>expresso</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Basic Usage

Here's how to use Expresso in your Java code:

```java
// Import the necessary classes
import com.expresso.ExpressionEvaluator;
import com.expresso.context.Context;

// Create an evaluator
ExpressionEvaluator evaluator = new ExpressionEvaluator();

// Create a context and add variables
Context context = new Context();
context.setVariable("name", "Alice");
context.setVariable("age", 25);

// Evaluate expressions
String result1 = (String) evaluator.evaluate("$name", context);
// result1 = "Alice"

int result2 = (Integer) evaluator.evaluate("$age", context);
// result2 = 25

// String functions
String result3 = (String) evaluator.evaluate("upperCase($name)", context);
// result3 = "ALICE"

// Math operations
int result4 = (Integer) evaluator.evaluate("$age + 5", context);
// result4 = 30

// Complex objects
context.setVariable("person", Map.of(
    "name", "Bob",
    "address", Map.of(
        "city", "New York",
        "zip", 10001
    )
));

// Access object properties
String result5 = (String) evaluator.evaluate("$person.name", context);
// result5 = "Bob"

String result6 = (String) evaluator.evaluate("$person.address.city", context);
// result6 = "New York"

// Null-safe property access
Object result7 = evaluator.evaluate("$person?.nonExistent?.property", context);
// result7 = null (instead of throwing an exception)

// Null coalescing operator for default values
String result8 = (String) evaluator.evaluate("$person?.nonExistent?.property ?? 'Default'", context);
// result8 = "Default"
```

## Custom Functions

You can extend Expresso with your own custom functions:

```java
// Register a custom function
evaluator.registerFunction("add", (args) -> {
    double x = ((Number) args[0]).doubleValue();
    double y = ((Number) args[1]).doubleValue();
    return x + y;
});

// Use the custom function
Double result = (Double) evaluator.evaluate("add(5, 3)", context);
// result = 8.0

// Custom string function
evaluator.registerFunction("greet", (args) -> {
    String name = (String) args[0];
    return "Hello, " + name + "!";
});

String greeting = (String) evaluator.evaluate("greet($name)", context);
// greeting = "Hello, Alice!"

// Function with variable number of arguments
evaluator.registerFunction("sum", (args) -> {
    double sum = 0;
    for (Object arg : args) {
        sum += ((Number) arg).doubleValue();
    }
    return sum;
});

Double total = (Double) evaluator.evaluate("sum(1, 2, 3, 4, 5)", context);
// total = 15.0
```

## Error Handling

Expresso provides comprehensive error handling:

```java
try {
    evaluator.evaluate("$nonExistentVariable", context);
} catch (PropertyNotFoundException e) {
    // Handle property not found
}

try {
    evaluator.evaluate("$person.address.city", context); // If address is null
} catch (PropertyNotFoundException e) {
    // Handle property not found
}

// Alternative: Use null-safe access to avoid exceptions
String city = (String) evaluator.evaluate(
    "$person?.address?.city ?? 'Unknown'", 
    context
);
// city will be "Unknown" if person or address is null
```

## Supported Data Types

Expresso supports various data types:

- Primitive types (int, long, double, boolean)
- String
- Arrays and Collections
- Custom Objects

```java
// Numbers
String numExpr = "$x + $y";
Map<String, Object> numVars = Map.of(
    "x", 10,
    "y", 20
);
Integer numResult = evaluator.evaluate(numExpr, numVars);
// Result: 30

// Booleans
String boolExpr = "$isActive && $hasPermission";
Map<String, Object> boolVars = Map.of(
    "isActive", true,
    "hasPermission", true
);
Boolean boolResult = evaluator.evaluate(boolExpr, boolVars);
// Result: true
``` 