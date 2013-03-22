package com.geargames.awt.cinematics;

import com.geargames.awt.Drawable;
import com.geargames.common.timers.TimerListener;
import com.geargames.common.timers.TimerManager;
import com.geargames.common.Graphics;
import com.geargames.common.String;
import com.geargames.common.logging.Debug;

import java.util.Vector;

/**
 * user: Mikhail V. Kutuzov
 * date: 04.12.11
 * time: 9:34
 * Класс движущийся объект, является расширением класса Drawable, что позволяет ему быть нарисованным на Graphics.
 * На самом деле он отдаёт право быть прорисованным некоторому внутреннему объекту, который возвращается вызовом
 * getDrawable(), ему же делегируются все вызовы характерные для Drawable.
 * Движущийся объект представяет собой совокупность движущейся точки и рисунка, который в этой точке рисуется.
 * Внутри содержится вектор точек, через которые объект пролетел, для прорисовки линии полёта в случае отладки.
 * Вектор этот будет создан только в режиме отладки.
 */
public abstract class MovingObject extends Drawable implements TimerListener {
    private Vector points;

    private int timerId;

    public MovingObject() {
        points = new Vector();
        timerId = TimerManager.NULL_TIMER;
    }

    //todo: Добавить функционал стартующий и останавливающий таймер!

    private void addDots() {
        points.addElement(getPoint().copy());
    }

    private void drawDots(Graphics graphics) {
        if (points == null) {
            points = new Vector();
        }
        CPoint prev = null;
        for (int i = 0; i < points.size(); i++) {
            if (prev != null) {
                graphics.drawLine((int) prev.getX(), (int) prev.getY(), (int) ((CPoint) points.get(i)).getX(), (int) ((CPoint) points.get(i)).getY());
            }
            prev = (CPoint) points.elementAt(i);
        }
    }

    /**
     * Метод передаёт вызов внутреннему классу Drawable, который предварительно настраивается согласно текущим координатам.
     *
     * @param graphics
     */
    @Override
    public void draw(Graphics graphics) {
        if (isInitiated()) {
            getDrawable().setX((int) getPoint().getX());
            getDrawable().setY((int) getPoint().getY());
            getDrawable().draw(graphics);
            if (Drawable.DEBUG) {
                drawDots(graphics);
            }
        }
    }

    public abstract void onFlyEnd();

    /**
     * Здесь, раз за тик, пересчитывается значение текущей координаты и в случае отладки, добавляется новое значение в
     * список пройденных точек.
     */
    @Override
    public void onTimer(int timerId) {
        if (timerId != this.timerId) {
            return;
        }
        if (isInitiated()) {
            if (!getFinishAdviser().isFinished()) {
                if (Drawable.DEBUG) {
                    addDots();
                }
                getFinishAdviser().onTick();
                if (Drawable.DEBUG) {
                    Debug.debug(String.valueOfI((int) getPoint().getX()).concat(" ").concatI((int) getPoint().getY()));
                }
                getPoint().move();
            } else {
                onFlyEnd();
            }
        }
    }

    /**
     * Каждый движущийся объект должен реализовывать этот метод, чтоб иметь представление о том: не пора ли заканчивать?
     *
     * @return
     */
    public abstract FinishAdviser getFinishAdviser();

    /**
     * Проведена ли первичная настройка объекта?
     *
     * @return
     */
    public abstract boolean isInitiated();

    /**
     * Настроить.
     */
    public abstract void initiate();

    /**
     * Каждый движущийся объект имеет отображение Drawable которое возвращается этим вызовом.
     *
     * @return
     */
    public abstract Drawable getDrawable();

    /**
     * Каждый движущийся объект имеет точку - текущее положение объекта на плоскости.
     *
     * @return
     */
    public abstract CMovingPoint getPoint();

}
