package com.geargames.common.io;

/**
 * User: mkutuzov
 * Date: 21.03.13
 */
public abstract class DataInput {
    public abstract byte readByte() throws Exception;

    public abstract short readShort() throws Exception;

    public abstract int readInteger() throws Exception;

    public abstract long readLong() throws Exception;

    public abstract String readUTF() throws Exception;

    public abstract int readBytes(byte[] data, int offset, int length) throws Exception;

    public abstract boolean available() throws Exception;
}
