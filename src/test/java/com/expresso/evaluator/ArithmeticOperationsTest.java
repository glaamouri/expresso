package com.expresso.evaluator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.expresso.ExpressionEvaluator;
import com.expresso.context.Context;

/**
 * Tests for arithmetic operations in the ExpressionEvaluator.
 */
@DisplayName("Arithmetic Operations")
class ArithmeticOperationsTest {
    
    private final ExpressionEvaluator evaluator = new ExpressionEvaluator();
    
    @Test
    @DisplayName("Basic arithmetic operations")
    void testBasicArithmetic() {
        Context context = new Context();
        
        // Basic arithmetic
        assertEquals(10.0, evaluator.evaluate("5 + 5", context));
        assertEquals(0.0, evaluator.evaluate("5 - 5", context));
        assertEquals(25.0, evaluator.evaluate("5 * 5", context));
        assertEquals(1.0, evaluator.evaluate("5 / 5", context));
        assertEquals(2.0, evaluator.evaluate("5 % 3", context));
    }
    
    @Test
    @DisplayName("Order of operations (PEMDAS)")
    void testOrderOfOperations() {
        Context context = new Context();
        
        // Order of operations
        assertEquals(15.0, evaluator.evaluate("5 + 5 * 2", context));
        assertEquals(20.0, evaluator.evaluate("(5 + 5) * 2", context));
        assertEquals(7.0, evaluator.evaluate("5 + 6 / 3", context));
        assertEquals(7.5, evaluator.evaluate("(5 + 10) / 2", context));
    }
    
    @Test
    @DisplayName("Arithmetic with mixed types")
    void testMixedTypeArithmetic() {
        Context context = new Context();
        
        // Mixed types
        assertEquals(5.5, evaluator.evaluate("5 + 0.5", context));
        assertEquals(10.5, evaluator.evaluate("10 + 0.5", context));
        assertEquals(4.5, evaluator.evaluate("5 - 0.5", context));
        assertEquals(2.5, evaluator.evaluate("5 / 2", context));
    }
    
    @Test
    @DisplayName("Arithmetic with negative numbers")
    void testNegativeNumbers() {
        Context context = new Context();
        
        // Negative numbers
        assertEquals(-5.0, evaluator.evaluate("-5", context));
        assertEquals(-15.0, evaluator.evaluate("-5 * 3", context));
        assertEquals(-2.0, evaluator.evaluate("-(5 - 3)", context));
    }
    
    @Test
    @DisplayName("Complex arithmetic expressions")
    void testComplexExpressions() {
        Context context = new Context();
        
        // Complex expressions
        assertEquals(21.0, evaluator.evaluate("3 * (5 + 2)", context));
        assertEquals(13.0, evaluator.evaluate("5 + 2 * 5 - 2", context));
        assertEquals(12.0, evaluator.evaluate("(5 + 2) * (10 / 5) - 2", context));
    }
    
    @Test
    @DisplayName("Arithmetic with variables")
    void testArithmeticWithVariables() {
        Context context = new Context();
        
        // With variables
        context.setVariable("x", 10);
        context.setVariable("y", 5);
        
        assertEquals(15.0, evaluator.evaluate("$x + $y", context));
        assertEquals(5.0, evaluator.evaluate("$x - $y", context));
        assertEquals(50.0, evaluator.evaluate("$x * $y", context));
        assertEquals(2.0, evaluator.evaluate("$x / $y", context));
        assertEquals(0.0, evaluator.evaluate("$x % $y", context));
    }
    
    @Test
    @DisplayName("Arithmetic with function results")
    void testArithmeticWithFunctions() {
        Context context = new Context();
        
        assertEquals(5.0, evaluator.evaluate("abs(-2) + ceil(2.1)", context));
        assertEquals(1.0, evaluator.evaluate("floor(5.9) - 4", context));
        assertEquals(9.0, evaluator.evaluate("round(3.3) * 3", context));
        assertEquals(2.5, evaluator.evaluate("10 / max(2, 4)", context));
    }
} 