package com.geargames.awt.cinematics;

/**
 * User: mikhail.kutuzov
 * Date: 26.11.11
 * Класс "движущего" вектора, используется для изменения подчинённых векторов.
 */
public class CMovingPoint extends CPoint {
    private CMovingPoint changer;

    /**
     * Здесь текущая координата изменяется на текущее значение вектора changer.
     */
    public void move() {
        if (changer != null) {
            setX(changer.getX() + getX());
            setY(changer.getY() + getY());
            changer.move();
        }
    }

    /**
     * Возвращает вектор для изменения.
     *
     * @return
     */
    public CMovingPoint getChanger() {
        return changer;
    }

    public void setChanger(CMovingPoint changer) {
        this.changer = changer;
    }
}
