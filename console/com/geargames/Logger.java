package com.geargames;

import java.io.File;
import java.util.Calendar;
import java.util.logging.*;

/** @author ewgen
 * logger*/
public abstract class Logger {

    /** @param args the command line arguments */
    public static boolean IS_JAR;

    public static final String LOG_DIR = "log";
    public static String LOG_FILE_NAME = "project";
    protected static boolean ENCODE_CP866 = false;//кодировка для вывода на консоль
    private static java.util.logging.Logger log_except = java.util.logging.Logger.getLogger("");

    static /*void createLogExcept()*/ {
        try {

            File out_dir = new File(LOG_DIR);
            if (!out_dir.exists()) out_dir.mkdir();

            Calendar c = Calendar.getInstance();
            String name = LOG_DIR + "/" + String.format("%tY", c) + String.format("%tm", c) + String.format("%td", c) + String.format("_%tH", c) + String.format("_%tM", c) + "_" + LOG_FILE_NAME + ".log";
            Handler handler = new FileHandler(name);

            handler.setFormatter(new SimpleFormatter() {
                public String format(LogRecord r) {

                    final StringBuffer sb = new StringBuffer();

                    Calendar c = Calendar.getInstance();
                    sb.append(String.format("%tT ", c));

                    sb.append(formatMessage(r));
                    sb.append('\n');
                    return sb.toString();
                }
            });
            log_except.addHandler(handler);
            log_except.setLevel(Level.INFO);

            Handler[] list = log_except.getHandlers();
            list[0].close();
            list[0].setLevel(Level.SEVERE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void log(String str) {
        log_except.log(Level.INFO, str);
        try {
            //encode для запуска из батника
            if (ENCODE_CP866) str = new String(str.getBytes("cp866"));
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logException(Exception e) {
        java.awt.Toolkit.getDefaultToolkit().beep();

        log_except.log(Level.INFO, e.toString());
        for (int i = 0; i < e.getStackTrace().length; i++) {
            log_except.log(Level.INFO, " " + e.getStackTrace()[i].toString());
        }

        e.printStackTrace();//в консоль
    }

}
