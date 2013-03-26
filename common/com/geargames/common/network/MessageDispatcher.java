package com.geargames.common.network;

import com.geargames.common.timers.TimerListener;
import com.geargames.common.timers.TimerManager;
import com.geargames.common.util.ArrayList;
import com.geargames.common.util.ArrayObject;

/**
 * User: mkutuzov
 * Date: 25.03.13
 * Класс описывает регистрацию слушателей сообщений сети и оповещает их о приходе сообщений.
 * После доставки сообщений из Network подписчикам происходит их удаление из Network.
 * Рассылка происходит с наименьшим требуемым подписчиками периодом(каждый подписчик возвращает этот желаемый параметр).
 */
public class MessageDispatcher implements TimerListener {
    private ArrayList listeners;
    private ArrayObject listenersByType;
    private Network network;
    private int id;
    private int period;

    /**
     * Класс создаётся по экземпляру Network из которого будут вычитываться сообщения и количеству типов сообщений
     * обслуживаемых приложением.
     *
     * @param network
     * @param size
     */
    public MessageDispatcher(Network network, int size) {
        this.network = network;
        this.listenersByType = new ArrayObject(size);
        this.listeners = new ArrayList();
    }

    /**
     * Подписаться на получение сообщений из Network по интерфейсу DataMessageListener.
     *
     * @param listener
     */
    public void register(DataMessageListener listener) {
        if (listeners.isEmpty()) {
            period = listener.getInterval();
            id = TimerManager.setPeriodicTimer(period, this);
        } else if (period > listener.getInterval()) {
            period = listener.getInterval();
            TimerManager.setPeriodicTimer(id, period, this);
        }

        listeners.add(listener);
        short[] types = listener.getTypes();
        int length = types.length;
        for (int i = 0; i < length; i++) {
            int type = types[i];
            ArrayList typeListeners = (ArrayList) listenersByType.get(type);
            if (typeListeners == null) {
                typeListeners = new ArrayList();
                listenersByType.set(type, typeListeners);
            }
            typeListeners.add(listener);
        }
    }

    /**
     * Отписаться на получение сообщений из Network по интерфейсу DataMessageListener.
     *
     * @param listener
     */
    public void unregister(DataMessageListener listener) {
        int size = listenersByType.length();
        for (int i = 0; i < size; i++) {
            ((ArrayList) listenersByType.get(i)).remove(listener);
        }
        listeners.remove(listener);

        if (listener.getInterval() == period) {
            size = listeners.size();
            if (size > 0) {
                period = ((DataMessageListener)listeners.get(0)).getInterval();
                for (int i = 1; i < size; i++) {
                    int time = ((DataMessageListener) listeners.get(i)).getInterval();
                    if (period > time) {
                        period = time;
                    }
                }
                TimerManager.setPeriodicTimer(id, period, this);
            } else {
                TimerManager.killTimer(id);
            }
        }
    }

    @Override
    public void onTimer(int timerId) {
        DataMessage[] messages = network.getAsynchronousDataMessages();
        for (int i = 0; i < messages.length; i++) {
            DataMessage message = messages[i];
            ArrayList listeners = (ArrayList) listenersByType.get(message.getMessageType());
            int size = listeners.size();
            for (int j = 0; j < size; j++) {
                ((DataMessageListener) listeners.get(j)).onReceive(message);
            }
        }
    }
}
