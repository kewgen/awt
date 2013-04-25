package com.geargames.common.serialization;

import com.geargames.common.util.ArrayUtils;

/**
 * User: mkutuzov, abarakov
 * Date: 19.06.12
 */
public abstract class SerializedMessage {

    public static final short HEAD_SIZE = 4;

    /**
     * Возвращает тип сообщения.
     * @return
     */
    public abstract short getType();

    /**
     * Каждое сообщение сериализуется в этот байтовый буфер.
     * @return
     */
    protected abstract MicroByteBuffer getBuffer();

    /**
     * В этом методе сериализуется содержимое сообщения.
     * @param buffer
     */
    public abstract void serialize(MicroByteBuffer buffer);

    /**
     * Метод сериализации сообщения, опирается на то, что все сообщения имеют общую структуру заголовка, но разное содержимое.
     * @return
     */
    public byte[] serialize(){
        MicroByteBuffer buffer = getBuffer();
        buffer.clear();
        buffer.setPosition(HEAD_SIZE);
        serialize(buffer);
        buffer.flip();
        short length = (short) (buffer.getLimit() - HEAD_SIZE);
        SimpleSerializer.serialize(length, buffer);
        SimpleSerializer.serialize(getType(), buffer);
        return ArrayUtils.copyOf(buffer.getBytes(), buffer.getLimit());
    }

}
