package com.geargames.platform;

import com.geargames.common.Port;

//consol
public class PortPlatform extends Port {

    public static void init() {
        Port.IS_CONSOLE = true;
        Port.SCREEN_DX = 8;
        Port.SCREEN_DY = 30;

        //setHalfGraphic(true);

    }

    public static void setWH(int w, int h) {
        Port.setWH(w, h);
        setScale(Port.scale);//обновляем масштабируемость при изменении размеров экрана
    }

    public static long getIMEI(Object/*MIDlet*/ miDlet) {
        return 1;
    }

    public static String getModel() {
        return "console";
    }

    public static String getBrand() {
        return "microsoft";
    }

    public static String getOSVersion() {
        return "windows";
    }

    public static short getDPI() {
        return 0;
    }

}
