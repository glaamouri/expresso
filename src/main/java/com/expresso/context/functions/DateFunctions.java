package com.expresso.context.functions;

import com.expresso.context.Context;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

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
} 