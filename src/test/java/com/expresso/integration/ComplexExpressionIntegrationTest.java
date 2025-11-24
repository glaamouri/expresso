package com.expresso.integration;

import com.expresso.ExpressionEvaluator;
import com.expresso.context.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for complex multi-function expressions and real-world
 * scenarios.
 */
@DisplayName("Complex Expression Integration Tests")
class ComplexExpressionIntegrationTest {

    private ExpressionEvaluator evaluator;
    private Context context;

    @BeforeEach
    void setUp() {
        evaluator = new ExpressionEvaluator();
        context = new Context();
    }

    @Nested
    @DisplayName("Deeply Nested Function Calls")
    class DeeplyNestedTests {

        @Test
        @DisplayName("should handle 3-level nested functions")
        void testThreeLevelNesting() {
            assertEquals("HELLO",
                    evaluator.evaluate("upperCase(trim(lowerCase('  HELLO  ')))", context));
        }

        @Test
        @DisplayName("should handle 4-level nested functions")
        void testFourLevelNesting() {
            assertEquals(5,
                    evaluator.evaluate("length(upperCase(trim(lowerCase('  hello  '))))", context));
        }

        @Test
        @DisplayName("should combine math and string functions")
        void testMixedFunctionTypes() {
            assertEquals("5.0",
                    evaluator.evaluate("toString(sqrt(25))", context));
        }

        @Test
        @DisplayName("should nest collection and math functions")
        void testCollectionMathNesting() {
            context.setVariable("nums", Arrays.asList(1, 2, 3, 4, 5));
            assertEquals(2.0,
                    evaluator.evaluate("avg(subList($nums, 0, 3))", context));
        }
    }

    @Nested
    @DisplayName("Mixed Operators and Functions")
    class MixedOperatorsTests {

        @Test
        @DisplayName("should combine arithmetic with functions")
        void testArithmeticWithFunctions() {
            assertEquals(10.0,
                    evaluator.evaluate("abs(-5) + sqrt(25)", context));
        }

        @Test
        @DisplayName("should use functions in comparisons")
        void testFunctionsInComparisons() {
            context.setVariable("text", "hello world");
            assertEquals(true,
                    evaluator.evaluate("length($text) > 10", context));
        }

        @Test
        @DisplayName("should combine logical operators with functions")
        void testLogicalWithFunctions() {
            context.setVariable("age", 25);
            assertEquals(true,
                    evaluator.evaluate("!isNull($age) && $age >= 18", context));
        }

        @Test
        @DisplayName("should use functions in ternary operator")
        void testFunctionsInTernary() {
            context.setVariable("score", 85);
            assertEquals("B",
                    evaluator.evaluate("$score >= 90 ? 'A' : ($score >= 80 ? 'B' : 'C')", context));
        }
    }

    @Nested
    @DisplayName("Complex Property Access with Functions")
    class PropertyAccessTests {

        @Test
        @DisplayName("should access nested properties with functions")
        void testNestedPropertyAccess() {
            Map<String, Object> user = new HashMap<>();
            user.put("name", "John Doe");
            Map<String, Object> address = new HashMap<>();
            address.put("city", "paris");
            user.put("address", address);
            context.setVariable("user", user);

            assertEquals("PARIS",
                    evaluator.evaluate("upperCase($user.address.city)", context));
        }

        @Test
        @DisplayName("should use array access with functions")
        void testArrayAccessWithFunctions() {
            context.setVariable("names", Arrays.asList("alice", "bob", "charlie"));
            assertEquals("ALICE",
                    evaluator.evaluate("upperCase($names[0])", context));
        }

        @Test
        @DisplayName("should combine property access, functions, and operators")
        void testComplexPropertyFunctionCombination() {
            Map<String, Object> data = new HashMap<>();
            data.put("value", "  test  ");
            context.setVariable("data", data);

            assertEquals(true,
                    evaluator.evaluate("length(trim($data.value)) > 0", context));
        }
    }

    @Nested
    @DisplayName("Real-World Scenarios")
    class RealWorldScenariosTests {

        @Test
        @DisplayName("should validate email format")
        void testEmailValidation() {
            context.setVariable("email", "user@example.com");
            assertEquals(true,
                    evaluator.evaluate("contains($email, '@') && contains($email, '.')", context));
        }

        @Test
        @DisplayName("should calculate age from birthdate")
        void testAgeCalculation() {
            LocalDate birthdate = LocalDate.of(1990, 1, 1);
            LocalDate today = LocalDate.now();
            context.setVariable("birthdate", birthdate);
            context.setVariable("today", today);

            Long daysBetween = (Long) evaluator.evaluate("daysBetween($birthdate, $today)", context);
            assertTrue(daysBetween > 0);
        }

        @Test
        @DisplayName("should format user display name")
        void testUserDisplayName() {
            context.setVariable("firstName", "  john  ");
            context.setVariable("lastName", "  doe  ");
            assertEquals("John Doe",
                    evaluator.evaluate(
                            "upperCase(charAt(trim($firstName), 0)) + lowerCase(substring(trim($firstName), 1)) + ' ' + upperCase(charAt(trim($lastName), 0)) + lowerCase(substring(trim($lastName), 1))",
                            context));
        }

        @Test
        @DisplayName("should calculate discount price")
        void testDiscountCalculation() {
            context.setVariable("price", 100.0);
            context.setVariable("discount", 0.2);
            assertEquals(80.0,
                    evaluator.evaluate("$price * (1 - $discount)", context));
        }

        @Test
        @DisplayName("should validate password strength")
        void testPasswordValidation() {
            context.setVariable("password", "MyP@ssw0rd");
            assertEquals(true,
                    evaluator.evaluate("length($password) >= 8 && contains($password, '@')", context));
        }
    }

    @Nested
    @DisplayName("Collection Operations with Functions")
    class CollectionOperationTests {

        @Test
        @DisplayName("should filter and transform collection")
        void testFilterTransform() {
            context.setVariable("nums", Arrays.asList(1, 2, 3, 4, 5));
            // Get sum of first 3 elements
            assertEquals(6.0,
                    evaluator.evaluate("sum(subList($nums, 0, 3))", context));
        }

        @Test
        @DisplayName("should sort and get extremes")
        void testSortExtremes() {
            context.setVariable("nums", Arrays.asList(5, 2, 8, 1, 9));
            assertEquals(1,
                    evaluator.evaluate("first(sort($nums))", context));
            assertEquals(9,
                    evaluator.evaluate("last(sort($nums))", context));
        }

        @Test
        @DisplayName("should join and split strings")
        void testJoinSplit() {
            context.setVariable("parts", Arrays.asList("hello", "world", "test"));
            String joined = (String) evaluator.evaluate("join(' ', $parts)", context);
            context.setVariable("joined", joined);

            List<?> result = (List<?>) evaluator.evaluate("split($joined, ' ')", context);
            assertEquals(3, result.size());
        }
    }

    @Nested
    @DisplayName("Date and Time Operations")
    class DateTimeOperationsTests {

        @Test
        @DisplayName("should calculate future date and format")
        void testFutureDateFormatting() {
            LocalDate today = LocalDate.of(2023, 1, 1);
            context.setVariable("today", today);

            LocalDate future = (LocalDate) evaluator.evaluate("addDays($today, 30)", context);
            context.setVariable("future", future);

            String formatted = (String) evaluator.evaluate("formatDate($future, 'yyyy-MM-dd')", context);
            assertEquals("2023-01-31", formatted);
        }

        @Test
        @DisplayName("should parse and manipulate dates")
        void testParseDateManipulation() {
            LocalDate parsed = (LocalDate) evaluator.evaluate("parseDate('2023-01-15')", context);
            context.setVariable("date", parsed);

            assertEquals(2023, evaluator.evaluate("getYear($date)", context));
            assertEquals(1, evaluator.evaluate("getMonth($date)", context));
            assertEquals(15, evaluator.evaluate("getDayOfMonth($date)", context));
        }
    }

    @Nested
    @DisplayName("Complex Conditional Logic")
    class ConditionalLogicTests {

        @Test
        @DisplayName("should handle nested conditionals with functions")
        void testNestedConditionalsWithFunctions() {
            context.setVariable("status", "active");
            context.setVariable("age", 25);

            assertEquals("eligible",
                    evaluator.evaluate(
                            "equals($status, 'active') ? ($age >= 18 ? 'eligible' : 'not eligible') : 'inactive'",
                            context));
        }

        @Test
        @DisplayName("should combine multiple conditions")
        void testMultipleConditions() {
            context.setVariable("score", 85);
            context.setVariable("attendance", 95);

            assertEquals(true,
                    evaluator.evaluate(
                            "$score >= 60 && $attendance >= 80",
                            context));
        }

        @Test
        @DisplayName("should use coalesce in complex expressions")
        void testCoalesceInExpression() {
            context.setVariable("primary", null);
            context.setVariable("secondary", null);
            context.setVariable("default", "fallback");

            assertEquals("fallback",
                    evaluator.evaluate("coalesce($primary, $secondary, $default)", context));
        }
    }

    @Nested
    @DisplayName("Type Conversion and Validation")
    class TypeConversionTests {

        @Test
        @DisplayName("should convert and validate types")
        void testConvertValidate() {
            context.setVariable("numStr", "42");
            assertEquals(true,
                    evaluator.evaluate("isNumber(toNumber($numStr))", context));
        }

        @Test
        @DisplayName("should chain type conversions")
        void testChainConversions() {
            assertEquals(true,
                    evaluator.evaluate("toBoolean(toNumber('1'))", context));
        }

        @Test
        @DisplayName("should validate with typeof")
        void testTypeofValidation() {
            context.setVariable("value", 42);
            assertEquals(true,
                    evaluator.evaluate("typeof($value) == 'number'", context));
        }
    }

    @Nested
    @DisplayName("Performance and Stress Tests")
    class PerformanceTests {

        @Test
        @DisplayName("should handle large string operations")
        void testLargeString() {
            StringBuilder largeText = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                largeText.append("test ");
            }
            context.setVariable("text", largeText.toString());

            Object result = evaluator.evaluate("length($text)", context);
            assertTrue((Integer) result > 4000);
        }

        @Test
        @DisplayName("should handle large lists")
        void testLargeList() {
            Integer[] largeArray = new Integer[1000];
            Arrays.fill(largeArray, 1);
            context.setVariable("nums", Arrays.asList(largeArray));

            assertEquals(1000, evaluator.evaluate("size($nums)", context));
            assertEquals(1000.0, (Double) evaluator.evaluate("sum($nums)", context), 0.001);
        }

        @Test
        @DisplayName("should handle many nested calls")
        void testManyNestedCalls() {
            // Multiple levels of abs calls
            assertEquals(5.0,
                    evaluator.evaluate("abs(abs(abs(abs(abs(-5)))))", context));
        }
    }
}
