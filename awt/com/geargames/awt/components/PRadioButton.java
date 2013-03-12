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
 * Для кнопки существует только одно действие onClick(), которое происходит при переключении кнопки в зажатое состояние.
 * Пакерный объект-прототип кнопки должен содержать следующие спрайты:
 *     s0   спрайт отжатой кнопки;
 *     s1   спрайт зажатой кнопки;
 *     s2   спрайт выключенной кнопки (disabled);
 *     s110 фрейм задающий размеры кнопки.
 */
public abstract class PRadioButton extends PButton {
    private PRadioGroup group;

    protected PRadioButton(PObject prototype) {
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
     * Обработчик события, возникающего при изменении состояния кнопки с отжатой на зажатую или наоборот.
     */
    public void onCheckedChanged() {
        // Пустая реализация. Объект, желающий обрабатывать событие кнопки, должен перекрыть данный метод.
    }

}
