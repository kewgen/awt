package com.geargames.awt.utils.motions;

import com.geargames.Debug;
import com.geargames.awt.Drawable;
import com.geargames.awt.utils.MotionListener;

/**
 * user: Mikhail V. Kutuzov
 * date: 15.01.12
 * time: 12:54
 * Центрующийся(дискретно позиционируется по пунктам меню), инертный MotionListener с функцией доводчика в позицию.
 *
 * @see InertMotionListener
 * @see ElasticInertMotionListener
 */
public class CenteredElasticInertMotionListener extends MotionListener {
    private int divider;
    private int accelerator;
    private int relativeClickedPosition;
    private int window;
    private int value;
    private int move;
    private int inertness;
    private int storedMove;
    private int draggingTicks;

    private int itemSize;
    private int center;
    private int itemOffset;
    private int itemsAmount;

    private int position;
    private int backSpeed;
    private boolean released;
    public boolean instinctPosition;

    public CenteredElasticInertMotionListener() {
        divider = 100;
        accelerator = 1;
        inertness = 110;
    }

    public void create(int center, int itemOffset, int window, int itemSize) {
        if (inertness < divider) {
            throw new IllegalArgumentException();
        }

        this.window = window;
        this.position = center;
        this.backSpeed = itemSize / 10;
        this.itemSize = itemSize;
        this.relativeClickedPosition = -1;
        this.center = center;
        this.itemOffset = itemOffset;
        this.released = true;
        instinctPosition = false;
        itemsAmount = window / itemSize;
        move = 0;
        value = 0;
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
        relativeClickedPosition = -1;
        move = y - value;
        position += move * accelerator;
        value = y;
        storedMove += move;
        if (Drawable.DEBUG) {
            Debug.trace("MOVE: " + move);
            Debug.trace("POSITION: " + position);
        }
    }

    public void onTick() {
        if (released) {
            if (storedMove != 0) {
                if (position + window <= center || position >= center) {
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
                    Debug.trace("INERTIA = " + storedMove);
                    Debug.trace("POSITION = " + position);
                }
            } else {
                if (relativeClickedPosition == -1) {
                    if (position > center) {
                        position -= position - center > backSpeed ? backSpeed : position - center;
                    } else if (position + window - (itemSize - itemOffset) < center) {
                        position += center - window - position + (itemSize - itemOffset) > backSpeed ? backSpeed : center - window - position + (itemSize - itemOffset);
                    } else {
                        int margin = (-position + center) % itemSize;
                        if (margin != 0) {
                            margin = getCentralPositionNumber() * itemSize + position - center;
                            if (margin > 0) {
                                position -= margin > backSpeed ? backSpeed : margin;
                            } else {
                                margin = -margin;
                                position += margin > backSpeed ? backSpeed : margin;
                            }
                        }
                    }
                } else {
                    int clickedPosition = position + relativeClickedPosition;
                    if (clickedPosition > center) {
                        position -= -center + clickedPosition > backSpeed ? backSpeed : -center + clickedPosition;
                    } else if (clickedPosition < center) {
                        position += -clickedPosition + center > backSpeed ? backSpeed : -clickedPosition + center;
                    }
                }

            }
            draggingTicks = 0;
        } else {
            draggingTicks++;
        }
    }

    public int getCentralPositionNumber() {
        int n = (-getPosition() + getCenter() + itemSize / 2) / itemSize;
        if (n < 0 || n >= itemsAmount) {
            return -1;
        } else {
            return n;
        }
    }

    public int getTop() {
        return center;
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

    public boolean isCentered() {
        return relativeClickedPosition != -1 && position + relativeClickedPosition == center;
    }

    public int getCenter() {
        return center;
    }

    public boolean isInstinctPosition() {
        return instinctPosition;
    }

    public void setInstinctPosition(boolean instinctPosition) {
        this.instinctPosition = instinctPosition;
    }

    public void onClick(int number) {
        relativeClickedPosition = number * itemSize;
        if (!instinctPosition) {
            if (Drawable.DEBUG) {
                Debug.trace("" + number);
                Debug.trace("" + relativeClickedPosition + " " + position);
                Debug.trace("" + position + " + " + window + " = ");
                Debug.trace("" + (position + window - (itemSize - itemOffset)));
                Debug.trace("" + center);
            }
        } else {
            position = center - relativeClickedPosition;
        }
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
