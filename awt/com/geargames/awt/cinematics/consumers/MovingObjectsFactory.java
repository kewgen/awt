package com.geargames.awt.cinematics.consumers;

import com.geargames.awt.Label;
import com.geargames.awt.VanishingLabel;
import com.geargames.awt.cinematics.CMovingPoint;
import com.geargames.awt.cinematics.CPoint;
import com.geargames.common.String;

/**
 * user: Mikhail V. Kutuzov
 * date: 27.12.11
 * time: 21:00
 */
public class MovingObjectsFactory {

    public static void updateCasted(Casted casted, int x, int y, int x2, int y2, int time) {
        TickFinishAdviser adviser = (TickFinishAdviser) casted.getFinishAdviser();
        adviser.setTick(time);
        casted.setX(x);
        casted.setX2(x2);
        casted.setY(y);
        casted.setY2(y2);
        casted.setTime(time);
    }

    /**
     * Здесь создаётся брошенная в сторону строчка символов(в постоянном поле ускорения).
     * Само ускорение проставляется объекту при добавлении в соотвествующую коробку движущихся объектов.
     *
     * @param data строка символов
     * @param x    начальные координаты
     * @param y
     * @param x2   конечные координаты
     * @param y2
     * @param time расчётное время полёта
     * @return объект движущийся во времени
     */
    public static AcceleratedObject createCasted(com.geargames.common.String data, int x, int y, int x2, int y2, int time) {
        Casted casted = new Casted();
        Label label = new Label();
        casted.setDrawable(label);
        label.setData(data);
        casted.setFinishAdviser(new TickFinishAdviser(time));
        casted.setX(x);
        casted.setX2(x2);
        casted.setY(y);
        casted.setY2(y2);
        casted.setTime(time);
        return casted;
    }

    /**
     * Здесь создаётся летящая вверх строчка символов, которая исчезает за одну секунду, начиная с третьей секунды полёта.
     *
     * @param data
     * @param x
     * @param y
     * @return объект движущийся во времени
     */
    public static AcceleratedObject createFlyingVanishingLabel(String data, int x, int y, byte color) {
        Flying flying = new Flying();
        VanishingLabel label = new VanishingLabel();
        label.setHideTimeout(1500);
        label.setStateChangeTime(500);
        label.setColor(color);
        flying.setDrawable(label);
        CMovingPoint speed = new CMovingPoint();
        speed.setX(0);
        speed.setY(-2);
        speed.setChanger(null);
        flying.setSpeed(speed);
        ((VanishingLabel) flying.getDrawable()).setData(data);
        flying.setFinishAdviser(new VanishFinishAdviser(label));
        flying.setX(x);
        flying.setY(y);
        ((VanishingLabel) flying.getDrawable()).show();
        return flying;
    }

    /**
     * Метод для создания нормального вектора к некоторому отрезку.
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param bySun true - вектор направлен так что предмет летит по часовой стрелке, иначе - против.
     * @return конечную точку вектора с начальной точкой в начале координат(0,0).
     */
    public static CPoint createNormalVector(int x1, int y1, int x2, int y2, boolean bySun) {
        CPoint vector = new CPoint();
        double vectorX = Math.abs(y2 - y1) / Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        double vectorY = Math.abs(x2 - x1) / Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));

        if (bySun) {
            if (x1 < x2 && y1 < y2) {
                vectorX = -vectorX;
            } else if (x1 < x2 && y1 > y2) {
            } else if (x1 > x2 && y1 < y2) {
                vectorX = -vectorX;
                vectorY = -vectorY;
            } else if (x1 > x2 && y1 > y2) {
                vectorY = -vectorY;
            } else if (y1 == y2 && x1 > x2) {
                vectorY = -vectorY;
            } else if (x1 == x2 && y1 < y2) {
                vectorX = -vectorX;
            }
        } else {
            if (x1 > x2 && y1 > y2) {
                vectorX = -vectorX;
            } else if (x1 > x2 && y1 < y2) {
            } else if (x1 < x2 && y1 < y2) {
                vectorY = -vectorY;
            } else if (x1 < x2 && y1 > y2) {
                vectorX = -vectorX;
                vectorY = -vectorY;
            } else if (y1 == y2 && x1 < x2) {
                vectorY = -vectorY;
            } else if (x1 == x2 && y1 > y2) {
                vectorX = -vectorX;
            }
        }
        vector.setX(vectorX);
        vector.setY(vectorY);
        return vector;
    }


}
