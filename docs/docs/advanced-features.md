---
id: advanced-features
title: Advanced Features
sidebar_position: 6
---

# Advanced Features

This guide covers advanced features in Expresso that enable more complex expression capabilities.

## Comparison and Logical Operators

Expresso supports a full range of comparison and logical operators:

### Comparison Operators

```java
// Equality operators
$x == $y    // Equal to
$x != $y    // Not equal to

// Relational operators
$x > $y     // Greater than
$x >= $y    // Greater than or equal to
$x < $y     // Less than
$x <= $y    // Less than or equal to
```

These operators work for various types:
- Numbers: Standard numeric comparison
- Strings: Lexicographic (dictionary) order
- Dates: Chronological order (when using date objects)

Expresso handles mixed numeric types intelligently:

```java
// Integer and Double comparisons work as expected by value
5 == 5.0     // true
5 != 5.0     // false
5 > 4.5      // true
5.5 > 5      // true

// The type is ignored for numeric comparisons
$intValue = 5
$doubleValue = 5.0
$intValue == $doubleValue    // true

// This applies to all numeric comparisons
$intValue = 10
$doubleValue = 10.0
$intValue * $doubleValue == 100    // true
```

### Logical Operators

```java
// Logical AND
$condition1 && $condition2   // true if both conditions are true

// Logical OR
$condition1 || $condition2   // true if either condition is true

// Logical NOT
!$condition                  // true if condition is false, false if condition is true
```

Logical operators use short-circuit evaluation, meaning they only evaluate the right side when necessary:
- `&&` only evaluates the right expression if the left expression is true
- `||` only evaluates the right expression if the left expression is false

### Example

```java
// Setup context
Context context = new Context();
context.setVariable("age", 25);
context.setVariable("name", "Alice");
context.setVariable("isStudent", true);
context.setVariable("minAge", 18);
context.setVariable("maxAge", 30);

// Comparison operators
boolean isAdult = (Boolean) evaluator.evaluate("$age >= $minAge", context);
// true

boolean nameCheck = (Boolean) evaluator.evaluate("$name == 'Alice'", context);
// true

// Logical operators
boolean complexCheck = (Boolean) evaluator.evaluate(
    "$age >= $minAge && $age <= $maxAge && ($isStudent || $name == 'Alice')",
    context
);
// true

// Short circuit evaluation
Object result = evaluator.evaluate("false && unknownFunction()", context);
// Returns false without evaluating unknownFunction()
```

## Conditional (Ternary) Operator

Expresso supports the conditional (ternary) operator, similar to the `?:` operator in Java:

```java
$condition ? $trueValue : $falseValue
```

This operator evaluates `$condition` and returns `$trueValue` if the condition is true, or `$falseValue` if the condition is false.

### Examples

```java
// Basic conditionals
Object result = evaluator.evaluate("$age >= 18 ? 'Adult' : 'Minor'", context);
// Returns "Adult" if age is 18 or greater, otherwise "Minor"

// Ternary with expressions on both sides
Object discount = evaluator.evaluate(
    "$isStudent ? $price * 0.8 : $price * 0.95", 
    context
);
// Returns price with 20% off if student, otherwise 5% off

// Nested ternary
Object category = evaluator.evaluate(
    "$age < 13 ? 'Child' : ($age < 20 ? 'Teenager' : 'Adult')",
    context
);
// Returns categorization based on age
```

## Date and Time Support

Expresso provides comprehensive date and time functions:

### Creating Dates

```java
// Get the current date
Object today = evaluator.evaluate("currentDate()", context);
// Returns java.time.LocalDate.now()

// Get the current time
Object now = evaluator.evaluate("currentTime()", context);
// Returns java.time.LocalTime.now()

// Get the current date and time
Object dateTime = evaluator.evaluate("currentDateTime()", context);
// Returns java.time.LocalDateTime.now()

// Parse a date from string
Object parsedDate = evaluator.evaluate("parseDate('2023-05-15')", context);
// Returns LocalDate for May 15, 2023

// Parse with custom format
Object customDate = evaluator.evaluate("parseDate('15/05/2023', 'dd/MM/yyyy')", context);
// Returns LocalDate for May 15, 2023

// Parse date and time
Object parsedDateTime = evaluator.evaluate("parseDateTime('2023-05-15T14:30:00')", context);
// Returns LocalDateTime
```

### Date Manipulations

```java
// Add days to a date
Object futureDate = evaluator.evaluate("addDays(currentDate(), 10)", context);
// Returns current date + 10 days

// Add months to a date
Object nextMonth = evaluator.evaluate("addMonths(currentDate(), 1)", context);
// Returns current date + 1 month

// Add years to a date
Object nextYear = evaluator.evaluate("addYears(currentDate(), 1)", context);
// Returns current date + 1 year

// Get days between dates
Object dayDiff = evaluator.evaluate(
    "daysBetween(parseDate('2023-01-01'), parseDate('2023-01-15'))",
    context
);
// Returns 14 (number of days between the dates)
```

### Extracting Date Components

```java
// Get day of month
Object day = evaluator.evaluate("getDayOfMonth(currentDate())", context);
// Returns current day of month (1-31)

// Get month
Object month = evaluator.evaluate("getMonth(currentDate())", context);
// Returns current month (1-12)

// Get year
Object year = evaluator.evaluate("getYear(currentDate())", context);
// Returns current year
```

### Date Comparisons

```java
// Check if a date is before another
Object isBefore = evaluator.evaluate(
    "isDateBefore(parseDate('2023-01-01'), parseDate('2023-02-01'))",
    context
);
// Returns true

// Check if a date is after another
Object isAfter = evaluator.evaluate(
    "isDateAfter(parseDate('2023-02-01'), parseDate('2023-01-01'))",
    context
);
// Returns true
```

### Formatting Dates

```java
// Format a date using a pattern
Object formattedDate = evaluator.evaluate(
    "formatDate(currentDate(), 'MMMM dd, yyyy')",
    context
);
// Returns something like "May 15, 2023"
```

## Utility Classes for Testing

Expresso provides utility classes to help with building test data for expressions.

### ObjectBuilder for Creating Object Graphs

The `ObjectBuilder` class provides a fluent API for creating complex object graphs:

```java
import com.expresso.util.ObjectBuilder;

// Create a simple object
Map<String, Object> simpleObject = ObjectBuilder.create()
    .set("name", "Alice")
    .set("age", 25)
    .set("isStudent", true)
    .build();

// Create a complex nested object
Map<String, Object> complexObject = ObjectBuilder.create()
    .set("id", 1001)
    .set("name", "John Doe")
    .object("address")
        .set("street", "123 Main St")
        .set("city", "New York")
        .set("state", "NY")
        .set("zip", 10001)
        .end()
    .list("hobbies")
        .add("reading")
        .add("gaming")
        .add("hiking")
        .end()
    .object("account")
        .set("id", "ACC-1001")
        .set("active", true)
        .list("transactions")
            .addObject()
                .set("id", "TRX-001")
                .set("amount", 150.75)
                .set("date", java.time.LocalDate.of(2023, 5, 10))
                .end()
            .addObject()
                .set("id", "TRX-002")
                .set("amount", 50.25)
                .set("date", java.time.LocalDate.of(2023, 5, 15))
                .end()
            .end()
        .end()
    .build();

// Use with Expresso
context.setVariable("user", complexObject);
String city = (String) evaluator.evaluate("$user.address.city", context);
// Returns "New York"
```

This utility makes it much easier to build complex object graphs for testing your expressions, without having to create numerous Java bean classes. 