package com.geargames.awt.utils;

import com.geargames.awt.PostDrawable;
import com.geargames.common.timers.TimerListener;
import com.geargames.common.timers.TimerIdMap;
import com.geargames.common.timers.TimerManager;
import com.geargames.common.Graphics;
import com.geargames.common.String;
import com.geargames.common.logging.Debug;

/**
 * User: mikhail.kutuzov, abarakov
 * Date: 21.11.11
 */
public class LinearVanishingStrategy extends GraphicsStrategy implements TimerListener {

    private final static boolean DEBUG = false;
    public final static byte HIDDEN_STATE  = 0; // Компонент невидим/скрыт
    public final static byte SHOWING_STATE = 1; // Компонент появляется
    public final static byte SHOWN_STATE   = 2; // Компонент появился
    public final static byte HIDING_STATE  = 3; // Компонент становится невидимым

    private PostDrawable owner;
    private byte state        = HIDDEN_STATE;
    private int showingTime   = 100;
    private int lifeTime      = 2000; // hideTimeout
    private int hidingTime    = 100;
    private byte transparency = 100;  // уровень прозрачности - число в интервале 0 .. 100, где 100 - полная прозрачность.
    private int startTime     = 0;    // время начала процесса (появления/сокрытия).

    public LinearVanishingStrategy(PostDrawable owner) {
        this.owner = owner;
    }

    @Override
    public void draw(Graphics graphics) {
        if (transparency > 0) {
            if (transparency < 100) {
                int oldTransparency = graphics.getTransparency();
                graphics.setTransparency(transparency);
                owner.postDraw(graphics);
                graphics.setTransparency(oldTransparency);
            }
        } else {
            owner.postDraw(graphics);
        }
    }

    /**
     * Метод вызывается каждый раз при срабатывании таймера.
     * @param timerId - идентификатор сработавшего таймера, который вызвал данный метод.
     */
    @Override
    public void onTimer(int timerId) {
        if (timerId == TimerIdMap.AWT_TEXTHINT_GRAPHICS_STRATEGY_TICK) {
            switch (state) {
                case HIDDEN_STATE:
                    if (DEBUG) {
                        Debug.debug(String.valueOfC("LinearVanishingStrategy.onTimer(): HIDDEN_STATE"));
                    }
                    break;
                case HIDING_STATE: {
                    //todo: Возможна вероятность перехода через 10-дневный срок запуска приложения
                    // Это может привести к тому, что одно время было сохранено до перехода, а второе после,
                    // следовательно два этих времени недопустимо сравнивать
                    int elapsedTime = TimerManager.millisTime() - startTime;
                    int currentTransparency = (int)((float)elapsedTime / hidingTime * 100);
                    if (DEBUG) {
                        Debug.debug(String.valueOfC("LinearVanishingStrategy.onTimer(): HIDING_STATE: transparency=").concat(currentTransparency));
                    }
                    if (currentTransparency >= 100) {
                        // Компонент стал полностью прозрачным
                        transparency = 100;
                        state = HIDDEN_STATE;
                        TimerManager.killTimer(TimerIdMap.AWT_TEXTHINT_GRAPHICS_STRATEGY_TICK);
                    } else {
                        transparency = (byte)currentTransparency;
                    }
                    break;
                }
                case SHOWING_STATE: {
                    //todo: Возможна вероятность перехода через 10-дневный скрок запуска приложения
                    // Это может привести к тому, что одно время было сохранено до перехода, а второе после,
                    // следовательно два этих времени недопустимо сравнивать
                    int elapsedTime = TimerManager.millisTime() - startTime;
                    int currentTransparency = (int) (100 - (float)elapsedTime / showingTime * 100);
                    if (DEBUG) {
                        Debug.debug(String.valueOfC("LinearVanishingStrategy.onTimer(): SHOWING_STATE: transparency=").concat(currentTransparency));
                    }
                    if (currentTransparency <= 0) {
                        // Компонент стал полностью непрозрачным
                        transparency = 0;
                        state = SHOWN_STATE;
                        starDelayedHiding();
                    } else {
                        transparency = (byte)currentTransparency;
                    }
                    break;
                }
                case SHOWN_STATE: {
                    // Сработал таймер с интервалом lifeTime
                    if (DEBUG) {
                        Debug.debug(String.valueOfC("LinearVanishingStrategy.onTimer(): SHOWN_STATE"));
                    }
                    startHiding();
                    break;
                }
                default:
                    if (DEBUG) {
                        Debug.error(String.valueOfC("LinearVanishingStrategy.onTimer(): unknown"));
                    }
                    break;
            }
        }
    }

    /**
     * Начать процесс отображения компонента.
     */
    public void startShowing() {
        if (showingTime == 0 || state == SHOWN_STATE) {
            // Компонент должен появиться мгновенно.
            state = SHOWN_STATE;
            transparency = 0;
            starDelayedHiding();
        } else
        if (state == SHOWING_STATE) {
            // Игнорируем вызов метода, т.к. компонент уже находится в стадии появления.
            //todo: Нужно начать процесс появления компонента заново
        } else {
            // Компонент должен быть отображен
            if (state == HIDING_STATE) {
                // Компонент находится в стадии сокрытия
                int elapsedTime = (int)(showingTime * (100.0 - transparency) / 100.0);
                startTime = TimerManager.millisTime() - elapsedTime; // Оформим начало процесса "задним числом"
            } else {
                // Компонент скрыт
                transparency = 100;
                startTime = TimerManager.millisTime();
            }
            state = SHOWING_STATE;
            TimerManager.setTickTimer(TimerIdMap.AWT_TEXTHINT_GRAPHICS_STRATEGY_TICK, this);
        }
    }

    /**
     * Начать процесс сокрытия компонента.
     */
    public void startHiding() {
        if (state == HIDDEN_STATE || state == HIDING_STATE) {
            // Игнорируем вызов метода, т.к. компонент либо уже полностью скрыт, либо находится в стадии сокрытия.
        } else
        if (hidingTime == 0) {
            // Компонент должен скрыться мгновенно.
            state = HIDDEN_STATE;
            transparency = 100;
            TimerManager.killTimer(TimerIdMap.AWT_TEXTHINT_GRAPHICS_STRATEGY_TICK);
        } else {
            // Компонент должен быть скрыт
            if (state == SHOWING_STATE) {
                // Компонент находится в стадии появления
                int elapsedTime = (int)(hidingTime * transparency / 100.0);
                startTime = TimerManager.millisTime() - elapsedTime; // Оформим начало процесса "задним числом"
            } else {
                // Компонент был отображен
                transparency = 0;
                startTime = TimerManager.millisTime();
            }
            state = HIDING_STATE;
            TimerManager.setTickTimer(TimerIdMap.AWT_TEXTHINT_GRAPHICS_STRATEGY_TICK/*, timerInterval*/, this);
        }
    }

    private void starDelayedHiding() {
        // Запустим таймер на автоматическое скрытие компонента спустя hideTimeout миллисекунд.
        TimerManager.setSingleTimer(TimerIdMap.AWT_TEXTHINT_GRAPHICS_STRATEGY_TICK, lifeTime, this);
    }

    /**
     * Вернуть компонент, видимостью которого, управляет данная стратегия.
     * @return
     */
    @Override
    public PostDrawable getOwner() {
        return owner;
    }

    /**
     * Вернуть текущее состояние видимости компонента.
     */
    public byte getState() {
        return state;
    }

    /**
     * Вернуть время, за которое компонент будет плавно переведен из состояния полностью невидим, в состояние полностью
     * видим.
     * @return Время в миллисекундах.
     */
    public int getShowingTime() {
        return showingTime;
    }

    public void setShowingTime(int time) {
        this.showingTime = time;
    }

    /**
     * Вернуть время, через которое компонент начнет процесс автоматического сокрытия.
     * @return Время в миллисекундах.
     */
    public int getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(int time) {
        this.lifeTime = time;
    }

    /**
     * Вернуть время, за которое компонент будет плавно переведен из состояния полностью видим, в состояние полностью
     * невидим.
     * @return Время в миллисекундах.
     */
    public int getHidingTime() {
        return hidingTime;
    }

    public void setHidingTime(int time) {
        this.hidingTime = time;
    }

    public boolean isFullTransparent() {
        return transparency == 100;
    }

}
