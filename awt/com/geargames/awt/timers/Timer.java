package com.geargames.awt.timers;

import com.geargames.awt.AWTObject;
import com.geargames.awt.Application;
import com.geargames.common.Event;

/**
 * User: abarakov
 * Date: 23.02.13 11:22
 */
public /*abstract*/ class Timer {

//    private boolean enabled;
    private int id;                    // ID таймера, именно через него к таймеру возможно обратиться из вне
    private int interval;              // Интервал в миллисекундах, с которым данный таймер должен срабатывать
                                       // Если interval == 0, это означает, что данный таймер будет однократно
                                       // срабатывать каждый tick приложения.
    // nextActivate, lastActivated
    private int timeActivate;          // Время срабатывания таймера. Время в данном контексте - это время относительно
                                       // времени старта системы
    private AWTObject callBackElement; // Элемент, которому будет сообщено о срабатывании таймера
    // isMultiple
//  private boolean isPeriodic;        // true, если таймер должен выполняться многократно
    private byte data;

    private final static byte TIMER_TYPE_MASK     = 0x07; // 0b00000111
    private final static byte TIMER_STATE_MASK    = 0x18; // 0b00011000

    public  final static byte SINGLE_TIMER_TYPE   = 1;    // 0b00000001  // ONESHOT_TIMER
    public  final static byte PERIODIC_TIMER_TYPE = 2;    // 0b00000010
    public  final static byte TICK_TIMER_TYPE     = 3;    // 0b00000011

    private final static byte NEED_INITIATE_STATE = 8;    // 0b00001000
//  private final static byte NEED_KILL_STATE     = 16;   // 0b00010000

    protected Timer() {

    }

    /**
     * Вызывается при инициализации таймера.
     */
    public void init(int id, int interval, byte timerType, AWTObject callBackElement) {
        this.id              = id;
        this.timeActivate    = interval == 0 ? 0 : TimerManager.millisTime() + interval;
        this.interval        = interval;
        this.callBackElement = callBackElement;
        this.data            = (byte) (timerType + NEED_INITIATE_STATE);
//        needInitiate();
    }

    protected void initiate() {
        data = (byte) (data & ~TIMER_STATE_MASK);
    }

//    public void reset() {
//        this.timeActivate = TimerManager.getInstance().millisTime() + interval;
//    }

//    protected void needInitiate() {
//        TimerManager.getInstance().timerRequiredInitiate(this);
//    }

    public void optimize(int subtractor) {
        timeActivate -= subtractor;
    }

//    public boolean getEnabled() {
//        return enabled;
//    }
//
//    public void setEnabled(boolean enabled) {
//        this.enabled = enabled;
//        if (enabled) {
//            needInitiate();
//        } else {
//            TimerManager.getInstance().stopTimer(this);
//        }
//    }

    public int getId() {
        return id;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
//        if (interval >= 0) {
//            needInitiate();
//        }
    }

    public int getTimeActivate() {
        return timeActivate;
    }

    public void setTimeActivate(int timeActivate) {
        this.timeActivate = timeActivate;
    }

    // needNextActivation, needReActivation
    public int getNextTimeActivation() {
        return (isPeriodicTimer() && interval > 0 ? timeActivate + interval : -1); // interval == 0 - случай игнорируется
    }

    public AWTObject getCallBackElement() {
        return callBackElement;
    }

    public byte getTimerType() {
        return (byte) (data & TIMER_TYPE_MASK);
    }

    public boolean isSingleTimer() {
        return (data & TIMER_TYPE_MASK) == SINGLE_TIMER_TYPE;
    }

    // isMultipleTimer
    public boolean isPeriodicTimer() {
        return (data & TIMER_TYPE_MASK) == PERIODIC_TIMER_TYPE;
    }

    public boolean isTickTimer() {
        return (data & TIMER_TYPE_MASK) == TICK_TIMER_TYPE;
    }

    public boolean isNeedInitiate() {
        return (data & TIMER_STATE_MASK) == NEED_INITIATE_STATE;
    }

//    public boolean isNeedKill() {
//        return (data & TIMER_STATE_MASK) == NEED_KILL_STATE;
//    }

//    /**
//     * Запустить таймер.
//     */
//    public void start() {
//        setEnabled(true);
//    }
//
//    /**
//     * Остановить таймер.
//     */
//    public void stop() {
//        setEnabled(false);
//    }

    /**
     * Вызывается каждый раз при срабатывании таймера.
     */
    protected void onTimer() {
//        Application.getInstance().eventAdd(Event.EVENT_TIMER, id, callBackElement);
        callBackElement.onTimer(id);
    }

}

// ----- Устаревший код ------------------------------------------------------------------------------------------------

///**
// * Класс таймера выполняющегося однократно.
// * User: abarakov
// * Date: 23.02.13 11:23
// */
//public abstract class SingleTimer extends Timer {
//
//    public boolean isMultiple() {
//        return false;
//    }
//
//    public int getNextTimeActivate() {
//        return -1;
//    }
//
//}
//
///**
// * Класс таймера выполняющегося многократно.
// * User: abarakov
// * Date: 23.02.13 11:23
// */
//public abstract class MultipleTimer extends Timer {
//
//    public boolean isMultiple() {
//        return true; // не всегда true
//    }
//
//    public int getNextTimeActivate() {
//        return getTimeActivate() + getInterval();
//    }
//
//}
//
///**
// * User: abarakov
// * Date: 23.02.13 12:03
// */
//// RepeatTimer
//public class PeriodicTimer {
//}
//
///**
// * Таймер создающий задержку перед началом работы другого таймера.
// * Или
// * Таймер с задержкой перед началом работы.
// * User: abarakov
// * Date: 23.02.13 12:00
// */
//public class DelayTimer {
//}
//
//public class TickTimer {
//}