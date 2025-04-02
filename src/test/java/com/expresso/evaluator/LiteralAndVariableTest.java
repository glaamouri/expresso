package com.expresso.evaluator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.expresso.ExpressionEvaluator;
import com.expresso.context.Context;
import com.expresso.exception.PropertyNotFoundException;

/**
 * Tests for literal values and variable expressions in the ExpressionEvaluator.
 */
@DisplayName("Literal Values and Variables")
class LiteralAndVariableTest {
    
    private final ExpressionEvaluator evaluator = new ExpressionEvaluator();
    
    @Test
    @DisplayName("Evaluate simple literal values")
    void testSimpleLiterals() {
        Context context = new Context();
        
        // Boolean literals
        assertEquals(true, evaluator.evaluate("true", context));
        assertEquals(false, evaluator.evaluate("false", context));
        
        // String literals
        assertEquals("hello", evaluator.evaluate("'hello'", context));
        assertEquals("world", evaluator.evaluate("\"world\"", context));
        
        // Null literal
        assertEquals(null, evaluator.evaluate("null", context));
    }
    
    @Test
    @DisplayName("Evaluate empty and special character strings")
    void testSpecialStrings() {
        Context context = new Context();
        
        // Empty string
        assertEquals("", evaluator.evaluate("''", context));
        assertEquals("", evaluator.evaluate("\"\"", context));
        
        // Special characters
        assertEquals("line 1\nline 2", evaluator.evaluate("'line 1\\nline 2'", context));
        assertEquals("tab\tseparated", evaluator.evaluate("'tab\\tseparated'", context));
        assertEquals("\"quoted\"", evaluator.evaluate("'\"quoted\"'", context));
        assertEquals("'quoted'", evaluator.evaluate("\"'quoted'\"", context));
    }
    
    @Test
    @DisplayName("Evaluate variables with different types")
    void testVariables() {
        Context context = new Context();
        
        // Setup variables of different types
        context.setVariable("stringVar", "text value");
        context.setVariable("numberVar", 42);
        context.setVariable("boolVar", true);
        context.setVariable("nullVar", null);
        
        // Access variables
        assertEquals("text value", evaluator.evaluate("$stringVar", context));
        assertEquals(42, evaluator.evaluate("$numberVar", context));
        assertEquals(true, evaluator.evaluate("$boolVar", context));
        assertEquals(null, evaluator.evaluate("$nullVar", context));
    }
    
    @Test
    @DisplayName("Throw error for non-existent variables")
    void testNonExistentVariables() {
        Context context = new Context();
        
        // Non-existent variable should throw exception
        assertThrows(PropertyNotFoundException.class, () -> 
                evaluator.evaluate("$nonExistent", context));
    }
    
    @Test
    @DisplayName("Evaluate numeric literals and operations")
    void testNumericLiterals() {
        Context context = new Context();
        
        // Integer literals (actually parsed as Long in the implementation)
        assertEquals(0L, evaluator.evaluate("0", context));
        assertEquals(123L, evaluator.evaluate("123", context));
        assertEquals(-456.0, evaluator.evaluate("-456", context));
        
        // Decimal literals (parsed as Double)
        assertEquals(3.14159, evaluator.evaluate("3.14159", context));
        assertEquals(-2.718, evaluator.evaluate("-2.718", context));
        assertEquals(0.0, evaluator.evaluate("0.0", context));
    }
} 