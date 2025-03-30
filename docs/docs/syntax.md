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
$x == $y
$x != $y
$x > $y
$x >= $y
$x < $y
$x <= $y

// Logical operators
$x && $y
$x || $y
!$x
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
7. Relational operators `<`, `<=`, `>`, `>=`
8. Equality operators `==`, `!=`
9. Logical AND `&&`
10. Logical OR `||`
11. Null coalescing operator `??` 