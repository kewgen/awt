package com.geargames.common.logging;

import com.geargames.common.String;

/**
 * User: abarakov
 * Date: 14.03.13
 */
public abstract class AbstractDebug {

    private static AbstractDebug instance;

    public static AbstractDebug getInstance() {
        return instance;
    }

    public static void setInstance(AbstractDebug instance) {
        AbstractDebug.instance = instance;
    }

//    public abstract void trace(String message);

    public abstract void log(byte level, String message);

    public abstract void log(byte level, String message, Exception ex);

    public abstract void sendLogMessage(byte level, String message);

    public abstract void sendLogMessage(byte level, String message, Exception ex);

}