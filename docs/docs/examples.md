---
id: examples
title: Examples
sidebar_position: 8
---

# Examples

This page provides various examples to help you understand how to use Expresso in different scenarios.

## Basic Examples

### Simple Arithmetic

```java
ExpressionEvaluator evaluator = new ExpressionEvaluator();
Context context = new Context();

context.setVariable("x", 10);
context.setVariable("y", 5);

// Basic arithmetic operations
Double sum = (Double) evaluator.evaluate("$x + $y", context); // 15.0
Double diff = (Double) evaluator.evaluate("$x - $y", context); // 5.0
Double product = (Double) evaluator.evaluate("$x * $y", context); // 50.0
Double quotient = (Double) evaluator.evaluate("$x / $y", context); // 2.0
Double modulus = (Double) evaluator.evaluate("$x % $y", context); // 0.0
```

### String Operations

```java
Context context = new Context();
context.setVariable("firstName", "John");
context.setVariable("lastName", "Doe");

ExpressionEvaluator evaluator = new ExpressionEvaluator();

// String concatenation
String fullName = (String) evaluator.evaluate("$firstName + ' ' + $lastName", context); // "John Doe"

// String functions
String upper = (String) evaluator.evaluate("upperCase($firstName)", context); // "JOHN"
String lower = (String) evaluator.evaluate("lowerCase($lastName)", context); // "doe"
Integer length = (Integer) evaluator.evaluate("length($firstName)", context); // 4
```

### Boolean Logic

```java
Context context = new Context();
context.setVariable("a", true);
context.setVariable("b", false);

ExpressionEvaluator evaluator = new ExpressionEvaluator();

// Logical operations
Boolean and = (Boolean) evaluator.evaluate("$a && $b", context); // false
Boolean or = (Boolean) evaluator.evaluate("$a || $b", context); // true
Boolean not = (Boolean) evaluator.evaluate("!$a", context); // false

// Comparison with conditions
Object result = evaluator.evaluate("$a ? 'Yes' : 'No'", context); // "Yes"
```

## Object Examples

### Accessing Object Properties

```java
// Create a Person object
Person person = new Person("John Doe", 30);
person.setAddress(new Address("123 Main St", "New York", "10001"));

// Add to context
Context context = new Context();
context.setVariable("person", person);

ExpressionEvaluator evaluator = new ExpressionEvaluator();

// Access properties
String name = (String) evaluator.evaluate("$person.name", context); // "John Doe"
Integer age = (Integer) evaluator.evaluate("$person.age", context); // 30
String city = (String) evaluator.evaluate("$person.address.city", context); // "New York"
```

### Working with Maps

```java
// Create a map
Map<String, Object> user = new HashMap<>();
user.put("name", "Alice");
user.put("email", "alice@example.com");
user.put("active", true);

// Create a nested map
Map<String, Object> settings = new HashMap<>();
settings.put("theme", "dark");
settings.put("notifications", true);
user.put("settings", settings);

// Add to context
Context context = new Context();
context.setVariable("user", user);

ExpressionEvaluator evaluator = new ExpressionEvaluator();

// Access map values
String name = (String) evaluator.evaluate("$user.name", context); // "Alice"
Boolean active = (Boolean) evaluator.evaluate("$user.active", context); // true
String theme = (String) evaluator.evaluate("$user.settings.theme", context); // "dark"
```

### Working with Lists and Arrays

```java
// Create a list
List<String> fruits = Arrays.asList("apple", "banana", "cherry", "date");

// Create an array
Integer[] numbers = {1, 2, 3, 4, 5};

// Add to context
Context context = new Context();
context.setVariable("fruits", fruits);
context.setVariable("numbers", numbers);

ExpressionEvaluator evaluator = new ExpressionEvaluator();

// Access list elements
String secondFruit = (String) evaluator.evaluate("$fruits[1]", context); // "banana"
Integer thirdNumber = (Integer) evaluator.evaluate("$numbers[2]", context); // 3

// Use contains function
Boolean hasBanana = (Boolean) evaluator.evaluate("contains($fruits, 'banana')", context); // true
Boolean hasNumber3 = (Boolean) evaluator.evaluate("contains($numbers, 3)", context); // true
```

## Complex Property Path Examples

### Working with Deeply Nested Structures

When working with complex, deeply nested data structures, it's best to break down the path into manageable steps:

```java
// Create a company with departments and employees
Map<String, Object> company = new HashMap<>();
company.put("name", "Acme Corp");

List<Map<String, Object>> departments = new ArrayList<>();

Map<String, Object> department1 = new HashMap<>();
department1.put("name", "Engineering");
department1.put("headcount", 50);

List<Map<String, Object>> employees = new ArrayList<>();
Map<String, Object> employee1 = new HashMap<>();
employee1.put("name", "Alice Smith");
employee1.put("position", "Lead Developer");
employees.add(employee1);

department1.put("employees", employees);
departments.add(department1);
company.put("departments", departments);

// Add to context
Context context = new Context();
context.setVariable("company", company);

ExpressionEvaluator evaluator = new ExpressionEvaluator();

// Break down complex paths into steps
// Step 1: Access the company departments list
Object departmentsList = evaluator.evaluate("$company.departments", context);
assertNotNull(departmentsList);
assertTrue(departmentsList instanceof List);

// Step 2: Access the first department
Object firstDept = evaluator.evaluate("$company.departments[0]", context);
assertNotNull(firstDept);
context.setVariable("dept", firstDept);  // Store in a variable

// Step 3: Access the department properties using the stored variable
assertEquals("Engineering", evaluator.evaluate("$dept.name", context));
assertEquals(50, evaluator.evaluate("$dept.headcount", context));

// Step 4: Access the employees list
Object employeesList = evaluator.evaluate("$dept.employees", context);
assertNotNull(employeesList);
assertTrue(employeesList instanceof List);

// Step 5: Access the first employee
Object firstEmployee = evaluator.evaluate("$dept.employees[0]", context);
assertNotNull(firstEmployee);
context.setVariable("employee", firstEmployee);  // Store in a variable

// Step 6: Access the employee properties
assertEquals("Alice Smith", evaluator.evaluate("$employee.name", context));
assertEquals("Lead Developer", evaluator.evaluate("$employee.position", context));
```

### Using Temporary Variables for Complex Paths

Another approach is to store intermediate results as variables in the context:

```java
// Starting with the same company structure as above
Context context = new Context();
context.setVariable("company", company);

ExpressionEvaluator evaluator = new ExpressionEvaluator();

// Store the first department as a variable
Object dept = evaluator.evaluate("$company.departments[0]", context);
context.setVariable("engineering", dept);

// Now use this variable to access properties more easily
String deptName = (String) evaluator.evaluate("$engineering.name", context); // "Engineering"
Integer headcount = (Integer) evaluator.evaluate("$engineering.headcount", context); // 50

// Access employees through the department variable
Object employee = evaluator.evaluate("$engineering.employees[0]", context);
context.setVariable("leadDeveloper", employee);

// Use the employee variable
String employeeName = (String) evaluator.evaluate("$leadDeveloper.name", context); // "Alice Smith"
```

### Testing Complex Paths

When testing expressions with complex property paths, always verify the structure:

```java
// Verify each step of the path exists
boolean hasCompany = (boolean) evaluator.evaluate("$company != null", context);
boolean hasDepartments = hasCompany && (boolean) evaluator.evaluate("$company.departments != null", context);
boolean hasFirstDept = hasDepartments && (boolean) evaluator.evaluate("$company.departments.size() > 0", context);

// Only proceed if the structure is valid
if (hasFirstDept) {
    // Store intermediate results to simplify access
    Object dept = evaluator.evaluate("$company.departments[0]", context);
    context.setVariable("dept", dept);
    
    // Now safely access department properties
    String deptName = (String) evaluator.evaluate("$dept.name", context);
    System.out.println("Department name: " + deptName);
}
```

## Null Safety Examples

### Handling Null Values

```java
Person person = new Person("John Doe", 30);
// Address is null

Context context = new Context();
context.setVariable("person", person);

ExpressionEvaluator evaluator = new ExpressionEvaluator();

// This would throw PropertyNotFoundException
try {
    evaluator.evaluate("$person.address.city", context);
} catch (PropertyNotFoundException e) {
    System.out.println("Error: " + e.getMessage());
}

// Using null-safe operator
Object city = evaluator.evaluate("$person?.address?.city", context); // Returns null, no exception

// Using null coalescing for default values
String cityWithDefault = (String) evaluator.evaluate("$person?.address?.city ?? 'Unknown'", context); // "Unknown"
```

### Null-Safe Array Access

```java
Person person = new Person("John Doe", 30);
// Hobbies list is null

Context context = new Context();
context.setVariable("person", person);

ExpressionEvaluator evaluator = new ExpressionEvaluator();

// This would throw PropertyNotFoundException
try {
    evaluator.evaluate("$person.hobbies[0]", context);
} catch (PropertyNotFoundException e) {
    System.out.println("Error: " + e.getMessage());
}

// Using null-safe array access
Object hobby = evaluator.evaluate("$person?.hobbies?[0]", context); // Returns null, no exception

// Using null coalescing for default values
String hobbyWithDefault = (String) evaluator.evaluate("$person?.hobbies?[0] ?? 'No hobby'", context); // "No hobby"
```

## Function Examples

### String Functions

```java
Context context = new Context();
context.setVariable("text", "Hello, World!");

ExpressionEvaluator evaluator = new ExpressionEvaluator();

String upper = (String) evaluator.evaluate("upperCase($text)", context); // "HELLO, WORLD!"
String lower = (String) evaluator.evaluate("lowerCase($text)", context); // "hello, world!"
String substring = (String) evaluator.evaluate("substring($text, 0, 5)", context); // "Hello"
Boolean contains = (Boolean) evaluator.evaluate("contains($text, 'World')", context); // true
Integer length = (Integer) evaluator.evaluate("length($text)", context); // 13
```

### Math Functions

```java
Context context = new Context();
context.setVariable("value", 16);
context.setVariable("negValue", -10);

ExpressionEvaluator evaluator = new ExpressionEvaluator();

Double sqrt = (Double) evaluator.evaluate("sqrt($value)", context); // 4.0
Double abs = (Double) evaluator.evaluate("abs($negValue)", context); // 10.0
Double pow = (Double) evaluator.evaluate("pow($value, 0.5)", context); // 4.0
Double max = (Double) evaluator.evaluate("max(5, 10)", context); // 10.0
Double min = (Double) evaluator.evaluate("min(5, 10)", context); // 5.0
```

### Date Functions

```java
Context context = new Context();
context.setVariable("now", new Date());
context.setVariable("date1", new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-01"));
context.setVariable("date2", new SimpleDateFormat("yyyy-MM-dd").parse("2023-12-31"));

ExpressionEvaluator evaluator = new ExpressionEvaluator();

Boolean before = (Boolean) evaluator.evaluate("dateBefore($date1, $date2)", context); // true
Boolean after = (Boolean) evaluator.evaluate("dateAfter($date2, $date1)", context); // true
Object formatted = evaluator.evaluate("formatDate($now, 'yyyy-MM-dd')", context); // "2023-06-15" (current date)
```

### Collection Functions

```java
Context context = new Context();
context.setVariable("numbers", Arrays.asList(1, 2, 3, 4, 5));
context.setVariable("fruits", Arrays.asList("apple", "banana", "cherry"));

ExpressionEvaluator evaluator = new ExpressionEvaluator();

Boolean containsNumber = (Boolean) evaluator.evaluate("contains($numbers, 3)", context); // true
Boolean containsFruit = (Boolean) evaluator.evaluate("contains($fruits, 'banana')", context); // true
Integer size = (Integer) evaluator.evaluate("size($numbers)", context); // 5
Boolean isEmpty = (Boolean) evaluator.evaluate("isEmpty($fruits)", context); // false
```

## Custom Function Examples

### Registering and Using Custom Functions

```java
// Create a custom function for calculating tax
Function<Object[], Object> calculateTax = args -> {
    if (args.length != 2) {
        throw new IllegalArgumentException("calculateTax requires 2 arguments: amount and rate");
    }
    
    Double amount = Double.valueOf(args[0].toString());
    Double rate = Double.valueOf(args[1].toString());
    return amount * (rate / 100);
};

// Register the function
Context context = new Context();
context.registerFunction("calculateTax", calculateTax);

// Use the function
ExpressionEvaluator evaluator = new ExpressionEvaluator();

Double tax = (Double) evaluator.evaluate("calculateTax(1000, 20)", context); // 200.0
Double total = (Double) evaluator.evaluate("1000 + calculateTax(1000, 20)", context); // 1200.0
```

## Advanced Examples

### Chaining Operations

```java
Context context = new Context();
context.setVariable("amount", 1000);
context.setVariable("taxRate", 20);
context.setVariable("discountRate", 10);

ExpressionEvaluator evaluator = new ExpressionEvaluator();

// Calculate final price with tax and discount
Double finalAmount = (Double) evaluator.evaluate(
    "$amount * (1 + $taxRate/100) * (1 - $discountRate/100)", 
    context
); // 1080.0
```

### Conditional Logic

```java
Context context = new Context();
context.setVariable("score", 85);

ExpressionEvaluator evaluator = new ExpressionEvaluator();

// Grade calculation
String grade = (String) evaluator.evaluate(
    "$score >= 90 ? 'A' : $score >= 80 ? 'B' : $score >= 70 ? 'C' : $score >= 60 ? 'D' : 'F'",
    context
); // "B"
```

### Complex Expressions

```java
// Complex example combining multiple features
Context context = new Context();
context.setVariable("person", Map.of(
    "name", "John Smith",
    "age", 35,
    "active", true,
    "scores", Arrays.asList(85, 90, 78, 92, 88)
));

ExpressionEvaluator evaluator = new ExpressionEvaluator();

// Calculate average score and determine if person is eligible
Boolean eligible = (Boolean) evaluator.evaluate(
    "$person.active && $person.age < 40 && (" +
    "($person.scores[0] + $person.scores[1] + $person.scores[2] + $person.scores[3] + $person.scores[4]) / 5 >= 80" +
    ")",
    context
); // true
```

## Testing Utilities

### ObjectBuilder for Creating Test Data

When testing Expresso expressions, you'll often need to create complex object graphs. The `ObjectBuilder` utility makes this easy with a fluent API:

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