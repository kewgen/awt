package com.geargames.awt;

import com.geargames.awt.utils.MotionListener;
import com.geargames.awt.utils.Region;
import com.geargames.packer.Graphics;

/**
 * User: mikhail.kutuzov
 * Date: 13.11.11
 * Time: 9:14
 */
abstract public class ScrollableRegion extends Drawable {
    private Region region;
    private boolean scrollable;
    private int scrollPercent;

    public ScrollableRegion() {
        scrollable = false;
    }

    public void draw(Graphics graphics) {
        if (!isInitiated()) {
            initiate(graphics);
        }
        if (DEBUG) {
            graphics.drawRect(getRegion().getMinX(), getRegion().getMinY(), getRegion().getWidth(), getRegion().getHeight());
        }
        if (scrollable) {
            int x = 0;
            int y = 0;
            if (isVertical()) {
                if (getRegion().getHeight() >= getWindow()) {
                    return;
                }
                scrollPercent = (100 * ((getRegion().getMinY() - getPosition()))) / getWindow();
            } else {
                if (getRegion().getWidth() >= getWindow()) {
                    return;
                }
                scrollPercent = (100 * ((getRegion().getMinX() - getPosition()))) / getWindow();
            }
        }
    }

    public Region getRegion() {
        return region;
    }

    protected void setRegion(Region region) {
        this.region = region;
    }

    public boolean isScrollable() {
        return scrollable;
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

    public int getScrollPercent() {
        return scrollPercent;
    }

    /**
     * Get a coordinate of the top(in case of vertical scroll) or left edge(in case of horizontal scroll).
     *
     * @return
     */
    protected int getPosition() {
        return getMotionListener().getPosition();
    }

    /**
     * If scrolling direction is vertical this method should return true.
     *
     * @return
     */
    public abstract boolean isVertical();

    /**
     * Is this scrollable component ready to be drawn.
     *
     * @return
     */
    public abstract boolean isInitiated();

    /**
     * @return
     */
    public abstract int getWindow();

    /**
     * Make this scrollable component ready to be shawn upon a graphical context.
     *
     * @param graphics
     */
    public abstract void initiate(Graphics graphics);

    /**
     * Return an appropriate MotionListener realization.
     *
     * @return
     */
    public abstract MotionListener getMotionListener();

}
