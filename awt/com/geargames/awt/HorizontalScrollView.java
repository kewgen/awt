package com.geargames.awt;

import com.geargames.awt.utils.ItemSkin;
import com.geargames.awt.utils.MotionListener;
import com.geargames.common.util.Region;
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

    private Region drawRegion;
    private Region touchRegion;
    private int margin;
    private int currentLastClicked;

    public HorizontalScrollView() {
        setInitiated(false);
        drawRegion = new Region();
        touchRegion = new Region();
    }

    public Region getTouchRegion() {
        return touchRegion;
    }

    public Region getDrawRegion() {
        return drawRegion;
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
                    number = getNumber(x);
                    if (number >= 0 && number <= getItemsAmount() - 1) {
                        currentLastClicked = number;
                    }
                    break;
                case Event.EVENT_TOUCH_RELEASED:
                    if (Math.abs(touchX - x) <= Port.TOUCH_ROUND && Math.abs(touchY - y) <= Port.TOUCH_ROUND) {
                        number = getNumber(x);
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


    private int getNumber(int x) {
        int i;
        i = (-getPosition() + getItemSkin().getOffsetX() + x) / getItemSize();
        if (x - getPosition() + getItemSkin().getOffsetX() > i * getItemSize() + getItemSkin().getWidth()) {
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
        return getItemSkin().getWidth() + margin;
    }

    public void drawItem(Graphics graphics, int itemIndex, int position, int coordinate) {
        ((ScrollViewItem) getItems().elementAt(itemIndex)).draw(graphics, position + margin, coordinate);
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
        return drawRegion.getWidth() / getItemSize();
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
