package com.geargames.awt.cinematics.consumers;

import com.geargames.awt.cinematics.CMovingPoint;
import com.geargames.awt.cinematics.MovingObject;

/**
 * user: Mikhail V. Kutuzov
 * date: 18.12.11
 * time: 22:13
 */
public abstract class AcceleratedObject extends MovingObject {
    private CMovingPoint acceleration;

    public CMovingPoint getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(CMovingPoint acceleration) {
        setInitiated(false);
        this.acceleration = acceleration;
    }

    protected abstract void setInitiated(boolean initiated);

    @Override
    public void onFlyEnd() {
        setInitiated(false);
        onFlyFinish();
    }

    protected abstract void onFlyFinish();

}
