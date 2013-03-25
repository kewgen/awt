package com.geargames.common.serialization;

/**
 * Класс наподобие nio.ByteBuffer, предоставляет функционал обработки массива байтов разной длинны из одного массива.
 * User: mkutuzov, abarakov
 * Date: 27.03.12
 */
public class MicroByteBuffer {
    private int position;
    private int limit;
    private int mark;
    private byte[] buffer;

    public MicroByteBuffer(){
    }

    public MicroByteBuffer(byte[] buffer) {
        this.buffer   = buffer;
        this.position = 0;
        this.limit    = buffer.length;
        this.mark     = 0;
    }

    public MicroByteBuffer initiate(byte[] buffer, int length) {
        this.buffer = buffer;
        setLimit(length);
        setPosition(0);
        mark = 0;
        return this;
    }

    public MicroByteBuffer initiate(byte[] buffer) {
        this.buffer = buffer;
        setLimit(buffer.length);
        setPosition(0);
        mark = 0;
        return this;
    }

    public void clear(){
        position = 0;
        limit = size();
        mark = 0;
    }

    public void flip(){
        limit = position + 1;
        position = 0;
    }

    private void check(int position) {
        if (position < 0 || position >= limit) {
            throw new IndexOutOfBoundsException("position = " + position + ", limit = " + limit);
        }
    }

    /**
     * Считать байт из массива по индексу position.
     * Если position >= limit, то будет вызвано исключение.
     * @param position
     * @return
     */
    public byte get(int position) {
        check(position);
        return buffer[position];
    }

    /**
     * Считать текущий байт из массива. Нарастить счётчик массива.
     * Если этот счётчик >= limit будет вызвано исключение.
     * @return
     */
    public byte get() {
        check(position);
        return buffer[position++];
    }

    public MicroByteBuffer put(byte value, int position) {
        check(position);
        buffer[position] = value;
        return this;
    }

    public MicroByteBuffer put(byte value) {
        check(position);
        buffer[position] = value;
        position++;
        return this;
    }

    public MicroByteBuffer put(MicroByteBuffer bytes) {
        if (this.position + bytes.limit - bytes.position > this.limit) {
            throw new IndexOutOfBoundsException("position = " + position + ", limit = " + limit + ", amount = " + (bytes.limit - bytes.position));
        }
        for (int i = bytes.position; i < bytes.limit; i++) {
            this.buffer[position + i] = bytes.buffer[i];
        }
        this.position += bytes.limit - bytes.position;
        bytes.position = bytes.limit;
        return this;
    }

    public MicroByteBuffer mark() {
        mark = position;
        return this;
    }

    public MicroByteBuffer reset() {
        position = mark;
        return this;
    }

    public int size() {
        return buffer.length;
    }

    public int getPosition() {
        return position;
    }

    public MicroByteBuffer setPosition(int position) {
        if (position < 0 || position > limit) {
            throw new IndexOutOfBoundsException("position = " + position + ", limit = " + limit);
        }
        this.position = position;
        return this;
    }

    public int getLimit() {
        return limit;
    }

    public MicroByteBuffer setLimit(int limit) {
        if (limit < 0 || limit > size()) {
            throw new IndexOutOfBoundsException("limit = " + limit + ", size = " + size());
        }
        this.limit = limit;
        if (position > limit) {
            position = limit;
        }
        return this;
    }

    public byte[] getBytes(){
        return buffer;
    }

}
