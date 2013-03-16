package com.geargames.platform.logging;

import com.geargames.common.String;
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
        fileWriter.append(string.toString());
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
            File out_dir = new File(logPathName.toString());
            if (!out_dir.exists()) {
                out_dir.mkdir();
            }

            // project_20130313_20_04.log
            SimpleDateFormat fileFormat = new SimpleDateFormat("yyyyMMdd_HH_mm");

            java.lang.String fileName = logPathName + "/" + logFileName + "_" + fileFormat.format( new Date() ) + ".log";

            File file = new File(fileName);
            fileWriter = new FileWriter(file);

            fileWriter.append("");
            fileWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}