package com.geargames.common.network;

import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.util.Lock;

/**
 * @author Mikhail_Kutuzov
 *         created: 25.05.12  12:02
 */
public class MessageLock {
    private Lock lock;
    private ClientDeSerializedMessage message;
    private short messageType;
    private boolean valid;

    public ClientDeSerializedMessage getMessage() {
        return message;
    }

    public void setMessage(ClientDeSerializedMessage message) {
        this.message = message;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }

    public Lock getLock() {
        return lock;
    }

    public short getMessageType() {
        return messageType;
    }

    public void setMessageType(short messageType) {
        this.messageType = messageType;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
