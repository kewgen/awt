package com.geargames.awt.components;

import com.geargames.common.Event;
import com.geargames.common.packer.PObject;

/**
 * User: mikhail v. kutuzov
 * Date: 03.12.12
 * Time: 20:58
 */
public abstract class PToggleButton extends PButton {
    private PToggleGroup group;

    protected PToggleButton(PObject prototype) {
        super(prototype);
    }

    public PToggleGroup getGroup() {
        return group;
    }

    public void setGroup(PToggleGroup group) {
        this.group = group;
    }

    protected abstract void action();

    public boolean event(int code, int param, int x, int y) {
        if (getTouchRegion().isWithIn(x, y)) {
            switch (code) {
                case Event.EVENT_TOUCH_PRESSED:
                    if(!isState()){
                        group.reset(this);
                        action();
                    }
            }
        }
        return false;
    }
}
