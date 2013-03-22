package com.geargames.platform.io;

import com.geargames.common.io.DataOutput;

import java.io.DataOutputStream;

/**
 * User: mkutuzov
 * Date: 22.03.13
 */
public class ConsoleDataOutput extends DataOutput {
    private DataOutputStream output;

    public ConsoleDataOutput(DataOutputStream output) {
        this.output = output;
    }

    public DataOutputStream getOutput() {
        return output;
    }

    public void setOutput(DataOutputStream output) {
        this.output = output;
    }

    public void writeByte(byte data) throws Exception {
        output.writeByte(data);
    }

    public void writeShort(short data) throws Exception {
        output.writeShort(data);
    }

    public void writeInteger(int data) throws Exception {
        output.writeInt(data);
    }

    public void writeLong(long data) throws Exception {
        output.writeLong(data);
    }

    public void writeBytes(byte[] data, int offset, int length) throws Exception {
        output.write(data, offset, length);
    }

    public void writeUTF(String data) throws Exception {
        output.writeUTF(data);
    }

    @Override
    public void flush() throws Exception {
        output.flush();
    }
}
