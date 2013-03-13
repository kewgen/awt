package com.geargames.awt.utils.motions;

import com.geargames.awt.Drawable;
import com.geargames.awt.timers.OnTimerListener;
import com.geargames.awt.timers.TimerManager;
import com.geargames.awt.utils.MotionListener;
import com.geargames.awt.utils.ScrollListener;
import com.geargames.common.String;
import com.geargames.common.env.SystemEnvironment;

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

    private ScrollListener scrollListener;

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
            SystemEnvironment.getInstance().getDebug().warning(String.valueOfC("Inertness is too tiny"));
        }
        this.top = top;
        this.down = down;
        this.window = window;
        this.position = top;
        this.backSpeed = (down - top) / 10;
        this.itemSize = itemSize;
        this.move = 0;
    }

    @Override
    public void onTouch(int y) {
        endMoving();
        value = y;
        move = 0;
        storedMove = 0;
        draggingTicks = 0;
        if (Drawable.DEBUG) {
            SystemEnvironment.getInstance().getDebug().trace(String.valueOfC("TOUCH: ").concat(y));
        }
    }

    @Override
    public void onMove(int y) {
        if (!released) {
            move = y - value;
            storedMove += move;
            value = y;
            setPosition(position + move * accelerator);
            if (Drawable.DEBUG) {
                SystemEnvironment.getInstance().getDebug().trace(String.valueOfC("MOVE: ").concat(move));
                SystemEnvironment.getInstance().getDebug().trace(String.valueOfC("POSITION: ").concat(position));
            }
        }
    }

    private void startMoving() {
        if (Drawable.DEBUG) {
            SystemEnvironment.getInstance().getDebug().trace(String.valueOfC("startMoving(): released=").concatB(released).concatC("; timerId=").concatI(timerId));
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
            SystemEnvironment.getInstance().getDebug().trace(String.valueOfC("endMoving(): released=").concatB(released).concatC("; timerId=").concatI(timerId));
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
                if (position + window <= down || position >= top) {
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
                    SystemEnvironment.getInstance().getDebug().trace(String.valueOfC("INERTIA = ").concat(storedMove));
                    SystemEnvironment.getInstance().getDebug().trace(String.valueOfC("POSITION = ").concat(position));
                }
            } else {
                if (position > top) {
                    setPosition(position - (position - top > backSpeed ? backSpeed : position - top));
                } else if (position != top && position + window < down) {
                    setPosition(position + (down - window - position > backSpeed ? backSpeed : down - window - position));
                } else {
                    int margin = (-position + top) % itemSize;
                    if (margin != 0) {
                        if (move > 0) {
                            margin = itemSize - margin;
                            setPosition(position - (margin > backSpeed ? backSpeed : margin));
                        } else {
                            setPosition(position + (margin > backSpeed ? backSpeed : margin));
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

    @Override
    public int getTop() {
        return top;
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
            SystemEnvironment.getInstance().getDebug().trace(String.valueOfC("RELEASED"));
        }
        startMoving();
    }

    @Override
    public void onOutOfBounds() {
        if (Drawable.DEBUG) {
            SystemEnvironment.getInstance().getDebug().trace(String.valueOfC("OUT OF BOUNDS"));
        }
        startMoving();
    }

    @Override
    public void onClick(int number) {
    }

    @Override
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