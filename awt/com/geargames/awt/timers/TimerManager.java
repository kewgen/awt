package com.geargames.awt.timers;

import com.geargames.Debug;
import com.geargames.awt.AWTObject;
import com.geargames.common.util.ArrayList;
import com.geargames.common.util.HashMap;
import com.geargames.common.String;
import com.geargames.env.ConsoleEnvironment;

import java.util.Random;

/**
 * User: abarakov
 * Date: 23.02.13 8:54
 */
public class TimerManager {

    private final static boolean DEBUG = true;
    private final static boolean STACK_TRACE = true;

    public final static int DINAMIC_TIMER_ID_MIN  = 20001;
    public final static int DINAMIC_TIMER_ID_MAX = Integer.MAX_VALUE;

    public final static int TIME_ONESHOT  = 0; // Таймер должен сработать спустя interval миллисекунд
    public final static int TIME_PERIODIC = 1; // Таймер должен срабатывать каждые interval миллисекунд

    private final static int TIMER_OPTIMIZE_SUBTRACTOR = 864000000; // 10 дней в миллисекундах = 10*24*60*60*1000

//    private static TimerManager instance;
    private static Random random = null; //todo: Думается, что рандом-генератор должен быть один на всю программу

    // ----- Функционал получения точного времени в тиках процессора ---------------------------------------------------

    // Время старта приложения, с него начинаются все отсчеты таймеров (в миллисекундах)
    // Такой baseTime нужен в случае, если компьютер/устройство работает бесперерывно уже более 10 суток
    private static long baseTime = ConsoleEnvironment.getInstance().nanoTime() / 1000000;

    /**
     * Вернуть время в миллисекундах
     */
    //todo: Перенести метод millisTime() в класс Environment
    public static int millisTime() {
        long time = ConsoleEnvironment.getInstance().nanoTime() / 1000000; // Перевод из наносекунд в миллисекунды.
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

    private static int       lastTime           = millisTime(); // Время последнего обновления таймеров (в миллисекундах)
    private static boolean   isUpdateWorked     = false;        // True, если в настоящий момент выполняется метод update().

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
    // generateTimerId
    //todo: тип id-шника - int или short?
    private static int getNewTimerId() {
        // Если выбрать тип short как timerId, то при большом количестве созданных таймеров (>15000), данный метод может
        // начать тормозить.
        int id;
        do {
            id = random.nextInt(DINAMIC_TIMER_ID_MAX - DINAMIC_TIMER_ID_MIN + 1) + DINAMIC_TIMER_ID_MAX;
        } while (timerIds.containsKey(id));
        return id;
    }

    // ----- Создание и удаление таймеров ------------------------------------------------------------------------------

    /**
     * Создать таймер.
     * @param timerId - выбирайте id из следующих соображений:
     *                 отрицательные id - для всех системных таймеров (таймеров общего кода).
     *                 0..20000 - для клиентских таймеров с константным id;
     *                 20001.. - для клиентских таймеров с динамическим id, который генерирует сам менеджер;
     * @param interval
     * @param flag   - определяет, будет ли таймер многократным или однократным. Должно принимать одно из значений:
     *                 TIME_ONESHOT или TIME_PERIODIC
     * @param callBackElement
     * @return
     */
    // createTimer
    private static int setTimer(int timerId, int interval, AWTObject callBackElement, int flag) {
        if (STACK_TRACE) {
            Debug.trace(java.lang.String.format(
                    "TimerManager.setTimer(timerId=%d; interval=%d; flag=%d; class of element='%s')",
                    timerId, interval, flag, callBackElement.getClass().getName()
            ));
        }
        killTimer(timerId);
        Timer timer;
        if (!unusedTimers.isEmpty()) {
            timer = (Timer)unusedTimers.remove(unusedTimers.size() - 1);
        } else {
            timer = new Timer();
        }

        if (DEBUG && initiateTimers.contains(timer)) {
            Debug.assertMsg(String.valueOfC(java.lang.String.format(
                    "TimerManager.setTimer(): Put a timer that is already in initiateTimers (id=%d; interval=%d; " +
                    "isPeriodic=%s; class of element='%s')",
                    timerId, interval, Boolean.toString(timer.isPeriodic()), callBackElement.getClass().getName()
            )), false);
        }

        timer.init(timerId, interval, callBackElement, flag == TIME_PERIODIC);
        initiateTimers.add(timer);
        //todo: Возможно таймер следует сразу же вставлять в список timers
        // Ситуация: Производится update (была большая задержка, потому elapsedTime большой), во время апдейта одного из
        // таймеров, в его обработчике создается еще один таймер, время срабатывания которого меньше чем newTime, т.е.
        // новый таймер должен сработать во время одного и того же update.
        // Почему это не нужно:
        // 1. Все фактические обработчики срабатываний таймеров производятся во вне метода update, в eventProcess

        if (DEBUG && (timerIds.containsKey(timerId) || timerIds.containsValue(timer))) {
            Debug.assertMsg(String.valueOfC(java.lang.String.format(
                    "TimerManager.setTimer(): Put a timer that is already in timerIds (id=%d; interval=%d; isPeriodic=%s; " +
                    "class of element='%s')",
                    timerId, interval, Boolean.toString(timer.isPeriodic()), callBackElement.getClass().getName()
                    )), false);
        }

        timerIds.put(timerId, timer);
        return timerId;
    }

    /**
     * Создать однократный таймер.
     * @param timerId
     * @param interval
     * @param callBackElement
     * @return
     */
    public static int setSingleTimer(int timerId, int interval, AWTObject callBackElement) {
        return setTimer(timerId, interval, callBackElement, TIME_ONESHOT);
    }

    /**
     * Создать однократный таймер, причем, id таймеру будет автоматически присвоен самим менеджером.
     * @param interval
     * @return
     */
    public static int setSingleTimer(int interval, AWTObject callBackElement) {
        return setTimer(getNewTimerId(), interval, callBackElement, TIME_ONESHOT);
    }

    /**
     * Создать многократный таймер.
     * @param timerId
     * @param interval
     * @param callBackElement
     * @return
     */
    // setMultipleTimer
    public static int setPeriodicTimer(int timerId, int interval, AWTObject callBackElement) {
        return setTimer(timerId, interval, callBackElement, TIME_PERIODIC);
    }

    /**
     * Создать многократный таймер, причем, id таймеру будет автоматически присвоен самим менеджером.
     * @param interval
     * @return
     */
    public static int setPeriodicTimer(int interval, AWTObject callBackElement) {
        return setTimer(getNewTimerId(), interval, callBackElement, TIME_PERIODIC);
    }

    /**
     * Создать многократный tick-таймер, т.е. таймер срабатывающий в каждой итерации цикла mainLoop.
     * @param timerId
     * @param callBackElement
     * @return
     */
    // setMultipleTimer
    public static int setTickTimer(int timerId, AWTObject callBackElement) {
        return setTimer(timerId, 0, callBackElement, TIME_PERIODIC);
    }

    /**
     * Создать многократный tick-таймер, т.е. таймер срабатывающий в каждой итерации цикла mainLoop, причем, id таймеру
     * будет автоматически присвоен самим менеджером.
     * @return
     */
    public static int setTickTimer(AWTObject callBackElement) {
        return setTimer(getNewTimerId(), 0, callBackElement, TIME_PERIODIC);
    }

    /**
     * Удалить таймер
     * @param timerId
     */
    // deleteTimer, removeTimer, releaseTimer
    public static void killTimer(int timerId) {
        if (STACK_TRACE) {
            Debug.trace("TimerManager.killTimer(timerId=" + timerId + ")");
        }
        Timer timer = findTimer(timerId);
        if (timer != null) {
            releaseTimer(timer);
        }
    }

    public static void clearTimers() {
        if (isUpdateWorked) {
            unusedTimersAssist.addAll(timers);
            unusedTimersAssist.addAll(tickTimers);
            unusedTimersAssist.addAll(initiateTimers);
        } else {
            unusedTimers.addAll(timers);
            unusedTimers.addAll(tickTimers);
            unusedTimers.addAll(initiateTimers);
        }
        if (!timers.isEmpty()) {
            if (isUpdateWorked) {
                Timer timer = (Timer)timers.get(timers.size() - 1);
                timer.setInterval(0); // Это для того, чтобы таймер не пересоздался в методе update
            }
            timers.clear();
        }
        tickTimers.clear();
        timerIds.clear();
        initiateTimers.clear();
    }

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
            Debug.trace(java.lang.String.format(
                    "TimerManager.releaseTimer(timerId=%d; interval=%d; isPeriodic=%s; class of element='%s')",
                    timer.getId(), timer.getInterval(), Boolean.toString(timer.isPeriodic()), timer.getCallBackElement().getClass().getName()
            ));
        }
        timer.setInterval(0); // Это для того, чтобы таймер не пересоздался в методе update
        //todo: таймер не может одновременно находиться сразу в трех списках timers, tickTimers и initiateTimers
        timers.remove(timer);
        tickTimers.remove(timer);
        timerIds.remove(timer.getId());
        initiateTimers.remove(timer);
        if (isUpdateWorked) {
            unusedTimersAssist.add(timer);
        } else {
            unusedTimers.add(timer);
        }
    }

//    public static void stopTimer(Timer timer) {
//        //todo: Безопасно удалить таймер изо всех списков и добавить в список disabledTimers.
//        // Учесть, что останавливаемый таймер, может, в настоящий момент, перемещаться по списку в методе update()
//    }
//
//    /**
//     * Добавить таймер timer в список на инициализацию.
//     * @param timer
//     */
//    public static void timerRequiredInitiate(Timer timer) {
//        if (/*timer.getEnabled() &&*/ !initiateTimers.contains(timer)) {
//            initiateTimers.add(timer);
//        }
//        //todo: А что, если таймер уже есть в списке timers?
//    }

    private static void movingLastTimer(int timeActivation) {
        // Данный код одновременно реализует две функциональности:
        // 1. Поиск места в списке, куда вставить элемент, с сохранением сортированности списка;
        // 2. Перемещение последнего элемента списка (многократного таймера) в новую позицию
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
                Debug.trace(java.lang.String.format(
                        "TimerManager.update(initiateTimers.size()=%d; timers.size()=%d; lastTime=%d; newTime=%d; deltaTime=%d)",
                        initiateTimers.size(), timers.size(), lastTime, newTime, newTime - lastTime
                ));
            }
        }
        if (isUpdateWorked) {
            //todo: это error
            Debug.warning(String.valueOfC("TimerManager.update(): Method was called at the time, when it is already running"));
        }
        isUpdateWorked = true;
        try {
            // Инициализация таймеров
            for (int i = 0; i < initiateTimers.size(); i++) {
                Timer timer = (Timer)initiateTimers.get(i);
                if (timer.getInterval() == 0) {
                    tickTimers.add(timer);
                } else {
                    timers.add(timer);
                    movingLastTimer(timer.getTimeActivate());
                }
                timer.initiate();
            }
            initiateTimers.clear();

            for (int i = 0; i < tickTimers.size(); i++) {
                Timer timer = (Timer)tickTimers.get(i);
                if (STACK_TRACE) {
                    Debug.trace(java.lang.String.format(
                            "TimerManager.update(): timer.onTickTimer(" +
                                    "id=%d; interval=%d; isPeriodic=%s; " +
                                    "timeActivation=%d; nextTimeActivation=%d; lastTime=%d; newTime=%d; " +
                                    "class of element='%s')",
                            timer.getId(), timer.getInterval(), Boolean.toString(timer.isPeriodic()),
                            timer.getTimeActivate(), timer.getNextTimeActivation(), lastTime, newTime,
                            timer.getCallBackElement().getClass().getName()
                    ));
                }
                timer.onTimer();
            }

            while (!timers.isEmpty()) {
                Timer timer = (Timer)timers.get(timers.size() - 1);
                int timeActivation = timer.getTimeActivate();
                if (timeActivation <= newTime) {
                    if (STACK_TRACE) {
                        Debug.trace(java.lang.String.format(
                                "TimerManager.update(): timer.onTimer(" +
                                "id=%d; interval=%d; isPeriodic=%s; " +
                                "timeActivation=%d; nextTimeActivation=%d; lastTime=%d; newTime=%d; " +
                                "class of element='%s')",
                                timer.getId(), timer.getInterval(), Boolean.toString(timer.isPeriodic()),
                                timeActivation, timer.getNextTimeActivation(), lastTime, newTime,
                                timer.getCallBackElement().getClass().getName()
                        ));
                    }
                    //todo: А если в обработчике таймера что-то сделать с этим таймером (остановить, запустить повторно, изменить)?
                    timer.onTimer();

                    int nextTimeActivation = timer.getNextTimeActivation();
                    if (DEBUG) {
                        if (nextTimeActivation != -1 && timeActivation >= nextTimeActivation) {
                            Debug.assertMsg(String.valueOfC(java.lang.String.format(
                                    "TimerManager.update(): timeActivation >= nextTimeActivation " +
                                            "(id=%d; interval=%d; isPeriodic=%s; " +
                                            "timeActivation=%d; nextTimeActivation=%d; lastTime=%d; newTime=%d; " +
                                            "class of element='%s')",
                                    timer.getId(), timer.getInterval(), Boolean.toString(timer.isPeriodic()),
                                    timeActivation, nextTimeActivation, lastTime, newTime,
                                    timer.getCallBackElement().getClass().getName()
                            )), false);
                        }
                        if (nextTimeActivation != -1 && lastTime >= nextTimeActivation) {
                            Debug.assertMsg(String.valueOfC(java.lang.String.format(
                                    "TimerManager.update(): nextTimeActivation <= lastTime " +
                                            "(id=%d; interval=%d; isPeriodic=%s; " +
                                            "timeActivation=%d; nextTimeActivation=%d; lastTime=%d; newTime=%d; " +
                                            "class of element='%s')",
                                    timer.getId(), timer.getInterval(), Boolean.toString(timer.isPeriodic()),
                                    timeActivation, nextTimeActivation, lastTime, newTime,
                                    timer.getCallBackElement().getClass().getName()
                            )), false);
                        }
                    }

                    if (nextTimeActivation >= 0) {
                        // Таймер требует повторной активации, поэтому он должен быть обновлен и перемещен в списке.
                        movingLastTimer(nextTimeActivation);
                        timer.setTimeActivate(nextTimeActivation);
                    } else {
                        releaseTimer(timer);
                    }
                } else {
                    // Если очередной таймер не сработал, значит и другие, более поздние, не должны сработать.
                    break;
                }
            }
            lastTime = newTime;

            unusedTimers.addAll(unusedTimersAssist);
            unusedTimersAssist.clear();

            // Оптимизация таймеров
            if (lastTime >= TIMER_OPTIMIZE_SUBTRACTOR) {
                // Данный код выполняется не чаще чем один раз в 10 дней
                lastTime -= TIMER_OPTIMIZE_SUBTRACTOR;
                baseTime += TIMER_OPTIMIZE_SUBTRACTOR;
                for (int i = 0; i < timers.size(); i++) {
                    Timer timer = (Timer)timers.get(i);
                    timer.optimize(TIMER_OPTIMIZE_SUBTRACTOR);
                }
            }
        } finally {
            isUpdateWorked = false;
        }
    }
}