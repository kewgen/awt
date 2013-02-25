package com.geargames.awt;


import com.geargames.common.Graphics;

/**
 * user: Mikhail V. Kutuzov
 * date: 20.11.11
 * time: 10:37
 * Базовый класс для сущностей которые могут быть нарисованы на экране
 * */
public abstract class Drawable {
    public static boolean DEBUG = true;//сдесь флаг менять нельзя, меняем из своего проекта!

    public abstract void draw(Graphics graphics);

    /**
     * Передать событие code нарисованному объекту.
     * @param code
     * @param param
     * @param x
     * @param y
     * @return true, если дальнейшие события будут поглощаться этим объектом, иначе - false
     */
    public abstract boolean event(int code, int param, int x, int y);

    public abstract void setY(int y);

    public abstract void setX(int x);

    public abstract int getX();

    public abstract int getY();

    /**
     * A method to be used to call an ancestor's drawing method.
     *
     * @param graphics
     */
    public void superDraw(Graphics graphics) {
    }

    public abstract int getHeight();

    public abstract int getWidth();
}
