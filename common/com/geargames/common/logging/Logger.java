package com.geargames.common.logging;

import java.io.IOException;

/**
 * User: abarakov
 * Date: 14.03.13
 */
public abstract class Logger {

    public static String logPathName = "log";
    public static String logFileName = "project";

    // Минимальный уровень логгирования для лог-файла. Сообщения более низкого уровня в файл логов выводиться не будут.
    public static int fileLogLevelMinimum = Debug.IS_DEBUG ? Level.DEBUG : Level.CONFIG;

    private static Logger instance;

    public static Logger getInstance() {
        return instance;
    }

    public static void setInstance(Logger instance) {
        Logger.instance = instance;
    }

    public abstract void publish(String string) throws IOException;

    public abstract void flush() throws IOException;

    public abstract void close() throws IOException;

}