package com.expresso.exception;

/**
 * Exception thrown when an array index is out of bounds.
 * This occurs when attempting to access an array element with an index
 * that is negative or greater than or equal to the array length.
 */
public class ArrayIndexOutOfBoundsException extends PropertyNotFoundException {
    private final Object array;
    private final int index;
    private final int size;

    /**
     * Creates a new ArrayIndexOutOfBoundsException with the specified details.
     * 
     * @param array the array or list being accessed
     * @param index the invalid index
     * @param size the size of the array or list
     */
    public ArrayIndexOutOfBoundsException(Object array, int index, int size) {
        super("Array index out of bounds: " + index + " (size: " + size + ")");
        this.array = array;
        this.index = index;
        this.size = size;
    }

    /**
     * Gets the array or list that was being accessed.
     * 
     * @return the array or list
     */
    public Object getArray() {
        return array;
    }

    /**
     * Gets the invalid index that was used.
     * 
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets the size of the array or list.
     * 
     * @return the size
     */
    public int getSize() {
        return size;
    }
} 