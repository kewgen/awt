package com.geargames.common.network;

import com.geargames.common.env.Environment;
import com.geargames.common.serialization.ClientDeSerializedMessage;

/**
 * User: mkutuzov
 * Date: 05.07.12
 */
public class ClientDeferredAnswer {
    private ClientDeSerializedMessage deSerializedMessage;

    public ClientDeferredAnswer(){
    }

    public ClientDeferredAnswer(ClientDeSerializedMessage deSerializedMessage) {
        this.deSerializedMessage = deSerializedMessage;
    }

    public ClientDeSerializedMessage getDeSerializedMessage() {
        return deSerializedMessage;
    }

    public void setDeSerializedMessage(ClientDeSerializedMessage deSerializedMessage) {
        this.deSerializedMessage = deSerializedMessage;
    }

    public ClientDeSerializedMessage getAnswer() {
        if (deSerializedMessage.ready()) {
            return deSerializedMessage;
        } else {
            return null;
        }
    }

    public boolean retrieve(int attempt) throws Exception {
        int i = 0;
        while (getAnswer() == null) {
            if (i++ >= attempt) {
                return false;
            }
            Environment.pause(100);
        }
        getAnswer().deSerialize();
        return true;
    }

}
