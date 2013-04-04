package com.geargames.awt.utils.motions;

import com.geargames.awt.Drawable;
import com.geargames.common.timers.TimerListener;
import com.geargames.common.timers.TimerManager;
import com.geargames.awt.utils.MotionListener;
import com.geargames.awt.utils.ScrollListener;
import com.geargames.common.logging.Debug;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Date: 15.01.12 12:54
 * Центрующийся (дискретно позиционируется по пунктам меню), инертный MotionListener с функцией доводчика в позицию.
 *
 * @see InertMotionListener
 * @see ElasticInertMotionListener
 */
public class CenteredElasticInertMotionListener extends MotionListener implements TimerListener {
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

    private ScrollListener scrollListener;

    private int timerId;

    public CenteredElasticInertMotionListener() {
        divider = 100;
        accelerator = 1;
        inertness = 110;
        timerId = TimerManager.NULL_TIMER;
    }

    public void create(int center, int itemOffset, int window, int itemSize) {
        if (inertness < divider) {
            inertness = divider;
            Debug.warning("Inertness is too tiny");
        }
        if (itemSize == 0) {
            Debug.error("itemSize == 0");
        }
        this.window = window;
        this.position = center;
        this.backStep = itemSize / 10;
        this.itemSize = itemSize;
        this.relativeClickedPosition = -1;
        this.center = center;
        this.itemDiff = itemSize + itemOffset;
        this.released = false;
        this.instinctPosition = false;
        this.itemsAmount = window / itemSize;
        this.move = 0;
        this.value = 0;
    }

    @Override
    public void onTouch(int y) {
        endMoving();
        value = y;
        move = 0;
        storedMove = 0;
        draggingTicks = 0;
        if (Drawable.DEBUG) {
            Debug.debug("TOUCH: "+y);
        }
    }

    @Override
    public void onMove(int y) {
        if (!released) {
            relativeClickedPosition = -1;
            move = y - value;
            storedMove += move;
            value = y;
            setPosition(position + move * accelerator);
            if (Drawable.DEBUG) {
                Debug.debug("MOVE: "+move);
                Debug.debug("POSITION: "+position);
            }
        }
    }

    private void startMoving() {
        if (Drawable.DEBUG) {
            Debug.debug("startMoving(): released="+released+"; timerId="+timerId);
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
            Debug.debug("endMoving(): released="+released+"; timerId="+timerId);
        }
        released = false;
        if (timerId != TimerManager.NULL_TIMER) {
            TimerManager.killTimer(timerId);
            timerId = TimerManager.NULL_TIMER;
        }
    }

    @Override
    public void onTimer(int timerId) {
        if (timerId != this.timerId) {
            return;
        }
        if (released) { //todo: это условие обязательно?
            if (storedMove != 0) {
                if (position + window <= center || position >= center) {
                    storedMove = 0;
                } else {
                    storedMove *= divider;
                    storedMove /= inertness;
                    if (draggingTicks != 0) { //todo: это зачем?
                        storedMove /= draggingTicks;
                    }
                }
                setPosition(position + storedMove * accelerator);
                if (Drawable.DEBUG) {
                    Debug.debug("INERTIA = "+storedMove);
                    Debug.debug("POSITION = "+position);
                }
            } else {
                if (relativeClickedPosition == -1) {
                    if (position > center) {
                        setPosition(position - (position - center > backStep ? backStep : position - center));
                    } else if (position + window - itemDiff < center) {
                        int tmp = center - window - position + itemDiff;
                        setPosition(position + (tmp > backStep ? backStep : tmp));
                    } else {
                        int margin = (-position + center) % itemSize;
                        if (margin != 0) {
                            margin = getCentralPositionNumber() * itemSize + position - center;
                            if (margin > 0) {
                                setPosition(position - (margin > backStep ? backStep : margin));
                            } else {
                                margin = -margin;
                                setPosition(position + (margin > backStep ? backStep : margin));
                            }
                        } else {
                            endMoving(); //todo: Только здесь останавливать таймер?
                        }
                    }
                } else {
                    int clickedPosition = position + relativeClickedPosition;
                    if (clickedPosition > center) {
                        setPosition(position - (-center + clickedPosition > backStep ? backStep : -center + clickedPosition));
                    } else if (clickedPosition < center) {
                        setPosition(position + (-clickedPosition + center > backStep ? backStep : -clickedPosition + center));
                    } else {
                        endMoving(); //todo: Здесь надо останавливать таймер?
                    }
                }
            }
            draggingTicks = 0;
        } else {
            draggingTicks++;
            endMoving(); //todo: Здесь надо останавливать таймер?
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

    @Override
    public int getTop() {
        return center;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public void setPosition(int position) {
        if (this.position != position) {
            this.position = position;
            if (scrollListener != null) {
                scrollListener.onPositionChanged();
            }
        }
    }

    @Override
    public void onRelease(int y) {
        if (Drawable.DEBUG) {
            Debug.debug("RELEASED");
        }
        startMoving();
    }

    @Override
    public void onOutOfBounds() {
        if (Drawable.DEBUG) {
            Debug.debug("OUT OF BOUNDS");
        }
        startMoving();
    }

    @Override
    public void onClick(int number) {
        relativeClickedPosition = number * itemSize;
        if (!instinctPosition) {
            if (Drawable.DEBUG) {
                Debug.debug(""+number);
                Debug.debug(relativeClickedPosition+" "+position);
                Debug.debug(position+" "+window);
                Debug.debug("" + (position + window - itemDiff));
                Debug.debug(""+center);
            }
        } else {
            setPosition(center - relativeClickedPosition);
        }
    }

    @Override
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

    /**
     * Вернет объект-слушатель следящего за изменениями позиции прокрутки списка. Чаще всего это компонент "Полоса прокрутки".
     */
    @Override
    public ScrollListener getScrollListener() {
        return scrollListener;
    }

    @Override
    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

}