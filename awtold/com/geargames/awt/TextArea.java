package com.geargames.awt;

import com.geargames.awt.utils.*;
import com.geargames.awt.utils.metrics.AwtFontMetric;
import com.geargames.awt.utils.metrics.CustomFontMetric;
import com.geargames.awt.utils.motions.InertMotionListener;
import com.geargames.awt.utils.motions.StubMotionListener;
import com.geargames.common.Event;
import com.geargames.common.String;
import com.geargames.common.packer.Render;
import com.geargames.common.Font;
import com.geargames.packer.Graphics;

import java.util.Vector;

public class TextArea extends ScrollableArea {
    public static char NEW_STRING = 10;

    private String data;
    private byte customFontId;
    private Font font;
    private int format;
    private int rawHeight;
    private int color;
    private MotionListener motionListener;
    private FontMetric fontMetric;
    private boolean ellipsis;
    private InertMotionListener vericalMotionListener;
    private StubMotionListener stubMotionListener;

    private int[] indexes;
    private Vector strings;
    private boolean initiated = false;

    public TextArea() {
        setRegion(new Region());
        vericalMotionListener = new InertMotionListener();
        stubMotionListener = new StubMotionListener();
    }

    public Region getClipRegion() {
        return getRegion();
    }

    public void initiate(Graphics graphics) {
        if (data == null || graphics == null) {
            throw new IllegalArgumentException();
        }
        getRegion().setMinX(getX());
        getRegion().setMinY(getY());
        getRegion().setWidth(getWidth());
        getRegion().setHeight(getHeight());
        com.geargames.common.Render render = Render.getInstance();
        byte oldFontId = 0;
        if (font == null) {
            fontMetric = CustomFontMetric.getInstance();
            oldFontId = render.getFont();
            render.setFont(customFontId);
        } else {
            fontMetric = AwtFontMetric.getInstance();
        }
        indexes = TextHelper.indexData(data, getRegion(), graphics, fontMetric, format);
        strings = new Vector(indexes.length / 2 + 1);
        if (!isEllipsis()) {
            motionListener = ScrollHelper.createVerticalMotionListener(vericalMotionListener, stubMotionListener, getRegion(), indexes.length / 2, rawHeight, format);
        } else {
            motionListener = ScrollHelper.createStubMotionListener(stubMotionListener, getRegion(), indexes.length / 2, rawHeight, format);
        }
        setStrictlyClipped(isEllipsis());
        if (font == null) {
            render.setFont(oldFontId);
        } else graphics.setFont(font, false);
        String string;
        for (int i = 0; i < getItemsAmount(); i++) {
            if (!isEllipsis() || i + 1 != getRegion().getHeight() / getRawHeight()) {
                if (i != getItemsAmount() - 1) {
                    string = data.substring(indexes[i * 2], indexes[(i + 1) * 2]).concat(NEW_STRING);
                } else {
                    string = data.substring(indexes[i * 2], indexes[(i + 1) * 2]);
                }
            } else {
                if (i != getItemsAmount() - 1) {
                    string = data.substring(indexes[i * 2], indexes[(i + 1) * 2] - 3).concat("...");
                } else {
                    string = data.substring(indexes[i * 2], indexes[(i + 1) * 2]).concat(NEW_STRING);
                }
            }
            strings.addElement(string);
        }

        setInitiated(true);
    }

    protected void setInitiated(boolean initiated) {
        this.initiated = initiated;
    }

    public void drawItem(Graphics graphics, int itemIndex, int position) {
        graphics.setColor(color);
        String string = (String) strings.elementAt(itemIndex);

        com.geargames.common.Render render = Render.getInstance();
        if (font == null) {
            byte old = render.getFont();
            render.setFont(customFontId);
            render.drawString(graphics, string, indexes[itemIndex * 2 + 1], position + fontMetric.getAscent(graphics), 0, com.geargames.common.Render.FONT_SMALL);
            render.setFont(old);
        } else {
            render.drawString(graphics, string, indexes[itemIndex * 2 + 1], position + fontMetric.getAscent(graphics), 0, com.geargames.common.Render.FONT_SYSTEM);
        }
    }

    public int getItemsAmount() {
        return indexes != null ? indexes.length >> 1 : 0;
    }

    /**
     * Выполнение всех манипуляций на один игровой тик
     */
    public void event(int code, int param, int x, int y) {
        super.event(code, param, x, y);
        switch (code) {
            case Event.EVENT_TICK:
                return;
        }
    }

    public boolean isVertical() {
        return true;
    }

    public MotionListener getMotionListener() {
        return motionListener;
    }

    public com.geargames.common.String getData() {
        return data;
    }

    public void setData(String data) {
        initiated = false;
        this.data = data;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
        setInitiated(false);
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public int getItemSize() {
        return rawHeight;
    }

    public int getRawHeight() {
        return rawHeight;
    }

    public void setRawHeight(int rawHeight) {
        this.rawHeight = rawHeight;
        setInitiated(false);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isInitiated() {
        return initiated;
    }

    public boolean isEllipsis() {
        return ellipsis;
    }

    public void setEllipsis(boolean ellipsis) {
        /*это значит что текст внутри не будет прокручиваться (100%) и дополнится троеточием,
         в том случае, если он переполняет окошко в котором мы его отрисовываем*/
        this.ellipsis = ellipsis;
    }

    public byte getCustomFontId() {
        return customFontId;
    }

    public void setCustomFontId(byte customFontId) {
        this.customFontId = customFontId;
        setInitiated(false);
    }
}
