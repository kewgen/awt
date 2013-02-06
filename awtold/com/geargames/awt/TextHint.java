package com.geargames.awt;

import com.geargames.awt.utils.*;
import com.geargames.awt.utils.metrics.AwtFontMetric;
import com.geargames.awt.utils.metrics.CustomFontMetric;
import com.geargames.common.Event;
import com.geargames.common.Port;
import com.geargames.common.String;
import com.geargames.common.packer.Render;
import com.geargames.packer.Font;
import com.geargames.packer.Graphics;

/**
 * User: mikhail.kutuzov
 * Date: 14.11.11
 * Time: 18:37
 */
public class TextHint extends PopUp {
    private TextArea textArea;
    private ItemSkin topLeftSkin;
    private ItemSkin topMiddleSkin;
    private ItemSkin topRightSkin;
    private ItemSkin middleLeftSkin;
    private ItemSkin middleMiddleSkin;
    private ItemSkin middleRightSkin;
    private ItemSkin bottomLeftSkin;
    private ItemSkin bottomMiddleSkin;
    private ItemSkin bottomRightSkin;

    private int margin;
    private int top;
    private int left;
    private byte font;
    private boolean initiated;
    private Region edgeRegion;
    private Region evaluatedRegion;
    private LinearVanishingStrategy graphicsStrategy;
    private ClickListener listener;

    public void draw(Graphics graphics) {
        graphicsStrategy.draw(graphics, this);
    }


    public void superDraw(Graphics graphics) {
        super.draw(graphics);
    }


    public void event(int code, int param, int x, int y) {
        graphicsStrategy.event(code, param, x, y);
        if (edgeRegion != null && listener != null && edgeRegion.isWithIn(x, y)) {
            switch (code) {
                case Event.EVENT_TOUCH_PRESSED:
                    listener.onEvent(this, x, y);
                    break;
            }
        }
    }

    private static TextHint instance;

    private TextHint() {
        textArea = new TextArea();
        evaluatedRegion = new Region();
        graphicsStrategy = new LinearVanishingStrategy();

        topLeftSkin = new ItemSkin();
        topMiddleSkin = new ItemSkin();
        topRightSkin = new ItemSkin();
        middleLeftSkin = new ItemSkin();
        middleMiddleSkin = new ItemSkin();
        middleRightSkin = new ItemSkin();
        bottomLeftSkin = new ItemSkin();
        bottomMiddleSkin = new ItemSkin();
        bottomRightSkin = new ItemSkin();
    }

    private void init(com.geargames.common.String text, Font font, int color, boolean scrollable, int time, int vanishTime, int margin) {
        textArea.setData(text);
        textArea.setFont(font);
        textArea.setColor(color);
        textArea.setFormat(Graphics.HCENTER | Graphics.TOP);
        initiated = false;
        textArea.setEllipsis(!scrollable);
        graphicsStrategy.setTime(time);
        graphicsStrategy.setTransparencyTime(vanishTime);
        edgeRegion = new Region();

        this.margin = margin;
    }

    private void init(String text, byte customFontId, int color, boolean scrollable, int time, int vanishTime, int margin) {
        textArea.setCustomFontId(customFontId);
        textArea.setData(text);
        textArea.setFont(null);
        textArea.setColor(color);
        textArea.setFormat(Graphics.HCENTER | Graphics.TOP);
        initiated = false;
        textArea.setEllipsis(!scrollable);
        graphicsStrategy.setTime(time);
        graphicsStrategy.setTransparencyTime(vanishTime);
        edgeRegion = new Region();

        this.margin = margin;
    }

    /**
     * Покажем подсказку нарисованную кастомным шрифтом.
     *
     * @param data
     * @param x
     * @param y
     * @param customFontId
     * @return
     */
    public static TextHint show(String data, int x, int y, byte customFontId) {
        if (instance == null) {
            instance = new TextHint();
            instance.setClickListener(HintClickListener.getInstance());
        }
        instance.init(data, customFontId, 0, false, 50, 10, 10);
        instance.setData(data);
        instance.setTime(50);
        instance.reset();
        instance.setX(x);
        instance.setY(y);

        return instance;
    }

    /**
     * Покажем подсказку нарисованную системным шрифтом по умолчанию.
     *
     * @param data
     * @param x
     * @param y
     * @return
     */
    public static TextHint show(String data, int x, int y) {
        if (instance == null) {
            instance = new TextHint();
            instance.setClickListener(HintClickListener.getInstance());
        }
        Font font1 = new Font(Font.SERIF, Font.BOLD, 12);
        instance.init(data, font1, 0, false, 50, 10, 10);
        instance.setData(data);
        instance.setTime(50);
        instance.reset();
        instance.setX(x);
        instance.setY(y);

        return instance;
    }

    public void reset() {
        graphicsStrategy.reset();
    }

    private void setBlocksSizes(int w1, int h1, int w2, int h2) {//размеры крайних и центрального блока
        topLeftSkin.setHeight(Port.getConvertedValue(h1));
        topLeftSkin.setWidth(Port.getConvertedValue(w1));
        topMiddleSkin.setHeight(Port.getConvertedValue(h2));
        topMiddleSkin.setWidth(Port.getConvertedValue(w2));

        topLeftSkin.copyTo(topRightSkin);
        topLeftSkin.copyTo(middleLeftSkin);
        topMiddleSkin.copyTo(middleMiddleSkin);
        topLeftSkin.copyTo(middleRightSkin);
        topLeftSkin.copyTo(bottomLeftSkin);
        topMiddleSkin.copyTo(bottomMiddleSkin);
        topLeftSkin.copyTo(bottomRightSkin);
    }

    public void setData(String data) {
        textArea.setData(data);
        initiated = false;
    }

    public String getData() {
        return textArea.getData();
    }

    public void setTime(int time) {
        graphicsStrategy.setTime(time);
    }

    public void setVanishTime(int vanishTime) {
        graphicsStrategy.setTransparencyTime(vanishTime);
    }

    protected ItemSkin getTopLeftSkin() {
        return topLeftSkin;
    }

    protected ItemSkin getTopMiddleSkin() {
        return topMiddleSkin;
    }

    protected ItemSkin getTopRightSkin() {
        return topRightSkin;
    }

    protected ItemSkin getMiddleLeftSkin() {
        return middleLeftSkin;
    }

    protected ItemSkin getMiddleMiddleSkin() {
        return middleMiddleSkin;
    }

    protected ItemSkin getMiddleRightSkin() {
        return middleRightSkin;
    }

    protected ItemSkin getBottomLeftSkin() {
        return bottomLeftSkin;
    }

    protected ItemSkin getBottomMiddleSkin() {
        return bottomMiddleSkin;
    }

    protected ItemSkin getBottomRightSkin() {
        return bottomRightSkin;
    }

    public void setY(int top) {
        this.top = top;
        initiated = false;
    }

    public int getMinMargin() {
        return margin;
    }

    public void setMinMargin(int margin) {
        this.margin = margin;
    }


    public int getTop() {
        return top;
    }

    public void setX(int left) {
        this.left = left;
        initiated = false;
    }


    public int getLeft() {
        return left;
    }

    public void hide() {
        setTime(0);
    }


    protected Region getRegionToDraw(Graphics graphics) {
        if (!initiated) {
            FontMetric metric;
            byte oldCustomFont = 0;
            if (textArea.getFont() == null) {
                metric = CustomFontMetric.getInstance();
                oldCustomFont = Render.getInstance().getFont();
                Render.getInstance().setFont(textArea.getCustomFontId());
            } else {
                metric = AwtFontMetric.getInstance();
            }
//            System.out.println("для рамочки " + Render.getInstance().getFont());
            int halfScreenW = Port.getScreenW() >> 1;
            int halfScreenH = Port.getScreenH() >> 1;
            int doubleMargin = margin << 1;
            int regionWidth = metric.getWidth(textArea.getData(), graphics);
            if (regionWidth > halfScreenW) {
                int word = TextHelper.getMaxWordLength(textArea.getData(), graphics, metric);
                regionWidth = halfScreenW > word ? halfScreenW : word;
            }
            int middleWidth = regionWidth + doubleMargin - getMiddleLeftSkin().getWidth() - getMiddleRightSkin().getWidth();
            int middleSkinAmount = middleWidth / getMiddleMiddleSkin().getWidth() + (middleWidth % getMiddleMiddleSkin().getWidth() != 0 ? 1 : 0);
            int fullRegionWidth = getMiddleMiddleSkin().getWidth() * middleSkinAmount + getMiddleLeftSkin().getWidth() + getMiddleRightSkin().getWidth();

            if (left + fullRegionWidth > Port.getScreenW()) {
                left = -fullRegionWidth + Port.getScreenW();
            }
            int rowHeight = TextHelper.getRowHeight(graphics, metric);
            textArea.setRawHeight(rowHeight);
            int hMargin = (fullRegionWidth - regionWidth) >> 1;
            evaluatedRegion.setMinX(left + hMargin);
            evaluatedRegion.setWidth(regionWidth);
            int rowCount = TextHelper.indexData(textArea.getData(), evaluatedRegion, graphics, metric, Graphics.HCENTER).length / 2;
            int regionHeight = rowCount * rowHeight < halfScreenH - doubleMargin ? rowCount * rowHeight : halfScreenH - doubleMargin;
            int skinAmount = ((doubleMargin + regionHeight) / getTopLeftSkin().getHeight()) + (regionHeight % getTopLeftSkin().getHeight() != 0 ? 1 : 0);
            int fullRegionHeight = skinAmount * getTopLeftSkin().getHeight();
            if (top + fullRegionHeight > Port.getScreenH()) {
                top = -fullRegionHeight + Port.getScreenH();
            }
            int vMargin = (fullRegionHeight - regionHeight) >> 1;
            textArea.setY(top + vMargin);
            textArea.setX(left + margin);
            textArea.setWidth(regionWidth);
            textArea.setHeight(regionHeight);
            textArea.getClipRegion().setMinX(textArea.getX());
            textArea.getClipRegion().setMinY(textArea.getY());
            textArea.getClipRegion().setHeight(textArea.getHeight());
            textArea.getClipRegion().setWidth(textArea.getWidth());
            edgeRegion.setMinX(left);
            edgeRegion.setMinY(top);
            edgeRegion.setWidth(fullRegionWidth);
            edgeRegion.setHeight(fullRegionHeight);
            initiated = true;
            if (textArea.getFont() == null) {
                Render.getInstance().setFont(oldCustomFont);
            }
        }
        return edgeRegion;
    }


    protected ScrollableArea getScrollableArea() {
        return textArea;
    }

    public ClickListener getClickListener() {
        return listener;
    }

    public void setClickListener(ClickListener listener) {
        this.listener = listener;
    }

    public int getSkinObject() {
        return topLeftSkin.getObject();
    }

    /**
     * Установим скин подложки и размеры частей её составляющих.
     *
     * @param skinObject
     * @param w1
     * @param h1
     * @param w2
     * @param h2
     */
    public void setSkinObject(int skinObject, int w1, int h1, int w2, int h2) {
        instance.setBlocksSizes(w1, h1, w2, h2);
        topLeftSkin.setObject(skinObject);
        topMiddleSkin.setObject(skinObject + 1);
        topRightSkin.setObject(skinObject + 2);
        middleLeftSkin.setObject(skinObject + 3);
        middleMiddleSkin.setObject(skinObject + 4);
        middleRightSkin.setObject(skinObject + 5);
        bottomLeftSkin.setObject(skinObject + 6);
        bottomMiddleSkin.setObject(skinObject + 7);
        bottomRightSkin.setObject(skinObject + 8);
        initiated = false;
    }

    public boolean isInitiated() {
        return initiated;
    }
}
