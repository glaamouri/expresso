---
id: functions
title: Functions
sidebar_position: 6
---

# Working with Functions

Expresso provides a rich set of built-in functions and allows you to define custom functions to extend its capabilities.

## Built-in Functions

### String Functions

| Function | Description | Example | Result |
|----------|-------------|---------|--------|
| `upperCase(str)` | Converts a string to uppercase | `upperCase("hello")` | `"HELLO"` |
| `lowerCase(str)` | Converts a string to lowercase | `lowerCase("HELLO")` | `"hello"` |
| `trim(str)` | Removes whitespace from both ends | `trim(" hello ")` | `"hello"` |
| `length(str)` | Returns the string length | `length("hello")` | `5` |
| `substring(str, start, end)` | Extracts part of a string | `substring("hello", 1, 3)` | `"el"` |
| `replace(str, old, new)` | Replaces text in a string | `replace("hello", "l", "x")` | `"hexxo"` |
| `contains(str, substr)` | Checks if a string contains a substring | `contains("hello", "el")` | `true` |
| `startsWith(str, prefix)` | Checks if a string starts with a prefix | `startsWith("hello", "he")` | `true` |
| `endsWith(str, suffix)` | Checks if a string ends with a suffix | `endsWith("hello", "lo")` | `true` |
| `concat(str1, str2, ...)` | Concatenates multiple strings | `concat("a", "b", "c")` | `"abc"` |

### Numeric Functions

| Function | Description | Example | Result |
|----------|-------------|---------|--------|
| `min(a, b, ...)` | Returns the smallest value | `min(5, 3, 7)` | `3` |
| `max(a, b, ...)` | Returns the largest value | `max(5, 3, 7)` | `7` |
| `abs(num)` | Returns the absolute value | `abs(-5)` | `5` |
| `round(num, places)` | Rounds to specified decimal places | `round(3.14159, 2)` | `3.14` |
| `floor(num)` | Rounds down to the nearest integer | `floor(3.7)` | `3` |
| `ceil(num)` | Rounds up to the nearest integer | `ceil(3.2)` | `4` |
| `sqrt(num)` | Returns the square root | `sqrt(16)` | `4` |
| `pow(base, exponent)` | Raises a number to a power | `pow(2, 3)` | `8` |
| `random()` | Returns a random number between 0 and 1 | `random()` | `0.7231...` |

### Collection Functions

| Function | Description | Example | Result |
|----------|-------------|---------|--------|
| `size(collection)` | Returns the collection size | `size([1, 2, 3])` | `3` |
| `sum(collection)` | Returns the sum of all elements | `sum([1, 2, 3])` | `6` |
| `avg(collection)` | Returns the average of all elements | `avg([1, 2, 3])` | `2.0` |
| `join(collection, delimiter)` | Joins elements into a string | `join(['a', 'b', 'c'], ",")` | `"a,b,c"` |
| `contains(collection, element)` | Checks if collection contains an element | `contains([1, 2, 3], 2)` | `true` |
| `filter(collection, expression)` | Filters elements based on a predicate | `filter($users, $item.age > 18)` | `[adult users]` |
| `map(collection, expression)` | Transforms each element | `map($users, $item.name)` | `[names]` |
| `sort(collection)` | Sorts elements in ascending order | `sort([3, 1, 2])` | `[1, 2, 3]` |
| `reverse(collection)` | Reverses the order of elements | `reverse([1, 2, 3])` | `[3, 2, 1]` |

### Date Functions

| Function | Description | Example | Result |
|----------|-------------|---------|--------|
| `now()` | Returns the current date and time | `now()` | `2023-05-20T14:30:15` |
| `dateFormat(date, pattern)` | Formats a date | `dateFormat(now(), "yyyy-MM-dd")` | `"2023-05-20"` |
| `dateParse(str, pattern)` | Parses a string to a date | `dateParse("2023-05-20", "yyyy-MM-dd")` | `date object` |
| `dateAdd(date, amount, unit)` | Adds time to a date | `dateAdd(now(), 1, "day")` | `tomorrow` |
| `dateDiff(date1, date2, unit)` | Returns the difference between dates | `dateDiff(now(), $futureDate, "days")` | `days between` |

#### Date and Time Support Examples

Expresso provides comprehensive date and time functionality:

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

// Get day of month
Object day = evaluator.evaluate("getDayOfMonth(currentDate())", context);
// Returns current day of month (1-31)

// Get month
Object month = evaluator.evaluate("getMonth(currentDate())", context);
// Returns current month (1-12)

// Get year
Object year = evaluator.evaluate("getYear(currentDate())", context);
// Returns current year

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

// Format a date using a pattern
Object formattedDate = evaluator.evaluate(
    "formatDate(currentDate(), 'MMMM dd, yyyy')",
    context
);
// Returns something like "May 15, 2023"
```

### Logical Functions

| Function | Description | Example | Result |
|----------|-------------|---------|--------|
| `if(condition, trueVal, falseVal)` | Returns value based on condition | `if($age >= 18, "Adult", "Minor")` | `"Adult"` or `"Minor"` |
| `coalesce(val1, val2, ...)` | Returns first non-null value | `coalesce($name, "Unknown")` | `$name` or `"Unknown"` |
| `isNull(value)` | Checks if a value is null | `isNull($name)` | `true` or `false` |
| `isNumber(value)` | Checks if a value is a number | `isNumber($age)` | `true` or `false` |
| `isString(value)` | Checks if a value is a string | `isString($name)` | `true` or `false` |
| `isBoolean(value)` | Checks if a value is a boolean | `isBoolean($active)` | `true` or `false` |
| `toString(value)` | Converts a value to string | `toString(123)` | `"123"` |
| `toNumber(value)` | Converts a value to number | `toNumber("123")` | `123` |
| `toBoolean(value)` | Converts a value to boolean | `toBoolean("true")` | `true` |

## Custom Functions

You can extend Expresso's capabilities by registering custom functions:

```java
// Create the evaluator
ExpressionEvaluator evaluator = new ExpressionEvaluator();

// Register a simple function
evaluator.registerFunction("double", args -> {
    double value = ((Number) args[0]).doubleValue();
    return value * 2;
});

// Use the custom function
Double result = (Double) evaluator.evaluate("double(5)", context); // 10.0

// Register a function with multiple parameters
evaluator.registerFunction("rectangle_area", args -> {
    double width = ((Number) args[0]).doubleValue();
    double height = ((Number) args[1]).doubleValue();
    return width * height;
});

// Use the multi-parameter function
Double area = (Double) evaluator.evaluate("rectangle_area(5, 3)", context); // 15.0

// Register a function with variable arguments
evaluator.registerFunction("average", args -> {
    if (args.length == 0) return 0.0;
    
    double sum = 0;
    for (Object arg : args) {
        sum += ((Number) arg).doubleValue();
    }
    return sum / args.length;
});

// Use the variable argument function
Double avg = (Double) evaluator.evaluate("average(10, 20, 30, 40)", context); // 25.0
```

### Function Registration Best Practices

1. **Type Safety**: Ensure your functions handle different input types appropriately
2. **Null Handling**: Handle null inputs gracefully
3. **Error Handling**: Provide meaningful error messages for invalid inputs
4. **Documentation**: Document your functions for other developers
5. **Security**: Be careful with functions that could expose sensitive operations

```java
// Example of a well-implemented custom function
evaluator.registerFunction("safeDiv", args -> {
    if (args.length != 2) {
        throw new IllegalArgumentException("safeDiv requires exactly 2 arguments");
    }
    
    // Handle null inputs
    if (args[0] == null || args[1] == null) {
        return null;
    }
    
    try {
        double numerator = ((Number) args[0]).doubleValue();
        double denominator = ((Number) args[1]).doubleValue();
        
        // Handle division by zero
        if (denominator == 0) {
            return null; // or throw an exception, or return a default value
        }
        
        return numerator / denominator;
    } catch (ClassCastException e) {
        throw new IllegalArgumentException("safeDiv requires numeric arguments");
    }
});
```

### Advanced Function Registration

For more complex use cases, you can create a function registry:

```java
public class CustomFunctionRegistry {
    private final ExpressionEvaluator evaluator;
    
    public CustomFunctionRegistry(ExpressionEvaluator evaluator) {
        this.evaluator = evaluator;
        registerAllFunctions();
    }
    
    private void registerAllFunctions() {
        // Math functions
        registerMathFunctions();
        
        // Date functions
        registerDateFunctions();
        
        // String functions
        registerStringFunctions();
        
        // Business functions
        registerBusinessFunctions();
    }
    
    private void registerMathFunctions() {
        evaluator.registerFunction("percentage", args -> {
            double value = ((Number) args[0]).doubleValue();
            double percent = ((Number) args[1]).doubleValue();
            return value * (percent / 100);
        });
        
        // More math functions...
    }
    
    private void registerDateFunctions() {
        // Date-related functions...
    }
    
    private void registerStringFunctions() {
        // String-related functions...
    }
    
    private void registerBusinessFunctions() {
        // Business-specific functions...
    }
}

// Usage
ExpressionEvaluator evaluator = new ExpressionEvaluator();
CustomFunctionRegistry registry = new CustomFunctionRegistry(evaluator);

// Now all registered functions are available
Double percent = (Double) evaluator.evaluate("percentage(200, 15)", context); // 30.0
```

This organized approach to function registration makes your code more maintainable as you add more custom functions. 