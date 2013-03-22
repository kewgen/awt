package com.geargames.platform.network;

import com.geargames.common.io.DataInput;
import com.geargames.common.io.DataOutput;
import com.geargames.common.network.*;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.util.Lock;
import com.geargames.platform.util.JavaLock;

/**
 * User: mkutuzov
 * Date: 22.03.13
 */
public class ConsoleNetwork extends Network {
    private Socket socket;
    private MicroByteBuffer buffer;
    private MessageLock messageLock;
    private Lock lock;

    /**
     * Конструктору необходимо передать байтовый буфер для заполнения ответами на синхронные сообщения.
     * @param buffer
     */
    public ConsoleNetwork(MicroByteBuffer buffer) {
        this.buffer = buffer;
        messageLock = new MessageLock();
        messageLock.setValid(false);
        messageLock.setLock(new JavaLock());
        lock = new JavaLock();
    }

    @Override
    public int getPort() {
        return socket.getPort();
    }

    @Override
    public String getAddress() {
        return socket.getAddress();
    }

    @Override
    protected Sender createSender(DataOutput output) {
        return new ConsoleSender(this);
    }

    @Override
    protected Receiver createReceiver(DataInput input) {
        return new ConsoleReceiver(this, this.buffer);
    }

    @Override
    protected Socket createSocket(String address, int port) {
        socket = new ConsoleSocket();
        socket.setPort(port);
        socket.setAddress(address);
        return socket;
    }

    @Override
    protected Lock getAsynchronousLock() {
        return lock;
    }

    @Override
    public MessageLock getMessageLock() {
        return messageLock;
    }
}
