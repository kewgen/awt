package com.geargames;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import com.geargames.common.String;
import com.geargames.common.util.ArrayList;
import org.acra.ErrorReporter;

import java.io.File;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.TimeZone;

public class Debug {

    private static MIDlet midlet;

    public Debug() {
    }

    public static void setMIDlet(MIDlet m) {
        midlet = m;
    }

    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    public static String getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return formatSize(availableBlocks * blockSize);
    }

    public static String getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return formatSize(totalBlocks * blockSize);
    }

    public static String getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return formatSize(availableBlocks * blockSize);
        } else {
            return String.valueOfC("ERROR");
        }
    }

    public static String getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return formatSize(totalBlocks * blockSize);
        } else {
            return String.valueOfC("ERROR");
        }
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

    public static void beep() {
    }

    public static void trace(String message, Throwable e) {
        if(e != null) {
            if (message != null) {
                log(message);
            }
            e.printStackTrace();
//            assertMsg(message.concatC("\n").concatC(e.toString()), false);
        }
    }

    public static void trace(boolean key, com.geargames.common.String message) {
        if(key)
            log(message);
    }

    public static void warning(String message) {
        Log.w("gg", message.toString());
    }

    public static void assertMsg(String message, boolean condition) {
        if(!condition) {
//            trace(String.valueOfC("Assert: ").concatC(message.toString()));
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

    public static void log_(String message) {
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

    public static void log(String message) {
        trace(message.toString());
    }

    public static void trace(java.lang.String message) {
        Log.i("gg", message);
    }

    public static void logEx(Exception e) {
        Log.e("gg", "", e);
        sendReport(e, null);
        //e.printStackTrace();
    }

    public static void logEx(Exception e, Hashtable params) {
        Log.e("gg", "", e);
        sendReport(e, params);
    }

    private static int errorCounter = 0;
    public static int ERROR_MAX = 1;//ограничение на колво отправленных логов
    public static void sendReport(Exception ex, Hashtable params) {//ACRA report
        if (PortPlatform.getLevelAPI() <= 4) return;//на x11 здесь подвисает
        if (Debug.errorCounter < Debug.ERROR_MAX) {
            try {
                Debug.errorCounter++;
                if (params != null) {
                    Object uid = params.get("uid");
                    Object cid = params.get("cid");
                    if (uid != null) ErrorReporter.getInstance().putCustomData("uid", (java.lang.String) uid);
                    if (cid != null) ErrorReporter.getInstance().putCustomData("cid", (java.lang.String) cid);
                }
                ErrorReporter.getInstance().handleException(ex);
            } catch (Exception e) {//java.lang.RuntimeException: Package manager has died
                trace("sendReport error." + e.toString());
            }
        }
    }
}

