package com.geargames.platform;

import com.geargames.common.logging.Debug;
import com.geargames.common.logging.Logger;
import com.geargames.common.String;
import com.geargames.common.util.Recorder;
import com.geargames.platform.env.ConsoleEnvironment;
import com.geargames.platform.logging.ConsoleDebug;
import com.geargames.platform.logging.ConsoleLogger;
import com.geargames.platform.util.ConsoleRecorder;

import java.io.File;
import java.io.IOException;

/**
 * Users: ewgen, abarakov
 */
public abstract class Main {

    /** @param args the command line arguments */
    public static boolean IS_JAR;

    protected Main() {
        ConsoleEnvironment.setInstance(new ConsoleEnvironment());
        Logger.setInstance(new ConsoleLogger());
        ConsoleDebug.setInstance(new ConsoleDebug());
        Recorder.setRecorder(new ConsoleRecorder(new File("regolith.data.storage"), new File("regolith.property.storage")));
    }

    public void commonMain(java.lang.String[] args) throws IOException {
        if (args != null) {
            for (java.lang.String string : args) {
                Debug.config(String.valueOfC("Read arg:" + string));
                if (string.equals("is_jar")) {//запуск из jar-файла
                    IS_JAR = true;
               } else if (string.equals("debug")) {//включаем дебаг
               }
            }
        }
    }

}
