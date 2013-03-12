package com.geargames.awt.cinematics.consumers;

import com.geargames.awt.Drawable;
import com.geargames.awt.cinematics.CMovingPoint;
import com.geargames.awt.cinematics.FinishAdviser;

/**
 * User: mikhail.kutuzov
 * Date: 26.11.11
 * Time: 20:43
 */
public class Flying extends AcceleratedObject {
    private CMovingPoint speed;
    private CMovingPoint point;
    private Drawable drawable;
    private boolean initiated;
    private FinishAdviser finishAdviser;
    private int y;
    private int x;

    public Flying() {
        point = new CMovingPoint();
        initiated = false;
    }

    public void setSpeed(CMovingPoint speed) {
        initiated = false;
        this.speed = speed;
    }

    protected void setInitiated(boolean initiated) {
        this.initiated = initiated;
    }

    public boolean isInitiated() {
        return initiated;
    }

    public void initiate() {
        speed.setChanger(getAcceleration());
        point.setChanger(speed);
        point.setX(x);
        point.setY(y);
        initiated = true;
    }

    public CMovingPoint getPoint() {
        return point;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setY(int top) {
        initiated = false;
        y = top;
    }

    public void setX(int left) {
        initiated = false;
        x = left;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setFinishAdviser(FinishAdviser finishAdviser) {
        this.finishAdviser = finishAdviser;
    }

    public FinishAdviser getFinishAdviser() {
        return finishAdviser;
    }

    public boolean onEvent(int code, int param, int x, int y) {
        super.onEvent(code, param, x, y);
        return drawable.onEvent(code, param, x, y);
    }

    public void onFlyFinish() {
        //Debug.log("Пупсик прилетел");
    }

    public int getHeight() {
        return drawable.getHeight();
    }

    public int getWidth() {
        return drawable.getWidth();
    }
}
