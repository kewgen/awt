package com.geargames.common.util;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 06.06.12
 * Time: 17:00
 * Обертка двумерного шортового массива
 */
public class ArrayShortDual {

    private short[][] array;

    public ArrayShortDual(int sizeY) {
        array = new short[sizeY][];
    }

    public ArrayShortDual(int sizeY, int sizeX) {
        array = new short[sizeY][sizeX];
    }

    public void createY(int y, int size) {
        array[y] = new short[size];
    }

    public short[][] getArray() {
        return array;
    }

    public void set(int y, int x, short value) {
        array[y][x] = value;
    }

    public short get(int y, int x) {
        return array[y][x];
    }

    public short[] get(int y) {
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
