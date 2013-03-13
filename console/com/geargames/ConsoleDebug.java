package com.geargames;

import com.geargames.common.String;

import java.util.Calendar;
import java.util.TimeZone;

public class ConsoleDebug implements com.geargames.common.env.Debug {

    public void error(String message) {
        trace(String.valueOfC("Error: ").concat(message));
    }


    public void warning(String message) {
        trace(String.valueOfC("Warning: ").concat(message));
    }

    public void log(String message) {
        trace(message);
    }

    public void trace(String message) {
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        Logger.log(intToStr(c.get(Calendar.HOUR_OF_DAY), 2)
                + ":" + intToStr(c.get(Calendar.MINUTE), 2)
                + ":" + intToStr(c.get(Calendar.SECOND), 2)
                + "." + intToStr(c.get(Calendar.MILLISECOND), 3)
                + " " + message.toString());

    }

    public void trace(String message, Exception e) {
        if (e != null) {
            trace(String.valueOfC("Exception: " + message));
            e.printStackTrace();
            log(message.concatC("\n").concatC(e.toString()));
        }
    }

    public void exception(String message, Exception e) {
        Logger.logException(e);
    }

    public void logException(Exception ex) {
        Logger.logException(ex);
    }

    private String intToStr(int value, int digits) {
        java.lang.String svalue = Integer.toString(value);
        StringBuffer sb;
        for (sb = new StringBuffer(); svalue.length() + sb.length() < digits; sb.append("0")) ;
        sb.append(svalue);
        return String.valueOfC(sb.toString());
    }

}

