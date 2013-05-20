package com.geargames.common.util;

/**
 * User: abarakov
 * Date: 28.03.13
 */
public class Mathematics {
    public static int PRECISION = 1000;
    public static int HALF_PRECISION = 500;

    public static int min(int a, int b) {
        return (a <= b) ? a : b;
    }

    public static int max(int a, int b) {
        return (a >= b) ? a : b;
    }

    public static int abs(int a){
        return (a < 0) ? -a : a;
    }
}