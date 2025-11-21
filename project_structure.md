# Project Structure

This document provides a global view of the Expresso project structure and its components.

## Global View

Expresso is a Java-based expression evaluator library designed to be lightweight and dependency-free. It follows a standard Maven project structure.

### Root Directory

- **`README.md`**: Project documentation, features, and usage examples.
- **`pom.xml`**: Maven configuration file defining dependencies (JUnit, Mockito for testing) and build plugins.
- **`LICENSE.txt`**: MIT License file.
- **`src/`**: Source code directory.
- **`docs/`**: Documentation website source code (Docusaurus project).

## Source Code Structure

The main source code is located in `src/main/java/com/expresso`.

### Key Components

1.  **`ExpressionEvaluator.java`**
    - The main entry point for the library.
    - Handles the evaluation of expressions against a given context.
    - Manages function registration and parsing delegation.

2.  **`ast/` (Abstract Syntax Tree)**
    - Contains classes representing the nodes of the parsed expression tree.
    - Likely includes nodes for literals, variables, binary operations, function calls, etc.

3.  **`context/`**
    - Manages the execution context.
    - Handles variable storage and retrieval during evaluation.

4.  **`exception/`**
    - Defines custom exceptions for the library.
    - expected exceptions: `SyntaxException`, `EvaluationException`, `PropertyNotFoundException`.

5.  **`parser/`**
    - Responsible for parsing string expressions into an Abstract Syntax Tree (AST).
    - Likely contains a tokenizer (lexer) and a parser implementation.

6.  **`util/`**
    - Utility classes used across the library.

7.  **`validation/`**
    - Logic for validating expressions or arguments.

## Documentation

The `docs/` directory contains the source code for the project's documentation website, built with [Docusaurus](https://docusaurus.io/).

- **`docusaurus.config.js`**: Configuration file for the Docusaurus site.
- **`docs/`**: Contains the actual markdown documentation files.
- **`src/`**: React components and pages for the documentation site.
- **`static/`**: Static assets (images, etc.).
- **`sidebars.js`**: Configuration for the documentation sidebar.

## Testing

Tests are located in `src/test/java` and follow the same package structure. They use JUnit 5 and Mockito for unit testing the components.
