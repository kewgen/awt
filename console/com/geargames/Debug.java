package com.geargames;

import com.geargames.common.String;

import java.util.Calendar;
import java.util.TimeZone;

public class Debug {

    private static MIDlet midlet;

    public Debug() {
    }

    public static void setMIDlet(MIDlet m) {
        midlet = m;
    }

    public static String getTotalInternalMemorySize() {
        return formatSize(Runtime.getRuntime().totalMemory());
    }

    public static String getAvailableInternalMemorySize() {
        return formatSize(Runtime.getRuntime().freeMemory());
    }

    public static String getTotalExternalMemorySize() {
        return String.valueOfC("none");
    }

    public static String getAvailableExternalMemorySize() {
        return String.valueOfC("none");
    }

    public static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = String.valueOfC("KB");
            size /= 1024;
            if (size >= 1024) {
                suffix = String.valueOfC("MB");
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return String.valueOfC(resultBuffer.toString());
    }

    public static void close() {
    }

    public static void trace(String message, Throwable e) {
        if(e != null) {
            trace("Exception: " + message);
            e.printStackTrace();
            assertMsg(message.concatC("\n").concatC(e.toString()), false);
        }
    }

//    public static void trace(java.lang.String message) {
//        log(message);
//    }


    public static void trace(boolean key, String message) {
        if(key)
            log(message);
    }

    public static void warning(String message) {
            trace("Warning: "+message.toString());
    }

    public static void assertMsg(String message, boolean condition) {
        if(!condition) {
            trace("Assert: "+message.toString());
//            Alert alert = new Alert("Assert !");
//            alert.setTimeout(Alert.FOREVER);
//            alert.setString(message.toString());
//            Display.getDisplay(midlet).setCurrent(alert);
//            try {
//                Thread.sleep(1000L);
//            }
//            catch(Exception e) { }
//            for(; Display.getDisplay(midlet).getCurrent() == alert;) {
//                try {
//                    Thread.sleep(50);
//                } catch(InterruptedException e) {}
//            }
        }
    }

    public static void beep() {
        java.awt.Toolkit.getDefaultToolkit().beep();
    }

    public static void log(String message) {
        trace(message.toString());
    }

    public static void trace(java.lang.String message) {
        //Logger.log(message);

        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        Logger.log(intToStr(c.get(Calendar.HOUR_OF_DAY), 2)
                + ":" + intToStr(c.get(Calendar.MINUTE), 2)
                + ":" + intToStr(c.get(Calendar.SECOND), 2)
                + "." + intToStr(c.get(Calendar.MILLISECOND), 3)
                + " " + message);

    }

    public static void logEx(Exception ex) {
        Logger.logException(ex);

//        ex.printStackTrace();
    }

    private static String intToStr(int value, int digits) {
        java.lang.String svalue = Integer.toString(value);
        StringBuffer sb;
        for(sb = new StringBuffer(); svalue.length() + sb.length() < digits; sb.append("0")) ;
        sb.append(svalue);
        return String.valueOfC(sb.toString());
    }

    public static void sendReport(Exception e, int uid) {
        logEx(e);
    }
}

