package com.geargames.awt;

/**
 * Базовый класс объектов способных обрабатывать события и уведомления.
 * User: abarakov
 * Date: 25.02.13
 */
public abstract class Eventable { // Notificable

    /**
     * Обработчик событий экранных касаний, клавиатуры и др.
     * @param code  идентификатор события, одно из значений com.geargames.common.Event.EVENT_TOUCH_*;
     * @param param дополнительный параметр, используемый некоторыми типами событий;
     * @param x     координата по оси Х в локальной системе координат объекта;
     * @param y     координата по оси Y в локальной системе координат объекта;
     * @return      true, если дальнейшие события будут поглощаться этим объектом, иначе - false.
     */
    // code ~ eventId
    public boolean onEvent(int code, int param, int x, int y) {
        // Пустая реализация. Объект, желающий обрабатывать события, должен перекрыть данный метод.
        return false;
    }

}
