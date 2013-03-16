package com.geargames;


import com.geargames.common.env.SystemEnvironment;
import com.geargames.common.util.Recorder;
import com.geargames.env.ConsoleEnvironment;
import com.geargames.util.ConsoleRecorder;

import java.io.File;
import java.io.IOException;

/** @author ewgen */
public abstract class Main extends Logger {

    protected Main() {
        SystemEnvironment systemEnvironment = SystemEnvironment.getInstance();
        systemEnvironment.setDebug(new ConsoleDebug());
        systemEnvironment.setEnvironment(ConsoleEnvironment.getInstance());
        File folder = new File("regolith.data.storage");
        if(folder.exists()){
            if(!folder.isDirectory()){
                return;
            }
        } else {
            folder.mkdirs();
        }
        Recorder.setRecorder(new ConsoleRecorder(folder, new File("regolith.property.storage")));
    }

    public void commonMain(String[] args) throws IOException {
        if (args != null) {
            for (String string : args) {
                log("Read arg:" + string);
                if (string.equals("cp866")) {//кодировка лога в консоль
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
