package com.geargames.common.timers;

/**
 * Базовый класс объектов способных обрабатывать события таймеров.
 * User: abarakov
 * Date: 25.02.13
 */
public interface TimerListener { // TimerNotificable

    /**
     * Метод вызывается каждый раз при срабатывании таймера.
     * @param timerId - идентификатор сработавшего таймера, который вызвал данный метод.
     */
    void onTimer(int timerId);

}
