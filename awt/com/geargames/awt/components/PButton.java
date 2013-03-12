package com.geargames.awt.components;

import com.geargames.common.Graphics;
import com.geargames.common.String;
import com.geargames.common.env.SystemEnvironment;
import com.geargames.common.packer.Index;
import com.geargames.common.packer.PObject;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Date: 03.12.12
 * Абстрактный класс кнопки, предназначен для зачитывания структуры объекта.
 * Кнопка может находиться в одном из двух состояний - отжатом или зажатом.
 * Способ переключения состояния кнопки задается логикой работы классов-наследников.
 * Пакерный объект-прототип кнопки должен содержать следующие спрайты:
 *     s0   спрайт отжатой кнопки;
 *     s1   спрайт зажатой кнопки;
 *     s2   спрайт выключенной кнопки (disabled);
 *     s110 фрейм задающий размеры кнопки.
 */
public abstract class PButton extends PObjectElement {
    private Index normalSkin;
    private Index pushedSkin;
    private Index disabledSkin;
    private boolean checked;

    public PButton(PObject prototype) {
        super(prototype);
        normalSkin   = prototype.getIndexBySlot(0);
        pushedSkin   = prototype.getIndexBySlot(1);
        disabledSkin = prototype.getIndexBySlot(2);
        if (disabledSkin == null) {
            disabledSkin = normalSkin;
            SystemEnvironment.getInstance().getDebug().trace(String.valueOfC(
                   "PButton: There is no skin of disabled state button ").concatC("pid= ").concatI(prototype.getPID()));
        }
        checked = false;
    }

    public void draw(Graphics graphics, int x, int y) {
        if (!getEnabled()) {
            disabledSkin.draw(graphics, x, y);
        } else {
            if (checked) {
                pushedSkin.draw(graphics, x, y);
            } else {
                normalSkin.draw(graphics, x, y);
            }
        }
    }

    /**
     * Вернуть скин кнопки в отжатом состоянии.
     */
    public Index getNormalSkin() {
        return normalSkin;
    }

    /**
     * Вернуть скин кнопки в зажатом состоянии.
     */
    public Index getPushedSkin() {
        return pushedSkin;
    }

    /**
     * Вернуть скин кнопки в выключенном состоянии.
     */
    public Index getDisabledSkin() {
        return disabledSkin;
    }

    /**
     * Вернет true, если кнопка зажата и false, если отжата.
     */
    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    /**
     * Обработчик события возникающего при клике тачем по кнопке.
     */
    // action
    public abstract void onClick();

}
