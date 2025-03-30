---
id: null-safety
title: Null-Safety Operators
sidebar_position: 3.5
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

## Combined Usage

These operators are particularly powerful when combined:

```java
// Combining null-safe property access, array access, and null coalescing
$person?.address?.city ?? 'Unknown City'
$person?.hobbies?[0] ?? 'No Hobby'
$orders?[0]?.items?[0]?.productName ?? 'No Product'
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
// Complex expression with chained null-safe access and default value
String result = (String) evaluator.evaluate(
    "$user?.orders?[0]?.items?[0]?.product?.name ?? 'No Product'", 
    context
);
```

This expression will return the product name if the entire chain is non-null, otherwise it returns 'No Product'. 