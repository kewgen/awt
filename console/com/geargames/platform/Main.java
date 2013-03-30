package com.geargames.platform;

import com.geargames.common.env.Environment;
import com.geargames.common.logging.Debug;
import com.geargames.common.logging.Logger;
import com.geargames.common.util.Recorder;
import com.geargames.platform.env.ConsoleEnvironment;
import com.geargames.platform.logging.ConsoleDebug;
import com.geargames.platform.logging.ConsoleLogger;
import com.geargames.platform.util.ConsoleRecorder;

import java.io.File;
import java.io.IOException;

/**
 * Users: ewgen, abarakov, mikhail v. kutuzov
 */
public abstract class Main {

    public static boolean IS_JAR;

    protected Main() {
        Environment.setInstance(new ConsoleEnvironment());
        Logger.setInstance(new ConsoleLogger());
        Debug.setInstance(new ConsoleDebug());
        Recorder.setRecorder(new ConsoleRecorder(new File(Recorder.storageFolder), new File(Recorder.storageProperties)));
    }

    public void commonMain(java.lang.String[] args) throws IOException {
        if (args != null) {
            for (java.lang.String string : args) {
                Debug.config("Read arg:" + string);
                if (string.equals("is_jar")) {//запуск из jar-файла
                    IS_JAR = true;
                } else if (string.equals("debug")) {//включаем дебаг
                }
            }
        }
    }

}
