package com.geargames.awt;

import com.geargames.awt.utils.ItemSkin;
import com.geargames.common.packer.Render;
import com.geargames.packer.Graphics;

/**
 * Класс представляет собой абстрактный элемент списка.
 * Список прорисовывает свои элементы сам, в своей области вывода, опираясь только на номер элемнета.
 */
public abstract class ScrollViewItem {
    public abstract void draw(Graphics g, int x, int y);

    public abstract ItemSkin getItemSkin();

    public abstract com.geargames.common.String getText();

    public abstract void click(ScrollView scrollView, int number);

    public static void create(ScrollViewItem item, int skin) {
        ((Render)Render.getInstance()).renderObject(null, item, skin, 0, 0);
    }

    public int render(Graphics g, int o_i_slot, int o_i_id, int o_x, int o_y) {
        return -1;
    }
}
