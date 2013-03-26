package com.geargames.common.network;

/**
 * User: mkutuzov
 * Date: 25.03.13
 * Слушатель полученных сообщений.
 */
public interface DataMessageListener {
    /**
     * Вернуть массив типов ожидаемых сообщений.
     * @return
     */
    short[] getTypes();

    /**
     * Вызывается MessageDispatcher в случае получения ожидаемого сообщения.
     * @param dataMessage
     */
    void onReceive(final DataMessage dataMessage);
}
