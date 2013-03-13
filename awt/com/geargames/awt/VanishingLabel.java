package com.geargames.awt;

import com.geargames.awt.utils.LinearVanishingStrategy;
import com.geargames.common.Graphics;

/**
 * User: mikhail.kutuzov, abarakov
 * Date: 27.11.11
 */
public class VanishingLabel extends Label {
    private LinearVanishingStrategy graphicsStrategy;

    public VanishingLabel() {
        graphicsStrategy = new LinearVanishingStrategy(this);
    }

    public void setShowingTime(int time) {
        graphicsStrategy.setShowingTime(time);
    }

    public void setLifeTime(int time) {
        graphicsStrategy.setLifeTime(time);
    }

    public void setHidingTime(int time) {
        graphicsStrategy.setHidingTime(time);
    }

    public void show() {
        graphicsStrategy.startShowing();
    }

    public boolean isFullTransparent() {
        return graphicsStrategy.isFullTransparent();
    }

    @Override
    public void draw(Graphics graphics) {
        graphicsStrategy.draw(graphics);
    }

    @Override
    public void superDraw(Graphics graphics) {
        super.draw(graphics);
    }

}
