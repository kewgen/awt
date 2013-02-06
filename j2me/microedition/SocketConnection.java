package gg.microedition;

import javax.microedition.io.Connector;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class SocketConnection {

    public static final byte DELAY = javax.microedition.io.SocketConnection.DELAY;
    public static final byte RCVBUF = javax.microedition.io.SocketConnection.RCVBUF;
    public static final byte SNDBUF = javax.microedition.io.SocketConnection.SNDBUF;
    public static final byte KEEPALIVE = javax.microedition.io.SocketConnection.KEEPALIVE;
    public static final int READ_WRITE = Connector.READ_WRITE;

    private javax.microedition.io.SocketConnection h_conn;

    public static SocketConnection open(java.lang.String str_, int port) throws java.io.IOException {
        SocketConnection o_conn = new SocketConnection();
        o_conn.h_conn = (javax.microedition.io.SocketConnection) Connector.open("socket://" + str_ + ":" + port);
        return o_conn;
    }

    public static SocketConnection open(java.lang.String str_, int port, int mode, boolean timeouts) throws java.io.IOException {
        SocketConnection o_conn = new SocketConnection();
        o_conn.h_conn = (javax.microedition.io.SocketConnection) Connector.open("socket://" + str_ + ":" + port, mode, timeouts);
        return o_conn;
    }

    public void close() throws java.io.IOException {
        h_conn.close();
    }

    public int getSocketOption(byte opt) throws java.io.IOException {
        return h_conn.getSocketOption(opt);
    }

    public DataInputStream openDataInputStream() throws java.io.IOException {
        return h_conn.openDataInputStream();
    }

    public DataOutputStream openDataOutputStream() throws java.io.IOException {
        return h_conn.openDataOutputStream();
    }

}
