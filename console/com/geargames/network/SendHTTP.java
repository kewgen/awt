package com.geargames.network;

import com.geargames.ConsoleDebug;
import com.geargames.common.env.SystemEnvironment;

/**
 * Created by IntelliJ IDEA.
 * User: kewgen
 * Date: 12.03.12
 * Time: 19:36
 */
public class SendHTTP extends com.geargames.common.network.SendHTTP {

    public void log(java.lang.String msg) {
        SystemEnvironment.getInstance().getDebug().trace(com.geargames.common.String.valueOfC(msg));
    }

    public void logEx(Exception exception) {
        ((ConsoleDebug)SystemEnvironment.getInstance().getDebug()).logEx(exception);
    }

}
