package com.geargames.awt.utils;

import com.geargames.awt.Drawable;
import com.geargames.common.Event;
import com.geargames.common.Graphics;

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
        transparent = true;
    }

    public void event(int code, int param, int x, int y) {
        if (!transparent) {
            switch (code) {
                case Event.EVENT_TICK:
                    tick++;
            }
        }
    }

    public void draw(Graphics graphics, Drawable drawable) {
        if (!transparent) {
            if (tick > time) {
                transparent = true;
                return;
            }
            long tTime = time - tick;
            if (tTime <= transparencyTime) {
                int old = graphics.getTransparency();
                graphics.setTransparency((int) (100 - ((100 * tTime) / transparencyTime)));
                drawable.superDraw(graphics);
                graphics.setTransparency(old);
            } else {
                drawable.superDraw(graphics);
            }
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
