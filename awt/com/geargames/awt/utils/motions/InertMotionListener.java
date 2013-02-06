package com.geargames.awt.utils.motions;

import com.geargames.Debug;
import com.geargames.awt.Drawable;
import com.geargames.awt.utils.MotionListener;

/**
 * @author Mikhail_Kutuzov
 *         created: 09.02.12  18:57
 *         Инертный (сохраняющий свою скорость на столько, на сколько мал параметр inertness по сравеннию с divider) MotionListener.
 *         По смыслу инерции никогда не следует держать inertness <= divider (это будет случай когда менющка не сможет затормозить ,
 *         пока не докатиться до своего края)
 */
public class InertMotionListener extends MotionListener {
    private int divider;
    private int accelerator;
    private int window;
    private int down;
    private int top;
    private int value;
    private int move;
    private int inertness;
    private int storedMove;
    private int draggingTicks;

    private int position;
    private int backSpeed;
    private boolean released;

    public InertMotionListener() {
        divider = 100;
        accelerator = 2;
        inertness = 110;
    }

    public void create(int top, int down, int window) {
        if (inertness < divider) {
            inertness = divider;
            Debug.logEx(new IllegalArgumentException());
        }
        this.top = top;
        this.down = down;
        this.window = window;
        this.position = top;
        this.backSpeed = (down - top) / 10;
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
            storedMove += move;
            position += move * accelerator;
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
                position += storedMove;
                if (Drawable.DEBUG) {
                    Debug.trace("INERCIA = " + storedMove);
                    Debug.trace("POSITION = " + position);
                }
            } else {

                if (position > top) {
                    position -= position - top > backSpeed ? backSpeed : position - top;
                } else if (position != top && position < down - window) {
                    position += down - window - position > backSpeed ? backSpeed : down - window - position;
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