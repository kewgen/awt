package com.geargames.platform.network;

import com.geargames.common.io.DataInput;
import com.geargames.common.io.DataOutput;
import com.geargames.common.logging.Debug;
import com.geargames.common.network.Socket;
import com.geargames.platform.io.ConsoleDataInput;
import com.geargames.platform.io.ConsoleDataOutput;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * User: mkutuzov
 * Date: 22.03.13
 */
public class ConsoleSocket extends Socket {
    private java.net.Socket socket;
    private ConsoleDataInput input;
    private ConsoleDataOutput output;

    public DataInput getDataInput() {
        return input;
    }

    public DataOutput getDataOutput() {
        return output;
    }

    public void connect() throws Exception {
        Debug.debug("Connect to " + getAddress() + ":" + getPort());
        SocketAddress socketAddress = new InetSocketAddress(getAddress(), getPort());
        socket = new java.net.Socket();
        socket.connect(socketAddress);
        input = new ConsoleDataInput(new DataInputStream(new BufferedInputStream(socket.getInputStream())));
        output = new ConsoleDataOutput(new DataOutputStream(new BufferedOutputStream(socket.getOutputStream())));
    }

    public void disconnect() {
        try {
            input.getInput().close();
        } catch (Exception e) {
        }
        try {
            output.getOutput().close();
        } catch (Exception e) {
        }
        try {
            socket.close();
        } catch (Exception e) {
        }
    }

    public boolean isConnected() {
        return socket.isConnected();
    }
}
