package com.geargames.common.io;

/**
 * User: mkutuzov
 * Date: 21.03.13
 */
public abstract class DataOutput {
    public abstract void writeByte(byte data) throws Exception;
    public abstract void writeShort(short data) throws Exception;
    public abstract void writeInteger(int data) throws Exception;
    public abstract void writeLong(long data) throws Exception;
    public abstract void writeBytes(byte[] data, int offset, int length) throws Exception;
    public abstract void writeUTF(String data) throws Exception;
}
