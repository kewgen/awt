package com.geargames.awt.utils.motions;

import com.geargames.awt.utils.MotionListener;
import com.geargames.awt.utils.ScrollListener;

/**
 * user: Mikhail V. Kutuzov
 * date: 24.10.11
 * time: 1:21
 */
public class StubMotionListener extends MotionListener {
    private int position;
    private ScrollListener scrollListener;

    public void create(int top) {
        this.position = top;
    }

    @Override
    public void onTouch(int y) {
    }

    @Override
    public void onMove(int y) {
    }

    @Override
    public int getTop() {
        return position;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void onRelease(int y) {
    }

    @Override
    public void onClick(int number) {
    }

    @Override
    public void onOutOfBounds() {
    }

    @Override
    public boolean isCentered() {
        return false;
    }

    /**
     * Вернет объект-слушатель следящего за изменениями позиции прокрутки списка. Чаще всего это компонент "Полоса прокрутки".
     */
    @Override
    public ScrollListener getScrollListener() {
        return scrollListener;
    }

    @Override
    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

}
