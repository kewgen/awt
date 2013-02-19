package com.geargames.awt.components;

import com.geargames.awt.utils.MotionListener;
import com.geargames.common.Event;
import com.geargames.common.Graphics;
import com.geargames.common.Port;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov
 * Date: 27.11.12
 * Time: 21:19
 */
public abstract class HorizontalScrollView extends HorizontalScrollableArea {
    private MotionListener motionListener;
    private int touchX;
    private int touchY;

    private int margin;
    private int currentLastClicked;

    public HorizontalScrollView() {
        setInitiated(false);
    }

    public boolean event(int code, int param, int x, int y) {
        int number;
        boolean result = super.event(code, param, x, y);
        if (getTouchRegion().isWithIn(x, y)) {
            if (code != Event.EVENT_TICK) {
                number = getNumber(x);
                switch (code) {
                    case Event.EVENT_TOUCH_PRESSED:
                        touchX = x;
                        touchY = y;
                        if (number >= 0 && number <= getItemsAmount() - 1) {
                            currentLastClicked = number;
                        }
                        break;
                    case Event.EVENT_TOUCH_RELEASED:
                        if (Math.abs(touchX - x) <= Port.TOUCH_ROUND && Math.abs(touchY - y) <= Port.TOUCH_ROUND) {
                            if (number >= 0 && number <= getItemsAmount() - 1) {
                                getMotionListener().onClick(number);
                                ((PElement) getItems().elementAt(number)).event(code, param, x, y);
                                ((PElement) getItems().elementAt(number)).event(Event.EVENT_SYNTHETIC_CLICK, number, x, y);
                            }
                        }
                        return result;
                }
                if (number >= 0 && number <= getItemsAmount() - 1) {
                    ((PElement) getItems().elementAt(number)).event(code, param, x, y);
                }
            }
        }
        return result;
    }


    private int getNumber(int x) {
        int length = (-getPosition() + x);
        if (length % getItemSize() > getItemSize() - margin) {
            return -1;
        }
        int i = length / getItemSize();
        if (length > i * getItemSize() + getPrototype().getDrawRegion().getWidth()) {
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
        return getPrototype().getDrawRegion().getWidth() + margin;
    }

    public void drawItem(Graphics graphics, int itemIndex, int position, int coordinate) {
        ((PElement) getItems().elementAt(itemIndex)).draw(graphics, position, coordinate);
    }

    /**
     * Вернуть скин элемента списка.
     *
     * @return
     */
    public abstract PPrototypeElement getPrototype();

    /**
     * Вернуть наибольшее количество целых элементов, которые могут быть одновременно видимы в списке.
     * Элементы, которые видимы частично, не учитываются.
     *
     * @return
     */
    public int getShownItemsAmount() {
        return getDrawRegion().getWidth() / getItemSize();
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

    /**
     * Вернуть номер последнего нажатого элемента.
     *
     * @return
     */
    public int getCurrentLastClicked() {
        return currentLastClicked;
    }

    /**
     * Установить номер нажатого элемента.
     *
     * @param currentLastClicked
     */
    public void setCurrentLastClicked(int currentLastClicked) {
        this.currentLastClicked = currentLastClicked;
        setInitiated(false);
    }

}
