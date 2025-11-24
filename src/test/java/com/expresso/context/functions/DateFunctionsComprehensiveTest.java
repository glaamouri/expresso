package com.expresso.context.functions;

import com.expresso.ExpressionEvaluator;
import com.expresso.context.Context;
import com.expresso.exception.EvaluationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for Date functions covering parsing, formatting,
 * arithmetic, and comparisons.
 */
@DisplayName("Date Functions - Comprehensive Tests")
class DateFunctionsComprehensiveTest {

    private ExpressionEvaluator evaluator;
    private Context context;

    @BeforeEach
    void setUp() {
        evaluator = new ExpressionEvaluator();
        context = new Context();
    }

    @Nested
    @DisplayName("Current Date/Time Functions")
    class CurrentDateTimeTests {

        @Test
        @DisplayName("currentDate should return today's date")
        void testCurrentDate() {
            LocalDate result = (LocalDate) evaluator.evaluate("currentDate()", context);
            assertEquals(LocalDate.now(), result);
        }

        @Test
        @DisplayName("currentTime should return current time")
        void testCurrentTime() {
            LocalTime result = (LocalTime) evaluator.evaluate("currentTime()", context);
            assertNotNull(result);
            assertTrue(result instanceof LocalTime);
        }

        @Test
        @DisplayName("currentDateTime should return current date and time")
        void testCurrentDateTime() {
            LocalDateTime result = (LocalDateTime) evaluator.evaluate("currentDateTime()", context);
            assertNotNull(result);
            assertTrue(result instanceof LocalDateTime);
        }
    }

    @Nested
    @DisplayName("parseDate Function")
    class ParseDateTests {

        @Test
        @DisplayName("should parse date with default format")
        void testDefaultFormat() {
            LocalDate result = (LocalDate) evaluator.evaluate("parseDate('2023-05-15')", context);
            assertEquals(LocalDate.of(2023, 5, 15), result);
        }

        @Test
        @DisplayName("should parse date with custom format")
        void testCustomFormat() {
            LocalDate result = (LocalDate) evaluator.evaluate("parseDate('15/05/2023', 'dd/MM/yyyy')", context);
            assertEquals(LocalDate.of(2023, 5, 15), result);
        }

        @Test
        @DisplayName("should parse date with various formats")
        void testVariousFormats() {
            LocalDate expected = LocalDate.of(2023, 12, 25);

            LocalDate result1 = (LocalDate) evaluator.evaluate("parseDate('2023-12-25', 'yyyy-MM-dd')", context);
            assertEquals(expected, result1);

            LocalDate result2 = (LocalDate) evaluator.evaluate("parseDate('25-12-2023', 'dd-MM-yyyy')", context);
            assertEquals(expected, result2);
        }

        @Test
        @DisplayName("should throw exception for invalid date string")
        void testInvalidDateString() {
            assertThrows(EvaluationException.class,
                    () -> evaluator.evaluate("parseDate('invalid')", context));
        }

        @Test
        @DisplayName("should throw exception for mismatched format")
        void testMismatchedFormat() {
            assertThrows(EvaluationException.class,
                    () -> evaluator.evaluate("parseDate('2023-05-15', 'dd/MM/yyyy')", context));
        }
    }

    @Nested
    @DisplayName("parseDateTime Function")
    class ParseDateTimeTests {

        @Test
        @DisplayName("should parse datetime with default format")
        void testDefaultFormat() {
            LocalDateTime result = (LocalDateTime) evaluator.evaluate(
                    "parseDateTime('2023-05-15T14:30:00')", context);
            assertEquals(LocalDateTime.of(2023, 5, 15, 14, 30, 0), result);
        }

        @Test
        @DisplayName("should parse datetime with custom format")
        void testCustomFormat() {
            LocalDateTime result = (LocalDateTime) evaluator.evaluate(
                    "parseDateTime('15/05/2023 14:30:00', 'dd/MM/yyyy HH:mm:ss')", context);
            assertEquals(LocalDateTime.of(2023, 5, 15, 14, 30, 0), result);
        }
    }

    @Nested
    @DisplayName("formatDate Function")
    class FormatDateTests {

        @Test
        @DisplayName("should format date with pattern")
        void testFormatDate() {
            LocalDate date = LocalDate.of(2023, 5, 15);
            context.setVariable("date", date);

            assertEquals("2023-05-15",
                    evaluator.evaluate("formatDate($date, 'yyyy-MM-dd')", context));
        }

        @Test
        @DisplayName("should format with various patterns")
        void testVariousPatterns() {
            LocalDate date = LocalDate.of(2023, 12, 25);
            context.setVariable("date", date);

            assertEquals("25/12/2023",
                    evaluator.evaluate("formatDate($date, 'dd/MM/yyyy')", context));
            assertEquals("2023-12-25",
                    evaluator.evaluate("formatDate($date, 'yyyy-MM-dd')", context));
            assertEquals("25-Dec-2023",
                    evaluator.evaluate("formatDate($date, 'dd-MMM-yyyy')", context));
        }
    }

    @Nested
    @DisplayName("addDays Function")
    class AddDaysTests {

        @Test
        @DisplayName("should add positive days")
        void testAddPositiveDays() {
            LocalDate date = LocalDate.of(2023, 5, 15);
            context.setVariable("date", date);

            LocalDate result = (LocalDate) evaluator.evaluate("addDays($date, 10)", context);
            assertEquals(LocalDate.of(2023, 5, 25), result);
        }

        @Test
        @DisplayName("should add negative days (subtract)")
        void testAddNegativeDays() {
            LocalDate date = LocalDate.of(2023, 5, 15);
            context.setVariable("date", date);

            LocalDate result = (LocalDate) evaluator.evaluate("addDays($date, -10)", context);
            assertEquals(LocalDate.of(2023, 5, 5), result);
        }

        @Test
        @DisplayName("should handle month boundary")
        void testMonthBoundary() {
            LocalDate date = LocalDate.of(2023, 5, 25);
            context.setVariable("date", date);

            LocalDate result = (LocalDate) evaluator.evaluate("addDays($date, 10)", context);
            assertEquals(LocalDate.of(2023, 6, 4), result);
        }

        @Test
        @DisplayName("should handle year boundary")
        void testYearBoundary() {
            LocalDate date = LocalDate.of(2023, 12, 25);
            context.setVariable("date", date);

            LocalDate result = (LocalDate) evaluator.evaluate("addDays($date, 10)", context);
            assertEquals(LocalDate.of(2024, 1, 4), result);
        }

        @Test
        @DisplayName("should handle leap year")
        void testLeapYear() {
            LocalDate date = LocalDate.of(2024, 2, 28);
            context.setVariable("date", date);

            LocalDate result = (LocalDate) evaluator.evaluate("addDays($date, 1)", context);
            assertEquals(LocalDate.of(2024, 2, 29), result);
        }
    }

    @Nested
    @DisplayName("addMonths Function")
    class AddMonthsTests {

        @Test
        @DisplayName("should add positive months")
        void testAddPositiveMonths() {
            LocalDate date = LocalDate.of(2023, 5, 15);
            context.setVariable("date", date);

            LocalDate result = (LocalDate) evaluator.evaluate("addMonths($date, 3)", context);
            assertEquals(LocalDate.of(2023, 8, 15), result);
        }

        @Test
        @DisplayName("should add negative months (subtract)")
        void testAddNegativeMonths() {
            LocalDate date = LocalDate.of(2023, 5, 15);
            context.setVariable("date", date);

            LocalDate result = (LocalDate) evaluator.evaluate("addMonths($date, -2)", context);
            assertEquals(LocalDate.of(2023, 3, 15), result);
        }

        @Test
        @DisplayName("should handle year boundary")
        void testYearBoundary() {
            LocalDate date = LocalDate.of(2023, 11, 15);
            context.setVariable("date", date);

            LocalDate result = (LocalDate) evaluator.evaluate("addMonths($date, 3)", context);
            assertEquals(LocalDate.of(2024, 2, 15), result);
        }
    }

    @Nested
    @DisplayName("addYears Function")
    class AddYearsTests {

        @Test
        @DisplayName("should add positive years")
        void testAddPositiveYears() {
            LocalDate date = LocalDate.of(2023, 5, 15);
            context.setVariable("date", date);

            LocalDate result = (LocalDate) evaluator.evaluate("addYears($date, 5)", context);
            assertEquals(LocalDate.of(2028, 5, 15), result);
        }

        @Test
        @DisplayName("should add negative years (subtract)")
        void testAddNegativeYears() {
            LocalDate date = LocalDate.of(2023, 5, 15);
            context.setVariable("date", date);

            LocalDate result = (LocalDate) evaluator.evaluate("addYears($date, -3)", context);
            assertEquals(LocalDate.of(2020, 5, 15), result);
        }
    }

    @Nested
    @DisplayName("daysBetween Function")
    class DaysBetweenTests {

        @Test
        @DisplayName("should calculate days between dates")
        void testDaysBetween() {
            LocalDate date1 = LocalDate.of(2023, 1, 1);
            LocalDate date2 = LocalDate.of(2023, 1, 15);
            context.setVariable("date1", date1);
            context.setVariable("date2", date2);

            assertEquals(14L, evaluator.evaluate("daysBetween($date1, $date2)", context));
        }

        @Test
        @DisplayName("should return negative for reversed dates")
        void testReversedDates() {
            LocalDate date1 = LocalDate.of(2023, 1, 15);
            LocalDate date2 = LocalDate.of(2023, 1, 1);
            context.setVariable("date1", date1);
            context.setVariable("date2", date2);

            assertEquals(-14L, evaluator.evaluate("daysBetween($date1, $date2)", context));
        }

        @Test
        @DisplayName("should return 0 for same date")
        void testSameDate() {
            LocalDate date = LocalDate.of(2023, 1, 1);
            context.setVariable("date", date);

            assertEquals(0L, evaluator.evaluate("daysBetween($date, $date)", context));
        }
    }

    @Nested
    @DisplayName("Date Comparison Functions")
    class DateComparisonTests {

        @Test
        @DisplayName("isDateBefore should return true for earlier date")
        void testIsDateBefore() {
            LocalDate date1 = LocalDate.of(2023, 1, 1);
            LocalDate date2 = LocalDate.of(2023, 12, 31);
            context.setVariable("date1", date1);
            context.setVariable("date2", date2);

            assertEquals(true, evaluator.evaluate("isDateBefore($date1, $date2)", context));
        }

        @Test
        @DisplayName("isDateBefore should return false for later date")
        void testIsNotDateBefore() {
            LocalDate date1 = LocalDate.of(2023, 12, 31);
            LocalDate date2 = LocalDate.of(2023, 1, 1);
            context.setVariable("date1", date1);
            context.setVariable("date2", date2);

            assertEquals(false, evaluator.evaluate("isDateBefore($date1, $date2)", context));
        }

        @Test
        @DisplayName("isDateAfter should return true for later date")
        void testIsDateAfter() {
            LocalDate date1 = LocalDate.of(2023, 12, 31);
            LocalDate date2 = LocalDate.of(2023, 1, 1);
            context.setVariable("date1", date1);
            context.setVariable("date2", date2);

            assertEquals(true, evaluator.evaluate("isDateAfter($date1, $date2)", context));
        }

        @Test
        @DisplayName("should compare dates with < operator")
        void testLessThanOperator() {
            LocalDate date1 = LocalDate.of(2023, 1, 1);
            LocalDate date2 = LocalDate.of(2023, 12, 31);
            context.setVariable("date1", date1);
            context.setVariable("date2", date2);

            assertEquals(true, evaluator.evaluate("$date1 < $date2", context));
        }
    }

    @Nested
    @DisplayName("Date Component Extraction")
    class DateComponentTests {

        @Test
        @DisplayName("should extract year")
        void testGetYear() {
            LocalDate date = LocalDate.of(2023, 5, 15);
            context.setVariable("date", date);

            assertEquals(2023, evaluator.evaluate("year($date)", context));
            assertEquals(2023, evaluator.evaluate("getYear($date)", context));
        }

        @Test
        @DisplayName("should extract month")
        void testGetMonth() {
            LocalDate date = LocalDate.of(2023, 5, 15);
            context.setVariable("date", date);

            assertEquals(5, evaluator.evaluate("month($date)", context));
            assertEquals(5, evaluator.evaluate("getMonth($date)", context));
        }

        @Test
        @DisplayName("should extract day of month")
        void testGetDayOfMonth() {
            LocalDate date = LocalDate.of(2023, 5, 15);
            context.setVariable("date", date);

            assertEquals(15, evaluator.evaluate("dayOfMonth($date)", context));
            assertEquals(15, evaluator.evaluate("getDayOfMonth($date)", context));
        }
    }

    @Nested
    @DisplayName("Complex Date Operations")
    class ComplexDateTests {

        @Test
        @DisplayName("should chain date operations")
        void testChainedOperations() {
            LocalDate date = LocalDate.of(2023, 1, 1);
            context.setVariable("date", date);

            // Add 1 year, then 1 month, then 1 day
            LocalDate result = (LocalDate) evaluator.evaluate(
                    "addDays(addMonths(addYears($date, 1), 1), 1)", context);
            assertEquals(LocalDate.of(2024, 2, 2), result);
        }

        @Test
        @DisplayName("should use dates in conditionals")
        void testDateInConditional() {
            LocalDate today = LocalDate.now();
            LocalDate tomorrow = today.plusDays(1);
            context.setVariable("today", today);
            context.setVariable("tomorrow", tomorrow);

            assertEquals("future",
                    evaluator.evaluate("$today < $tomorrow ? 'future' : 'past'", context));
        }

        @Test
        @DisplayName("should format and parse round-trip")
        void testFormatParseRoundTrip() {
            LocalDate original = LocalDate.of(2023, 5, 15);
            context.setVariable("date", original);

            String formatted = (String) evaluator.evaluate("formatDate($date, 'yyyy-MM-dd')", context);
            context.setVariable("formatted", formatted);

            LocalDate parsed = (LocalDate) evaluator.evaluate("parseDate($formatted)", context);
            assertEquals(original, parsed);
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCasesTests {

        @Test
        @DisplayName("should handle leap year day")
        void testLeapYearDay() {
            LocalDate leapDay = LocalDate.of(2024, 2, 29);
            context.setVariable("date", leapDay);

            assertEquals(29, evaluator.evaluate("getDayOfMonth($date)", context));
            assertEquals(2, evaluator.evaluate("getMonth($date)", context));
        }

        @Test
        @DisplayName("should handle end of year")
        void testEndOfYear() {
            LocalDate endOfYear = LocalDate.of(2023, 12, 31);
            context.setVariable("date", endOfYear);

            LocalDate nextDay = (LocalDate) evaluator.evaluate("addDays($date, 1)", context);
            assertEquals(LocalDate.of(2024, 1, 1), nextDay);
        }

        @Test
        @DisplayName("should handle very old dates")
        void testOldDates() {
            LocalDate oldDate = LocalDate.of(1900, 1, 1);
            context.setVariable("date", oldDate);

            assertEquals(1900, evaluator.evaluate("getYear($date)", context));
        }

        @Test
        @DisplayName("should handle far future dates")
        void testFutureDates() {
            LocalDate futureDate = LocalDate.of(2100, 12, 31);
            context.setVariable("date", futureDate);

            assertEquals(2100, evaluator.evaluate("getYear($date)", context));
        }
    }
}
