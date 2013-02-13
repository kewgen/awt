package com.geargames.awt.components;

import com.geargames.common.Event;
import com.geargames.common.packer.PObject;

/**
 * User: mikhail v. kutuzov
 * Кнопка меняет своё состояние по нажатию.
 */
public abstract class PToggleButton extends PButton {
    public PToggleButton(PObject prototype) {
        super(prototype);
    }

    protected abstract void action();

    public boolean event(int code, int param, int x, int y) {
        if (Event.EVENT_TOUCH_PRESSED == code && getTouchRegion().isWithIn(x, y)) {
            setState(!isState());
            action();
        }
        return false;
    }

}
