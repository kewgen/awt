package com.geargames.common.logging;

import com.geargames.common.String;

/**
 * User: abarakov
 * Date: 14.03.13
 */
public class Debug {

    public static final boolean IS_DEBUG = true;

    // формат лог-записи:
    // [ 0:00:00.065] Notice    Log session started: 29.07.2010, 01:24:25

    @Deprecated
    public static void trace(String message) {
        AbstractDebug.getInstance().trace(message);
    }

    public static void log(byte level, String message) {
        AbstractDebug.getInstance().log(level, message);
    }

    public static void log(byte level, String message, Exception ex) {
        AbstractDebug.getInstance().log(level, message, ex);
    }

    public static void debug(String message) {
        AbstractDebug.getInstance().log(Level.DEBUG, message);
    }

    public static void config(String message) {
        AbstractDebug.getInstance().log(Level.CONFIG, message);
    }

    public static void info(String message) {
        AbstractDebug.getInstance().log(Level.INFO, message);
    }

    public static void warning(String message) {
        AbstractDebug.getInstance().log(Level.WARNING, message);
    }

    public static void warning(String message, Exception ex) {
        AbstractDebug.getInstance().log(Level.WARNING, message, ex);
    }

    public static void error(String message) {
        AbstractDebug.getInstance().log(Level.ERROR, message);
    }

    public static void error(String message, Exception ex) {
        AbstractDebug.getInstance().log(Level.ERROR, message, ex);
    }

    public static void critical(String message) {
        AbstractDebug.getInstance().log(Level.CRITICAL, message);
    }

    public static void critical(String message, Exception ex) {
        AbstractDebug.getInstance().log(Level.CRITICAL, message, ex);
    }

    public static void alert(String message) {
        AbstractDebug.getInstance().log(Level.ALERT, message);
    }

    public static void alert(String message, Exception ex) {
        AbstractDebug.getInstance().log(Level.ALERT, message, ex);
    }

    public static void fatal(String message) {
        AbstractDebug.getInstance().log(Level.FATAL, message);
    }

    public static void fatal(String message, Exception ex) {
        AbstractDebug.getInstance().log(Level.FATAL, message, ex);
    }

}
