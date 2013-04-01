package com.geargames.platform.network;

import com.geargames.common.logging.Debug;

/**
 * User: kewgen
 * Date: 12.03.12
 * Time: 19:36
 */
public class SendHTTP extends com.geargames.common.network.SendHTTP {

    @Override
    public void log(java.lang.String msg) {
        Debug.debug(msg);
    }

    @Override
    public void logEx(Exception exception) {
        Debug.error("", exception);
    }

}
