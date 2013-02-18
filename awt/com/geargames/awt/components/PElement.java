package com.geargames.awt.components;

import com.geargames.common.util.Region;
import com.geargames.common.Graphics;

/**
 * User: mikhail v. kutuzov
 * Базовый класс любого компонента пользовательского интерфейса.
 */
public abstract class PElement {
    private int x;
    private int y;

    /**
     * Нарисовать элемент на экране в точке (x, y).
     * @param graphics
     * @param x координата по оси Х в локальной системе координат объекта.
     * @param y координата по оси Y в локальной системе координат объекта.
     */
    public abstract void draw(Graphics graphics, int x, int y);

    /**
     * Передать событие code нарисованному объекту.
     * @param code
     * @param param
     * @param x координата по оси Х в локальной системе координат объекта.
     * @param y координата по оси Y в локальной системе координат объекта.
     * @return true если дальнейшие события будут поглащаться этим объектом, иначе - false
     */
    public abstract boolean event(int code, int param, int x, int y);

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
     * Смещение объекта относительно его родителя по оси X.
     * @return
     */
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    /**
     * Смещение объекта относительно его родителя по оси Y.
     * @return
     */
    public int getY() {
        return y;
    }


    public void setY(int y) {
        this.y = y;
    }

    //TODO нужно ли элементу игровой панели становиться не видимым на этой панели? по-моему - нет.
    /**
     * Видим ли компонент на своём родителе.
     * @return
     */
    public abstract boolean isVisible();
}
