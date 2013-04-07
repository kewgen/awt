package com.geargames.awt;

import com.geargames.common.Port;

/**
 * User: m.v.kutuzov
 * Date: 05.04.13
 */
public abstract class Screen extends Drawable {

    public int getX() {
        return 0;
    }

    public void setX(int x) {
    }

    public int getY() {
        return 0;
    }

    public void setY(int y) {
    }

    public int getHeight() {
        return Port.getScreenH();
    }

    public int getWidth() {
        return Port.getScreenW();
    }

    public abstract void onShow();

    public abstract void onHide();
}
