package com.expresso.context.functions;

import com.expresso.ExpressionEvaluator;
import com.expresso.context.Context;
import com.expresso.exception.EvaluationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for String functions covering edge cases, error handling,
 * and null safety.
 */
@DisplayName("String Functions - Comprehensive Tests")
class StringFunctionsComprehensiveTest {

    private ExpressionEvaluator evaluator;
    private Context context;

    @BeforeEach
    void setUp() {
        evaluator = new ExpressionEvaluator();
        context = new Context();
    }

    @Nested
    @DisplayName("upperCase Function")
    class UpperCaseTests {

        @Test
        @DisplayName("should convert lowercase to uppercase")
        void testBasicUpperCase() {
            assertEquals("HELLO", evaluator.evaluate("upperCase('hello')", context));
        }

        @Test
        @DisplayName("should handle already uppercase strings")
        void testAlreadyUpperCase() {
            assertEquals("HELLO", evaluator.evaluate("upperCase('HELLO')", context));
        }

        @Test
        @DisplayName("should handle mixed case strings")
        void testMixedCase() {
            assertEquals("HELLO WORLD", evaluator.evaluate("upperCase('Hello World')", context));
        }

        @Test
        @DisplayName("should handle empty strings")
        void testEmptyString() {
            assertEquals("", evaluator.evaluate("upperCase('')", context));
        }

        @Test
        @DisplayName("should handle special characters")
        void testSpecialCharacters() {
            assertEquals("HELLO@WORLD#123!", evaluator.evaluate("upperCase('hello@world#123!')", context));
        }

        @Test
        @DisplayName("should handle Unicode characters")
        void testUnicodeCharacters() {
            assertEquals("CAFÉ", evaluator.evaluate("upperCase('café')", context));
            assertEquals("NAÏVE", evaluator.evaluate("upperCase('naïve')", context));
        }

        @Test
        @DisplayName("should throw exception for null input")
        void testNullInput() {
            assertThrows(EvaluationException.class,
                    () -> evaluator.evaluate("upperCase(null)", context));
        }

        @Test
        @DisplayName("should throw exception for non-string input")
        void testNonStringInput() {
            assertThrows(EvaluationException.class,
                    () -> evaluator.evaluate("upperCase(123)", context));
        }
    }

    @Nested
    @DisplayName("lowerCase Function")
    class LowerCaseTests {

        @Test
        @DisplayName("should convert uppercase to lowercase")
        void testBasicLowerCase() {
            assertEquals("hello", evaluator.evaluate("lowerCase('HELLO')", context));
        }

        @Test
        @DisplayName("should handle already lowercase strings")
        void testAlreadyLowerCase() {
            assertEquals("hello", evaluator.evaluate("lowerCase('hello')", context));
        }

        @Test
        @DisplayName("should handle mixed case strings")
        void testMixedCase() {
            assertEquals("hello world", evaluator.evaluate("lowerCase('Hello World')", context));
        }

        @Test
        @DisplayName("should handle empty strings")
        void testEmptyString() {
            assertEquals("", evaluator.evaluate("lowerCase('')", context));
        }

        @Test
        @DisplayName("should handle Unicode characters")
        void testUnicodeCharacters() {
            assertEquals("café", evaluator.evaluate("lowerCase('CAFÉ')", context));
        }
    }

    @Nested
    @DisplayName("trim Function")
    class TrimTests {

        @Test
        @DisplayName("should remove leading whitespace")
        void testLeadingWhitespace() {
            assertEquals("hello", evaluator.evaluate("trim('   hello')", context));
        }

        @Test
        @DisplayName("should remove trailing whitespace")
        void testTrailingWhitespace() {
            assertEquals("hello", evaluator.evaluate("trim('hello   ')", context));
        }

        @Test
        @DisplayName("should remove both leading and trailing whitespace")
        void testBothWhitespace() {
            assertEquals("hello", evaluator.evaluate("trim('   hello   ')", context));
        }

        @Test
        @DisplayName("should preserve internal whitespace")
        void testInternalWhitespace() {
            assertEquals("hello world", evaluator.evaluate("trim('  hello world  ')", context));
        }

        @Test
        @DisplayName("should handle empty strings")
        void testEmptyString() {
            assertEquals("", evaluator.evaluate("trim('')", context));
        }

        @Test
        @DisplayName("should handle whitespace-only strings")
        void testWhitespaceOnlyString() {
            assertEquals("", evaluator.evaluate("trim('     ')", context));
        }

        @Test
        @DisplayName("should handle tabs and newlines")
        void testTabsAndNewlines() {
            assertEquals("hello", evaluator.evaluate("trim('\\t\\nhello\\n\\t')", context));
        }
    }

    @Nested
    @DisplayName("length Function")
    class LengthTests {

        @Test
        @DisplayName("should return correct length for normal strings")
        void testNormalString() {
            assertEquals(5, evaluator.evaluate("length('hello')", context));
        }

        @Test
        @DisplayName("should return 0 for empty strings")
        void testEmptyString() {
            assertEquals(0, evaluator.evaluate("length('')", context));
        }

        @Test
        @DisplayName("should count whitespace characters")
        void testWhitespace() {
            assertEquals(5, evaluator.evaluate("length('  h  ')", context));
        }

        @Test
        @DisplayName("should count special characters")
        void testSpecialCharacters() {
            assertEquals(11, evaluator.evaluate("length('hello@world')", context));
        }

        @Test
        @DisplayName("should handle Unicode characters")
        void testUnicodeCharacters() {
            assertEquals(4, evaluator.evaluate("length('café')", context));
        }

        @Test
        @DisplayName("should handle emojis")
        void testEmojis() {
            // Note: emoji length may vary depending on encoding
            Object result = evaluator.evaluate("length('Hello 👋')", context);
            assertTrue(result instanceof Integer);
        }
    }

    @Nested
    @DisplayName("substring Function")
    class SubstringTests {

        @Test
        @DisplayName("should extract substring from start to end")
        void testBasicSubstring() {
            assertEquals("ell", evaluator.evaluate("substring('hello', 1, 3)", context));
        }

        @Test
        @DisplayName("should extract substring from start to end of string")
        void testSubstringToEnd() {
            assertEquals("llo", evaluator.evaluate("substring('hello', 2)", context));
        }

        @Test
        @DisplayName("should handle start at 0")
        void testStartAtZero() {
            assertEquals("hel", evaluator.evaluate("substring('hello', 0, 3)", context));
        }

        @Test
        @DisplayName("should handle length exceeding string length")
        void testLengthExceedingStringLength() {
            assertEquals("hello", evaluator.evaluate("substring('hello', 0, 100)", context));
        }

        @Test
        @DisplayName("should return empty string for start at end")
        void testStartAtEnd() {
            assertEquals("", evaluator.evaluate("substring('hello', 5)", context));
        }

        @Test
        @DisplayName("should return empty string for zero length")
        void testZeroLength() {
            assertEquals("", evaluator.evaluate("substring('hello', 0, 0)", context));
        }

        @Test
        @DisplayName("should handle empty input string")
        void testEmptyString() {
            assertEquals("", evaluator.evaluate("substring('', 0, 5)", context));
        }
    }

    @Nested
    @DisplayName("replace Function")
    class ReplaceTests {

        @Test
        @DisplayName("should replace single character")
        void testReplaceSingleChar() {
            assertEquals("hxllo", evaluator.evaluate("replace('hello', 'e', 'x')", context));
        }

        @Test
        @DisplayName("should replace multiple occurrences")
        void testReplaceMultiple() {
            assertEquals("hexxo", evaluator.evaluate("replace('hello', 'l', 'x')", context));
        }

        @Test
        @DisplayName("should replace word")
        void testReplaceWord() {
            assertEquals("hi world", evaluator.evaluate("replace('hello world', 'hello', 'hi')", context));
        }

        @Test
        @DisplayName("should handle no match")
        void testNoMatch() {
            assertEquals("hello", evaluator.evaluate("replace('hello', 'x', 'y')", context));
        }

        @Test
        @DisplayName("should replace with empty string")
        void testReplaceWithEmpty() {
            assertEquals("heo", evaluator.evaluate("replace('hello', 'l', '')", context));
        }

        @Test
        @DisplayName("should handle empty search string")
        void testEmptySearch() {
            // Replacing empty string typically inserts between every character
            Object result = evaluator.evaluate("replace('hi', '', 'x')", context);
            assertNotNull(result);
        }

        @Test
        @DisplayName("should handle case-sensitive replacement")
        void testCaseSensitive() {
            assertEquals("hello", evaluator.evaluate("replace('hello', 'H', 'J')", context));
            assertEquals("Jello", evaluator.evaluate("replace('Hello', 'H', 'J')", context));
        }
    }

    @Nested
    @DisplayName("contains Function")
    class ContainsTests {

        @Test
        @DisplayName("should return true when substring exists")
        void testContainsSubstring() {
            assertEquals(true, evaluator.evaluate("contains('hello world', 'world')", context));
        }

        @Test
        @DisplayName("should return false when substring does not exist")
        void testDoesNotContain() {
            assertEquals(false, evaluator.evaluate("contains('hello', 'world')", context));
        }

        @Test
        @DisplayName("should be case-sensitive")
        void testCaseSensitive() {
            assertEquals(false, evaluator.evaluate("contains('hello', 'HELLO')", context));
            assertEquals(true, evaluator.evaluate("contains('hello', 'hello')", context));
        }

        @Test
        @DisplayName("should handle empty search string")
        void testEmptySearch() {
            assertEquals(true, evaluator.evaluate("contains('hello', '')", context));
        }

        @Test
        @DisplayName("should handle empty target string")
        void testEmptyTarget() {
            assertEquals(false, evaluator.evaluate("contains('', 'hello')", context));
        }

        @Test
        @DisplayName("should work with collections")
        void testContainsInList() {
            context.setVariable("list", Arrays.asList(1, 2, 3, 4, 5));
            assertEquals(true, evaluator.evaluate("contains($list, 3)", context));
            assertEquals(false, evaluator.evaluate("contains($list, 10)", context));
        }

        @Test
        @DisplayName("should handle null in collections")
        void testNullInCollection() {
            context.setVariable("list", Arrays.asList(1, null, 3));
            assertEquals(true, evaluator.evaluate("contains($list, null)", context));
        }

        @Test
        @DisplayName("should return false for null container")
        void testNullContainer() {
            assertEquals(false, evaluator.evaluate("contains(null, 'test')", context));
        }
    }

    @Nested
    @DisplayName("startsWith Function")
    class StartsWithTests {

        @Test
        @DisplayName("should return true when string starts with prefix")
        void testStartsWithPrefix() {
            assertEquals(true, evaluator.evaluate("startsWith('hello', 'he')", context));
        }

        @Test
        @DisplayName("should return false when string does not start with prefix")
        void testDoesNotStartWith() {
            assertEquals(false, evaluator.evaluate("startsWith('hello', 'lo')", context));
        }

        @Test
        @DisplayName("should be case-sensitive")
        void testCaseSensitive() {
            assertEquals(false, evaluator.evaluate("startsWith('hello', 'He')", context));
        }

        @Test
        @DisplayName("should handle empty prefix")
        void testEmptyPrefix() {
            assertEquals(true, evaluator.evaluate("startsWith('hello', '')", context));
        }

        @Test
        @DisplayName("should handle prefix longer than string")
        void testLongPrefix() {
            assertEquals(false, evaluator.evaluate("startsWith('hi', 'hello')", context));
        }

        @Test
        @DisplayName("should handle identical strings")
        void testIdenticalStrings() {
            assertEquals(true, evaluator.evaluate("startsWith('hello', 'hello')", context));
        }
    }

    @Nested
    @DisplayName("endsWith Function")
    class EndsWithTests {

        @Test
        @DisplayName("should return true when string ends with suffix")
        void testEndsWithSuffix() {
            assertEquals(true, evaluator.evaluate("endsWith('hello', 'lo')", context));
        }

        @Test
        @DisplayName("should return false when string does not end with suffix")
        void testDoesNotEndWith() {
            assertEquals(false, evaluator.evaluate("endsWith('hello', 'he')", context));
        }

        @Test
        @DisplayName("should be case-sensitive")
        void testCaseSensitive() {
            assertEquals(false, evaluator.evaluate("endsWith('hello', 'LO')", context));
        }

        @Test
        @DisplayName("should handle empty suffix")
        void testEmptySuffix() {
            assertEquals(true, evaluator.evaluate("endsWith('hello', '')", context));
        }

        @Test
        @DisplayName("should handle suffix longer than string")
        void testLongSuffix() {
            assertEquals(false, evaluator.evaluate("endsWith('hi', 'hello')", context));
        }
    }

    @Nested
    @DisplayName("split Function")
    class SplitTests {

        @Test
        @DisplayName("should split by comma")
        void testSplitByComma() {
            List<?> result = (List<?>) evaluator.evaluate("split('a,b,c', ',')", context);
            assertEquals(Arrays.asList("a", "b", "c"), result);
        }

        @Test
        @DisplayName("should split by space")
        void testSplitBySpace() {
            List<?> result = (List<?>) evaluator.evaluate("split('hello world test', ' ')", context);
            assertEquals(Arrays.asList("hello", "world", "test"), result);
        }

        @Test
        @DisplayName("should split by regex pattern")
        void testSplitByRegex() {
            List<?> result = (List<?>) evaluator.evaluate("split('a1b2c3', '\\\\d')", context);
            assertEquals(Arrays.asList("a", "b", "c"), result);
        }

        @Test
        @DisplayName("should handle no delimiter in string")
        void testNoDelimiter() {
            List<?> result = (List<?>) evaluator.evaluate("split('hello', ',')", context);
            assertEquals(Arrays.asList("hello"), result);
        }

        @Test
        @DisplayName("should handle empty string")
        void testEmptyString() {
            List<?> result = (List<?>) evaluator.evaluate("split('', ',')", context);
            assertEquals(Arrays.asList(""), result);
        }

        @Test
        @DisplayName("should handle consecutive delimiters")
        void testConsecutiveDelimiters() {
            List<?> result = (List<?>) evaluator.evaluate("split('a,,b', ',')", context);
            assertEquals(Arrays.asList("a", "", "b"), result);
        }
    }

    @Nested
    @DisplayName("join Function")
    class JoinTests {

        @Test
        @DisplayName("should join with comma")
        void testJoinWithComma() {
            context.setVariable("list", Arrays.asList("a", "b", "c"));
            assertEquals("a,b,c", evaluator.evaluate("join(',', $list)", context));
        }

        @Test
        @DisplayName("should join with space")
        void testJoinWithSpace() {
            context.setVariable("list", Arrays.asList("hello", "world"));
            assertEquals("hello world", evaluator.evaluate("join(' ', $list)", context));
        }

        @Test
        @DisplayName("should join with empty delimiter")
        void testJoinWithEmpty() {
            context.setVariable("list", Arrays.asList("a", "b", "c"));
            assertEquals("abc", evaluator.evaluate("join('', $list)", context));
        }

        @Test
        @DisplayName("should handle empty list")
        void testEmptyList() {
            context.setVariable("list", Arrays.asList());
            assertEquals("", evaluator.evaluate("join(',', $list)", context));
        }

        @Test
        @DisplayName("should handle single element list")
        void testSingleElement() {
            context.setVariable("list", Arrays.asList("hello"));
            assertEquals("hello", evaluator.evaluate("join(',', $list)", context));
        }

        @Test
        @DisplayName("should convert numbers to strings")
        void testJoinNumbers() {
            context.setVariable("list", Arrays.asList(1, 2, 3));
            assertEquals("1,2,3", evaluator.evaluate("join(',', $list)", context));
        }
    }

    @Nested
    @DisplayName("charAt Function")
    class CharAtTests {

        @Test
        @DisplayName("should return character at index")
        void testCharAtIndex() {
            assertEquals("e", evaluator.evaluate("charAt('hello', 1)", context));
        }

        @Test
        @DisplayName("should return first character")
        void testFirstChar() {
            assertEquals("h", evaluator.evaluate("charAt('hello', 0)", context));
        }

        @Test
        @DisplayName("should return last character")
        void testLastChar() {
            assertEquals("o", evaluator.evaluate("charAt('hello', 4)", context));
        }

        @Test
        @DisplayName("should throw exception for index out of bounds")
        void testIndexOutOfBounds() {
            assertThrows(EvaluationException.class,
                    () -> evaluator.evaluate("charAt('hello', 10)", context));
        }

        @Test
        @DisplayName("should throw exception for negative index")
        void testNegativeIndex() {
            assertThrows(EvaluationException.class,
                    () -> evaluator.evaluate("charAt('hello', -1)", context));
        }
    }

    @Nested
    @DisplayName("indexOf Function")
    class IndexOfTests {

        @Test
        @DisplayName("should return index of substring")
        void testIndexOfSubstring() {
            assertEquals(1, evaluator.evaluate("indexOf('hello', 'el')", context));
        }

        @Test
        @DisplayName("should return 0 for substring at start")
        void testIndexOfAtStart() {
            assertEquals(0, evaluator.evaluate("indexOf('hello', 'he')", context));
        }

        @Test
        @DisplayName("should return -1 when substring not found")
        void testNotFound() {
            assertEquals(-1, evaluator.evaluate("indexOf('hello', 'world')", context));
        }

        @Test
        @DisplayName("should be case-sensitive")
        void testCaseSensitive() {
            assertEquals(-1, evaluator.evaluate("indexOf('hello', 'HE')", context));
        }

        @Test
        @DisplayName("should handle empty search string")
        void testEmptySearch() {
            assertEquals(0, evaluator.evaluate("indexOf('hello', '')", context));
        }

        @Test
        @DisplayName("should return first occurrence index")
        void testFirstOccurrence() {
            assertEquals(2, evaluator.evaluate("indexOf('hello', 'l')", context));
        }
    }

    @Nested
    @DisplayName("Complex String Operations")
    class ComplexStringTests {

        @Test
        @DisplayName("should chain multiple string functions")
        void testChainedFunctions() {
            assertEquals("HELLO",
                    evaluator.evaluate("upperCase(trim('  hello  '))", context));
        }

        @Test
        @DisplayName("should combine string functions with operators")
        void testStringOperations() {
            context.setVariable("firstName", "John");
            context.setVariable("lastName", "Doe");
            assertEquals("JOHN DOE",
                    evaluator.evaluate("upperCase($firstName + ' ' + $lastName)", context));
        }

        @Test
        @DisplayName("should use string functions with conditionals")
        void testStringWithConditionals() {
            context.setVariable("name", "alice");
            assertEquals("ALICE",
                    evaluator.evaluate("length($name) > 0 ? upperCase($name) : 'empty'", context));
        }
    }
}
