package com.geargames.common.logging;

/**
 * User: abarakov
 * Date: 14.03.13
 */
public class Debug {

    public static final boolean IS_DEBUG = true;

    private static AbstractDebug instance;

    public static AbstractDebug getInstance() {
        return instance;
    }

    public static void setInstance(AbstractDebug instance) {
        Debug.instance = instance;
    }

    public static void log(byte level, String message) {
        instance.log(level, message);
    }

    public static void log(byte level, String message, Exception ex) {
        instance.log(level, message, ex);
    }

    public static void debug(String message) {
        instance.log(Level.DEBUG, message);
    }

    public static void config(String message) {
        instance.log(Level.CONFIG, message);
    }

    public static void info(String message) {
        instance.log(Level.INFO, message);
    }

    public static void warning(String message) {
        instance.log(Level.WARNING, message);
    }

    public static void warning(String message, Exception ex) {
        instance.log(Level.WARNING, message, ex);
    }

    public static void error(String message) {
        instance.log(Level.ERROR, message);
    }

    public static void error(String message, Exception ex) {
        instance.log(Level.ERROR, message, ex);
    }

    public static void critical(String message) {
        instance.log(Level.CRITICAL, message);
    }

    public static void critical(String message, Exception ex) {
        instance.log(Level.CRITICAL, message, ex);
    }

    public static void alert(String message) {
        instance.log(Level.ALERT, message);
    }

    public static void alert(String message, Exception ex) {
        instance.log(Level.ALERT, message, ex);
    }

    public static void fatal(String message) {
        instance.log(Level.FATAL, message);
    }

    public static void fatal(String message, Exception ex) {
        instance.log(Level.FATAL, message, ex);
    }
}
