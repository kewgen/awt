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
    private AWTObject callBackElement; // Элемент, которому следует сообщать о срабатывании таймера
    // isMultiple
    private boolean isPeriodic;        // true, если таймер должен выполняться многократно

    protected Timer() {

    }

    /**
     * Вызывается при инициализации таймера.
     */
    public void init(int id, int interval, AWTObject callBackElement, boolean isPeriodic) {
        this.id              = id;
        this.timeActivate    = interval == 0 ? 0 : TimerManager.millisTime() + interval;
        this.interval        = interval;
        this.callBackElement = callBackElement;
        this.isPeriodic      = isPeriodic;
//        needInitiate();
    }

    protected void initiate() {

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
        return (isPeriodic && interval > 0 ? timeActivate + interval : -1); // interval==0 - случай игнорируется
    }

    public AWTObject getCallBackElement() {
        return callBackElement;
    }

    // isMultiple
    public boolean isPeriodic() {
        return isPeriodic;
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
    protected void onTimer() {
        Application.getInstance().eventAdd(Event.EVENT_TIMER, id, callBackElement);
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