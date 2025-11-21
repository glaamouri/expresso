package com.expresso.context.functions;

import com.expresso.context.Context;
import com.expresso.ExpressionEvaluator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CollectionFunctionsTest {

    private Context context;
    private ExpressionEvaluator evaluator;

    @BeforeEach
    void setUp() {
        context = new Context();
        evaluator = new ExpressionEvaluator();
        // Register functions
        new CollectionFunctions().registerFunctions(context);
    }

    @Test
    void testSum() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        context.setVariable("list", list);

        Object result = evaluator.evaluate("sum($list)", context);
        assertEquals(15.0, result);
    }

    @Test
    void testAvg() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        context.setVariable("list", list);

        Object result = evaluator.evaluate("avg($list)", context);
        assertEquals(3.0, result);
    }

    @Test
    void testSort() {
        List<Integer> list = Arrays.asList(5, 3, 1, 4, 2);
        context.setVariable("list", list);

        Object result = evaluator.evaluate("sort($list)", context);
        assertTrue(result instanceof List);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), result);
    }

    @Test
    void testReverse() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        context.setVariable("list", list);

        Object result = evaluator.evaluate("reverse($list)", context);
        assertTrue(result instanceof List);
        assertEquals(Arrays.asList(3, 2, 1), result);
    }

    @Test
    void testFirst() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        context.setVariable("list", list);

        Object result = evaluator.evaluate("first($list)", context);
        assertEquals(1, result);
    }

    @Test
    void testLast() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        context.setVariable("list", list);

        Object result = evaluator.evaluate("last($list)", context);
        assertEquals(3, result);
    }

    @Test
    void testSubList() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        context.setVariable("list", list);

        Object result = evaluator.evaluate("subList($list, 1, 3)", context);
        assertTrue(result instanceof List);
        assertEquals(Arrays.asList(2, 3), result);
    }
}
