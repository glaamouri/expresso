package com.expresso.integration;

import com.expresso.ExpressionEvaluator;
import com.expresso.context.Context;
import com.expresso.exception.ArithmeticExpressionException;
import com.expresso.exception.EvaluationException;
import com.expresso.exception.PropertyNotFoundException;
import com.expresso.exception.SyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for error handling, exceptions, and error messages across
 * the library.
 */
@DisplayName("Error Handling Integration Tests")
class ErrorHandlingIntegrationTest {

    private ExpressionEvaluator evaluator;
    private Context context;

    @BeforeEach
    void setUp() {
        evaluator = new ExpressionEvaluator();
        context = new Context();
    }

    @Nested
    @DisplayName("Syntax Errors")
    class SyntaxErrorTests {

        @Test
        @DisplayName("should throw SyntaxException for invalid syntax")
        void testInvalidSyntax() {
            assertThrows(SyntaxException.class,
                    () -> evaluator.evaluate("invalid syntax here", context));
        }

        @Test
        @DisplayName("should throw SyntaxException for unclosed parenthesis")
        void testUnclosedParenthesis() {
            assertThrows(SyntaxException.class,
                    () -> evaluator.evaluate("upperCase('hello'", context));
        }

        @Test
        @DisplayName("should throw SyntaxException for unclosed string")
        void testUnclosedString() {
            assertThrows(SyntaxException.class,
                    () -> evaluator.evaluate("'unclosed string", context));
        }

        @Test
        @DisplayName("should throw SyntaxException for invalid operators")
        void testInvalidOperators() {
            assertThrows(SyntaxException.class,
                    () -> evaluator.evaluate("5 ++ 3", context));
        }
    }

    @Nested
    @DisplayName("Property Not Found Errors")
    class PropertyNotFoundTests {

        @Test
        @DisplayName("should throw PropertyNotFoundException for undefined variable")
        void testUndefinedVariable() {
            assertThrows(PropertyNotFoundException.class,
                    () -> evaluator.evaluate("$undefinedVar", context));
        }

        @Test
        @DisplayName("should throw PropertyNotFoundException for non-existent property")
        void testNonExistentProperty() {
            Map<String, Object> user = new HashMap<>();
            user.put("name", "John");
            context.setVariable("user", user);

            assertThrows(PropertyNotFoundException.class,
                    () -> evaluator.evaluate("$user.nonExistent", context));
        }

        @Test
        @DisplayName("should throw PropertyNotFoundException for array index out of bounds")
        void testArrayIndexOutOfBounds() {
            context.setVariable("list", Arrays.asList(1, 2, 3));

            assertThrows(PropertyNotFoundException.class,
                    () -> evaluator.evaluate("$list[10]", context));
        }

        @Test
        @DisplayName("should not throw for null-safe access of non-existent property")
        void testNullSafeDoesNotThrow() {
            Map<String, Object> user = new HashMap<>();
            user.put("name", "John");
            context.setVariable("user", user);

            // Should not throw, should return null
            assertNull(evaluator.evaluate("$user?.nonExistent", context));
        }
    }

    @Nested
    @DisplayName("Function Evaluation Errors")
    class FunctionEvaluationTests {

        @Test
        @DisplayName("should throw EvaluationException for wrong argument count")
        void testWrongArgumentCount() {
            assertThrows(EvaluationException.class,
                    () -> evaluator.evaluate("upperCase()", context));
        }

        @Test
        @DisplayName("should throw EvaluationException for wrong argument type")
        void testWrongArgumentType() {
            assertThrows(EvaluationException.class,
                    () -> evaluator.evaluate("upperCase(42)", context));
        }

        @Test
        @DisplayName("should throw EvaluationException for null argument in non-null-safe function")
        void testNullArgument() {
            assertThrows(EvaluationException.class,
                    () -> evaluator.evaluate("upperCase(null)", context));
        }

        @Test
        @DisplayName("should throw EvaluationException for undefined function")
        void testUndefinedFunction() {
            assertThrows(EvaluationException.class,
                    () -> evaluator.evaluate("undefinedFunction('test')", context));
        }

        @Test
        @DisplayName("should throw EvaluationException for invalid conversion")
        void testInvalidConversion() {
            assertThrows(EvaluationException.class,
                    () -> evaluator.evaluate("toNumber('not a number')", context));
        }
    }

    @Nested
    @DisplayName("Type Mismatch Errors")
    class TypeMismatchTests {

        @Test
        @DisplayName("should throw exception for incompatible types in comparison")
        void testIncompatibleComparison() {
            // Comparing incompatible types should throw
            assertThrows(Exception.class,
                    () -> evaluator.evaluate("greaterThan('text', 42)", context));
        }

        @Test
        @DisplayName("should act as string concatenation")
        void testInvalidArithmetic() {
            assertEquals("text42", evaluator.evaluate("'text' + 42", context));
        }

        @Test
        @DisplayName("should throw exception for invalid collection operation")
        void testInvalidCollectionOperation() {
            assertThrows(Exception.class,
                    () -> evaluator.evaluate("sum('not a collection')", context));
        }
    }

    @Nested
    @DisplayName("Error Message Quality")
    class ErrorMessageTests {

        @Test
        @DisplayName("should provide clear error message for syntax errors")
        void testSyntaxErrorMessage() {
            try {
                evaluator.evaluate("5 +", context);
                fail("Should have thrown SyntaxException");
            } catch (SyntaxException e) {
                assertNotNull(e.getMessage());
                assertFalse(e.getMessage().isEmpty());
            }
        }

        @Test
        @DisplayName("should provide clear error message for property not found")
        void testPropertyNotFoundMessage() {
            try {
                evaluator.evaluate("$undefinedVar", context);
                fail("Should have thrown PropertyNotFoundException");
            } catch (PropertyNotFoundException e) {
                assertNotNull(e.getMessage());
                assertTrue(e.getMessage().contains("undefinedVar") ||
                        e.getMessage().contains("not found") ||
                        e.getMessage().contains("undefined"));
            }
        }

        @Test
        @DisplayName("should provide clear error message for function errors")
        void testFunctionErrorMessage() {
            try {
                evaluator.evaluate("upperCase(42)", context);
                fail("Should have thrown EvaluationException");
            } catch (EvaluationException e) {
                assertNotNull(e.getMessage());
                assertFalse(e.getMessage().isEmpty());
            }
        }
    }

    @Nested
    @DisplayName("Error Recovery")
    class ErrorRecoveryTests {

        @Test
        @DisplayName("should recover after error and evaluate new expression")
        void testRecoveryAfterError() {
            // First expression throws error
            assertThrows(Exception.class,
                    () -> evaluator.evaluate("$undefinedVar", context));

            // Should still be able to evaluate new expressions
            context.setVariable("name", "John");
            assertEquals("John", evaluator.evaluate("$name", context));
        }

        @Test
        @DisplayName("should handle errors in nested expressions gracefully")
        void testNestedExpressionError() {
            context.setVariable("value", null);
            assertThrows(Exception.class,
                    () -> evaluator.evaluate("upperCase($value)", context));
        }
    }

    @Nested
    @DisplayName("Complex Error Scenarios")
    class ComplexErrorScenarios {

        @Test
        @DisplayName("should handle errors in deeply nested function calls")
        void testDeeplyNestedError() {
            assertThrows(Exception.class,
                    () -> evaluator.evaluate("upperCase(lowerCase(trim($undefined)))", context));
        }

        @Test
        @DisplayName("should handle errors in complex conditional expressions")
        void testConditionalError() {
            assertThrows(Exception.class,
                    () -> evaluator.evaluate("$undefined ? 'yes' : 'no'", context));
        }

        @Test
        @DisplayName("should handle errors in collection operations")
        void testCollectionOperationError() {
            context.setVariable("notAList", "text");
            assertThrows(Exception.class,
                    () -> evaluator.evaluate("sort($notAList)", context));
        }
    }

    @Nested
    @DisplayName("Edge Case Errors")
    class EdgeCaseErrors {

        @Test
        @DisplayName("should handle division by zero gracefully")
        void testDivisionByZero() {
            assertThrows(ArithmeticExpressionException.class, () -> evaluator.evaluate("10 / 0", context));
        }

        @Test
        @DisplayName("should handle modulo by zero")
        void testModuloByZero() {
            // Modulo by zero should either throw or return NaN
            try {
                Object result = evaluator.evaluate("10 % 0", context);
                assertTrue(result instanceof Double);
            } catch (Exception e) {
                // Exception is also acceptable
                assertTrue(true);
            }
        }

        @Test
        @DisplayName("should handle empty expressions")
        void testEmptyExpression() {
            assertThrows(Exception.class,
                    () -> evaluator.evaluate("", context));
        }

        @Test
        @DisplayName("should handle whitespace-only expressions")
        void testWhitespaceOnlyExpression() {
            assertThrows(Exception.class,
                    () -> evaluator.evaluate("   ", context));
        }
    }

    @Nested
    @DisplayName("Custom Function Errors")
    class CustomFunctionErrors {

        @Test
        @DisplayName("should handle errors in custom functions")
        void testCustomFunctionError() {
            evaluator.registerFunction("errorFunc", args -> {
                throw new RuntimeException("Custom error");
            });

            assertThrows(Exception.class,
                    () -> evaluator.evaluate("errorFunc('test')", context));
        }

        @Test
        @DisplayName("should handle null return from custom function")
        void testCustomFunctionNullReturn() {
            evaluator.registerFunction("nullFunc", args -> null);

            assertNull(evaluator.evaluate("nullFunc('test')", context));
        }
    }
}
