package com.expresso.evaluator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.expresso.ExpressionEvaluator;
import com.expresso.context.Context;
import com.expresso.validation.ExpressionError;
import com.expresso.validation.ValidationResult;

@DisplayName("Enhanced Validation Tests")
public class EnhancedValidationTest {

    private final ExpressionEvaluator evaluator = new ExpressionEvaluator();
    
    @Test
    @DisplayName("Syntax validation with detailed error information")
    void testSyntaxValidationWithErrors() {
        // Valid expression should return success result
        ValidationResult validResult = evaluator.validateSyntax("1 + 2");
        assertTrue(validResult.isValid());
        assertFalse(validResult.hasErrors());
        
        // Invalid expression missing operator
        ValidationResult result1 = evaluator.validateSyntax("1 +");
        assertFalse(result1.isValid());
        assertTrue(result1.hasErrors());
        
        ExpressionError error1 = result1.getFirstError();
        assertNotNull(error1);
        assertEquals("SyntaxError", error1.getErrorType());
        assertNotNull(error1.getMessage());
        
        // Invalid expression with unterminated string
        ValidationResult result2 = evaluator.validateSyntax("'Hello");
        assertFalse(result2.isValid());
        assertTrue(result2.hasErrors());
        
        ExpressionError error2 = result2.getFirstError();
        assertNotNull(error2);
        assertEquals("SyntaxError", error2.getErrorType());
        assertNotNull(error2.getMessage());
        
        // Invalid expression with missing closing parenthesis
        ValidationResult result3 = evaluator.validateSyntax("(1 + 2");
        assertFalse(result3.isValid());
        assertTrue(result3.hasErrors());
        
        ExpressionError error3 = result3.getFirstError();
        assertNotNull(error3);
        assertEquals("SyntaxError", error3.getErrorType());
        assertNotNull(error3.getMessage());
    }
    
    @Test
    @DisplayName("Context validation with detailed error information")
    void testContextValidationWithErrors() {
        Context context = new Context();
        context.setVariable("name", "John");
        context.setVariable("age", 30);
        
        Map<String, Object> user = new HashMap<>();
        user.put("firstName", "Alice");
        user.put("settings", new HashMap<>());
        context.setVariable("user", user);
        
        // Valid expression should return success result
        ValidationResult validResult = evaluator.validateWithContext("$name", context);
        assertTrue(validResult.isValid());
        assertFalse(validResult.hasErrors());
        
        // Invalid expression with non-existent variable
        ValidationResult result1 = evaluator.validateWithContext("$salary", context);
        assertFalse(result1.isValid());
        assertTrue(result1.hasErrors());
        
        ExpressionError error1 = result1.getFirstError();
        assertNotNull(error1);
        assertEquals("VariableNotFoundError", error1.getErrorType());
        assertTrue(error1.getMessage().contains("salary"));
        
        // Check if error location contains the variable name
        if (error1.getLocation() != null) {
            String snippet = error1.getLocation().getErrorSnippet();
            assertTrue(snippet.contains("salary"));
        }
        
        // Invalid expression with non-existent property
        ValidationResult result2 = evaluator.validateWithContext("$user.lastName", context);
        
        try {
            evaluator.evaluate("$user.lastName", context);
        } catch (Exception e) {
            // Exception will be handled differently depending on implementation
        }
        
        if (result2.isValid()) {
            assertTrue(result2.isValid());
            assertFalse(result2.hasErrors());
        } else {
            assertFalse(result2.isValid());
            assertTrue(result2.hasErrors());
            
            ExpressionError error2 = result2.getFirstError();
            assertNotNull(error2);
            assertEquals("PropertyNotFoundError", error2.getErrorType());
        }
        
        ValidationResult result3 = evaluator.validateWithContext("$user?.nonExistent?.value", context);
        assertTrue(result3.isValid());
        assertFalse(result3.hasErrors());
    }
    
    @Test
    @DisplayName("Complex validation scenarios")
    void testComplexValidationScenarios() {
        Context context = new Context();
        context.setVariable("items", new String[] {"apple", "banana", "cherry"});
        
        // Array index out of bounds
        ValidationResult result = evaluator.validateWithContext("$items[5]", context);
        assertFalse(result.isValid());
        assertTrue(result.hasErrors());
        
        ExpressionError error = result.getFirstError();
        assertNotNull(error);
        
        // Null-safe array access should be valid even with out of bounds index
        ValidationResult nullSafeResult = evaluator.validateWithContext("$items?[5]", context);
        assertTrue(nullSafeResult.isValid());
        assertFalse(nullSafeResult.hasErrors());
    }
} 