package com.geargames.common.timers;

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
    private int timeActivation;        // Время срабатывания таймера. Время в данном контексте - это время относительно
                                       // времени старта системы
    //todo: Переименовать callBackElement
    private TimerListener callBackElement; // Элемент, которому будет сообщено о срабатывании таймера
    // isMultiple
//  private boolean isPeriodic;        // true, если таймер должен выполняться многократно
    private byte data; // state

    private static final byte TIMER_TYPE_MASK     = 0x07; // 0b00000111
    private static final byte TIMER_STATE_MASK    = 0x18; // 0b00011000

    public  static final byte SINGLE_TIMER_TYPE   = 1;    // 0b00000001  // ONESHOT_TIMER
    public  static final byte PERIODIC_TIMER_TYPE = 2;    // 0b00000010
    public  static final byte TICK_TIMER_TYPE     = 3;    // 0b00000011

    private static final byte NEED_INITIATE_STATE = 8;    // 0b00001000
    private static final byte KILLED_STATE        = 16;   // 0b00010000

    protected Timer() {

    }

    /**
     * Вызывается при инициализации таймера.
     */
    public void init(int id, int interval, byte timerType, TimerListener callBackElement) {
        this.id              = id;
        this.timeActivation  = interval == 0 ? 0 : TimerManager.millisTime() + interval;
        this.interval        = interval;
        this.callBackElement = callBackElement;
        this.data            = (byte) ((timerType /*& TIMER_TYPE_MASK*/) | NEED_INITIATE_STATE);
//        needInitiate();
    }

    protected void initiate() {
        data = (byte) (data & ~TIMER_STATE_MASK);
    }

    protected void killed() {
        data = (byte) (data | KILLED_STATE);
    }

//    public void reset() {
//        this.timeActivation = TimerManager.getInstance().millisTime() + interval;
//    }

//    protected void needInitiate() {
//        TimerManager.getInstance().timerRequiredInitiate(this);
//    }

//    public void optimize(int subtractor) {
//        timeActivate -= subtractor;
//    }

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

    public int getTimeActivation() {
        return timeActivation;
    }

    public void setTimeActivation(int timeActivation) {
        this.timeActivation = timeActivation;
    }

    // needNextActivation, needReActivation
    public int getNextTimeActivation() {
        return timeActivation + interval;
    }

    public TimerListener getCallBackElement() {
        return callBackElement;
    }

    @Deprecated
    public byte getData() {
        return data;
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

    public boolean isKilled() {
        return (data & TIMER_STATE_MASK) == KILLED_STATE;
    }

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
    public void onTimer() {
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
//        return getTimeActivation() + getInterval();
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