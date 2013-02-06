package com.geargames.awt.cinematics.consumers;

import com.geargames.awt.Drawable;
import com.geargames.awt.cinematics.CMovingPoint;
import com.geargames.awt.cinematics.FinishAdviser;

/**
 * User: mikhail.kutuzov
 * Date: 12/27/11
 * Time: 9:59 AM
 */
public class Casted extends AcceleratedObject {
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private int time;
    private CMovingPoint speed;

    private boolean initiated = false;
    private CMovingPoint point;
    private Drawable drawable;
    private FinishAdviser adviser;

    public Casted() {
        point = new CMovingPoint();
        speed = new CMovingPoint();
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    protected void setInitiated(boolean initiated) {
        this.initiated = initiated;
    }

    public void setFinishAdviser(FinishAdviser adviser) {
        this.adviser = adviser;
    }

    public FinishAdviser getFinishAdviser() {
        return adviser;
    }

    public boolean isInitiated() {
        return initiated;
    }

    public void initiate() {
        if (getAcceleration() == null) {
            throw new IllegalArgumentException("An acceleration is not set.");
        } else if (getAcceleration().getChanger() != null) {
            throw new IllegalArgumentException("An acceleration is not constant.");
        }

        CMovingPoint acceleration = getAcceleration();
        double speedX = -acceleration.getX() * time / 2 - ((double) (getX() - getX2())) / (double) time;
        double speedY = -acceleration.getY() * time / 2 - ((double) (getY() - getY2())) / (double) time;

        speed.setX(speedX);
        speed.setY(speedY);
        speed.setChanger(acceleration);

        point.setX(getX());
        point.setY(getY());
        point.setChanger(speed);
        initiated = true;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public CMovingPoint getPoint() {
        return point;
    }

    public void setY(int top) {
        initiated = false;
        y1 = top;
    }

    public void setX(int left) {
        initiated = false;
        x1 = left;
    }

    public int getX() {
        return x1;
    }

    public int getY() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        initiated = false;
        this.x2 = x2;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        initiated = false;
        this.y2 = y2;
    }

    public void onFlyFinish() {
        //Debug.log("Пупсик прилетел");
    }
}
