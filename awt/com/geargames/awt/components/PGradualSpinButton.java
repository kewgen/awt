package com.geargames.awt.components;

import com.geargames.Debug;
import com.geargames.common.Event;
import com.geargames.common.packer.PObject;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Date: 27.12.12
 * Time: 13:13
 */
public class PGradualSpinButton extends PTouchButton {
    private byte counter;
    private int fps;
    private int tickCounter;
    private PGradualSpinBox parentBox;
    private byte direction = 0;

    public PGradualSpinButton(PObject prototype, boolean isIncreaseDirection) {
        super(prototype);
        tickCounter = 0;
        counter = 0;
        this.direction = (byte) (isIncreaseDirection ? 1 : -1);
    }

    /**
     * Вернуть число кадров в секунду для приложения.
     * @return
     */
    public int getFps() {
        return fps;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    protected void second() {
        parentBox.setValue((short) (parentBox.getValue() + counter * direction));
    }

    public boolean event(int code, int param, int x, int y) {
        if (code == Event.EVENT_TICK) {
            if (isState()) {
                if (tickCounter++ % fps == 0) {
                    int seconds = tickCounter / fps;
                    Debug.trace("tick counter " + tickCounter + " fps " + fps);
                    Debug.trace("seconds " + seconds);
                    if (seconds < 10) {
                        counter = 1;
                    } else if (seconds < 19) {
                        counter = 10;
                    } else {
                        counter = 100;
                    }
                    second();
                }
            }
            return false;
        }
        return super.event(code, param, x, y);
    }

    public void action() {
//        parentBox.setValue((short) (parentBox.getValue() + (counter == 0 ? 1 : counter) * direction));
        tickCounter = 0;
        counter = 0;
    }

    public void setBox(PGradualSpinBox box) {
        this.parentBox = box;
    }
}
