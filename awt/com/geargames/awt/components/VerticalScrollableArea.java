package com.geargames.awt.components;

import com.geargames.awt.Drawable;
import com.geargames.common.util.Region;
import com.geargames.common.Event;
import com.geargames.common.Graphics;

/**
 * User: mikhail v. kutuzov, abarakov
 * Date: 27.11.12
 */
public abstract class VerticalScrollableArea extends ScrollableArea {

    @Override
    public void draw(Graphics graphics, int x, int y) {
        if (!isInitiated()) {
            initiate(graphics);
        }
        Region drawRegion = getDrawRegion();
        if (Drawable.DEBUG) {
            Region touchRegion = getTouchRegion();
            graphics.drawRect(x + touchRegion.getMinX(), y + touchRegion.getMinY(), touchRegion.getWidth(), touchRegion.getHeight());
            graphics.drawRect(x + drawRegion.getMinX(), y + drawRegion.getMinY(), drawRegion.getWidth(), drawRegion.getHeight());
        }

        graphics.setClip(x + drawRegion.getMinX(), y + drawRegion.getMinY(), drawRegion.getWidth(), drawRegion.getHeight());
        int localPosition = y + getPosition();
        int clipMin = y + drawRegion.getMinY();
        int clipMax = clipMin + drawRegion.getHeight();
        if (isStrictlyClipped()) {
            for (int i = 0; i < getItemsAmount(); i++) {
                if (localPosition >= clipMin && localPosition + getItemSize() <= clipMax) {
                    drawItem(graphics, i, x + getItemOffsetX(), localPosition);
                }
                localPosition += getItemSize();
            }
        } else {
            for (int i = 0; i < getItemsAmount(); i++) {
                if (localPosition + getItemSize() > clipMin && localPosition - getItemSize() <= clipMax) {
                    drawItem(graphics, i, x + getItemOffsetX(), localPosition);
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
     * @param x
     * @param y
     */
    public abstract void drawItem(Graphics graphics, int itemIndex, int x, int y);

    /**
     * Обработчик событий экранных касаний и клавиатуры.
     */
    @Override
    public boolean onEvent(int code, int param, int x, int y) {
        if (isStuck() || getMotionListener() == null) {
            return false;
        }
        if (getTouchRegion().isWithIn(x, y)) {
            switch (code) {
//                case Event.EVENT_KEY_PRESSED:
                case Event.EVENT_TOUCH_PRESSED:
                    getMotionListener().onTouch(y);
                    break;
//                case Event.EVENT_KEY_REPEATED:
                case Event.EVENT_TOUCH_MOVED:
                    getMotionListener().onMove(y);
                    break;
//                case Event.EVENT_KEY_RELEASED:
                case Event.EVENT_TOUCH_RELEASED:
                    getMotionListener().onRelease(y);
                    break;
            }
            return true;
        } else {
            getMotionListener().onOutOfBounds();
            return false;
        }
    }

    /**
     * Вернуть высоту видимой области
     *
     * @return
     */
    @Override
    public int getDrawableAreaSize() {
        return getDrawRegion().getHeight();
    }

    /**
     * Вернуть процентную долю списка прокрученную от начальной позиции до текущей.
     *
     * @return
     */
    @Override
    public int getScrollPercent() {
        int window = getItemsAmount() * getItemSize();
//      window -= getDrawRegion().getHeight(); //todo: Вероятно это тоже нужно?
        if (getTouchRegion().getHeight() >= window) {
            return 0;
        } else {
            return (100 * (getTouchRegion().getMinY() - getPosition())) / window;
        }
    }


    /**
     * Вернуть смещение по оси x для всех элементов списка.
     * @return
     */
    public abstract int getItemOffsetX();
}
