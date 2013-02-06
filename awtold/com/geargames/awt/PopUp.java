package com.geargames.awt;

import com.geargames.awt.utils.ItemSkin;
import com.geargames.awt.utils.Region;
import com.geargames.packer.Graphics;

/**
 * User: mikhail.kutuzov
 * Date: 13.11.11
 * Time: 15:05
 */
public abstract class PopUp extends Drawable {

    public void draw(Graphics graphics) {
        Region tmp = getRegionToDraw(graphics);
        if (DEBUG) {
            graphics.drawRect(tmp.getMinX(), tmp.getMinY(), tmp.getWidth(), tmp.getHeight());
        }
        int xAmount = 2 + ((tmp.getWidth() - getMiddleLeftSkin().getWidth() - getMiddleRightSkin().getWidth()) / getMiddleMiddleSkin().getWidth());
        int yAmount = 2 + ((tmp.getHeight() - getTopLeftSkin().getHeight() - getBottomLeftSkin().getHeight()) / getMiddleMiddleSkin().getHeight());
        int width = getLeft();
        for (int i = 0; i < xAmount; i++) {
            ItemSkin itemSkin = null;
            for (int j = 0; j < yAmount; j++) {
                if (i == 0) {
                    if (j == 0) {
                        itemSkin = getTopLeftSkin();
                    } else if (j == yAmount - 1) {
                        itemSkin = getBottomLeftSkin();
                    } else {
                        itemSkin = getMiddleLeftSkin();
                    }
                } else if (i == xAmount - 1) {
                    if (j == 0) {
                        itemSkin = getTopRightSkin();
                    } else if (j == yAmount - 1) {
                        itemSkin = getBottomRightSkin();
                    } else {
                        itemSkin = getMiddleRightSkin();
                    }
                } else {
                    if (j == 0) {
                        itemSkin = getTopMiddleSkin();
                    } else if (j == yAmount - 1) {
                        itemSkin = getBottomMiddleSkin();
                    } else {
                        itemSkin = getMiddleMiddleSkin();
                    }
                }
                graphics.renderFrame(itemSkin.getObject(),
                        width,
                        getTop() + itemSkin.getHeight() * j, null);
            }
            width += itemSkin.getWidth();
        }
        if (DEBUG) {
            graphics.drawRect(getScrollableArea().getX(), getScrollableArea().getY(), getScrollableArea().getWidth(), getScrollableArea().getHeight());
        }
        getScrollableArea().draw(graphics);
    }

    protected abstract ItemSkin getTopLeftSkin();

    protected abstract ItemSkin getTopMiddleSkin();

    protected abstract ItemSkin getTopRightSkin();

    protected abstract ItemSkin getMiddleLeftSkin();

    protected abstract ItemSkin getMiddleMiddleSkin();

    protected abstract ItemSkin getMiddleRightSkin();

    protected abstract ItemSkin getBottomLeftSkin();

    protected abstract ItemSkin getBottomMiddleSkin();

    protected abstract ItemSkin getBottomRightSkin();

    public abstract int getTop();

    public abstract int getLeft();

    protected abstract Region getRegionToDraw(Graphics graphics);

    protected abstract ScrollableArea getScrollableArea();

}
