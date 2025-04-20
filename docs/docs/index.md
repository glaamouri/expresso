---
id: introduction
title: Overview
sidebar_position: 1
---

# Expresso - Dynamic Expression Evaluator

Expresso is a lightweight, zero-dependency Java expression evaluator library designed for dynamically evaluating expressions in your applications. Whether you need to evaluate user-defined formulas, content stored in databases, or rule-based calculations, Expresso provides a powerful engine to safely evaluate dynamic expressions with contextual data.

## Key Features

- **Dynamic Evaluation**: Evaluate expressions written by users or stored in databases
- **Context-Driven**: Calculate results based on supplied context variables
- **Zero Dependencies**: Pure Java implementation with no external libraries
- **Type-Safe**: Strong type checking ensures reliable results
- **Null Safety**: Safely handle null values with specialized operators
- **Security**: Validate expressions before evaluation
- **Extensible**: Add custom functions to meet your specific needs
- **Comprehensive Error Handling**: Detailed error reporting

## Use Cases

- **User-Defined Formulas**: Allow end-users to define custom calculations
- **Rule Engines**: Implement business rules stored in databases
- **Dynamic Content**: Generate content based on contextual variables
- **Configurable Workflows**: Create configurable business processes
- **Data Transformation**: Transform data using dynamic rules
- **Template Engines**: Enhance templates with expression evaluation

## Quick Start

Add Expresso to your project using Maven:

```xml
<dependency>
    <groupId>work.ghassen</groupId>
    <artifactId>expresso</artifactId>
    <version>{latestStable.version}</version>
</dependency>
```

## Requirements

- Java 17 or higher
- Maven 3.6 or higher (for building) 