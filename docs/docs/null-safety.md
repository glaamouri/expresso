---
id: null-safety
title: Null Safety
sidebar_position: 5
---

# Null-Safety Operators

Expresso provides powerful null-safety operators that help you write more robust code with elegant handling of null values.

## The Problem with Null Values

In traditional Java code, handling null values often requires verbose null checks:

```java
// Traditional null handling in Java
String cityName = null;
if (person != null) {
    Address address = person.getAddress();
    if (address != null) {
        City city = address.getCity();
        if (city != null) {
            cityName = city.getName();
        }
    }
}
// Use a default value if null
String displayCity = cityName != null ? cityName : "Unknown";
```

This leads to deeply nested if-statements and repetitive code.

## Null-Safe Property Access Operator (`?.`)

Expresso's null-safe property access operator makes null handling simple and concise:

```java
// Null-safe property access
$person?.address?.city?.name

// If any part of the chain is null, the result is null
// instead of throwing a PropertyNotFoundException
```

The null-safe property access operator:
- Returns null if the target object is null rather than throwing an exception
- Can be chained for multiple property accesses
- Works with any object property

## Null-Safe Array Access Operator (`?[]`)

For arrays and collections, Expresso provides a null-safe array access operator:

```java
// Null-safe array access
$person?.hobbies?[0]

// Returns null if:
// - The person object is null
// - The hobbies collection/array is null
// - The index is out of bounds
```

The null-safe array access operator:
- Returns null if the array/list is null rather than throwing an exception
- Returns null if the index is out of bounds
- Can be combined with property access (`$person?.hobbies?[0]?.name`)

## Working with Complex Property Paths

Expresso supports complex property paths including array access with subsequent property access:

```java
// Complex property paths with array access work seamlessly
$company.departments[0].name  // Access the name of the first department
$company.departments[1].employees[0].title  // Access the title of the first employee in the second department

// You can also use null-safe operators for complex paths
$company?.departments?[0]?.employees?[0]?.skills?[0] ?? 'No skill'
```

When working with complex property paths, particularly those that involve arrays and deeply nested structures:

1. Use null-safe operators (`?.` and `?[]`) when there's any chance that a value in the path might be null
2. Provide default values using the null coalescing operator (`??`) for important values
3. Consider breaking very complex paths into multiple expressions for better readability and error handling

## Null Coalescing Operator (`??`)

The null coalescing operator allows you to provide a default value when an expression evaluates to null:

```java
// Null coalescing operator
$person?.name ?? 'Unnamed'

// If $person?.name is null, 'Unnamed' is returned
```

The null coalescing operator:
- Takes a left expression and a right expression
- Returns the left expression's value if it's not null
- Returns the right expression's value if the left expression is null

## Null Handling in Logical Operations

Expresso treats null values as falsy in logical operations, making it easy to write clean conditional expressions:

```java
// Null values are treated as false in logical operations
$nullVariable && $trueValue       // => false
$nullVariable || $trueValue       // => true
$nullVariable || $falseValue      // => false
!$nullVariable                    // => true

// This works with properties too
$person?.address?.isValid && $otherCondition
$person?.manager?.isActive || $fallbackCondition

// And with null-safe property access
$data?.user?.profile?.isActive ?? false
```

Expresso follows these "truthiness" rules:
- `null` is falsy (equivalent to `false` in logical operations)
- `Boolean` values behave as expected (`true` is truthy, `false` is falsy)
- All other non-null values are truthy

This allows for concise expressions that gracefully handle null values without throwing exceptions.

## Combined Usage

These operators are particularly powerful when combined:

```java
// Combining null-safe property access, array access, and null coalescing
$person?.address?.city ?? 'Unknown City'
$person?.hobbies?[0] ?? 'No Hobby'

// With complex paths including array access
$orders?[0]?.items?[0]?.productName ?? 'No Product'

// Combining with logical operations
$user?.profile?.isVerified && $user?.subscription?.isActive
$product?.inStock || $product?.backorderAvailable
```

## Benefits

Using these null-safety operators offers several advantages:
- **Concise code**: Less boilerplate and fewer lines of code
- **More readable**: Clear intention to handle null values safely
- **Less error-prone**: Eliminates common errors like NullPointerException
- **Expressive**: Makes the code's intent clear and direct

## Examples

### Basic Property Access

```java
// Person with null address
Person personWithNullAddress = new Person("Alice", 25, null, Arrays.asList("reading"));
context.setVariable("person", personWithNullAddress);

// This returns null instead of throwing an exception
Object result = evaluator.evaluate("$person?.address?.city", context);
// result == null

// With a default value
String city = (String) evaluator.evaluate("$person?.address?.city ?? 'Unknown'", context);
// city == "Unknown"
```

### Array Access

```java
// Person with null hobbies
Person personWithNullHobbies = new Person("Charlie", 35, 
    new Address("London", "UK", 10000), null);
context.setVariable("person", personWithNullHobbies);

// This returns null instead of throwing an exception
Object hobby = evaluator.evaluate("$person?.hobbies?[0]", context);
// hobby == null

// With a default value
String firstHobby = (String) evaluator.evaluate("$person?.hobbies?[0] ?? 'Unknown'", context);
// firstHobby == "Unknown"
```

### Chained Operations

```java
// Complex expressions with array access work reliably
String result = evaluator.evaluate("$user?.orders?[0]?.items?[0]?.product?.name ?? 'No Product'", context);

// You can also break complex expressions into steps for clarity
context.setVariable("firstOrder", evaluator.evaluate("$user?.orders?[0]", context));
context.setVariable("firstItem", evaluator.evaluate("$firstOrder?.items?[0]", context));
String productName = (String) evaluator.evaluate("$firstItem?.product?.name ?? 'No Product'", context);
```

### Logical Operations with Null

```java
// Using null in logical operations
context.setVariable("user", null);
context.setVariable("hasAccess", true);

// Returns true because null || true => true
Boolean result1 = (Boolean) evaluator.evaluate("$user?.isAdmin || $hasAccess", context);

// Returns false because null && true => false
Boolean result2 = (Boolean) evaluator.evaluate("$user?.isActive && $hasAccess", context);
``` 