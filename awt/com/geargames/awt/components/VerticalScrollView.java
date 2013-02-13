package com.geargames.awt.components;

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

    public boolean event(int code, int param, int x, int y) {
        int number;
        boolean result = super.event(code, param, x, y);
        if (getTouchRegion().isWithIn(x, y)) {
            if (code != Event.EVENT_TICK) {
                number = getNumber(y);
                if (number >= 0 && number <= getItemsAmount() - 1) {
                    switch (code) {
                        case Event.EVENT_TOUCH_PRESSED:
                            touchX = x;
                            touchY = y;
                            break;
                        case Event.EVENT_TOUCH_RELEASED:
                            if (Math.abs(touchX - x) <= Port.TOUCH_ROUND && Math.abs(touchY - y) <= Port.TOUCH_ROUND) {
                                getMotionListener().onClick(number);
                                ((PElement) getItems().elementAt(number)).event(code, param, x, y);
                                ((PElement) getItems().elementAt(number)).event(Event.EVENT_SYNTHETIC_CLICK, number, x, y);
                            }
                            return result;
                    }
                    ((PElement) getItems().elementAt(number)).event(code, param, x, y);
                }
            }
        }
        return result;
    }


    private int getNumber(int y) {
        int length = -getPosition() + y;
        if(length % getItemSize() > getItemSize() - margin){
            return -1;
        }
        int i = length / getItemSize();
        if (length > i * getItemSize() + getPrototype().getDrawRegion().getHeight()) {
            return -1;
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
        return getPrototype().getDrawRegion().getHeight() + margin;
    }

    public void drawItem(Graphics graphics, int itemIndex, int position, int coordinate) {
        ((PElement) getItems().elementAt(itemIndex)).draw(graphics, coordinate, position);
    }

    /**
     * Вернуть скин элемента списка.
     *
     * @return
     */
    public abstract PPrototypeElement getPrototype();

    /**
     * Вернуть наибольшее количество целых элементов которое может быть видимо в списке.
     *
     * @return
     */
    public int getShownItemsAmount() {
        return getDrawRegion().getHeight() / getItemSize();
    }

    /**
     * Вернуть вектор элементов списка.
     *
     * @return
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
