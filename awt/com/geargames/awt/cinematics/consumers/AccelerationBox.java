package com.geargames.awt.cinematics.consumers;

import com.geargames.awt.cinematics.CMovingPoint;

/**
 * user: Mikhail V. Kutuzov
 * date: 17.12.11
 * time: 17:30
 * Коробка с ускорением добавляет дополнительный изменяющий вектор CMovingPoint к каждому объекту AcceleratedObject
 * как ускорение.
 */
public class AccelerationBox extends MovingObjectsBox {
    private CMovingPoint acceleration;

    public CMovingPoint getAcceleration() {
        return acceleration;
    }

    public AccelerationBox(CMovingPoint acceleration) {
        if (acceleration == null) {
            throw new IllegalArgumentException();
        }
        this.acceleration = acceleration;
    }

    /**
     * Процедура добавления объекта дополнена настройкой ускорения.
     *
     * @param movingObject
     */
    public void addMovingObject(AcceleratedObject movingObject) {
        movingObject.setAcceleration(acceleration);
        super.addMovingObject(movingObject);
    }
}
