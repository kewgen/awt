package com.geargames.awt;

import com.geargames.awt.components.ScrollableArea;
import com.geargames.awt.components.TextArea;
import com.geargames.awt.utils.*;
import com.geargames.common.Port;
import com.geargames.common.Render;
import com.geargames.common.String;
import com.geargames.common.Graphics;
import com.geargames.common.packer.PFont;
import com.geargames.common.packer.PFrame;
import com.geargames.common.util.Region;

/**
 * Users: mikhail.kutuzov, abarakov
 * Date: 14.11.11
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
    private TouchListener listener;

    private static TextHint instance;

    private int defaultShowingTime    = 150;      // 0.15 сек
    private int defaultLifeTime       = 4000;     // 4 сек
    private int defaultHidingTime     = 150;      // 0.15 сек
    private PFont defaultFont         = null;     // системный шрифт
    private boolean defaultScrollable = false;    //
    private int defaultColor          = 0x000000; // черный
    private int defaultMargin         = 10;       //

    public static TextHint getInstance()
    {
        if (instance == null) {
            instance = new TextHint();
        }
        return instance;
    }

    private TextHint() {
        textArea = new TextArea();
        evaluatedRegion = new Region();
        graphicsStrategy = new LinearVanishingStrategy(this);

        setTouchListener(new HintTouchListener());

        topLeftSkin      = new ItemSkin();
        topMiddleSkin    = new ItemSkin();
        topRightSkin     = new ItemSkin();
        middleLeftSkin   = new ItemSkin();
        middleMiddleSkin = new ItemSkin();
        middleRightSkin  = new ItemSkin();
        bottomLeftSkin   = new ItemSkin();
        bottomMiddleSkin = new ItemSkin();
        bottomRightSkin  = new ItemSkin();
        initiated = false;
    }

    /**
     * Показать подсказку с заданными настройками.
     *
     * @param text         тест подсказки
     * @param x            координата по оси X в локальной системе координат окна приложения
     * @param y            координата по оси Y в локальной системе координат окна приложения
     * @param showingTime  время, за которое подсказка появится (в миллисекундах)
     * @param lifeTime     время, через которое подсказка автоматически скроется (в миллисекундах)
     * @param hidingTime   время, за которое подсказка скроется (в миллисекундах)
     * @param scrollable   если true, то текст подсказки можно будет скроллить
     * @param font         шрифт, которым будет отрисовываться текст подсказки
     */
    public static void show(com.geargames.common.String text, int x, int y, int showingTime, int lifeTime, int hidingTime,
                            PFont font, boolean scrollable/*, int color, int margin*/) {
        TextHint instance = getInstance();
        instance.textArea.setText(text);
        instance.textArea.setFont(font);
        instance.textArea.setColor(instance.defaultColor);
        instance.textArea.setFormat(Graphics.HCENTER | Graphics.TOP);
        instance.textArea.setEllipsis(!scrollable);
        instance.graphicsStrategy.setShowingTime(showingTime);
        instance.graphicsStrategy.setLifeTime(lifeTime);        // setTransparencyTime(vanishTime);
        instance.graphicsStrategy.setHidingTime(hidingTime);    // setTime(time);
        instance.edgeRegion = new Region();
        instance.margin = instance.defaultMargin;
        instance.setX(x);
        instance.setY(y);
        instance.initiated = false;
        instance.graphicsStrategy.startShowing();
    }

    /**
     * Показать подсказку нарисованную растровым шрифтом и настройками по умолчанию.
     *
     * @param text
     * @param x
     * @param y
     * @param font
     * @return
     */
    public static void show(String text, int x, int y, PFont font) {
        TextHint instance = getInstance();
        show(text, x, y, instance.defaultShowingTime, instance.defaultLifeTime, instance.defaultHidingTime,
                font, instance.defaultScrollable);
    }

    /**
     * Показать подсказку нарисованную системным шрифтом по умолчанию и настройками по умолчанию.
     *
     * @param text
     * @param x
     * @param y
     * @return
     */
    public static void show(String text, int x, int y) {
        TextHint instance = getInstance();
        show(text, x, y, instance.defaultShowingTime, instance.defaultLifeTime, instance.defaultHidingTime,
                instance.defaultFont, instance.defaultScrollable);
    }

    public void draw(Graphics graphics) {
        graphicsStrategy.draw(graphics);
    }

    public void superDraw(Graphics graphics) {
        super.draw(graphics);
    }

    public boolean onEvent(int code, int param, int x, int y) {
        if (listener != null /*&& edgeRegion != null && edgeRegion.isWithIn(x, y)*/) {
            listener.onEvent(this, code, param, x, y);
        }
        return false;
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

    private void setText(String text) {
        textArea.setText(text);
        initiated = false;
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

    public int getY() {
        return top;
    }

    public void setY(int top) {
        this.top = top;
        initiated = false;
    }

    public int getX() {
        return left;
    }

    public void setX(int left) {
        this.left = left;
        initiated = false;
    }

    public void hide() {
        graphicsStrategy.startHiding();
    }

    protected Region getRegionToDraw(Graphics graphics) {
        if (!initiated) {
            // Нужно для TextHelper
            PFont oldFont = graphics.getFont();
            graphics.setFont(textArea.getFont());

            int halfScreenW = Port.getScreenW() / 2;
            int halfScreenH = Port.getScreenH() / 2;
            int doubleMargin = margin * 2;
            int regionWidth = graphics.getWidth(textArea.getText());
            if (regionWidth > halfScreenW) {
                int word = TextHelper.getMaxWordLength(textArea.getText(), graphics);
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
            int hMargin = (fullRegionWidth - regionWidth) / 2;
            evaluatedRegion.setMinX(left + hMargin);
            evaluatedRegion.setWidth(regionWidth);
            int[] indexes = TextHelper.textIndexing(textArea.getText(), evaluatedRegion, graphics, Graphics.HCENTER);
            int rowCount = indexes.length / 3;
            int regionHeight = rowCount * rowHeight < halfScreenH - doubleMargin ? rowCount * rowHeight : halfScreenH - doubleMargin;
            int skinAmount = ((doubleMargin + regionHeight) / getTopLeftSkin().getHeight()) + (regionHeight % getTopLeftSkin().getHeight() != 0 ? 1 : 0);
            int fullRegionHeight = skinAmount * getTopLeftSkin().getHeight();
            if (top + fullRegionHeight > Port.getScreenH()) {
                top = -fullRegionHeight + Port.getScreenH();
            }
            int vertMargin = (fullRegionHeight - regionHeight) / 2;

            Region drawRegion = textArea.getDrawRegion();
            drawRegion.setMinX(margin);
            drawRegion.setMinY(vertMargin);
            drawRegion.setWidth(regionWidth);
            drawRegion.setHeight(regionHeight);
            textArea.setInitiated(false);

            graphics.setFont(oldFont);

            edgeRegion.setMinX(left);
            edgeRegion.setMinY(top);
            edgeRegion.setWidth(fullRegionWidth);
            edgeRegion.setHeight(fullRegionHeight);
            initiated = true;
        }
        return edgeRegion;
    }

    public ScrollableArea getScrollableArea() {
        return textArea;
    }

    private void setTouchListener(TouchListener listener) {
        this.listener = listener;
    }

    /**
     * Установить скин подложки и размеры составляющих её частей.
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

    public int getDefaultShowingTime() {
        return defaultShowingTime;
    }

    public void setDefaultShowingTime(int time) {
        defaultShowingTime = time;
    }

    public int getDefaultLifeTime() {
        return defaultLifeTime;
    }

    public void setDefaultLifeTime(int time) {
        defaultLifeTime = time;
    }

    public int getDefaultHidingTime() {
        return defaultHidingTime;
    }

    public void setDefaultHidingTime(int time) {
        defaultHidingTime = time;
    }

    public PFont getDefaultFont() {
        return defaultFont;
    }

    public void setDefaultFont(PFont font) {
        defaultFont = font;
    }

    public boolean getDefaultScrollable() {
        return defaultScrollable;
    }

    public void setDefaultScrollable(boolean scrollable) {
        defaultScrollable = scrollable;
    }

    public int getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(int color) {
        defaultColor = color;
    }

    public int getDefaultMargin() {
        return defaultMargin;
    }

    public void setDefaultMargin(int margin) {
        defaultMargin = margin;
    }

    public boolean isInitiated() {
        return initiated;
    }

    public int getHeight() {
        if (edgeRegion != null) {
            return edgeRegion.getHeight();
        } else {
            return 0;
        }
    }

    public int getWidth() {
        if (edgeRegion != null) {
            return edgeRegion.getWidth();
        } else {
            return 0;
        }
    }

}
