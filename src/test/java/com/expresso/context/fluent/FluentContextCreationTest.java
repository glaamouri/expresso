package com.expresso.context.fluent;

import static org.junit.jupiter.api.Assertions.*;

import com.expresso.context.Context;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the fluent API in the Context class - focusing on creation and variable management.
 */
@DisplayName("Fluent Context Creation")
class FluentContextCreationTest {

    @Test
    @DisplayName("Create context with fluent 'with' method chaining")
    void createWithFluentMethods() {
        Context context = new Context()
            .with("name", "Alice")
            .with("age", 25)
            .with("isStudent", true);
        
        assertEquals("Alice", context.getVariable("name"));
        assertEquals(25, context.getVariable("age"));
        assertEquals(true, context.getVariable("isStudent"));
    }
    
    @Test
    @DisplayName("Create context with static 'of' factory method for single variable")
    void createWithStaticFactoryMethod() {
        Context context = Context.of("name", "Alice");
        
        assertEquals("Alice", context.getVariable("name"));
        assertTrue(context.variableExists("name"));
        assertFalse(context.variableExists("age"));
    }
    
    @Test
    @DisplayName("Create context with 'of' factory method for multiple variables")
    void createWithStaticFactoryMethodForMultipleVariables() {
        Map<String, Object> variables = Map.of(
            "name", "Bob",
            "age", 30,
            "city", "New York"
        );
        
        Context context = Context.of(variables);
        
        assertEquals("Bob", context.getVariable("name"));
        assertEquals(30, context.getVariable("age"));
        assertEquals("New York", context.getVariable("city"));
    }
    
    @Test
    @DisplayName("Add multiple variables with 'withAll' method")
    void addMultipleVariablesWithWithAllMethod() {
        Context context = new Context()
            .with("name", "Alice")
            .withAll(Map.of("age", 25, "city", "London"));
        
        assertEquals("Alice", context.getVariable("name"));
        assertEquals(25, context.getVariable("age"));
        assertEquals("London", context.getVariable("city"));
    }
    
    @Test
    @DisplayName("Variable overwriting with chained methods")
    void variableOverwritingWithChainedMethods() {
        Context context = new Context()
            .with("name", "Alice")
            .with("age", 25)
            .with("name", "Bob");  // Overwrite name
        
        assertEquals("Bob", context.getVariable("name"));
        assertEquals(25, context.getVariable("age"));
    }
    
    @Test
    @DisplayName("Combine traditional and fluent API methods")
    void combineTraditionalAndFluentMethods() {
        Context context = new Context();
        context.setVariable("name", "Alice");
        
        context.with("age", 25)
               .with("city", "London");
        
        assertEquals("Alice", context.getVariable("name"));
        assertEquals(25, context.getVariable("age"));
        assertEquals("London", context.getVariable("city"));
    }
} 