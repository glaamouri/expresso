---
id: security
title: Security Best Practices
sidebar_position: 9
---

# Security Best Practices

When allowing users or external systems to provide expressions that will be evaluated in your application, it's important to implement proper security measures. This page outlines best practices for using Expresso securely.

## Validation Before Evaluation

The most important security practice is to validate all expressions before evaluating them:

```java
// Never directly evaluate untrusted expressions
String userExpression = getUserInputExpression();
Context context = prepareContext();

// WRONG - Don't do this with untrusted input
Object result = evaluator.evaluate(userExpression, context); // SECURITY RISK!

// RIGHT - Always validate first
ValidationResult validationResult = evaluator.validateWithContext(userExpression, context);
if (validationResult.isValid()) {
    // Safe to evaluate
    Object result = evaluator.evaluate(userExpression, context);
    processResult(result);
} else {
    // Handle invalid expression
    handleValidationError(validationResult.getFirstError());
}
```

## Sandboxed Execution

Limit what expressions can access:

```java
// Create a dedicated, controlled context for user expressions
Context sandboxedContext = new Context();

// Only provide necessary variables
sandboxedContext.setVariable("product", Map.of(
    "name", product.getName(),
    "price", product.getPrice(),
    "category", product.getCategory()
));

// Don't expose sensitive data
// WRONG
sandboxedContext.setVariable("user", currentUser); // May expose sensitive fields

// RIGHT
sandboxedContext.setVariable("user", Map.of(
    "name", currentUser.getName(),
    "isLoggedIn", currentUser.isLoggedIn()
    // Only expose what's needed
));
```

## Custom Function Safety

Be careful when registering custom functions:

```java
// WRONG - This could allow file system access
evaluator.registerFunction("readFile", args -> {
    String filename = (String) args[0];
    return new String(Files.readAllBytes(Path.of(filename)));
}); // SECURITY RISK!

// WRONG - This could allow arbitrary code execution
evaluator.registerFunction("execute", args -> {
    String command = (String) args[0];
    Process process = Runtime.getRuntime().exec(command);
    // ... read output
    return output;
}); // SECURITY RISK!

// RIGHT - Only register safe functions
evaluator.registerFunction("round", args -> {
    double number = ((Number) args[0]).doubleValue();
    int decimals = ((Number) args[1]).intValue();
    return BigDecimal.valueOf(number)
            .setScale(decimals, RoundingMode.HALF_UP)
            .doubleValue();
});
```

## Expression Complexity Limits

Prevent DoS attacks by limiting expression complexity:

```java
public class SecurityValidator {
    private static final int MAX_EXPRESSION_LENGTH = 500;
    private static final int MAX_NESTING_LEVEL = 5;
    
    public static boolean isSafeExpression(String expression) {
        // Check expression length
        if (expression.length() > MAX_EXPRESSION_LENGTH) {
            return false;
        }
        
        // Check nesting level (simplified example)
        int maxParenLevel = 0;
        int currentLevel = 0;
        
        for (char c : expression.toCharArray()) {
            if (c == '(') {
                currentLevel++;
                maxParenLevel = Math.max(maxParenLevel, currentLevel);
            } else if (c == ')') {
                currentLevel--;
            }
        }
        
        return maxParenLevel <= MAX_NESTING_LEVEL;
    }
}

// Usage
String userExpression = getUserExpression();
if (!SecurityValidator.isSafeExpression(userExpression)) {
    throw new SecurityException("Expression exceeds complexity limits");
}
```

## Input Sanitization

Sanitize expressions before validation:

```java
public class ExpressionSanitizer {
    private static final Pattern VALID_EXPRESSION_PATTERN = Pattern.compile(
        "^[\\w\\s\\d$.,(){}\\[\\]?:+\\-*/\"'<>=!&|%]+$"
    );
    
    public static String sanitize(String expression) {
        // Check if expression contains only allowed characters
        if (!VALID_EXPRESSION_PATTERN.matcher(expression).matches()) {
            throw new IllegalArgumentException("Expression contains invalid characters");
        }
        
        return expression;
    }
}

// Usage
String userExpression = getUserExpression();
try {
    String sanitized = ExpressionSanitizer.sanitize(userExpression);
    // Continue with validation and evaluation
} catch (IllegalArgumentException e) {
    // Handle invalid expression
}
```

## Comprehensive Security Strategy

For a complete security strategy:

1. **Input Validation**: Sanitize and validate all expressions
2. **Least Privilege**: Only expose necessary variables and functions
3. **Resource Limits**: Implement timeouts and complexity limits
4. **Auditing**: Log all expression evaluations for security review
5. **Error Handling**: Don't leak sensitive information in error messages

```java
public class SecureExpressionService {
    private final ExpressionEvaluator evaluator;
    private final Logger logger = LoggerFactory.getLogger(SecureExpressionService.class);
    
    public SecureExpressionService() {
        this.evaluator = new ExpressionEvaluator();
        // Register only safe functions
        registerSafeFunctions();
    }
    
    public Object evaluateUserExpression(String expression, Map<String, Object> userVariables, String userId) {
        // 1. Log the attempt
        logger.info("Expression evaluation requested by user {}: {}", userId, expression);
        
        try {
            // 2. Sanitize
            if (!ExpressionSanitizer.isSafeExpression(expression)) {
                logger.warn("Rejected unsafe expression from user {}: {}", userId, expression);
                throw new SecurityException("Expression contains potentially unsafe elements");
            }
            
            // 3. Prepare safe context
            Context context = createSandboxedContext(userVariables);
            
            // 4. Validate with timeout
            CompletableFuture<ValidationResult> validationFuture = CompletableFuture.supplyAsync(() -> 
                evaluator.validateWithContext(expression, context)
            );
            
            ValidationResult validationResult = validationFuture.get(2, TimeUnit.SECONDS);
            
            if (!validationResult.isValid()) {
                logger.warn("Invalid expression from user {}: {}", userId, validationResult.getFirstError());
                throw new IllegalArgumentException("Invalid expression: " + validationResult.getFirstError().getMessage());
            }
            
            // 5. Evaluate with timeout
            CompletableFuture<Object> evaluationFuture = CompletableFuture.supplyAsync(() -> 
                evaluator.evaluate(expression, context)
            );
            
            Object result = evaluationFuture.get(5, TimeUnit.SECONDS);
            
            // 6. Log success
            logger.info("Successfully evaluated expression for user {}", userId);
            
            return result;
            
        } catch (TimeoutException e) {
            logger.warn("Evaluation timeout for user {}: {}", userId, expression);
            throw new RuntimeException("Expression evaluation timed out");
        } catch (Exception e) {
            logger.error("Error evaluating expression for user {}: {}", userId, e.getMessage());
            throw new RuntimeException("Expression evaluation failed");
        }
    }
    
    private Context createSandboxedContext(Map<String, Object> userVariables) {
        Context context = new Context();
        
        // Filter and sanitize variables
        userVariables.forEach((key, value) -> {
            if (isSafeVariable(key, value)) {
                context.setVariable(key, sanitizeValue(value));
            }
        });
        
        return context;
    }
    
    private boolean isSafeVariable(String key, Object value) {
        // Implement security checks for variable names and values
        return true; // Simplified for example
    }
    
    private Object sanitizeValue(Object value) {
        // Implement deep sanitization of complex objects
        return value; // Simplified for example
    }
    
    private void registerSafeFunctions() {
        // Only register safe, controlled functions
    }
}
```

By following these security practices, you can safely allow users to define custom expressions while minimizing security risks. 