package com.geargames.common.network;

import com.geargames.common.io.DataInput;
import com.geargames.common.io.DataOutput;

/**
 * User: mkutuzov
 * Date: 21.03.13
 */
public abstract class Socket {
    private String address;
    private int port;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public abstract DataInput getDataInput();

    public abstract DataOutput getDataOutput();

    public abstract void connect() throws Exception;

    public abstract void disconnect();

    public abstract boolean isConnected();
}
