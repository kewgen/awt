package com.geargames;


import com.geargames.common.env.SystemEnvironment;
import com.geargames.env.ConsoleEnvironment;

import java.io.IOException;

/** @author ewgen */
public abstract class Main extends Logger {

    /** @param args the command line arguments */
    public static boolean IS_JAR;

    protected Main() {
        SystemEnvironment systemEnvironment = SystemEnvironment.getInstance();
        systemEnvironment.setDebug(new ConsoleDebug());
        systemEnvironment.setEnvironment(ConsoleEnvironment.getInstance());
    }

    public void commonMain(String[] args) throws IOException {
        if (args != null) {
            for (String string : args) {
                log("Read arg:" + string);
                if (string.equals("is_jar")) {//запуск из жара
                    IS_JAR = true;
                } else if (string.equals("cp866")) {//кодировка лога в консоль
                    ENCODE_CP866 = true;
                } else if (string.equals("double")) {//двойная графика
                    PortPlatform.setPort(3);//DoubleGraphic(true);
                    PortPlatform.setWH(1280, 720);//480 * 2 320 * 2);
                } else if (string.equals("fourthirds")) {//четыре-трети графика
                    PortPlatform.setPort(2);//setFourThirdsGraphic(true);
                    PortPlatform.setWH(480 * 4 / 3, 320 * 4 / 3);
                    PortPlatform.setWH(800, 480);
                } else if (string.equals("half")) {//две-трети графика
                    PortPlatform.setPort(0);//setHalfGraphic(true);
                    PortPlatform.setWH(480 * 2 / 3, 320 * 2 / 3);
                } else if (string.equals("debug")) {//включаем дебаг

                }
            }
        }
    }

}
