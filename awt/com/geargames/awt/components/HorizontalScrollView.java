package com.geargames.awt.components;

import com.geargames.awt.utils.MotionListener;
import com.geargames.common.Event;
import com.geargames.common.Graphics;
import com.geargames.common.Port;
import com.geargames.common.util.Region;

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
    @Override
    public boolean onEvent(int code, int param, int x, int y) {
        boolean result = super.onEvent(code, param, x, y);
        if (getTouchRegion().isWithIn(x, y)) {
            int number = getItemAtLinearPosition(x, y);
            if (0 <= number && number < getItemsAmount()) {
                PElement item = ((PElement) getItems().elementAt(number));
                int offsetX = getItemOffsetX(number);
                switch (code) {
                    case Event.EVENT_TOUCH_PRESSED:
                        touchX = x;
                        touchY = y;
                        currentLastClicked = number;
                        break;
                    case Event.EVENT_TOUCH_RELEASED:
                        item.onEvent(code, param, x - offsetX, y - getItemOffsetY());
                        if (Math.abs(touchX - x) <= Port.TOUCH_ROUND && Math.abs(touchY - y) <= Port.TOUCH_ROUND) {
                            getMotionListener().onClick(number);
                            item.onEvent(Event.EVENT_SYNTHETIC_CLICK, number, x - offsetX, y - getItemOffsetY());
                        }
                        return result;
                }
                item.onEvent(code, param, x - offsetX, y - getItemOffsetY());
            }
        }
        return result;
    }

    /**
     * Вернуть индекс элемента списка по заданной линейной координате
     *
     * @param x
     * @return
     */
    public int getItemAtLinearPosition(int x, int y) {
        Region region = getPrototype().getDrawRegion();
        if (y >= getItemOffsetY() + region.getMinY() && y <= getItemOffsetY() + region.getMaxY()) {
            int length = -getPosition() + x;
            if (length % getItemSize() > getItemSize() - margin) {
                return -1;
            }
            int i = length / getItemSize();
            if (length > i * getItemSize() + getPrototype().getDrawRegion().getWidth()) {
                return -1;
            }
            return i;
        } else {
            return -1;
        }
    }

    /**
     * Вернуть количество элементов списка.
     *
     * @return
     */
    @Override
    public int getItemsAmount() {
        return getItems().size();
    }

    /**
     * Вернуть линейный размер элемента (ширину).
     *
     * @return
     */
    @Override
    public int getItemSize() {
        return getPrototype().getDrawRegion().getWidth() + margin;
    }

    @Override
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

    @Override
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

    /**
     * Вернуть смещение осей координат по оси x для элемента номер number.
     * @param number
     * @return
     */
    public int getItemOffsetX(int number) {
        return getPosition() +  getItemSize()*number - getPrototype().getDrawRegion().getMinX();
    }
}
