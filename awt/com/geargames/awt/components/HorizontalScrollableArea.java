package com.geargames.awt.components;

import com.geargames.awt.Drawable;
import com.geargames.common.util.Region;
import com.geargames.common.Event;
import com.geargames.common.Graphics;

/**
 * User: mikhail v. kutuzov
 * Date: 27.11.12
 * Time: 16:32
 */
public abstract class HorizontalScrollableArea extends ScrollableArea {

    public void draw(Graphics graphics, int x, int y) {
        if (!isInitiated()) {
            initiate(graphics);
        }
        Region touchRegion = getTouchRegion();
        Region drawRegion = getDrawRegion();
        if (Drawable.DEBUG) {
            graphics.drawRect(x + touchRegion.getMinX(), y + touchRegion.getMinY(), touchRegion.getWidth(), touchRegion.getHeight());
            graphics.drawRect(x + drawRegion.getMinX(), y + drawRegion.getMinY(), drawRegion.getWidth(), drawRegion.getHeight());
        }

        int clipMin = x;
        int clipMax = clipMin + drawRegion.getWidth();
        graphics.setClip(x + drawRegion.getMinX(), y + drawRegion.getMinY(), drawRegion.getWidth(), drawRegion.getHeight());
        int localPosition = clipMin + getPosition();
        if (isStrictlyClipped()) {
            for (int i = 0; i < getItemsAmount(); i++) {
                if (localPosition >= clipMin && localPosition + getItemSize() <= clipMax) {
                    drawItem(graphics, i, localPosition, y);
                }
                localPosition += getItemSize();
            }
        } else {
            for (int i = 0; i < getItemsAmount(); i++) {
                if (localPosition + getItemSize() >= clipMin && localPosition - getItemSize() <= clipMax) {
                    drawItem(graphics, i, localPosition, y);
                }
                localPosition += getItemSize();
            }
        }
        graphics.resetClip();
    }

    /**
     * Show an item having an index equals to itemIndex on the graphical context in a place determined with position.
     *
     * @param graphics
     * @param itemIndex
     * @param position
     */
    public abstract void drawItem(Graphics graphics, int itemIndex, int position, int coordinate);


    /**
     * Выполнение всех манипуляций на один игровой тик
     */
    public boolean event(int code, int param, int x, int y) {
        if (getMotionListener() == null) {
            return false;
        }
        if (code == Event.EVENT_TICK) {
            getMotionListener().onTick();
            return false;
        } else {
            if (!isStuck()) {
                if (getTouchRegion().isWithIn(x, y)) {
                    switch (code) {
                        case Event.EVENT_KEY_RELEASED:
                        case Event.EVENT_TOUCH_RELEASED:
                            getMotionListener().onRelease(x);
                            break;
                        case Event.EVENT_KEY_REPEATED:
                        case Event.EVENT_TOUCH_MOVED:
                            getMotionListener().onMove(x);
                            break;
                        case Event.EVENT_KEY_PRESSED:
                        case Event.EVENT_TOUCH_PRESSED:
                            getMotionListener().onTouch(x);
                            break;
                    }
                    return true;
                } else {
                    getMotionListener().onOutOfBounds();
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    /**
     * Вернуть процентную долю списка прокрурученную от начальной позиции до текущей.
     *
     * @return
     */
    public int getScrollPercent() {
        int window = getItemsAmount() * getItemSize();
        if (getTouchRegion().getWidth() >= window) {
            return 0;
        } else {
            return (100 * ((getTouchRegion().getMinX() - getPosition()))) / window;
        }
    }
}
