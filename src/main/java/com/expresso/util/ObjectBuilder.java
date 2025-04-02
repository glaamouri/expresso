package com.expresso.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A utility class for building complex object graphs for testing expressions.
 * Provides a fluent API for creating nested Maps and Lists.
 */
public class ObjectBuilder {
    private final Map<String, Object> root;
    private final Stack<BuilderContext> contextStack;

    private ObjectBuilder() {
        this.root = new HashMap<>();
        this.contextStack = new Stack<>();
        this.contextStack.push(new MapContext(root));
    }

    /**
     * Creates a new ObjectBuilder
     *
     * @return A new ObjectBuilder instance
     */
    public static ObjectBuilder create() {
        return new ObjectBuilder();
    }

    /**
     * Sets a property on the current object
     *
     * @param name The property name
     * @param value The property value
     * @return This builder instance for chaining
     */
    public ObjectBuilder set(String name, Object value) {
        BuilderContext context = contextStack.peek();
        if (context instanceof MapContext) {
            ((MapContext) context).map.put(name, value);
        } else {
            throw new IllegalStateException("Cannot set property in non-map context");
        }
        return this;
    }

    /**
     * Starts building a nested object
     *
     * @param name The property name for the nested object
     * @return This builder instance for chaining
     */
    public ObjectBuilder object(String name) {
        Map<String, Object> map = new HashMap<>();
        BuilderContext context = contextStack.peek();
        if (context instanceof MapContext) {
            ((MapContext) context).map.put(name, map);
        } else {
            throw new IllegalStateException("Cannot create object in non-map context");
        }
        contextStack.push(new MapContext(map));
        return this;
    }

    /**
     * Starts building a list property
     *
     * @param name The property name for the list
     * @return This builder instance for chaining
     */
    public ObjectBuilder list(String name) {
        List<Object> list = new ArrayList<>();
        BuilderContext context = contextStack.peek();
        if (context instanceof MapContext) {
            ((MapContext) context).map.put(name, list);
        } else {
            throw new IllegalStateException("Cannot create list in non-map context");
        }
        contextStack.push(new ListContext(list));
        return this;
    }

    /**
     * Adds a value to the current list
     *
     * @param value The value to add
     * @return This builder instance for chaining
     */
    public ObjectBuilder add(Object value) {
        BuilderContext context = contextStack.peek();
        if (context instanceof ListContext) {
            ((ListContext) context).list.add(value);
        } else {
            throw new IllegalStateException("Cannot add to non-list context");
        }
        return this;
    }

    /**
     * Starts building an object within a list
     *
     * @return This builder instance for chaining
     */
    public ObjectBuilder addObject() {
        Map<String, Object> map = new HashMap<>();
        BuilderContext context = contextStack.peek();
        if (context instanceof ListContext) {
            ((ListContext) context).list.add(map);
        } else {
            throw new IllegalStateException("Cannot add object to non-list context");
        }
        contextStack.push(new MapContext(map));
        return this;
    }

    /**
     * Ends the current context (list or object) and returns to the parent
     *
     * @return This builder instance for chaining
     */
    public ObjectBuilder end() {
        if (contextStack.size() <= 1) {
            throw new IllegalStateException("Cannot end root context");
        }
        contextStack.pop();
        return this;
    }

    /**
     * Builds and returns the constructed object graph
     *
     * @return The completed object graph as a Map
     */
    public Map<String, Object> build() {
        if (contextStack.size() > 1) {
            throw new IllegalStateException("Unclosed contexts remain");
        }
        return new HashMap<>(root);
    }

    /**
     * Interface for builder contexts
     */
    private interface BuilderContext {}

    /**
     * Context for building a map
     */
    private static class MapContext implements BuilderContext {
        final Map<String, Object> map;

        MapContext(Map<String, Object> map) {
            this.map = map;
        }
    }

    /**
     * Context for building a list
     */
    private static class ListContext implements BuilderContext {
        final List<Object> list;

        ListContext(List<Object> list) {
            this.list = list;
        }
    }

    /**
     * Simple stack implementation
     */
    private static class Stack<T> {
        private final ArrayList<T> elements = new ArrayList<>();

        void push(T element) {
            elements.add(element);
        }

        T pop() {
            if (elements.isEmpty()) {
                throw new IllegalStateException("Cannot pop from empty stack");
            }
            return elements.remove(elements.size() - 1);
        }

        T peek() {
            if (elements.isEmpty()) {
                throw new IllegalStateException("Cannot peek empty stack");
            }
            return elements.get(elements.size() - 1);
        }

        int size() {
            return elements.size();
        }
    }
} 