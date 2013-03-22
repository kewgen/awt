package com.geargames.common.serialization;

import com.geargames.common.logging.Debug;

/**
 * User: mkutuzov, abarakov
 * Date: 04.07.12
 */
public abstract class DeSerializedMessage {

    public abstract void deSerialize(MicroByteBuffer buffer) throws Exception;

    public abstract MicroByteBuffer getBuffer();

    public void deSerialize() throws Exception {
        MicroByteBuffer buffer = getBuffer();
        deSerialize(buffer);
        // Проверка того, что сообщение было считано полностью и не считано лишних данных
        if (buffer.position() - 1 != buffer.limit()) {
            Debug.error("DeSerializedMessage.deSerialize(): The expected position does not coincide with the actual position (" +
                            "expected=" + buffer.limit() + "; actual=" + (buffer.position() - 1) + ")");
        }
    }

}
