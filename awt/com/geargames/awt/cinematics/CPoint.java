package com.geargames.awt.cinematics;

/**
 * User: mikhail.kutuzov
 * Date: 26.11.11
 * Time: 16:45
 * Каждая точка на плоскости описывается парой чисел. Так же парой чисел мы будем описывать любой
 * вектор с началом в точке (0,0).
 */
public class CPoint {
    public static final CPoint ZERRO_POINT = new CPoint();
    public static final int COORDINATE_SCALE = 100;
    private double x;
    private double y;

    public CPoint() {
    }

    public CPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public CPoint copy() {
        return new CPoint(x, y);
    }
}
