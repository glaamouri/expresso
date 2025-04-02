package com.expresso.evaluator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.expresso.ExpressionEvaluator;
import com.expresso.context.Context;

/**
 * Tests for logical operations in the ExpressionEvaluator.
 */
@DisplayName("Logical Operations")
class LogicalOperationsTest {
    
    private final ExpressionEvaluator evaluator = new ExpressionEvaluator();
    
    @Test
    @DisplayName("Basic logical operations")
    void testBasicLogicalOperations() {
        Context context = new Context();
        
        // AND operations
        assertEquals(true, evaluator.evaluate("true && true", context));
        assertEquals(false, evaluator.evaluate("true && false", context));
        assertEquals(false, evaluator.evaluate("false && true", context));
        assertEquals(false, evaluator.evaluate("false && false", context));
        
        // OR operations
        assertEquals(true, evaluator.evaluate("true || true", context));
        assertEquals(true, evaluator.evaluate("true || false", context));
        assertEquals(true, evaluator.evaluate("false || true", context));
        assertEquals(false, evaluator.evaluate("false || false", context));
        
        // NOT operations
        assertEquals(false, evaluator.evaluate("!true", context));
        assertEquals(true, evaluator.evaluate("!false", context));
        
        // Precedence - AND has higher precedence than OR
        assertEquals(true, evaluator.evaluate("true || false && false", context));
        assertEquals(true, evaluator.evaluate("false || true && true", context));
        assertEquals(false, evaluator.evaluate("false || false && true", context));
        
        // With parentheses to override precedence
        assertEquals(false, evaluator.evaluate("(true || false) && false", context));
    }
    
    @Test
    @DisplayName("Combined logical operations")
    void testCombinedLogicalOperations() {
        Context context = new Context();
        
        // Combined operations
        assertEquals(true, evaluator.evaluate("true && true || false", context));
        assertEquals(false, evaluator.evaluate("true && false || false", context));
        assertEquals(true, evaluator.evaluate("true && (false || true)", context));
        assertEquals(false, evaluator.evaluate("(true || false) && false", context));
        
        // With NOT operations
        assertEquals(false, evaluator.evaluate("!true && true", context));
        assertEquals(true, evaluator.evaluate("!(false && false)", context));
    }
    
    @Test
    @DisplayName("Using variables in logical operations")
    void testLogicalOperationsWithVariables() {
        Context context = new Context();
        
        context.setVariable("a", true);
        context.setVariable("b", false);
        
        // Basic usage of variables
        assertEquals(true, evaluator.evaluate("$a || $b", context));
        assertEquals(false, evaluator.evaluate("$a && $b", context));
        assertEquals(true, evaluator.evaluate("!$b", context));
        
        // Combined operations with variables
        assertEquals(true, evaluator.evaluate("$a || $b && $b", context));
        assertEquals(false, evaluator.evaluate("$a && $b || $b && $b", context));
        assertEquals(true, evaluator.evaluate("$a && !$b", context));
        assertEquals(false, evaluator.evaluate("!($a || !$b)", context));
    }
    
    @Test
    @DisplayName("Logical operations with comparison expressions")
    void testLogicalOperationsWithComparisons() {
        Context context = new Context();
        
        context.setVariable("age", 25);
        context.setVariable("score", 85);
        
        // Verify comparison operations return boolean values
        assertEquals(true, evaluator.evaluate("$age > 18 && $score >= 70", context));
        assertEquals(false, evaluator.evaluate("$age < 21 || $score < 60", context));
        assertEquals(true, evaluator.evaluate("!($age < 21) || $score < 60", context));
        
        // More complex combinations
        assertEquals(true, evaluator.evaluate("($age > 20 && $age < 30) && $score > 80", context));
        assertEquals(false, evaluator.evaluate("($age < 20 || $age > 30) && $score > 90", context));
    }
    
    @Test
    @DisplayName("Triple logical operations")
    void testTripleLogicalOperations() {
        Context context = new Context();
        
        // Triple logical operators
        assertEquals(true, evaluator.evaluate("true && true && true", context));
        assertEquals(false, evaluator.evaluate("true && true && false", context));
        assertEquals(false, evaluator.evaluate("true && false && true", context));
        assertEquals(false, evaluator.evaluate("false && true && true", context));
        
        assertEquals(true, evaluator.evaluate("true || true || true", context));
        assertEquals(true, evaluator.evaluate("true || true || false", context));
        assertEquals(true, evaluator.evaluate("true || false || true", context));
        assertEquals(true, evaluator.evaluate("false || true || true", context));
        assertEquals(false, evaluator.evaluate("false || false || false", context));
    }
    
    @Test
    @DisplayName("Nested logical expressions")
    void testNestedLogicalExpressions() {
        Context context = new Context();
        
        // Nested expressions
        assertEquals(true, evaluator.evaluate("!(false && true) && true", context));
        assertEquals(true, evaluator.evaluate("!false && !false", context));
        assertEquals(false, evaluator.evaluate("!(true || false) || false", context));
        
        // Multiple NOT operations
        assertEquals(true, evaluator.evaluate("!!true", context));
        assertEquals(false, evaluator.evaluate("!!false", context));
        assertEquals(false, evaluator.evaluate("!!!true", context));
        assertEquals(true, evaluator.evaluate("!!!false", context));
    }
    
    @Test
    @DisplayName("Null values in logical operations")
    void testNullInLogicalOperations() {
        Context context = new Context();
        
        // Setup test data
        context.setVariable("trueValue", true);
        context.setVariable("falseValue", false);
        context.setVariable("nullVariable", null);
        
        // Test basic logical operations with nulls
        assertEquals(false, evaluator.evaluate("$nullVariable && $trueValue", context));
        assertEquals(true, evaluator.evaluate("$nullVariable || $trueValue", context));
        assertEquals(false, evaluator.evaluate("$nullVariable || $falseValue", context));
        assertEquals(true, evaluator.evaluate("!$nullVariable", context));
        
        // Complex expressions with null variable
        assertEquals(false, evaluator.evaluate("$nullVariable && $trueValue && $trueValue", context));
        assertEquals(true, evaluator.evaluate("$nullVariable || $trueValue || $falseValue", context));
    }
} 