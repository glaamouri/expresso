---
id: functions
title: Built-in Functions
sidebar_position: 4
---

# Built-in Functions

Expresso provides a variety of built-in functions to help you perform common operations in your expressions.

## String Functions

```java
// Length of a string
length($string)

// Convert to uppercase
upperCase($string)

// Convert to lowercase
lowerCase($string)

// Get substring
substring($string, start, length)

// Replace text
replace($string, old, new)

// Trim whitespace
trim($string)

// Check if string contains
contains($string, search)
```

## Math Functions

```java
// Absolute value
abs($number)

// Round number
round($number, decimals)

// Maximum value
max($x, $y)

// Minimum value
min($x, $y)

// Power function
pow($base, $exponent)

// Square root
sqrt($number)

// Random number
random()
```

## Logic Functions

```java
// Check if value is null
isNull($value)

// Check if value is empty
isEmpty($value)

// Check if value is number
isNumber($value)

// Check if value is string
isString($value)

// Check if value is boolean
isBoolean($value)
```

## Date Functions

```java
// Format date
format($date, 'yyyy-MM-dd')

// Parse date string
parseDate($string, 'yyyy-MM-dd')

// Get current date
now()

// Add days to date
addDays($date, days)

// Get date difference
dateDiff($date1, $date2)
```

## Custom Functions

You can register your own custom functions with the evaluator:

```java
// Register a simple custom function
evaluator.registerFunction("add", (args) -> {
    double x = ((Number) args[0]).doubleValue();
    double y = ((Number) args[1]).doubleValue();
    return x + y;
});

// Use the custom function in expressions
add(5, 10)  // Returns: 15.0

// Register a function with string manipulation
evaluator.registerFunction("greet", (args) -> {
    String name = (String) args[0];
    String time = (String) args[1];
    return "Good " + time + ", " + name + "!";
});

// Use the custom function
greet('Alice', 'morning')  // Returns: "Good morning, Alice!"

// Register a function with multiple arguments
evaluator.registerFunction("calculate", (args) -> {
    double x = ((Number) args[0]).doubleValue();
    double y = ((Number) args[1]).doubleValue();
    double z = ((Number) args[2]).doubleValue();
    return (x + y) * z;
});

// Use the custom function
calculate(2, 3, 6)  // Returns: 30.0

// Register a function with variable arguments
evaluator.registerFunction("sum", (args) -> {
    double sum = 0;
    for (Object arg : args) {
        sum += ((Number) arg).doubleValue();
    }
    return sum;
});

// Use the custom function
sum(1, 2, 3, 4, 5)  // Returns: 15.0

// Register a function with type conversion
evaluator.registerFunction("concat", (args) -> {
    StringBuilder result = new StringBuilder();
    for (Object arg : args) {
        result.append(String.valueOf(arg));
    }
    return result.toString();
});

// Use the custom function
concat(1, 2, 3, true)  // Returns: "123true"

// Register a function with null handling
evaluator.registerFunction("safeLength", (args) -> {
    String str = (String) args[0];
    return str != null ? str.length() : 0;
});

// Use the custom function
safeLength(null)       // Returns: 0
safeLength("hello")    // Returns: 5

// Register a function with complex logic
evaluator.registerFunction("grade", (args) -> {
    double score = ((Number) args[0]).doubleValue();
    if (score >= 90) return "A";
    if (score >= 80) return "B";
    if (score >= 70) return "C";
    if (score >= 60) return "D";
    return "F";
});

// Use the custom function
grade(95)  // Returns: "A"
grade(85)  // Returns: "B"
grade(75)  // Returns: "C"
grade(65)  // Returns: "D"
grade(55)  // Returns: "F"
```

Custom functions can:

- Take any number of arguments
- Return any type of value
- Access context variables
- Handle various numeric types using Number.doubleValue()
- Process null values safely
- Throw custom exceptions
- Perform complex calculations and type conversions 