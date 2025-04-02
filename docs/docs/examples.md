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

// Access properties on array elements (limitations apply)
// The following might not work reliably for complex property paths:
// "$company.departments[0].name"

// Instead, break complex property paths with array access into steps:
// Step 1: Get the array element
Object firstDept = evaluator.evaluate("$company.departments[0]", context);
context.setVariable("firstDept", firstDept);

// Step 2: Access properties on the element
String deptName = (String) evaluator.evaluate("$firstDept.name", context);
// Result: "Engineering"

// For nested arrays, continue the pattern:
// Instead of: "$company.departments[0].employees[0].name" 
// Do this:
Object dept = evaluator.evaluate("$company.departments[0]", context);
context.setVariable("dept", dept);
Object employee = evaluator.evaluate("$dept.employees[0]", context);
context.setVariable("employee", employee);
String name = (String) evaluator.evaluate("$employee.name", context);
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

## Advanced Functions Usage

### String Functions

```java
// Splitting and joining text
// First get the tokens:
// context.setVariable("tokens", evaluator.evaluate("split('red,green,blue', ',')", context));
// Then use them in another expression:
join("-", $tokens)                        // Returns: "red-green-blue"

// Working with string positions
charAt("Hello", 0)                        // Returns: "H"
indexOf("Hello World", "World")           // Returns: 6

// String predicates
startsWith("Hello World", "Hello")        // Returns: true
endsWith("Hello World", "World")          // Returns: true
```

### Math Functions

```java
// Trigonometric functions
sin(0)                                    // Returns: 0.0
cos(0)                                    // Returns: 1.0
tan(0)                                    // Returns: 0.0

// Logarithmic and exponential functions
log(10)                                   // Returns: 2.302585092994046
log10(100)                                // Returns: 2.0
exp(1)                                    // Returns: 2.718281828459045
```

### Logic Functions

```java
// Type checking
// isList([1, 2, 3])  - Unsupported syntax
// isMap({"key": "value"})  - Unsupported syntax

// Instead, use variables like this:
// Context setup (not shown in usage)
// context.setVariable("myList", List.of(1, 2, 3));
// context.setVariable("myMap", Map.of("key", "value"));

// Then in expressions:
isList($myList)                          // Returns: true
isMap($myMap)                            // Returns: true

// Equality and conditionals
equals("test", "test")                    // Returns: true
equals(null, null)                        // Returns: true

// Conditional expression (like ternary operator)
// Note: Comparison operators like >, <, >= are not directly supported in expressions
// Instead, compute the condition beforehand and store it in a variable:
// context.setVariable("isAdult", age >= 18);
ifThen($isAdult, "adult", "minor")        // Returns: "adult" if $isAdult is true
ifThen(true, "greater", "less")           // Returns: "greater"
ifThen(false, "greater", "less")          // Returns: "less"
```

### Date Functions

```java
// Get components from a date
// First parse the date:
// context.setVariable("date", evaluator.evaluate("parseDate('2023-05-15', 'yyyy-MM-dd')", context));
// Then use it in expressions:
year($date)                               // Returns: 2023
month($date)                              // Returns: 5
dayOfMonth($date)                         // Returns: 15

// Date manipulations
addMonths($date, 3)                       // Returns: 2023-08-15
addYears($date, 1)                        // Returns: 2024-05-15
```

### Collection Functions

```java
// Working with lists - requires setting variables first
// Context setup (not shown in usage):
// context.setVariable("numbers", List.of(10, 20, 30, 40, 50));

// Then in expressions:
size($numbers)                            // Returns: 5
first($numbers)                           // Returns: 10
last($numbers)                            // Returns: 50
subList($numbers, 1, 4)                   // Returns: [20, 30, 40]
contains($numbers, 30)                    // Returns: true

// Getting collection sizes
size("Hello")                             // Returns: 5 (string length)
// For maps:
// context.setVariable("myMap", Map.of("a", 1, "b", 2));
size($myMap)                              // Returns: 2 (map size)
```

### Utility Functions

```java
// Type detection and conversion
typeof(42)                                // Returns: "number"
typeof("hello")                           // Returns: "string"
typeof(null)                              // Returns: "null"

// Type conversions
toString(42)                              // Returns: "42"
toNumber("123")                           // Returns: 123
toBoolean("true")                         // Returns: true
toBoolean(1)                              // Returns: true
```

### Combining Functions

```java
// Working with collections and string functions
// Instead of:
// $items = split("apple,banana,cherry", ",")

// Do this:
// First get the result of split:
// List<?> items = (List<?>) evaluator.evaluate("split('apple,banana,cherry', ',')", context);
// context.setVariable("items", items);

// Then use items in another expression:
join(" + ", subList($items, 0, 2))        // Returns: "apple + banana"
```

## Logical Operators Examples

Expresso now supports logical operators (`&&`, `||`, `!`) that work with boolean values and handle nulls gracefully:

```java
// Basic logical operations
context.setVariable("isActive", true);
context.setVariable("isAdmin", false);

// AND operator
boolean isActiveAdmin = (boolean) evaluator.evaluate("$isActive && $isAdmin", context);
// Result: false

// OR operator
boolean canAccess = (boolean) evaluator.evaluate("$isActive || $isAdmin", context);
// Result: true

// NOT operator
boolean isNotAdmin = (boolean) evaluator.evaluate("!$isAdmin", context);
// Result: true

// Complex conditions
boolean complexCondition = (boolean) evaluator.evaluate(
    "($isActive && !$isAdmin) || ($isActive && $isAdmin)", context);
// Result: true

// Short-circuit evaluation
context.setVariable("user", null);
// This won't throw an error due to short-circuit
boolean result = (boolean) evaluator.evaluate("false && $user.isAdmin", context);
// Result: false

// Null handling in logical operations
context.setVariable("nullVar", null);
boolean nullIsFalsy = (boolean) evaluator.evaluate("!$nullVar", context);
// Result: true (null is treated as false)

boolean orWithNull = (boolean) evaluator.evaluate("true || $nullVar", context);
// Result: true

boolean andWithNull = (boolean) evaluator.evaluate("$nullVar && true", context);
// Result: false
```

## Comparison Functions Examples

Expresso provides comparison functions for comparing values:

```java
// Numeric comparisons
context.setVariable("age", 25);
context.setVariable("minAge", 18);
context.setVariable("maxAge", 65);

boolean isAdult = (boolean) evaluator.evaluate("greaterThanOrEqual($age, $minAge)", context);
// Result: true

boolean isRetired = (boolean) evaluator.evaluate("greaterThan($age, $maxAge)", context);
// Result: false

// String comparisons
context.setVariable("name", "Alice");
context.setVariable("otherName", "Bob");

boolean nameComesFirst = (boolean) evaluator.evaluate("lessThan($name, $otherName)", context);
// Result: true (alphabetically, "Alice" comes before "Bob")

// Date comparisons
context.setVariable("today", LocalDate.now());
context.setVariable("pastDate", LocalDate.now().minusDays(30));

boolean isPast = (boolean) evaluator.evaluate("lessThan($pastDate, $today)", context);
// Result: true

// Equality checks
context.setVariable("x", 5);
context.setVariable("y", "5");

boolean strictlyEqual = (boolean) evaluator.evaluate("strictEquals($x, $y)", context);
// Result: false (different types)

boolean notEqual = (boolean) evaluator.evaluate("notEquals($x, $y)", context);
// Result: true

// Combining comparisons with logical operators
boolean inRange = (boolean) evaluator.evaluate(
    "greaterThanOrEqual($age, $minAge) && lessThanOrEqual($age, $maxAge)", context);
// Result: true
``` 