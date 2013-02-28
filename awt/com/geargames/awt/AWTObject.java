package com.geargames.awt;

/**
 * user: abarakov
 * date: 25.02.13
 * Базовый класс любого компонента пользовательского интерфейса
 */
public abstract class AWTObject { // Eventable

//    /**
//     * Нарисовать элемент на экране в точке (x, y).
//     * @param graphics
//     * @param x координата по оси Х в локальной системе координат объекта.
//     * @param y координата по оси Y в локальной системе координат объекта.
//     */
//    public abstract void draw(Graphics graphics, int x, int y);

    /**
     * Передать событие code объекту.
     * @param code
     * @param param
     * @param x координата по оси Х в локальной системе координат объекта.
     * @param y координата по оси Y в локальной системе координат объекта.
     * @return true, если дальнейшие события будут поглощаться этим объектом, иначе - false
     */
    public abstract boolean event(int code, int param, int x, int y);

}
