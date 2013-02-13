package com.geargames.awt;

import com.geargames.awt.components.ScrollableArea;
import com.geargames.awt.components.TextArea;
import com.geargames.awt.utils.*;
import com.geargames.common.Event;
import com.geargames.common.Port;
import com.geargames.common.Render;
import com.geargames.common.String;
import com.geargames.common.Graphics;
import com.geargames.common.packer.PFont;
import com.geargames.common.packer.PFrame;
import com.geargames.common.util.Region;

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

    public boolean event(int code, int param, int x, int y) {
        graphicsStrategy.event(code, param, x, y);
        if (edgeRegion != null && listener != null && edgeRegion.isWithIn(x, y)) {
            switch (code) {
                case Event.EVENT_TOUCH_PRESSED:
                    listener.onEvent(this, x, y);
                    break;
            }
        }
        return false;
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

    private void init(com.geargames.common.String text, PFont font, int color, boolean scrollable, int time, int vanishTime, int margin) {
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

    /**
     * Покажем подсказку нарисованную кастомным шрифтом.
     *
     * @param data
     * @param x
     * @param y
     * @param font
     * @return
     */
    public static void show(String data, int x, int y, PFont font) {
        if (instance == null) {
            instance = new TextHint();
            instance.setClickListener(HintClickListener.getInstance());
        }
        instance.init(data, font, 0, false, 50, 10, 10);
        instance.setData(data);
        instance.setTime(50);
        instance.reset();
        instance.setX(x);
        instance.setY(y);
    }

    /**
     * Покажем подсказку нарисованную системным шрифтом по умолчанию.
     *
     * @param data
     * @param x
     * @param y
     * @return
     */
    public static void show(String data, int x, int y) {
        show(data,x,y,null);
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

    private void setData(String data) {
        textArea.setData(data);
        initiated = false;
    }

    private void setTime(int time) {
        graphicsStrategy.setTime(time);
    }

    private void setVanishTime(int vanishTime) {
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

    public int getY() {
        return top;
    }

    public void setX(int left) {
        this.left = left;
        initiated = false;
    }

    public int getX() {
        return left;
    }

    public void hide() {
        setTime(0);
    }


    protected Region getRegionToDraw(Graphics graphics) {
        if (!initiated) {
            int halfScreenW = Port.getScreenW() >> 1;
            int halfScreenH = Port.getScreenH() >> 1;
            int doubleMargin = margin << 1;
            int regionWidth = graphics.getWidth(textArea.getData());
            if (regionWidth > halfScreenW) {
                int word = TextHelper.getMaxWordLength(textArea.getData(), graphics);
                regionWidth = halfScreenW > word ? halfScreenW : word;
            }
            int middleWidth = regionWidth + doubleMargin - getMiddleLeftSkin().getWidth() - getMiddleRightSkin().getWidth();
            int middleSkinAmount = middleWidth / getMiddleMiddleSkin().getWidth() + (middleWidth % getMiddleMiddleSkin().getWidth() != 0 ? 1 : 0);
            int fullRegionWidth = getMiddleMiddleSkin().getWidth() * middleSkinAmount + getMiddleLeftSkin().getWidth() + getMiddleRightSkin().getWidth();

            if (left + fullRegionWidth > Port.getScreenW()) {
                left = -fullRegionWidth + Port.getScreenW();
            }
            int rowHeight = TextHelper.getRowHeight(graphics);
            textArea.setRawHeight(rowHeight);
            int hMargin = (fullRegionWidth - regionWidth) >> 1;
            evaluatedRegion.setMinX(left + hMargin);
            evaluatedRegion.setWidth(regionWidth);
            int rowCount = TextHelper.indexData(textArea.getData(), evaluatedRegion, graphics, Graphics.HCENTER).length / 2;
            int regionHeight = rowCount * rowHeight < halfScreenH - doubleMargin ? rowCount * rowHeight : halfScreenH - doubleMargin;
            int skinAmount = ((doubleMargin + regionHeight) / getTopLeftSkin().getHeight()) + (regionHeight % getTopLeftSkin().getHeight() != 0 ? 1 : 0);
            int fullRegionHeight = skinAmount * getTopLeftSkin().getHeight();
            if (top + fullRegionHeight > Port.getScreenH()) {
                top = -fullRegionHeight + Port.getScreenH();
            }
            int vMargin = (fullRegionHeight - regionHeight) >> 1;

            textArea.getDrawRegion().setMinY(vMargin);
            textArea.getDrawRegion().setMinX(margin);
            textArea.getDrawRegion().setWidth(regionWidth);
            textArea.getDrawRegion().setHeight(regionHeight);
            textArea.setInitiated(false);

            edgeRegion.setMinX(left);
            edgeRegion.setMinY(top);
            edgeRegion.setWidth(fullRegionWidth);
            edgeRegion.setHeight(fullRegionHeight);
            initiated = true;
        }
        return edgeRegion;
    }


    protected ScrollableArea getScrollableArea() {
        return textArea;
    }

    private void setClickListener(ClickListener listener) {
        this.listener = listener;
    }

    /**
     * Установим скин подложки и размеры частей её составляющих.
     *
     * @param skin
     * @param render
     * @param w1
     * @param h1
     * @param w2
     * @param h2
     */
    public void setSkinObject(PFrame skin, Render render, int w1, int h1, int w2, int h2) {
        instance.setBlocksSizes(w1, h1, w2, h2);
        topLeftSkin.setPrototype(skin);
        topMiddleSkin.setPrototype(render.getFrame(skin.getPID() + 1));
        topRightSkin.setPrototype(render.getFrame(skin.getPID() + 2));
        middleLeftSkin.setPrototype(render.getFrame(skin.getPID() + 3));
        middleMiddleSkin.setPrototype(render.getFrame(skin.getPID() + 4));
        middleRightSkin.setPrototype(render.getFrame(skin.getPID() + 5));
        bottomLeftSkin.setPrototype(render.getFrame(skin.getPID() + 6));
        bottomMiddleSkin.setPrototype(render.getFrame(skin.getPID() + 7));
        bottomRightSkin.setPrototype(render.getFrame(skin.getPID() + 8));
        initiated = false;
    }

    public boolean isInitiated() {
        return initiated;
    }

    public int getHeight() {
        if(edgeRegion != null){
            return edgeRegion.getHeight();
        }else{
            return 0;
        }
    }

    public int getWidth() {
        if(edgeRegion != null){
            return edgeRegion.getWidth();
        }else{
            return 0;
        }
    }
}
