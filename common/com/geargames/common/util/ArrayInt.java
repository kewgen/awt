package com.geargames.common.util;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 08.08.12
 * Time: 18:07
 * Обертка одномерного интового массива
 */
public class ArrayInt implements Array {

    private int[] array;

    public ArrayInt(int len) {
        //не забывай убить массив array.free() !
        array = new int[len];
    }

    public ArrayInt(int[] data, int portData) {//portData решаем проблему перегрузки в ObjC
        array = data;
    }

    public void set(int i, int value) {
        array[i] = value;
    }

    public int get(int pos) {
        return array[pos];
    }

    public int[] getArray() {
        return array;
    }

    public int length() {
        return array.length;
    }

    public void free() {
        array = null;
    }

}
