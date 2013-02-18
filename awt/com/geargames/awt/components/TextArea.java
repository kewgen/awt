package com.geargames.awt.components;

import com.geargames.awt.utils.*;
import com.geargames.awt.utils.motions.InertMotionListener;
import com.geargames.awt.utils.motions.StubMotionListener;
import com.geargames.common.String;
import com.geargames.common.Graphics;
import com.geargames.common.packer.PFont;
import com.geargames.common.util.Region;

import java.util.Vector;

public class TextArea extends VerticalScrollableArea {
    public static char NEW_STRING = 10;

    private String data;
    private PFont font;
    private int format;
    private int rawHeight;
    private int color;
    private MotionListener motionListener;
    private boolean ellipsis;
    private InertMotionListener verticalMotionListener;
    private StubMotionListener stubMotionListener;
    private Region region;

    private int[] indexes;
    private Vector strings;

    public TextArea() {
        region = new Region();
        verticalMotionListener = new InertMotionListener();
        stubMotionListener = new StubMotionListener();
    }

    public Region getDrawRegion() {
        return region;
    }

    public Region getTouchRegion() {
        return region;
    }

    public void initiate(Graphics graphics) {
        if (data == null || graphics == null) {
            return;
        }
        indexes = TextHelper.indexData(data, region, graphics, format);
        strings = new Vector(indexes.length / 2 + 1);
        if (!isEllipsis()) {
            motionListener = ScrollHelper.createVerticalMotionListener(verticalMotionListener, stubMotionListener, region, indexes.length / 2, rawHeight, format);
        } else {
            motionListener = ScrollHelper.adjustStubMotionListener(stubMotionListener, region, indexes.length / 2, rawHeight, format);
        }
        setStrictlyClipped(isEllipsis());
        String string;
        for (int i = 0; i < getItemsAmount(); i++) {
            if (!isEllipsis() || i + 1 != region.getHeight() / getRawHeight()) {
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

    public void drawItem(Graphics graphics, int itemIndex, int position, int coordinate) {
        graphics.setColor(color);
        String string = (String) strings.elementAt(itemIndex);
        PFont old = graphics.getFont();
        graphics.setFont(font);
        graphics.drawString(string, coordinate + indexes[itemIndex * 2 + 1], position + graphics.getAscent(), 0);
        graphics.setFont(old);
    }

    public int getItemsAmount() {
        return indexes != null ? indexes.length >> 1 : 0;
    }

    /**
     * Выполнение всех манипуляций на один игровой тик
     */
    public boolean event(int code, int param, int x, int y) {
        return super.event(code, param, x, y);
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
        this.data = data;
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

    /** Это значит, что текст внутри не будет прокручиваться (100%) и дополнится троеточием,
     *  в том случае, если он переполняет окошко в котором мы его отрисовываем
     */
    public boolean isEllipsis() {
        return ellipsis;
    }

    public void setEllipsis(boolean ellipsis) {
        this.ellipsis = ellipsis;
    }

    public PFont getFont() {
        return font;
    }

    public void setFont(PFont font) {
        this.font = font;
        setInitiated(false);
    }

    public boolean isVisible() {
        return true;
    }
}
