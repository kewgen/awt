package com.geargames.platform.io;

import com.geargames.common.io.DataInput;

import java.io.DataInputStream;

/**
 * User: mkutuzov
 * Date: 22.03.13
 */
public class ConsoleDataInput extends DataInput {
    private DataInputStream input;

    public ConsoleDataInput(DataInputStream input) {
        this.input = input;
    }

    public DataInputStream getInput() {
        return input;
    }

    public void setInput(DataInputStream input) {
        this.input = input;
    }

    public byte readByte() throws Exception {
        return input.readByte();
    }

    public short readShort() throws Exception {
        return input.readShort();
    }

    public int readInteger() throws Exception {
        return input.readInt();
    }

    public long readLong() throws Exception {
        return input.readLong();
    }

    public String readUTF() throws Exception {
        return input.readUTF();
    }

    public void readBytes(byte[] data, int offset, int length) throws Exception {
        input.read(data, offset, length);
    }

    public boolean available() throws Exception {
        return input.available() > 0;
    }
}
