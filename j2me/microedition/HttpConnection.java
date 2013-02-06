package gg.microedition;

import javax.microedition.io.Connector;
import java.io.InputStream;

public class HttpConnection {

    public static final java.lang.String GET = javax.microedition.io.HttpConnection.GET;
    public static final int HTTP_OK = javax.microedition.io.HttpConnection.HTTP_OK;

    private javax.microedition.io.HttpConnection h_conn;

    public static HttpConnection open(java.lang.String str_) throws java.io.IOException {
        HttpConnection o_conn = new HttpConnection();
        o_conn.h_conn = (javax.microedition.io.HttpConnection) Connector.open(str_);
        return o_conn;
    }

    public void close() throws java.io.IOException {
        h_conn.close();
    }

    public void setRequestMethod(java.lang.String str_) throws java.io.IOException {//только для использования в непортируемых классах
        h_conn.setRequestMethod(str_);
    }

    public int getResponseCode() throws java.io.IOException {
        return h_conn.getResponseCode();
    }

    public InputStream openInputStream() throws java.io.IOException {
        return h_conn.openInputStream();
    }

}
