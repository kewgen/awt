package com.geargames.awt.components;

import com.geargames.Debug;
import com.geargames.awt.timers.TimerIdMap;
import com.geargames.awt.timers.TimerManager;
import com.geargames.common.Event;
import com.geargames.common.packer.PObject;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Date: 27.12.12
 * Time: 13:13
 */
public class PGradualSpinButton extends PTouchButton {
    private PGradualSpinBox parentBox;
    private int step;
    private int tickCounter;

    public PGradualSpinButton(PObject prototype) {
        super(prototype);
        this.step = 1;
        tickCounter = 0;
    }

    public void setBox(PGradualSpinBox box) {
        this.parentBox = box;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public boolean event(int code, int param, int x, int y) {
        if (code == Event.EVENT_TOUCH_PRESSED) {
            tickCounter = 0;
            TimerManager.setSingleTimer(TimerIdMap.AWT_SPINBOX_BUTTON_TICK, 500, this);
            parentBox.setValue(parentBox.getValue() + step);
//            pulse(step);
        } else
        if (code == Event.EVENT_TOUCH_RELEASED) {
            TimerManager.killTimer(TimerIdMap.AWT_SPINBOX_BUTTON_TICK);
        } else
        if (code == Event.EVENT_TIMER && param == TimerIdMap.AWT_SPINBOX_BUTTON_TICK) {
            tickCounter++;
            if (tickCounter >= 20) {
                int value = step * 5;
                value = ((parentBox.getValue() + value) / value) * value;
                parentBox.setValue(value);
            } else {
                if (tickCounter == 1) {
                    TimerManager.setPeriodicTimer(TimerIdMap.AWT_SPINBOX_BUTTON_TICK, 100, this);
                }
                parentBox.setValue(parentBox.getValue() + step);
            }
//            Debug.trace("value: " + parentBox.getValue());
//            pulse(scalableStep);
            return false;
        }
        return super.event(code, param, x, y);
    }

//    protected void pulse(int scalableStep) {
//        parentBox.setValue(parentBox.getValue() + scalableStep);
//    }

    public void action() {
//        pulse(step);
//        tickCounter = 0;
    }

}
