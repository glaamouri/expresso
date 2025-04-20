---
id: core-concepts
title: Core Concepts
sidebar_position: 2
---

# Core Concepts

Understanding these fundamental concepts will help you effectively use Expresso in your applications.

## Expression Evaluation

At its core, Expresso evaluates string expressions against a context to produce results:

```java
ExpressionEvaluator evaluator = new ExpressionEvaluator();
Context context = new Context();
context.setVariable("price", 100);
context.setVariable("taxRate", 0.07);

// Evaluate an expression
Object result = evaluator.evaluate("$price * (1 + $taxRate)", context);
// result = 107.0
```

## The Context Object

The `Context` object is central to Expresso's functionality:

- It stores variables that expressions can reference
- It provides access to objects, collections, and primitive values
- It maintains the state during expression evaluation

```java
// Create a context
Context context = new Context();

// Add primitive variables
context.setVariable("name", "John");
context.setVariable("age", 30);

// Add complex objects
Person person = new Person("Alice", 25);
context.setVariable("person", person);

// Add collections
List<String> fruits = Arrays.asList("apple", "banana", "cherry");
context.setVariable("fruits", fruits);

Map<String, Object> settings = Map.of(
    "theme", "dark",
    "notifications", true
);
context.setVariable("settings", settings);
```

## Expression Syntax

Expresso uses a simple, intuitive syntax:

- **Variables**: Accessed with `$` prefix (`$name`, `$person.age`)
- **Operators**: Arithmetic (`+`, `-`, `*`, `/`, `%`), comparison (`==`, `!=`, `>`, `<`, `>=`, `<=`), logical (`&&`, `||`, `!`)
- **Property Access**: Dot notation (`$person.address.city`)
- **Array/List Access**: Bracket notation (`$fruits[0]`)
- **Function Calls**: Name followed by parentheses (`length($name)`, `max($a, $b)`)
- **Null Safety**: Special operators (`?.`, `?[]`, `??`) for safe handling of null values

## Type System

Expresso handles various types:

- **Primitives**: Strings, numbers, booleans, null
- **Collections**: Lists, arrays, maps
- **Objects**: Java objects with property access
- **Automatic Conversion**: Between compatible types when needed

## Validation & Security

Before evaluating expressions (especially from external sources), validate them:

```java
// Basic validation
boolean isValid = evaluator.validate("$price * (1 + $taxRate)", context);

// Detailed validation with error information
ValidationResult result = evaluator.validateWithContext("$price * (1 + $taxRate)", context);
if (!result.isValid()) {
    // Handle errors
    ExpressionError error = result.getFirstError();
    System.err.println(error.getErrorType() + ": " + error.getMessage());
}
```

## Extensibility

Expresso can be extended with custom functions:

```java
// Register a custom function
evaluator.registerFunction("discount", (args) -> {
    double price = ((Number) args[0]).doubleValue();
    double discountRate = ((Number) args[1]).doubleValue();
    return price * (1 - discountRate);
});

// Use the function in expressions
Double discountedPrice = (Double) evaluator.evaluate("discount($price, 0.1)", context);
``` 