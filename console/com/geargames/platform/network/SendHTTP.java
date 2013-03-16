package com.geargames.platform.network;

import com.geargames.common.logging.Debug;
import com.geargames.common.String;

/**
 * User: kewgen
 * Date: 12.03.12
 * Time: 19:36
 */
public class SendHTTP extends com.geargames.common.network.SendHTTP {

    @Override
    public void log(java.lang.String msg) {
        Debug.debug(com.geargames.common.String.valueOfC(msg));
    }

    @Override
    public void logEx(Exception exception) {
        Debug.error(String.valueOfC(""), exception);
    }

}
