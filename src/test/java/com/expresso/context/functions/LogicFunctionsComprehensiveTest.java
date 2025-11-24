package com.expresso.context.functions;

import com.expresso.ExpressionEvaluator;
import com.expresso.context.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for Logic functions covering null handling, type
 * checking, and conditionals.
 */
@DisplayName("Logic Functions - Comprehensive Tests")
class LogicFunctionsComprehensiveTest {

    private ExpressionEvaluator evaluator;
    private Context context;

    @BeforeEach
    void setUp() {
        evaluator = new ExpressionEvaluator();
        context = new Context();
    }

    @Nested
    @DisplayName("isNull Function")
    class IsNullTests {

        @Test
        @DisplayName("should return true for null")
        void testNull() {
            assertEquals(true, evaluator.evaluate("isNull(null)", context));
        }

        @Test
        @DisplayName("should return false for non-null values")
        void testNonNull() {
            assertEquals(false, evaluator.evaluate("isNull(0)", context));
            assertEquals(false, evaluator.evaluate("isNull('')", context));
            assertEquals(false, evaluator.evaluate("isNull(false)", context));
            assertEquals(false, evaluator.evaluate("isNull('hello')", context));
        }

        @Test
        @DisplayName("should return false for empty string")
        void testEmptyString() {
            assertEquals(false, evaluator.evaluate("isNull('')", context));
        }

        @Test
        @DisplayName("should work with variables")
        void testWithVariables() {
            context.setVariable("nullVar", null);
            context.setVariable("notNull", "value");

            assertEquals(true, evaluator.evaluate("isNull($nullVar)", context));
            assertEquals(false, evaluator.evaluate("isNull($notNull)", context));
        }
    }

    @Nested
    @DisplayName("coalesce Function")
    class CoalesceTests {

        @Test
        @DisplayName("should return first non-null value")
        void testFirstNonNull() {
            assertEquals("first", evaluator.evaluate("coalesce('first', 'second')", context));
        }

        @Test
        @DisplayName("should skip null values")
        void testSkipNulls() {
            assertEquals("second", evaluator.evaluate("coalesce(null, 'second', 'third')", context));
            assertEquals("third", evaluator.evaluate("coalesce(null, null, 'third')", context));
        }

        @Test
        @DisplayName("should return null if all values are null")
        void testAllNull() {
            assertNull(evaluator.evaluate("coalesce(null, null, null)", context));
        }

        @Test
        @DisplayName("should handle single argument")
        void testSingleArgument() {
            assertEquals("value", evaluator.evaluate("coalesce('value')", context));
            assertNull(evaluator.evaluate("coalesce(null)", context));
        }

        @Test
        @DisplayName("should handle different types")
        void testDifferentTypes() {
            assertEquals(0L, evaluator.evaluate("coalesce(null, 0, 'text')", context));
            assertEquals("text", evaluator.evaluate("coalesce(null, null, 'text')", context));
        }

        @Test
        @DisplayName("should accept false and 0 as valid values")
        void testFalsyValues() {
            assertEquals(false, evaluator.evaluate("coalesce(null, false, true)", context));
            assertEquals(0L, evaluator.evaluate("coalesce(null, 0, 1)", context));
            assertEquals("", evaluator.evaluate("coalesce(null, '', 'default')", context));
        }
    }

    @Nested
    @DisplayName("isEmpty Function")
    class IsEmptyTests {

        @Test
        @DisplayName("should return true for null")
        void testNull() {
            context.setVariable("nullVar", null);
            assertEquals(true, evaluator.evaluate("isEmpty($nullVar)", context));
        }

        @Test
        @DisplayName("should return true for empty string")
        void testEmptyString() {
            assertEquals(true, evaluator.evaluate("isEmpty('')", context));
        }

        @Test
        @DisplayName("should return false for non-empty string")
        void testNonEmptyString() {
            assertEquals(false, evaluator.evaluate("isEmpty('hello')", context));
        }

        @Test
        @DisplayName("should return true for empty list")
        void testEmptyList() {
            context.setVariable("list", Collections.emptyList());
            assertEquals(true, evaluator.evaluate("isEmpty($list)", context));
        }

        @Test
        @DisplayName("should return false for non-empty list")
        void testNonEmptyList() {
            context.setVariable("list", Arrays.asList(1, 2, 3));
            assertEquals(false, evaluator.evaluate("isEmpty($list)", context));
        }

        @Test
        @DisplayName("should return true for empty map")
        void testEmptyMap() {
            context.setVariable("map", new HashMap<>());
            assertEquals(true, evaluator.evaluate("isEmpty($map)", context));
        }

        @Test
        @DisplayName("should return false for non-empty map")
        void testNonEmptyMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("key", "value");
            context.setVariable("map", map);
            assertEquals(false, evaluator.evaluate("isEmpty($map)", context));
        }

        @Test
        @DisplayName("should return true for empty array")
        void testEmptyArray() {
            context.setVariable("arr", new int[0]);
            assertEquals(true, evaluator.evaluate("isEmpty($arr)", context));
        }

        @Test
        @DisplayName("should return false for non-empty array")
        void testNonEmptyArray() {
            context.setVariable("arr", new int[] { 1, 2, 3 });
            assertEquals(false, evaluator.evaluate("isEmpty($arr)", context));
        }
    }

    @Nested
    @DisplayName("Type Checking Functions")
    class TypeCheckingTests {

        @Test
        @DisplayName("isNumber should correctly identify numbers")
        void testIsNumber() {
            assertEquals(true, evaluator.evaluate("isNumber(42)", context));
            assertEquals(true, evaluator.evaluate("isNumber(3.14)", context));
            assertEquals(true, evaluator.evaluate("isNumber(-5)", context));
            assertEquals(false, evaluator.evaluate("isNumber('42')", context));
            assertEquals(false, evaluator.evaluate("isNumber(true)", context));
            assertEquals(false, evaluator.evaluate("isNumber(null)", context));
        }

        @Test
        @DisplayName("isString should correctly identify strings")
        void testIsString() {
            assertEquals(true, evaluator.evaluate("isString('hello')", context));
            assertEquals(true, evaluator.evaluate("isString('')", context));
            assertEquals(false, evaluator.evaluate("isString(42)", context));
            assertEquals(false, evaluator.evaluate("isString(true)", context));
            assertEquals(false, evaluator.evaluate("isString(null)", context));
        }

        @Test
        @DisplayName("isBoolean should correctly identify booleans")
        void testIsBoolean() {
            assertEquals(true, evaluator.evaluate("isBoolean(true)", context));
            assertEquals(true, evaluator.evaluate("isBoolean(false)", context));
            assertEquals(false, evaluator.evaluate("isBoolean(1)", context));
            assertEquals(false, evaluator.evaluate("isBoolean('true')", context));
            assertEquals(false, evaluator.evaluate("isBoolean(null)", context));
        }

        @Test
        @DisplayName("isList should correctly identify lists and arrays")
        void testIsList() {
            context.setVariable("list", Arrays.asList(1, 2, 3));
            context.setVariable("arr", new int[] { 1, 2, 3 });

            assertEquals(true, evaluator.evaluate("isList($list)", context));
            assertEquals(true, evaluator.evaluate("isList($arr)", context));
            assertEquals(false, evaluator.evaluate("isList('hello')", context));
            assertEquals(false, evaluator.evaluate("isList(42)", context));
            assertEquals(false, evaluator.evaluate("isList(null)", context));
        }

        @Test
        @DisplayName("isMap should correctly identify maps")
        void testIsMap() {
            Map<String, Object> map = new HashMap<>();
            context.setVariable("map", map);

            assertEquals(true, evaluator.evaluate("isMap($map)", context));
            assertEquals(false, evaluator.evaluate("isMap('hello')", context));
            assertEquals(false, evaluator.evaluate("isMap(42)", context));
            assertEquals(false, evaluator.evaluate("isMap(null)", context));
        }
    }

    @Nested
    @DisplayName("equals Function")
    class EqualsTests {

        @Test
        @DisplayName("should compare equal values")
        void testEqualValues() {
            assertEquals(true, evaluator.evaluate("equals(5, 5)", context));
            assertEquals(true, evaluator.evaluate("equals('hello', 'hello')", context));
            assertEquals(true, evaluator.evaluate("equals(true, true)", context));
        }

        @Test
        @DisplayName("should compare unequal values")
        void testUnequalValues() {
            assertEquals(false, evaluator.evaluate("equals(5, 10)", context));
            assertEquals(false, evaluator.evaluate("equals('hello', 'world')", context));
            assertEquals(false, evaluator.evaluate("equals(true, false)", context));
        }

        @Test
        @DisplayName("should handle null comparisons")
        void testNullComparisons() {
            assertEquals(true, evaluator.evaluate("equals(null, null)", context));
            assertEquals(false, evaluator.evaluate("equals(null, 5)", context));
            assertEquals(false, evaluator.evaluate("equals('hello', null)", context));
        }

        @Test
        @DisplayName("should be type-sensitive")
        void testTypeSensitive() {
            assertEquals(false, evaluator.evaluate("equals(5, '5')", context));
            assertEquals(false, evaluator.evaluate("equals(1, true)", context));
        }

        @Test
        @DisplayName("should compare lists")
        void testListComparison() {
            context.setVariable("list1", Arrays.asList(1, 2, 3));
            context.setVariable("list2", Arrays.asList(1, 2, 3));
            context.setVariable("list3", Arrays.asList(1, 2, 4));

            assertEquals(true, evaluator.evaluate("equals($list1, $list2)", context));
            assertEquals(false, evaluator.evaluate("equals($list1, $list3)", context));
        }
    }

    @Nested
    @DisplayName("ifThen Function")
    class IfThenTests {

        @Test
        @DisplayName("should return then value when condition is true")
        void testTrueCondition() {
            assertEquals("yes", evaluator.evaluate("ifThen(true, 'yes', 'no')", context));
            assertEquals("yes", evaluator.evaluate("ifThen(5 > 3, 'yes', 'no')", context));
        }

        @Test
        @DisplayName("should return else value when condition is false")
        void testFalseCondition() {
            assertEquals("no", evaluator.evaluate("ifThen(false, 'yes', 'no')", context));
            assertEquals("no", evaluator.evaluate("ifThen(5 < 3, 'yes', 'no')", context));
        }

        @Test
        @DisplayName("should handle null condition as false")
        void testNullCondition() {
            context.setVariable("nullVar", null);
            assertEquals("no", evaluator.evaluate("ifThen($nullVar, 'yes', 'no')", context));
        }

        @Test
        @DisplayName("should handle non-boolean conditions")
        void testNonBooleanCondition() {
            // Non-null, non-boolean values are considered true
            assertEquals("yes", evaluator.evaluate("ifThen('text', 'yes', 'no')", context));
            assertEquals("yes", evaluator.evaluate("ifThen(42, 'yes', 'no')", context));
        }

        @Test
        @DisplayName("should work with complex expressions")
        void testComplexExpressions() {
            context.setVariable("age", 25);
            assertEquals("adult",
                    evaluator.evaluate("ifThen($age >= 18, 'adult', 'minor')", context));
        }

        @Test
        @DisplayName("should handle different return types")
        void testDifferentReturnTypes() {
            assertEquals(100L, evaluator.evaluate("ifThen(true, 100, 200)", context));
            assertEquals(false, evaluator.evaluate("ifThen(false, true, false)", context));
        }

        @Test
        @DisplayName("should work with nested ifThen calls")
        void testNestedIfThen() {
            context.setVariable("score", 85);
            assertEquals("B", evaluator.evaluate(
                    "ifThen($score >= 90, 'A', ifThen($score >= 80, 'B', 'C'))", context));
        }
    }

    @Nested
    @DisplayName("Complex Logic Operations")
    class ComplexLogicTests {

        @Test
        @DisplayName("should combine multiple logic functions")
        void testCombinedFunctions() {
            context.setVariable("value", "hello");
            assertEquals(true,
                    evaluator.evaluate("!isNull($value) && isString($value)", context));
        }

        @Test
        @DisplayName("should use coalesce with conditionals")
        void testCoalesceWithConditional() {
            context.setVariable("name", null);
            assertEquals("Guest",
                    evaluator.evaluate("coalesce($name, 'Guest')", context));
        }

        @Test
        @DisplayName("should chain type checks")
        void testChainedTypeChecks() {
            context.setVariable("value", 42);
            assertEquals(true,
                    evaluator.evaluate("isNumber($value) && !isString($value)", context));
        }

        @Test
        @DisplayName("should use isEmpty in conditionals")
        void testIsEmptyInConditional() {
            context.setVariable("list", Collections.emptyList());
            assertEquals("empty",
                    evaluator.evaluate("isEmpty($list) ? 'empty' : 'not empty'", context));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCasesTests {

        @Test
        @DisplayName("should handle empty string vs null")
        void testEmptyStringVsNull() {
            assertEquals(false, evaluator.evaluate("isNull('')", context));
            assertEquals(true, evaluator.evaluate("isEmpty('')", context));
        }

        @Test
        @DisplayName("should handle zero vs null")
        void testZeroVsNull() {
            assertEquals(false, evaluator.evaluate("isNull(0)", context));
            assertEquals(true, evaluator.evaluate("isNumber(0)", context));
        }

        @Test
        @DisplayName("should handle false vs null")
        void testFalseVsNull() {
            assertEquals(false, evaluator.evaluate("isNull(false)", context));
            assertEquals(true, evaluator.evaluate("isBoolean(false)", context));
        }

        @Test
        @DisplayName("should handle coalesce with all falsy values")
        void testCoalesceFalsyValues() {
            // 0 and false are valid non-null values
            assertEquals(0L, evaluator.evaluate("coalesce(null, 0)", context));
            assertEquals(false, evaluator.evaluate("coalesce(null, false)", context));
            assertEquals("", evaluator.evaluate("coalesce(null, '')", context));
        }
    }
}
