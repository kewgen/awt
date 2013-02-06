package com.geargames.awt.utils;

import com.geargames.awt.Drawable;
import com.geargames.common.Event;
import com.geargames.packer.Graphics;

/**
 * User: mikhail.kutuzov
 * Date: 21.11.11
 * Time: 18:34
 */
public class LinearVanishingStrategy extends GraphicsStrategy {
    private int time;
    private int transparencyTime;
    private boolean transparent;
    private long tick;

    public LinearVanishingStrategy() {
        transparent = false;
    }

    public void event(int code, int param, int x, int y) {
        switch (code) {
            case Event.EVENT_TICK:
                tick++;
        }
    }

    public void draw(Graphics graphics, Drawable drawable) {
        if (!transparent) {
            if (tick > time) {
                transparent = true;
                return;
            }
            long ttime = time - tick;
            if (ttime < transparencyTime) {
                graphics.setTransparency((int) (100 - ((100 * ttime) / transparencyTime)));
            }
            drawable.superDraw(graphics);
            graphics.setTransparency(0);
        }
    }

    public void reset() {
        tick = 0;
        transparent = false;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isTransparent() {
        return transparent;
    }

    public int getTransparencyTime() {
        return transparencyTime;
    }

    public void setTransparencyTime(int transparencyTime) {
        this.transparencyTime = transparencyTime;
    }

}
