package com.geargames.platform.logging;

import com.geargames.common.logging.AbstractDebug;
import com.geargames.common.logging.Debug;
import com.geargames.common.logging.Level;
import com.geargames.common.logging.Logger;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * User: abarakov
 * Date: 14.03.13
 */
public class ConsoleDebug extends AbstractDebug {
    private static final char LINE_SEPARATOR  = '\n';

    // Минимальный уровень логгирования для консоли вывода. Сообщения более низкого уровня в консоль выводиться не будут.
    public static int consoleLogLevelMinimum = Debug.IS_DEBUG ? Level.DEBUG : Level.ALL;
    public static int beepLogLevelMinimum    = Level.WARNING;

    private StringBuilder stringBuilder = new StringBuilder(256);
    private Calendar calendar = new GregorianCalendar(TimeZone.getDefault(), Locale.getDefault());

    public ConsoleDebug() {
    }

    private void trace(String message) {
        try {
            Logger.getInstance().publish(message + LINE_SEPARATOR);
        } catch (IOException ex) {
            sendLogMessage(Level.ERROR, "Message could not be published to the logfile", ex);
            if (Level.ERROR >= consoleLogLevelMinimum) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void log(byte level, String message) {
        String messageFormated = messageFormat(level, message);
        if (level >= Logger.fileLogLevelMinimum) {
            trace(messageFormated);
        }
        if (level >= consoleLogLevelMinimum) {
            (level >= Level.WARNING ? System.err : System.out).println(messageFormated);
            if (level >= Level.ERROR) {
//                (new Exception()).printStackTrace();
                StackTraceElement[] stackTrace = (new Exception()).getStackTrace();
                for (int i = 2; i < stackTrace.length; i++) // исключем из стек трейса методы ConsoleDebug.log и Debug.error
                    System.err.println("\tat " + stackTrace[i]);
            }
        }
        if (level >= beepLogLevelMinimum) {
            java.awt.Toolkit.getDefaultToolkit().beep();
        }
    }

    @Override
    public void log(byte level, String message, Exception ex) {
        String messageFormated = messageFormat(level, message);
        if (level >= Logger.fileLogLevelMinimum) {
            trace(messageFormated);
            trace(exceptionFormat(ex));
        }
        if (level >= consoleLogLevelMinimum) {
            (level >= Level.WARNING ? System.err : System.out).println(messageFormated);
            ex.printStackTrace();
        }
        if (level >= beepLogLevelMinimum) {
            java.awt.Toolkit.getDefaultToolkit().beep();
        }
    }

    @Override
    public void sendLogMessage(byte level, String message) {
        //todo: Требуется реализация
    }

    @Override
    public void sendLogMessage(byte level, String message, Exception ex) {
        //todo: Требуется реализация
    }

    public String messageFormat(byte level, String message) {
        calendar.setTimeInMillis(System.currentTimeMillis()); //todo: использовать System.nanoTime()
        return  "[" +
                intToStr(calendar.get(Calendar.HOUR_OF_DAY), 2) + ":" +
                intToStr(calendar.get(Calendar.MINUTE),      2) + ":" +
                intToStr(calendar.get(Calendar.SECOND),      2) + "." +
                intToStr(calendar.get(Calendar.MILLISECOND), 3) + "] " +
                supplementedStringRight(Level.getLevelName(level), Level.MAXIMUM_LENGTH_LEVEL_NAMES + 1, ' ') +
                message;
    }

    public synchronized String exceptionFormat(Exception ex) {
        stringBuilder.setLength(0);
        stringBuilder.append(ex.toString());

        StackTraceElement[] stackTrace = ex.getStackTrace();
        for (int i = 0; i < stackTrace.length; i++) {
            stringBuilder.append(LINE_SEPARATOR);
            stringBuilder.append(stackTrace[i].toString());
        }

        return stringBuilder.toString();
    }

    private StringBuffer stringBuffer = new StringBuffer(4);

    private synchronized String supplementedStringLeft(String value, int charCount, char addingChar) {
        stringBuffer.setLength(0);
        for (int i = value.length() + 1; i <= charCount; i++) {
            stringBuffer.append(addingChar);
        }
        stringBuffer.append(value);
        return stringBuffer.toString();
    }

    private synchronized String supplementedStringRight(String value, int charCount, char addingChar) {
        stringBuffer.setLength(0);
        stringBuffer.append(value);
        for (int i = value.length() + 1; i <= charCount; i++) {
            stringBuffer.append(addingChar);
        }
        return stringBuffer.toString();
    }

    private String intToStr(int value, int digits) {
        return supplementedStringLeft(String.valueOf(value), digits, '0');
    }

}

