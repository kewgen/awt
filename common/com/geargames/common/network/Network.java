package com.geargames.common.network;

import com.geargames.common.io.DataInput;
import com.geargames.common.io.DataOutput;
import com.geargames.common.logging.Debug;
import com.geargames.common.serialization.ClientDeSerializedMessage;
import com.geargames.common.util.Lock;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.common.serialization.SerializedMessage;

import java.util.Vector;

/**
 * Класс запускает потоки приёма и передачи сообщений между клиентом и сервером.
 */
public abstract class Network {
    private Vector asynchronousMessages;
    private MicroByteBuffer buffer;
    private Receiver receiver;
    private Sender sender;
    private Socket socket;

    protected Network() {
        asynchronousMessages = new Vector();
        buffer = new MicroByteBuffer();
    }

    /**
     * Присоединиться к серверу по адресу address на порте port
     * @param address
     * @param port
     * @return
     */
    public boolean connect(String address, int port){
        socket = createSocket(address, port);
        try{
            socket.connect();
            receiver = createReceiver(socket.getDataInput());
            sender = createSender(socket.getDataOutput());
            receiver.starting(socket.getDataInput());
            sender.starting(socket.getDataOutput());
        }catch (Exception e){
            Debug.error(com.geargames.common.String.valueOfC("Server connection could not be established"), e);
            return false;
        }
        return true;
    }

    /**
     * Отсоедениться.
     */
    public void disconnect(){
        sender.finishing();
        receiver.finishing();
        socket.disconnect();
    }

    /**
     * Порт сервера.
     * @return
     */
    public abstract int getPort();

    /**
     * IP адрес сервера.
     * @return
     */
    public abstract String getAddress();

    /**
     * Создать поток для отправки соообщений серверу.
     * @return
     */
    protected abstract Sender createSender(DataOutput output);

    /**
     * Создать поток для чтения сообщений сервера.
     * @return
     */
    protected abstract Receiver createReceiver(DataInput input);

    /**
     * Создать платформенный сокет.
     * @param string
     * @param port
     * @return
     */
    protected abstract Socket createSocket(String string, int port);

    /**
     * Вернём симафор для доступа очереди асинхронных ответов.
     * @return
     */
    protected abstract Lock getAsynchronousLock();

    /**
     * Вернём сообщение-блокировку синхронных сообщений.
     * @return
     */
    public abstract MessageLock getMessageLock();

    /**
     * Послать сообщение на сервер.
     * Ответ ожидать через очередь асинхронных сообщений.
     * @param message
     */
    public void sendMessage(SerializedMessage message) {
        sender.sendMessage(message);
    }

    /**
     * Послать сообщение и получить объект-отложенный ответ.
     * @param request
     * @param answer
     * @return
     */
    public ClientDeferredAnswer sendSynchronousMessage(SerializedMessage request, ClientDeSerializedMessage answer){
        MessageLock lock = getMessageLock();
        lock.setMessageType(request.getType());
        lock.setValid(true);
        lock.setMessage(answer);
        sender.sendMessage(request);

        return new ClientDeferredAnswer(answer);
    }

    /**
     * Вернуть количество асинхронных ответов ожидающих обработки.
     * @return
     */
    public int getAsynchronousMessagesSize() {
        getAsynchronousLock().lock();
        int result = asynchronousMessages.size();
        getAsynchronousLock().release();
        return result;
    }

    /**
     * Достать самый старый ответ на асинхронное сообщение типа type.
     * @param type
     * @return
     */
    public DataMessage getAsynchronousMessageByType(short type) {
        getAsynchronousLock().lock();
        DataMessage dataMessage = null;
        for (int i = 0; i < asynchronousMessages.size(); i++) {
            dataMessage = (DataMessage) asynchronousMessages.get(i);
            if (dataMessage.getMessageType() == type) {
                asynchronousMessages.removeElementAt(i);
                break;
            }
            dataMessage = null;
        }
        getAsynchronousLock().release();
        return dataMessage;
    }

    /**
     * Прочитать ответ на асинхронное сообщение типа messageType в объект answer.
     * @param answer
     * @param messageType
     * @return true если answer успешно заполнен данными.
     */
    public synchronized boolean getAsynchronousAnswer(ClientDeSerializedMessage answer, short messageType) throws Exception {
        DataMessage message = getAsynchronousMessageByType(messageType);
        if (message != null) {
            buffer.initiate(message.getData());
            answer.setBuffer(buffer);
            answer.deSerialize();
            answer.setBuffer(null);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Добавить ответ на асинхронный запрос в очередь.
     * @param dataMessage
     */
    public void addAsynchronousMessage(DataMessage dataMessage) {
        getAsynchronousLock().lock();
        asynchronousMessages.addElement(dataMessage);
        getAsynchronousLock().release();
    }

}