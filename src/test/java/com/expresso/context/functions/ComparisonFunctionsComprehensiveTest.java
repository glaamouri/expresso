package com.expresso.context.functions;

import com.expresso.ExpressionEvaluator;
import com.expresso.context.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for Comparison functions covering all types and null
 * handling.
 */
@DisplayName("Comparison Functions - Comprehensive Tests")
class ComparisonFunctionsComprehensiveTest {

    private ExpressionEvaluator evaluator;
    private Context context;

    @BeforeEach
    void setUp() {
        evaluator = new ExpressionEvaluator();
        context = new Context();
    }

    @Nested
    @DisplayName("greaterThan Function")
    class GreaterThanTests {

        @Test
        @DisplayName("should compare numbers")
        void testNumbers() {
            assertEquals(true, evaluator.evaluate("greaterThan(10, 5)", context));
            assertEquals(false, evaluator.evaluate("greaterThan(5, 10)", context));
            assertEquals(false, evaluator.evaluate("greaterThan(5, 5)", context));
        }

        @Test
        @DisplayName("should compare negative numbers")
        void testNegativeNumbers() {
            assertEquals(true, evaluator.evaluate("greaterThan(-5, -10)", context));
            assertEquals(false, evaluator.evaluate("greaterThan(-10, -5)", context));
        }

        @Test
        @DisplayName("should compare decimals")
        void testDecimals() {
            assertEquals(true, evaluator.evaluate("greaterThan(3.14, 2.71)", context));
            assertEquals(false, evaluator.evaluate("greaterThan(2.71, 3.14)", context));
        }

        @Test
        @DisplayName("should compare strings lexicographically")
        void testStrings() {
            assertEquals(true, evaluator.evaluate("greaterThan('b', 'a')", context));
            assertEquals(false, evaluator.evaluate("greaterThan('a', 'b')", context));
            assertEquals(false, evaluator.evaluate("greaterThan('a', 'a')", context));
        }

        @Test
        @DisplayName("should compare dates")
        void testDates() {
            LocalDate date1 = LocalDate.of(2023, 12, 31);
            LocalDate date2 = LocalDate.of(2023, 1, 1);
            context.setVariable("date1", date1);
            context.setVariable("date2", date2);

            assertEquals(true, evaluator.evaluate("greaterThan($date1, $date2)", context));
            assertEquals(false, evaluator.evaluate("greaterThan($date2, $date1)", context));
        }

        @Test
        @DisplayName("should return false for null values")
        void testNullValues() {
            context.setVariable("nullVar", null);
            assertEquals(false, evaluator.evaluate("greaterThan($nullVar, 5)", context));
            assertEquals(false, evaluator.evaluate("greaterThan(5, $nullVar)", context));
        }
    }

    @Nested
    @DisplayName("lessThan Function")
    class LessThanTests {

        @Test
        @DisplayName("should compare numbers")
        void testNumbers() {
            assertEquals(true, evaluator.evaluate("lessThan(5, 10)", context));
            assertEquals(false, evaluator.evaluate("lessThan(10, 5)", context));
            assertEquals(false, evaluator.evaluate("lessThan(5, 5)", context));
        }

        @Test
        @DisplayName("should compare negative numbers")
        void testNegativeNumbers() {
            assertEquals(true, evaluator.evaluate("lessThan(-10, -5)", context));
            assertEquals(false, evaluator.evaluate("lessThan(-5, -10)", context));
        }

        @Test
        @DisplayName("should compare decimals")
        void testDecimals() {
            assertEquals(true, evaluator.evaluate("lessThan(2.71, 3.14)", context));
            assertEquals(false, evaluator.evaluate("lessThan(3.14, 2.71)", context));
        }

        @Test
        @DisplayName("should compare strings lexicographically")
        void testStrings() {
            assertEquals(true, evaluator.evaluate("lessThan('a', 'b')", context));
            assertEquals(false, evaluator.evaluate("lessThan('b', 'a')", context));
        }

        @Test
        @DisplayName("should compare dates")
        void testDates() {
            LocalDate date1 = LocalDate.of(2023, 1, 1);
            LocalDate date2 = LocalDate.of(2023, 12, 31);
            context.setVariable("date1", date1);
            context.setVariable("date2", date2);

            assertEquals(true, evaluator.evaluate("lessThan($date1, $date2)", context));
            assertEquals(false, evaluator.evaluate("lessThan($date2, $date1)", context));
        }

        @Test
        @DisplayName("should return false for null values")
        void testNullValues() {
            context.setVariable("nullVar", null);
            assertEquals(false, evaluator.evaluate("lessThan($nullVar, 5)", context));
            assertEquals(false, evaluator.evaluate("lessThan(5, $nullVar)", context));
        }
    }

    @Nested
    @DisplayName("greaterThanOrEqual Function")
    class GreaterThanOrEqualTests {

        @Test
        @DisplayName("should return true when greater")
        void testGreater() {
            assertEquals(true, evaluator.evaluate("greaterThanOrEqual(10, 5)", context));
        }

        @Test
        @DisplayName("should return true when equal")
        void testEqual() {
            assertEquals(true, evaluator.evaluate("greaterThanOrEqual(5, 5)", context));
        }

        @Test
        @DisplayName("should return false when less")
        void testLess() {
            assertEquals(false, evaluator.evaluate("greaterThanOrEqual(5, 10)", context));
        }

        @Test
        @DisplayName("should work with strings")
        void testStrings() {
            assertEquals(true, evaluator.evaluate("greaterThanOrEqual('b', 'a')", context));
            assertEquals(true, evaluator.evaluate("greaterThanOrEqual('a', 'a')", context));
            assertEquals(false, evaluator.evaluate("greaterThanOrEqual('a', 'b')", context));
        }

        @Test
        @DisplayName("should work with dates")
        void testDates() {
            LocalDate date = LocalDate.of(2023, 5, 15);
            context.setVariable("date", date);

            assertEquals(true, evaluator.evaluate("greaterThanOrEqual($date, $date)", context));
        }
    }

    @Nested
    @DisplayName("lessThanOrEqual Function")
    class LessThanOrEqualTests {

        @Test
        @DisplayName("should return true when less")
        void testLess() {
            assertEquals(true, evaluator.evaluate("lessThanOrEqual(5, 10)", context));
        }

        @Test
        @DisplayName("should return true when equal")
        void testEqual() {
            assertEquals(true, evaluator.evaluate("lessThanOrEqual(5, 5)", context));
        }

        @Test
        @DisplayName("should return false when greater")
        void testGreater() {
            assertEquals(false, evaluator.evaluate("lessThanOrEqual(10, 5)", context));
        }

        @Test
        @DisplayName("should work with strings")
        void testStrings() {
            assertEquals(true, evaluator.evaluate("lessThanOrEqual('a', 'b')", context));
            assertEquals(true, evaluator.evaluate("lessThanOrEqual('a', 'a')", context));
            assertEquals(false, evaluator.evaluate("lessThanOrEqual('b', 'a')", context));
        }
    }

    @Nested
    @DisplayName("strictEquals Function")
    class StrictEqualsTests {

        @Test
        @DisplayName("should compare equal values")
        void testEqualValues() {
            assertEquals(true, evaluator.evaluate("strictEquals(5, 5)", context));
            assertEquals(true, evaluator.evaluate("strictEquals('hello', 'hello')", context));
            assertEquals(true, evaluator.evaluate("strictEquals(true, true)", context));
        }

        @Test
        @DisplayName("should compare unequal values")
        void testUnequalValues() {
            assertEquals(false, evaluator.evaluate("strictEquals(5, 10)", context));
            assertEquals(false, evaluator.evaluate("strictEquals('hello', 'world')", context));
        }

        @Test
        @DisplayName("should handle null comparisons")
        void testNullComparisons() {
            assertEquals(true, evaluator.evaluate("strictEquals(null, null)", context));
            assertEquals(false, evaluator.evaluate("strictEquals(null, 5)", context));
            assertEquals(false, evaluator.evaluate("strictEquals('hello', null)", context));
        }

        @Test
        @DisplayName("should be type-sensitive")
        void testTypeSensitive() {
            assertEquals(false, evaluator.evaluate("strictEquals(5, '5')", context));
            assertEquals(false, evaluator.evaluate("strictEquals(1, true)", context));
        }
    }

    @Nested
    @DisplayName("notEquals Function")
    class NotEqualsTests {

        @Test
        @DisplayName("should return true for unequal values")
        void testUnequalValues() {
            assertEquals(true, evaluator.evaluate("notEquals(5, 10)", context));
            assertEquals(true, evaluator.evaluate("notEquals('hello', 'world')", context));
        }

        @Test
        @DisplayName("should return false for equal values")
        void testEqualValues() {
            assertEquals(false, evaluator.evaluate("notEquals(5, 5)", context));
            assertEquals(false, evaluator.evaluate("notEquals('hello', 'hello')", context));
        }

        @Test
        @DisplayName("should handle null comparisons")
        void testNullComparisons() {
            assertEquals(false, evaluator.evaluate("notEquals(null, null)", context));
            assertEquals(true, evaluator.evaluate("notEquals(null, 5)", context));
            assertEquals(true, evaluator.evaluate("notEquals('hello', null)", context));
        }
    }

    @Nested
    @DisplayName("Complex Comparison Operations")
    class ComplexComparisonTests {

        @Test
        @DisplayName("should combine multiple comparisons")
        void testCombinedComparisons() {
            context.setVariable("age", 25);
            assertEquals(true,
                    evaluator.evaluate("greaterThanOrEqual($age, 18) && lessThan($age, 65)", context));
        }

        @Test
        @DisplayName("should use comparisons in conditionals")
        void testComparisonsInConditionals() {
            context.setVariable("score", 85);
            assertEquals("pass",
                    evaluator.evaluate("greaterThanOrEqual($score, 60) ? 'pass' : 'fail'", context));
        }

        @Test
        @DisplayName("should chain comparisons")
        void testChainedComparisons() {
            context.setVariable("x", 10);
            context.setVariable("y", 20);
            context.setVariable("z", 30);

            assertEquals(true,
                    evaluator.evaluate("lessThan($x, $y) && lessThan($y, $z)", context));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCasesTests {

        @Test
        @DisplayName("should handle zero comparisons")
        void testZeroComparisons() {
            assertEquals(true, evaluator.evaluate("greaterThan(0, -1)", context));
            assertEquals(true, evaluator.evaluate("lessThan(-1, 0)", context));
            assertEquals(true, evaluator.evaluate("strictEquals(0, 0)", context));
        }

        @Test
        @DisplayName("should handle very large numbers")
        void testLargeNumbers() {
            assertEquals(true, evaluator.evaluate("greaterThan(1000000, 999999)", context));
        }

        @Test
        @DisplayName("should handle very small decimals")
        void testSmallDecimals() {
            assertEquals(true, evaluator.evaluate("lessThan(0.0001, 0.0002)", context));
        }

        @Test
        @DisplayName("should handle empty strings")
        void testEmptyStrings() {
            assertEquals(true, evaluator.evaluate("strictEquals('', '')", context));
            assertEquals(true, evaluator.evaluate("lessThan('', 'a')", context));
        }

        @Test
        @DisplayName("should handle case-sensitive string comparison")
        void testCaseSensitiveStrings() {
            assertEquals(true, evaluator.evaluate("lessThan('A', 'a')", context));
            assertEquals(false, evaluator.evaluate("strictEquals('A', 'a')", context));
        }
    }
}
