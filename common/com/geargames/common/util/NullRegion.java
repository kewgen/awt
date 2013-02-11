package com.geargames.common.util;

/**
 * User: mikhail v. kutuzov
 * Date: 11.01.13
 * Time: 15:39
 */
public class NullRegion extends Region {
    public static final Region instance = new NullRegion();

    public void setMinX(int minX) {
    }

    public void setMinY(int minY) {
    }

    public void setWidth(int width) {
    }

    public void setHeight(int height) {
    }
}
