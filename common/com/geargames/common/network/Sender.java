package com.geargames.common.network;

import com.geargames.common.String;
import com.geargames.common.io.DataOutput;
import com.geargames.common.logging.Debug;
import com.geargames.common.serialization.SerializedMessage;
import com.geargames.common.util.Lock;

import java.util.Vector;

/**
 * User: m.v.kutuzov
 * Date: 20.03.13
 */
public abstract class Sender {
    private Vector messageQueue;
    private boolean running;
    private DataOutput output;

    private Network network;

    protected Sender(Network network) {
        messageQueue = new Vector();
        this.network = network;
    }

    public boolean isRunning() {
        return running;
    }


    public void starting(DataOutput output) {
        this.output = output;
        running = true;
        startSending();
    }

    public void finishing() {
        stopSending();
        running = false;
    }

    /**
     * Запуск потока рассылки сообщений.
     */
    protected abstract void startSending();

    /**
     * Останов потока рассылки сообщений.
     */
    protected abstract void stopSending();


    protected abstract int getErrorThreshold();

    /**
     * Экземпляр класса симафора на конкретной платформе.
     * Будет использоваться для блокировки работы потока в случае отсутсвия исходящих сообщений.
     *
     * @return
     */
    protected abstract Lock getWorkLock();

    public void sending() {
        int errors = 0;
        while (isRunning()) {
            if (messageQueue.isEmpty()) {
                getWorkLock().lock();
                if (messageQueue.isEmpty()) {
                    getWorkLock().lock();
                }
                if (!running) {
                    break;
                }
            }

            try {
                SerializedMessage message = (SerializedMessage) messageQueue.elementAt(0);
                byte[] serialized = message.serialize();
                output.writeBytes(serialized, 0, serialized.length);
                messageQueue.removeElementAt(0);
                MessageLock messageLock = network.getMessageLock();
                messageLock.setMessageType(message.getType());
                messageLock.getLock().lock();
            } catch (Exception e) {
                errors++;
                if (errors > getErrorThreshold()) {
                    Debug.error(com.geargames.common.String.valueOfC("Sender: too many errors, disconnecting"), e);
                    network.disconnect();
                    return;
                }
                Debug.error(String.valueOfC("Sender exception"), e);
            }
        }
    }

    public void sendMessage(SerializedMessage message) {
        messageQueue.addElement(message);
        getWorkLock().release();
    }

}
