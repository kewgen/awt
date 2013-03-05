package com.geargames.awt.components;

import com.geargames.common.packer.IndexObject;
import com.geargames.common.Graphics;
import com.geargames.common.packer.PFont;
import com.geargames.common.String;

/**
 * User: mikhail v. kutuzov
 * Легкая метка, для рисования на панели текстовых строк.
 */
public abstract class PLabel extends PElement {
    private String text;
    private PFont font;
    private byte anchor;

    public PLabel(IndexObject index){
        setAnchor((byte)(1 << index.getShift()));
    }

    public PLabel() {
    }

    public void draw(Graphics graphics, int x, int y) {
        PFont oldFont = graphics.getFont();
        graphics.setFont(font);
        graphics.drawString(text, x, y, anchor);
        graphics.setFont(oldFont);
    }

    public byte getAnchor() {
        return anchor;
    }

    public void setAnchor(byte anchor) {
        this.anchor = anchor;
    }

    public PFont getFont() {
        return font;
    }

    public void setFont(PFont font) {
        this.font = font;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
