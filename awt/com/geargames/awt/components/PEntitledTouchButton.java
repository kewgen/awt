package com.geargames.awt.components;

import com.geargames.common.Graphics;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.common.String;

/**
 * User: mikhail v. kutuzov
 * Date: 14.12.12
 * Time: 14:29
 */
public abstract class PEntitledTouchButton extends PTouchButton {
    private PLabel caption;

    public PEntitledTouchButton(PObject prototype, String title) {
        super(prototype);
        IndexObject index = (IndexObject) prototype.getIndexBySlot(2);
        caption = new PLabel(index);
        caption.setX(index.getX());
        caption.setY(index.getY());
        caption.setData(title);
    }

    public void draw(Graphics graphics, int x, int y) {
        super.draw(graphics, x, y);
        caption.draw(graphics, x + caption.getX(), y+caption.getY());
    }

}
