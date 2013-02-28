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
 * Для кнопки существует только одно действие action(), которое происходит при переключении кнопки в зажатое состояние.
 * Пакерный объект-прототип кнопки должен содержать следующие спрайты:
 *     s0   спрайт зажатой кнопки;
 *     s1   спрайт отжатой кнопки;
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
            this.group = group;
            if (isState()) {
                group.reset(this);
            }
        }
    }

    public boolean event(int code, int param, int x, int y) {
        if (code == Event.EVENT_TOUCH_PRESSED) {
            if (getTouchRegion().isWithIn(x, y) && !isState()) {
                group.reset(this);
                action();
            }
        }
        return false;
    }

}
