package com.expresso.context.functions;

import com.expresso.ExpressionEvaluator;
import com.expresso.context.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import com.expresso.context.functions.FunctionInfo.ParameterInfo;

/**
 * Comprehensive tests for Function Discovery API covering metadata retrieval
 * and validation.
 */
@DisplayName("Function Discovery - Comprehensive Tests")
class FunctionDiscoveryComprehensiveTest {

    private ExpressionEvaluator evaluator;
    private Context context;

    @BeforeEach
    void setUp() {
        evaluator = new ExpressionEvaluator();
        context = new Context();
    }

    @Nested
    @DisplayName("getAvailableFunctions Tests")
    class GetAvailableFunctionsTests {

        @Test
        @DisplayName("should return non-empty list of functions")
        void testNonEmptyList() {
            List<FunctionInfo> functions = evaluator.getAvailableFunctions();
            assertNotNull(functions);
            assertFalse(functions.isEmpty());
        }

        @Test
        @DisplayName("should return at least 70 built-in functions")
        void testMinimumFunctionCount() {
            List<FunctionInfo> functions = evaluator.getAvailableFunctions();
            long builtInCount = functions.stream()
                    .filter(FunctionInfo::isBuiltIn)
                    .count();
            assertTrue(builtInCount >= 70,
                    "Expected at least 70 built-in functions, found: " + builtInCount);
        }

        @Test
        @DisplayName("should include custom functions")
        void testIncludesCustomFunctions() {
            evaluator.registerFunction("customTest", args -> "test");

            List<FunctionInfo> functions = evaluator.getAvailableFunctions();
            boolean hasCustom = functions.stream()
                    .anyMatch(f -> f.getName().equals("customTest"));

            assertTrue(hasCustom, "Should include custom functions");
        }

        @Test
        @DisplayName("should return unique function names")
        void testUniqueFunctionNames() {
            List<FunctionInfo> functions = evaluator.getAvailableFunctions();
            Set<String> names = functions.stream()
                    .map(FunctionInfo::getName)
                    .collect(Collectors.toSet());

            assertEquals(functions.size(), names.size(),
                    "All function names should be unique");
        }
    }

    @Nested
    @DisplayName("getFunctionInfo Tests")
    class GetFunctionInfoTests {

        @Test
        @DisplayName("should return info for existing function")
        void testExistingFunction() {
            FunctionInfo info = evaluator.getFunctionInfo("upperCase");
            assertNotNull(info);
            assertEquals("upperCase", info.getName());
        }

        @Test
        @DisplayName("should return null for non-existent function")
        void testNonExistentFunction() {
            FunctionInfo info = evaluator.getFunctionInfo("nonExistentFunction");
            assertNull(info);
        }

        @Test
        @DisplayName("should be case-sensitive")
        void testCaseSensitive() {
            FunctionInfo info = evaluator.getFunctionInfo("UPPERCASE");
            assertNull(info, "Function lookup should be case-sensitive");
        }

        @Test
        @DisplayName("should return info for custom function")
        void testCustomFunction() {
            evaluator.registerFunction("myCustomFunc", args -> args[0]);

            FunctionInfo info = evaluator.getFunctionInfo("myCustomFunc");
            assertNotNull(info);
            assertEquals("myCustomFunc", info.getName());
            assertFalse(info.isBuiltIn());
        }
    }

    @Nested
    @DisplayName("FunctionInfo Metadata Tests")
    class FunctionMetadataTests {

        @Test
        @DisplayName("should have complete metadata for string functions")
        void testStringFunctionMetadata() {
            FunctionInfo upperCase = evaluator.getFunctionInfo("upperCase");
            assertNotNull(upperCase);
            assertNotNull(upperCase.getName());
            assertNotNull(upperCase.getDescription());
            assertNotNull(upperCase.getReturnType());
            assertNotNull(upperCase.getParameters());
            assertTrue(upperCase.isBuiltIn());
        }

        @Test
        @DisplayName("should have complete metadata for math functions")
        void testMathFunctionMetadata() {
            FunctionInfo abs = evaluator.getFunctionInfo("abs");
            assertNotNull(abs);
            assertEquals("abs", abs.getName());
            assertFalse(abs.getDescription().isEmpty());
            assertFalse(abs.getReturnType().isEmpty());
            assertFalse(abs.getParameters().isEmpty());
        }

        @Test
        @DisplayName("should have complete metadata for collection functions")
        void testCollectionFunctionMetadata() {
            FunctionInfo size = evaluator.getFunctionInfo("size");
            assertNotNull(size);
            assertEquals("size", size.getName());
            assertNotNull(size.getDescription());
            assertNotNull(size.getReturnType());
        }

        @Test
        @DisplayName("should have complete metadata for date functions")
        void testDateFunctionMetadata() {
            FunctionInfo currentDate = evaluator.getFunctionInfo("currentDate");
            assertNotNull(currentDate);
            assertEquals("currentDate", currentDate.getName());
            assertNotNull(currentDate.getDescription());
            assertNotNull(currentDate.getReturnType());
        }
    }

    @Nested
    @DisplayName("Parameter Information Tests")
    class ParameterInfoTests {

        @Test
        @DisplayName("should have parameter information for multi-param functions")
        void testMultiParamFunction() {
            FunctionInfo substring = evaluator.getFunctionInfo("substring");
            assertNotNull(substring);

            List<ParameterInfo> params = substring.getParameters();
            assertNotNull(params);
            assertTrue(params.size() >= 2, "substring should have at least 2 parameters");

            // Check first parameter
            ParameterInfo firstParam = params.get(0);
            assertNotNull(firstParam.getName());
            assertNotNull(firstParam.getType());
            assertNotNull(firstParam.getDescription());
        }

        @Test
        @DisplayName("should have complete parameter info")
        void testCompleteParameterInfo() {
            FunctionInfo replace = evaluator.getFunctionInfo("replace");
            assertNotNull(replace);

            List<ParameterInfo> params = replace.getParameters();
            for (ParameterInfo param : params) {
                assertNotNull(param.getName(), "Parameter name should not be null");
                assertNotNull(param.getType(), "Parameter type should not be null");
                assertNotNull(param.getDescription(), "Parameter description should not be null");
                assertFalse(param.getName().isEmpty(), "Parameter name should not be empty");
                assertFalse(param.getType().isEmpty(), "Parameter type should not be empty");
            }
        }

        @Test
        @DisplayName("should handle functions with no parameters")
        void testNoParameterFunction() {
            FunctionInfo currentDate = evaluator.getFunctionInfo("currentDate");
            assertNotNull(currentDate);

            List<ParameterInfo> params = currentDate.getParameters();
            assertNotNull(params);
            assertTrue(params.isEmpty(), "currentDate should have no parameters");
        }
    }

    @Nested
    @DisplayName("Function Categories Tests")
    class FunctionCategoriesTests {

        @Test
        @DisplayName("should include all string functions")
        void testAllStringFunctions() {
            List<FunctionInfo> functions = evaluator.getAvailableFunctions();

            String[] expectedStringFuncs = {
                    "upperCase", "lowerCase", "trim", "length", "substring",
                    "replace", "contains", "startsWith", "endsWith", "split",
                    "join", "charAt", "indexOf"
            };

            for (String funcName : expectedStringFuncs) {
                assertTrue(functions.stream().anyMatch(f -> f.getName().equals(funcName)),
                        "Missing string function: " + funcName);
            }
        }

        @Test
        @DisplayName("should include all math functions")
        void testAllMathFunctions() {
            List<FunctionInfo> functions = evaluator.getAvailableFunctions();

            String[] expectedMathFuncs = {
                    "abs", "ceil", "floor", "round", "max", "min", "pow",
                    "sqrt", "random", "sin", "cos", "tan", "log", "log10", "exp"
            };

            for (String funcName : expectedMathFuncs) {
                assertTrue(functions.stream().anyMatch(f -> f.getName().equals(funcName)),
                        "Missing math function: " + funcName);
            }
        }

        @Test
        @DisplayName("should include all collection functions")
        void testAllCollectionFunctions() {
            List<FunctionInfo> functions = evaluator.getAvailableFunctions();

            String[] expectedCollectionFuncs = {
                    "size", "sum", "avg", "sort", "reverse", "first", "last", "subList"
            };

            for (String funcName : expectedCollectionFuncs) {
                assertTrue(functions.stream().anyMatch(f -> f.getName().equals(funcName)),
                        "Missing collection function: " + funcName);
            }
        }

        @Test
        @DisplayName("should include all date functions")
        void testAllDateFunctions() {
            List<FunctionInfo> functions = evaluator.getAvailableFunctions();

            String[] expectedDateFuncs = {
                    "currentDate", "currentTime", "currentDateTime", "parseDate",
                    "formatDate", "addDays", "addMonths", "addYears", "daysBetween"
            };

            for (String funcName : expectedDateFuncs) {
                assertTrue(functions.stream().anyMatch(f -> f.getName().equals(funcName)),
                        "Missing date function: " + funcName);
            }
        }

        @Test
        @DisplayName("should include all logic functions")
        void testAllLogicFunctions() {
            List<FunctionInfo> functions = evaluator.getAvailableFunctions();

            String[] expectedLogicFuncs = {
                    "isNull", "coalesce", "isEmpty", "isNumber", "isString",
                    "isBoolean", "isList", "isMap", "equals", "ifThen"
            };

            for (String funcName : expectedLogicFuncs) {
                assertTrue(functions.stream().anyMatch(f -> f.getName().equals(funcName)),
                        "Missing logic function: " + funcName);
            }
        }

        @Test
        @DisplayName("should include all comparison functions")
        void testAllComparisonFunctions() {
            List<FunctionInfo> functions = evaluator.getAvailableFunctions();

            String[] expectedComparisonFuncs = {
                    "greaterThan", "lessThan", "greaterThanOrEqual",
                    "lessThanOrEqual", "strictEquals", "notEquals"
            };

            for (String funcName : expectedComparisonFuncs) {
                assertTrue(functions.stream().anyMatch(f -> f.getName().equals(funcName)),
                        "Missing comparison function: " + funcName);
            }
        }

        @Test
        @DisplayName("should include all utility functions")
        void testAllUtilityFunctions() {
            List<FunctionInfo> functions = evaluator.getAvailableFunctions();

            String[] expectedUtilityFuncs = {
                    "typeof", "toString", "toNumber", "toBoolean"
            };

            for (String funcName : expectedUtilityFuncs) {
                assertTrue(functions.stream().anyMatch(f -> f.getName().equals(funcName)),
                        "Missing utility function: " + funcName);
            }
        }
    }

    @Nested
    @DisplayName("Custom Function Discovery Tests")
    class CustomFunctionDiscoveryTests {

        @Test
        @DisplayName("should distinguish built-in from custom functions")
        void testBuiltInVsCustom() {
            evaluator.registerFunction("myFunc", args -> "test");

            FunctionInfo builtIn = evaluator.getFunctionInfo("upperCase");
            FunctionInfo custom = evaluator.getFunctionInfo("myFunc");

            assertTrue(builtIn.isBuiltIn());
            assertFalse(custom.isBuiltIn());
        }

        @Test
        @DisplayName("should provide metadata for custom functions")
        void testCustomFunctionMetadata() {
            evaluator.registerFunction("customAdd",
                    args -> ((Number) args[0]).doubleValue() + ((Number) args[1]).doubleValue());

            FunctionInfo info = evaluator.getFunctionInfo("customAdd");
            assertNotNull(info);
            assertEquals("customAdd", info.getName());
            assertNotNull(info.getDescription());
            assertNotNull(info.getReturnType());
            assertFalse(info.isBuiltIn());
        }

        @Test
        @DisplayName("should update function list after registration")
        void testDynamicRegistration() {
            int initialCount = evaluator.getAvailableFunctions().size();

            evaluator.registerFunction("dynamicFunc", args -> "result");

            int newCount = evaluator.getAvailableFunctions().size();
            assertEquals(initialCount + 1, newCount);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Validation")
    class EdgeCasesTests {

        @Test
        @DisplayName("should handle null function name in getFunctionInfo")
        void testNullFunctionName() {
            FunctionInfo info = evaluator.getFunctionInfo(null);
            assertNull(info);
        }

        @Test
        @DisplayName("should handle empty function name")
        void testEmptyFunctionName() {
            FunctionInfo info = evaluator.getFunctionInfo("");
            assertNull(info);
        }

        @Test
        @DisplayName("should return consistent results on multiple calls")
        void testConsistentResults() {
            List<FunctionInfo> first = evaluator.getAvailableFunctions();
            List<FunctionInfo> second = evaluator.getAvailableFunctions();

            assertEquals(first.size(), second.size());
        }

        @Test
        @DisplayName("should not allow modification of returned list")
        void testImmutableList() {
            List<FunctionInfo> functions = evaluator.getAvailableFunctions();
            int originalSize = functions.size();

            // Try to modify the list (should be immutable or a copy)
            try {
                functions.clear();
                // If we get here, it should not affect the next call
                List<FunctionInfo> newFunctions = evaluator.getAvailableFunctions();
                assertEquals(originalSize, newFunctions.size());
            } catch (UnsupportedOperationException e) {
                // This is also acceptable - list is immutable
                assertTrue(true);
            }
        }
    }

    @Nested
    @DisplayName("Real-World Use Cases")
    class RealWorldUseCasesTests {

        @Test
        @DisplayName("should support IDE autocomplete scenario")
        void testIDEAutocomplete() {
            String prefix = "upper";
            List<FunctionInfo> functions = evaluator.getAvailableFunctions();

            List<String> suggestions = functions.stream()
                    .filter(f -> f.getName().startsWith(prefix))
                    .map(f -> f.getName())
                    .collect(Collectors.toList());

            assertTrue(suggestions.contains("upperCase"));
        }

        @Test
        @DisplayName("should support documentation generation scenario")
        void testDocumentationGeneration() {
            List<FunctionInfo> functions = evaluator.getAvailableFunctions();

            // Verify all functions have enough info for documentation
            for (FunctionInfo func : functions) {
                if (func.isBuiltIn()) {
                    assertNotNull(func.getName());
                    assertNotNull(func.getDescription());
                    assertNotNull(func.getReturnType());
                    assertFalse(func.getName().isEmpty());
                    assertFalse(func.getDescription().isEmpty());
                    assertFalse(func.getReturnType().isEmpty());
                }
            }
        }

        @Test
        @DisplayName("should support function validation scenario")
        void testFunctionValidation() {
            String[] requiredFunctions = { "upperCase", "lowerCase", "length" };
            Set<String> availableFunctions = evaluator.getAvailableFunctions().stream()
                    .map(FunctionInfo::getName)
                    .collect(Collectors.toSet());

            for (String required : requiredFunctions) {
                assertTrue(availableFunctions.contains(required),
                        "Missing required function: " + required);
            }
        }
    }
}
