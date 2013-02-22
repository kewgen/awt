package com.geargames.common.util;

/**
 * User: mikhail v. kutuzov
 * Date: 21.02.13
 * Time: 14:47
 */
public class ArrayHelper {
    public static int findNext(ArrayChar string, int position, char element){
        int length = string.length();
        for(int i = position; i < length; i++){
            if(string.get(position) == element){
                return i;
            }
        }
        return -1;
    }
}
