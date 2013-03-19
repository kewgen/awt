package com.geargames.platform.logging;

import com.geargames.common.logging.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: abarakov
 * Date: 14.03.13
 */
public class ConsoleLogger extends Logger {

    private FileWriter fileWriter;

    public ConsoleLogger() {
        createLogFile();
    }

    @Override
    public void publish(String string) throws IOException {
        fileWriter.append(string);
        flush();
    }

    @Override
    public void flush() throws IOException {
        fileWriter.flush();
    }

    @Override
    public void close() throws IOException {
        fileWriter.close();
    }

    private void createLogFile() {
        try {
            File out_dir = new File(logPathName);
            if (!out_dir.exists()) {
                out_dir.mkdir();
            }

            // log/project_20130313_2004.log
            SimpleDateFormat fileFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
            String fileName = logPathName + File.separator + logFileName + "_" + fileFormat.format( new Date() ) + ".log";

            File file = new File(fileName);
            fileWriter = new FileWriter(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}