package com.expresso.evaluator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.expresso.ExpressionEvaluator;
import com.expresso.context.Context;

/**
 * Tests for comparison operations in the ExpressionEvaluator.
 */
@DisplayName("Comparison Operations")
class ComparisonOperationTest {
    
    private final ExpressionEvaluator evaluator = new ExpressionEvaluator();
    
    @Test
    @DisplayName("Debug test for expressions")
    void testDebugExpressions() {
        Context context = new Context();
        context.setVariable("x", 10);
        context.setVariable("y", 5);
        
        // Register a print function for debugging
        context.registerFunction("print", args -> {
            System.out.println("DEBUG: " + (args.length > 0 ? args[0] : "null"));
            return args.length > 0 ? args[0] : null;
        });
        
        // Test each expression and print the actual result
        Object result1 = evaluator.evaluate("print($x + $y > $x)", context);
        System.out.println("$x + $y > $x = " + result1);
        
        Object result2 = evaluator.evaluate("print($x - $y < $y)", context);
        System.out.println("$x - $y < $y = " + result2);
        
        Object result3 = evaluator.evaluate("print($x * $y == 50)", context);
        System.out.println("$x * $y == 50 = " + result3);
        
        Object result4 = evaluator.evaluate("print($x * $y)", context);
        System.out.println("$x * $y = " + result4);
        
        Object result5 = evaluator.evaluate("print($x / $y != 2)", context);
        System.out.println("$x / $y != 2 = " + result5);
        
        Object result6 = evaluator.evaluate("print($x / $y)", context);
        System.out.println("$x / $y = " + result6);
    }
    
    @Test
    @DisplayName("Equal and not equal comparisons")
    void testEqualityComparisons() {
        Context context = new Context();
        
        // Number equality
        assertEquals(true, evaluator.evaluate("5 == 5", context));
        assertEquals(false, evaluator.evaluate("5 == 10", context));
        assertEquals(true, evaluator.evaluate("5 != 10", context));
        assertEquals(false, evaluator.evaluate("5 != 5", context));
        
        // String equality
        assertEquals(true, evaluator.evaluate("'hello' == 'hello'", context));
        assertEquals(false, evaluator.evaluate("'hello' == 'world'", context));
        assertEquals(true, evaluator.evaluate("'hello' != 'world'", context));
        assertEquals(false, evaluator.evaluate("'hello' != 'hello'", context));
        
        // Boolean equality
        assertEquals(true, evaluator.evaluate("true == true", context));
        assertEquals(false, evaluator.evaluate("true == false", context));
        assertEquals(true, evaluator.evaluate("true != false", context));
        assertEquals(false, evaluator.evaluate("false != false", context));
        
        // Null equality
        assertEquals(true, evaluator.evaluate("null == null", context));
        assertEquals(false, evaluator.evaluate("'hello' == null", context));
        assertEquals(true, evaluator.evaluate("'hello' != null", context));
        assertEquals(false, evaluator.evaluate("null != null", context));
    }
    
    @Test
    @DisplayName("Numeric comparison operations")
    void testNumericComparisons() {
        Context context = new Context();
        
        // Greater than
        assertEquals(true, evaluator.evaluate("10 > 5", context));
        assertEquals(false, evaluator.evaluate("5 > 10", context));
        assertEquals(false, evaluator.evaluate("5 > 5", context));
        
        // Greater than or equal
        assertEquals(true, evaluator.evaluate("10 >= 5", context));
        assertEquals(false, evaluator.evaluate("5 >= 10", context));
        assertEquals(true, evaluator.evaluate("5 >= 5", context));
        
        // Less than
        assertEquals(false, evaluator.evaluate("10 < 5", context));
        assertEquals(true, evaluator.evaluate("5 < 10", context));
        assertEquals(false, evaluator.evaluate("5 < 5", context));
        
        // Less than or equal
        assertEquals(false, evaluator.evaluate("10 <= 5", context));
        assertEquals(true, evaluator.evaluate("5 <= 10", context));
        assertEquals(true, evaluator.evaluate("5 <= 5", context));
    }
    
    @Test
    @DisplayName("String comparison operations")
    void testStringComparisons() {
        Context context = new Context();
        
        assertEquals(true, evaluator.evaluate("'banana' > 'apple'", context));
        assertEquals(false, evaluator.evaluate("'apple' > 'banana'", context));
        assertEquals(true, evaluator.evaluate("'banana' >= 'banana'", context));
        assertEquals(true, evaluator.evaluate("'apple' < 'banana'", context));
        assertEquals(false, evaluator.evaluate("'banana' < 'apple'", context));
        assertEquals(true, evaluator.evaluate("'banana' <= 'banana'", context));
    }
    
    @Test
    @DisplayName("Mixed type comparisons")
    void testMixedTypeComparisons() {
        Context context = new Context();
        
        // Number and string comparisons
        assertEquals(false, evaluator.evaluate("5 == '5'", context));
        assertEquals(true, evaluator.evaluate("5 != '5'", context));
        
        // Boolean and other type comparisons
        assertEquals(false, evaluator.evaluate("true == 1", context));
        assertEquals(true, evaluator.evaluate("true != 1", context));
        assertEquals(false, evaluator.evaluate("false == 0", context));
        assertEquals(true, evaluator.evaluate("false != 0", context));
        
        // Null and other type comparisons
        assertEquals(false, evaluator.evaluate("null == 0", context));
        assertEquals(true, evaluator.evaluate("null != 0", context));
        assertEquals(false, evaluator.evaluate("null == ''", context));
        assertEquals(true, evaluator.evaluate("null != ''", context));
    }
    
    @Test
    @DisplayName("Comparisons with variables")
    void testComparisonsWithVariables() {
        Context context = new Context();
        context.setVariable("age", 25);
        context.setVariable("name", "Alice");
        context.setVariable("isActive", true);
        context.setVariable("nullValue", null);
        
        // Comparisons with variables
        assertEquals(true, evaluator.evaluate("$age > 18", context));
        assertEquals(false, evaluator.evaluate("$age < 18", context));
        assertEquals(true, evaluator.evaluate("$name == 'Alice'", context));
        assertEquals(false, evaluator.evaluate("$name != 'Alice'", context));
        assertEquals(true, evaluator.evaluate("$isActive == true", context));
        assertEquals(false, evaluator.evaluate("$isActive != true", context));
        assertEquals(true, evaluator.evaluate("$nullValue == null", context));
        assertEquals(false, evaluator.evaluate("$nullValue != null", context));
    }
    
    @Test
    @DisplayName("Comparisons with expressions")
    void testComparisonsWithExpressions() {
        Context context = new Context();
        context.setVariable("x", 10);
        context.setVariable("y", 5);
        
        // Comparisons with expressions
        assertEquals(true, evaluator.evaluate("$x + $y > $x", context));
        assertEquals(false, evaluator.evaluate("$x - $y < $y", context));
        assertEquals(true, evaluator.evaluate("$x * $y == 50", context));
        assertEquals(false, evaluator.evaluate("$x / $y != 2", context));
    }
    
    @Test
    @DisplayName("Equality function for deep comparison")
    void testEqualsFunctionForDeepComparison() {
        Context context = new Context();
        
        // Testing equals function for arrays/lists
        context.setVariable("list1", Arrays.asList(1, 2, 3));
        context.setVariable("list2", Arrays.asList(1, 2, 3));
        context.setVariable("list3", Arrays.asList(1, 2, 4));
        
        assertEquals(true, evaluator.evaluate("equals($list1, $list2)", context));
        assertEquals(false, evaluator.evaluate("equals($list1, $list3)", context));
        
        // Testing equals function for maps
        Map<String, Object> map1 = new HashMap<>();
        map1.put("name", "Alice");
        map1.put("age", 25);
        
        Map<String, Object> map2 = new HashMap<>();
        map2.put("name", "Alice");
        map2.put("age", 25);
        
        Map<String, Object> map3 = new HashMap<>();
        map3.put("name", "Bob");
        map3.put("age", 30);
        
        context.setVariable("map1", map1);
        context.setVariable("map2", map2);
        context.setVariable("map3", map3);
        
        assertEquals(true, evaluator.evaluate("equals($map1, $map2)", context));
        assertEquals(false, evaluator.evaluate("equals($map1, $map3)", context));
    }
} 