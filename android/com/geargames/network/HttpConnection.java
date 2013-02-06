package com.geargames.network;

//import javax.microedition.io.Connector;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnection {

    public static final java.lang.String GET = "GET";
    public static final int HTTP_OK = HttpURLConnection.HTTP_OK;

    private HttpURLConnection h_conn;

    public static HttpConnection open(java.lang.String str_) throws java.io.IOException {
        URL url = new URL(str_);
        HttpConnection o_conn = new HttpConnection();
        o_conn.h_conn = (HttpURLConnection) url.openConnection();

        return o_conn;
    }

    public void close() throws java.io.IOException {
        h_conn.disconnect();
    }

    public void setRequestMethod(java.lang.String str_) throws java.io.IOException {//только для использования в непортируемых классах
//        h_conn.setRequestMethod(str_);

//        h_conn.setReadTimeout(15000);
//        h_conn.setConnectTimeout(15000);

//        h_conn.setUseCaches(false);
//        h_conn.setDoOutput(true);
//        h_conn.setDoInput(true);

//        h_conn.connect();
    }

    public int getResponseCode() throws java.io.IOException {
        return h_conn.getResponseCode();
    }

    public InputStream openInputStream() throws java.io.IOException {
        return new BufferedInputStream(h_conn.getInputStream());
    }

}
