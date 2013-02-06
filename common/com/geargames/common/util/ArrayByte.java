package com.geargames.common.util;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 06.06.12
 * Time: 15:41
 * Обертка одномерного байтового массива
 */
public class ArrayByte implements Array {

    private byte[] array;

    public ArrayByte(int len) {
        //не забывай убить массив array.free() !
        array = new byte[len];
    }

    public ArrayByte(byte[] data, int portData) {//portData решаем проблему перегрузки в ObjC
        //не забывай убить массив array.free() !
        array = data;
    }

    public void set(int i, byte value) {
        array[i] = value;
    }

    public byte get(int pos) {
        return array[pos];
    }

    public byte[] getArray() {
        return array;
    }

    public int length() {
        return array.length;
    }

    public void free() {
        array = null;
    }

}
