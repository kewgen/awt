package com.geargames.awt.timers;

import com.geargames.Debug;
import com.geargames.awt.AWTObject;
import com.geargames.common.util.ArrayList;
import com.geargames.common.util.HashMap;
import com.geargames.common.String;
import com.geargames.env.ConsoleEnvironment;

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

    private final static boolean DEBUG       = true;
    private final static boolean STACK_TRACE = true;

    public final static int DINAMIC_TIMER_ID_MIN = 20001;
    public final static int DINAMIC_TIMER_ID_MAX = Integer.MAX_VALUE;

//    private final static int TIMER_OPTIMIZE_SUBTRACTOR = 864000000; // 10 дней в миллисекундах = 10*24*60*60*1000

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

    private static int     lastTime       = millisTime(); // Время последнего обновления таймеров (в миллисекундах)
    private static boolean isUpdateWorked = false;        // True, если в настоящий момент выполняется метод update().
    private static int     nextDinamicId  = DINAMIC_TIMER_ID_MIN; // id, который будет присвоен следующему таймеру.

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
        // Если выбрать тип short как timerId, то при большом количестве созданных таймеров (>5000), id которым был
        // присвоен самим менеджером, данный метод может начать тормозить.
//        int id;
//        do {
//          id = random.nextInt(DINAMIC_TIMER_ID_MAX - DINAMIC_TIMER_ID_MIN + 1) + DINAMIC_TIMER_ID_MAX;
//        } while (timerIds.containsKey(id));
//        return id;

        while (timerIds.containsKey(nextDinamicId)) {
            nextDinamicId++;
        }
        int id = nextDinamicId;
        nextDinamicId++;
        if (id < 0 || id > DINAMIC_TIMER_ID_MAX) {
            Debug.warning(String.valueOfC("Id out of range (id=" + nextDinamicId + ")"));
        }
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
     * @param timerType - определяет типа таймера, будет ли таймер однократным, многократным или tick-таймером.
     *                  Должно принимать одно из значений: Timer.SINGLE_TIMER_TYPE, Timer.PERIODIC_TIMER_TYPE или
     *                  Timer.TICK_TIMER_TYPE
     * @param callBackElement
     * @return
     */
    // createTimer
    private static int setTimer(int timerId, int interval, byte timerType, AWTObject callBackElement) {
        if (STACK_TRACE) {
            Debug.trace(java.lang.String.format(
                    "TimerManager.setTimer(timerId=%d; interval=%d; timerType=%d; class of element='%s')",
                    timerId, interval, timerType, callBackElement.getClass().getName()
            ));
        }
        if (DEBUG && timerIds.containsKey(timerId)) {
            Debug.assertMsg(String.valueOfC(java.lang.String.format(
                    "TimerManager.setTimer(): Put timerId that is already in timerIds (" +
                            "id=%d; interval=%d; timerType=%d; class of element='%s')",
                    timerId, interval, timerType, callBackElement.getClass().getName()
            )), false);
        }
        killTimer(timerId);
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
    public static int setSingleTimer(int timerId, int interval, AWTObject callBackElement) {
        return setTimer(timerId, interval, Timer.SINGLE_TIMER_TYPE, callBackElement);
    }

    /**
     * Создать однократный таймер, причем, id таймеру будет автоматически присвоен самим менеджером.
     * @param interval
     * @return
     */
    public static int setSingleTimer(int interval, AWTObject callBackElement) {
        return setTimer(getNewTimerId(), interval, Timer.SINGLE_TIMER_TYPE, callBackElement);
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
        return setTimer(timerId, interval, Timer.PERIODIC_TIMER_TYPE, callBackElement);
    }

    /**
     * Создать многократный таймер, причем, id таймеру будет автоматически присвоен самим менеджером.
     * @param interval
     * @return
     */
    public static int setPeriodicTimer(int interval, AWTObject callBackElement) {
        return setTimer(getNewTimerId(), interval, Timer.PERIODIC_TIMER_TYPE, callBackElement);
    }

    /**
     * Создать многократный tick-таймер, т.е. таймер срабатывающий в каждой итерации цикла mainLoop.
     * @param timerId
     * @param callBackElement
     * @return
     */
    // setMultipleTimer
    public static int setTickTimer(int timerId, AWTObject callBackElement) {
        return setTimer(timerId, 0, Timer.TICK_TIMER_TYPE, callBackElement);
    }

    /**
     * Создать многократный tick-таймер, т.е. таймер срабатывающий в каждой итерации цикла mainLoop, причем, id таймеру
     * будет автоматически присвоен самим менеджером.
     * @return
     */
    public static int setTickTimer(AWTObject callBackElement) {
        return setTimer(getNewTimerId(), 0, Timer.TICK_TIMER_TYPE, callBackElement);
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
        initiateTimers.clear();
        timerIds.clear();
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
                    "TimerManager.releaseTimer(timerId=%d; interval=%d; timerType=%d; class of element='%s')",
                    timer.getId(), timer.getInterval(), timer.getTimerType(), timer.getCallBackElement().getClass().getName()
            ));
        }
        timer.setInterval(0); // Это для того, чтобы таймер не пересоздался в методе update

        if (timer.isNeedInitiate()) {
            initiateTimers.remove(timer);
        } else {
            switch (timer.getTimerType()) {
                case Timer.SINGLE_TIMER_TYPE:
                case Timer.PERIODIC_TIMER_TYPE:
                    timers.remove(timers);
                    break;
                case Timer.TICK_TIMER_TYPE:
                    tickTimers.remove(timers);
                    break;
            }
        }
        timerIds.remove(timer.getId());

        if (DEBUG) {
            // Убеждаемся, что таймера нет ни в одном списке
            if (timers.contains(timer)) {
                Debug.assertMsg(String.valueOfC(java.lang.String.format(
                        "TimerManager.releaseTimer(): Timer is still in the list 'timers' (id=%d; interval=%d; " +
                        "timerType=%d; class of element='%s')",
                        timer.getId(), timer.getInterval(), timer.getTimerType(), timer.getCallBackElement().getClass().getName()
                )), false);
            }
            if (tickTimers.contains(timer)) {
                Debug.assertMsg(String.valueOfC(java.lang.String.format(
                        "TimerManager.releaseTimer(): Timer is still in the list 'tickTimers' (id=%d; interval=%d; " +
                        "timerType=%d; class of element='%s')",
                        timer.getId(), timer.getInterval(), timer.getTimerType(), timer.getCallBackElement().getClass().getName()
                )), false);
            }
            if (initiateTimers.contains(timer)) {
                Debug.assertMsg(String.valueOfC(java.lang.String.format(
                        "TimerManager.releaseTimer(): Timer is still in the list 'initiateTimers' (id=%d; interval=%d; " +
                        "timerType=%d; class of element='%s')",
                        timer.getId(), timer.getInterval(), timer.getTimerType(), timer.getCallBackElement().getClass().getName()
                )), false);
            }
            if (timerIds.containsValue(timer)) {
                Debug.assertMsg(String.valueOfC(java.lang.String.format(
                        "TimerManager.releaseTimer(): Timer is still in the list 'timerIds' (id=%d; interval=%d; " +
                                "timerType=%d; class of element='%s')",
                        timer.getId(), timer.getInterval(), timer.getTimerType(), timer.getCallBackElement().getClass().getName()
                )), false);
            }
        }

        if (isUpdateWorked) {
            unusedTimersAssist.add(timer);
        } else {
            unusedTimers.add(timer);
        }
    }

//    /**
//     * Добавить таймер timer в список на инициализацию.
//     * @param timer
//     */
//    public static void timerRequiredInitiate(Timer timer) {
//        if (/*timer.getEnabled() &&*/ !initiateTimers.contains(timer)) {
//            initiateTimers.add(timer);
//        }
//        //to do: А что, если таймер уже есть в списке timers?
//    }

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
                if (timer.isTickTimer()) {
                    tickTimers.add(timer);
                } else {
                    timers.add(timer);
                    movingLastTimer(timer.getTimeActivate());
                }
                timer.initiate();
            }
            initiateTimers.clear();

            //todo: использовать типа списка LinkedList
            for (int i = 0; i < tickTimers.size(); i++) {
                //todo: список может измениться, таймеры могут быть удалены, перемещны, изменены
                Timer timer = (Timer)tickTimers.get(i);
                if (STACK_TRACE) {
                    Debug.trace(java.lang.String.format(
                            "TimerManager.update(): timer.onTickTimer(" +
                                    "id=%d; interval=%d; timerType=%d; " +
                                    "timeActivation=%d; nextTimeActivation=%d; lastTime=%d; newTime=%d; " +
                                    "class of element='%s')",
                            timer.getId(), timer.getInterval(), timer.getTimerType(),
                            timer.getTimeActivate(), timer.getNextTimeActivation(), lastTime, newTime,
                            timer.getCallBackElement().getClass().getName()
                    ));
                }
                try{
                    timer.onTimer();
                } catch (Exception e) {
                    Debug.logEx(e);
                }
            }

            while (!timers.isEmpty()) {
                Timer timer = (Timer)timers.get(timers.size() - 1);
                int timeActivation = timer.getTimeActivate();
                if (timeActivation <= newTime) {
                    if (STACK_TRACE) {
                        Debug.trace(java.lang.String.format(
                                "TimerManager.update(): timer.onTimer(" +
                                "id=%d; interval=%d; timerType=%d; " +
                                "timeActivation=%d; nextTimeActivation=%d; lastTime=%d; newTime=%d; " +
                                "class of element='%s')",
                                timer.getId(), timer.getInterval(), timer.getTimerType(),
                                timeActivation, timer.getNextTimeActivation(), lastTime, newTime,
                                timer.getCallBackElement().getClass().getName()
                        ));
                    }
                    //todo: А если в обработчике таймера что-то сделать с этим таймером (остановить, запустить повторно, изменить)?
                    try{
                        timer.onTimer();
                    } catch (Exception e) {
                        Debug.logEx(e);
                    }

                    int nextTimeActivation = timer.getNextTimeActivation();
                    if (DEBUG) {
                        if (nextTimeActivation != -1 && timeActivation >= nextTimeActivation) {
                            Debug.assertMsg(String.valueOfC(java.lang.String.format(
                                    "TimerManager.update(): timeActivation >= nextTimeActivation " +
                                            "(id=%d; interval=%d; timerType=%d; " +
                                            "timeActivation=%d; nextTimeActivation=%d; lastTime=%d; newTime=%d; " +
                                            "class of element='%s')",
                                    timer.getId(), timer.getInterval(), timer.getTimerType(),
                                    timeActivation, nextTimeActivation, lastTime, newTime,
                                    timer.getCallBackElement().getClass().getName()
                            )), false);
                        }
                        if (nextTimeActivation != -1 && lastTime >= nextTimeActivation) {
                            Debug.assertMsg(String.valueOfC(java.lang.String.format(
                                    "TimerManager.update(): nextTimeActivation <= lastTime " +
                                            "(id=%d; interval=%d; timerType=%d; " +
                                            "timeActivation=%d; nextTimeActivation=%d; lastTime=%d; newTime=%d; " +
                                            "class of element='%s')",
                                    timer.getId(), timer.getInterval(), timer.getTimerType(),
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
            isUpdateWorked = false;
        }
    }
}