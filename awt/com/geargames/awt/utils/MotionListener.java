package com.geargames.awt.utils;

/**
 * user: Mikhail V. Kutuzov
 * date: 24.10.11
 * time: 1:22
 * Интерфейс отвечает за "двигательные" способности всех прокручиваемых сушьностей(меню, текстов...) и дергается из
 * @see com.geargames.awt.ScrollableArea
 */
public abstract class MotionListener {
    public abstract void onOutOfBounds();

    public abstract void onRelease(int y);

    public abstract void onTouch(int y);

    public abstract void onMove(int y);

    public abstract void onTick();

    public abstract void onClick(int number);

    public abstract int getTop();

    public abstract int getPosition();

    public abstract void setPosition(int position);

    public abstract boolean isCentered();
}
