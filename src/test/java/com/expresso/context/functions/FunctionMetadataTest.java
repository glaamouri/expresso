package com.expresso.context.functions;

import com.expresso.ExpressionEvaluator;
import com.expresso.context.Context;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for function metadata retrieval functionality.
 */
public class FunctionMetadataTest {

    @Test
    public void testGetAllBuiltInFunctions() {
        List<FunctionInfo> functions = FunctionRegistry.getAllFunctionInfo();

        assertNotNull(functions);
        assertFalse(functions.isEmpty(), "Should have built-in functions");

        // Verify that all built-in functions are marked as such
        functions.forEach(info -> {
            assertNotNull(info.getName(), "Function name should not be null");
            assertTrue(info.isBuiltIn(), "Built-in functions should be marked as built-in");
        });

        // Verify presence of some common functions
        assertTrue(functions.stream().anyMatch(f -> f.getName().equals("upperCase")));
        assertTrue(functions.stream().anyMatch(f -> f.getName().equals("abs")));
        assertTrue(functions.stream().anyMatch(f -> f.getName().equals("size")));
        assertTrue(functions.stream().anyMatch(f -> f.getName().equals("isNull")));
    }

    @Test
    public void testGetFunctionsFromContext() {
        Context context = new Context();
        List<FunctionInfo> functions = context.getAvailableFunctions();

        assertNotNull(functions);
        assertFalse(functions.isEmpty(), "Context should have built-in functions");

        // All functions from context should be built-in initially
        functions.forEach(info -> assertTrue(info.isBuiltIn()));
    }

    @Test
    public void testGetFunctionsFromContextWithCustomFunction() {
        Context context = new Context();

        // Register a custom function
        context.registerFunction("myCustomFunction", args -> "custom");

        List<FunctionInfo> functions = context.getAvailableFunctions();

        // Verify custom function is included
        FunctionInfo customFunc = functions.stream()
                .filter(f -> f.getName().equals("myCustomFunction"))
                .findFirst()
                .orElse(null);

        assertNotNull(customFunc, "Custom function should be in the list");
        assertFalse(customFunc.isBuiltIn(), "Custom function should not be marked as built-in");
        assertEquals("Custom function", customFunc.getDescription());
    }

    @Test
    public void testGetSpecificFunctionInfo() {
        Context context = new Context();

        FunctionInfo upperCaseInfo = context.getFunctionInfo("upperCase");
        assertNotNull(upperCaseInfo, "Should find upperCase function");
        assertEquals("upperCase", upperCaseInfo.getName());
        assertTrue(upperCaseInfo.isBuiltIn());
        assertNotNull(upperCaseInfo.getDescription());
        assertFalse(upperCaseInfo.getParameters().isEmpty(), "upperCase should have parameters");

        FunctionInfo absInfo = context.getFunctionInfo("abs");
        assertNotNull(absInfo, "Should find abs function");
        assertEquals("abs", absInfo.getName());
        assertEquals("Number", absInfo.getReturnType());

        // Non-existent function
        FunctionInfo nonExistent = context.getFunctionInfo("nonExistentFunction");
        assertNull(nonExistent, "Should return null for non-existent function");
    }

    @Test
    public void testGetFunctionsFromEvaluator() {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        List<FunctionInfo> functions = evaluator.getAvailableFunctions();

        assertNotNull(functions);
        assertFalse(functions.isEmpty(), "Evaluator should have built-in functions");
    }

    @Test
    public void testGetFunctionsFromEvaluatorWithCustomFunction() {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();

        // Register a custom function
        evaluator.registerFunction("customAdd",
                args -> ((Number) args[0]).doubleValue() + ((Number) args[1]).doubleValue());

        List<FunctionInfo> functions = evaluator.getAvailableFunctions();

        // Verify custom function is included
        FunctionInfo customFunc = functions.stream()
                .filter(f -> f.getName().equals("customAdd"))
                .findFirst()
                .orElse(null);

        assertNotNull(customFunc, "Custom function should be in the list");
        assertFalse(customFunc.isBuiltIn(), "Custom function should not be marked as built-in");
    }

    @Test
    public void testGetSpecificFunctionInfoFromEvaluator() {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();

        FunctionInfo upperCaseInfo = evaluator.getFunctionInfo("upperCase");
        assertNotNull(upperCaseInfo, "Should find upperCase function");
        assertEquals("upperCase", upperCaseInfo.getName());

        // Register custom function and retrieve its info
        evaluator.registerFunction("myFunc", args -> null);
        FunctionInfo customInfo = evaluator.getFunctionInfo("myFunc");
        assertNotNull(customInfo, "Should find custom function");
        assertEquals("myFunc", customInfo.getName());
        assertFalse(customInfo.isBuiltIn());
    }

    @Test
    public void testFunctionInfoHasMetadata() {
        Context context = new Context();
        FunctionInfo substringInfo = context.getFunctionInfo("substring");

        assertNotNull(substringInfo);
        assertEquals("substring", substringInfo.getName());
        assertTrue(substringInfo.isBuiltIn());
        assertNotNull(substringInfo.getDescription());
        assertFalse(substringInfo.getDescription().isEmpty());
        assertEquals("String", substringInfo.getReturnType());
        assertFalse(substringInfo.getParameters().isEmpty(), "substring should have parameters");

        // Check parameter details
        FunctionInfo.ParameterInfo firstParam = substringInfo.getParameters().get(0);
        assertNotNull(firstParam.getName());
        assertNotNull(firstParam.getType());
        assertNotNull(firstParam.getDescription());
    }

    @Test
    public void testAllProvidersHaveMetadata() {
        List<FunctionInfo> allFunctions = FunctionRegistry.getAllFunctionInfo();

        // Verify each function has complete metadata
        allFunctions.forEach(info -> {
            assertNotNull(info.getName(), "Function must have a name");
            assertNotNull(info.getDescription(), "Function must have a description");
            assertNotNull(info.getReturnType(), "Function must have a return type");
            assertTrue(info.isBuiltIn(), "Registry functions should be built-in");

            // Verify parameters have metadata too
            info.getParameters().forEach(param -> {
                assertNotNull(param.getName(), "Parameter must have a name");
                assertNotNull(param.getType(), "Parameter must have a type");
                assertNotNull(param.getDescription(), "Parameter must have a description");
            });
        });

        // Should have functions from all providers
        assertTrue(allFunctions.size() > 50, "Should have many built-in functions");
    }
}
