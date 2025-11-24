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
        assertEquals(10.0, (Double) evaluator.evaluate("5 + 5", context), 0.001);
        assertEquals(0.0, (Double) evaluator.evaluate("5 - 5", context), 0.001);
        assertEquals(25.0, (Double) evaluator.evaluate("5 * 5", context), 0.001);
        assertEquals(1.0, (Double) evaluator.evaluate("5 / 5", context), 0.001);
        assertEquals(2.0, (Double) evaluator.evaluate("5 % 3", context), 0.001);
    }
    
    @Test
    @DisplayName("Order of operations (PEMDAS)")
    void testOrderOfOperations() {
        Context context = new Context();
        
        // Order of operations
        assertEquals(15.0, (Double) evaluator.evaluate("5 + 5 * 2", context), 0.001);
        assertEquals(20.0, (Double) evaluator.evaluate("(5 + 5) * 2", context), 0.001);
        assertEquals(7.0, (Double) evaluator.evaluate("5 + 6 / 3", context), 0.001);
        assertEquals(7.5, (Double) evaluator.evaluate("(5 + 10) / 2", context), 0.001);
    }
    
    @Test
    @DisplayName("Arithmetic with mixed types")
    void testMixedTypeArithmetic() {
        Context context = new Context();
        
        // Mixed types
        assertEquals(5.5, (Double) evaluator.evaluate("5 + 0.5", context), 0.001);
        assertEquals(10.5, (Double) evaluator.evaluate("10 + 0.5", context), 0.001);
        assertEquals(4.5, (Double) evaluator.evaluate("5 - 0.5", context), 0.001);
        assertEquals(2.5, (Double) evaluator.evaluate("5 / 2", context), 0.001);
    }
    
    @Test
    @DisplayName("Arithmetic with negative numbers")
    void testNegativeNumbers() {
        Context context = new Context();
        
        // Negative numbers
        assertEquals(-5.0, (Double) evaluator.evaluate("-5", context), 0.001);
        assertEquals(-15.0, (Double) evaluator.evaluate("-5 * 3", context), 0.001);
        assertEquals(-2.0, (Double) evaluator.evaluate("-(5 - 3)", context), 0.001);
    }
    
    @Test
    @DisplayName("Complex arithmetic expressions")
    void testComplexExpressions() {
        Context context = new Context();
        
        // Complex expressions
        assertEquals(21.0, (Double) evaluator.evaluate("3 * (5 + 2)", context), 0.001);
        assertEquals(13.0, (Double) evaluator.evaluate("5 + 2 * 5 - 2", context), 0.001);
        assertEquals(12.0, (Double) evaluator.evaluate("(5 + 2) * (10 / 5) - 2", context), 0.001);
    }
    
    @Test
    @DisplayName("Arithmetic with variables")
    void testArithmeticWithVariables() {
        Context context = new Context();
        
        // With variables
        context.setVariable("x", 10);
        context.setVariable("y", 5);
        
        assertEquals(15.0, (Double) evaluator.evaluate("$x + $y", context), 0.001);
        assertEquals(5.0, (Double) evaluator.evaluate("$x - $y", context), 0.001);
        assertEquals(50.0, (Double) evaluator.evaluate("$x * $y", context), 0.001);
        assertEquals(2.0, (Double) evaluator.evaluate("$x / $y", context), 0.001);
        assertEquals(0.0, (Double) evaluator.evaluate("$x % $y", context), 0.001);
    }
    
    @Test
    @DisplayName("Arithmetic with function results")
    void testArithmeticWithFunctions() {
        Context context = new Context();
        
        assertEquals(5.0, (Double) evaluator.evaluate("abs(-2) + ceil(2.1)", context), 0.001);
        assertEquals(1.0, (Double) evaluator.evaluate("floor(5.9) - 4", context), 0.001);
        assertEquals(9.0, (Double) evaluator.evaluate("round(3.3) * 3", context), 0.001);
        assertEquals(2.5, (Double) evaluator.evaluate("10 / max(2, 4)", context), 0.001);
    }
} 
