package com.geargames.awt;

import com.geargames.common.Graphics;

/**
 * users: Mikhail V. Kutuzov, abarakov
 * date: 20.11.11
 * Базовый класс для сущностей, которые могут быть нарисованы на экране.
 */
public abstract class Drawable extends Eventable {

    public static boolean DEBUG = true; // Здесь флаг менять нельзя, меняем из своего проекта!

    /**
     * Нарисовать элемент на экране в точке (x, y).
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
