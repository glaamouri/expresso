package com.expresso.context.functions;

import com.expresso.ExpressionEvaluator;
import com.expresso.context.Context;
import com.expresso.exception.EvaluationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for Math functions covering edge cases, special values,
 * and precision.
 */
@DisplayName("Math Functions - Comprehensive Tests")
class MathFunctionsComprehensiveTest {

    private ExpressionEvaluator evaluator;
    private Context context;

    @BeforeEach
    void setUp() {
        evaluator = new ExpressionEvaluator();
        context = new Context();
    }

    @Nested
    @DisplayName("abs Function")
    class AbsTests {

        @Test
        @DisplayName("should return absolute value of negative number")
        void testNegativeNumber() {
            assertEquals(5.0, (Double) evaluator.evaluate("abs(-5)", context), 0.001);
        }

        @Test
        @DisplayName("should return absolute value of positive number")
        void testPositiveNumber() {
            assertEquals(5.0, (Double) evaluator.evaluate("abs(5)", context), 0.001);
        }

        @Test
        @DisplayName("should return 0 for 0")
        void testZero() {
            assertEquals(0.0, (Double) evaluator.evaluate("abs(0)", context), 0.001);
        }

        @Test
        @DisplayName("should handle large numbers")
        void testLargeNumbers() {
            assertEquals(1000000.0, (Double) evaluator.evaluate("abs(-1000000)", context), 0.001);
        }

        @Test
        @DisplayName("should handle decimal numbers")
        void testDecimalNumbers() {
            assertEquals(3.14, (Double) evaluator.evaluate("abs(-3.14)", context), 0.001);
        }

        @Test
        @DisplayName("should handle very small numbers")
        void testSmallNumbers() {
            assertEquals(0.0001, (Double) evaluator.evaluate("abs(-0.0001)", context), 0.00001);
        }
    }

    @Nested
    @DisplayName("ceil Function")
    class CeilTests {

        @Test
        @DisplayName("should round up positive decimal")
        void testPositiveDecimal() {
            assertEquals(4.0, (Double) evaluator.evaluate("ceil(3.1)", context), 0.001);
            assertEquals(4.0, (Double) evaluator.evaluate("ceil(3.9)", context), 0.001);
        }

        @Test
        @DisplayName("should round up negative decimal")
        void testNegativeDecimal() {
            assertEquals(-3.0, (Double) evaluator.evaluate("ceil(-3.9)", context), 0.001);
            assertEquals(-3.0, (Double) evaluator.evaluate("ceil(-3.1)", context), 0.001);
        }

        @Test
        @DisplayName("should return same value for integers")
        void testIntegers() {
            assertEquals(5.0, (Double) evaluator.evaluate("ceil(5)", context), 0.001);
            assertEquals(-5.0, (Double) evaluator.evaluate("ceil(-5)", context), 0.001);
        }

        @Test
        @DisplayName("should return 0 for 0")
        void testZero() {
            assertEquals(0.0, (Double) evaluator.evaluate("ceil(0)", context), 0.001);
        }

        @Test
        @DisplayName("should handle very small decimals")
        void testSmallDecimals() {
            assertEquals(1.0, (Double) evaluator.evaluate("ceil(0.0001)", context), 0.001);
        }
    }

    @Nested
    @DisplayName("floor Function")
    class FloorTests {

        @Test
        @DisplayName("should round down positive decimal")
        void testPositiveDecimal() {
            assertEquals(3.0, (Double) evaluator.evaluate("floor(3.1)", context), 0.001);
            assertEquals(3.0, (Double) evaluator.evaluate("floor(3.9)", context), 0.001);
        }

        @Test
        @DisplayName("should round down negative decimal")
        void testNegativeDecimal() {
            assertEquals(-4.0, (Double) evaluator.evaluate("floor(-3.1)", context), 0.001);
            assertEquals(-4.0, (Double) evaluator.evaluate("floor(-3.9)", context), 0.001);
        }

        @Test
        @DisplayName("should return same value for integers")
        void testIntegers() {
            assertEquals(5.0, (Double) evaluator.evaluate("floor(5)", context), 0.001);
            assertEquals(-5.0, (Double) evaluator.evaluate("floor(-5)", context), 0.001);
        }

        @Test
        @DisplayName("should return 0 for 0")
        void testZero() {
            assertEquals(0.0, (Double) evaluator.evaluate("floor(0)", context), 0.001);
        }
    }

    @Nested
    @DisplayName("round Function")
    class RoundTests {

        @Test
        @DisplayName("should round to nearest integer")
        void testRoundToInteger() {
            Object result1 = evaluator.evaluate("round(3.4)", context);
            assertEquals(3, ((Number) result1).intValue());

            Object result2 = evaluator.evaluate("round(3.5)", context);
            assertEquals(4, ((Number) result2).intValue());

            Object result3 = evaluator.evaluate("round(3.6)", context);
            assertEquals(4, ((Number) result3).intValue());
        }

        @Test
        @DisplayName("should round negative numbers")
        void testRoundNegative() {
            Object result1 = evaluator.evaluate("round(-3.4)", context);
            assertEquals(-3, ((Number) result1).intValue());

            Object result2 = evaluator.evaluate("round(-3.6)", context);
            assertEquals(-4, ((Number) result2).intValue());
        }

        @Test
        @DisplayName("should round to specified decimal places")
        void testRoundToDecimals() {
            assertEquals(3.14, (Double) evaluator.evaluate("round(3.14159, 2)", context), 0.001);
            assertEquals(3.142, (Double) evaluator.evaluate("round(3.14159, 3)", context), 0.001);
            assertEquals(3.1, (Double) evaluator.evaluate("round(3.14159, 1)", context), 0.001);
        }

        @Test
        @DisplayName("should handle zero decimal places")
        void testZeroDecimals() {
            Object result = evaluator.evaluate("round(3.7, 0)", context);
            assertEquals(4.0, ((Number) result).doubleValue(), 0.001);
        }

        @Test
        @DisplayName("should round up at .5")
        void testRoundHalf() {
            Object result = evaluator.evaluate("round(2.5)", context);
            assertEquals(3, ((Number) result).intValue());
        }
    }

    @Nested
    @DisplayName("max Function")
    class MaxTests {

        @Test
        @DisplayName("should return larger of two positive numbers")
        void testPositiveNumbers() {
            assertEquals(10.0, (Double) evaluator.evaluate("max(5, 10)", context), 0.001);
            assertEquals(10.0, (Double) evaluator.evaluate("max(10, 5)", context), 0.001);
        }

        @Test
        @DisplayName("should return larger of two negative numbers")
        void testNegativeNumbers() {
            assertEquals(-5.0, (Double) evaluator.evaluate("max(-10, -5)", context), 0.001);
        }

        @Test
        @DisplayName("should handle mixed positive and negative")
        void testMixedNumbers() {
            assertEquals(5.0, (Double) evaluator.evaluate("max(-10, 5)", context), 0.001);
            assertEquals(5.0, (Double) evaluator.evaluate("max(5, -10)", context), 0.001);
        }

        @Test
        @DisplayName("should handle equal numbers")
        void testEqualNumbers() {
            assertEquals(5.0, (Double) evaluator.evaluate("max(5, 5)", context), 0.001);
        }

        @Test
        @DisplayName("should handle zero")
        void testZero() {
            assertEquals(5.0, (Double) evaluator.evaluate("max(0, 5)", context), 0.001);
            assertEquals(0.0, (Double) evaluator.evaluate("max(-5, 0)", context), 0.001);
        }

        @Test
        @DisplayName("should handle decimals")
        void testDecimals() {
            assertEquals(3.14, (Double) evaluator.evaluate("max(3.14, 2.71)", context), 0.001);
        }
    }

    @Nested
    @DisplayName("min Function")
    class MinTests {

        @Test
        @DisplayName("should return smaller of two positive numbers")
        void testPositiveNumbers() {
            assertEquals(5.0, (Double) evaluator.evaluate("min(5, 10)", context), 0.001);
            assertEquals(5.0, (Double) evaluator.evaluate("min(10, 5)", context), 0.001);
        }

        @Test
        @DisplayName("should return smaller of two negative numbers")
        void testNegativeNumbers() {
            assertEquals(-10.0, (Double) evaluator.evaluate("min(-10, -5)", context), 0.001);
        }

        @Test
        @DisplayName("should handle mixed positive and negative")
        void testMixedNumbers() {
            assertEquals(-10.0, (Double) evaluator.evaluate("min(-10, 5)", context), 0.001);
        }

        @Test
        @DisplayName("should handle equal numbers")
        void testEqualNumbers() {
            assertEquals(5.0, (Double) evaluator.evaluate("min(5, 5)", context), 0.001);
        }

        @Test
        @DisplayName("should handle zero")
        void testZero() {
            assertEquals(0.0, (Double) evaluator.evaluate("min(0, 5)", context), 0.001);
            assertEquals(-5.0, (Double) evaluator.evaluate("min(-5, 0)", context), 0.001);
        }
    }

    @Nested
    @DisplayName("pow Function")
    class PowTests {

        @Test
        @DisplayName("should calculate positive exponent")
        void testPositiveExponent() {
            assertEquals(8.0, (Double) evaluator.evaluate("pow(2, 3)", context), 0.001);
            assertEquals(25.0, (Double) evaluator.evaluate("pow(5, 2)", context), 0.001);
        }

        @Test
        @DisplayName("should handle exponent of 0")
        void testZeroExponent() {
            assertEquals(1.0, (Double) evaluator.evaluate("pow(5, 0)", context), 0.001);
            assertEquals(1.0, (Double) evaluator.evaluate("pow(0, 0)", context), 0.001);
        }

        @Test
        @DisplayName("should handle exponent of 1")
        void testExponentOfOne() {
            assertEquals(5.0, (Double) evaluator.evaluate("pow(5, 1)", context), 0.001);
        }

        @Test
        @DisplayName("should handle negative exponent")
        void testNegativeExponent() {
            assertEquals(0.5, (Double) evaluator.evaluate("pow(2, -1)", context), 0.001);
            assertEquals(0.25, (Double) evaluator.evaluate("pow(2, -2)", context), 0.001);
        }

        @Test
        @DisplayName("should handle decimal exponent")
        void testDecimalExponent() {
            assertEquals(4.0, (Double) evaluator.evaluate("pow(16, 0.5)", context), 0.001); // Square root
        }

        @Test
        @DisplayName("should handle negative base with even exponent")
        void testNegativeBaseEvenExponent() {
            assertEquals(4.0, (Double) evaluator.evaluate("pow(-2, 2)", context), 0.001);
        }

        @Test
        @DisplayName("should handle negative base with odd exponent")
        void testNegativeBaseOddExponent() {
            assertEquals(-8.0, (Double) evaluator.evaluate("pow(-2, 3)", context), 0.001);
        }
    }

    @Nested
    @DisplayName("sqrt Function")
    class SqrtTests {

        @Test
        @DisplayName("should calculate square root of perfect square")
        void testPerfectSquare() {
            assertEquals(4.0, (Double) evaluator.evaluate("sqrt(16)", context), 0.001);
            assertEquals(5.0, (Double) evaluator.evaluate("sqrt(25)", context), 0.001);
        }

        @Test
        @DisplayName("should calculate square root of non-perfect square")
        void testNonPerfectSquare() {
            assertEquals(1.414, (Double) evaluator.evaluate("sqrt(2)", context), 0.001);
            assertEquals(1.732, (Double) evaluator.evaluate("sqrt(3)", context), 0.001);
        }

        @Test
        @DisplayName("should return 0 for 0")
        void testZero() {
            assertEquals(0.0, (Double) evaluator.evaluate("sqrt(0)", context), 0.001);
        }

        @Test
        @DisplayName("should return 1 for 1")
        void testOne() {
            assertEquals(1.0, (Double) evaluator.evaluate("sqrt(1)", context), 0.001);
        }

        @Test
        @DisplayName("should handle decimal numbers")
        void testDecimals() {
            assertEquals(0.5, (Double) evaluator.evaluate("sqrt(0.25)", context), 0.001);
        }

        @Test
        @DisplayName("should return NaN for negative numbers")
        void testNegativeNumber() {
            Object result = evaluator.evaluate("sqrt(-1)", context);
            assertTrue(Double.isNaN((Double) result));
        }
    }

    @Nested
    @DisplayName("random Function")
    class RandomTests {

        @Test
        @DisplayName("should return a number between 0 and 1")
        void testRandomRange() {
            for (int i = 0; i < 10; i++) {
                Double result = (Double) evaluator.evaluate("random()", context);
                assertTrue(result >= 0.0 && result < 1.0,
                        "Random should be >= 0 and < 1, got: " + result);
            }
        }

        @Test
        @DisplayName("should return different values on multiple calls")
        void testRandomVariability() {
            Double result1 = (Double) evaluator.evaluate("random()", context);
            Double result2 = (Double) evaluator.evaluate("random()", context);
            Double result3 = (Double) evaluator.evaluate("random()", context);

            // Very unlikely all three are exactly the same
            assertFalse(result1.equals(result2) && result2.equals(result3),
                    "Random should produce different values");
        }
    }

    @Nested
    @DisplayName("Trigonometric Functions")
    class TrigonometricTests {

        @Test
        @DisplayName("sin should return correct values")
        void testSin() {
            assertEquals(0.0, (Double) evaluator.evaluate("sin(0)", context), 0.001);
            assertEquals(1.0, (Double) evaluator.evaluate("sin(1.5708)", context), 0.001); // π/2
            assertEquals(0.0, (Double) evaluator.evaluate("sin(3.14159)", context), 0.001); // π
        }

        @Test
        @DisplayName("cos should return correct values")
        void testCos() {
            assertEquals(1.0, (Double) evaluator.evaluate("cos(0)", context), 0.001);
            assertEquals(0.0, (Double) evaluator.evaluate("cos(1.5708)", context), 0.001); // π/2
            assertEquals(-1.0, (Double) evaluator.evaluate("cos(3.14159)", context), 0.001); // π
        }

        @Test
        @DisplayName("tan should return correct values")
        void testTan() {
            assertEquals(0.0, (Double) evaluator.evaluate("tan(0)", context), 0.001);
            assertEquals(1.0, (Double) evaluator.evaluate("tan(0.7854)", context), 0.001); // π/4
        }

        @Test
        @DisplayName("should handle negative angles")
        void testNegativeAngles() {
            Double sinNeg = (Double) evaluator.evaluate("sin(-1)", context);
            Double sinPos = (Double) evaluator.evaluate("sin(1)", context);
            assertEquals(-sinPos, sinNeg, 0.001);
        }
    }

    @Nested
    @DisplayName("Logarithm Functions")
    class LogarithmTests {

        @Test
        @DisplayName("log should calculate natural logarithm")
        void testNaturalLog() {
            assertEquals(0.0, (Double) evaluator.evaluate("log(1)", context), 0.001);
            assertEquals(1.0, (Double) evaluator.evaluate("log(2.71828)", context), 0.001); // e
            assertEquals(2.0, (Double) evaluator.evaluate("log(7.389)", context), 0.01);
        }

        @Test
        @DisplayName("log10 should calculate base 10 logarithm")
        void testLog10() {
            assertEquals(0.0, (Double) evaluator.evaluate("log10(1)", context), 0.001);
            assertEquals(1.0, (Double) evaluator.evaluate("log10(10)", context), 0.001);
            assertEquals(2.0, (Double) evaluator.evaluate("log10(100)", context), 0.001);
            assertEquals(3.0, (Double) evaluator.evaluate("log10(1000)", context), 0.001);
        }

        @Test
        @DisplayName("log of numbers less than 1 should be negative")
        void testLogLessThanOne() {
            Double result = (Double) evaluator.evaluate("log(0.5)", context);
            assertTrue(result < 0);
        }

        @Test
        @DisplayName("log of 0 should return negative infinity")
        void testLogZero() {
            Double result = (Double) evaluator.evaluate("log(0)", context);
            assertTrue(Double.isInfinite(result) && result < 0);
        }

        @Test
        @DisplayName("log of negative number should return NaN")
        void testLogNegative() {
            Double result = (Double) evaluator.evaluate("log(-1)", context);
            assertTrue(Double.isNaN(result));
        }
    }

    @Nested
    @DisplayName("exp Function")
    class ExpTests {

        @Test
        @DisplayName("should calculate e^x")
        void testExp() {
            assertEquals(1.0, (Double) evaluator.evaluate("exp(0)", context), 0.001);
            assertEquals(2.71828, (Double) evaluator.evaluate("exp(1)", context), 0.001);
            assertEquals(7.389, (Double) evaluator.evaluate("exp(2)", context), 0.01);
        }

        @Test
        @DisplayName("should handle negative numbers")
        void testExpNegative() {
            assertEquals(0.368, (Double) evaluator.evaluate("exp(-1)", context), 0.01);
        }

        @Test
        @DisplayName("should return values greater than 0")
        void testExpPositive() {
            Double result = (Double) evaluator.evaluate("exp(-100)", context);
            assertTrue(result > 0);
        }
    }

    @Nested
    @DisplayName("Complex Math Operations")
    class ComplexMathTests {

        @Test
        @DisplayName("should combine multiple math functions")
        void testCombinedFunctions() {
            assertEquals(5.0, (Double) evaluator.evaluate("sqrt(pow(3, 2) + pow(4, 2))", context), 0.001);
        }

        @Test
        @DisplayName("should handle nested function calls")
        void testNestedFunctions() {
            assertEquals(5.0, (Double) evaluator.evaluate("abs(floor(-4.7))", context), 0.001);
            assertEquals(4.0, (Double) evaluator.evaluate("abs(floor(4.7))", context), 0.001);
        }

        @Test
        @DisplayName("should work with arithmetic operators")
        void testWithArithmetic() {
            assertEquals(8.0, (Double) evaluator.evaluate("pow(2, 2) + pow(2, 2)", context), 0.001);
            assertEquals(0.0, (Double) evaluator.evaluate("pow(2, 2) - pow(2, 2)", context), 0.001);
        }

        @Test
        @DisplayName("should handle complex expressions")
        void testComplexExpressions() {
            context.setVariable("x", 10);
            context.setVariable("y", 3);
            assertEquals(13.0, (Double) evaluator.evaluate("max($x, $y) + min($x, $y)", context), 0.001);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Special Values")
    class EdgeCasesTests {

        @Test
        @DisplayName("should handle very large numbers")
        void testLargeNumbers() {
            Double result = (Double) evaluator.evaluate("abs(-99999999)", context);
            assertEquals(99999999.0, result);
        }

        @Test
        @DisplayName("should handle very small numbers")
        void testSmallNumbers() {
            Double result = (Double) evaluator.evaluate("abs(-0.000001)", context);
            assertEquals(0.000001, result, 0.0000001);
        }

        @Test
        @DisplayName("should handle division by zero in custom expressions")
        void testDivisionByZero() {
            // sqrt of negative returns NaN
            Object result = evaluator.evaluate("sqrt(-1)", context);
            assertTrue(result instanceof Double);
            assertTrue(Double.isNaN((Double) result));
        }
    }
}
