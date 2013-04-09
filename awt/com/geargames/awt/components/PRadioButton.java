package com.geargames.awt.components;

import com.geargames.common.Event;
import com.geargames.common.packer.PObject;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Date: 03.12.12
 * Абстрактный класс кнопки, которая обазательно должна входить в группу PRadioGroup и которая может переключаться между
 * двумя состояниями - зажатая/отжатая. Переключение производится по клику над поверхностью кнопки, при этом, если в
 * группе PRadioGroup есть другая кнопка, находящаяся в зажатом состоянии, то она будет отжата.
 * В группе PRadioGroup только одна кнопка может находиться в зажатом состоянии.
 * Пакерный объект-прототип кнопки должен содержать следующие спрайты:
 *     s0    normal    слот кнопки в нормальном состоянии;
 *     s1    pushed    слот нажатой кнопки;
 *     s2    disabled  слот выключенной кнопки;
 *     s10   caption   слот для заголовка кнопки;
 *     s110  bounds    фрейм задающий размеры кнопки.
 */
public class PRadioButton extends PButton {
    private PRadioGroup group;

    public PRadioButton(PObject prototype) {
        super(prototype);
    }

    public PRadioGroup getGroup() {
        return group;
    }

    public void setGroup(PRadioGroup group) {
        if (this.group != group) {
            if (this.group != null) {
                group.removeButton(this);
            }
            this.group = group;
            if (this.group != null) {
                group.addButton(this);
            }
            if (getChecked()) {
                group.reset(this);
            }
        }
    }

    @Override
    public void setChecked(boolean checked) {
        if (this.getChecked() != checked) {
            super.setChecked(checked);
            if (checked) {
                if (group != null) {
                    group.reset(this);
                }
//              onClick();
            }
            onCheckedChanged();
        }
    }

    @Override
    public boolean onEvent(int code, int param, int x, int y) {
        if (code == Event.EVENT_TOUCH_PRESSED) {
            if (getTouchRegion().isWithIn(x, y)) {
                setChecked(true);
                if (getChecked()) {
                    onClick();
                }
            }
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
     * Обработчик события, возникающего при изменении состояния кнопки с отжатой на зажатую или наоборот.
     */
    public void onCheckedChanged() {
        // Пустая реализация. Объект, желающий обрабатывать событие кнопки, должен перекрыть данный метод.
    }

}
