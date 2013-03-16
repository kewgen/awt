package com.geargames.awt.utils.motions;

import com.geargames.awt.Drawable;
import com.geargames.awt.timers.OnTimerListener;
import com.geargames.awt.timers.TimerManager;
import com.geargames.awt.utils.MotionListener;
import com.geargames.awt.utils.ScrollListener;
import com.geargames.common.String;
import com.geargames.common.logging.Debug;

/**
 * @author mikhail v. kutuzov
 * created: 09.02.12  18:57
 * Инертный (сохраняющий свою скорость на столько, на сколько мал параметр inertness по сравеннию с divider) MotionListener.
 * По смыслу инерции никогда не следует держать inertness <= divider (это будет случай когда менюшка не сможет
 * затормозить, пока не докатится до своего края).
 */
public class InertMotionListener extends MotionListener implements OnTimerListener {
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

    private ScrollListener scrollListener;

    private int timerId;

    public InertMotionListener() {
        divider = 100;
        accelerator = 2;
        inertness = 110;
        timerId = TimerManager.NULL_TIMER;
    }

    public void create(int top, int down, int window) {
        if (inertness < divider) {
            inertness = divider;
            Debug.warning(String.valueOfC("Inertness is too tiny"));
        }
        this.top = top;
        this.down = down;
        this.window = window;
        this.position = top;
        this.backSpeed = (down - top) / 10;
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
            Debug.debug(String.valueOfC("TOUCH: ").concat(y));
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
                Debug.debug(String.valueOfC("MOVE: ").concat(move));
                Debug.debug(String.valueOfC("POSITION: ").concat(position));
            }
        }
    }

    private void startMoving() {
        if (Drawable.DEBUG) {
            Debug.debug(String.valueOfC("startMoving(): released=").concatB(released).concatC("; timerId=").concatI(timerId));
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
            Debug.debug(String.valueOfC("endMoving(): released=").concatB(released).concatC("; timerId=").concatI(timerId));
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
                setPosition(position + storedMove/* * accelerator*/); //todo: умножение на accelerator?
                if (Drawable.DEBUG) {
                    Debug.debug(String.valueOfC("INERTIA = ").concat(storedMove));
                    Debug.debug(String.valueOfC("POSITION = ").concat(position));
                }
            } else {
                if (position > top) {
                    if (Drawable.DEBUG) {
                        Debug.debug(String.valueOfC("return up"));
                    }
                    setPosition(position - (position - top > backSpeed ? backSpeed : position - top));
                } else if (position != top && position + window < down) {
                    if (Drawable.DEBUG) {
                        Debug.debug(String.valueOfC("return down position=").concat(position));
                        Debug.debug(String.valueOfC("window=").concat(window));
                        Debug.debug(String.valueOfC("down=").concat(down));
                        Debug.debug(String.valueOfC("top=").concat(top));
                    }
                    setPosition(position + (down - window - position > backSpeed ? backSpeed : down - window - position));
                } else {
                    endMoving(); //todo: Только здесь останавливать таймер?
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
            Debug.debug(String.valueOfC("RELEASED"));
        }
        startMoving();
    }

    @Override
    public void onOutOfBounds() {
        if (Drawable.DEBUG) {
            Debug.debug(String.valueOfC("OUT OF BOUNDS"));
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