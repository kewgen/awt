package com.geargames.common.util;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 08.08.12
 * Time: 19:26
 * Обертка двумерного массива объектов
 */
public class ArrayObjectDual {

    private Object[][] array;

    public ArrayObjectDual(int sizeY) {
        array = new Object[sizeY][];
    }

    public ArrayObjectDual(int sizeY, int sizeX) {
        array = new Object[sizeY][sizeX];
    }

    public void createY(int y, int size) {
        array[y] = new Object[size];
    }

    public Object[][] getArray() {
        return array;
    }

    public void set(int y, int x, Object value) {
        array[y][x] = value;
    }

    public Object get(int y, int x) {
        return array[y][x];
    }

    public Object[] get(int y) {
        return array[y];
    }

    public int length() {
        return array.length;
    }

    public int length(int i) {
        return array[i].length;
    }

    public void free() {
        array = null;
    }

}
