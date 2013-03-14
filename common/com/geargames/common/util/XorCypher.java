package com.geargames.common.util;

/**
 * User: mkutuzov
 * Date: 14.03.13
 * Класс кодирует через исключающее или.
 * В данной реализации метод "портит" переданные данные, а не копиию.
 */
public class XorCypher implements Cypher {

    public ArrayByte encode(ArrayByte data) {
        xor(data, 0, data.length());
        return data;
    }

    public ArrayByte decode(ArrayByte data) {
        xor(data, 0, data.length());
        return data;
    }

    /**
     * Накладываем исключающее или на данные.
     * @param data
     * @param start_pos
     * @param len
     */
    private static void xor(ArrayByte data, int start_pos, int len) {
        int pos = 0;
        byte[] key = {52, 45, 18, 45, 87, 52, 45, 18, 45, 87, 11, 93, 12, 53, 3, 122, 65, 98, 34, 55, 11, 65, 23, 36, 86, 23, 29, 44, 2, 76, 33, 98, 2, 8, 33, 48, 61, 113, 34, 23, 54, 22, 29, 44, 2, 76, 33, 98, 2, 8, 33, 48, 61, 113, 34, 23, 54, 22, 98, 34, 55, 11, 65, 23, 113, 34, 23, 54, 22, 98, 34, 55, 44, 2, 76, 33, 98, 2, 8, 33, 48, 61, 113, 34, 23, 54, 22, 98, 34, 55, 11, 65, 23, 113, 34, 23, 54, 22, 98, 34, 55};
        int keyLen = key.length;
        for (int i = start_pos; i < len; i++) {
            data.set (i, (byte) ((data.get(i) & 0xff) ^ key[pos]));
            pos = (pos + 1) % keyLen;
        }
    }

}
