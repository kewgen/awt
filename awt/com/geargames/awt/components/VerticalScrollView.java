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
 */
public abstract class VerticalScrollView extends VerticalScrollableArea {
    private MotionListener motionListener;
    private int touchX;
    private int touchY;
    private int margin;

    public VerticalScrollView() {
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
                int offsetY = getItemOffsetY(number);
                switch (code) {
                    case Event.EVENT_TOUCH_PRESSED:
                        touchX = x;
                        touchY = y;
                        break;
                    case Event.EVENT_TOUCH_RELEASED:
                        item.onEvent(code, param, x - getItemOffsetX(), y);
                        if (Math.abs(touchX - x) <= Port.TOUCH_ROUND && Math.abs(touchY - y) <= Port.TOUCH_ROUND) {
                            getMotionListener().onClick(number);
                            item.onEvent(Event.EVENT_SYNTHETIC_CLICK, number, x - getItemOffsetX(), y - offsetY);
                        }
                        return result;
                }
                ((PElement) getItems().elementAt(number)).onEvent(code, param, x - getItemOffsetX() , y - offsetY);
            }
        }
        return result;
    }

    /**
     * Вернуть индекс элемента списка по заданной линейной координате
     *
     * @param y
     * @return
     */
    protected int getItemAtLinearPosition(int x, int y) {
        Region region = getPrototype().getDrawRegion();
        if (x >= getItemOffsetX() + region.getMinX() && x <= getItemOffsetX() + region.getMaxX()) {
            int length = -getPosition() + y;
            if (length % getItemSize() > getItemSize() - margin) {
                return -1;
            }
            int i = length / getItemSize();
            if (length > i * getItemSize() + getPrototype().getDrawRegion().getHeight()) {
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
     * Вернуть линейный размер элемента (высоту).
     *
     * @return
     */
    @Override
    public int getItemSize() {
        return getPrototype().getDrawRegion().getHeight() + margin;
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
     * Вернуть смещение по оси y для элмента номер number.
     * @param number
     * @return
     */
    public int getItemOffsetY(int number) {
        return getPosition() + getItemSize()*number - getPrototype().getDrawRegion().getMinY();
    }



}
