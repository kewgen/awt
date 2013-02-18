package com.geargames.awt.components;

import com.geargames.common.Graphics;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PFont;
import com.geargames.common.packer.PObject;
import com.geargames.common.String;

/**
 * User: mikhail v. kutuzov
 * Date: 14.12.12
 * Time: 23:50
 */
public abstract class PEntitledRadioButton extends PRadioButton {
    private PLabel labelCaption;

    public PEntitledRadioButton(PObject prototype, String caption) {
        super(prototype);
        IndexObject index = (IndexObject) prototype.getIndexBySlot(2);
        labelCaption = new PSimpleLabel(index);
        labelCaption.setX(index.getX());
        labelCaption.setY(index.getY());
        labelCaption.setData(caption);
    }

    public void draw(Graphics graphics, int x, int y) {
        super.draw(graphics, x, y);
        labelCaption.draw(graphics, x + labelCaption.getX(), y + labelCaption.getY());
    }

    public String getCaption() {
        return labelCaption.getData();
    }

    public void setCaption(String caption) {
        labelCaption.setData(caption);
    }

    public PFont getFont() {
        return labelCaption.getFont();
    }

    public void setFont(PFont font) {
        labelCaption.setFont(font);
    }

}
