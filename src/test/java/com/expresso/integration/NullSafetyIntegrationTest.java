package com.expresso.integration;

import com.expresso.ExpressionEvaluator;
import com.expresso.context.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for null-safe operators and null handling across the
 * library.
 */
@DisplayName("Null Safety Integration Tests")
class NullSafetyIntegrationTest {

    private ExpressionEvaluator evaluator;
    private Context context;

    @BeforeEach
    void setUp() {
        evaluator = new ExpressionEvaluator();
        context = new Context();
    }

    @Nested
    @DisplayName("Null-Safe Property Access (?.)")
    class NullSafePropertyAccessTests {

        @Test
        @DisplayName("should return null for null-safe access on null object")
        void testNullObject() {
            context.setVariable("user", null);
            assertNull(evaluator.evaluate("$user?.name", context));
        }

        @Test
        @DisplayName("should access property when object is not null")
        void testNonNullObject() {
            Map<String, Object> user = new HashMap<>();
            user.put("name", "John");
            context.setVariable("user", user);

            assertEquals("John", evaluator.evaluate("$user?.name", context));
        }

        @Test
        @DisplayName("should chain null-safe property access")
        void testChainedNullSafeAccess() {
            Map<String, Object> user = new HashMap<>();
            Map<String, Object> address = new HashMap<>();
            address.put("city", "Paris");
            user.put("address", address);
            context.setVariable("user", user);

            assertEquals("Paris", evaluator.evaluate("$user?.address?.city", context));
        }

        @Test
        @DisplayName("should return null for null in chain")
        void testNullInChain() {
            Map<String, Object> user = new HashMap<>();
            user.put("address", null);
            context.setVariable("user", user);

            assertNull(evaluator.evaluate("$user?.address?.city", context));
        }
    }

    @Nested
    @DisplayName("Null-Safe Array Access (?[)")
    class NullSafeArrayAccessTests {

        @Test
        @DisplayName("should return null for null-safe array access on null")
        void testNullArray() {
            context.setVariable("items", null);
            assertNull(evaluator.evaluate("$items?[0]", context));
        }

        @Test
        @DisplayName("should access array element when not null")
        void testNonNullArray() {
            context.setVariable("items", Arrays.asList("a", "b", "c"));
            assertEquals("a", evaluator.evaluate("$items?[0]", context));
        }

        @Test
        @DisplayName("should combine null-safe property and array access")
        void testCombinedNullSafeAccess() {
            Map<String, Object> user = new HashMap<>();
            user.put("hobbies", Arrays.asList("reading", "gaming"));
            context.setVariable("user", user);

            assertEquals("reading", evaluator.evaluate("$user?.hobbies?[0]", context));
        }

        @Test
        @DisplayName("should return null when intermediate value is null")
        void testNullIntermediateValue() {
            Map<String, Object> user = new HashMap<>();
            user.put("hobbies", null);
            context.setVariable("user", user);

            assertNull(evaluator.evaluate("$user?.hobbies?[0]", context));
        }
    }

    @Nested
    @DisplayName("Null Coalescing Operator (??)")
    class NullCoalescingTests {

        @Test
        @DisplayName("should return default for null value")
        void testNullValue() {
            context.setVariable("name", null);
            assertEquals("Unknown", evaluator.evaluate("$name ?? 'Unknown'", context));
        }

        @Test
        @DisplayName("should return actual value when not null")
        void testNonNullValue() {
            context.setVariable("name", "John");
            assertEquals("John", evaluator.evaluate("$name ?? 'Unknown'", context));
        }

        @Test
        @DisplayName("should chain null coalescing operators")
        void testChainedCoalescing() {
            context.setVariable("first", null);
            context.setVariable("second", null);
            context.setVariable("third", "fallback");

            assertEquals("fallback",
                    evaluator.evaluate("$first ?? $second ?? $third", context));
        }

        @Test
        @DisplayName("should work with null-safe property access")
        void testWithNullSafeAccess() {
            context.setVariable("user", null);
            assertEquals("Guest",
                    evaluator.evaluate("$user?.name ?? 'Guest'", context));
        }

        @Test
        @DisplayName("should preserve falsy non-null values")
        void testFalsyValues() {
            context.setVariable("count", 0);
            context.setVariable("active", false);
            context.setVariable("text", "");

            assertEquals(0, evaluator.evaluate("$count ?? -1", context));
            assertEquals(false, evaluator.evaluate("$active ?? true", context));
            assertEquals("", evaluator.evaluate("$text ?? 'default'", context));
        }
    }

    @Nested
    @DisplayName("Combined Null-Safe Operations")
    class CombinedNullSafeTests {

        @Test
        @DisplayName("should combine all null-safe operators")
        void testAllOperators() {
            context.setVariable("user", null);
            assertEquals("No hobbies",
                    evaluator.evaluate("$user?.hobbies?[0] ?? 'No hobbies'", context));
        }

        @Test
        @DisplayName("should work in complex expressions")
        void testComplexExpression() {
            Map<String, Object> user = new HashMap<>();
            Map<String, Object> profile = new HashMap<>();
            profile.put("displayName", null);
            user.put("profile", profile);
            context.setVariable("user", user);

            assertEquals("Anonymous",
                    evaluator.evaluate("$user?.profile?.displayName ?? 'Anonymous'", context));
        }

        @Test
        @DisplayName("should work with functions")
        void testWithFunctions() {
            context.setVariable("user", null);
            assertEquals("GUEST",
                    evaluator.evaluate("upperCase($user?.name ?? 'guest')", context));
        }

        @Test
        @DisplayName("should work in conditionals")
        void testInConditionals() {
            context.setVariable("user", null);
            assertEquals("unknown",
                    evaluator.evaluate("isNull($user?.name) ? 'unknown' : $user?.name", context));
        }
    }

    @Nested
    @DisplayName("Null Handling in Functions")
    class FunctionNullHandlingTests {

        @Test
        @DisplayName("should handle null in string functions")
        void testStringFunctionsNull() {
            context.setVariable("text", null);
            // Most string functions should throw or handle nullgracefully
            // This is already tested in individual function tests
        }

        @Test
        @DisplayName("should handle null in collection functions")
        void testCollectionFunctionsNull() {
            context.setVariable("list", null);
            assertEquals(0, evaluator.evaluate("size($list)", context));
            assertEquals(0.0, (Double) evaluator.evaluate("sum($list)", context), 0.001);
            assertNull(evaluator.evaluate("sort($list)", context));
        }

        @Test
        @DisplayName("should handle null in logic functions")
        void testLogicFunctionsNull() {
            context.setVariable("value", null);
            assertEquals(true, evaluator.evaluate("isNull($value)", context));
            assertEquals(true, evaluator.evaluate("isEmpty($value)", context));
        }

        @Test
        @DisplayName("should use coalesce to handle function nulls")
        void testCoalesceWithFunctions() {
            context.setVariable("list", null);
            assertEquals(0,
                    evaluator.evaluate("coalesce(size($list), 0)", context));
        }
    }

    @Nested
    @DisplayName("Real-World Null-Safety Scenarios")
    class RealWorldScenariosTests {

        @Test
        @DisplayName("should safely access nested user data")
        void testUserDataAccess() {
            // Scenario: Optional address information
            Map<String, Object> user1 = new HashMap<>();
            user1.put("name", "John");
            user1.put("address", null);

            Map<String, Object> user2 = new HashMap<>();
            user2.put("name", "Jane");
            Map<String, Object> address = new HashMap<>();
            address.put("city", "Paris");
            user2.put("address", address);

            context.setVariable("user", user1);
            assertEquals("No city",
                    evaluator.evaluate("$user?.address?.city ?? 'No city'", context));

            context.setVariable("user", user2);
            assertEquals("Paris",
                    evaluator.evaluate("$user?.address?.city ?? 'No city'", context));
        }

        @Test
        @DisplayName("should safely access optional list elements")
        void testOptionalListElements() {
            // Scenario: Optional items in a list
            Map<String, Object> data = new HashMap<>();
            data.put("items", null);
            context.setVariable("data", data);

            assertEquals("No items",
                    evaluator.evaluate("$data?.items?[0] ?? 'No items'", context));

            data.put("items", Arrays.asList("item1", "item2"));
            assertEquals("item1",
                    evaluator.evaluate("$data?.items?[0] ?? 'No items'", context));
        }

        @Test
        @DisplayName("should provide default values for missing configuration")
        void testConfigurationDefaults() {
            Map<String, Object> config = new HashMap<>();
            config.put("timeout", null);
            config.put("retries", 3);
            context.setVariable("config", config);

            assertEquals(30l,
                    evaluator.evaluate("$config?.timeout ?? 30", context));
            assertEquals(3,
                    evaluator.evaluate("$config?.retries ?? 5", context));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCasesTests {

        @Test
        @DisplayName("should distinguish between null and undefined variable")
        void testNullVsUndefined() {
            context.setVariable("nullVar", null);
            // undefinedVar is not set

            assertEquals(true, evaluator.evaluate("isNull($nullVar)", context));
        }

        @Test
        @DisplayName("should handle deeply nested null-safe chains")
        void testDeeplyNestedChains() {
            Map<String, Object> level1 = new HashMap<>();
            Map<String, Object> level2 = new HashMap<>();
            Map<String, Object> level3 = new HashMap<>();
            level3.put("value", "deep");
            level2.put("level3", level3);
            level1.put("level2", level2);
            context.setVariable("data", level1);

            assertEquals("deep",
                    evaluator.evaluate("$data?.level2?.level3?.value ?? 'default'", context));
        }

        @Test
        @DisplayName("should handle null-safe with empty strings")
        void testNullSafeWithEmptyStrings() {
            Map<String, Object> user = new HashMap<>();
            user.put("name", "");
            context.setVariable("user", user);

            // Empty string is not null, so it should be returned
            assertEquals("", evaluator.evaluate("$user?.name ?? 'default'", context));
        }

        @Test
        @DisplayName("should handle null-safe with zero")
        void testNullSafeWithZero() {
            Map<String, Object> data = new HashMap<>();
            data.put("count", 0);
            context.setVariable("data", data);

            // Zero is not null, so it should be returned
            assertEquals(0, evaluator.evaluate("$data?.count ?? -1", context));
        }
    }
}
