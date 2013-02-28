package com.geargames.awt;

import com.geargames.Debug;
import com.geargames.common.Event;
import com.geargames.common.Render;

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

    public static Application getInstance(){
        return instance;
    }

    public abstract Render getRender();

    // ----- Main loop -------------------------------------------------------------------------------------------------

    public abstract void mainLoop();

    // ----- Events control --------------------------------------------------------------------------------------------

    // Очередь сообщений, дублёр предназначен для исключения добавления нового события в момент обработки списка событий
    private Vector msgQueue = new Vector(64);

    public void eventAdd(int eventid, int param, Object data) { //todo: Object -> AWTObject
        eventAdd(eventid, param, data, 0, 0);
    }

    public void eventAdd(int eventid, int param, Object data, int x, int y) {
        boolean normalSize = msgQueue.size() < 64;
        if (!normalSize) {
            Debug.warning(com.geargames.common.String.valueOfC("Queue length exceed 64 events (length=" + msgQueue.size() + ")"));
        }
        Event event = new Event(eventid, param, data, x, y);
        msgQueue.addElement(event);
    }

    protected void eventProcess() {
        try {
            //передача указателей на хранилища событий для разнесения обработки и добавления событий
            synchronized (msgQueue) {

                while (!msgQueue.isEmpty()) {
                    Event event = (Event) msgQueue.firstElement();
                    msgQueue.remove(event); //перед вызовом события нужно его убрать из очереди на случай эксепта
                    onEvent(event);
                    event = null;//ObjC
                }

            }
//            if (Application.isTimer(Manager.TIMERID_KEYDELAY) && !Application.isTimer(Manager.TIMERID_KEYREPEAT))//TODO сделать один интервал на все фпс
//                eventAdd(Event.EVENT_KEY_REPEATED, Manager.getInstance().getPressedKey(), null);
        } catch (Exception e) {
            Debug.logEx(e);
        }
    }

    // ----- Message handlers ------------------------------------------------------------------------------------------

    /**
     * Выполнение всех манипуляций на один игровой тик
     */
    protected abstract void onEvent(Event event);
}