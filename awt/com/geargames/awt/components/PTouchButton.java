package com.geargames.awt.components;

import com.geargames.common.Event;
import com.geargames.common.packer.PObject;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Абстрактный класс кнопки, на которую можно кликнуть, но при отжатии, кнопка возвращается в отжатое состояние.
 * Для кнопки существует только одно действие onClick(), которое происходит по клику над поверхностью кнопки.
 * Пакерный объект-прототип кнопки должен содержать следующие спрайты:
 *     s0   спрайт отжатой кнопки;
 *     s1   спрайт зажатой кнопки;
 *     s2   спрайт выключенной кнопки (disabled);
 *     s110 фрейм задающий размеры кнопки.
 */
public abstract class PTouchButton extends PButton {

    protected PTouchButton(PObject prototype) {
        super(prototype);
    }

    public void setChecked(boolean checked) {
        // Кнопка не должна менять своего состояния никаким способом, кроме как по нажатию тачем.
    }

    public boolean onEvent(int code, int param, int x, int y) {
        switch (code) {
            case Event.EVENT_TOUCH_PRESSED:
                super.setChecked(true);
                return true;
            case Event.EVENT_TOUCH_RELEASED:
                boolean checked = getChecked();
                super.setChecked(false);
                if (checked && getTouchRegion().isWithIn(x, y)) {
                    onClick();
                }
                break;
        }
        return false;
    }

}
