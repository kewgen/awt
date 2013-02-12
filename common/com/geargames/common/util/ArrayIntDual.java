package com.geargames.common.util;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 08.08.12
 * Time: 19:00
 * Обертка двумерного интового массива
 */
public class ArrayIntDual implements Array {

    private int[][] array;

    public ArrayIntDual(int sizeY) {
        array = new int[sizeY][];
        this.sizeY = sizeY;
        this.sizeX = 0;
    }

    public ArrayIntDual(int sizeY, int sizeX) {
        array = new int[sizeY][sizeX];
        this.sizeY = sizeY;
        this.sizeX = sizeX;
    }

    private ArrayIntDual(int[][] array) {
        this.array = array;
        this.sizeY = array.length;
        this.sizeX = array[0].length;
    }

    public void createY(int y, int size) {//deprecated размеры всех внутренних массивов одинаковы
        array[y] = new int[size];
    }

    public int[][] getArray() {
        return array;
    }

    public void set(int y, int x, int value) {
        array[y][x] = value;
    }

    public int[] get(int y) {
        return array[y];
    }

    public int get(int y, int x) {
        return array[y][x];
    }

    public int length() {
        return sizeY;//array.length;
    }

    public int length(int i) {
        return sizeX;//array[i].length;
    }

    public void free() {
        array = null;
    }

    public ArrayIntDual copy() {
        int[][] arrayNew = new int[length()][length(0)];
        for (int y = 0; y < length(); y++) {
            System.arraycopy(array[y], 0, arrayNew[y], 0, length(0));
        }
        return new ArrayIntDual(arrayNew);
    }

    private int sizeY;
    private int sizeX;
}
