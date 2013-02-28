package com.geargames.awt.components;

import com.geargames.common.Event;
import com.geargames.common.packer.PObject;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Абстрактный класс кнопки, которая может переключаться между двумя состояниями - зажатая/отжатая. Переключение
 * производится по клику над поверхностью кнопки.
 * Для кнопки существует только одно действие action(), которое происходит при переключении состояния кнопки.
 * Пакерный объект-прототип кнопки должен содержать следующие спрайты:
 *     s0   спрайт зажатой кнопки;
 *     s1   спрайт отжатой кнопки;
 *     s110 фрейм задающий размеры кнопки.
 */
public abstract class PToggleButton extends PButton {

    public PToggleButton(PObject prototype) {
        super(prototype);
    }

    public boolean event(int code, int param, int x, int y) {
        if (code == Event.EVENT_TOUCH_PRESSED && getTouchRegion().isWithIn(x, y)) {
            setState(!isState());
            action();
        }
        return false;
    }

}
