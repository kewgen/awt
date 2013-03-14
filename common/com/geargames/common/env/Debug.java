package com.geargames.common.env;

import com.geargames.common.String;

/**
 * User: mkutuzov
 * Date: 09.03.13
 */
public interface Debug {
    public static final boolean DEBUG = false;

    void trace(String message);

    void log(String message);

    void warning(String message);

    void error(String message);

    void exception(String message, Exception e);

    void logException(Exception exception);
}
