---
id: function-discovery
title: Function Discovery
sidebar_position: 7
---

# Function Discovery

Expresso provides a powerful API to programmatically discover all available functions and their metadata. This enables you to build IDE integrations, generate documentation, or create dynamic user interfaces.

## Overview

The function discovery API allows you to:
- Retrieve a list of all available functions (built-in and custom)
- Get detailed information about specific functions
- Access parameter types and descriptions
- Determine return types
- Distinguish between built-in and custom functions

## Getting All Available Functions

Use `getAvailableFunctions()` to retrieve metadata for all functions:

```java
ExpressionEvaluator evaluator = new ExpressionEvaluator();

// Get all available functions
List<FunctionInfo> allFunctions = evaluator.getAvailableFunctions();

// Print function names and descriptions
allFunctions.forEach(info -> {
    System.out.println(info.getName() + " - " + info.getDescription());
});

// Example output:
// upperCase - Converts a string to uppercase
// lowerCase - Converts a string to lowercase
// abs - Returns the absolute value of a number
// ... and 70+ more functions
```

## Getting Specific Function Information

Use `getFunctionInfo(String name)` to get metadata for a specific function:

```java
ExpressionEvaluator evaluator = new ExpressionEvaluator();

// Get metadata for a specific function
FunctionInfo upperCaseInfo = evaluator.getFunctionInfo("upperCase");

if (upperCaseInfo != null) {
    System.out.println("Function: " + upperCaseInfo.getName());
    System.out.println("Description: " + upperCaseInfo.getDescription());
    System.out.println("Return Type: " + upperCaseInfo.getReturnType());
    System.out.println("Is Built-in: " + upperCaseInfo.isBuiltIn());
    
    // Get parameter information
    System.out.println("Parameters:");
    upperCaseInfo.getParameters().forEach(param -> {
        System.out.println("  - " + param.getName() + 
                         " (" + param.getType() + "): " + 
                         param.getDescription());
    });
}

// Output:
// Function: upperCase
// Description: Converts a string to uppercase
// Return Type: String
// Is Built-in: true
// Parameters:
//   - str (String): The string to convert
```

## FunctionInfo API

The `FunctionInfo` class provides the following methods:

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getName()` | `String` | The function name |
| `getDescription()` | `String` | Description of what the function does |
| `getReturnType()` |  `String` | The return type (e.g., "String", "Number", "Boolean") |
| `getParameters()` | `List<ParameterInfo>` | List of parameter information |
| `isBuiltIn()` | `boolean` | Whether it's a built-in function or custom |

The `ParameterInfo` class provides:

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getName()` | `String` | The parameter name |
| `getType()` | `String` | The parameter type |
| `getDescription()` | `String` | Description of the parameter |

## Custom Functions

Custom functions are also included in the function metadata:

```java
ExpressionEvaluator evaluator = new ExpressionEvaluator();

// Register a custom function
evaluator.registerFunction("customAdd", args -> 
    ((Number) args[0]).doubleValue() + ((Number) args[1]).doubleValue());

// Get all functions (including custom)
List<FunctionInfo> allFunctions = evaluator.getAvailableFunctions();

// Find the custom function
FunctionInfo customFunc = allFunctions.stream()
    .filter(f -> f.getName().equals("customAdd"))
    .findFirst()
    .orElse(null);

if (customFunc != null) {
    System.out.println("Found custom function: " + customFunc.getName());
    System.out.println("Is built-in: " + customFunc.isBuiltIn()); // false
}
```

Custom functions will have basic metadata:
- **Name**: The registered function name
- **Description**: "Custom function"
- **Return Type**: "Object"
- **Is Built-in**: `false`

## Use Cases

### 1. IDE Autocomplete

Build intelligent autocomplete features that suggest available functions with their signatures:

```java
public List<String> getAutoCompleteSuggestions(String prefix) {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    List<FunctionInfo> functions = evaluator.getAvailableFunctions();
    
    return functions.stream()
        .filter(f -> f.getName().startsWith(prefix))
        .map(f -> {
            String params = f.getParameters().stream()
                .map(p -> p.getType() + " " + p.getName())
                .collect(Collectors.joining(", "));
            return f.getName() + "(" + params + ") : " + f.getReturnType();
        })
        .collect(Collectors.toList());
}
```

### 2. Documentation Generation

Automatically generate documentation from function metadata:

```java
public void generateDocumentation() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    List<FunctionInfo> functions = evaluator.getAvailableFunctions();
    
    functions.stream()
        .filter(FunctionInfo::isBuiltIn)
        .forEach(func -> {
            System.out.println("### " + func.getName());
            System.out.println(func.getDescription());
            System.out.println("\n**Parameters:**");
            func.getParameters().forEach(p -> {
                System.out.println("- `" + p.getName() + "` (" + p.getType() + "): " + p.getDescription());
            });
            System.out.println("\n**Returns:** " + func.getReturnType() + "\n");
        });
}
```

### 3. Function Validation

Validate that all required functions are available before evaluation:

```java
public boolean validateRequiredFunctions(String[] requiredFunctions) {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Set<String> availableFunctions = evaluator.getAvailableFunctions().stream()
        .map(FunctionInfo::getName)
        .collect(Collectors.toSet());
    
    for (String required : requiredFunctions) {
        if (!availableFunctions.contains(required)) {
            System.err.println("Missing required function: " + required);
            return false;
        }
    }
    return true;
}
```

### 4. Dynamic Function Lists

Build dynamic user interfaces that show available functions:

```java
public Map<String, List<FunctionInfo>> getFunctionsByCategory() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    List<FunctionInfo> functions = evaluator.getAvailableFunctions();
    
    // Group by return type as a simple categorization
    return functions.stream()
        .filter(FunctionInfo::isBuiltIn)
        .collect(Collectors.grouping(
            FunctionInfo::getReturnType
        ));
}
```

## Best Practices

1. **Cache Function Metadata**: Function metadata doesn't change during runtime, so cache it if you're calling `getAvailableFunctions()` frequently

2. **Filter Built-in vs Custom**: Use `isBuiltIn()` to distinguish between library functions and user-defined functions

3. **Validate Before Evaluation**: Check that required functions exist before evaluating expressions

4. **Use for Code Generation**: Leverage metadata to generate type-safe wrappers or DSLs

5. **Build Development Tools**: Create IDE plugins, documentation generators, or debugging tools using this API
