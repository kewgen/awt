package com.geargames.awt.components;

import com.geargames.common.Event;
import com.geargames.common.packer.PObject;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Абстрактный класс кнопки, на которую можно кликнуть, но при отжатии, кнопка возвращается в отжатое состояние.
 * Пакерный объект-прототип кнопки должен содержать следующие спрайты:
 *     s0    normal    слот кнопки в нормальном состоянии;
 *     s1    pushed    слот нажатой кнопки;
 *     s2    disabled  слот выключенной кнопки;
 *     s10   caption   слот для заголовка кнопки;
 *     s110  bounds    фрейм задающий размеры кнопки.
 */
public abstract class PTouchButton extends PButton {

    protected PTouchButton(PObject prototype) {
        super(prototype);
    }

    @Override
    public void setChecked(boolean checked) {
        // Кнопка не должна менять своего состояния никаким способом, кроме как по нажатию тачем.
    }

    @Override
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

    /**
     * Обработчик события возникающего при клике по поверхности кнопки.
     */
    @Override
    public void onClick() {
        // Пустая реализация. Объект, желающий обрабатывать событие кнопки, должен перекрыть данный метод.
    }

}
