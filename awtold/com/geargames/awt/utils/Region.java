package com.geargames.awt.utils;

/**
 * User: mikhail.kutuzov
 * Date: 26.10.11
 * Time: 18:37
 */
public class Region {
    private int minX;
    private int minY;
    private int width;
    private int height;

    public Region() {
    }

    public Region(int minX, int minY, int maxX, int maxY) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException();
        }
        this.minX = minX;
        this.minY = minY;
        this.height = maxY - minY;
        this.width = maxX - minX;
    }

    public int getMinX() {
        return minX;
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public int getMinY() {
        return minY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getMaxX() {
        return getMinX() + getWidth();
    }

    public int getMaxY() {
        return getMinY() + getHeight();
    }

    public int getCenterX() {
        return getMinX() + (getWidth() >> 1);
    }

    public int getCenterY() {
        return getMinY() + (getHeight() >> 1);
    }

    public boolean isEqual(Region region) {
        return this == region || (region.getMinX() == getMinX() && region.getMinY() == getMinY() && region.getWidth() == getWidth() && region.getHeight() == getHeight());
    }

    public boolean isWithIn(int x, int y) {
        return y > minY && y < minY + height && x > minX && x < minX + width;
    }
}
