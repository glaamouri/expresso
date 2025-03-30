package com.expresso;

import com.expresso.context.Context;
import com.expresso.exception.EvaluationException;
import com.expresso.exception.PropertyNotFoundException;
import com.expresso.exception.SyntaxException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExpressionEvaluatorTest {
    private final ExpressionEvaluator evaluator = new ExpressionEvaluator();

    @Test
    void testSimpleLiterals() {
        Context context = new Context();

        assertEquals("Hello", evaluator.evaluate("\"Hello\"", context));
        assertEquals(42L, evaluator.evaluate("42", context));
        assertEquals(3.14, evaluator.evaluate("3.14", context));
        assertEquals(true, evaluator.evaluate("true", context));
        assertEquals(false, evaluator.evaluate("false", context));
    }

    @Test
    void testVariables() {
        Context context = new Context();
        context.setVariable("name", "Alice");
        context.setVariable("age", 25);
        context.setVariable("isActive", true);

        assertEquals("Alice", evaluator.evaluate("$name", context));
        assertEquals(25, evaluator.evaluate("$age", context));
        assertEquals(true, evaluator.evaluate("$isActive", context));
    }

    @Test
    void testFunctions() {
        Context context = new Context();

        // String functions
        assertEquals("HELLO", evaluator.evaluate("upperCase(\"hello\")", context));
        assertEquals("hello", evaluator.evaluate("lowerCase(\"HELLO\")", context));
        assertEquals(5, (Integer) evaluator.evaluate("length(\"hello\")", context));
        assertEquals("hello", evaluator.evaluate("trim(\"  hello  \")", context));

        // Math functions
        assertEquals(42.0, evaluator.evaluate("abs(-42)", context));
        assertEquals(43.0, evaluator.evaluate("ceil(42.1)", context));
        assertEquals(42.0, evaluator.evaluate("floor(42.9)", context));
        assertEquals(43L, evaluator.evaluate("round(42.5)", context));

        // Logic functions
        assertEquals(true, evaluator.evaluate("isNull(null)", context));
        assertEquals(false, evaluator.evaluate("isNull(\"test\")", context));
        assertEquals("default", evaluator.evaluate("coalesce(null, \"default\")", context));
        assertEquals("first", evaluator.evaluate("coalesce(\"first\", \"second\")", context));
    }

    @Test
    void testPropertyAccess() {
        Context context = new Context();
        Address address = new Address("Paris", "France", 75000);
        Person person = new Person("Alice", 25, address, Arrays.asList("reading", "gaming"));
        context.setVariable("person", person);

        // Basic property access
        assertEquals("Alice", evaluator.evaluate("$person.name", context));
        assertEquals(25, evaluator.evaluate("$person.age", context));

        // Nested property access
        assertEquals("Paris", evaluator.evaluate("$person.address.city", context));
        assertEquals("France", evaluator.evaluate("$person.address.country", context));
        assertEquals(75000, evaluator.evaluate("$person.address.zipCode", context));

        // List property access
        assertEquals("reading", evaluator.evaluate("$person.hobbies[0]", context));
        assertEquals("gaming", evaluator.evaluate("$person.hobbies[1]", context));
    }

    @Test
    void testComplexExpressions() {
        Context context = new Context();
        context.setVariable("user", Map.of(
                "name", "Alice",
                "age", 25,
                "scores", List.of(85, 92, 78),
                "address", Map.of(
                        "city", "Paris",
                        "country", "France"
                )
        ));

        // String operations
        assertEquals("ALICE", evaluator.evaluate("upperCase($user.name)", context));
        assertEquals("paris", evaluator.evaluate("lowerCase($user.address.city)", context));

        // Array access
        assertEquals(92, evaluator.evaluate("$user.scores[1]", context));

        // Nested map access
        assertEquals("Paris", evaluator.evaluate("$user.address.city", context));
        assertEquals("France", evaluator.evaluate("$user.address.country", context));
    }

    @Test
    void testEdgeCases() {
        Context context = new Context();

        // Empty strings
        assertEquals("", evaluator.evaluate("\"\"", context));
        assertEquals(0, evaluator.evaluate("length(\"\")", context));

        // Special characters in strings
        assertEquals("Hello\nWorld", evaluator.evaluate("\"Hello\\nWorld\"", context));
        assertEquals("Hello\tWorld", evaluator.evaluate("\"Hello\\tWorld\"", context));

        // Null handling
        assertEquals(true, evaluator.evaluate("isNull(null)", context));
        assertEquals("default", evaluator.evaluate("coalesce(null, \"default\")", context));

        // Negative numbers
        assertEquals(-42.0, evaluator.evaluate("-42", context));
        assertEquals(42.0, evaluator.evaluate("abs(-42)", context));

        // Decimal numbers
        assertEquals(3.14159, evaluator.evaluate("3.14159", context));
        assertEquals(4.0, evaluator.evaluate("ceil(3.14159)", context));
        assertEquals(3.0, evaluator.evaluate("floor(3.14159)", context));
    }

    @Test
    void testErrorCases() {
        Context context = new Context();

        // Invalid variable access
        assertThrows(PropertyNotFoundException.class, () ->
                evaluator.evaluate("$nonexistent", context));

        // Invalid array access
        context.setVariable("scores", List.of(1, 2, 3));
        assertThrows(PropertyNotFoundException.class, () ->
                evaluator.evaluate("$scores[5]", context));

        // Invalid function calls
        assertThrows(EvaluationException.class, () ->
                evaluator.evaluate("upperCase(42)", context));

        // Invalid property access
        context.setVariable("person", new Person("Alice", 25, null, null));
        assertThrows(PropertyNotFoundException.class, () ->
                evaluator.evaluate("$person.address.city", context));
    }

    @Test
    void testNullSafePropertyAccess() {
        Context context = new Context();

        // Create a person with null address
        Person personWithNullAddress = new Person("Alice", 25, null, List.of("reading"));
        context.setVariable("person", personWithNullAddress);

        // Test unsafe property access (should throw exception)
        assertThrows(PropertyNotFoundException.class, () ->
                evaluator.evaluate("$person.address.city", context));

        // Test null-safe property access with null coalescing
        assertEquals("Unknown", evaluator.evaluate("$person?.address?.city ?? 'Unknown'", context));

        // Test null-safe property access with nested null values
        assertEquals("Unknown", evaluator.evaluate("$person?.address?.country ?? 'Unknown'", context));

        // Test null-safe property access with non-null values
        Person personWithAddress = new Person("Bob", 30,
                new Address("Paris", "France", 75000),
                List.of("gaming"));
        context.setVariable("person", personWithAddress);

        assertEquals("Paris", evaluator.evaluate("$person?.address?.city ?? 'Unknown'", context));
        assertEquals("France", evaluator.evaluate("$person?.address?.country ?? 'Unknown'", context));

        // Test null-safe property access with null person
        context.setVariable("person", null);
        assertEquals("Unknown", evaluator.evaluate("$person?.address?.city ?? 'Unknown'", context));

        // Test null-safe property access with null list
        Person personWithNullHobbies = new Person("Charlie", 35,
                new Address("London", "UK", 10000),
                null);
        context.setVariable("person", personWithNullHobbies);

        assertThrows(PropertyNotFoundException.class, () ->
                evaluator.evaluate("$person.hobbies[0]", context));
        assertEquals("Unknown", evaluator.evaluate("$person?.hobbies?[0] ?? 'Unknown'", context));
    }

    @Test
    void testCustomFunctions() {
        Context context = new Context();

        // Test basic custom function
        evaluator.registerFunction("add", (args) -> {
            double x = ((Number) args[0]).doubleValue();
            double y = ((Number) args[1]).doubleValue();
            return x + y;
        });
        assertEquals(15.0, evaluator.evaluate("add(5, 10)", context));

        // Test custom function with string manipulation
        evaluator.registerFunction("greet", (args) -> {
            String name = (String) args[0];
            String time = (String) args[1];
            return "Good " + time + ", " + name + "!";
        });
        assertEquals("Good morning, Alice!",
                evaluator.evaluate("greet('Alice', 'morning')", context));

        // Test custom function with multiple arguments
        evaluator.registerFunction("calculate", (args) -> {
            double x = ((Number) args[0]).doubleValue();
            double y = ((Number) args[1]).doubleValue();
            double z = ((Number) args[2]).doubleValue();
            return (x + y) * z;
        });
        assertEquals(30.0, evaluator.evaluate("calculate(2, 3, 6)", context));

        // Test custom function with variable arguments
        evaluator.registerFunction("sum", (args) -> {
            double sum = 0;
            for (Object arg : args) {
                sum += ((Number) arg).doubleValue();
            }
            return sum;
        });
        assertEquals(15.0, evaluator.evaluate("sum(1, 2, 3, 4, 5)", context));

        // Test custom function with type conversion
        evaluator.registerFunction("concat", (args) -> {
            StringBuilder result = new StringBuilder();
            for (Object arg : args) {
                result.append(String.valueOf(arg));
            }
            return result.toString();
        });
        assertEquals("123true", evaluator.evaluate("concat(1, 2, 3, true)", context));

        // Test custom function with null handling
        evaluator.registerFunction("safeLength", (args) -> {
            String str = (String) args[0];
            return str != null ? str.length() : 0;
        });
        assertEquals(0, evaluator.evaluate("safeLength(null)", context));
        assertEquals(5, evaluator.evaluate("safeLength('hello')", context));

        // Test custom function with complex logic
        evaluator.registerFunction("grade", (args) -> {
            double score = ((Number) args[0]).doubleValue();
            if (score >= 90) return "A";
            if (score >= 80) return "B";
            if (score >= 70) return "C";
            if (score >= 60) return "D";
            return "F";
        });
        assertEquals("A", evaluator.evaluate("grade(95)", context));
        assertEquals("B", evaluator.evaluate("grade(85)", context));
        assertEquals("C", evaluator.evaluate("grade(75)", context));
        assertEquals("D", evaluator.evaluate("grade(65)", context));
        assertEquals("F", evaluator.evaluate("grade(55)", context));

        // Test custom function with context variables
        context.setVariable("x", 10);
        context.setVariable("y", 5);
        evaluator.registerFunction("multiply", (args) -> {
            double x = ((Number) args[0]).doubleValue();
            double y = ((Number) args[1]).doubleValue();
            return x * y;
        });
        assertEquals(50.0, evaluator.evaluate("multiply($x, $y)", context));

        // Test error handling for custom functions
        evaluator.registerFunction("divide", (args) -> {
            double x = ((Number) args[0]).doubleValue();
            double y = ((Number) args[1]).doubleValue();
            if (y == 0) throw new ArithmeticException("Division by zero");
            return x / y;
        });
        assertThrows(EvaluationException.class, () ->
                evaluator.evaluate("divide(10, 0)", context));
    }

    @Test
    void testDateFunctions() {
        Context context = new Context();
        
        // Set a known date for testing
        java.time.LocalDate testDate = java.time.LocalDate.of(2023, 3, 30);
        context.setVariable("date", testDate);
        
        // Test date formatting
        assertEquals("2023-03-30", evaluator.evaluate("format($date, 'yyyy-MM-dd')", context));
        assertEquals("30/03/2023", evaluator.evaluate("format($date, 'dd/MM/yyyy')", context));
        
        // Test adding days
        java.time.LocalDate expectedDate = testDate.plusDays(5);
        String expectedFormatted = expectedDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        assertEquals(expectedFormatted, evaluator.evaluate("format(addDays($date, 5), 'yyyy-MM-dd')", context));
        
        // Test parsing dates
        String dateStr = "2022-12-25";
        java.time.LocalDate parsedDate = java.time.LocalDate.parse(dateStr, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        assertEquals(parsedDate, evaluator.evaluate("parseDate('2022-12-25', 'yyyy-MM-dd')", context));
        
        // Test date difference
        context.setVariable("date2", java.time.LocalDate.of(2023, 4, 10));
        assertEquals(11L, evaluator.evaluate("dateDiff($date, $date2)", context));
        
        // Test now() function returns a LocalDate
        Object now = evaluator.evaluate("now()", context);
        assertTrue(now instanceof java.time.LocalDate);
    }

    @Test
    void testNestedFunctionCalls() {
        Context context = new Context();
        
        // Test nested string functions
        context.setVariable("name", "  John Doe  ");
        assertEquals("JOHN", evaluator.evaluate("upperCase(substring(trim($name), 0, 4))", context));
        
        // Test nested math functions
        context.setVariable("x", 3);
        context.setVariable("y", 4);
        assertEquals(5.0, evaluator.evaluate("round(sqrt(pow($x, 2) + pow($y, 2)), 0)", context));
        
        // Test nested date functions
        context.setVariable("startDate", LocalDate.of(2023, 1, 1));
        String result = evaluator.evaluate(
            "format(addDays(parseDate('2023-01-01', 'yyyy-MM-dd'), 5), 'yyyy-MM-dd')", 
            context
        ).toString();
        assertEquals("2023-01-06", result);
    }

    @Test
    void testArithmeticOperations() {
        Context context = new Context();
        
        // Basic arithmetic
        assertEquals(10.0, evaluator.evaluate("5 + 5", context));
        assertEquals(0.0, evaluator.evaluate("5 - 5", context));
        assertEquals(25.0, evaluator.evaluate("5 * 5", context));
        assertEquals(1.0, evaluator.evaluate("5 / 5", context));
        assertEquals(2.0, evaluator.evaluate("5 % 3", context));
        
        // Order of operations
        assertEquals(15.0, evaluator.evaluate("5 + 5 * 2", context));
        assertEquals(20.0, evaluator.evaluate("(5 + 5) * 2", context));
        assertEquals(7.0, evaluator.evaluate("5 + 6 / 3", context));
        assertEquals(7.5, evaluator.evaluate("(5 + 10) / 2", context));
        
        // Mixed types
        assertEquals(5.5, evaluator.evaluate("5 + 0.5", context));
        assertEquals(10.5, evaluator.evaluate("10 + 0.5", context));
        
        // Negative numbers
        assertEquals(-5.0, evaluator.evaluate("-5", context));
        assertEquals(-15.0, evaluator.evaluate("-5 * 3", context));
        assertEquals(-2.0, evaluator.evaluate("-(5 - 3)", context));
        
        // Complex expressions
        assertEquals(21.0, evaluator.evaluate("3 * (5 + 2)", context));
        assertEquals(13.0, evaluator.evaluate("5 + 2 * 5 - 2", context));
        assertEquals(12.0, evaluator.evaluate("(5 + 2) * (10 / 5) - 2", context));
        
        // With variables
        context.setVariable("x", 10);
        context.setVariable("y", 5);
        assertEquals(15.0, evaluator.evaluate("$x + $y", context));
        assertEquals(5.0, evaluator.evaluate("$x - $y", context));
        assertEquals(50.0, evaluator.evaluate("$x * $y", context));
        assertEquals(2.0, evaluator.evaluate("$x / $y", context));
        assertEquals(0.0, evaluator.evaluate("$x % $y", context));
        
        // With function results
        assertEquals(7.0, evaluator.evaluate("abs(-2) + 5", context));
        assertEquals(9.0, evaluator.evaluate("ceil(3.4) + floor(5.7)", context));
    }
    
    @Test
    void testAdvancedNestedFunctionCalls() {
        Context context = new Context();
        
        // Multiple levels of nesting
        assertEquals(4.0, evaluator.evaluate("abs(ceil(sqrt(25)) - 1)", context));
        
        // Nesting with string functions
        context.setVariable("text", "  hello WORLD  ");
        assertEquals("HELLO WORLD", evaluator.evaluate("upperCase(trim($text))", context));
        assertEquals("hello world", evaluator.evaluate("lowerCase(trim($text))", context));
        assertEquals("HEL", evaluator.evaluate("upperCase(substring(trim($text), 0, 3))", context));
        
        // Nesting with math functions
        context.setVariable("num", 16.8);
        assertEquals(4.0, evaluator.evaluate("sqrt(floor($num))", context));
        assertEquals(5L, evaluator.evaluate("round(sqrt(abs($num)) + 1)", context));
        
        // Complex arithmetic with functions
        context.setVariable("a", 3);
        context.setVariable("b", 4);
        assertEquals(5.0, evaluator.evaluate("sqrt(pow($a, 2) + pow($b, 2))", context));
        assertEquals(25.0, evaluator.evaluate("pow(sqrt(pow($a, 2) + pow($b, 2)), 2)", context));
        
        // Mixing function calls and arithmetic
        assertEquals(15.0, evaluator.evaluate("sqrt(25) * 3", context));
        assertEquals(10.0, evaluator.evaluate("sqrt(25) + sqrt(25)", context));
        assertEquals(4.0, evaluator.evaluate("pow(2, abs(-2))", context));
    }
    
    @Test
    void testAdvancedDateFunctions() {
        Context context = new Context();
        
        // Setup test dates
        LocalDate date1 = LocalDate.of(2023, 3, 15);
        LocalDate date2 = LocalDate.of(2023, 4, 20);
        context.setVariable("date1", date1);
        context.setVariable("date2", date2);
        
        // Test date difference
        assertEquals(36L, evaluator.evaluate("dateDiff($date1, $date2)", context));
        assertEquals(-36L, evaluator.evaluate("dateDiff($date2, $date1)", context));
        
        // Test adding days and chaining operations
        assertEquals("2023-03-25", evaluator.evaluate("format(addDays($date1, 10), 'yyyy-MM-dd')", context));
        assertEquals("2023-03-05", evaluator.evaluate("format(addDays($date1, -10), 'yyyy-MM-dd')", context));
        
        // Complex date manipulation with nested calls
        String complexDateExpr = evaluator.evaluate(
            "format(addDays(parseDate(format($date1, 'yyyy-MM-dd'), 'yyyy-MM-dd'), 30), 'yyyy-MM-dd')", 
            context
        ).toString();
        assertEquals("2023-04-14", complexDateExpr);
        
        // Test with arithmetic operations
        context.setVariable("days", 5);
        context.setVariable("extraDays", 3);
        assertEquals("2023-03-23", evaluator.evaluate("format(addDays($date1, $days + $extraDays), 'yyyy-MM-dd')", context));
    }
    
    @Test
    void testStringManipulation() {
        Context context = new Context();
        
        // Test string concatenation with +
        assertEquals("HelloWorld", evaluator.evaluate("'Hello' + 'World'", context));
        assertEquals("Hello123", evaluator.evaluate("'Hello' + 123", context));
        assertEquals("123Hello", evaluator.evaluate("123 + 'Hello'", context));
        
        // Test string functions
        assertEquals("hello", evaluator.evaluate("lowerCase('HELLO')", context));
        assertEquals("WORLD", evaluator.evaluate("upperCase('world')", context));
        assertEquals(5, evaluator.evaluate("length('Hello')", context));
        assertEquals("Hello", evaluator.evaluate("trim('  Hello  ')", context));
        
        // Test substring
        assertEquals("Hell", evaluator.evaluate("substring('Hello', 0, 4)", context));
        assertEquals("ello", evaluator.evaluate("substring('Hello', 1)", context));
        
        // Test replace
        assertEquals("Hallo World", evaluator.evaluate("replace('Hello World', 'e', 'a')", context));
        
        // Test contains
        assertEquals(true, evaluator.evaluate("contains('Hello World', 'World')", context));
        assertEquals(false, evaluator.evaluate("contains('Hello World', 'Goodbye')", context));
        
        // Nested string operations
        assertEquals("HELLO", evaluator.evaluate("upperCase(trim('  hello  '))", context));
        assertEquals(3, evaluator.evaluate("length(substring('Hello', 1, 3))", context));
    }
    
    @Test
    void testLogicalOperations() {
        Context context = new Context();
        
        // Setup test variables
        context.setVariable("age", 25);
        context.setVariable("isStudent", true);
        context.setVariable("score", 85);
        
        // NOT operator
        assertEquals(false, evaluator.evaluate("!true", context));
        assertEquals(true, evaluator.evaluate("!false", context));
        assertEquals(false, evaluator.evaluate("!$isStudent", context));
        
        // Complex conditions - will need to implement comparison operators
        context.setVariable("x", 10);
        context.setVariable("y", 20);
        context.setVariable("z", 10);
        
        // Test isEmpty function
        assertEquals(true, evaluator.evaluate("isEmpty('')", context));
        assertEquals(false, evaluator.evaluate("isEmpty('hello')", context));
        context.setVariable("emptyList", List.of());
        context.setVariable("nonEmptyList", List.of(1, 2, 3));
        assertEquals(true, evaluator.evaluate("isEmpty($emptyList)", context));
        assertEquals(false, evaluator.evaluate("isEmpty($nonEmptyList)", context));
        
        // Type checking functions
        assertEquals(true, evaluator.evaluate("isString('hello')", context));
        assertEquals(false, evaluator.evaluate("isString(123)", context));
        assertEquals(true, evaluator.evaluate("isNumber(123)", context));
        assertEquals(false, evaluator.evaluate("isNumber('hello')", context));
        assertEquals(true, evaluator.evaluate("isBoolean(true)", context));
        assertEquals(false, evaluator.evaluate("isBoolean(123)", context));
    }
    
    @Test
    void testErrorHandlingAndEdgeCases() {
        Context context = new Context();
        
        // Division by zero
        assertThrows(EvaluationException.class, () -> evaluator.evaluate("5 / 0", context));
        assertThrows(EvaluationException.class, () -> evaluator.evaluate("5 % 0", context));
        
        // Type errors in operations
        assertThrows(EvaluationException.class, () -> evaluator.evaluate("'hello' * 5", context));
        assertThrows(EvaluationException.class, () -> evaluator.evaluate("'hello' - 'world'", context));
        
        // Incorrect function arguments
        assertThrows(EvaluationException.class, () -> evaluator.evaluate("sqrt('hello')", context));
        assertThrows(EvaluationException.class, () -> evaluator.evaluate("pow(2)", context)); // Missing second argument
        
        // Parse errors
        assertThrows(SyntaxException.class, () -> evaluator.evaluate("5 + ", context));
        assertThrows(SyntaxException.class, () -> evaluator.evaluate("(5 + 3", context)); // Unclosed parenthesis
        
        // Nonexistent variable access
        assertThrows(PropertyNotFoundException.class, () -> evaluator.evaluate("$missingVar", context));
        
        // Use null-safe property access for the null coalescing test
        context.setVariable("obj", null);
        assertEquals("default", evaluator.evaluate("$obj?.value ?? 'default'", context));
        
        // Null value handling
        context.setVariable("nullVar", null);
        
        // Test that nullVar exists and is null
        Object nullVarValue = context.getVariable("nullVar");
        assertEquals(null, nullVarValue);
        
        // Direct isNull test with existing null variable
        assertEquals(true, evaluator.evaluate("isNull($nullVar)", context));
        
        // Direct isNull test with nonexistent variable
        assertEquals(true, evaluator.evaluate("isNull($nonExistentVar)", context));
        
        // Test with nullVar in coalesce function (should not throw exception)
        assertEquals("default", evaluator.evaluate("coalesce($nullVar, 'default')", context));
        
        // Test with nonexistent variable in coalesce function (should not throw exception)
        assertEquals("default", evaluator.evaluate("coalesce($nonExistentVar, 'default')", context));
        
        // Test nested function calls with isNull
        assertEquals(true, evaluator.evaluate("isNull(coalesce($nonExistentVar, null))", context));
        
        // Complex null coalescing chains
        Map<String, Object> profile = null;
        Map<String, Object> user = new HashMap<>();
        user.put("profile", profile);
        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        context.setVariable("data", data);
        assertEquals("Unknown", evaluator.evaluate("$data?.user?.profile?.name ?? 'Unknown'", context));
    }

    public static class Person {
        private final String name;
        private final int age;
        private final Address address;
        private final List<String> hobbies;

        public Person(String name, int age, Address address, List<String> hobbies) {
            this.name = name;
            this.age = age;
            this.address = address;
            this.hobbies = hobbies;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public Address getAddress() {
            return address;
        }

        public List<String> getHobbies() {
            return hobbies;
        }
    }

    public static class Address {
        private final String city;
        private final String country;
        private final int zipCode;

        public Address(String city, String country, int zipCode) {
            this.city = city;
            this.country = country;
            this.zipCode = zipCode;
        }

        public String getCity() {
            return city;
        }

        public String getCountry() {
            return country;
        }

        public int getZipCode() {
            return zipCode;
        }
    }
} 