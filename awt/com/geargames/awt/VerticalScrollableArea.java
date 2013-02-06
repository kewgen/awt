package com.geargames.awt;

import com.geargames.common.util.Region;
import com.geargames.common.Event;
import com.geargames.common.Graphics;

/**
 * User: mikhail v. kutuzov
 * Date: 27.11.12
 * Time: 16:17
 */
public abstract class VerticalScrollableArea extends ScrollableArea {

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
        int clipMin = y;
        int clipMax = clipMin + drawRegion.getHeight();
        graphics.setClip(x + drawRegion.getMinX(), y + drawRegion.getMinY(), drawRegion.getWidth(), drawRegion.getHeight());
        int localPosition = clipMin + getPosition();
        if (isStrictlyClipped()) {
            for (int i = 0; i < getItemsAmount(); i++) {
                if (localPosition >= clipMin && localPosition + getItemSize() <= clipMax) {
                    drawItem(graphics, i, localPosition, x);
                }
                localPosition += getItemSize();
            }
        } else {
            for (int i = 0; i < getItemsAmount(); i++) {
                if (localPosition + getItemSize() >= clipMin && localPosition - getItemSize() <= clipMax) {
                    drawItem(graphics, i, localPosition, x);
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
        switch (code) {
            case Event.EVENT_TICK:
                getMotionListener().onTick();
                return false;
        }
        if (!isStuck()) {
            if (getTouchRegion().isWithIn(x, y)) {
                switch (code) {
                    case Event.EVENT_KEY_RELEASED:
                    case Event.EVENT_TOUCH_RELEASED:
                        getMotionListener().onRelease(y);
                        break;
                    case Event.EVENT_KEY_REPEATED:
                    case Event.EVENT_TOUCH_MOVED:
                        getMotionListener().onMove(y);
                        break;
                    case Event.EVENT_KEY_PRESSED:
                    case Event.EVENT_TOUCH_PRESSED:
                        getMotionListener().onTouch(y);
                        break;
                }
                return true;
            } else {
                switch (code) {
                    case Event.EVENT_KEY_REPEATED:
                    case Event.EVENT_TOUCH_MOVED:
                        getMotionListener().onOutOfBounds();
                        break;
                }
                return false;
            }
        } else {
            return false;
        }
    }


    /**
     * Вернуть процентную долю списка прокрурученную от начальной позиции до текущей.
     *
     * @return
     */
    public int getScrollPercent() {
        int window = getItemsAmount() * getItemSize();

        if (getTouchRegion().getHeight() >= window) {
            return 0;
        } else {
            return (100 * ((getTouchRegion().getMinY() - getPosition()))) / window;
        }
    }

}
