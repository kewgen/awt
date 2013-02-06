package com.geargames.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class SocketConnection {

    public static final byte DELAY = 0;
    public static final byte RCVBUF = 1;
    public static final byte SNDBUF = 2;
    public static final byte KEEPALIVE = 3;
    public static final int READ_WRITE = 0;

    private Socket h_conn;

    public static SocketConnection open(java.lang.String str_, int port) throws java.io.IOException {
        SocketConnection o_conn = new SocketConnection();
        o_conn.h_conn = new Socket(str_, port);
        return o_conn;
    }

    public static SocketConnection open(java.lang.String str_, int port, int mode, boolean timeouts) throws java.io.IOException {
        SocketConnection o_conn = new SocketConnection();
        o_conn.h_conn = new Socket(str_, port);
        return o_conn;
    }

    public void close() throws java.io.IOException {
        h_conn.close();
    }

    public int getSocketOption(byte opt) throws java.io.IOException {
        switch (opt) {
            case RCVBUF:
                return h_conn.getReceiveBufferSize();
            case SNDBUF:
                return h_conn.getSendBufferSize();
            case KEEPALIVE:
                return (h_conn.getKeepAlive() ? 1 : 0);
        }
        return 0;
    }

    public DataInputStream openDataInputStream() throws java.io.IOException {
        return new DataInputStream(h_conn.getInputStream());
    }

    public DataOutputStream openDataOutputStream() throws java.io.IOException {
        return new DataOutputStream(h_conn.getOutputStream());
    }

}
