package com.expresso.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.expresso.ExpressionEvaluator;
import com.expresso.context.Context;

/**
 * Comprehensive test suite for all exception types in the Expresso library.
 * Tests all exception scenarios to ensure proper exception handling.
 */
@DisplayName("Exception Test Suite")
public class ExceptionTestSuite {

    private final ExpressionEvaluator evaluator = new ExpressionEvaluator();
    
    @Test
    @DisplayName("Syntax exceptions")
    void testSyntaxExceptions() {
        Context context = new Context();
        
        // Missing closing parenthesis
        assertThrows(SyntaxException.class, () -> 
                evaluator.evaluate("sum(1, 2", context));
        
        // Unbalanced brackets
        assertThrows(SyntaxException.class, () -> 
                evaluator.evaluate("$arr[0", context));
        
        // Invalid operator sequence
        assertThrows(SyntaxException.class, () -> 
                evaluator.evaluate("5 + * 3", context));
        
        // Unexpected end of expression
        assertThrows(SyntaxException.class, () -> 
                evaluator.evaluate("5 +", context));
    }
    
    @Test
    @DisplayName("Property not found exceptions")
    void testPropertyNotFoundExceptions() {
        Context context = new Context();
        
        // Variable not found
        Exception ex = assertThrows(VariableNotFoundException.class, () -> 
                evaluator.evaluate("$nonExistentVar", context));
        assertTrue(ex.getMessage().contains("nonExistentVar"));
        
        // Property not found on object - using a custom class to ensure PropertyAccessException
        class TestObject {}
        context.setVariable("obj", new TestObject());
        ex = assertThrows(PropertyAccessException.class, () -> 
                evaluator.evaluate("$obj.nonExistentProperty", context));
        assertTrue(ex.getMessage().contains("nonExistentProperty"));
        
        // Array index out of bounds
        context.setVariable("list", Arrays.asList(1, 2, 3));
        ex = assertThrows(ArrayIndexOutOfBoundsException.class, () -> 
                evaluator.evaluate("$list[10]", context));
        assertTrue(ex.getMessage().contains("Array index out of bounds"));
        
        // Nested property not found
        Map<String, Object> person = new HashMap<>();
        person.put("name", "John");
        context.setVariable("person", person);
        ex = assertThrows(PropertyNotFoundException.class, () -> 
                evaluator.evaluate("$person.address.city", context));
        assertTrue(ex.getMessage().contains("Cannot access property on null value"));
    }
    
    @Test
    @DisplayName("Evaluation exceptions")
    void testEvaluationExceptions() {
        Context context = new Context();
        
        // Unknown function
        Exception ex = assertThrows(UnknownFunctionException.class, () -> 
                evaluator.evaluate("nonExistentFunction()", context));
        assertTrue(ex.getMessage().contains("Function not found"));
        assertEquals("nonExistentFunction", ((UnknownFunctionException)ex).getFunctionName());
        
        // Missing function argument
        context.registerFunction("requiresTwoArgs", args -> {
            if (args.length < 2) {
                throw new MissingArgumentException("requiresTwoArgs", 2, args.length);
            }
            return args[0];
        });
        ex = assertThrows(FunctionExecutionException.class, () -> 
                evaluator.evaluate("requiresTwoArgs(1)", context));
        assertTrue(ex.getMessage().contains("Expected 2 arguments, but got 1"));
        
        // Type conversion error
        context.registerFunction("requiresNumber", args -> {
            if (!(args[0] instanceof Number)) {
                throw new TypeConversionException(args[0], Number.class, "Expected a number");
            }
            return args[0];
        });
        ex = assertThrows(FunctionExecutionException.class, () -> 
                evaluator.evaluate("requiresNumber('string')", context));
        assertTrue(ex.getMessage().contains("Expected a number"));
    }
    
    @Test
    @DisplayName("Arithmetic exceptions")
    void testArithmeticExceptions() {
        Context context = new Context();
        
        // Division by zero
        Exception ex = assertThrows(ArithmeticExpressionException.class, () -> 
                evaluator.evaluate("5 / 0", context));
        assertTrue(ex.getMessage().contains("Division by zero"));
        assertEquals("/", ((ArithmeticExpressionException)ex).getOperation());
        
        // Modulo by zero
        ex = assertThrows(ArithmeticExpressionException.class, () -> 
                evaluator.evaluate("5 % 0", context));
        assertTrue(ex.getMessage().contains("Modulo by zero"));
        assertEquals("%", ((ArithmeticExpressionException)ex).getOperation());
    }
    
    @Test
    @DisplayName("Invalid operation exceptions")
    void testInvalidOperationExceptions() {
        Context context = new Context();
        
        // Cannot add incompatible types
        Exception ex = assertThrows(InvalidOperationException.class, () -> 
                evaluator.evaluate("true + 5", context));
        assertTrue(ex.getMessage().contains("Cannot add these types"));
        
        // Cannot subtract strings
        ex = assertThrows(InvalidOperationException.class, () -> 
                evaluator.evaluate("'hello' - 'world'", context));
        assertTrue(ex.getMessage().contains("Cannot subtract"));
        
        // Cannot multiply strings
        ex = assertThrows(InvalidOperationException.class, () -> 
                evaluator.evaluate("'hello' * 'world'", context));
        assertTrue(ex.getMessage().contains("Cannot multiply"));
        
        // Cannot divide strings
        ex = assertThrows(InvalidOperationException.class, () -> 
                evaluator.evaluate("'hello' / 'world'", context));
        assertTrue(ex.getMessage().contains("Cannot divide"));
        
        // Cannot compare incompatible types
        ex = assertThrows(InvalidOperationException.class, () -> 
                evaluator.evaluate("'hello' > true", context));
        assertTrue(ex.getMessage().contains("Cannot compare these types"));
    }
    
    @Test
    @DisplayName("Null-safe operator handling")
    void testNullSafeOperators() {
        Context context = new Context();
        
        // Null-safe property access
        Object result = evaluator.evaluate("$nonExistentVar?.property", context);
        assertNull(result);
        
        // Null-safe array access
        result = evaluator.evaluate("$nonExistentVar?[0]", context);
        assertNull(result);
        
        Map<String, Object> user = new HashMap<>();
        context.setVariable("user", user);
        
        // Nested null-safe property access
        result = evaluator.evaluate("$user?.address?.city", context);
        assertNull(result);
        
        // Null coalescing operator
        result = evaluator.evaluate("$user?.address?.city ?? 'Unknown'", context);
        assertEquals("Unknown", result);
    }
    
    @Test
    @DisplayName("isNull and coalesce special functions")
    void testSpecialFunctions() {
        Context context = new Context();
        
        // isNull doesn't throw for non-existent variables
        Boolean result = (Boolean) evaluator.evaluate("isNull($nonExistentVar)", context);
        assertTrue(result);
        
        // isNull with null property
        Map<String, Object> user = new HashMap<>();
        context.setVariable("user", user);
        result = (Boolean) evaluator.evaluate("isNull($user.address)", context);
        assertTrue(result);
        
        // coalesce skips exceptions
        Object coalesceResult = evaluator.evaluate("coalesce($nonExistentVar, $alsoNonExistent, 'Default')", context);
        assertEquals("Default", coalesceResult);
        
        // coalesce returns first non-null value
        context.setVariable("first", null);
        context.setVariable("second", "Value");
        coalesceResult = evaluator.evaluate("coalesce($first, $second, 'Default')", context);
        assertEquals("Value", coalesceResult);
    }
    
    @Test
    @DisplayName("Exception hierarchy and catching")
    void testExceptionHierarchy() {
        Context context = new Context();
        
        try {
            evaluator.evaluate("5 / 0", context);
            fail("Should have thrown an exception");
        } catch (ArithmeticExpressionException e) {
            // Expected - most specific
            assertEquals("/", e.getOperation());
            // Use equals() comparison instead of exact type comparison since values could be Integer or Double
            assertEquals(5, ((Number)e.getLeftOperand()).intValue());
            assertEquals(0, ((Number)e.getRightOperand()).intValue());
        } catch (InvalidOperationException e) {
            fail("Should have caught more specific ArithmeticExpressionException");
        } catch (EvaluationException e) {
            fail("Should have caught more specific InvalidOperationException");
        }
        
        try {
            evaluator.evaluate("$nonExistentVar", context);
            fail("Should have thrown an exception");
        } catch (VariableNotFoundException e) {
            // Expected - most specific
            assertEquals("nonExistentVar", e.getVariableName());
        } catch (PropertyNotFoundException e) {
            fail("Should have caught more specific VariableNotFoundException");
        } catch (ExpressionException e) {
            fail("Should have caught more specific PropertyNotFoundException");
        }
    }
} 