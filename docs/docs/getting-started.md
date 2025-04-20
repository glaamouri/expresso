---
id: getting-started
title: Getting Started
sidebar_position: 3
---

# Getting Started with Expresso

This guide will help you quickly integrate Expresso into your application.

## Installation

Add Expresso to your project using Maven:

```xml
<dependency>
    <groupId>work.ghassen</groupId>
    <artifactId>expresso</artifactId>
    <version>{latestStable.version}</version>
</dependency>
```


## Basic Usage

### 1. Create an Evaluator

```java
import com.expresso.ExpressionEvaluator;

// Create an evaluator instance
ExpressionEvaluator evaluator = new ExpressionEvaluator();
```

### 2. Prepare a Context

```java
import com.expresso.context.Context;

// Create a context and add variables
Context context = new Context();
context.setVariable("price", 100);
context.setVariable("quantity", 5);
context.setVariable("taxRate", 0.07);
```

### 3. Evaluate Expressions

```java
// Simple arithmetic
Double subtotal = (Double) evaluator.evaluate("$price * $quantity", context);
// subtotal = 500.0

// More complex calculations
Double total = (Double) evaluator.evaluate("$price * $quantity * (1 + $taxRate)", context);
// total = 535.0

// Conditional logic
Boolean eligible = (Boolean) evaluator.evaluate("$quantity >= 5", context);
// eligible = true

Double finalPrice = (Double) evaluator.evaluate("$quantity >= 5 ? $price * 0.9 : $price", context);
// finalPrice = 90.0 (10% discount applied)
```

### 4. Handle Different Data Types

```java
// String data
context.setVariable("firstName", "John");
context.setVariable("lastName", "Doe");

String fullName = (String) evaluator.evaluate("$firstName + ' ' + $lastName", context);
// fullName = "John Doe"

// Lists and arrays
List<Integer> scores = Arrays.asList(85, 90, 78, 92, 88);
context.setVariable("scores", scores);

Integer highestScore = (Integer) evaluator.evaluate("max($scores)", context);
// highestScore = 92

Double average = (Double) evaluator.evaluate("avg($scores)", context);
// average = 86.6

// Objects
User user = new User("alice@example.com", "Alice", true);
context.setVariable("user", user);

String email = (String) evaluator.evaluate("$user.email", context);
// email = "alice@example.com"

Boolean active = (Boolean) evaluator.evaluate("$user.active", context);
// active = true
```

### 5. Safe Evaluation

Before evaluating untrusted expressions (such as those input by users):

```java
// Validate the expression
ValidationResult result = evaluator.validateWithContext("$price * $quantity * (1 + $taxRate)", context);

if (result.isValid()) {
    // Safe to evaluate
    Object value = evaluator.evaluate("$price * $quantity * (1 + $taxRate)", context);
    System.out.println("Result: " + value);
} else {
    // Handle validation errors
    System.err.println("Invalid expression: " + result.getFirstError().getMessage());
}
```

## Next Steps

- Learn more about the [Expression Syntax](syntax)
- Explore [Built-in Functions](functions)
- Understand [Null Safety](null-safety)
- See practical [Examples](examples)
- Implement [Error Handling](error-handling) 