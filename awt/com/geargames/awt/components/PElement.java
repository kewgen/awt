package com.geargames.awt.components;

import com.geargames.awt.Eventable;
import com.geargames.common.Graphics;
import com.geargames.common.util.Region;

/**
 * User: mikhail v. kutuzov, abarakov
 * Базовый класс любого компонента пользовательского интерфейса.
 */
public abstract class PElement extends Eventable {

    private static final byte VISIBLE_STATE = 1 << 0;
    private static final byte ENABLED_STATE  = 1 << 1;

    private int x;
    private int y;
    private byte state;

    public PElement() {
        state = VISIBLE_STATE | ENABLED_STATE;
    }

    /**
     * Нарисовать элемент на экране в точке (x, y).
     * @param graphics графический контекст, на котором должен быть нарисован объект.
     * @param x        координата по оси Х в локальной системе координат объекта.
     * @param y        координата по оси Y в локальной системе координат объекта.
     */
    public abstract void draw(Graphics graphics, int x, int y);

    /**
     * Вернуть область в рамках родительского компонента, которая содержит текущий объект.
     * @return
     */
    public abstract Region getDrawRegion();

    /**
     * Вернуть область касания текущего компонента в рамках его родителя.
     * @return
     */
    public abstract Region getTouchRegion();

    /**
     * Вернуть смещение объекта относительно его родителя по оси X.
     * @return
     */
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    /**
     * Вернуть смещение объекта относительно его родителя по оси Y.
     * @return
     */
    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * Вернет true, если компонент видим на своём родителе.
     * @return
     */
    // isVisible
    public boolean getVisible() {
        return (state & VISIBLE_STATE) == VISIBLE_STATE;
    }

    public void setVisible(boolean visible) {
        if (visible) {
            state = (byte) (state | VISIBLE_STATE);
        } else {
            state = (byte) (state & ~VISIBLE_STATE);
        }
    }

    /**
     * Вернет true, если компонент включен, т.е. компоненту разрешается обработка сообщений.
     * @return
     */
    public boolean getEnabled() {
        return (state & ENABLED_STATE) == ENABLED_STATE;
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            state = (byte) (state | ENABLED_STATE);
        } else {
            state = (byte) (state & ~ENABLED_STATE);
        }
    }

}
