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
    public boolean isRunning(){
        return running;
    }

    public void starting(DataInput input){
        this.input = input;
        running = true;
        startReceiving();
    }

    public void finishing(){
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

        try {
            while (isRunning()) {
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
                    res = read(input, getAnswersBuffer().getBytes(), 0, length);
                    if (res != length) {
                        throw new Exception();
                    }
                    MicroByteBuffer buffer = getAnswersBuffer();
                    buffer.initiate(getAnswersBuffer().getBytes(), length);
                    messageLock.getMessage().setBuffer(buffer);
                    messageLock.setValid(false);
                    messageLock.getLock().release();
                } else {
                    // Зачитывание асинхронного сообщения
                    byte[] data = new byte[length];
                    res = read(input, data, 0, length);
                    if (res != length) {
                        throw new Exception();
                    }
                    DataMessage dataMessage = new DataMessage();
                    dataMessage.setData(data);
                    dataMessage.setLength(length);
                    dataMessage.setMessageType(type);
                    network.addAsynchronousMessage(dataMessage);
                }
                Debug.debug("Receiver: received message: type=" + type + " (" + (type & 0xff) + "), len=" + length + ", res=" + res);
                if (length != res) {
                    Debug.error("Error received len: type=" + type + " (" + (type & 0xff) + "), len=" + length + " != res=" + res);
                    continue;
                } else if (res == -1) {
                    Debug.error("Error received: type=" + type + " (" + (type & 0xff) + "), len=" + length);
                    continue;
                }

                Environment.pause(10);
            }
        } catch (Exception e) {
            errors++;
            if (isRunning()) {
                Environment.pause(2000);
            }
            Debug.error("Receiver Exception: ", e);
            if (errors > getErrorThreshold()) {
                Debug.error("Receiver: too many errors, disconnecting");
                network.disconnect();
                return;
            }
        }
    }

    private MessageLock getMessageLockIfItExists(short type) {
        MessageLock messageLock = network.getMessageLock();
        return messageLock.isValid() && messageLock.getMessageType() == type ? messageLock : null;
    }

    private int read(DataInput dis, byte bytes[], int off, int len) throws Exception {
        if (bytes == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > bytes.length - off) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }

        int c = dis.readByte();
        if (c == -1) {
            return -1;
        }
        bytes[off] = (byte) c;

        int i = 1;
        for (; i < len; i++) {
            c = dis.readByte();
//            if (c == -1) {
//                Debug.error(String.valueOfC("read, c = ").concatI(c).concatC(" (").concatI(i).concatC(")"));
//            }
            bytes[off + i] = (byte) c;
        }
        return i;
    }

}
