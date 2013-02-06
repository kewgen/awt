package com.geargames.network;

import com.geargames.Debug;

/**
 * Created by IntelliJ IDEA.
 * User: kewgen
 * Date: 12.03.12
 * Time: 19:36
 */
public class SendHTTP extends com.geargames.common.network.SendHTTP {

    public void log(java.lang.String msg) {
        Debug.trace(msg);
    }

    public void logEx(Exception exception) {
        Debug.logEx(exception);
    }

}
