package com.geargames.network;

import com.geargames.Debug;
import com.geargames.PortPlatform;
import com.geargames.common.network.Result;

/**
 * Created by IntelliJ IDEA.
 * User: kewgen
 * Date: 12.03.12
 * Time: 19:36
 */
public class SendHTTP extends com.geargames.common.network.SendHTTP {

    public SendHTTP() {
        disableConnectionReuseIfNecessary();
    }

    private void disableConnectionReuseIfNecessary() {
        // HTTP connection reuse which was buggy pre-froyo
        if (PortPlatform.getLevelAPI() >= PortPlatform.FROYO) {//Build.VERSION_CODES.FROYO 2.2
            System.setProperty("http.keepAlive", "false");
        }
    }


    @Override
    public void log(java.lang.String msg) {
        Debug.trace(msg);
    }

    @Override
    public void logEx(Exception exception) {
        Debug.logEx(exception);
    }

}
