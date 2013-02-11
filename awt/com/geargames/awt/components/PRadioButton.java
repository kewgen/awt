package com.geargames.awt.components;

import com.geargames.common.Event;
import com.geargames.common.packer.PObject;

/**
 * User: mikhail v. kutuzov
 * Date: 03.12.12
 * Time: 20:58
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
        this.group = group;
    }

    protected abstract void action();

    public boolean event(int code, int param, int x, int y) {
        if (Event.EVENT_TOUCH_PRESSED == code) {
            if (getTouchRegion().isWithIn(x, y) && !isState()) {
                group.reset(this);
                action();
            }
        }
        return false;
    }
}
