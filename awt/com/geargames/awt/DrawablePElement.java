package com.geargames.awt;

import com.geargames.awt.components.PElement;
import com.geargames.common.Port;
import com.geargames.common.util.Region;
import com.geargames.common.Graphics;

/**
 * User: mikhail v. kutuzov
 * Обёртка для отображения PElement-ов как Drawable.
 * Отображает окно в точке (x; y) экрана - левый верхний угол окна. Соответсвено, совмещая левый верхний угол элемента
 * с углом окна, отображаем элемент element и его содержимое в этом окне.
 * События в element посылаем так же по относительным от левого верхнего угла окна координатам.
 */
public class DrawablePElement extends Drawable {
    private int x;
    private int y;
    private byte anchor;
    private PElement element;

    /**
     * Прорисовываем компонент прорисовки так, чтоб его левый верхний угол совпадал с координатами (0:0) окна.
     * @param graphics
     */
    public void draw(Graphics graphics) {
        element.draw(graphics, getX() - element.getDrawRegion().getMinX(), getY() - element.getDrawRegion().getMinY());
    }

    /**
     * Передаём компоненту прорисовки координаты события в его собственной системе координат.
     * @param code
     * @param param
     * @param x
     * @param y
     * @return
     */
    public boolean event(int code, int param, int x, int y) {
        Region region = element.getTouchRegion();
        return element.event(code, param, x - getX() + region.getMinX(), y - getY() + region.getMinY());
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        return element.getDrawRegion().getHeight();
    }

    public int getWidth() {
        return element.getDrawRegion().getWidth();
    }

    /**
     * Вернуть компонент для прорисовки.
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
     * @return
     */
    public byte getAnchor() {
        return anchor;
    }

    public void setAnchor(byte anchor) {
        this.anchor = anchor;
    }

    /**
     * Настройка расположения окна для компонента прорисовки на экране, в случае
     * простановки ему экранного якоря.
     */
    public void init(){
        switch (anchor){
            case Anchors.NONE_ANCHOR:
                break;
            case Anchors.BOTTOM_LEFT_ANCHOR:
                setY(Port.getH());
                setX(0);
                break;
            case Anchors.TOP_LEFT_ANCHOR:
                setY(0);
                setX(0);
                break;
            case Anchors.TOP_RIGHT_ANCHOR:
                setX(Port.getW() - element.getDrawRegion().getWidth());
                setY(0);
                break;
            case Anchors.CENTER_ANCHOR:
                setX((Port.getScreenW() / 2) - element.getDrawRegion().getWidth() / 2);
                setY((Port.getScreenH() / 2) - element.getDrawRegion().getHeight() / 2);
                break;
            case Anchors.BOTTOM_RIGHT_ANCHOR:
                setX(Port.getW() - element.getDrawRegion().getWidth());
                setY(Port.getH() - element.getDrawRegion().getHeight());
                break;
        }
    }
}
