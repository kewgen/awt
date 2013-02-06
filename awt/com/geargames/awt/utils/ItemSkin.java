package com.geargames.awt.utils;

import com.geargames.common.packer.Prototype;

/**
 * user: Mikhail V. Kutuzov
 * date: 11.11.11
 * time: 20:24
 */
public class ItemSkin {
    private Prototype prototype;
    private int width;
    private int height;
    private int offsetX;
    private int offsetY;

    public Prototype getPrototype() {
        return prototype;
    }

    public void setPrototype(Prototype prototype) {
        this.prototype = prototype;
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

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public ItemSkin copy() {
        ItemSkin item = new ItemSkin();
        item.setOffsetY(getOffsetY());
        item.setOffsetX(getOffsetX());
        item.setPrototype(getPrototype());
        item.setHeight(getHeight());
        item.setWidth(getWidth());
        return item;
    }

    public void copyTo(ItemSkin item) {
        item.setOffsetY(getOffsetY());
        item.setOffsetX(getOffsetX());
        item.setPrototype(getPrototype());
        item.setHeight(getHeight());
        item.setWidth(getWidth());
    }
}
