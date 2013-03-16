package com.geargames.common;

import java.lang.String;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 22.06.12
 * Time: 14:43
 * платформенные настройки
 */
public abstract class Port {

    public static boolean IS_ANDROID = false;
    public static boolean IS_IPHONE = false;
    public static boolean IS_CONSOLE = false;

    public static boolean IS_DOUBLE_GRAPHIC = false;
    public static boolean IS_FOURTHIRDS_GRAPHIC = false;// 4/3, графика 2/3 от двойной
    public static boolean IS_HALF_GRAPHIC = false;// 2/3

    public static boolean OPEN_GL = false;
    private static final boolean RETINA = false;
    public static int TOUCH_ROUND = 3;//допустимая разница в пикселах между точкой нажатия и точкой отжатия для фиксирования клика

    private static int SSZ_H = 320;
    private static int SSZ_W = 480;

    public static int SCREEN_DX = 0;//смещение канваза от области вывода
    public static int SCREEN_DY = 0;

    public static int scale = 100;
    private static int scaledW;//при уменьшении масштаба рабочая поверхность увеличивается
    private static int scaledH;
    private static int rescaledW;//при уменьшении масштаба рабочая поверхность увеличивается
    private static int rescaledH;

    private static boolean isLight;//флаг облегченного порта для слабых устройств

    private static boolean isOpenGL;//Deprecated убрать в iOS

    public static int getConvertedValue(int value) {
        if (Port.port == -1) {
            new IllegalArgumentException("Error port");
        }
        if (Port.IS_DOUBLE_GRAPHIC) return value << 1;
        else if (Port.IS_FOURTHIRDS_GRAPHIC) return (value << 2) / 3;//точность дб именно такая! остаток нужно потерять
        else if (Port.IS_HALF_GRAPHIC) return (value << 1) / 3;//точность дб именно такая! остаток нужно потерять
        return value;
    }

    public static void setWH(int w, int h) {
        Port.SSZ_W = w;
        Port.SSZ_H = h;
        System.out.println("Set port wh(" + w + "," + h + ")");
    }

    public static int getW() {
        return Port.SSZ_W;
    }

    public static int getH() {
        return Port.SSZ_H;
    }

    public static int getScreenW() {//для координат которые будут обрабатываться в графиксе
        return Port.SSZ_W;
    }

    public static int getScreenH() {
        return Port.SSZ_H;
    }

    public static byte getPort() {
        return port;
    }

    public static void setPort(int port) {
        Port.IS_HALF_GRAPHIC = false;
        Port.IS_FOURTHIRDS_GRAPHIC = false;
        Port.IS_DOUBLE_GRAPHIC = false;
        Port.port = (byte)port;
        switch (port) {
            case 0:
                Port.IS_HALF_GRAPHIC = true;
                break;
            case 1:
                break;
            case 2:
                Port.IS_FOURTHIRDS_GRAPHIC = true;
                break;
            case 3:
                Port.IS_DOUBLE_GRAPHIC = true;
                break;
        }
        System.out.println("Set port " + port);
    }

    private static byte port = -1;

    public static boolean isRetina() {
        return false;
    }


    public static void setOpenGL(boolean flag) {//Deprecated
        Port.isOpenGL = flag;
    }

    public static boolean isOpenGl() {//Deprecated
        return Port.isOpenGL;
    }


    public static int getScale() {
        return Port.scale;
    }

    public static void setScale(int scale_) {
        Port.scale = scale_;
        Port.scaledW = Port.SSZ_W * 100 / Port.scale;
        Port.scaledH = Port.SSZ_H * 100 / Port.scale;
        Port.rescaledW = Port.SSZ_W * Port.scale / 100;
        Port.rescaledH = Port.SSZ_H * Port.scale / 100;
    }

    public static int getScaledW() {
        return Port.scaledW;
    }

    public static int getScaledH() {
        return Port.scaledH;
    }

    public static int getRescaledW() {
        return Port.rescaledW;
    }

    public static int getRescaledH() {
        return Port.rescaledH;
    }

    public static String getModel() {
        return "model none";
    }

    public static String getOSVersion() {
        return "os ver none";
    }

    public static long getIMEI(Object/*MIDlet*/ miDlet) {
        return 0;
    }

    public static void setLight(boolean light) {
        Port.isLight = light;
    }

    public static boolean isLight() {
        return Port.isLight;
    }


}
