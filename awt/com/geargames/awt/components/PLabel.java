package com.geargames.awt.components;

import com.geargames.common.packer.IndexObject;
import com.geargames.common.util.Region;
import com.geargames.common.Graphics;
import com.geargames.common.packer.PFont;
import com.geargames.common.String;


/**
 * User: mikhail v. kutuzov
 * Легкая метка, для рисования на панели содержимого.
 */
public class PLabel extends PElement {
    private String data;
    private Region region;
    private PFont font;
    private byte anchor;

    public PLabel(IndexObject index){
        this();
        setAnchor((byte)(1<<index.getShift()));
    }

    public PLabel() {
        region = new Region();
    }

    public void draw(Graphics graphics, int x, int y) {
        PFont tmp = graphics.getFont();
        graphics.setFont(font);
        graphics.drawString(data, x, y, anchor);
        graphics.setFont(tmp);
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Region getTouchRegion() {
        return region;
    }

    public boolean event(int code, int param, int x, int y) {
        return false;
    }

    public Region getDrawRegion() {
        return region;
    }

    public boolean isVisible() {
        return true;
    }
}
