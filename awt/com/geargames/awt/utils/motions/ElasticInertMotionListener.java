package com.geargames.awt.utils.motions;

import com.geargames.Debug;
import com.geargames.awt.Drawable;
import com.geargames.awt.utils.MotionListener;

/**
 * user: Mikhail V. Kutuzov
 * date: 11.12.11
 * time: 16:14
 * Инертный MotionListener c доводчиком позиции.
 *
 * @see InertMotionListener
 */
public class ElasticInertMotionListener extends MotionListener {
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

    public ElasticInertMotionListener() {
        divider = 100;
        accelerator = 2;
        inertness = 110;
    }

    public void create(int top, int down, int window, int itemSize) {
        if (inertness < divider) {
            throw new IllegalArgumentException();
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
        released = false;
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
            position += move * accelerator;
            storedMove += move;
            value = y;
            if (Drawable.DEBUG) {
                Debug.trace("MOVE: " + move);
                Debug.trace("POSITION: " + position);
            }
        }
    }

    public void onTick() {
        if (released) {
            if (storedMove != 0) {
                if (position + window <= down || position >= top) {
                    storedMove = 0;
                } else {
                    storedMove *= divider;
                    storedMove /= inertness;
                    if (draggingTicks != 0) {
                        storedMove /= draggingTicks;
                    }
                }
                position += storedMove * accelerator;
                if (Drawable.DEBUG) {
                    Debug.trace("INERCIA = " + storedMove);
                    Debug.trace("POSITION = " + position);
                }
            } else {
                if (position > top) {
                    position -= position - top > backSpeed ? backSpeed : position - top;
                } else if (position != top && position < down - window) {
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
                    }
                }
            }
            draggingTicks = 0;
        } else {
            draggingTicks++;
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
        released = true;
    }

    public void onOutOfBounds() {
        if (Drawable.DEBUG) {
            Debug.trace("OUT OF BOUNDS");
        }
        released = true;
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