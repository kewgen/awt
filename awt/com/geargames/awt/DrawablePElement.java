package com.geargames.awt;

import com.geargames.awt.components.PElement;
import com.geargames.common.Port;
import com.geargames.common.util.Region;
import com.geargames.common.Graphics;

/**
 * User: mikhail v. kutuzov, abarakov
 * Обёртка для отображения PElement-ов как Drawable.
 * Отображает окно в точке (x, y) экрана - левый верхний угол окна. Соответсвено, совмещая левый верхний угол элемента
 * с углом окна, отображаем элемент element и его содержимое в этом окне.
 * События в element посылаем так же по относительным от левого верхнего угла окна координатам.
 */
public class DrawablePElement extends Drawable {
    private int x;
    private int y;
    private byte anchor;
    private boolean dedicated;
    private PElement element;

    public DrawablePElement() {
        dedicated = false;
    }

    /**
     * Прорисовываем компонент прорисовки так, чтобы его левый верхний угол совпадал с координатами (0, 0) окна.
     *
     * @param graphics
     */
    @Override
    public void draw(Graphics graphics) {
        element.draw(graphics, getX() - element.getDrawRegion().getMinX(), getY() - element.getDrawRegion().getMinY());
    }

    /**
     * Touch события транслируются компоненту, если событие попадает на touch область компонента.
     * Координаты передаются в локальной системе координат этого компонента.
     *
     * @param code
     * @param param
     * @param x
     * @param y
     * @return
     */
    @Override
    public boolean onEvent(int code, int param, int x, int y) {
        Region region = element.getTouchRegion();
        int localX = x - getX() + region.getMinX();
        int localY = y - getY() + region.getMinY();
        if (dedicated) {
            dedicated = element.onEvent(code, param, localX, localY);
        } else {
            if (region.isWithIn(localX, localY)) {
                dedicated = element.onEvent(code, param, localX, localY);
            }
        }
        return dedicated;
    }

    /**
     * Передаём все события в модальном режиме.
     *
     * @param code
     * @param param
     * @param x
     * @param y
     * @return
     */
    public boolean modalEvent(int code, int param, int x, int y) {
        Region region = element.getTouchRegion();
        int localX = x - getX() + region.getMinX();
        int localY = y - getY() + region.getMinY();
        return element.onEvent(code, param, localX, localY);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getHeight() {
        return element.getDrawRegion().getHeight();
    }

    @Override
    public int getWidth() {
        return element.getDrawRegion().getWidth();
    }

    /**
     * Вернуть компонент для прорисовки.
     *
     * @return
     */
    public PElement getElement() {
        return element;
    }

    public void setElement(PElement element) {
        this.element = element;
    }

    /**
     * Вернуть экранный якорь компонента прорисовки.
     *
     * @return
     */
    public byte getAnchor() {
        return anchor;
    }

    public void setAnchor(byte anchor) {
        this.anchor = anchor;
    }

    /**
     * Настройка расположения панелек верхнего уровня для компонента прорисовки на экране, в случае простановки ему
     * экранного якоря.
     */
    public void init() {
        switch (anchor) {
            case Anchors.NONE_ANCHOR:
                break;
            case Anchors.TOP_LEFT_ANCHOR:
            case Anchors.CENTER_LEFT_ANCHOR:
            case Anchors.BOTTOM_LEFT_ANCHOR:
                setX(0);
                break;
            case Anchors.TOP_CENTER_ANCHOR:
            case Anchors.CENTER_CENTER_ANCHOR:
            case Anchors.BOTTOM_CENTER_ANCHOR:
                setX((Port.getScreenW() - element.getDrawRegion().getWidth()) / 2);
                break;
            case Anchors.TOP_RIGHT_ANCHOR:
            case Anchors.CENTER_RIGHT_ANCHOR:
            case Anchors.BOTTOM_RIGHT_ANCHOR:
                setX(Port.getScreenW() - element.getDrawRegion().getWidth()); //todo: getW или getScreenW?
                break;
        }
        switch (anchor) {
            case Anchors.NONE_ANCHOR:
                break;
            case Anchors.TOP_LEFT_ANCHOR:
            case Anchors.TOP_CENTER_ANCHOR:
            case Anchors.TOP_RIGHT_ANCHOR:
                setY(0);
                break;
            case Anchors.CENTER_LEFT_ANCHOR:
            case Anchors.CENTER_CENTER_ANCHOR:
            case Anchors.CENTER_RIGHT_ANCHOR:
                setY((Port.getScreenH() - element.getDrawRegion().getHeight()) / 2);
                break;
            case Anchors.BOTTOM_LEFT_ANCHOR:
            case Anchors.BOTTOM_CENTER_ANCHOR:
            case Anchors.BOTTOM_RIGHT_ANCHOR:
                setY(Port.getScreenH() - element.getDrawRegion().getHeight()); //todo: getH или getScreenH?
                break;
        }
    }

}
