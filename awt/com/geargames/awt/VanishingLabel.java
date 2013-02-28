package com.geargames.awt;

import com.geargames.awt.utils.LinearVanishingStrategy;
import com.geargames.common.Graphics;

/**
 * User: mikhail.kutuzov
 * Date: 27.11.11
 * Time: 20:50
 */
public class VanishingLabel extends Label {
    private LinearVanishingStrategy graphicsStrategy;

    public VanishingLabel() {
        graphicsStrategy = new LinearVanishingStrategy(this);
    }

    public void setHideTimeout(int time) {
        graphicsStrategy.setHideTimeout(time);
    }

    public void setStateChangeTime(int time) {
        graphicsStrategy.setStateChangeTime(time);
    }

    public void show() {
        graphicsStrategy.startShowing();
    }

    public boolean isFullTransparent() {
        return graphicsStrategy.isFullTransparent();
    }

    public void draw(Graphics graphics) {
        graphicsStrategy.draw(graphics);
    }

    public void superDraw(Graphics graphics) {
        super.draw(graphics);
    }

    public boolean event(int code, int param, int x, int y) {
        graphicsStrategy.event(code, param, x, y);
        return false;
    }
}
