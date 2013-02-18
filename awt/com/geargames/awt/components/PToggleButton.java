package com.geargames.awt.components;

import com.geargames.common.Event;
import com.geargames.common.packer.PObject;

/**
 * User: mikhail v. kutuzov
 * Кнопка переключающая своё состояние по клику с отжатого на зажатое и обратно.
 */
public abstract class PToggleButton extends PButton {

    public PToggleButton(PObject prototype) {
        super(prototype);
    }

    protected abstract void action();

    public boolean event(int code, int param, int x, int y) {
        if (code == Event.EVENT_TOUCH_PRESSED && getTouchRegion().isWithIn(x, y)) {
            setState(!isState());
            action();
        }
        return false;
    }

}
