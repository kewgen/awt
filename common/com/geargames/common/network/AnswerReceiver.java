package com.geargames.common.network;

import com.geargames.common.timers.*;
import com.geargames.common.util.ArrayList;

/**
 * User: abarakov
 * Date: 24.03.13
 * Класс, задача которого, чтение сетевых сообщений и оповещение об этом интересующих объектов.
 */
// AnswerReader, AnswerManager
public class AnswerReceiver implements TimerListener {

    private ArrayList listenerList;
    private ArrayList timeList;

    private NetworkMessageListener getActiveListener() {
        if (listenerList.size() > 0) {
            return (NetworkMessageListener) listenerList.get(listenerList.size() - 1);
        }
        return null;
    }

    public static void push(NetworkMessageListener listener) {

    }

    // Добавить/зарегистрировать слушателя сообщений с типом msgType
    // registerMessageListener
    public static void setMessageHandler(short msgType, ClientDeferredAnswer message, NetworkMessageListener listener) {

    }

    public static void setUnhandledMessageHandler(NetworkMessageListener listener) {

    }

    private static void doMessage(short msgType, ClientDeferredAnswer message) {

    }

    private static void doUnhandledMessage(short msgType) {

    }

    private void start() {
        TimerManager.setPeriodicTimer(TimerIdMap.NETWORK_ANSWER_READER_TIMER, 1000, this);
    }

    private void stop() {
        TimerManager.killTimer(TimerIdMap.NETWORK_ANSWER_READER_TIMER);
    }

    @Override
    public void onTimer(int timerId) {

    }

}