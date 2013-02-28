package com.geargames.awt.components;

import com.geargames.common.Graphics;
import com.geargames.common.packer.Index;
import com.geargames.common.packer.PObject;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Date: 03.12.12
 * Абстрактный класс кнопки, предназначен для зачитывания структуры объекта.
 * Кнопка может находиться в одном из двух состояний:
 *     зажатом (при isState() == true);
 *     отжатом (при isState() == false).
 * Способ переключения состояния кнопки задается логикой работы классов-наследников.
 * Пакерный объект-прототип кнопки должен содержать следующие спрайты:
 *     s0   спрайт зажатой кнопки;
 *     s1   спрайт отжатой кнопки;
 *     s110 фрейм задающий размеры кнопки.
 */
public abstract class PButton extends PObjectElement {
    private Index pushedSkin;
    private Index poppedSkin;
    private boolean state;

    public PButton(PObject prototype) {
        super(prototype);
        pushedSkin = prototype.getIndexBySlot(0);
        poppedSkin = prototype.getIndexBySlot(1);
        state = false;
    }

    public void draw(Graphics graphics, int x, int y) {
        if (state) {
            pushedSkin.draw(graphics, x, y);
        } else {
            poppedSkin.draw(graphics, x, y);
        }
    }

    /**
     * Вернуть скин кнопки в зажатом состоянии
     */
    public Index getPushedSkin() {
        return pushedSkin;
    }

    /**
     * Вернуть скин кнопки в отжатом состоянии
     */
    public Index getPoppedSkin() {
        return poppedSkin;
    }

    /**
     * Вернуть состояние кнопки. True, если кнопка зажата и false, если отжата
     */
    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public abstract void action();

}
