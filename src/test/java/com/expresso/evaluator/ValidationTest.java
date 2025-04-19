package com.expresso.evaluator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.expresso.ExpressionEvaluator;
import com.expresso.context.Context;

@DisplayName("Expression Validation Tests")
public class ValidationTest {

    private final ExpressionEvaluator evaluator = new ExpressionEvaluator();
    
    @Test
    @DisplayName("Basic validation functionality")
    void testBasicValidation() {
        // Simple expressions should be valid
        assertTrue(evaluator.validate("1 + 2"));
        assertTrue(evaluator.validate("'hello' + ' world'"));
        
        // Invalid expression should return false
        assertFalse(evaluator.validate("1 +"));
    }
    
    @Test
    @DisplayName("Validate with context")
    void testValidateWithContext() {
        Context context = new Context();
        context.setVariable("name", "John");
        context.setVariable("age", 30);
        
        // Valid expression with existing variables
        assertTrue(evaluator.validate("$name", context));
        assertTrue(evaluator.validate("$age >= 18", context));
        
        // Invalid expression with non-existent variables
        assertFalse(evaluator.validate("$salary", context));
    }
    
    @Test
    @DisplayName("Validate complex expressions")
    void testValidateComplexExpressions() {
        Context context = new Context();
        Map<String, Object> user = new HashMap<>();
        user.put("name", "Alice");
        context.setVariable("user", user);
        
        // Valid expression with existing properties
        assertTrue(evaluator.validate("$user.name", context));
        
        try {
            evaluator.evaluate("$nonExistentObject.property", context);
            fail("Expected PropertyNotFoundException was not thrown");
        } catch (Exception e) {
            assertFalse(evaluator.validate("$nonExistentObject.property", context));
        }
    }
    
    @Test
    @DisplayName("Validate null-safe expressions")
    void testValidateNullSafeExpressions() {
        Context context = new Context();
        context.setVariable("user", null);
        
        // Null-safe property access should be valid
        assertTrue(evaluator.validate("$user?.name", context));
        assertTrue(evaluator.validate("$user?.name ?? 'Unknown'", context));
    }
} 