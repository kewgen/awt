package com.geargames.common.network;

import com.geargames.common.env.Environment;
import com.geargames.common.io.DataInput;
import com.geargames.common.logging.Debug;
import com.geargames.common.serialization.MicroByteBuffer;

public abstract class Receiver {
    private boolean running;
    private Network network;
    private DataInput input;

    protected Receiver(Network network) {
        this.network = network;
    }

    /**
     * Вернет true, если вычитывающий поток в работает.
     */
    public boolean isRunning() {
        return running;
    }

    public void starting(DataInput input) {
        this.input = input;
        running = true;
        startReceiving();
    }

    public void finishing() {
        running = false;
        stopReceiving();
    }

    /**
     * Запустить вычитывающий поток.
     */
    protected abstract void startReceiving();

    /**
     * Остановить вычитывающий поток.
     */
    protected abstract void stopReceiving();

    /**
     * Получить максимальное количество ошибок чтения, после которого будет разорвана связь с сервером.
     * @return
     */
    public abstract int getErrorThreshold();

    /**
     * Получить буфер для записи ответов на синхронные сообщения.
     * @return
     */
    protected abstract MicroByteBuffer getAnswersBuffer();

    protected void receiving() {
        int errors = 0;
        short type;  // ID сообщения
        int length;  // Длина данных сообщения

        while (isRunning()) {
            try {
                while (!input.available()) {
                    if (!isRunning()) {
                        break;
                    }
                    Environment.pause(50);
                }
                if (!isRunning()) {
                    break;
                }

                length = input.readShort() & 0xffff;
                type = input.readShort();
                int res;

                MessageLock messageLock = getMessageLockIfItExists(type);
                if (messageLock != null) {
                    // Зачитывание синхронного сообщения
                    res = input.readBytes(getAnswersBuffer().getBytes(), 0, length);
                    if (res != length) {
                        throw new Exception("Error received len: type=" + type + " (" + (type & 0xff) + "), len=" + length + " != res=" + res);
                    }
                    MicroByteBuffer buffer = getAnswersBuffer();
                    buffer.initiate(getAnswersBuffer().getBytes(), length);
                    messageLock.getMessage().setBuffer(buffer);
                    messageLock.setValid(false);
                    messageLock.getLock().release();
                } else {
                    // Зачитывание асинхронного сообщения
                    byte[] data = new byte[length];
                    res = input.readBytes(data, 0, length);
                    if (res != length) {
                        throw new Exception("Error received len: type=" + type + " (" + (type & 0xff) + "), len=" + length + " != res=" + res);
                    }
                    if (res <= 0) {
                        Debug.critical("Error received: type=" + type + " (" + (type & 0xff) + "), len=" + length);
                        continue;
                    }
                    DataMessage dataMessage = new DataMessage();
                    dataMessage.setMessageType(type);
                    dataMessage.setLength(length);
                    dataMessage.setData(data);
                    network.addAsynchronousMessage(dataMessage);
                }
            } catch (Exception e) {
                Debug.critical("Receiver Exception:", e);
                errors++;
                if (errors > getErrorThreshold()) {
                    Debug.fatal("Receiver: too many errors, disconnecting (error count = " + errors + ")");
                    network.disconnect();
                    return;
                }
            }
        }
    }

    private MessageLock getMessageLockIfItExists(short type) {
        MessageLock messageLock = network.getMessageLock();
        return messageLock.isValid() && messageLock.getMessageType() == type ? messageLock : null;
    }
}
