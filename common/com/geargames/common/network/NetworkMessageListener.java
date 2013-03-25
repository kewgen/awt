package com.geargames.common.network;

/**
 * User: abarakov
 * Date: 24.03.13
 */
public interface NetworkMessageListener {

    /**
     * Метод вызывается каждый раз, когда получено сетевое сообщение .
     * @param msgType - тип сообщения.
     * @param message - полученное сообщение
     */
    // onNotify
    void onMessageReceived(short msgType, ClientDeferredAnswer message);

}