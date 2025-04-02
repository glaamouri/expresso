package com.expresso.context;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.Test;

class ContextTest {

    @Test
    void testTraditionalContextCreation() {
        Context context = new Context();
        context.setVariable("name", "Alice");
        context.setVariable("age", 25);
        
        assertEquals("Alice", context.getVariable("name"));
        assertEquals(25, context.getVariable("age"));
    }
    
    @Test
    void testFluentContextCreation() {
        Context context = new Context()
            .with("name", "Alice")
            .with("age", 25)
            .with("isStudent", true);
        
        assertEquals("Alice", context.getVariable("name"));
        assertEquals(25, context.getVariable("age"));
        assertEquals(true, context.getVariable("isStudent"));
    }
    
    @Test
    void testStaticFactoryMethod() {
        Context context = Context.of("name", "Alice");
        
        assertEquals("Alice", context.getVariable("name"));
        assertTrue(context.variableExists("name"));
        assertFalse(context.variableExists("age"));
    }
    
    @Test
    void testFactoryMethodWithMap() {
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
    void testWithAllMethod() {
        Context context = new Context()
            .with("name", "Alice")
            .withAll(Map.of("age", 25, "city", "London"));
        
        assertEquals("Alice", context.getVariable("name"));
        assertEquals(25, context.getVariable("age"));
        assertEquals("London", context.getVariable("city"));
    }
    
    @Test
    void testChainedMethodsOverwrite() {
        Context context = new Context()
            .with("name", "Alice")
            .with("age", 25)
            .with("name", "Bob");  // Overwrite name
        
        assertEquals("Bob", context.getVariable("name"));
        assertEquals(25, context.getVariable("age"));
    }
} 