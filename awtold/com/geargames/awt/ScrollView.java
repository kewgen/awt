package com.geargames.awt;

import com.geargames.awt.utils.ItemSkin;
import com.geargames.awt.utils.MotionListener;
import com.geargames.awt.utils.Region;
import com.geargames.awt.utils.ScrollHelper;
import com.geargames.awt.utils.motions.CenteredElasticInertMotionListener;
import com.geargames.awt.utils.motions.InertMotionListener;
import com.geargames.awt.utils.motions.StubMotionListener;
import com.geargames.common.Event;
import com.geargames.common.Port;
import com.geargames.packer.Graphics;

import java.util.Vector;

/**
 * User: mikhail.kutuzov
 * Date: 04.11.11
 * Time: 17:15
 */
public class ScrollView extends ScrollableArea {
    private MotionListener motionListener;
    private boolean initiated;
    private int touchX;
    private int touchY;
    private int relative;
    private int mode;

    private Vector items;
    private boolean vertical;
    private int showedItemsAmount;
    private Region clipRegion;
    private int margin;
    private int currentLastClicked;
    private CenteredElasticInertMotionListener centeredMotionListener;
    private InertMotionListener freeMotionListener;
    private StubMotionListener stubMotionListener;

    public ScrollView() {
        initiated = false;
        clipRegion = new Region();
        setRegion(new Region());
        centeredMotionListener = new CenteredElasticInertMotionListener();
        freeMotionListener = new InertMotionListener();
        stubMotionListener = new StubMotionListener();
    }

    public Region getClipRegion() {
        return clipRegion;
    }

    public void initiate(Graphics graphics) {
        if (!initiated) {
            if (getItems() == null || getItemSize() == 0) {
                throw new IllegalArgumentException();
            }
            getRegion().setMinY(getY());
            getRegion().setMinX(getX());
            boolean haveToSet = false;
            if (motionListener != null) {
                relative = motionListener.getTop() - motionListener.getPosition();
                haveToSet = true;
            }
            if (isVertical()) {
                getRegion().setHeight(getItemSize() * getShowedItemsAmount());
                getRegion().setWidth(getItemHorizontalSize());
                motionListener = ScrollHelper.createFreeMenuMotionListener(freeMotionListener, getRegion(), getItemsAmount(), getItemSize());
            } else {
                getRegion().setHeight(getItemVerticalSize());
                getRegion().setWidth(getItemSize() * getShowedItemsAmount());
                if (mode == ScrollHelper.FREE_BUT_STUCK_IF_POSSIBLE) {
                    motionListener = ScrollHelper.createFreeHorizontalMenuMotionListener(freeMotionListener, stubMotionListener, getClipRegion(), getItemsAmount(), getItemSize(), getMargin(), getItemSkin().getOffsetX(), Graphics.HCENTER);
                } else {
                    motionListener = ScrollHelper.createCenteredMenuMotionListener(centeredMotionListener, getRegion(), getItemsAmount(), getItemSize(), getItemSkin().getOffsetX());
                }
            }
            if (haveToSet) {
                motionListener.setPosition(motionListener.getTop() - relative);
            } else {
                motionListener.onClick(currentLastClicked);
            }
            initiated = true;
        }
    }

    public void setInitiated(boolean initiated) {
        this.initiated = initiated;
    }

    /**
     * Выполнение всех манипуляций на один игровой тик
     */
    public void event(int code, int param, int x, int y) {
        int number;
        super.event(code, param, x, y);
        if (getClipRegion().isWithIn(x, y)) {
            switch (code) {
                case Event.EVENT_TOUCH_PRESSED:
                    touchX = x;
                    touchY = y;
                    number = getNumber(x, y);
                    if (number >= 0 && number <= getItemsAmount() - 1) {
                        currentLastClicked = number;
                    }
                    break;
                case Event.EVENT_TOUCH_RELEASED:
                    if (Math.abs(touchX - x) <= Port.TOUCH_ROUND && Math.abs(touchY - y) <= Port.TOUCH_ROUND) {
                        number = getNumber(x, y);
                        if (number >= 0 && number <= getItemsAmount() - 1) {
                            getMotionListener().onClick(number);
                            ScrollViewItem viewItem = (ScrollViewItem) items.elementAt(number);
                            viewItem.click(this, number);
                        }
                    }
                    break;
            }
        }
    }


    private int getNumber(int x, int y) {
        int i;
        if (isVertical()) {
            i = (-getPosition() + getItemSkin().getOffsetY() + y) / getItemSize();
            if (y - getPosition() + getItemSkin().getOffsetY() > i * getItemSize() + getItemSkin().getHeight()) {
                i = -1;
            }
        } else {
            i = (-getPosition() + getItemSkin().getOffsetX() + x) / getItemSize();
            if (x - getPosition() + getItemSkin().getOffsetX() > i * getItemSize() + getItemSkin().getWidth()) {
                i = -1;
            }
        }
        return i;
    }

    public MotionListener getMotionListener() {
        return motionListener;
    }

    public boolean isInitiated() {
        return initiated;
    }

    public int getItemsAmount() {
        return getItems().size();
    }

    public int getItemSize() {
        return (isVertical() ? getItemVerticalSize() : getItemHorizontalSize()) + margin;
    }

    public void drawItem(Graphics graphics, int itemIndex, int position) {
        if (isVertical()) {
            ((ScrollViewItem) getItems().elementAt(itemIndex)).draw(graphics, getRegion().getMinX(), position);
        } else {
            ((ScrollViewItem) getItems().elementAt(itemIndex)).draw(graphics, position, getRegion().getMinY());
        }
    }

    private int getItemVerticalSize() {
        return getItemSkin().getHeight();
    }

    private int getItemHorizontalSize() {
        return getItemSkin().getWidth();
    }

    public ItemSkin getItemSkin() {
        return ((ScrollViewItem) getItems().elementAt(0)).getItemSkin();
    }

    public int getShowedItemsAmount() {
        return showedItemsAmount;
    }

    public void setItems(Vector items) {
        this.items = items;
        setInitiated(false);
    }

    public void setShowedItemsAmount(int showedItemsAmount) {
        this.showedItemsAmount = showedItemsAmount;
        setInitiated(false);
    }

    public Vector getItems() {
        return items;
    }

    public boolean isVertical() {
        return vertical;
    }

    public void setVertical(boolean vertical) {
        this.vertical = vertical;
        setInitiated(false);
    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
        setInitiated(false);
    }

    public int getCurrentLastClicked() {
        return currentLastClicked;
    }

    public void setCurrentLastClicked(int currentLastClicked) {
        this.currentLastClicked = currentLastClicked;
        setInitiated(false);
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
