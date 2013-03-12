package com.geargames.common.env;

import com.geargames.common.String;

/**
 * User: mkutuzov
 * Date: 09.03.13
 */
public abstract class Debug {
    public static final boolean DEBUG = false;

    public abstract void trace(boolean key, com.geargames.common.String message);

    public abstract void warning(String message);

    public abstract void trace(String message);

    public abstract void log(String message);
}
