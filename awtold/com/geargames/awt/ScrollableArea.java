package com.geargames.awt;

import com.geargames.awt.utils.Region;
import com.geargames.common.Event;
import com.geargames.packer.Graphics;

/**
 * User: mikhail.kutuzov
 * Date: 04.11.11
 * Time: 20:04
 */
public abstract class ScrollableArea extends ScrollableRegion {
    private boolean stuck;
    private int top;
    private int left;
    private int width;
    private int height;
    private boolean strictlyClipped;

    public void draw(Graphics graphics) {
        if (!isInitiated()) {
            initiate(graphics);
        }
        int clipMin = isVertical() ? getClipRegion().getMinY() : getClipRegion().getMinX();
        int clipMax = isVertical() ? getClipRegion().getMinY() + getClipRegion().getHeight() : getClipRegion().getMinX() + getClipRegion().getWidth();
        super.draw(graphics);
        graphics.setClip(getClipRegion().getMinX(), getClipRegion().getMinY() - 1, getClipRegion().getWidth(), getClipRegion().getHeight() + 1);
        int localPosition = getPosition();
        if (isStrictlyClipped()) {
            for (int i = 0; i < getItemsAmount(); i++) {
                if (localPosition >= clipMin && localPosition + getItemSize() <= clipMax) {
                    drawItem(graphics, i, localPosition);
                }
                localPosition += getItemSize();
            }
        } else {
            for (int i = 0; i < getItemsAmount(); i++) {
                if (localPosition + getItemSize() >= clipMin && localPosition - getItemSize() <= clipMax) {
                    drawItem(graphics, i, localPosition);
                }
                localPosition += getItemSize();
            }
        }
        graphics.resetClip();//setClip(0, 0, Port.getScreenW(), Port.getScreenH());
    }

    /**
     * Выполнение всех манипуляций на один игровой тик
     */
    public void event(int code, int param, int x, int y) {
        if (getMotionListener() == null) {
            return;
        }
        //Обслуживание прочих событий
        switch (code) {
            case Event.EVENT_TICK:
                getMotionListener().onTick();
                return;
        }
        //Обработка нажатий
        if (!stuck) {
            if (getClipRegion().isWithIn(x, y)) {
                switch (code) {
                    case Event.EVENT_KEY_RELEASED:
                    case Event.EVENT_TOUCH_RELEASED:
                        getMotionListener().onRelease(isVertical() ? y : x);
                        break;
                    case Event.EVENT_KEY_REPEATED:
                    case Event.EVENT_TOUCH_MOVED:
                        getMotionListener().onMove(isVertical() ? y : x);
                        break;
                    case Event.EVENT_KEY_PRESSED:
                    case Event.EVENT_TOUCH_PRESSED:
                        getMotionListener().onTouch(isVertical() ? y : x);
                        break;
                }
            } else {
                switch (code) {
                    case Event.EVENT_KEY_REPEATED:
                    case Event.EVENT_TOUCH_MOVED:
                        getMotionListener().onOutOfBounds();
                        break;
                }
            }
        }
    }

    /**
     * Return an amount of rectangular items to be shawn.
     *
     * @return
     */
    public abstract int getItemsAmount();

    /**
     * Return a linear size of the item to be shawn.
     *
     * @return
     */
    public abstract int getItemSize();

    /**
     * Show an item having an index equals to itemIndex on the graphical context in a place determined with position.
     *
     * @param graphics
     * @param itemIndex
     * @param position
     */
    public abstract void drawItem(Graphics graphics, int itemIndex, int position);

    protected abstract void setInitiated(boolean initiated);

    public abstract Region getClipRegion();

    public int getWindow() {
        return getItemsAmount() * getItemSize();
    }

    public int getY() {
        return top;
    }

    public void setY(int top) {
        this.top = top;
        setInitiated(false);
    }

    public int getX() {
        return left;
    }

    public void setX(int left) {
        this.left = left;
        setInitiated(false);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        setInitiated(false);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        setInitiated(false);
    }

    public boolean isStrictlyClipped() {
        return strictlyClipped;
    }

    public void setStrictlyClipped(boolean strictlyClipped) {
        this.strictlyClipped = strictlyClipped;
    }

    public boolean isStuck() {
        return stuck;
    }

    public void setStuck(boolean stuck) {//чтоб он не крутилось, чтоб крутилось false
        this.stuck = stuck;
    }
}
