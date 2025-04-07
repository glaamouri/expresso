package com.expresso.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.expresso.ExpressionEvaluator;
import com.expresso.context.Context;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for using the fluent Context API with ExpressionEvaluator.
 */
@DisplayName("Fluent Context with Expression Evaluator")
class FluentContextExpressionTest {

    private final ExpressionEvaluator evaluator = new ExpressionEvaluator();

    @Test
    @DisplayName("Evaluate expressions with fluent context")
    void evaluateExpressionsWithFluentContext() {
        // Create context with fluent API
        Context context = new Context()
                .with("name", "Alice")
                .with("age", 30)
                .with("isStudent", true);
        
        // Evaluate individual values
        assertEquals("Alice", evaluator.evaluate("$name", context));
        assertEquals(30, evaluator.evaluate("$age", context));
        assertEquals(true, evaluator.evaluate("$isStudent", context));
        
        // Evaluate a compound expression
        String result = (String) evaluator.evaluate(
                "($isStudent ? 'Student' : 'Not a student') + ': ' + $name", context);
        assertEquals("Student: Alice", result);
    }
    
    @Test
    @DisplayName("Evaluate expressions with context created by factory method")
    void evaluateExpressionsWithFactoryCreatedContext() {
        // Create context using factory method
        Context context = Context.of("user", Map.of(
                "name", "Bob",
                "age", 25,
                "active", true
        ));
        
        // Evaluate properties
        assertEquals("Bob", evaluator.evaluate("$user.name", context));
        assertEquals(25, evaluator.evaluate("$user.age", context));
        assertEquals(true, evaluator.evaluate("$user.active", context));
        
        // Evaluate a compound expression
        String status = (String) evaluator.evaluate(
                "$user.active ? 'Active user' : 'Inactive user'", context);
        assertEquals("Active user", status);
    }
    
    @Test
    @DisplayName("Evaluate expressions with withAll method")
    void evaluateExpressionsWithWithAllMethod() {
        // Create context with withAll method
        Context context = new Context()
                .with("firstName", "John")
                .withAll(Map.of(
                        "lastName", "Doe",
                        "email", "john.doe@example.com"
                ));
        
        // Evaluate variables
        assertEquals("John", evaluator.evaluate("$firstName", context));
        assertEquals("Doe", evaluator.evaluate("$lastName", context));
        assertEquals("john.doe@example.com", evaluator.evaluate("$email", context));
        
        // Evaluate a compound expression
        String fullName = (String) evaluator.evaluate("$firstName + ' ' + $lastName", context);
        assertEquals("John Doe", fullName);
    }
    
    @Test
    @DisplayName("Combine different fluent API methods")
    void combineDifferentFluentApiMethods() {
        // Start with a factory method
        Context context = Context.of("person", Map.of("name", "Alice"));
        
        // Add more with fluent API
        context.with("company", "Acme Corp")
               .with("position", "Developer")
               .withAll(Map.of(
                       "skills", "Java, JavaScript, SQL",
                       "years", 5
               ));
        
        // Evaluate individual values
        assertEquals("Alice", evaluator.evaluate("$person.name", context));
        assertEquals("Acme Corp", evaluator.evaluate("$company", context));
        assertEquals("Developer", evaluator.evaluate("$position", context));
        assertEquals("Java, JavaScript, SQL", evaluator.evaluate("$skills", context));
        assertEquals(5, evaluator.evaluate("$years", context));
        
        // Evaluate a compound expression
        String summary = (String) evaluator.evaluate(
                "$person.name + ' works at ' + $company + ' as a ' + $position", context);
        assertEquals("Alice works at Acme Corp as a Developer", summary);
    }
} 