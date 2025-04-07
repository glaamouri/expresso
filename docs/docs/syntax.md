---
id: syntax
title: Expression Syntax
sidebar_position: 3
---

# Expression Syntax

Expresso provides a simple yet powerful syntax for writing expressions. This page covers all the syntax elements available in the language.

## Literals

Expresso supports various types of literals:

```java
// String literals
"Hello, World!"
'Hello, World!'

// Number literals
42
3.14
-42
-3.14

// Boolean literals
true
false

// Null literal
null
```

## Variables

Variables are accessed using the `$` prefix:

```java
// Simple variable
$name

// Object property access
$person.name
$person.address.city

// Array access
$scores[0]
$items[1].name

// Null-safe property access
$person?.name
$person?.address?.city

// Null-safe array access
$person?.hobbies?[0]

// Null coalescing operator (default value)
$person?.name ?? 'Unknown'
$person?.address?.city ?? 'No City'
$person?.hobbies?[0] ?? 'No Hobby'
```

## Null Safety Operators

Expresso provides special operators for handling null values safely:

```java
// Null-safe property access (?.)
// Returns null if person is null instead of throwing an exception
$person?.name

// Null-safe chained property access
// Returns null if any part of the chain is null
$person?.address?.city

// Null-safe array access (?[])
// Returns null if the array/list is null or the index is out of bounds
$person?.hobbies?[0]

// Null coalescing operator (??)
// Provides a default value if the left expression evaluates to null
$person?.name ?? 'Unknown'
$person?.address?.city ?? 'No City'
$scores?[5] ?? 0
```

These operators help you write more concise and robust code by eliminating the need for explicit null checks.

## Operators

Expresso supports various operators:

```java
// Arithmetic operators
$x + $y
$x - $y
$x * $y
$x / $y
$x % $y

// Comparison operators
$x > $y    // Greater than
$x < $y    // Less than
$x >= $y   // Greater than or equal to
$x <= $y   // Less than or equal to
$x == $y   // Equality
$x != $y   // Inequality

// Logical operators
$condition1 && $condition2  // Logical AND
$condition1 || $condition2  // Logical OR
!$condition  // Logical NOT
```

Comparison operators work with numbers, strings, and dates:

```java
// Numeric comparisons
10 > 5                      // true
5 < 10                      // true
5 >= 5                      // true
5 <= 5                      // true

// String comparisons (lexicographic ordering)
'banana' > 'apple'          // true
'apple' < 'banana'          // true
'banana' >= 'banana'        // true
'apple' <= 'banana'         // true
```

In logical operations, Expresso follows "truthiness" rules similar to JavaScript:

- `null` values are falsy (treated as `false` in logical expressions)
- `Boolean` values behave as expected (`true` is truthy, `false` is falsy)
- All other non-null values (numbers, strings, objects, arrays) are truthy

This allows for concise null checking and logical expressions:

```java
// These evaluate to true
!null
true || null
null || true
!null && true
"hello" && true     // Non-null non-boolean values are truthy
42 || false         // Numbers are truthy

// These evaluate to false
null && true
true && null
null && null
null || false
false && "hello"    // Short-circuit makes this false
```

## Type Handling in Comparisons

When comparing values of different types, Expresso follows these rules:

```java
// Number comparison - types are normalized for numeric comparisons
5 == 5.0                   // true
5 != 5.0                   // false
10 > 5.0                   // true

// String equality - type matters
5 == "5"                   // false (different types)
5 != "5"                   // true (different types)

// Null comparisons
$value == null             // true if $value is null
$value != null             // true if $value is not null

// Equality in comparisons with null
null == null               // true
"hello" == null            // false
null != "hello"            // true

// Ordering comparisons with null always return false
null > 5                   // false
5 > null                   // false
null < 5                   // false
5 < null                   // false
```

## Function Calls

Functions are called using parentheses:

```java
// Built-in functions
length($string)
substring($string, 0, 5)
round($number, 2)
max($x, $y)

// Function with multiple arguments
concat($first, $second, $third)
```

## Operator Precedence

Expresso follows standard operator precedence rules:

1. Parentheses `()`
2. Function calls `func()`
3. Member access `.`, Array access `[]`, Null-safe access `?.`, Null-safe array access `?[]`
4. Unary operators `!`, `-`
5. Multiplicative operators `*`, `/`, `%`
6. Additive operators `+`, `-`
7. Relational operators `>`, `<`, `>=`, `<=`
8. Equality operators `==`, `!=`
9. Logical AND `&&`
10. Logical OR `||`
11. Null coalescing operator `??` 