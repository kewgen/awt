package com.geargames.common.network;

import com.geargames.common.logging.Debug;
import com.geargames.common.serialization.ClientDeSerializedMessage;
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
public abstract class MessageDispatcher implements TimerListener {
    private ArrayList listeners;
    private ArrayObject listenersByType;
    private Network network;
    private int timerId;
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
        this.timerId = TimerManager.NULL_TIMER;
    }

    /**
     * Подписаться на получение сообщений из Network по интерфейсу DataMessageListener.
     *
     * @param listener
     */
    public void register(DataMessageListener listener) {
        short[] types = listener.getTypes();
        if (types != null) {
            if (timerId == TimerManager.NULL_TIMER) {
                period = listener.getInterval();
                timerId = TimerManager.setPeriodicTimer(period, this);
            } else if (period > listener.getInterval()) {
                period = listener.getInterval();
                TimerManager.setPeriodicTimer(timerId, period, this);
            }

            listeners.add(listener);

            int length = types.length;
            for (int i = 0; i < length; i++) {
                int type = types[i];
                ArrayList typeListeners = (ArrayList) listenersByType.get(type);
                if (typeListeners == null) {
                    typeListeners = new ArrayList(); //todo: использовать Set
                    listenersByType.set(type, typeListeners);
                }
                typeListeners.add(listener);
            }
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
            ArrayList typeListeners = (ArrayList) listenersByType.get(i);
            if (typeListeners != null) {
                typeListeners.remove(listener);
            }
        }
        listeners.remove(listener);

        if (listener.getInterval() == period) {
            size = listeners.size();
            if (size > 0) {
                period = ((DataMessageListener) listeners.get(0)).getInterval();
                for (int i = 1; i < size; i++) {
                    int otherPeriod = ((DataMessageListener) listeners.get(i)).getInterval();
                    if (period > otherPeriod) {
                        period = otherPeriod;
                    }
                }
                TimerManager.setPeriodicTimer(timerId, period, this);
            } else {
                TimerManager.killTimer(timerId);
                timerId = TimerManager.NULL_TIMER;
            }
        }
    }

    /**
     * Всем подписчика отсылается один экземпляр класса DataMessage, поэтому экземпляры DataMessage
     * стоит использовать только для чтения.
     * @param timerId - идентификатор сработавшего таймера, который вызвал данный метод.
     */
    public void onTimer(int timerId) {
        // Должно быть this.timerId == timerId
        DataMessage[] messages = network.getAsynchronousDataMessages();
        for (int i = 0; i < messages.length; i++) {
            DataMessage message = messages[i];
            ArrayList listeners = (ArrayList) listenersByType.get(message.getMessageType());
            if (listeners == null || listeners.size() == 0) {
                unhandledMessageHandler(message);
            } else {
                try {
                    ClientDeSerializedMessage deserializedMessage = getMessage(message);
                    if (deserializedMessage != null) {
                        deserializedMessage.deSerialize();
                        short messageType = message.getMessageType();
                        for (int j = 0; j < listeners.size(); j++) {
                            ((DataMessageListener) listeners.get(j)).onReceive(deserializedMessage, messageType);
                        }
                    } else {
                        //todo: отреагировать на эту ситуацию
                    }
                } catch (Exception e) {
                    //todo: Отправить обработку исключения подписчику на ошибки
                    Debug.error("Could not deserialize answer", e);
                }
            }
        }
    }

    protected abstract ClientDeSerializedMessage getMessage(DataMessage dataMessage);

    protected void unhandledMessageHandler(DataMessage message) {
        //todo: Оповестить подписчиков на ошибки о получении неизвестного сообщения.
    }

}
