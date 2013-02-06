package com.geargames.awt;

import com.geargames.awt.components.PElement;
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
    private PElement element;

    /**
     * Прорисовываем элемент содержимого так, чтоб его левый верхний угол совпадал с координатами (0:0) окна.
     * @param graphics
     */
    public void draw(Graphics graphics) {
        element.draw(graphics, getX() - element.getDrawRegion().getMinX(), getY() - element.getDrawRegion().getMinY());
    }

    /**
     * Передаём панели содержимого(чаще всего) или просто элемент содержимого координаты события в его собственной
     * системе координат.
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

    public PElement getElement() {
        return element;
    }

    public void setElement(PElement element) {
        this.element = element;
    }
}
