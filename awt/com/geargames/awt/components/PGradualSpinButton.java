package com.geargames.awt.components;

import com.geargames.awt.timers.TimerManager;
import com.geargames.common.Event;
import com.geargames.common.packer.PObject;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Date: 27.12.12
 */
public class PGradualSpinButton extends PButton {

    private static final int REPEAT_DELAY    = 500;
    private static final int REPEAT_INTERVAL = 100;

    private PGradualSpinBox parentBox;
    private int step;
    private int tickCounter;
    private int timerId;

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

    public boolean onEvent(int code, int param, int x, int y) {
        if (code == Event.EVENT_TOUCH_PRESSED) {
            tickCounter = 0;
            timerId = TimerManager.setSingleTimer(REPEAT_DELAY, this);
            parentBox.setValue(parentBox.getValue() + step);
        } else
        if (code == Event.EVENT_TOUCH_RELEASED) {
            TimerManager.killTimer(timerId);
        }
        return super.onEvent(code, param, x, y);
    }

    /**
     * Метод вызывается каждый раз при срабатывании таймера.
     * @param timerId - идентификатор сработавшего таймера, который вызвал данный метод.
     */
    public void onTimer(int timerId) {
        if (timerId == this.timerId) {
            tickCounter++;
            if (tickCounter >= 20) {
                int value = step * 5;
                value = ((parentBox.getValue() + value) / value) * value;
                parentBox.setValue(value);
            } else {
                if (tickCounter == 1) {
                    TimerManager.setPeriodicTimer(this.timerId, REPEAT_INTERVAL, this);
                }
                parentBox.setValue(parentBox.getValue() + step);
            }
        }
    }

    public void onClick() {
    }

}
