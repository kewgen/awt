package com.geargames.common.util;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 30.11.12
 * Time: 17:07
 * Обертка двумерного byte массива
 */
public class ArrayByteDual {

    private byte[][] array;

    public ArrayByteDual(int sizeY) {
        array = new byte[sizeY][];
    }

    public ArrayByteDual(int sizeY, int sizeX) {
        array = new byte[sizeY][sizeX];
    }

    public void createY(int y, int size) {
        array[y] = new byte[size];
    }

    public byte[][] getArray() {
        return array;
    }

    public void set(int y, int x, byte value) {
        array[y][x] = value;
    }

    public short get(int y, int x) {
        return array[y][x];
    }

    public byte[] get(int y) {
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
