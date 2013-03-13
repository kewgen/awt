package com.geargames.awt;

import com.geargames.common.Graphics;

/**
 * Базовый класс для объектов, которые могут быть нарисованы.
 * Users: mikhail v. kutuzov, abarakov
 * Date: 20.11.11
 */
public abstract class Drawable extends Eventable {

    public static boolean DEBUG = true; // Здесь флаг менять нельзя, меняем аналогичный флаг в своем проекте!

    /**
     * Нарисовать объект на графическом контексте в его собственной локальной системе координат.
     *
     * @param graphics графический контекст, на котором должен быть нарисован объект.
     */
    public abstract void draw(Graphics graphics);

    /**
     * A method to be used to call an ancestor's drawing method.
     *
     * @param graphics графический контекст, на котором должен быть нарисован объект.
     */
    public void superDraw(Graphics graphics) {
    }

    /**
     * Вернуть смещение объекта относительно его родителя по оси X.
     * @return
     */
    public abstract int getX();

    public abstract void setX(int x);

    /**
     * Вернуть смещение объекта относительно его родителя по оси Y.
     * @return
     */
    public abstract int getY();

    public abstract void setY(int y);

    public abstract int getHeight();

    public abstract int getWidth();
}
