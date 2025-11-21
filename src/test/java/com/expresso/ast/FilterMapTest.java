package com.expresso.ast;

import com.expresso.context.Context;
import com.expresso.ExpressionEvaluator;
import com.expresso.context.functions.CollectionFunctions;
import com.expresso.context.functions.ComparisonFunctions;
import com.expresso.context.functions.MathFunctions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class FilterMapTest {

    private Context context;
    private ExpressionEvaluator evaluator;

    @BeforeEach
    void setUp() {
        context = new Context();
        evaluator = new ExpressionEvaluator();
        // Register necessary functions
        new CollectionFunctions().registerFunctions(context);
        new ComparisonFunctions().registerFunctions(context);
        new MathFunctions().registerFunctions(context);
    }

    @Test
    void testFilter() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        context.setVariable("list", list);

        Object result = evaluator.evaluate("filter($list, $item > 3)", context);
        assertTrue(result instanceof List);
        assertEquals(Arrays.asList(4, 5), result);
    }

    @Test
    void testMap() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        context.setVariable("list", list);

        Object result = evaluator.evaluate("map($list, $item * 2)", context);
        assertTrue(result instanceof List);
        assertEquals(Arrays.asList(2.0, 4.0, 6.0), result);
    }

    @Test
    void testMapObject() {
        Map<String, Object> user1 = new HashMap<>();
        user1.put("name", "Alice");
        user1.put("age", 25);

        Map<String, Object> user2 = new HashMap<>();
        user2.put("name", "Bob");
        user2.put("age", 30);

        List<Map<String, Object>> users = Arrays.asList(user1, user2);
        context.setVariable("users", users);

        Object result = evaluator.evaluate("map($users, $item.name)", context);
        assertTrue(result instanceof List);
        assertEquals(Arrays.asList("Alice", "Bob"), result);
    }

    @Test
    void testItemAccess() {
        Map<String, Object> user = new HashMap<>();
        user.put("name", "Alice");
        context.setVariable("item", user);

        Object result = evaluator.evaluate("$item.name", context);
        assertEquals("Alice", result);
    }
}
