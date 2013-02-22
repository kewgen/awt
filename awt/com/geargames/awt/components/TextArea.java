package com.geargames.awt.components;

import com.geargames.awt.utils.*;
import com.geargames.awt.utils.motions.InertMotionListener;
import com.geargames.awt.utils.motions.StubMotionListener;
import com.geargames.common.String;
import com.geargames.common.Graphics;
import com.geargames.common.packer.PFont;
import com.geargames.common.util.Region;

public class TextArea extends VerticalScrollableArea {
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
    private final static String STR_ELIPSIS = String.valueOfC("...");
    private int itemEllipsisIndex = -1;
    private int ellipsisOffsetX = 0;

    public TextArea() {
        region = new Region();
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

        indexes = TextHelper.textIndexing(text, getDrawRegion(), graphics, format);

        // Выясняем необходимость добавления многоточия в конец отображаемого текста
        if (isEllipsis() && getShownItemsAmount() < getItemsAmount()) {
            // Многоточие будет добавлено
            itemEllipsisIndex = getShownItemsAmount() - 1;
            int ellipsisWidth = graphics.getWidth(STR_ELIPSIS);
            int endCharIndex = indexes[itemEllipsisIndex * 2 + 2];
            int charIndex = indexes[itemEllipsisIndex * 2 + 0];
            int substringWidth = ellipsisWidth;
            int drawRegionWidth = getDrawRegion().getWidth();
            while (charIndex < endCharIndex) {
                int charWidth = graphics.getWidth(text.charAt(charIndex));
                substringWidth += charWidth;
                if (substringWidth > drawRegionWidth) {
                    substringWidth -= charWidth;
                    break;
                }
                charIndex++;
            }
            int offsetX = ScrollHelper.getXTextBegin(format, getDrawRegion(), substringWidth);
            indexes[itemEllipsisIndex * 2 + 1] = offsetX;
            indexes[itemEllipsisIndex * 2 + 2] = charIndex;
            ellipsisOffsetX = offsetX + substringWidth - ellipsisWidth;
        } else {
            // В добавлении многоточия нет необходимости
            itemEllipsisIndex = -1;
            ellipsisOffsetX = 0;
        }
        graphics.setFont(oldFont);

        if (stubMotionListener == null) {
            stubMotionListener = new StubMotionListener();
        }
        if (!isStuck()) {
            if (verticalMotionListener == null) {
                verticalMotionListener = new InertMotionListener();
            }
            motionListener = ScrollHelper.createVerticalMotionListener(
                    verticalMotionListener, stubMotionListener, region, indexes.length / 2, rawHeight, format);
        } else {
            motionListener = ScrollHelper.adjustStubMotionListener(
                    stubMotionListener, region, indexes.length / 2, rawHeight, format);
        }

        setInitiated(true);
    }

    public void drawItem(Graphics graphics, int itemIndex, int x, int y) {
        graphics.setColor(color);
        PFont oldFont = graphics.getFont();
        graphics.setFont(font);
        graphics.drawSubstring(
                text,
                indexes[itemIndex * 2 + 0], indexes[itemIndex * 2 + 2],
                x + indexes[itemIndex * 2 + 1], y + graphics.getAscent(), 0);
        if (itemIndex == itemEllipsisIndex) {
            graphics.drawString(STR_ELIPSIS, x + ellipsisOffsetX, y + graphics.getAscent(), 0);
        }
        graphics.setFont(oldFont);
    }

    public int getItemsAmount() {
        return indexes != null ? indexes.length / 2 : 0;
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
     * @return одно из значений Graphics.LEFT, Graphics.RIGHT или Graphics.HCENTER
     */
    public int getFormat() {
        return format;
    }

    /**
     * Установить формат горизонтального выравнивания
     * @param format - одно из значений Graphics.LEFT, Graphics.RIGHT или Graphics.HCENTER
     */
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

    public boolean isStrictlyClipped() {
        return isEllipsis() || super.isStrictlyClipped();
    }

    public boolean isStuck() {
        return isEllipsis() || super.isStuck();
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
