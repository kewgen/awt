package com.geargames.common.packer;

import com.geargames.common.Graphics;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 18.09.12
 * Time: 17:56
 * расширяем сущность чтобы можно было хранить в списке
 */
public class Index {

    public Index(Prototype prototype, int x, int y) {
        this.x = x;
        this.y = y;
        this.prototype = prototype;
    }

    public void draw(Graphics graphics, int x, int y) {
        prototype.draw(graphics, x + this.x, y + this.y);
    }

    public Prototype getPrototype() {
        return prototype;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Index{" +
                "x=" + x +
                ", y=" + y +
                ", prototype=" + prototype +
                '}';
    }

    private int x;
    private int y;
    private Prototype prototype;

}
