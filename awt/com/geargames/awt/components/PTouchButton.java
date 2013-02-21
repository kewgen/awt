package com.geargames.awt.components;

import com.geargames.common.Event;
import com.geargames.common.packer.Index;
import com.geargames.common.packer.PFrame;
import com.geargames.common.packer.PObject;
import com.geargames.common.Graphics;

/**
 * User: mikhail v. kutuzov
 * Абстрактный класс кнопка, предназначен для зачитывания объекта структуры
 * Общие требования к пакерному объекту-прототипу:
 * s0 спрайт нажат
 * s1 спрайт отжат
 * s110 размер компонента в виде фрейма
 * Для кнопки существует только одно действие action() и происходит оно по нажатию/отжатию на поверхности кнопки.
 */
public abstract class PTouchButton extends PButton {

    protected PTouchButton(PObject prototype) {
        super(prototype);
    }

    public abstract void action();

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
