package com.expresso.context.functions;

import com.expresso.ExpressionEvaluator;
import com.expresso.context.Context;
import com.expresso.exception.EvaluationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for Collection functions covering edge cases, null
 * safety, and complex operations.
 */
@DisplayName("Collection Functions - Comprehensive Tests")
class CollectionFunctionsComprehensiveTest {

    private ExpressionEvaluator evaluator;
    private Context context;

    @BeforeEach
    void setUp() {
        evaluator = new ExpressionEvaluator();
        context = new Context();
    }

    @Nested
    @DisplayName("size Function")
    class SizeTests {

        @Test
        @DisplayName("should return size of list")
        void testListSize() {
            context.setVariable("list", Arrays.asList(1, 2, 3, 4, 5));
            assertEquals(5, evaluator.evaluate("size($list)", context));
        }

        @Test
        @DisplayName("should return 0 for empty list")
        void testEmptyList() {
            context.setVariable("list", Collections.emptyList());
            assertEquals(0, evaluator.evaluate("size($list)", context));
        }

        @Test
        @DisplayName("should return 0 for null")
        void testNull() {
            context.setVariable("list", null);
            assertEquals(0, evaluator.evaluate("size($list)", context));
        }

        @Test
        @DisplayName("should return length of string")
        void testStringSize() {
            context.setVariable("str", "hello");
            assertEquals(5, evaluator.evaluate("size($str)", context));
        }

        @Test
        @DisplayName("should return size of array")
        void testArraySize() {
            context.setVariable("arr", new int[] { 1, 2, 3 });
            assertEquals(3, evaluator.evaluate("size($arr)", context));
        }
    }

    @Nested
    @DisplayName("sum Function")
    class SumTests {

        @Test
        @DisplayName("should sum integers")
        void testSumIntegers() {
            context.setVariable("nums", Arrays.asList(1, 2, 3, 4, 5));
            assertEquals(15.0, (Double) evaluator.evaluate("sum($nums)", context), 0.001);
        }

        @Test
        @DisplayName("should sum decimals")
        void testSumDecimals() {
            context.setVariable("nums", Arrays.asList(1.5, 2.5, 3.0));
            assertEquals(7.0, (Double) evaluator.evaluate("sum($nums)", context), 0.001);
        }

        @Test
        @DisplayName("should return 0 for empty list")
        void testEmptyList() {
            context.setVariable("nums", Collections.emptyList());
            assertEquals(0.0, (Double) evaluator.evaluate("sum($nums)", context), 0.001);
        }

        @Test
        @DisplayName("should return 0 for null")
        void testNull() {
            context.setVariable("nums", null);
            assertEquals(0.0, (Double) evaluator.evaluate("sum($nums)", context), 0.001);
        }

        @Test
        @DisplayName("should handle negative numbers")
        void testNegativeNumbers() {
            context.setVariable("nums", Arrays.asList(-1, -2, -3));
            assertEquals(-6.0, (Double) evaluator.evaluate("sum($nums)", context), 0.001);
        }

        @Test
        @DisplayName("should handle mixed positive and negative")
        void testMixedNumbers() {
            context.setVariable("nums", Arrays.asList(10, -5, 3, -2));
            assertEquals(6.0, (Double) evaluator.evaluate("sum($nums)", context), 0.001);
        }

        @Test
        @DisplayName("should skip non-numeric values")
        void testMixedTypes() {
            context.setVariable("mixed", Arrays.asList(1, "hello", 2, null, 3));
            assertEquals(6.0, (Double) evaluator.evaluate("sum($mixed)", context), 0.001);
        }

        @Test
        @DisplayName("should sum array")
        void testArray() {
            context.setVariable("arr", new int[] { 1, 2, 3, 4 });
            assertEquals(10.0, (Double) evaluator.evaluate("sum($arr)", context), 0.001);
        }
    }

    @Nested
    @DisplayName("avg Function")
    class AvgTests {

        @Test
        @DisplayName("should calculate average of integers")
        void testAverageIntegers() {
            context.setVariable("nums", Arrays.asList(1, 2, 3, 4, 5));
            assertEquals(3.0, (Double) evaluator.evaluate("avg($nums)", context), 0.001);
        }

        @Test
        @DisplayName("should calculate average of decimals")
        void testAverageDecimals() {
            context.setVariable("nums", Arrays.asList(1.5, 2.5, 3.5));
            assertEquals(2.5, (Double) evaluator.evaluate("avg($nums)", context), 0.001);
        }

        @Test
        @DisplayName("should return 0 for empty list")
        void testEmptyList() {
            context.setVariable("nums", Collections.emptyList());
            assertEquals(0.0, (Double) evaluator.evaluate("avg($nums)", context), 0.001);
        }

        @Test
        @DisplayName("should return 0 for null")
        void testNull() {
            context.setVariable("nums", null);
            assertEquals(0.0, (Double) evaluator.evaluate("avg($nums)", context), 0.001);
        }

        @Test
        @DisplayName("should handle negative numbers")
        void testNegativeNumbers() {
            context.setVariable("nums", Arrays.asList(-10, -20, -30));
            assertEquals(-20.0, (Double) evaluator.evaluate("avg($nums)", context), 0.001);
        }

        @Test
        @DisplayName("should handle single element")
        void testSingleElement() {
            context.setVariable("nums", Arrays.asList(42));
            assertEquals(42.0, (Double) evaluator.evaluate("avg($nums)", context), 0.001);
        }
    }

    @Nested
    @DisplayName("sort Function")
    class SortTests {

        @Test
        @DisplayName("should sort integers in ascending order")
        void testSortIntegers() {
            context.setVariable("nums", Arrays.asList(5, 2, 8, 1, 9));
            List<?> result = (List<?>) evaluator.evaluate("sort($nums)", context);
            assertEquals(Arrays.asList(1, 2, 5, 8, 9), result);
        }

        @Test
        @DisplayName("should sort strings alphabetically")
        void testSortStrings() {
            context.setVariable("words", Arrays.asList("zebra", "apple", "banana"));
            List<?> result = (List<?>) evaluator.evaluate("sort($words)", context);
            assertEquals(Arrays.asList("apple", "banana", "zebra"), result);
        }

        @Test
        @DisplayName("should handle already sorted list")
        void testAlreadySorted() {
            context.setVariable("nums", Arrays.asList(1, 2, 3, 4, 5));
            List<?> result = (List<?>) evaluator.evaluate("sort($nums)", context);
            assertEquals(Arrays.asList(1, 2, 3, 4, 5), result);
        }

        @Test
        @DisplayName("should handle reverse sorted list")
        void testReverseSorted() {
            context.setVariable("nums", Arrays.asList(5, 4, 3, 2, 1));
            List<?> result = (List<?>) evaluator.evaluate("sort($nums)", context);
            assertEquals(Arrays.asList(1, 2, 3, 4, 5), result);
        }

        @Test
        @DisplayName("should handle empty list")
        void testEmptyList() {
            context.setVariable("nums", Collections.emptyList());
            List<?> result = (List<?>) evaluator.evaluate("sort($nums)", context);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("should return null for null input")
        void testNull() {
            context.setVariable("nums", null);
            assertNull(evaluator.evaluate("sort($nums)", context));
        }

        @Test
        @DisplayName("should handle list with duplicates")
        void testDuplicates() {
            context.setVariable("nums", Arrays.asList(3, 1, 2, 1, 3));
            List<?> result = (List<?>) evaluator.evaluate("sort($nums)", context);
            assertEquals(Arrays.asList(1, 1, 2, 3, 3), result);
        }

        @Test
        @DisplayName("should handle list with nulls")
        void testWithNulls() {
            context.setVariable("nums", Arrays.asList(3, null, 1, null, 2));
            List<?> result = (List<?>) evaluator.evaluate("sort($nums)", context);
            assertNotNull(result);
            assertEquals(5, result.size());
        }
    }

    @Nested
    @DisplayName("reverse Function")
    class ReverseTests {

        @Test
        @DisplayName("should reverse list")
        void testReverse() {
            context.setVariable("nums", Arrays.asList(1, 2, 3, 4, 5));
            List<?> result = (List<?>) evaluator.evaluate("reverse($nums)", context);
            assertEquals(Arrays.asList(5, 4, 3, 2, 1), result);
        }

        @Test
        @DisplayName("should handle single element")
        void testSingleElement() {
            context.setVariable("nums", Arrays.asList(42));
            List<?> result = (List<?>) evaluator.evaluate("reverse($nums)", context);
            assertEquals(Arrays.asList(42), result);
        }

        @Test
        @DisplayName("should handle empty list")
        void testEmptyList() {
            context.setVariable("nums", Collections.emptyList());
            List<?> result = (List<?>) evaluator.evaluate("reverse($nums)", context);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("should return null for null input")
        void testNull() {
            context.setVariable("nums", null);
            assertNull(evaluator.evaluate("reverse($nums)", context));
        }

        @Test
        @DisplayName("should handle strings")
        void testStrings() {
            context.setVariable("words", Arrays.asList("a", "b", "c"));
            List<?> result = (List<?>) evaluator.evaluate("reverse($words)", context);
            assertEquals(Arrays.asList("c", "b", "a"), result);
        }
    }

    @Nested
    @DisplayName("first Function")
    class FirstTests {

        @Test
        @DisplayName("should return first element of list")
        void testFirstElement() {
            context.setVariable("nums", Arrays.asList(1, 2, 3, 4, 5));
            assertEquals(1, evaluator.evaluate("first($nums)", context));
        }

        @Test
        @DisplayName("should return null for empty list")
        void testEmptyList() {
            context.setVariable("nums", Collections.emptyList());
            assertNull(evaluator.evaluate("first($nums)", context));
        }

        @Test
        @DisplayName("should return null for null input")
        void testNull() {
            context.setVariable("nums", null);
            assertNull(evaluator.evaluate("first($nums)", context));
        }

        @Test
        @DisplayName("should handle array")
        void testArray() {
            context.setVariable("arr", new int[] { 10, 20, 30 });
            assertEquals(10, evaluator.evaluate("first($arr)", context));
        }

        @Test
        @DisplayName("should handle string list")
        void testStrings() {
            context.setVariable("words", Arrays.asList("apple", "banana", "cherry"));
            assertEquals("apple", evaluator.evaluate("first($words)", context));
        }
    }

    @Nested
    @DisplayName("last Function")
    class LastTests {

        @Test
        @DisplayName("should return last element of list")
        void testLastElement() {
            context.setVariable("nums", Arrays.asList(1, 2, 3, 4, 5));
            assertEquals(5, evaluator.evaluate("last($nums)", context));
        }

        @Test
        @DisplayName("should return null for empty list")
        void testEmptyList() {
            context.setVariable("nums", Collections.emptyList());
            assertNull(evaluator.evaluate("last($nums)", context));
        }

        @Test
        @DisplayName("should return null for null input")
        void testNull() {
            context.setVariable("nums", null);
            assertNull(evaluator.evaluate("last($nums)", context));
        }

        @Test
        @DisplayName("should handle array")
        void testArray() {
            context.setVariable("arr", new int[] { 10, 20, 30 });
            assertEquals(30, evaluator.evaluate("last($arr)", context));
        }

        @Test
        @DisplayName("should handle string list")
        void testStrings() {
            context.setVariable("words", Arrays.asList("apple", "banana", "cherry"));
            assertEquals("cherry", evaluator.evaluate("last($words)", context));
        }

        @Test
        @DisplayName("should handle single element")
        void testSingleElement() {
            context.setVariable("nums", Arrays.asList(42));
            assertEquals(42, evaluator.evaluate("last($nums)", context));
        }
    }

    @Nested
    @DisplayName("subList Function")
    class SubListTests {

        @Test
        @DisplayName("should extract sublist")
        void testSubList() {
            context.setVariable("nums", Arrays.asList(1, 2, 3, 4, 5));
            List<?> result = (List<?>) evaluator.evaluate("subList($nums, 1, 3)", context);
            assertEquals(Arrays.asList(2, 3), result);
        }

        @Test
        @DisplayName("should extract from start to end")
        void testSubListToEnd() {
            context.setVariable("nums", Arrays.asList(1, 2, 3, 4, 5));
            List<?> result = (List<?>) evaluator.evaluate("subList($nums, 2)", context);
            assertEquals(Arrays.asList(3, 4, 5), result);
        }

        @Test
        @DisplayName("should handle start at 0")
        void testStartAtZero() {
            context.setVariable("nums", Arrays.asList(1, 2, 3, 4, 5));
            List<?> result = (List<?>) evaluator.evaluate("subList($nums, 0, 2)", context);
            assertEquals(Arrays.asList(1, 2), result);
        }

        @Test
        @DisplayName("should handle end beyond list size")
        void testEndBeyondSize() {
            context.setVariable("nums", Arrays.asList(1, 2, 3));
            List<?> result = (List<?>) evaluator.evaluate("subList($nums, 1, 100)", context);
            assertEquals(Arrays.asList(2, 3), result);
        }

        @Test
        @DisplayName("should handle start beyond list size")
        void testStartBeyondSize() {
            context.setVariable("nums", Arrays.asList(1, 2, 3));
            List<?> result = (List<?>) evaluator.evaluate("subList($nums, 10, 20)", context);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("should return empty for null input")
        void testNull() {
            context.setVariable("nums", null);
            List<?> result = (List<?>) evaluator.evaluate("subList($nums, 0, 5)", context);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("should handle empty list")
        void testEmptyList() {
            context.setVariable("nums", Collections.emptyList());
            List<?> result = (List<?>) evaluator.evaluate("subList($nums, 0, 5)", context);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("should handle negative start")
        void testNegativeStart() {
            context.setVariable("nums", Arrays.asList(1, 2, 3));
            List<?> result = (List<?>) evaluator.evaluate("subList($nums, -1, 2)", context);
            // Negative start should be treated as 0
            assertEquals(Arrays.asList(1, 2), result);
        }
    }

    @Nested
    @DisplayName("Complex Collection Operations")
    class ComplexCollectionTests {

        @Test
        @DisplayName("should combine multiple collection functions")
        void testCombinedFunctions() {
            context.setVariable("nums", Arrays.asList(5, 2, 8, 1, 9, 3));
            // Sort then get first element
            assertEquals(1, evaluator.evaluate("first(sort($nums))", context));
            // Sort then get last element
            assertEquals(9, evaluator.evaluate("last(sort($nums))", context));
        }

        @Test
        @DisplayName("should chain collection operations")
        void testChainedOperations() {
            context.setVariable("nums", Arrays.asList(1, 2, 3, 4, 5));
            // Reverse then get sublist
            List<?> result = (List<?>) evaluator.evaluate("subList(reverse($nums), 1, 3)", context);
            assertEquals(Arrays.asList(4, 3), result);
        }

        @Test
        @DisplayName("should use size in conditionals")
        void testSizeInConditional() {
            context.setVariable("nums", Arrays.asList(1, 2, 3));
            assertEquals("not empty",
                    evaluator.evaluate("size($nums) > 0 ? 'not empty' : 'empty'", context));
        }

        @Test
        @DisplayName("should handle nested collections")
        void testNestedCollections() {
            context.setVariable("nested", Arrays.asList(
                    Arrays.asList(1, 2),
                    Arrays.asList(3, 4),
                    Arrays.asList(5, 6)));
            assertEquals(3, evaluator.evaluate("size($nested)", context));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCasesTests {

        @Test
        @DisplayName("should handle very large lists")
        void testLargeList() {
            Integer[] largeArray = new Integer[10000];
            Arrays.fill(largeArray, 1);
            context.setVariable("large", Arrays.asList(largeArray));
            assertEquals(10000, evaluator.evaluate("size($large)", context));
            assertEquals(10000.0, (Double) evaluator.evaluate("sum($large)", context), 0.001);
        }

        @Test
        @DisplayName("should handle list with all nulls")
        void testAllNulls() {
            context.setVariable("nulls", Arrays.asList(null, null, null));
            assertEquals(3, evaluator.evaluate("size($nulls)", context));
            assertEquals(0.0, (Double) evaluator.evaluate("sum($nulls)", context), 0.001);
        }

        @Test
        @DisplayName("should handle list with single null")
        void testSingleNull() {
            context.setVariable("singleNull", Arrays.asList((Object) null));
            assertEquals(1, evaluator.evaluate("size($singleNull)", context));
            assertNull(evaluator.evaluate("first($singleNull)", context));
        }
    }
}
