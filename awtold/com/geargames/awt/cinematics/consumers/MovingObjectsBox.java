package com.geargames.awt.cinematics.consumers;

import com.geargames.awt.cinematics.MovingObject;
import com.geargames.packer.Graphics;

import java.util.Vector;

/**
 * user: Mikhail V. Kutuzov
 * date: 22.12.11
 * time: 13:35
 * Коробка движущихся объектов, все помещённые в коробку объекты, в случае если их круг движения не закончен, они
 * получают события системы, иначе - объект удаляется из коробки.
 */
public class MovingObjectsBox {
    private Vector movingObjects;

    public MovingObjectsBox() {
        movingObjects = new Vector();
    }

    public void draw(Graphics graphics) {
        for (int i = 0; i < movingObjects.size(); i++) {
            ((MovingObject) movingObjects.elementAt(i)).draw(graphics);
        }
    }

    public void event(int code, int param, int x, int y) {
        for (int i = 0; i < movingObjects.size(); i++) {
            if (!((MovingObject) movingObjects.elementAt(i)).getFinishAdviser().isFinished()) {
                ((MovingObject) movingObjects.elementAt(i)).event(code, param, x, y);
            } else {
                ((MovingObject) movingObjects.elementAt(i)).event(code, param, x, y);
                movingObjects.removeElementAt(i--);
            }
        }
    }

    /**
     * В данную коробку можно добавлять только объеты летящие во времени.
     *
     * @param movingObject
     */
    public void addMovingObject(AcceleratedObject movingObject) {
        if (movingObject.getFinishAdviser() == null) {
            throw new IllegalArgumentException();
        }
        movingObject.initiate();
        movingObjects.addElement(movingObject);
    }

}
