---
id: functions
title: Built-in Functions
sidebar_position: 4
---

# Built-in Functions

Expresso provides a variety of built-in functions to help you perform common operations in your expressions. Each function is documented with its parameter types and return type.

## String Functions

| Function | Description | Parameters | Return Type |
|----------|-------------|------------|------------|
| `length(str)` | Returns the length of a string | `str: String` | `Integer` |
| `upperCase(str)` | Converts string to uppercase | `str: String` | `String` |
| `lowerCase(str)` | Converts string to lowercase | `str: String` | `String` |
| `substring(str, start, [length])` | Gets a substring | `str: String`, `start: Number`, `length: Number (optional)` | `String` |
| `replace(str, old, new)` | Replaces text in a string | `str: String`, `old: String`, `new: String` | `String` |
| `trim(str)` | Removes whitespace from string | `str: String` | `String` |
| `contains(str, search)` | Checks if string contains text | `str: String`, `search: String` | `Boolean` |
| `startsWith(str, prefix)` | Checks if string starts with text | `str: String`, `prefix: String` | `Boolean` |
| `endsWith(str, suffix)` | Checks if string ends with text | `str: String`, `suffix: String` | `Boolean` |
| `split(str, delimiter)` | Splits string into a list | `str: String`, `delimiter: String` | `List<String>` |
| `join(delimiter, elements)` | Joins list elements into a string | `delimiter: String`, `elements: List` | `String` |
| `charAt(str, index)` | Gets character at index | `str: String`, `index: Number` | `String` |
| `indexOf(str, search)` | Gets position of substring | `str: String`, `search: String` | `Integer` |

## Math Functions

| Function | Description | Parameters | Return Type |
|----------|-------------|------------|------------|
| `abs(num)` | Absolute value | `num: Number` | `Number` |
| `ceil(num)` | Rounds up to nearest integer | `num: Number` | `Number` |
| `floor(num)` | Rounds down to nearest integer | `num: Number` | `Number` |
| `round(num, [decimals])` | Rounds to nearest integer or decimal places | `num: Number`, `decimals: Number (optional)` | `Number` |
| `max(a, b)` | Maximum value | `a: Number`, `b: Number` | `Number` |
| `min(a, b)` | Minimum value | `a: Number`, `b: Number` | `Number` |
| `pow(base, exponent)` | Power function | `base: Number`, `exponent: Number` | `Number` |
| `sqrt(num)` | Square root | `num: Number` | `Number` |
| `random()` | Random number between 0 and 1 | none | `Number` |
| `sin(angle)` | Sine of angle (in radians) | `angle: Number` | `Number` |
| `cos(angle)` | Cosine of angle (in radians) | `angle: Number` | `Number` |
| `tan(angle)` | Tangent of angle (in radians) | `angle: Number` | `Number` |
| `log(value)` | Natural logarithm | `value: Number` | `Number` |
| `log10(value)` | Base-10 logarithm | `value: Number` | `Number` |
| `exp(value)` | e raised to the power | `value: Number` | `Number` |

## Logic Functions

| Function | Description | Parameters | Return Type |
|----------|-------------|------------|------------|
| `isNull(value)` | Checks if value is null | `value: Any` | `Boolean` |
| `coalesce(value1, value2, ...)` | Returns first non-null value | `value1: Any`, `value2: Any`, ... | `Any` |
| `isEmpty(value)` | Checks if value is empty | `value: Any` | `Boolean` |
| `isNumber(value)` | Checks if value is a number | `value: Any` | `Boolean` |
| `isString(value)` | Checks if value is a string | `value: Any` | `Boolean` |
| `isBoolean(value)` | Checks if value is boolean | `value: Any` | `Boolean` |
| `isList(value)` | Checks if value is a list | `value: Any` | `Boolean` |
| `isMap(value)` | Checks if value is a map | `value: Any` | `Boolean` |
| `equals(value1, value2)` | Checks if values are equal | `value1: Any`, `value2: Any` | `Boolean` |
| `ifThen(condition, trueValue, falseValue)` | Conditional operator | `condition: Boolean`, `trueValue: Any`, `falseValue: Any` | `Any` |

## Comparison Functions

| Function | Description | Parameters | Return Type |
|----------|-------------|------------|------------|
| `greaterThan(a, b)` | Checks if a > b | `a: Number/String/Date`, `b: Number/String/Date` | `Boolean` |
| `lessThan(a, b)` | Checks if a < b | `a: Number/String/Date`, `b: Number/String/Date` | `Boolean` |
| `greaterThanOrEqual(a, b)` | Checks if a >= b | `a: Number/String/Date`, `b: Number/String/Date` | `Boolean` |
| `lessThanOrEqual(a, b)` | Checks if a <= b | `a: Number/String/Date`, `b: Number/String/Date` | `Boolean` |
| `strictEquals(a, b)` | Checks if a equals b (strict type check) | `a: Any`, `b: Any` | `Boolean` |
| `notEquals(a, b)` | Checks if a is not equal to b | `a: Any`, `b: Any` | `Boolean` |

> **Note:** In addition to comparison functions, you can now use logical operators (`&&`, `||`, `!`) directly in expressions for boolean operations. These operators treat `null` values as `false`.

## Date Functions

| Function | Description | Parameters | Return Type |
|----------|-------------|------------|------------|
| `format(date, pattern)` | Format date | `date: LocalDate`, `pattern: String` | `String` |
| `parseDate(str, pattern)` | Parse date string | `str: String`, `pattern: String` | `LocalDate` |
| `now()` | Get current date | none | `LocalDate` |
| `addDays(date, days)` | Add days to date | `date: LocalDate`, `days: Number` | `LocalDate` |
| `dateDiff(date1, date2)` | Get days between dates | `date1: LocalDate`, `date2: LocalDate` | `Number` |
| `addMonths(date, months)` | Add months to date | `date: LocalDate`, `months: Number` | `LocalDate` |
| `addYears(date, years)` | Add years to date | `date: LocalDate`, `years: Number` | `LocalDate` |
| `year(date)` | Get year from date | `date: LocalDate` | `Number` |
| `month(date)` | Get month from date | `date: LocalDate` | `Number` |
| `dayOfMonth(date)` | Get day of month from date | `date: LocalDate` | `Number` |

## Collection Functions

| Function | Description | Parameters | Return Type |
|----------|-------------|------------|------------|
| `size(collection)` | Get size of collection, string, or map | `collection: List/Map/String` | `Number` |
| `first(list)` | Get first element of list | `list: List` | `Any` |
| `last(list)` | Get last element of list | `list: List` | `Any` |
| `subList(list, start, [end])` | Get subset of list | `list: List`, `start: Number`, `end: Number (optional)` | `List` |
| `contains(collection, item)` | Check if collection contains item | `collection: List/Array/String`, `item: Any` | `Boolean` |

The `contains` function works with different types of collections:

```java
// With strings
contains("hello world", "world")    // true
contains("hello world", "bye")      // false

// With lists
List<String> fruits = Arrays.asList("apple", "banana", "cherry");
context.setVariable("fruits", fruits);
contains($fruits, "banana")         // true
contains($fruits, "kiwi")           // false

// With arrays
Integer[] numbers = {1, 2, 3, 4, 5};
context.setVariable("numbers", numbers);
contains($numbers, 3)               // true
contains($numbers, 10)              // false

// With null values
contains($fruits, null)             // false if there are no null elements
contains(null, "anything")          // false
```

The `contains` function also handles numeric equality appropriately:

```java
// Numeric comparisons in contains
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
context.setVariable("numbers", numbers);
contains($numbers, 3)               // true for exact match
contains($numbers, 3.0)             // true - numbers are compared by value, not type

// Similarly, an array of integers will find a double value if numerically equal
Integer[] numbersArray = {1, 2, 3, 4, 5};
context.setVariable("numbersArray", numbersArray);
contains($numbersArray, 3.0)         // true
```

## Utility Functions

| Function | Description | Parameters | Return Type |
|----------|-------------|------------|------------|
| `typeof(value)` | Get type of value | `value: Any` | `String` |
| `toString(value)` | Convert value to string | `value: Any` | `String` |
| `toNumber(value)` | Convert value to number | `value: Any` | `Number` |
| `toBoolean(value)` | Convert value to boolean | `value: Any` | `Boolean` |

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