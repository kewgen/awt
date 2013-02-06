package gg;

/**
 * Created by IntelliJ IDEA.
 * User: evgenykulagin
 * Date: 05.10.2011
 * Time: 18:35:29
 * Симулятор байтового массива для iOS
 * Решаем проблему получения размера массива
 */
public class ByteArray {

    public static ByteArray toByteArray(byte[] data) {
        ByteArray barray = new ByteArray();
        barray.data = data;
        return barray;
    }

    public byte[] getData() {
        return data;
    }

    public int getSize() {
        return data.length;
    }
    private byte[] data;

}
