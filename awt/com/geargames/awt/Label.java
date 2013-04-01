package com.geargames.awt;

import com.geargames.common.Graphics;

@Deprecated
public class Label extends Drawable {
    private String data;
    private int x;
    private int y;
    private byte color;
    private byte anchor;

    @Override
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int left) {
        this.x = left;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int top) {
        this.y = top;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getWidth() {
        return 0;
    }

}
