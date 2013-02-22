package com.geargames.common.util;

/**
 * User: mikhail v. kutuzov
 * Date: 21.02.13
 * Time: 14:38
 */
public class ArrayChar implements Array {
    private char[] array;

    public ArrayChar(int len) {
        //не забывай убить массив array.free() !
        array = new char[len];
    }

    public ArrayChar(char[] data, int portData) {//portData решаем проблему перегрузки в ObjC
        array = data;
    }

    public void set(int i, char value) {
        array[i] = value;
    }

    public char get(int pos) {
        return array[pos];
    }

    public char[] getArray() {
        return array;
    }

    public int length() {
        return array.length;
    }

    public void free() {

    }
}
