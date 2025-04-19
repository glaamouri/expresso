package com.expresso.parser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.expresso.ExpressionEvaluator;
import com.expresso.context.Context;
import com.expresso.exception.PropertyNotFoundException;

@DisplayName("Complex Property Path Tests")
public class ComplexPropertyPathTest {
    
    private ExpressionEvaluator evaluator;
    private Context context;
    private Map<String, Object> company;
    
    @BeforeEach
    void setUp() {
        evaluator = new ExpressionEvaluator();
        context = new Context();
        
        // Create a complex nested structure with arrays
        company = new HashMap<>();
        company.put("name", "Acme Corp");
        
        List<Map<String, Object>> departments = new ArrayList<>();
        
        // First department
        Map<String, Object> dept1 = new HashMap<>();
        dept1.put("name", "Engineering");
        dept1.put("headcount", 50);
        
        List<Map<String, Object>> engineers = new ArrayList<>();
        
        Map<String, Object> eng1 = new HashMap<>();
        eng1.put("name", "John");
        eng1.put("title", "Senior Developer");
        eng1.put("skills", Arrays.asList("Java", "Python", "JavaScript"));
        
        Map<String, Object> eng2 = new HashMap<>();
        eng2.put("name", "Mary");
        eng2.put("title", "Tech Lead");
        eng2.put("skills", Arrays.asList("C++", "Rust", "Go"));
        
        engineers.add(eng1);
        engineers.add(eng2);
        dept1.put("employees", engineers);
        
        // Second department
        Map<String, Object> dept2 = new HashMap<>();
        dept2.put("name", "Marketing");
        dept2.put("headcount", 30);
        
        List<Map<String, Object>> marketers = new ArrayList<>();
        
        Map<String, Object> marketer1 = new HashMap<>();
        marketer1.put("name", "Sarah");
        marketer1.put("title", "Marketing Manager");
        marketer1.put("campaigns", Arrays.asList("Summer Sale", "Holiday Special"));
        
        marketers.add(marketer1);
        dept2.put("employees", marketers);
        
        // Add departments to company
        departments.add(dept1);
        departments.add(dept2);
        company.put("departments", departments);
        
        // Set in context
        context.setVariable("company", company);
    }
    
    @Test
    @DisplayName("Basic property access with arrays")
    void testBasicPropertyAccessWithArrays() {
        // Access company name
        assertEquals("Acme Corp", evaluator.evaluate("$company.name", context));
        
        // Access first department name
        assertEquals("Engineering", evaluator.evaluate("$company.departments[0].name", context));
        
        // Access second department headcount
        assertEquals(30, evaluator.evaluate("$company.departments[1].headcount", context));
    }
    
    @Test
    @DisplayName("Complex property access with arrays")
    void testComplexPropertyAccessWithArrays() {
        // Access employee name in first department
        assertEquals("John", evaluator.evaluate("$company.departments[0].employees[0].name", context));
        
        // Access employee title in second department
        assertEquals("Marketing Manager", evaluator.evaluate("$company.departments[1].employees[0].title", context));
        
        // Access skill of an employee
        assertEquals("Java", evaluator.evaluate("$company.departments[0].employees[0].skills[0]", context));
        
        // Access campaign of marketing employee
        assertEquals("Summer Sale", evaluator.evaluate("$company.departments[1].employees[0].campaigns[0]", context));
    }
    
    @Test
    @DisplayName("Null safe access with arrays")
    void testNullSafeAccessWithArrays() {
        // Test with null-safe operator for properties that don't exist
        assertNull(evaluator.evaluate("$company?.departments?[0]?.employees?[0]?.nonExistentProp", context));
        
        // Test with null coalescing
        assertEquals("Default", evaluator.evaluate("$company?.departments?[0]?.employees?[0]?.nonExistentProp ?? 'Default'", context));
        
        // Test out of bounds with null-safe
        assertNull(evaluator.evaluate("$company?.departments?[0]?.employees?[10]?.name", context));
        assertEquals("Unknown", evaluator.evaluate("$company?.departments?[0]?.employees?[10]?.name ?? 'Unknown'", context));
    }
    
    @Test
    @DisplayName("Invalid property paths with arrays")
    void testInvalidPropertyPathsWithArrays() {
        // Testing the issue with complex property paths that should be fixed
        // Before fix this might throw PropertyNotFoundException
        try {
            Object result = evaluator.evaluate("$company.departments[0].employees[0].title", context);
            assertEquals("Senior Developer", result);
        } catch (PropertyNotFoundException e) {
            fail("Should not throw PropertyNotFoundException: " + e.getMessage());
        }
    }
} 