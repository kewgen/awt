package com.geargames.awt.utils.motions;

import com.geargames.Debug;
import com.geargames.awt.Drawable;
import com.geargames.awt.timers.OnTimerListener;
import com.geargames.awt.timers.TimerManager;
import com.geargames.awt.utils.MotionListener;
import com.geargames.common.String;

/**
 * user: mikhail v. kutuzov
 * date: 11.12.11 16:14
 * Инертный MotionListener c доводчиком позиции.
 *
 * @see InertMotionListener
 */
public class ElasticInertMotionListener extends MotionListener implements OnTimerListener {
    private int divider;
    private int accelerator;
    private int window;
    private int down;
    private int top;
    private int value;
    private int move;
    private int inertness;
    private int itemSize;
    private int storedMove;
    private int draggingTicks;

    private int position;
    private int backSpeed;
    private boolean released;

    private int timerId;

    public ElasticInertMotionListener() {
        divider = 100;
        accelerator = 2;
        inertness = 110;
        timerId = TimerManager.NULL_TIMER;
    }

    public void create(int top, int down, int window, int itemSize) {
        if (inertness < divider) {
            inertness = divider;
            Debug.logEx(new IllegalArgumentException());
        }
        this.top = top;
        this.down = down;
        this.window = window;
        this.position = top;
        this.backSpeed = (down - top) / 10;
        this.itemSize = itemSize;
        this.move = 0;
    }

    public void onTouch(int y) {
        endMoving();
        value = y;
        move = 0;
        storedMove = 0;
        draggingTicks = 0;
        if (Drawable.DEBUG) {
            Debug.trace("TOUCH:" + y);
        }
    }

    public void onMove(int y) {
        if (!released) {
            move = y - value;
            storedMove += move;
            position += move * accelerator;
            value = y;
            if (Drawable.DEBUG) {
                Debug.trace("MOVE: " + move);
                Debug.trace("POSITION: " + position);
            }
        }
    }

    private void startMoving() {
        if (Drawable.DEBUG) {
            Debug.log(String.valueOfC("startMoving(): released=").concatB(released).concatC("; timerId=").concatI(timerId));
        }
        released = true;
        if (timerId != TimerManager.NULL_TIMER) {
            TimerManager.setTickTimer(timerId, this);
        } else {
            timerId = TimerManager.setTickTimer(this);
        }
    }

    private void endMoving() {
        if (Drawable.DEBUG) {
            Debug.log(String.valueOfC("endMoving(): released=").concatB(released).concatC("; timerId=").concatI(timerId));
        }
        released = false;
        if (timerId != TimerManager.NULL_TIMER) {
            TimerManager.killTimer(timerId);
            timerId = TimerManager.NULL_TIMER;
        }
    }

    public void onTimer(int timerId) {
        if (timerId != this.timerId) {
            return;
        }
        if (released) { //todo: это условие обязательно?
            if (storedMove != 0) {
                if (position + window <= down || position >= top) {
                    storedMove = 0;
                } else {
                    storedMove *= divider;
                    storedMove /= inertness;
                    if (draggingTicks != 0) { //todo: это зачем?
                        storedMove /= draggingTicks;
                    }
                }
                position += storedMove * accelerator;
                if (Drawable.DEBUG) {
                    Debug.trace("INERTIA = " + storedMove);
                    Debug.trace("POSITION = " + position);
                }
            } else {
                if (position > top) {
                    position -= position - top > backSpeed ? backSpeed : position - top;
                } else if (position != top && position + window < down) {
                    position += down - window - position > backSpeed ? backSpeed : down - window - position;
                } else {
                    int margin = (-position + top) % itemSize;
                    if (margin != 0) {
                        if (move > 0) {
                            margin = itemSize - margin;
                            position -= margin > backSpeed ? backSpeed : margin;
                        } else {
                            position += margin > backSpeed ? backSpeed : margin;
                        }
                    } else {
                        endMoving(); //todo: Только здесь останавливать таймер?
                    }
                }
            }
            draggingTicks = 0;
        } else {
            draggingTicks++;
            endMoving(); //todo: Здесь надо останавливать таймер?
        }
    }

    public int getTop() {
        return top;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void onRelease(int y) {
        if (Drawable.DEBUG) {
            Debug.trace("RELEASED");
        }
        startMoving();
    }

    public void onOutOfBounds() {
        if (Drawable.DEBUG) {
            Debug.trace("OUT OF BOUNDS");
        }
        startMoving();
    }

    public void onClick(int number) {
    }

    public boolean isCentered() {
        return false;
    }

    public int getDivider() {
        return divider;
    }

    public void setDivider(int divider) {
        this.divider = divider;
    }

    public int getAccelerator() {
        return accelerator;
    }

    public void setAccelerator(int accelerator) {
        this.accelerator = accelerator;
    }

    public int getInertness() {
        return inertness;
    }

    public void setInertness(int inertness) {
        this.inertness = inertness;
    }

}