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
 */
public class MessageDispetcher implements TimerListener {
    private ArrayObject messageListeners;
    private Network network;
    private int id;

    /**
     * Класс создаётся по экземпляру Network из которого будут вычитываться сообщения и количеству типов сообщений
     * обслуживаемых приложением.
     *
     * @param network
     * @param size
     */
    public MessageDispetcher(Network network, int size) {
        this.network = network;
        this.messageListeners = new ArrayObject(size);
        id = TimerManager.setPeriodicTimer(10000, this);
    }

    /**
     * Подписаться на получение сообщений из Network по интерфейсу DataMessageListener.
     * @param listener
     */
    public void register(DataMessageListener listener) {
        short[] types = listener.getTypes();
        int length = types.length;
        for (int i = 0; i < length; i++) {
            int type = types[i];
            ArrayList typeListeners = (ArrayList) messageListeners.get(type);
            if (messageListeners.get(type) == null) {
                typeListeners = new ArrayList();
                messageListeners.set(type, typeListeners);
            }
            typeListeners.add(listener);
        }
    }

    /**
     * Сменить период опроса Network о сообщениях.
     * @param period в миллисекундах
     */
    public void changePeriod(int period){
        TimerManager.setPeriodicTimer(id, period, this);
    }

    /**
     * Отписаться на получение сообщений из Network по интерфейсу DataMessageListener.
     * @param listener
     */
    public void unregister(DataMessageListener listener) {
        int size = messageListeners.length();
        for (int i = 0; i < size; i++) {
            ((ArrayList) messageListeners.get(0)).remove(listener);
        }
    }

    @Override
    public void onTimer(int timerId) {
        DataMessage[] messages = network.getAsynchronousDataMessages();
        for (int i = 0; i < messages.length; i++) {
            DataMessage message = messages[i];
            ArrayList listeners = (ArrayList) messageListeners.get(message.getMessageType());
            int size = listeners.size();
            for (int j = 0; j < size; j++) {
                ((DataMessageListener) listeners.get(j)).onReceive(message);
            }
        }
    }
}
