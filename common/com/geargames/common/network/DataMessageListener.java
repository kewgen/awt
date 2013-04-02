package com.geargames.common.network;

import com.geargames.common.serialization.ClientDeSerializedMessage;

/**
 * User: mkutuzov
 * Date: 25.03.13
 * Слушатель полученных сообщений.
 */
public interface DataMessageListener {

    /**
     * Желаемый интервал опроса очереди асинхронных сообщений (в миллисекундах).
     * @return
     */
    int getInterval();

    /**
     * Получить массив типов ожидаемых сообщений.
     * @return
     */
    short[] getTypes();

    /**
     * Вызывается MessageDispatcher'ом в случае получения одного из ожидаемых сообщений.
     * @param message
     * @param type
     */
    void onReceive(ClientDeSerializedMessage message, short type);

}
