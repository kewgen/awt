package com.geargames.awt;

import com.geargames.awt.utils.ItemSkin;
import com.geargames.awt.utils.MotionListener;
import com.geargames.common.Event;
import com.geargames.common.Graphics;
import com.geargames.common.Port;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov
 * Date: 27.11.12
 * Time: 16:39
 */
public abstract class VerticalScrollView extends VerticalScrollableArea {
    private MotionListener motionListener;
    private int touchX;
    private int touchY;

    private int margin;

    public VerticalScrollView() {
        setInitiated(false);
    }

    public void initiate(Graphics graphics) {
        setInitiated(true);
    }

    public boolean event(int code, int param, int x, int y) {
        int number;
        boolean result = super.event(code, param, x, y);
        if (getTouchRegion().isWithIn(x, y)) {
            switch (code) {
                case Event.EVENT_TOUCH_PRESSED:
                    touchX = x;
                    touchY = y;
                    break;
                case Event.EVENT_TOUCH_RELEASED:
                    if (Math.abs(touchX - x) <= Port.TOUCH_ROUND && Math.abs(touchY - y) <= Port.TOUCH_ROUND) {
                        number = getNumber(y);
                        if (number >= 0 && number <= getItemsAmount() - 1) {
                            getMotionListener().onClick(number);
                            ScrollViewItem viewItem = (ScrollViewItem) getItems().elementAt(number);
                            viewItem.click(number);
                        }
                    }
                    break;
            }
        }
        return result;
    }


    private int getNumber(int y) {
        int i;
        i = (-getPosition() + getItemSkin().getOffsetY() + y) / getItemSize();
        if (y - getPosition() + getItemSkin().getOffsetY() > i * getItemSize() + getItemSkin().getHeight()) {
            i = -1;
        }
        return i;
    }

    /**
     * Установить способ прокрутки списка.
     *
     * @param motionListener
     */
    public void setMotionListener(MotionListener motionListener) {
        this.motionListener = motionListener;
    }

    public MotionListener getMotionListener() {
        return motionListener;
    }

    public int getItemsAmount() {
        return getItems().size();
    }

    public int getItemSize() {
        return getItemSkin().getHeight() + margin;
    }

    public void drawItem(Graphics graphics, int itemIndex, int position, int coordinate) {
       ((ScrollViewItem) getItems().elementAt(itemIndex)).draw(graphics, coordinate, position + margin);
    }

    /**
     * Вернуть скин элемента списка.
     *
     * @return
     */
    public abstract ItemSkin getItemSkin();

    /**
     * Вернуть наибольшее количество целых элементов которое может быть видимо в списке.
     *
     * @return
     */
    public int getShowedItemsAmount() {
        return getDrawRegion().getHeight() / getItemSize();
    }

    /**
     * Вернуть вектор элементов списка.
     *
     * @return
     * @See ScrollViewItem
     */
    public abstract Vector getItems();

    /**
     * Вернуть расстояние между элементами.
     *
     * @return
     */
    public int getMargin() {
        return margin;
    }

    /**
     * Установить расстояние между элементами.
     *
     * @param margin
     */
    public void setMargin(int margin) {
        this.margin = margin;
        setInitiated(false);
    }
}
