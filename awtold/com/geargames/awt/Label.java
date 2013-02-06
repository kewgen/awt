package com.geargames.awt;

import com.geargames.common.String;
import com.geargames.common.packer.Render;
import com.geargames.packer.Graphics;

public class Label extends Drawable {
    private String data;
    private int x;
    private int y;
    private com.geargames.common.Font font;
    private byte color;

    public void draw(Graphics graphics) {
        byte tmp = Render.getInstance().getStringColor();
        Render.getInstance().setStringColor(color);
        Render.getInstance().drawString(graphics, data, x, y, 0, font == null ? com.geargames.common.Render.FONT_BIG : com.geargames.common.Render.FONT_SYSTEM);
        Render.getInstance().setStringColor(tmp);
    }

    public byte getColor() {
        return color;
    }

    public void setColor(byte color) {
        this.color = color;
    }

    public void event(int code, int param, int x, int y) {
    }

    public com.geargames.common.String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public com.geargames.common.Font getFont() {
        return font;
    }

    public void setFont(com.geargames.common.Font font) {
        this.font = font;
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
}
