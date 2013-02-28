package com.geargames.awt.components;

import com.geargames.common.Event;
import com.geargames.common.packer.PObject;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Абстрактный класс кнопки, на которую можно кликнуть, но при отжатии, кнопка возвращается в отжатое состояние.
 * Для кнопки существует только одно действие action(), которое происходит по клику над поверхностью кнопки.
 * Пакерный объект-прототип кнопки должен содержать следующие спрайты:
 *     s0   спрайт зажатой кнопки;
 *     s1   спрайт отжатой кнопки;
 *     s110 фрейм задающий размеры кнопки.
 */
public abstract class PTouchButton extends PButton {

    protected PTouchButton(PObject prototype) {
        super(prototype);
    }

    public boolean event(int code, int param, int x, int y) {
        switch (code) {
            case Event.EVENT_TOUCH_PRESSED:
                setState(true);
                return true;
            case Event.EVENT_TOUCH_RELEASED:
                if (isState() && getTouchRegion().isWithIn(x, y)) {
                    action();
                }
                setState(false);
                break;
        }
        return false;
    }

}
