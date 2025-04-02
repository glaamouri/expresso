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

## Current Limitations with Complex Property Paths

There are some limitations with the current implementation of property access on array elements:

```java
// Accessing properties on array elements may not work as expected in complex paths
$company.departments[0].name  // This may throw a PropertyNotFoundException

// Instead, use separate expressions or variables:
// 1. First, get the department
$dept = $company.departments[0]
// 2. Then access its properties
$dept.name
```

When working with complex property paths that include both array access and subsequent property access, you might encounter issues with the property path parser. As a workaround:

1. Use separate expressions to retrieve array elements first
2. Then access properties on the retrieved elements
3. Alternatively, consider using the null-safe versions of operators even if you believe the values won't be null

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

// For complex paths with array access, use intermediary steps:
// Instead of this (which might not work reliably):
// $orders?[0]?.items?[0]?.productName ?? 'No Product'

// Do this:
$firstOrder = $orders?[0]
$firstItem = $firstOrder?.items?[0]
$productName = $firstItem?.productName ?? 'No Product'

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
// For complex expressions with array access, break them into steps
// Instead of:
// String result = evaluator.evaluate("$user?.orders?[0]?.items?[0]?.product?.name ?? 'No Product'", context);

// Do this:
context.setVariable("firstOrder", evaluator.evaluate("$user?.orders?[0]", context));
context.setVariable("firstItem", evaluator.evaluate("$firstOrder?.items?[0]", context));
String result = (String) evaluator.evaluate("$firstItem?.product?.name ?? 'No Product'", context);
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