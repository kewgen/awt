package com.geargames.awt;

import com.geargames.ConsoleDebug;
import com.geargames.common.Event;
import com.geargames.common.env.SystemEnvironment;

import java.util.Vector;

/**
 * User: abarakov
 * Date: 26.02.13 15:56
 */
public abstract class Application {

    private static Application instance; // appInstance

    // Конструктор
    protected Application() {
        instance = this;
    }

    public static Application getInstance() {
        return instance;
    }

    // ----- Main loop -------------------------------------------------------------------------------------------------

    public abstract void mainLoop();

    // ----- Events control --------------------------------------------------------------------------------------------

    private Vector msgQueue = new Vector(64);

    public void eventAdd(int eventid, int param, Object data) { //todo: Object -> AWTObject
        eventAdd(eventid, param, data, 0, 0);
    }

    public void eventAdd(int eventid, int param, Object data, int x, int y) {
        boolean normalSize = msgQueue.size() < 64;
        if (!normalSize) {
            SystemEnvironment.getInstance().getDebug().warning(com.geargames.common.String.valueOfC("Queue length exceed 64 events (length=" + msgQueue.size() + ")"));
        }
        Event event = new Event(eventid, param, data, x, y);
        msgQueue.addElement(event);
    }

    protected void eventProcess() {
        while (!msgQueue.isEmpty()) {
            try {
                Event event = (Event) msgQueue.remove(0); // Перед вызовом события нужно его убрать из очереди на случай эксепта
                onEvent(event);
                event = null;//ObjC
            } catch (Exception e) {
                ((ConsoleDebug)SystemEnvironment.getInstance().getDebug()).logEx(e);
            }
        }
//        if (Application.isTimer(Manager.TIMERID_KEYDELAY) && !Application.isTimer(Manager.TIMERID_KEYREPEAT))//TODO сделать один интервал на все фпс
//            eventAdd(Event.EVENT_KEY_REPEATED, Manager.getInstance().getPressedKey(), null);
    }

    // ----- Message handlers ------------------------------------------------------------------------------------------

    /**
     * Выполнение всех манипуляций на один игровой тик
     */
    protected abstract void onEvent(Event event);
}