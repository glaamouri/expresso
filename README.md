# Expresso - Java Expression Evaluator Library

A flexible Java library for evaluating expressions with simple syntax, supporting multiple data types, property access, null-safe operations, and custom functions.

## Documentation

For full documentation, visit: [expresso.ghassen.work](https://expresso.ghassen.work)

## Features

- Support for primitive and complex data types
- Variable and parameter injection
- Property access on objects
- Null-safe property access and null coalescing operators
- Array and list access with null-safety
- Custom function registration
- Built-in functions for strings, math, and logic operations
- Type-safe evaluation
- Comprehensive error handling
- Zero external dependencies - pure Java implementation

## Requirements

- Java 17 or higher
- No external dependencies required

## Quick Start

Add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>work.ghassen</groupId>
    <artifactId>expresso</artifactId>
    <version>1.0</version>
</dependency>
```

## Basic Usage

```java
// Create an evaluator
ExpressionEvaluator evaluator = new ExpressionEvaluator();

// Create a context with variables
Context context = new Context();
context.setVariable("user", new User("Alice", 25));

// Evaluate expressions
String name = (String) evaluator.evaluate("$user.name", context);
int age = (Integer) evaluator.evaluate("$user.age", context);
String upperName = (String) evaluator.evaluate("upperCase($user.name)", context);
```

## Expression Syntax

### Literals
- Strings: `"Hello"`, `'World'`
- Numbers: `42`, `3.14`, `-5`
- Booleans: `true`, `false`
- Null: `null`

### Variables
- Simple: `$variableName`
- Object properties: `$user.name`, `$user.address.city`
- Array/List access: `$scores[0]`, `$user.hobbies[1]`
- Null-safe property access: `$user?.address?.city`
- Null-safe array access: `$user?.hobbies?[0]`
- Null coalescing operator: `$user?.name ?? 'Unknown'`

### Functions

#### String Functions
```java
// Convert case
upperCase("hello")  // Returns "HELLO"
lowerCase("HELLO")  // Returns "hello"

// String manipulation
length("hello")     // Returns 5
trim("  hello  ")   // Returns "hello"
```

#### Math Functions
```java
// Basic math
abs(-42)            // Returns 42.0
ceil(42.1)          // Returns 43.0
floor(42.9)         // Returns 42.0
round(42.5)         // Returns 43
```

#### Logic Functions
```java
// Null handling
isNull(null)        // Returns true
isNull("test")      // Returns false

// Null coalescing
coalesce(null, "default")  // Returns "default"
coalesce("first", "second") // Returns "first"
```

### Custom Functions

You can register your own custom functions:

```java
// Register a custom function
evaluator.registerFunction("add", (args) -> {
    double x = ((Number) args[0]).doubleValue();
    double y = ((Number) args[1]).doubleValue();
    return x + y;
});

// Use the custom function
double result = (Double) evaluator.evaluate("add(5, 10)", context);  // Returns 15.0
```

## Complex Examples

### Working with Nested Objects
```java
class Person {
    private final String name;
    private final Address address;
    private final List<String> hobbies;
    
    public Person(String name, Address address, List<String> hobbies) {
        this.name = name;
        this.address = address;
        this.hobbies = hobbies;
    }
    
    public String getName() { return name; }
    public Address getAddress() { return address; }
    public List<String> getHobbies() { return hobbies; }
}

class Address {
    private final String city;
    private final String country;
    
    public Address(String city, String country) {
        this.city = city;
        this.country = country;
    }
    
    public String getCity() { return city; }
    public String getCountry() { return country; }
}

// Usage
Address address = new Address("Paris", "France");
Person person = new Person("Alice", address, Arrays.asList("reading", "gaming"));
context.setVariable("person", person);

// Evaluate nested properties
String city = (String) evaluator.evaluate("$person.address.city", context);     // Returns "Paris"
String country = (String) evaluator.evaluate("$person.address.country", context); // Returns "France"
String hobby = (String) evaluator.evaluate("$person.hobbies[0]", context);      // Returns "reading"
```

### Null-Safe Property Access

```java
// Person with null address
Person personWithNullAddress = new Person("Alice", null, Arrays.asList("reading"));
context.setVariable("person", personWithNullAddress);

// Unsafe property access (throws PropertyNotFoundException)
try {
    evaluator.evaluate("$person.address.city", context);
} catch (PropertyNotFoundException e) {
    // Will throw exception
}

// Null-safe property access with null coalescing
String city = (String) evaluator.evaluate("$person?.address?.city ?? 'Unknown'", context);  // Returns "Unknown"

// Person with valid address
Person personWithAddress = new Person("Bob", new Address("Paris", "France"), Arrays.asList("gaming"));
context.setVariable("person", personWithAddress);

// Null-safe property access returns actual value when not null
String city = (String) evaluator.evaluate("$person?.address?.city ?? 'Unknown'", context);  // Returns "Paris"

// Null-safe array access
Person personWithNullHobbies = new Person("Charlie", new Address("London", "UK"), null);
context.setVariable("person", personWithNullHobbies);

// Unsafe array access (throws PropertyNotFoundException)
try {
    evaluator.evaluate("$person.hobbies[0]", context);
} catch (PropertyNotFoundException e) {
    // Will throw exception
}

// Null-safe array access with null coalescing
String hobby = (String) evaluator.evaluate("$person?.hobbies?[0] ?? 'Unknown'", context);  // Returns "Unknown"
```

### Working with Collections
```java
// Lists
List<Integer> scores = Arrays.asList(85, 92, 78);
context.setVariable("scores", scores);

// Access list elements
int firstScore = (Integer) evaluator.evaluate("$scores[0]", context);  // Returns 85
int secondScore = (Integer) evaluator.evaluate("$scores[1]", context); // Returns 92

// Maps
Map<String, Object> user = Map.of(
    "name", "Alice",
    "age", 25,
    "scores", List.of(85, 92, 78),
    "address", Map.of(
        "city", "Paris",
        "country", "France"
    )
);
context.setVariable("user", user);

// Access map values
String name = (String) evaluator.evaluate("$user.name", context);           // Returns "Alice"
String city = (String) evaluator.evaluate("$user.address.city", context);   // Returns "Paris"
int score = (Integer) evaluator.evaluate("$user.scores[1]", context);       // Returns 92
```

### Custom Functions

```java
// Basic custom function
evaluator.registerFunction("add", (args) -> {
    double x = ((Number) args[0]).doubleValue();
    double y = ((Number) args[1]).doubleValue();
    return x + y;
});
double sum = (Double) evaluator.evaluate("add(5, 10)", context);  // Returns 15.0

// String manipulation function
evaluator.registerFunction("greet", (args) -> {
    String name = (String) args[0];
    String time = (String) args[1];
    return "Good " + time + ", " + name + "!";
});
String greeting = (String) evaluator.evaluate("greet('Alice', 'morning')", context);  // Returns "Good morning, Alice!"

// Multiple arguments function
evaluator.registerFunction("calculate", (args) -> {
    double x = ((Number) args[0]).doubleValue();
    double y = ((Number) args[1]).doubleValue();
    double z = ((Number) args[2]).doubleValue();
    return (x + y) * z;
});
double result = (Double) evaluator.evaluate("calculate(2, 3, 6)", context);  // Returns 30.0

// Variable arguments function
evaluator.registerFunction("sum", (args) -> {
    double sum = 0;
    for (Object arg : args) {
        sum += ((Number) arg).doubleValue();
    }
    return sum;
});
double total = (Double) evaluator.evaluate("sum(1, 2, 3, 4, 5)", context);  // Returns 15.0

// Type conversion function
evaluator.registerFunction("concat", (args) -> {
    StringBuilder result = new StringBuilder();
    for (Object arg : args) {
        result.append(String.valueOf(arg));
    }
    return result.toString();
});
String concatenated = (String) evaluator.evaluate("concat(1, 2, 3, true)", context);  // Returns "123true"

// Null handling function
evaluator.registerFunction("safeLength", (args) -> {
    String str = (String) args[0];
    return str != null ? str.length() : 0;
});
int length = (Integer) evaluator.evaluate("safeLength(null)", context);  // Returns 0
int length = (Integer) evaluator.evaluate("safeLength('hello')", context);  // Returns 5
```

## Error Handling

The library provides several exception types:

### SyntaxException
Thrown when there's a syntax error in the expression:
```java
try {
    evaluator.evaluate("invalid syntax", context);
} catch (SyntaxException e) {
    // Handle syntax error
}
```

### PropertyNotFoundException
Thrown when trying to access a non-existent property or array index:
```java
try {
    evaluator.evaluate("$nonexistent", context);
} catch (PropertyNotFoundException e) {
    // Handle property not found
}

try {
    evaluator.evaluate("$scores[5]", context); // When scores has less than 5 elements
} catch (PropertyNotFoundException e) {
    // Handle array index out of bounds
}
```

### EvaluationException
Thrown when there's an error during expression evaluation:
```java
try {
    evaluator.evaluate("upperCase(42)", context); // Invalid argument type
} catch (EvaluationException e) {
    // Handle evaluation error
}
```

## Best Practices

1. **Type Safety**
   - Always cast the result to the expected type
   - Use appropriate type checking before function calls

2. **Null Handling**
   - Use null-safe property access (`?.`) for properties that might be null
   - Use null-safe array access (`?[`) for arrays/lists that might be null
   - Use the null coalescing operator (`??`) to provide default values

3. **Custom Functions**
   - Ensure proper type checking in custom functions
   - Handle null values gracefully in custom functions
   - Use the `Number` interface with `doubleValue()` for numeric arguments to support all number types

4. **Error Handling**
   - Always wrap evaluation in try-catch blocks
   - Handle specific exceptions appropriately

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the LICENSE file for details. 
