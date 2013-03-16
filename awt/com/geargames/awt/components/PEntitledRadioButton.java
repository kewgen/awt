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
    private PLabel labelTitle;

    public PEntitledRadioButton(PObject prototype, String text) {
        super(prototype);
        IndexObject index = (IndexObject) prototype.getIndexBySlot(2);
        labelTitle = new PSimpleLabel(index);
        labelTitle.setX(index.getX());
        labelTitle.setY(index.getY());
        labelTitle.setText(text);
    }

    @Override
    public void draw(Graphics graphics, int x, int y) {
        super.draw(graphics, x, y);
        labelTitle.draw(graphics, x + labelTitle.getX(), y + labelTitle.getY());
    }

    public String getText() {
        return labelTitle.getText();
    }

    public void setText(String text) {
        labelTitle.setText(text);
    }

    public PFont getFont() {
        return labelTitle.getFont();
    }

    public void setFont(PFont font) {
        labelTitle.setFont(font);
    }

}
