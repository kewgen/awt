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

    @Override
    protected void setInitiated(boolean initiated) {
        this.initiated = initiated;
    }

    @Override
    public boolean isInitiated() {
        return initiated;
    }

    @Override
    public void initiate() {
        speed.setChanger(getAcceleration());
        point.setChanger(speed);
        point.setX(x);
        point.setY(y);
        initiated = true;
    }

    @Override
    public CMovingPoint getPoint() {
        return point;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    @Override
    public Drawable getDrawable() {
        return drawable;
    }

    @Override
    public void setY(int top) {
        initiated = false;
        y = top;
    }

    @Override
    public void setX(int left) {
        initiated = false;
        x = left;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    public void setFinishAdviser(FinishAdviser finishAdviser) {
        this.finishAdviser = finishAdviser;
    }

    @Override
    public FinishAdviser getFinishAdviser() {
        return finishAdviser;
    }

    @Override
    public boolean onEvent(int code, int param, int x, int y) {
        super.onEvent(code, param, x, y);
        return drawable.onEvent(code, param, x, y);
    }

    @Override
    public void onFlyFinish() {
        //Debug.log("Пупсик прилетел");
    }

    @Override
    public int getHeight() {
        return drawable.getHeight();
    }

    @Override
    public int getWidth() {
        return drawable.getWidth();
    }
}
