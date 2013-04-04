package com.geargames.common;

import com.geargames.common.logging.Debug;
import com.geargames.common.util.ArrayList;

/**
 * User: abarakov
 * Date: 26.02.13
 */
public abstract class Application {

    public abstract void mainLoop();

    private ArrayList msgQueue = new ArrayList(64);

    public void eventAdd(int eventId, int param, Object data) {
        eventAdd(eventId, param, data, 0, 0);
    }

    public void eventAdd(int eventId, int param, Object data, int x, int y) {
        boolean normalSize = msgQueue.size() < 64;
        if (!normalSize) {
            Debug.warning("Queue length exceed 64 events (length=" + msgQueue.size() + ")");
        }
        Event event = new Event(eventId, param, data, x, y);
        msgQueue.add(event);
    }

    protected void eventProcess() {
        while (!msgQueue.isEmpty()) {
            Event event = (Event) msgQueue.remove(0);
            try {
                onEvent(event);
            } catch (Exception e) {
                Debug.error("Event processing exception.", e);
            }
        }
    }

    /**
     * Выполнение всех манипуляций на один игровой тик
     */
    protected abstract void onEvent(Event event);

}