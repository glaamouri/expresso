package com.expresso.context.functions;

import com.expresso.context.Context;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Provider for date manipulation functions.
 */
public class DateFunctions implements FunctionProvider {

    @Override
    public void registerFunctions(Context context) {
        // Current date/time functions
        context.registerFunction("currentDate", args -> LocalDate.now());
        context.registerFunction("currentTime", args -> LocalTime.now());
        context.registerFunction("currentDateTime", args -> LocalDateTime.now());

        // Format functions
        context.registerFunction("formatDate", args -> {
            LocalDate date = (LocalDate) args[0];
            String pattern = (String) args[1];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return date.format(formatter);
        });

        context.registerFunction("format", args -> {
            LocalDate date = (LocalDate) args[0];
            String pattern = (String) args[1];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return date.format(formatter);
        });

        // Parse functions
        context.registerFunction("parseDate", args -> {
            String dateString = (String) args[0];
            String pattern = args.length > 1 ? (String) args[1] : "yyyy-MM-dd";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return LocalDate.parse(dateString, formatter);
        });

        context.registerFunction("parseDateTime", args -> {
            String dateTimeString = (String) args[0];
            String pattern = args.length > 1 ? (String) args[1] : "yyyy-MM-dd'T'HH:mm:ss";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return LocalDateTime.parse(dateTimeString, formatter);
        });

        // Date comparison functions
        context.registerFunction("isDateBefore", args -> {
            LocalDate date1 = (LocalDate) args[0];
            LocalDate date2 = (LocalDate) args[1];
            return date1.isBefore(date2);
        });

        context.registerFunction("isDateAfter", args -> {
            LocalDate date1 = (LocalDate) args[0];
            LocalDate date2 = (LocalDate) args[1];
            return date1.isAfter(date2);
        });

        context.registerFunction("daysBetween", args -> {
            LocalDate date1 = (LocalDate) args[0];
            LocalDate date2 = (LocalDate) args[1];
            return ChronoUnit.DAYS.between(date1, date2);
        });

        // Legacy function names
        context.registerFunction("now", args -> LocalDate.now());

        context.registerFunction("addDays", args -> {
            LocalDate date = (LocalDate) args[0];
            int days = ((Number) args[1]).intValue();
            return date.plusDays(days);
        });

        context.registerFunction("dateDiff", args -> {
            LocalDate date1 = (LocalDate) args[0];
            LocalDate date2 = (LocalDate) args[1];
            return ChronoUnit.DAYS.between(date1, date2);
        });

        context.registerFunction("addMonths", args -> {
            LocalDate date = (LocalDate) args[0];
            int months = ((Number) args[1]).intValue();
            return date.plusMonths(months);
        });

        context.registerFunction("addYears", args -> {
            LocalDate date = (LocalDate) args[0];
            int years = ((Number) args[1]).intValue();
            return date.plusYears(years);
        });

        // Date component extraction
        context.registerFunction("year", args -> ((LocalDate) args[0]).getYear());
        context.registerFunction("month", args -> ((LocalDate) args[0]).getMonthValue());
        context.registerFunction("dayOfMonth", args -> ((LocalDate) args[0]).getDayOfMonth());

        // GetDate* functions for consistency with documentation
        context.registerFunction("getYear", args -> ((LocalDate) args[0]).getYear());
        context.registerFunction("getMonth", args -> ((LocalDate) args[0]).getMonthValue());
        context.registerFunction("getDayOfMonth", args -> ((LocalDate) args[0]).getDayOfMonth());
    }

    @Override
    public List<FunctionInfo> getFunctionInfo() {
        return List.of(
                FunctionInfo.builder("currentDate")
                        .description("Returns the current date")
                        .returnType("LocalDate")
                        .build(),

                FunctionInfo.builder("currentTime")
                        .description("Returns the current time")
                        .returnType("LocalTime")
                        .build(),

                FunctionInfo.builder("currentDateTime")
                        .description("Returns the current date and time")
                        .returnType("LocalDateTime")
                        .build(),

                FunctionInfo.builder("formatDate")
                        .description("Formats a date using the specified pattern")
                        .parameter("date", "LocalDate", "The date to format")
                        .parameter("pattern", "String", "The format pattern (e.g., 'yyyy-MM-dd')")
                        .returnType("String")
                        .build(),

                FunctionInfo.builder("format")
                        .description("Formats a date using the specified pattern")
                        .parameter("date", "LocalDate", "The date to format")
                        .parameter("pattern", "String", "The format pattern (e.g., 'yyyy-MM-dd')")
                        .returnType("String")
                        .build(),

                FunctionInfo.builder("parseDate")
                        .description("Parses a date string using the specified pattern")
                        .parameter("dateString", "String", "The date string to parse")
                        .parameter("pattern", "String", "The format pattern (optional, default: 'yyyy-MM-dd')")
                        .returnType("LocalDate")
                        .build(),

                FunctionInfo.builder("parseDateTime")
                        .description("Parses a date-time string using the specified pattern")
                        .parameter("dateTimeString", "String", "The date-time string to parse")
                        .parameter("pattern", "String",
                                "The format pattern (optional, default: 'yyyy-MM-dd'T'HH:mm:ss')")
                        .returnType("LocalDateTime")
                        .build(),

                FunctionInfo.builder("isDateBefore")
                        .description("Checks if first date is before second date")
                        .parameter("date1", "LocalDate", "First date")
                        .parameter("date2", "LocalDate", "Second date")
                        .returnType("Boolean")
                        .build(),

                FunctionInfo.builder("isDateAfter")
                        .description("Checks if first date is after second date")
                        .parameter("date1", "LocalDate", "First date")
                        .parameter("date2", "LocalDate", "Second date")
                        .returnType("Boolean")
                        .build(),

                FunctionInfo.builder("daysBetween")
                        .description("Returns the number of days between two dates")
                        .parameter("date1", "LocalDate", "Start date")
                        .parameter("date2", "LocalDate", "End date")
                        .returnType("Long")
                        .build(),

                FunctionInfo.builder("now")
                        .description("Returns the current date")
                        .returnType("LocalDate")
                        .build(),

                FunctionInfo.builder("addDays")
                        .description("Adds the specified number of days to a date")
                        .parameter("date", "LocalDate", "The date")
                        .parameter("days", "Integer", "Number of days to add")
                        .returnType("LocalDate")
                        .build(),

                FunctionInfo.builder("dateDiff")
                        .description("Returns the number of days between two dates")
                        .parameter("date1", "LocalDate", "Start date")
                        .parameter("date2", "LocalDate", "End date")
                        .returnType("Long")
                        .build(),

                FunctionInfo.builder("addMonths")
                        .description("Adds the specified number of months to a date")
                        .parameter("date", "LocalDate", "The date")
                        .parameter("months", "Integer", "Number of months to add")
                        .returnType("LocalDate")
                        .build(),

                FunctionInfo.builder("addYears")
                        .description("Adds the specified number of years to a date")
                        .parameter("date", "LocalDate", "The date")
                        .parameter("years", "Integer", "Number of years to add")
                        .returnType("LocalDate")
                        .build(),

                FunctionInfo.builder("year")
                        .description("Extracts the year from a date")
                        .parameter("date", "LocalDate", "The date")
                        .returnType("Integer")
                        .build(),

                FunctionInfo.builder("month")
                        .description("Extracts the month from a date")
                        .parameter("date", "LocalDate", "The date")
                        .returnType("Integer")
                        .build(),

                FunctionInfo.builder("dayOfMonth")
                        .description("Extracts the day of month from a date")
                        .parameter("date", "LocalDate", "The date")
                        .returnType("Integer")
                        .build(),

                FunctionInfo.builder("getYear")
                        .description("Extracts the year from a date")
                        .parameter("date", "LocalDate", "The date")
                        .returnType("Integer")
                        .build(),

                FunctionInfo.builder("getMonth")
                        .description("Extracts the month from a date")
                        .parameter("date", "LocalDate", "The date")
                        .returnType("Integer")
                        .build(),

                FunctionInfo.builder("getDayOfMonth")
                        .description("Extracts the day of month from a date")
                        .parameter("date", "LocalDate", "The date")
                        .returnType("Integer")
                        .build());
    }
}