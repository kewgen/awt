package com.geargames.awt;

/**
 * Базовый класс компонентов способных обрабатывать события и уведомления.
 * User: abarakov
 * Date: 25.02.13
 */
public abstract class Eventable { // AWTObject, Notificable

//    /**
//     * Нарисовать элемент на экране в точке (x, y).
//     * @param graphics
//     * @param x координата по оси Х в локальной системе координат объекта.
//     * @param y координата по оси Y в локальной системе координат объекта.
//     */
//    public abstract void draw(Graphics graphics, int x, int y);

    /**
     * Передать событие с идентификатором code объекту
     * @param code  одно из значений com.geargames.common.Event.EVENT_TOUCH_*
     * @param param
     * @param x     координата по оси Х в локальной системе координат объекта.
     * @param y     координата по оси Y в локальной системе координат объекта.
     * @return true, если дальнейшие события будут поглощаться этим объектом, иначе - false
     */
    public boolean onEvent(int code, int param, int x, int y) {
        // Пустая реализация. Объект, желающий обрабатывать события, должен перекрыть данный метод.
        return false;
    }

    /**
     * Метод вызывается каждый раз при срабатывании таймера.
     * @param timerId - идентификатор сработавшего таймера, который вызвал данный метод.
     */
    public void onTimer(int timerId) {
        // Пустая реализация. Объект, желающий обрабатывать события таймеров, должен перекрыть данный метод.
    }

}
