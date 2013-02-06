package com.geargames.awt;

import com.geargames.awt.utils.ItemSkin;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov
 * Реализация HorizontalScrollView для постоянного списка элементов(меню).
 */
public class HorizontalMenu extends HorizontalScrollView {
    private Vector items;
    private ItemSkin itemSkin;

    public Vector getItems() {
        return items;
    }

    /**
     * Установить элементы списка.
     * @param items
     */
    public void setItems(Vector items) {
        this.items = items;
        setInitiated(false);
    }

    public ItemSkin getItemSkin() {
        return itemSkin;
    }

    public void setItemSkin(ItemSkin itemSkin) {
        this.itemSkin = itemSkin;
    }

    public boolean isVisible() {
        return true;
    }
}
