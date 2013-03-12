package com.geargames.awt.timers;

import com.geargames.ConsoleDebug;
import com.geargames.awt.Eventable;
import com.geargames.common.env.SystemEnvironment;
import com.geargames.common.util.ArrayList;
import com.geargames.common.util.HashMap;
import com.geargames.common.String;

import java.util.Random;

/**
 * Класс предоставляющий функционал по использованию таймеров.
 * Каждый таймер обладает следующими обязательными свойствами:
 *     * id               Значение этого свойства используется для идентификации таймеров среди остальных;
 *     * interval         Интервал в миллисекундах, с которым данный таймер должен срабатывать;
 *     * callBackElement  Элемент, которому будет сообщено о срабатывании таймера.
 * Доступны для использования три вида таймеров:
 *     * однократный таймер   Этот тип таймера сработает только один раз спустя interval миллисекунд;
 *                            Однократный таймер удобно применять, когда требуется выполнить некоторую однократную
 *                            операцию, спустя продолжительное время (время, большее чем несколько тиков программы);
 *     * многократный таймер  Этот тип таймера может срабатывать бесконечное число раз, каждые interval миллисекунд, до
 *                            тех пор, пока таймер не будет остановлен;
 *                            Его удобно использовать для выполнения одинаковых операций, многократно, через равные
 *                            промежутки времени;
 *     * tick-таймер          Данный таймер также является многократным, но отличается от предыдущего тем, что свойство
 *                            interval для него задается всегда автоматически, таким образом, чтобы таймер срабатывал
 *                            однократно, но на каждом тике программы (на каждой итерации цикла mainLoop).
 *                            этот таймер удобнее всего использовать в тех случаях, когда реализуется некоторый плавно
 *                            протекающий процесс, например плавное угасание или перемещение компонента.
 *                            Если вам потребуется однократный tick-таймер, то вы можете создать однократный таймер со
 *                            значением свойства interval равным 0.
 * Когда любой из типов таймеров срабатывает, у элемента callBackElement будет вызван метод onTimer(), в который будет
 * передан id сработавшего таймера.
 * User: abarakov
 * Date: 23.02.13 8:54
 */
public class TimerManager {

    private static final boolean DEBUG       = false;
    private static final boolean STACK_TRACE = false;

    public static final int DINAMIC_TIMER_ID_MIN = 20001;
    public static final int DINAMIC_TIMER_ID_MAX = Integer.MAX_VALUE;

//    private static final int TIMER_OPTIMIZE_SUBTRACTOR = 864000000; // 10 дней в миллисекундах = 10*24*60*60*1000

    private static final byte UPDATE_STATUS_NONE        = 0; // метод update не выполняется
    private static final byte TICK_TIMERS_UPDATE_STATUS = 1; // в методе update апдейтятся tick-таймеры
    private static final byte TIMERS_UPDATE_STATUS      = 2; // в методе update апдейтятся таймеры

//    private static TimerManager instance;
    private static Random random = null; //todo: Думается, что рандом-генератор должен быть один на всю программу

    // ----- Функционал получения точного времени в тиках процессора ---------------------------------------------------

    // Время старта приложения, с него начинаются все отсчеты таймеров (в миллисекундах)
    // Такой baseTime нужен в случае, если компьютер/устройство работает бесперерывно уже более 10 суток
    private static long baseTime = SystemEnvironment.getInstance().getEnvironment().nanoTime() / 1000000; //todo: ConsoleEnvironment?

    /**
     * Вернуть время в миллисекундах
     */
    //todo: Перенести метод millisTime() в класс Environment
    public static int millisTime() {
        long time = SystemEnvironment.getInstance().getEnvironment().nanoTime() / 1000000; // Перевод из наносекунд в миллисекунды.
        time -= baseTime;  // Нормализация времени.
        return (int) time;
    }

    // ----- Class fields ----------------------------------------------------------------------------------------------

    // Массив таймеров сортированный в порядке уменьшения времени, когда таймер должен сработать.
    // Т.е. первый таймер в списке - таймер, который сработает позже всех и наоборот, в конце списка таймер сработающий
    // раньше всех.
    //todo: Какие значения capacity выбрать?
    private static ArrayList timers             = new ArrayList(16); //todo: ArrayList, Vector или LinkedList?
    // Список tick-таймеров
    private static ArrayList tickTimers         = new ArrayList(16);
    // Массив соответствий, id -> timer. Нужен только для ускорения поиска таймера по его id. Можно упразднить, т.к.
    // id также хранится и в самом объекте таймера.
    private static HashMap   timerIds           = new HashMap();     //todo: можно попробовать избавиться от этого списка
    // Список таймеров, требующих инициализации и размещения в основном списке таймеров.
    private static ArrayList initiateTimers     = new ArrayList(8);
//    private static ArrayList disabledTimers; // Список остановленных таймеров.
    // Список таймеров, ранее созданных, но не используемых в данный момент. Список нужен для сохранения принципа
    // "Создавай, но ничего не уничтожай, а используй повторно".
    private static ArrayList unusedTimers       = new ArrayList(16);
    private static ArrayList unusedTimersAssist = new ArrayList(8);  //todo: можно попробовать избавиться от этого списка

    private static int  lastTime      = millisTime();         // Время последнего обновления таймеров (в миллисекундах)
    private static byte updateStatus  = UPDATE_STATUS_NONE;   // Что в настоящий момент выполняется в методе update()
    private static int  nextDinamicId = DINAMIC_TIMER_ID_MIN; // id, который будет присвоен следующему таймеру.

//    public TimerManager() {
//        //todo: Какие значения capacity выбрать?
//        timers             = new ArrayList(32);
//        timerIds           = new HashMap();
//        initiateTimers     = new ArrayList(8);
//        unusedTimers       = new ArrayList(16);
//        unusedTimersAssist = new ArrayList(8);
//
//        lastTime = millisTime();
//    }

    // ----- Instance management ---------------------------------------------------------------------------------------

//    public static TimerManager getInstance() {
//        if (instance == null) {
//            instance = new TimerManager();
//            if (random == null) {
//                random = new Random();
//            }
//        }
//        return instance;
//    }

    // ----- Property management ---------------------------------------------------------------------------------------

    // ----- Вспомогательные методы ------------------------------------------------------------------------------------

    /**
     * Вернуть id таймера, который еще не занят.
     * @return
     */
    //todo: тип id-шника - int или short?
    private static int generateTimerId() {
//        int id;
//        do {
//          id = random.nextInt(DINAMIC_TIMER_ID_MAX - DINAMIC_TIMER_ID_MIN + 1) + DINAMIC_TIMER_ID_MAX;
//        } while (timerIds.containsKey(id));
//        return id;

        if (nextDinamicId < 0 || nextDinamicId > DINAMIC_TIMER_ID_MAX) {
            nextDinamicId = DINAMIC_TIMER_ID_MIN;
        }
        while (timerIds.containsKey(nextDinamicId)) {
            if (nextDinamicId == DINAMIC_TIMER_ID_MAX) {
                nextDinamicId = DINAMIC_TIMER_ID_MIN;
            } else {
                nextDinamicId++;
            }
        }
        return nextDinamicId++;
    }

    // ----- Создание и удаление таймеров ------------------------------------------------------------------------------

    /**
     * Создать таймер.
     * @param timerId - выбирайте id из следующих соображений:
     *                 отрицательные id - для всех системных таймеров (таймеров общего кода).
     *                 0..20000 - для клиентских таймеров с константным id;
     *                 20001.. - для клиентских таймеров с динамическим id, который генерирует сам менеджер;
     * @param interval
     * @param timerType - определяет типа таймера, будет ли таймер однократным, многократным или tick-таймером.
     *                  Должно принимать одно из значений: Timer.SINGLE_TIMER_TYPE, Timer.PERIODIC_TIMER_TYPE или
     *                  Timer.TICK_TIMER_TYPE
     * @param callBackElement
     * @return
     */
    // createTimer
    private static int setTimer(int timerId, int interval, byte timerType, Eventable callBackElement) {
        if (STACK_TRACE) {
            SystemEnvironment.getInstance().getDebug().trace(String.valueOfC("TimerManager.setTimer").concat(
            String.valueOfC("timer id = ").concatI(timerId)).concat(
            String.valueOfC("interval = ").concatI(interval)).concat(
            String.valueOfC("timer type = ").concatI(timerType)));
        }
        killTimer(timerId);
        if (DEBUG && timerIds.containsKey(timerId)) {
            SystemEnvironment.getInstance().getDebug().trace(String.valueOfC("TimerManager.setTimer").concat(
            String.valueOfC("Put timerId that is already in timerIds")).concat(
            String.valueOfC("timer id = ").concatI(timerId)).concat(
            String.valueOfC("interval = ").concatI(interval)).concat(
            String.valueOfC("timer type = ").concatI(timerType)));
        }
        Timer timer;
        if (!unusedTimers.isEmpty()) {
            timer = (Timer)unusedTimers.remove(unusedTimers.size() - 1);
        } else {
            timer = new Timer();
        }
        timer.init(timerId, interval, timerType, callBackElement);
        timerIds.put(timerId, timer);
        initiateTimers.add(timer);
        return timerId;
    }

    /**
     * Создать однократный таймер.
     * @param timerId
     * @param interval
     * @param callBackElement
     * @return
     */
    public static int setSingleTimer(int timerId, int interval, Eventable callBackElement) {
        return setTimer(timerId, interval, Timer.SINGLE_TIMER_TYPE, callBackElement);
    }

    /**
     * Создать однократный таймер, причем, id таймеру будет автоматически присвоен самим менеджером.
     * @param interval
     * @return
     */
    public static int setSingleTimer(int interval, Eventable callBackElement) {
        return setTimer(generateTimerId(), interval, Timer.SINGLE_TIMER_TYPE, callBackElement);
    }

    /**
     * Создать многократный таймер.
     * @param timerId
     * @param interval
     * @param callBackElement
     * @return
     */
    // setMultipleTimer
    public static int setPeriodicTimer(int timerId, int interval, Eventable callBackElement) {
        return setTimer(timerId, interval, Timer.PERIODIC_TIMER_TYPE, callBackElement);
    }

    /**
     * Создать многократный таймер, причем, id таймеру будет автоматически присвоен самим менеджером.
     * @param interval
     * @return
     */
    public static int setPeriodicTimer(int interval, Eventable callBackElement) {
        return setTimer(generateTimerId(), interval, Timer.PERIODIC_TIMER_TYPE, callBackElement);
    }

    /**
     * Создать многократный tick-таймер, т.е. таймер срабатывающий в каждой итерации цикла mainLoop.
     * @param timerId
     * @param callBackElement
     * @return
     */
    // setMultipleTimer
    public static int setTickTimer(int timerId, Eventable callBackElement) {
        return setTimer(timerId, 0, Timer.TICK_TIMER_TYPE, callBackElement);
    }

    /**
     * Создать многократный tick-таймер, т.е. таймер срабатывающий в каждой итерации цикла mainLoop, причем, id таймеру
     * будет автоматически присвоен самим менеджером.
     * @return
     */
    public static int setTickTimer(Eventable callBackElement) {
        return setTimer(generateTimerId(), 0, Timer.TICK_TIMER_TYPE, callBackElement);
    }

    /**
     * Удалить таймер
     * @param timerId
     */
    // deleteTimer, removeTimer, releaseTimer
    public static void killTimer(int timerId) {
        if (STACK_TRACE) {
            SystemEnvironment.getInstance().getDebug().trace(String.valueOfC("TimerManager.killTimer(timerId=").concat(timerId).concatC(")"));
        }
        Timer timer = findTimer(timerId);
        if (timer != null) {
            releaseTimer(timer);
        }
    }

//    /**
//     * Остановить и удалить все таймеры.
//     */
//    public static void clearTimers() {
//        if (!timers.isEmpty()) {
//            if (updateStatus == TIMERS_UPDATE_STATUS) {
//                Timer timer = (Timer)timers.get(timers.size() - 1);
//                timer.setInterval(0);              // Это для того, чтобы таймер не пересоздался в методе update
//                //todo: все таймеры сразу можно помещать в список unusedTimers, все кроме последнего таймера, который может еще использоваться
//                unusedTimersAssist.addAll(timers); // Все таймеры будут удалены сразу по окончанию цикла апдейта таймеров
//            } else {
//                unusedTimers.addAll(timers);
//                timers.clear();
//            }
//        }
//        if (!tickTimers.isEmpty()) {
//            if (updateStatus == TICK_TIMERS_UPDATE_STATUS) {
//                unusedTimersAssist.addAll(tickTimers); // Все tick-таймеры будут удалены сразу по окончанию цикла апдейта tick-таймеров
//            } else {
//                unusedTimers.addAll(tickTimers);
//                tickTimers.clear();
//            }
//        }
//        if (!initiateTimers.isEmpty()) {
//            unusedTimers.addAll(initiateTimers);
//            initiateTimers.clear();
//        }
//
//        timerIds.clear();
//    }

    // ----- Функции поиска таймеров -----------------------------------------------------------------------------------

    private static Timer findTimer(int timerId) {
        return (Timer)timerIds.get(timerId);
    }

    public static boolean isTimer(int timerId) {
        return timerIds.containsKey(timerId);
    }

    // ----- Вспомогательные методы ------------------------------------------------------------------------------------

    // removeTimer
    private static void releaseTimer(Timer timer) {
        if (STACK_TRACE) {
            SystemEnvironment.getInstance().getDebug().trace(String.valueOfC("TimerManager.releaseTimer").concat(
            String.valueOfC("timer id = ").concatI(timer.getId())).concat(
            String.valueOfC("interval = ").concatI(timer.getInterval())).concat(
            String.valueOfC("timer type = ").concatI(timer.getTimerType())).concat(
            String.valueOfC("timer status = ").concatI(updateStatus)));
        }
//        timer.setInterval(0); // Это для того, чтобы таймер не пересоздался в методе update
        if (timer.isKilled()) {
            return;
        }

        //todo: Таймер следует удалять по заранее известному индексу. С другой стороны, индекс мог уже измениться
        if (timer.isNeedInitiate()) {
            initiateTimers.remove(timer);
            unusedTimers.add(timer);
        } else {
            switch (timer.getTimerType()) {
                case Timer.SINGLE_TIMER_TYPE:
                case Timer.PERIODIC_TIMER_TYPE:
                    timers.remove(timer);
                    if (updateStatus == TIMERS_UPDATE_STATUS) {
                        unusedTimersAssist.add(timer); // Таймер будет удален сразу по окончанию цикла апдейта tick-таймеров
                    } else {
                        unusedTimers.add(timer);
                    }
                    break;
                case Timer.TICK_TIMER_TYPE:
                    if (updateStatus == TICK_TIMERS_UPDATE_STATUS) {
                        unusedTimersAssist.add(timer); // Таймер будет удален сразу по окончанию цикла апдейта таймеров
                    } else {
                        tickTimers.remove(timer);
                        unusedTimers.add(timer);
                    }
                    break;
                default:
                    //todo: это error
                    SystemEnvironment.getInstance().getDebug().warning(String.valueOfC("\"TimerManager.releaseTimer(): illegal timerType").concat(
                    String.valueOfC("timer id = ").concatI(timer.getId())).concat(
                    String.valueOfC("interval = ").concatI(timer.getInterval())).concat(
                    String.valueOfC("timer type = ").concatI(timer.getTimerType())));
            }
        }
        timerIds.remove(timer.getId());
        timer.killed();
    }

    private static void movingLastTimer(int timeActivation) {
        // Данный код одновременно реализует две функциональности:
        // 1. Поиск места в списке, куда вставить элемент, с сохранением сортированности списка;
        // 2. Перемещение последнего элемента списка (многократного таймера) в новую позицию.
        int i = timers.size() - 1;
        Timer timer = (Timer)timers.get(i);
        i--;
        /* time examples:
           48 36 35 17 17 9 2 x
                   ^- 23
         */
        while (i >= 0) {
            Timer tmpTimer = (Timer)timers.get(i);
            if (timeActivation < tmpTimer.getTimeActivate()) {
                break;
            }
            timers.set(i + 1, tmpTimer);
            i--;
        }
        timers.set(i + 1, timer);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Метод вызывается в цикле mainLoop.
     */
    public static void update(/*int elapsedTime*/) {
        int newTime = millisTime();
        if (false && STACK_TRACE) {
            if (initiateTimers.size() > 0 || timers.size() > 0) {
                SystemEnvironment.getInstance().getDebug().trace(String.valueOfC("TimerManager.update").concat(
                String.valueOfC("initiateTimers.size() = ").concatI(initiateTimers.size())).concat(
                String.valueOfC("timers.size() = ").concatI(timers.size())).concat(
                String.valueOfC("last time = ").concatI(lastTime)).concat(
                String.valueOfC("new time = ").concatI(newTime)).concat(
                String.valueOfC("delta time = ").concatI(newTime - lastTime)));
            }
        }
        if (updateStatus != UPDATE_STATUS_NONE) {
            //todo: это error
            SystemEnvironment.getInstance().getDebug().warning(String.valueOfC("TimerManager.update(): Method was called at the time, when it is already running (").concatI(updateStatus).concat(")"));
        }
        try {
            if (DEBUG && !unusedTimersAssist.isEmpty()) {
                SystemEnvironment.getInstance().getDebug().warning(String.valueOfC("TimerManager.update(): list unusedTimersAssist is not empty (size=").concatI(unusedTimersAssist.size()).concat(")"));
            }
            // Отложенная инициализация таймеров
            for (int i = 0; i < initiateTimers.size(); i++) {
                Timer timer = (Timer)initiateTimers.get(i);
                if (timer.isTickTimer()) {
                    tickTimers.add(timer);
                } else {
                    timers.add(timer);
                    movingLastTimer(timer.getTimeActivate());
                }
                timer.initiate();
            }
            initiateTimers.clear();

            //todo: использовать тип списка LinkedList
            updateStatus = TICK_TIMERS_UPDATE_STATUS;
            for (int i = 0; i < tickTimers.size(); i++) {
                //todo: список может измениться, таймеры могут быть удалены, перемещны, изменены
                Timer timer = (Timer)tickTimers.get(i);
                if (STACK_TRACE) {
                    SystemEnvironment.getInstance().getDebug().trace(String.valueOfC("TimerManager.update(): timer.onTickTimer").concat(
                    String.valueOfC("timer id=").concatI(timer.getId())).concat(
                    String.valueOfC("interval=").concatI(timer.getInterval())).concat(
                    String.valueOfC("timer type=").concatI(timer.getTimerType())).concat(
                    String.valueOfC("time activation=").concatI(timer.getTimeActivate())).concat(
                    String.valueOfC("next time activation=").concatI(timer.getNextTimeActivation())).concat(
                    String.valueOfC("last time =").concatI(lastTime)).concat(
                    String.valueOfC("new time =").concatI(newTime)));
                }
                try {
                    timer.onTimer();
                } catch (Exception e) {
                    ((ConsoleDebug)SystemEnvironment.getInstance().getDebug()).logEx(e);
                }
            }
            if (!unusedTimersAssist.isEmpty()) {
                tickTimers.removeAll(unusedTimersAssist);
                unusedTimers.addAll(unusedTimersAssist);
                unusedTimersAssist.clear();
            }

            updateStatus = TIMERS_UPDATE_STATUS;
            while (!timers.isEmpty()) {
                Timer timer = (Timer)timers.get(timers.size() - 1);
                int timeActivation = timer.getTimeActivate();
                if (timeActivation <= newTime) {
                    if (STACK_TRACE) {
                        SystemEnvironment.getInstance().getDebug().trace(String.valueOfC("TimerManager.update(): timer.onTimer").concat(
                        String.valueOfC("timer id=").concatI(timer.getId())).concat(
                        String.valueOfC("interval=").concatI(timer.getInterval())).concat(
                        String.valueOfC("timer type=").concatI(timer.getTimerType())).concat(
                        String.valueOfC("time activation=").concatI(timer.getTimeActivate())).concat(
                        String.valueOfC("next time activation=").concatI(timer.getNextTimeActivation())).concat(
                        String.valueOfC("last time =").concatI(lastTime)).concat(
                        String.valueOfC("new time =").concatI(newTime)));
                    }
                    //todo: А если в обработчике таймера что-то сделать с этим таймером (остановить, запустить повторно, изменить)?
                    try {
                        timer.onTimer();
                    } catch (Exception e) {
                        ((ConsoleDebug)SystemEnvironment.getInstance().getDebug()).logEx(e);
                    }

                    if (!timer.isKilled()) {
                        if (timer.isPeriodicTimer()) {
                            int nextTimeActivation = timer.getNextTimeActivation();
                            if (DEBUG) {
                                if (nextTimeActivation != -1 && timeActivation >= nextTimeActivation) {
                                    SystemEnvironment.getInstance().getDebug().trace(String.valueOfC("TimerManager.update(): timeActivation >= nextTimeActivation ").concat(
                                    String.valueOfC("timer id=").concatI(timer.getId())).concat(
                                    String.valueOfC("interval=").concatI(timer.getInterval())).concat(
                                    String.valueOfC("timer type=").concatI(timer.getTimerType())).concat(
                                    String.valueOfC("time activation=").concatI(timer.getTimeActivate())).concat(
                                    String.valueOfC("next time activation=").concatI(timer.getNextTimeActivation())).concat(
                                    String.valueOfC("last time =").concatI(lastTime)).concat(
                                    String.valueOfC("new time =").concatI(newTime)));
                                }
                                if (nextTimeActivation != -1 && lastTime >= nextTimeActivation) {
                                    SystemEnvironment.getInstance().getDebug().trace(String.valueOfC("TimerManager.update(): nextTimeActivation <= lastTime ").concat(
                                    String.valueOfC("timer id=").concatI(timer.getId())).concat(
                                    String.valueOfC("interval=").concatI(timer.getInterval())).concat(
                                    String.valueOfC("timer type=").concatI(timer.getTimerType())).concat(
                                    String.valueOfC("time activation=").concatI(timer.getTimeActivate())).concat(
                                    String.valueOfC("next time activation=").concatI(timer.getNextTimeActivation())).concat(
                                    String.valueOfC("last time =").concatI(lastTime)).concat(
                                    String.valueOfC("new time =").concatI(newTime)));
                                }
                            }
                            // Таймер требует повторной активации, поэтому он должен быть обновлен и перемещен в списке.
                            movingLastTimer(nextTimeActivation);
                            timer.setTimeActivate(nextTimeActivation);
                        } else {
                            releaseTimer(timer);
                        }
                    }
                } else {
                    // Если очередной таймер не сработал, значит и другие, более поздние, не должны сработать.
                    break;
                }
            }
            if (!unusedTimersAssist.isEmpty()) {
//                timers.removeAll(unusedTimersAssist); //todo: точно не нужно?
                unusedTimers.addAll(unusedTimersAssist);
                unusedTimersAssist.clear();
            }

            lastTime = newTime;

//            // Нормализация времени таймеров
//            if (lastTime >= TIMER_OPTIMIZE_SUBTRACTOR) {
//                // Данный код выполняется не чаще чем один раз в 10 дней
//                lastTime -= TIMER_OPTIMIZE_SUBTRACTOR;
//                baseTime += TIMER_OPTIMIZE_SUBTRACTOR;
//                for (int i = 0; i < timers.size(); i++) {
//                    Timer timer = (Timer)timers.get(i);
//                    timer.optimize(TIMER_OPTIMIZE_SUBTRACTOR);
//                }
//            }
        } finally {
            updateStatus = UPDATE_STATUS_NONE;
        }
    }
}