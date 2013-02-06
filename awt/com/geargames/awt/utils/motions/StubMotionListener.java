package com.geargames.awt.utils.motions;

import com.geargames.awt.utils.MotionListener;

/**
 * user: Mikhail V. Kutuzov
 * date: 24.10.11
 * time: 1:21
 */
public class StubMotionListener extends MotionListener {
    private int position;

    public void create(int top) {
        this.position = top;
    }

    public void onTouch(int y) {
    }

    public void onMove(int y) {
    }

    public void onTick() {
    }

    public int getTop() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void onRelease(int y) {
    }

    public void onClick(int number) {
    }

    public void onOutOfBounds() {
    }

    public boolean isCentered() {
        return false;
    }
}
