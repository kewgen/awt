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
    
    private String text;
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
        if (text == null || graphics == null) {
            return;
        }
        PFont oldFont = graphics.getFont();
        graphics.setFont(font);

        indexes = TextHelper.textIndexing(text, region, graphics, format);
        strings = new Vector(indexes.length / 2 + 1);
        if (!isEllipsis()) {
            motionListener = ScrollHelper.createVerticalMotionListener(verticalMotionListener, stubMotionListener, region, indexes.length / 2, rawHeight, format);
        } else {
            motionListener = ScrollHelper.adjustStubMotionListener(stubMotionListener, region, indexes.length / 2, rawHeight, format);
        }
        setStrictlyClipped(isEllipsis());

        for (int i = 0; i < getItemsAmount(); i++) {
            String string;
            if (!isEllipsis() || i + 1 != region.getHeight() / getRawHeight()) {
                if (i != getItemsAmount() - 1) {
                    string = text.substring(indexes[i * 3], indexes[i * 3] + indexes[i * 3 + 1]).concat(NEW_STRING);
                } else {
                    string = text.substring(indexes[i * 3], indexes[i * 3] + indexes[i * 3 + 1]);
                }
            } else {
                if (i != getItemsAmount() - 1) {
                    string = text.substring(indexes[i * 3], indexes[i * 3] + indexes[i * 3 + 1]).concat("...");
                } else {
                    string = text.substring(indexes[i * 3], indexes[i * 3] + indexes[i * 3 + 1]).concat(NEW_STRING);
                }
            }
            strings.addElement(string);
        }
        graphics.setFont(oldFont);
        setInitiated(true);
    }

    public void drawItem(Graphics graphics, int itemIndex, int position, int coordinate) {
        graphics.setColor(color);
        String string = (String) strings.elementAt(itemIndex);
        PFont oldFont = graphics.getFont();
        graphics.setFont(font);
        graphics.drawString(string, coordinate + indexes[itemIndex * 3 + 2], position + graphics.getAscent(), 0);
        graphics.setFont(oldFont);
    }

    public int getItemsAmount() {
        return indexes != null ? indexes.length / 3 : 0;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        setInitiated(false);
    }

    /**
     * Вернуть формат горизонтального выравнивания.
     * Одно из значений Graphics.LEFT, Graphics.RIGHT или Graphics.HCENTER
     */
    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
        setInitiated(false);
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

    /**
     * Если True, то для компонента запрещена прокрутка текста и в случае если текст целиком не помещается в пределах
     * региона отрисовки, то он будет "обрезан" и в конец "обрезанного" текста будет добавлено троеточие.
     */
    public boolean isEllipsis() {
        return ellipsis;
    }

    public void setEllipsis(boolean ellipsis) {
        this.ellipsis = ellipsis;
        setInitiated(false);
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
