package com.expresso;

import static org.junit.jupiter.api.Assertions.*;

import com.expresso.context.Context;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for using the fluent Context API with expression evaluation.
 */
class FluentContextApiTest {

    private final ExpressionEvaluator evaluator = new ExpressionEvaluator();

    @Test
    void testFluentContextWithExpressionEvaluation() {
        // Create context with chained with() calls
        Context context = new Context()
                .with("name", "Alice")
                .with("age", 30)
                .with("isStudent", true);
        
        // Evaluate expressions using fluent context
        assertEquals("Alice", evaluator.evaluate("$name", context));
        assertEquals(30, evaluator.evaluate("$age", context));
        assertEquals(true, evaluator.evaluate("$isStudent", context));
        
        // Evaluate compound expression
        String result = (String) evaluator.evaluate(
                "($isStudent ? 'Student' : 'Not a student') + ': ' + $name + ', age ' + $age",
                context);
        assertEquals("Student: Alice, age 30", result);
    }
    
    @Test
    void testFactoryMethodWithExpressionEvaluation() {
        // Create context using factory method
        Context context = Context.of("user", Map.of(
                "name", "Bob",
                "details", Map.of(
                        "age", 25,
                        "city", "New York",
                        "active", true
                )
        ));
        
        // Access nested properties
        assertEquals("Bob", evaluator.evaluate("$user.name", context));
        assertEquals(25, evaluator.evaluate("$user.details.age", context));
        assertEquals("New York", evaluator.evaluate("$user.details.city", context));
        assertEquals(true, evaluator.evaluate("$user.details.active", context));
        
        // Test null-safe access and default values
        Object result = evaluator.evaluate("$user?.nonExistent?.value ?? 'Default'", context);
        assertEquals("Default", result);
    }
    
    @Test
    void testWithAllMethod() {
        // Initial context with some variables
        Context context = new Context()
                .with("firstName", "John")
                .with("lastName", "Doe");
        
        // Add more variables with withAll
        context.withAll(Map.of(
                "age", 40,
                "city", "London",
                "isEmployed", true
        ));
        
        // Test accessing all variables
        assertEquals("John", evaluator.evaluate("$firstName", context));
        assertEquals("Doe", evaluator.evaluate("$lastName", context));
        assertEquals(40, evaluator.evaluate("$age", context));
        assertEquals("London", evaluator.evaluate("$city", context));
        assertEquals(true, evaluator.evaluate("$isEmployed", context));
        
        // Test combined expression
        String fullName = (String) evaluator.evaluate("$firstName + ' ' + $lastName", context);
        assertEquals("John Doe", fullName);
        
        // Verify values directly without comparison operators
        Object age = evaluator.evaluate("$age", context);
        assertEquals(40, age);
        
        Object employed = evaluator.evaluate("$isEmployed", context);
        assertEquals(true, employed);
    }
} 