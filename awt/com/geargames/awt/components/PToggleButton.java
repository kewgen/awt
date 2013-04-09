package com.geargames.awt.components;

import com.geargames.common.Event;
import com.geargames.common.packer.PObject;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Абстрактный класс кнопки, которая может переключаться между двумя состояниями - зажатая и отжатая. Переключение
 * производится по клику над поверхностью кнопки.
 * При задании соответствующего скина для кнопки данного класса можно реализовать checkBox.
 * Пакерный объект-прототип кнопки должен содержать следующие спрайты:
 *     s0    normal    слот кнопки в нормальном состоянии;
 *     s1    pushed    слот нажатой кнопки;
 *     s2    disabled  слот выключенной кнопки;
 *     s10   caption   слот для заголовка кнопки;
 *     s110  bounds    фрейм задающий размеры кнопки.
 */
public class PToggleButton extends PButton {

    public PToggleButton(PObject prototype) {
        super(prototype);
    }

    @Override
    public void setChecked(boolean checked) {
        if (this.getChecked() != checked) {
            super.setChecked(checked);
            onCheckedChanged();
        }
    }

    @Override
    public boolean onEvent(int code, int param, int x, int y) {
        if (code == Event.EVENT_TOUCH_PRESSED && getTouchRegion().isWithIn(x, y)) {
            setChecked(!getChecked());
//          if (getChecked()) {
                onClick();
//          }
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

    /**
     * Обработчик события возникающего при изменении состояния кнопки с отжатой на зажатую или наоборот.
     */
    public void onCheckedChanged() {
        // Пустая реализация. Объект, желающий обрабатывать событие кнопки, должен перекрыть данный метод.
    }

}
