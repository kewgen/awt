package com.geargames.common.network;

import com.geargames.common.env.Environment;
import com.geargames.common.serialization.ClientDeSerializedMessage;

/**
 * User: mkutuzov
 * Date: 05.07.12
 */
public class ClientDeferredAnswer {
    private ClientDeSerializedMessage deSerializedMessage;
    private boolean deserialized;

    public ClientDeSerializedMessage getDeSerializedMessage() {
        return deSerializedMessage;
    }

    /**
     * Установить объект deSerializedMessage который будет синхронно опрашиваться о данных и в случае удачи, десериализует их
     * в игровые обекты.
     * @param deSerializedMessage
     */
    public void setDeSerializedMessage(ClientDeSerializedMessage deSerializedMessage) {
        this.deSerializedMessage = deSerializedMessage;
        deserialized = false;
    }

    public ClientDeSerializedMessage getAnswer() {
        if (deserialized) {
            return deSerializedMessage;
        } else {
            return null;
        }
    }

    /**
     * Считать сообщение за attempt попыток с приостановкой текущего потока в случае отсутсвия данных.
     * Десериализовать данные способом определённым реализацией deSerializedMessage.
     *
     * @param attempt
     * @return
     * @throws Exception в случае ошибки десериализации.
     */
    public boolean retrieve(int attempt) throws Exception {
        if (!deserialized) {
            int i = 0;
            while (getAnswer() == null) {
                if (i++ >= attempt) {
                    return false;
                }
                Environment.pause(100);
            }
            getAnswer().deSerialize();
            deserialized = true;
        }
        return deserialized;
    }

}
