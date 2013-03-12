package com.geargames.awt.utils.motions;

import com.geargames.ConsoleDebug;
import com.geargames.awt.Drawable;
import com.geargames.awt.utils.MotionListener;
import com.geargames.common.*;
import com.geargames.common.String;
import com.geargames.common.env.SystemEnvironment;

/**
 * user: Mikhail V. Kutuzov
 * date: 15.01.12
 * time: 12:54
 * Центрующийся (дискретно позиционируется по пунктам меню), инертный MotionListener с функцией доводчика в позицию.
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
    private int itemDiff;
    private int itemsAmount;

    private int position;
    private int backStep;
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
        this.backStep = itemSize / 10;
        this.itemSize = itemSize;
        this.relativeClickedPosition = -1;
        this.center = center;
        this.itemDiff = itemSize + itemOffset;
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
            SystemEnvironment.getInstance().getDebug().trace(com.geargames.common.String.valueOfC("TOUCH:").concatI(y));
        }
    }

    public void onMove(int y) {
        relativeClickedPosition = -1;
        move = y - value;
        position += move * accelerator;
        value = y;
        storedMove += move;
        if (Drawable.DEBUG) {
            SystemEnvironment.getInstance().getDebug().trace(String.valueOfC("MOVE: ").concatI(move));
            SystemEnvironment.getInstance().getDebug().trace(String.valueOfC("POSITION: ").concatI(position));
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
                    SystemEnvironment.getInstance().getDebug().trace(String.valueOfC("INERTIA = ").concatI(storedMove));
                    SystemEnvironment.getInstance().getDebug().trace(String.valueOfC("POSITION = ").concatI(position));
                }
            } else {
                if (relativeClickedPosition == -1) {
                    if (position > center) {
                        position -= position - center > backStep ? backStep : position - center;
                    } else if (position + window - itemDiff < center) {
                        int tmp = center - window - position + itemDiff;
                        position += tmp > backStep ? backStep : tmp;
                    } else {
                        int margin = (-position + center) % itemSize;
                        if (margin != 0) {
                            margin = getCentralPositionNumber() * itemSize + position - center;
                            if (margin > 0) {
                                position -= margin > backStep ? backStep : margin;
                            } else {
                                margin = -margin;
                                position += margin > backStep ? backStep : margin;
                            }
                        }
                    }
                } else {
                    int clickedPosition = position + relativeClickedPosition;
                    if (clickedPosition > center) {
                        position -= -center + clickedPosition > backStep ? backStep : -center + clickedPosition;
                    } else if (clickedPosition < center) {
                        position += -clickedPosition + center > backStep ? backStep : -clickedPosition + center;
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
            SystemEnvironment.getInstance().getDebug().trace(String.valueOfC("RELEASED"));
        }
        released = true;
    }

    public void onOutOfBounds() {
        if (Drawable.DEBUG) {
            SystemEnvironment.getInstance().getDebug().trace(String.valueOfC("OUT OF BOUNDS"));
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
                SystemEnvironment.getInstance().getDebug().trace(String.valueOfI(number));
                SystemEnvironment.getInstance().getDebug().trace(String.valueOfI(relativeClickedPosition).concatC(" ").concatI(position));
                SystemEnvironment.getInstance().getDebug().trace(String.valueOfI(position).concatC(" ").concatI(window));
                SystemEnvironment.getInstance().getDebug().trace(String.valueOfI(position + window - itemDiff));
                SystemEnvironment.getInstance().getDebug().trace(String.valueOfI(center));
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
