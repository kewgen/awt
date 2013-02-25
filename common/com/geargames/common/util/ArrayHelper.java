package com.geargames.common.util;

/**
 * User: mikhail v. kutuzov
 * Date: 21.02.13
 * Time: 14:47
 */
public class ArrayHelper {

    public static int findNext(ArrayChar characters, int position, char element) {
        int length = characters.length();
        for (int i = position; i < length; i++) {
            if (characters.get(position) == element) {
                return i;
            }
        }
        return -1;
    }

}
