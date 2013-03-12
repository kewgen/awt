package com.geargames.awt.components;

import com.geargames.awt.utils.MotionListener;
import com.geargames.common.Event;
import com.geargames.common.Graphics;
import com.geargames.common.Port;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov, abarakov
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

    /**
     * Обработчик событий экранных касаний и клавиатуры.
     */
    public boolean onEvent(int code, int param, int x, int y) {
        boolean result = super.onEvent(code, param, x, y);
        if (getTouchRegion().isWithIn(x, y)) {
            int number = getItemAtLinearPosition(x);
            if (0 <= number && number < getItemsAmount()) {
                switch (code) {
                    case Event.EVENT_TOUCH_PRESSED:
                        touchX = x;
                        touchY = y;
                        currentLastClicked = number;
                        break;
                    case Event.EVENT_TOUCH_RELEASED:
                        if (Math.abs(touchX - x) <= Port.TOUCH_ROUND && Math.abs(touchY - y) <= Port.TOUCH_ROUND) {
                            getMotionListener().onClick(number);
                            ((PElement) getItems().elementAt(number)).onEvent(code, param, x, y);
                            ((PElement) getItems().elementAt(number)).onEvent(Event.EVENT_SYNTHETIC_CLICK, number, x, y);
                        }
                        return result;
                }
                ((PElement) getItems().elementAt(number)).onEvent(code, param, x, y);
            }
        }
        return result;
    }

    /**
     * Вернуть индекс элемента списка по заданной линейной координате
     *
     * @param pos
     * @return
     */
    public int getItemAtLinearPosition(int pos) {
        int length = -getPosition() + pos;
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
     * Вернуть количество элементов списка.
     *
     * @return
     */
    public int getItemsAmount() {
        return getItems().size();
    }

    /**
     * Вернуть линейный размер элемента (ширину).
     *
     * @return
     */
    public int getItemSize() {
        return getPrototype().getDrawRegion().getWidth() + margin;
    }

    public void drawItem(Graphics graphics, int itemIndex, int x, int y) {
        ((PElement) getItems().elementAt(itemIndex)).draw(graphics, x, y);
    }

    /**
     * Вернуть скин элемента списка.
     *
     * @return
     */
    public abstract PPrototypeElement getPrototype();

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

    public MotionListener getMotionListener() {
        return motionListener;
    }

    /**
     * Установить способ прокрутки списка.
     *
     * @param motionListener
     */
    public void setMotionListener(MotionListener motionListener) {
        this.motionListener = motionListener;
    }

}
