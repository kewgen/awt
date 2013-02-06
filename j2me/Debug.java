package gg;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Display;
import gg.microedition.MIDlet;
import java.util.Calendar;
import java.util.TimeZone;
import gg.microedition.String;

public class Debug {

    private static MIDlet midlet;

    public Debug() {
    }

    public static void setMIDlet(MIDlet m) {
        midlet = m;
    }

    public static void close() {
    }

    public static void beep() {
    }

    public static void trace(String message, Throwable e) {
        if(e != null) {
            log(String.valueOfC("Exception: ").concat(message).toString());
            e.printStackTrace();
            assertMsg(message.concatC("\n").concatC(e.toString()), false);
        }
    }

    public static void trace(gg.microedition.String message) {
        log(message.toString());
    }

    public static void logEx(Exception e) {
        e.printStackTrace();
    }

    public static void trace(boolean key, String message) {
        if(key)
            log(message.toString());
    }

    public static void warning(boolean condition, String message) {
        if(!condition)
            trace(String.valueOfC("Warning: ").concatC(message.toString()));
    }

    public static void assertMsg(String message, boolean condition) {
        if(!condition) {
            trace(String.valueOfC("Assert: ").concatC(message.toString()));
            Alert alert = new Alert("Assert !");
            alert.setTimeout(Alert.FOREVER);
            alert.setString(message.toString());
            Display.getDisplay(midlet).setCurrent(alert);
            try {
                Thread.sleep(1000L);
            }
            catch(Exception e) { }
            for(; Display.getDisplay(midlet).getCurrent() == alert;) {
                try {
                    Thread.sleep(50);
                } catch(InterruptedException e) {}
            }
        }
    }

    public static void log(java.lang.String message) {
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        System.out.println(intToStr(c.get(Calendar.HOUR_OF_DAY), 2)
                           + ":" + intToStr(c.get(Calendar.MINUTE), 2)
                           + ":" + intToStr(c.get(Calendar.SECOND), 2)
                           + "." + intToStr(c.get(Calendar.MILLISECOND), 3)
                           + " " + message);
    }

    private static String intToStr(int value, int digits) {
        java.lang.String svalue = Integer.toString(value);
        StringBuffer sb;
        for(sb = new StringBuffer(); svalue.length() + sb.length() < digits; sb.append("0")) ;
        sb.append(svalue);
        return String.valueOfC(sb.toString());
    }
}

