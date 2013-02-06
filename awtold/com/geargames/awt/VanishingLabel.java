package com.geargames.awt;

import com.geargames.awt.utils.LinearVanishingStrategy;
import com.geargames.packer.Graphics;

/**
 * User: mikhail.kutuzov
 * Date: 27.11.11
 * Time: 20:50
 */
public class VanishingLabel extends Label {
    private LinearVanishingStrategy graphicsStrategy;

    public VanishingLabel() {
        graphicsStrategy = new LinearVanishingStrategy();
    }

    public void setTransparencyTime(int vanishingTime) {
        graphicsStrategy.setTransparencyTime(vanishingTime);
    }

    public void setTime(int time) {
        graphicsStrategy.setTime(time);
    }

    public void reset() {
        graphicsStrategy.reset();
    }

    public boolean isTransparent() {
        return graphicsStrategy.isTransparent();
    }

    public void draw(Graphics graphics) {
        graphicsStrategy.draw(graphics, this);
    }

    public void superDraw(Graphics graphics) {
        super.draw(graphics);
    }

    public void event(int code, int param, int x, int y) {
        graphicsStrategy.event(code, param, x, y);
    }
}
