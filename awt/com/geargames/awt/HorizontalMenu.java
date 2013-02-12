package com.geargames.awt;

import com.geargames.awt.components.PPrototypeElement;
import com.geargames.awt.utils.ItemSkin;
import com.geargames.common.Graphics;
import com.geargames.common.util.Region;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov
 * Реализация HorizontalScrollView для постоянного списка элементов(меню).
 */
public class HorizontalMenu extends HorizontalScrollView {
    private Vector items;
    private PPrototypeElement itemSkin;
    private Region region;

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

    public PPrototypeElement getPrototype() {
        return itemSkin;
    }

    public void setItemSkin(PPrototypeElement itemSkin) {
        this.itemSkin = itemSkin;
    }

    public boolean isVisible() {
        return true;
    }

    public Region getDrawRegion() {
        return region;
    }

    public Region getTouchRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public void initiate(Graphics graphics) {
        setInitiated(true);
    }
}
