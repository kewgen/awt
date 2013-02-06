package com.geargames.common.util;

/**
 * User: mikhail v. kutuzov
 * Date: 03.10.12
 * Time: 14:49
 */
public class ArrayIntegerDual {
    private int[][] array;

    public ArrayIntegerDual(int sizeY) {
        array = new int[sizeY][];
    }

    public ArrayIntegerDual(int sizeY, int sizeX) {
        array = new int[sizeY][sizeX];
    }

    public void createY(int y, int size) {
        array[y] = new int[size];
    }

    public int[][] getArray() {
        return array;
    }

    public void set(int y, int x, int value) {
        array[y][x] = value;
    }

    public int get(int y, int x) {
        return array[y][x];
    }

    public int[] get(int y) {
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
