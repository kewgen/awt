package com.geargames.common.timers;

/**
 * User: abarakov
 * Date: 25.02.13 10:16
 * Список кодов системных таймеров (таймеров общего кода).
 */
public class TimerIdMap {
    /*
     * Пожалуйста, выбирайте id следуя следующему правилу:
     *     отрицательные id - для всех системных таймеров (таймеров общего кода);
     *     id = 0..19999 - для клиентских таймеров с константным id.
     */

    // awt components timers
    public final static int AWT_TEXTHINT_GRAPHICS_STRATEGY_TICK = -5001;
//    public final static int AWT_SPINBOX_BUTTON_TICK             = -6001;

}