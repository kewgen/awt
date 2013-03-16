package com.geargames.awt.utils;

/**
 * user: mikhail v. kutuzov, abarakov
 * date: 24.10.11
 * time: 1:22
 * Интерфейс отвечает за "двигательные" способности всех прокручиваемых сушностей (меню, текстов...) и дергается из
 * @see com.geargames.awt.components.ScrollableArea
 */
public abstract class MotionListener {

    public abstract void onOutOfBounds();

    public abstract void onRelease(int y);

    public abstract void onTouch(int y);

    public abstract void onMove(int y);

    public abstract void onClick(int number);

    public abstract int getTop();

    public abstract int getPosition();

    public abstract void setPosition(int position);

    public abstract boolean isCentered();

    /**
     * Вернет объект-слушатель следящего за изменениями позиции прокрутки списка. Чаще всего это компонент "Полоса прокрутки".
     */
    public abstract ScrollListener getScrollListener();

    public abstract void setScrollListener(ScrollListener scrollListener);

}
