package com.geargames.awt;

import com.geargames.common.String;
import com.geargames.common.Graphics;

public class Label extends Drawable {
    private String data;
    private int x;
    private int y;
    private byte color;
    private byte anchor;

    public void draw(Graphics graphics) {
        graphics.drawString(data, x, y, anchor);
    }

    public byte getAnchor() {
        return anchor;
    }

    public void setAnchor(byte anchor) {
        this.anchor = anchor;
    }

    public byte getColor() {
        return color;
    }

    public void setColor(byte color) {
        this.color = color;
    }

    public boolean event(int code, int param, int x, int y) {
        return false;
    }

    public com.geargames.common.String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getX() {
        return x;
    }

    public void setX(int left) {
        this.x = left;
    }

    public int getY() {
        return y;
    }

    public void setY(int top) {
        this.y = top;
    }

    public int getHeight() {
        return 0;
    }

    public int getWidth() {
        return 0;
    }
}
