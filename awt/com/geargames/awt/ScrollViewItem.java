package com.geargames.awt;

import com.geargames.common.Graphics;

/**
 * Класс представляет собой абстрактный элемент списка.
 * Список прорисовывает свои элементы сам, в своей области вывода, опираясь только на номер элемента.
 */
public abstract class ScrollViewItem {
    public abstract void draw(Graphics graphics, int x, int y);

    public abstract com.geargames.common.String getText();

    public abstract void click(int number);
}
