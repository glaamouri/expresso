package com.expresso.context;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.HashMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for basic functionality of the Context class.
 */
@DisplayName("Basic Context Functionality")
class BasicContextTest {

    @Test
    @DisplayName("Set and get variables")
    void setAndGetVariables() {
        Context context = new Context();
        context.setVariable("name", "Alice");
        context.setVariable("age", 25);
        
        assertEquals("Alice", context.getVariable("name"));
        assertEquals(25, context.getVariable("age"));
    }
    
    @Test
    @DisplayName("Check variable existence")
    void checkVariableExistence() {
        Context context = new Context();
        context.setVariable("name", "Alice");
        
        assertTrue(context.variableExists("name"));
        assertFalse(context.variableExists("age"));
    }
    
    @Test
    @DisplayName("Update existing variables")
    void updateExistingVariables() {
        Context context = new Context();
        context.setVariable("name", "Alice");
        context.setVariable("age", 25);
        
        // Update variables
        context.setVariable("name", "Bob");
        context.setVariable("age", 30);
        
        assertEquals("Bob", context.getVariable("name"));
        assertEquals(30, context.getVariable("age"));
    }
    
    @Test
    @DisplayName("Get non-existent variable returns null")
    void getNonExistentVariableReturnsNull() {
        Context context = new Context();
        
        assertNull(context.getVariable("nonexistent"));
    }
    
    @Test
    @DisplayName("Set null value for variable")
    void setNullValueForVariable() {
        Context context = new Context();
        context.setVariable("nullValue", null);
        
        assertTrue(context.variableExists("nullValue"));
        assertNull(context.getVariable("nullValue"));
    }
    
    @Test
    @DisplayName("Register and use custom function")
    void registerAndUseCustomFunction() {
        Context context = new Context();
        
        // Register a simple addition function
        context.registerFunction("add", args -> {
            int a = args[0] instanceof Number ? ((Number) args[0]).intValue() : 0;
            int b = args[1] instanceof Number ? ((Number) args[1]).intValue() : 0;
            return a + b;
        });
        
        // Get the function
        var addFunction = context.getFunction("add");
        assertNotNull(addFunction);
        
        // Execute the function
        Object result = addFunction.apply(new Object[] { 5, 3 });
        assertEquals(8, result);
    }
    
    @Test
    @DisplayName("Resolve property on map")
    void resolvePropertyOnMap() {
        Context context = new Context();
        
        Map<String, Object> person = new HashMap<>();
        person.put("name", "Alice");
        person.put("age", 25);
        
        assertEquals("Alice", context.resolveProperty(person, "name"));
        assertEquals(25, context.resolveProperty(person, "age"));
    }
    
    @Test
    @DisplayName("Resolve nested property path")
    void resolveNestedPropertyPath() {
        Context context = new Context();
        
        Map<String, Object> address = new HashMap<>();
        address.put("city", "New York");
        address.put("zipcode", "10001");
        
        Map<String, Object> person = new HashMap<>();
        person.put("name", "Alice");
        person.put("address", address);
        
        assertEquals("New York", context.resolveProperty(person, "address.city"));
        assertEquals("10001", context.resolveProperty(person, "address.zipcode"));
    }
} 