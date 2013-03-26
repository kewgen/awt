package com.geargames.common.util;

/**
 * User: mkutuzov
 * Date: 25.03.13
 */
public class ArrayObject implements Array{

    private Object[] array;

    public ArrayObject(int len) {
        array = new Object[len];
    }

    public ArrayObject(Object[] data, int portData) {
        array = data;
    }

    public void set(int i, Object value) {
        array[i] = value;
    }

    public Object get(int pos) {
        return array[pos];
    }

    public Object[] getArray() {
        return array;
    }

    public int length() {
        return array.length;
    }

    public void free() {
        array = null;
    }


}
