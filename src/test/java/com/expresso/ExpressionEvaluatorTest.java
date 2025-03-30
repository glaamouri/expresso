package com.expresso;

import com.expresso.context.Context;
import com.expresso.exception.EvaluationException;
import com.expresso.exception.PropertyNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        assertEquals(-42L, evaluator.evaluate("-42", context));
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