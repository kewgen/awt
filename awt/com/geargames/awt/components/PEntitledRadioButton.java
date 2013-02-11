package com.geargames.awt.components;

import com.geargames.common.Graphics;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.common.String;

/**
 * User: mikhail v. kutuzov
 * Date: 14.12.12
 * Time: 23:50
 */
public abstract class PEntitledRadioButton extends PRadioButton {
    private PLabel caption;

    public PEntitledRadioButton(PObject prototype, String title) {
        super(prototype);
        IndexObject index = (IndexObject) prototype.getIndexBySlot(2);
        caption = new PSimpleLabel(index);
        caption.setX(index.getX());
        caption.setY(index.getY());
        caption.setData(title);
    }

    public void draw(Graphics graphics, int x, int y) {
        super.draw(graphics, x, y);
        caption.draw(graphics, x + caption.getX(), y + caption.getY());
    }

}
