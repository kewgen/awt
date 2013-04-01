package com.geargames.awt.components;

import com.geargames.awt.Drawable;
import com.geargames.common.util.Region;
import com.geargames.common.Event;
import com.geargames.common.Graphics;

/**
 * User: mikhail v. kutuzov, abarakov
 * Date: 27.11.12
 */
public abstract class HorizontalScrollableArea extends ScrollableArea {

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
        int localPosition = x + getPosition();
        int clipMin = x + drawRegion.getMinX();
        int clipMax = clipMin + drawRegion.getWidth();
        if (isStrictlyClipped()) {
            for (int i = 0; i < getItemsAmount(); i++) {
                if (localPosition >= clipMin && localPosition + getItemSize() <= clipMax) {
                    drawItem(graphics, i, localPosition, y + getItemOffsetY());
                }
                localPosition += getItemSize();
            }
        } else {
            for (int i = 0; i < getItemsAmount(); i++) {
                if (localPosition + getItemSize() > clipMin && localPosition - getItemSize() <= clipMax) {
                    drawItem(graphics, i, localPosition, y + getItemOffsetY());
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
                case Event.EVENT_TOUCH_PRESSED:
                    getMotionListener().onTouch(x);
                    break;
                case Event.EVENT_TOUCH_MOVED:
                    getMotionListener().onMove(x);
                    break;
                case Event.EVENT_TOUCH_RELEASED:
                    getMotionListener().onRelease(x);
                    break;
            }
            return true;
        } else {
            getMotionListener().onOutOfBounds();
            return false;
        }
    }

    /**
     * Вернуть ширину видимой области.
     *
     * @return
     */
    @Override
    public int getDrawableAreaSize() {
        return getDrawRegion().getWidth();
    }

    /**
     * Вернуть процентную долю списка прокрученную от начальной позиции до текущей.
     *
     * @return
     */
    @Override
    public int getScrollPercent() {
        int window = getItemsAmount() * getItemSize();
//      window -= getDrawRegion().getWidth(); //todo: Вероятно это тоже нужно?
        if (getTouchRegion().getWidth() >= window) {
            return 0;
        } else {
            return (100 * (getTouchRegion().getMinX() - getPosition())) / window;
        }
    }


    /**
     * Вернуть смещение осей координат по оси y для всех элементов списка.
     * @return
     */
    public abstract int getItemOffsetY();
}
