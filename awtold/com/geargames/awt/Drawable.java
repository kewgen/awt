package com.geargames.awt;


import com.geargames.packer.Graphics;

/**
 * user: Mikhail V. Kutuzov
 * date: 20.11.11
 * time: 10:37
 */
public abstract class Drawable {

    public static boolean DEBUG = false;//здесь флаг менять нельзя, меняем из своего проекта!

    public abstract void draw(Graphics graphics);

    public abstract void event(int code, int param, int x, int y);

    public abstract void setY(int top);

    public abstract void setX(int left);

    /**
     * A method to be used to call an ancestor's drawing method.
     *
     * @param graphics
     */
    public void superDraw(Graphics graphics) {
    }

}
