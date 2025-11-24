package com.expresso.context.functions;

import com.expresso.ExpressionEvaluator;
import com.expresso.context.Context;
import com.expresso.exception.EvaluationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for Utility functions covering type conversion and
 * introspection.
 */
@DisplayName("Utility Functions - Comprehensive Tests")
class UtilityFunctionsComprehensiveTest {

    private ExpressionEvaluator evaluator;
    private Context context;

    @BeforeEach
    void setUp() {
        evaluator = new ExpressionEvaluator();
        context = new Context();
    }

    @Nested
    @DisplayName("typeof Function")
    class TypeofTests {

        @Test
        @DisplayName("should return 'null' for null")
        void testNull() {
            context.setVariable("nullVar", null);
            assertEquals("null", evaluator.evaluate("typeof($nullVar)", context));
        }

        @Test
        @DisplayName("should return 'string' for strings")
        void testString() {
            assertEquals("string", evaluator.evaluate("typeof('hello')", context));
            assertEquals("string", evaluator.evaluate("typeof('')", context));
        }

        @Test
        @DisplayName("should return 'number' for numbers")
        void testNumber() {
            assertEquals("number", evaluator.evaluate("typeof(42)", context));
            assertEquals("number", evaluator.evaluate("typeof(3.14)", context));
            assertEquals("number", evaluator.evaluate("typeof(-5)", context));
        }

        @Test
        @DisplayName("should return 'boolean' for booleans")
        void testBoolean() {
            assertEquals("boolean", evaluator.evaluate("typeof(true)", context));
            assertEquals("boolean", evaluator.evaluate("typeof(false)", context));
        }

        @Test
        @DisplayName("should return 'list' for lists and arrays")
        void testList() {
            context.setVariable("list", Arrays.asList(1, 2, 3));
            context.setVariable("arr", new int[] { 1, 2, 3 });

            assertEquals("list", evaluator.evaluate("typeof($list)", context));
            assertEquals("list", evaluator.evaluate("typeof($arr)", context));
        }

        @Test
        @DisplayName("should return 'map' for maps")
        void testMap() {
            Map<String, Object> map = new HashMap<>();
            context.setVariable("map", map);

            assertEquals("map", evaluator.evaluate("typeof($map)", context));
        }

        @Test
        @DisplayName("should return 'date' for dates")
        void testDate() {
            LocalDate date = LocalDate.now();
            context.setVariable("date", date);

            assertEquals("date", evaluator.evaluate("typeof($date)", context));
        }
    }

    @Nested
    @DisplayName("toString Function")
    class ToStringTests {

        @Test
        @DisplayName("should convert number to string")
        void testNumberToString() {
            assertEquals("42", evaluator.evaluate("toString(42)", context));
            assertEquals("3.14", evaluator.evaluate("toString(3.14)", context));
        }

        @Test
        @DisplayName("should handle string input")
        void testStringInput() {
            assertEquals("hello", evaluator.evaluate("toString('hello')", context));
        }

        @Test
        @DisplayName("should convert boolean to string")
        void testBooleanToString() {
            assertEquals("true", evaluator.evaluate("toString(true)", context));
            assertEquals("false", evaluator.evaluate("toString(false)", context));
        }

        @Test
        @DisplayName("should convert null to 'null'")
        void testNullToString() {
            context.setVariable("nullVar", null);
            assertEquals("null", evaluator.evaluate("toString($nullVar)", context));
        }

        @Test
        @DisplayName("should convert list to string")
        void testListToString() {
            context.setVariable("list", Arrays.asList(1, 2, 3));
            String result = (String) evaluator.evaluate("toString($list)", context);
            assertNotNull(result);
            assertTrue(result.contains("1"));
        }

        @Test
        @DisplayName("should handle negative numbers")
        void testNegativeNumber() {
            assertEquals("-42.0", evaluator.evaluate("toString(-42)", context));
        }
    }

    @Nested
    @DisplayName("toNumber Function")
    class ToNumberTests {

        @Test
        @DisplayName("should convert string to number")
        void testStringToNumber() {
            assertEquals(42.0, (Double) evaluator.evaluate("toNumber('42')", context), 0.001);
            assertEquals(3.14, (Double) evaluator.evaluate("toNumber('3.14')", context), 0.001);
        }

        @Test
        @DisplayName("should handle negative numbers")
        void testNegativeNumbers() {
            assertEquals(-42.0, (Double) evaluator.evaluate("toNumber('-42')", context), 0.001);
            assertEquals(-3.14, (Double) evaluator.evaluate("toNumber('-3.14')", context), 0.001);
        }

        @Test
        @DisplayName("should handle number input")
        void testNumberInput() {
            assertEquals(42.0, (Double) evaluator.evaluate("toNumber(42)", context), 0.001);
        }

        @Test
        @DisplayName("should convert boolean to number")
        void testBooleanToNumber() {
            assertEquals(1.0, (Double) evaluator.evaluate("toNumber(true)", context), 0.001);
            assertEquals(0.0, (Double) evaluator.evaluate("toNumber(false)", context), 0.001);
        }

        @Test
        @DisplayName("should convert null to 0")
        void testNullToNumber() {
            context.setVariable("nullVar", null);
            assertEquals(0.0, (Double) evaluator.evaluate("toNumber($nullVar)", context), 0.001);
        }

        @Test
        @DisplayName("should throw exception for invalid string")
        void testInvalidString() {
            assertThrows(EvaluationException.class,
                    () -> evaluator.evaluate("toNumber('invalid')", context));
        }

        @Test
        @DisplayName("should handle decimal strings")
        void testDecimalStrings() {
            assertEquals(3.14159, (Double) evaluator.evaluate("toNumber('3.14159')", context), 0.001);
        }

        @Test
        @DisplayName("should handle scientific notation")
        void testScientificNotation() {
            assertEquals(1000.0, (Double) evaluator.evaluate("toNumber('1e3')", context), 0.001);
        }
    }

    @Nested
    @DisplayName("toBoolean Function")
    class ToBooleanTests {

        @Test
        @DisplayName("should convert boolean input")
        void testBooleanInput() {
            assertEquals(true, evaluator.evaluate("toBoolean(true)", context));
            assertEquals(false, evaluator.evaluate("toBoolean(false)", context));
        }

        @Test
        @DisplayName("should convert string 'true' to true")
        void testStringTrue() {
            assertEquals(true, evaluator.evaluate("toBoolean('true')", context));
            assertEquals(true, evaluator.evaluate("toBoolean('True')", context));
            assertEquals(true, evaluator.evaluate("toBoolean('TRUE')", context));
        }

        @Test
        @DisplayName("should convert string 'false' to false")
        void testStringFalse() {
            assertEquals(false, evaluator.evaluate("toBoolean('false')", context));
            assertEquals(false, evaluator.evaluate("toBoolean('False')", context));
            assertEquals(false, evaluator.evaluate("toBoolean('FALSE')", context));
        }

        @Test
        @DisplayName("should convert 'yes' and 'no'")
        void testYesNo() {
            assertEquals(true, evaluator.evaluate("toBoolean('yes')", context));
            assertEquals(true, evaluator.evaluate("toBoolean('Yes')", context));
            assertEquals(false, evaluator.evaluate("toBoolean('no')", context));
            assertEquals(false, evaluator.evaluate("toBoolean('No')", context));
        }

        @Test
        @DisplayName("should convert '1' and '0'")
        void testOneZero() {
            assertEquals(true, evaluator.evaluate("toBoolean('1')", context));
            assertEquals(false, evaluator.evaluate("toBoolean('0')", context));
        }

        @Test
        @DisplayName("should convert numbers to boolean")
        void testNumberToBoolean() {
            assertEquals(true, evaluator.evaluate("toBoolean(1)", context));
            assertEquals(true, evaluator.evaluate("toBoolean(42)", context));
            assertEquals(true, evaluator.evaluate("toBoolean(-1)", context));
            assertEquals(false, evaluator.evaluate("toBoolean(0)", context));
        }

        @Test
        @DisplayName("should convert null to false")
        void testNullToBoolean() {
            context.setVariable("nullVar", null);
            assertEquals(false, evaluator.evaluate("toBoolean($nullVar)", context));
        }

        @Test
        @DisplayName("should convert non-empty string to true")
        void testNonEmptyString() {
            assertEquals(true, evaluator.evaluate("toBoolean('hello')", context));
        }

        @Test
        @DisplayName("should convert empty string to false")
        void testEmptyString() {
            assertEquals(false, evaluator.evaluate("toBoolean('')", context));
        }
    }

    @Nested
    @DisplayName("Complex Utility Operations")
    class ComplexUtilityTests {

        @Test
        @DisplayName("should use typeof in conditionals")
        void testTypeofInConditional() {
            context.setVariable("value", 42);
            assertEquals("number",
                    evaluator.evaluate("typeof($value) == 'number' ? 'number' : 'other'", context));
        }

        @Test
        @DisplayName("should chain conversions")
        void testChainedConversions() {
            assertEquals("123.0",
                    evaluator.evaluate("toString(toNumber('123'))", context));
        }

        @Test
        @DisplayName("should convert and compare")
        void testConvertAndCompare() {
            assertEquals(true,
                    evaluator.evaluate("toNumber('42') == 42", context));
        }

        @Test
        @DisplayName("should use toBoolean in logic")
        void testToBooleanInLogic() {
            assertEquals(true,
                    evaluator.evaluate("toBoolean('yes') && toBoolean(1)", context));
            assertEquals(false,
                    evaluator.evaluate("toBoolean('no') || toBoolean(0)", context));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCasesTests {

        @Test
        @DisplayName("should handle toString with very large numbers")
        void testLargeNumberToString() {
            String result = (String) evaluator.evaluate("toString(99999999)", context);
            assertTrue(result.contains("99999999"));
        }

        @Test
        @DisplayName("should handle toNumber with leading/trailing spaces")
        void testNumberWithSpaces() {
            assertEquals(42.0, (Double) evaluator.evaluate("toNumber('  42  ')", context), 0.001);
        }

        @Test
        @DisplayName("should handle multiple type conversions")
        void testMultipleConversions() {
            // Convert number to string, then back to number
            Object result = evaluator.evaluate("toNumber(toString(42))", context);
            assertEquals(42.0, result);
        }

        @Test
        @DisplayName("should handle toBoolean with decimal string")
        void testBooleanWithDecimal() {
            assertEquals(true, evaluator.evaluate("toBoolean('3.14')", context));
        }

        @Test
        @DisplayName("should distinguish between 0 and null")
        void testZeroVsNull() {
            assertEquals("number", evaluator.evaluate("typeof(0)", context));
            context.setVariable("nullVar", null);
            assertEquals("null", evaluator.evaluate("typeof($nullVar)", context));
        }

        @Test
        @DisplayName("should distinguish between empty string and null")
        void testEmptyStringVsNull() {
            assertEquals("string", evaluator.evaluate("typeof('')", context));
            context.setVariable("nullVar", null);
            assertEquals("null", evaluator.evaluate("typeof($nullVar)", context));
        }

        @Test
        @DisplayName("should handle typeof with custom objects")
        void testCustomObjects() {
            context.setVariable("user", new Object());
            String result = (String) evaluator.evaluate("typeof($user)", context);
            assertNotNull(result);
        }
    }
}
