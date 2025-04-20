---
id: contribution
title: Contributing to Expresso
sidebar_position: 11
---

# Contributing to Expresso

We welcome contributions to Expresso! This guide will help you get started with contributing to the project.

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Git

### Setup

1. Fork the repository on GitHub
2. Clone your fork:
   ```bash
   git clone https://github.com/your-username/expresso.git
   cd expresso
   ```
3. Add the upstream repository:
   ```bash
   git remote add upstream https://github.com/glaamouri/expresso.git
   ```
4. Create a branch for your work:
   ```bash
   git checkout -b feature/your-feature-name
   ```

### Building Locally

```bash
mvn clean install
```

### Running Tests

```bash
mvn test
```

## Development Guidelines

### Code Style

Expresso follows standard Java code style conventions:

- Use 4 spaces for indentation (no tabs)
- Use camelCase for variables and methods
- Use PascalCase for class names
- Use UPPER_SNAKE_CASE for constants


### Adding Tests

We use JUnit 5 for testing. Add tests for any new functionality:

```java
@Test
void testMyNewFunction() {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Context context = new Context();
    context.setVariable("a", 10);
    context.setVariable("b", 20);
    
    Object result = evaluator.evaluate("myNewFunction($a, $b)", context);
    assertEquals(expectedValue, result);
}
```

## Pull Request Process

1. Update your branch with the latest upstream changes:
   ```bash
   git fetch upstream
   git rebase upstream/main
   ```

2. Run all tests to ensure your changes don't break existing functionality:
   ```bash
   mvn test
   ```

3. Push your changes to your fork:
   ```bash
   git push origin feature/your-feature-name
   ```

4. Create a pull request on GitHub with a clear description:
   - What the PR changes
   - Why these changes are needed
   - Any related issues

5. Address any code review feedback

## Documentation

When adding new features, please update the documentation:

1. Update relevant Markdown files in the `docs/` directory
2. Add Javadoc comments to public API methods
3. Provide examples of how to use the new feature

## Reporting Bugs

When reporting bugs, please include:

1. What you were trying to do
2. The actual result
3. The expected result
4. Steps to reproduce
5. Version information (Java version, OS, Expresso version)
6. If possible, a minimal code example that reproduces the bug

## Feature Requests

Feature requests are welcome! When suggesting a feature:

1. Describe the problem you're trying to solve
2. Explain how your feature would help
3. Provide examples of how the feature would be used
4. If possible, suggest an implementation approach

## Code of Conduct

- Be respectful and inclusive
- Focus on constructive feedback
- Accept feedback graciously
- Help others learn and grow

Thank you for contributing to Expresso! 