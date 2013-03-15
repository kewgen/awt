package com.geargames.platform;

import com.geargames.common.logging.Debug;
import com.geargames.common.logging.Logger;
import com.geargames.common.String;
import com.geargames.platform.env.ConsoleEnvironment;
import com.geargames.platform.logging.ConsoleDebug;
import com.geargames.platform.logging.ConsoleLogger;

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
    }

    public void commonMain(java.lang.String[] args) throws IOException {
        if (args != null) {
            for (java.lang.String string : args) {
                Debug.config(String.valueOfC("Read arg:" + string));
                if (string.equals("is_jar")) {//запуск из jar-файла
                    IS_JAR = true;
//                } else if (string.equals("cp866")) {//кодировка лога в консоль
//                    ENCODE_CP866 = true;
//                } else if (string.equals("double")) {//двойная графика
//                    PortPlatform.setPort(3);//DoubleGraphic(true);
//                    PortPlatform.setWH(1280, 720);//480 * 2 320 * 2);
//                } else if (string.equals("fourthirds")) {//четыре-трети графика
//                    PortPlatform.setPort(2);//setFourThirdsGraphic(true);
//                    PortPlatform.setWH(480 * 4 / 3, 320 * 4 / 3);
//                    PortPlatform.setWH(800, 480);
//                } else if (string.equals("half")) {//две-трети графика
//                    PortPlatform.setPort(0);//setHalfGraphic(true);
//                    PortPlatform.setWH(480 * 2 / 3, 320 * 2 / 3);
                } else if (string.equals("debug")) {//включаем дебаг

                }
            }
        }
    }

}
