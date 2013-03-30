package com.geargames.common;

import com.geargames.common.logging.Debug;

import java.util.Vector;

/**
 * User: abarakov
 * Date: 26.02.13 15:56
 */
public abstract class Application {

    public abstract void mainLoop();

    private Vector msgQueue = new Vector(64);

    public void eventAdd(int eventId, int param, Object data) {
        eventAdd(eventId, param, data, 0, 0);
    }

    public void eventAdd(int eventId, int param, Object data, int x, int y) {
        boolean normalSize = msgQueue.size() < 64;
        if (!normalSize) {
            Debug.warning("Queue length exceed 64 events (length="+msgQueue.size()+")");
        }
        Event event = new Event(eventId, param, data, x, y);
        msgQueue.addElement(event);
    }

    protected void eventProcess() {
        while (!msgQueue.isEmpty()) {
            Event event = (Event) msgQueue.remove(0);
            onEvent(event);
        }
    }

    /**
     * Выполнение всех манипуляций на один игровой тик
     */
    protected abstract void onEvent(Event event);
}