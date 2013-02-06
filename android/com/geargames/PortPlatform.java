package com.geargames;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import com.geargames.common.Port;

/*
* Android platform description
* */
public abstract class PortPlatform extends Port {

    public static void init() {
        Port.IS_ANDROID = true;
        Port.IS_OPENGL = true;

        //ограничим загрузку звуков и картинок
        //разгружаем процессор
/*
        if (PortPlatform.getModel().equals("LG-P500")
                || PortPlatform.getModel().equals("LG-P350")//outOfMemory new version 2.3.7 not loaded
                || PortPlatform.getModel().equals("ST18i")//outOfMemory уменьшить экран до 320х480
                || PortPlatform.getModel().equals("MT11i")//outOfMemory
                || PortPlatform.getModel().equals("HTC Desire")//outOfMemory not loaded
                || PortPlatform.getModel().equals("U8800")//outOfMemory
                || PortPlatform.getModel().equals("GT-P7300")//outOfMemory
                || PortPlatform.getModel().equals("RMD-1020")//outOfMemory
                || PortPlatform.getModel().equals("LT18i")//outOfMemory
                || PortPlatform.getModel().equals("MID7010")//outOfMemory
                || PortPlatform.getModel().equals("LG-P705")//outOfMemory sound
                || PortPlatform.getModel().equals("Android")//outOfMemory sound
                || PortPlatform.getModel().equals("LG-P990")//outOfMemory sound
                || PortPlatform.getModel().equals("LG-P970")//outOfMemory sound
                || PortPlatform.getModel().equals("Blade")//outOfMemory sound
                || PortPlatform.getModel().equals("Vega")//outOfMemory
                || PortPlatform.getModel().equals("HTC Desire HD A9191")//outOfMemory sound
                || PortPlatform.getModel().equals("HTC Incredible S")//outOfMemory
                || PortPlatform.getModel().equals("HTC EVO 3D X515m")//outOfMemory
                || PortPlatform.getModel().equals("Liquid MT")//outOfMemory
                || PortPlatform.getModel().equals("MT27i")) {
            Port.setLight(true);
        }
*/

        if (Port.getW() < Port.getH()) {
            //неверные параметры экрана девайса
            //обычно происходит при запуске в паузе после выхода их паузы
            Debug.trace((new IllegalStateException("Error landscape size. wh(" + getW() + "," + getH() + ")")).toString());
            Port.setWH(getH(), getW());
        } else {

/*
            if (Port.getW() < 480) {
                //половинная графика
                setHalfGraphic(true);
                Debug.log("Set HALF graphics.");
            } else if (Port.getW() < 800) {
                //одинарная графика
                Debug.log("Set NORMAL graphics.");
            } else if (Port.getW() < 960 || Port.getH() < 640) {
                setFourThirdsGraphic(true);
                Debug.log("Set FOURTHIRDS_GRAPHIC graphics.");
            } else {
                setDoubleGraphic(true);
                Debug.log("Set DOUBLE_GRAPHIC graphics.");
            }

            if (PortPlatform.getModel().equals("mercury")) {
                PortPlatform.setFourThirdsGraphic(false);
            }
*/

            if (Port.getW() >= 800) Port.TOUCH_ROUND = 14;
            Debug.trace("Port.wh(" + getW() + "," + getH() + "), touchRound:" + Port.TOUCH_ROUND);

        }
        //change on 20 pix for status bar Kindle Fire
        if (PortPlatform.getModel().equals("Kindle Fire")) {//Kindle Fire
            Port.setWH(Port.getW(), Port.getH() - 20);
        }

    }


    public static boolean isDebugMode() {
        return false;
    }

    public static String getModel() {
        return Build.MODEL;
    }

    public static String getBrand() {
        return Build.BRAND;
    }

    public static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    public static int getLevelAPI() {//11 - 3.0
        return Build.VERSION.SDK_INT;//Integer.valueOf(android.os.Build.VERSION.SDK);
    }

    public static void printDeviceInformation() {
        String model = Build.MODEL;
        String device = Build.DEVICE;
        String display = Build.DISPLAY;
        String manufacturer = Build.MANUFACTURER;
        String product = Build.PRODUCT;
        String serial = "";//Build.SERIAL;

        String release = Build.VERSION.RELEASE;
        String codename = Build.VERSION.CODENAME;
        String sdk = String.valueOf(getLevelAPI());
        Debug.trace("Model:" + model
                + "\n device:" + device
                + "\n display:" + display
                + "\n manufacturer:" + manufacturer
                + "\n product:" + product
                + "\n serial:" + serial
                + "\n release:" + release
                + "\n codename:" + codename
                + "\n sdk:" + sdk
        );
    }

    private static long IMEI_OLD = 0;
    public static long getIMEI_OLD(MIDlet midlet) {
        if (PortPlatform.IMEI_OLD > 0) return PortPlatform.IMEI_OLD;
        long value = 0;
        try {
            //клиент ИД вычесляется очень криво
            TelephonyManager telephonyManager = (TelephonyManager) midlet.getSystemService(Context.TELEPHONY_SERVICE);
            String IMEI = telephonyManager.getDeviceId();
            String DEVICE_ID = null;
            if (IMEI == null) {
                DEVICE_ID = android.provider.Settings.System.getString(midlet.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                if (DEVICE_ID != null) {
                    Debug.trace("DEVICE_ID:" + DEVICE_ID);
                    if (DEVICE_ID.length() > 18) DEVICE_ID = DEVICE_ID.substring(DEVICE_ID.length() - 18);
                    String id = DEVICE_ID.substring(1);//это лишняя операция
                    id = id.replaceAll("[^0-9]", "");//а это нужно перенести до обрезания
                    value = Long.parseLong(id, 16);
                } else value = 1;
            } else {
                Debug.trace("IMEI:" + IMEI);
                if (IMEI.length() > 18) IMEI = IMEI.substring(IMEI.length() - 18);//200001552946000155264020121
                IMEI = IMEI.replaceAll("[^0-9]", "");//F200001E55294D60001E55264020121 перенести до обрезания
                if (IMEI.length() > 1) value = Long.parseLong(IMEI);//java.lang.NoSuchMethodError: java.lang.String.isEmpty
            }

        } catch (SecurityException e) {
            value = -1;
            Debug.logEx(e);
        } catch (Exception e) {
            Debug.logEx(e);
        }
        PortPlatform.IMEI_OLD = value;
        return value;
    }

    public static long getCliendID(MIDlet midlet) {
        //совокупность IMEI и DEVICE_ID
        long value = 0;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) midlet.getSystemService(Context.TELEPHONY_SERVICE);
            String IMEI = telephonyManager.getDeviceId();
            if (IMEI == null) {
                String DEVICE_ID = android.provider.Settings.System.getString(midlet.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                if (DEVICE_ID != null) {
                    Debug.trace("DEVICE_ID:" + DEVICE_ID);
                    DEVICE_ID = DEVICE_ID.replaceAll("[^0-9]", "");
                    if (DEVICE_ID.length() > 18) DEVICE_ID = DEVICE_ID.substring(DEVICE_ID.length() - 18);
                    value = Long.parseLong(DEVICE_ID, 16);
                } else value = 1;
            } else {
                Debug.trace("IMEI:" + IMEI);
                IMEI = IMEI.replaceAll("[^0-9]", "");//F200001E55294D60001E55264020121 todo если удалять буквы можем получить короткий и неуникальный ид
                if (IMEI.length() > 18) IMEI = IMEI.substring(IMEI.length() - 18);//200001552946000155264020121
                if (IMEI.length() > 1) value = Long.parseLong(IMEI);//java.lang.NoSuchMethodError: java.lang.String.isEmpty
            }

        } catch (SecurityException e) {
            value = -1;
            Debug.logEx(e);
        } catch (Exception e) {
            Debug.logEx(e);
//            WifiManager wm = (WifiManager)Ctxt.getSystemService(Context.WIFI_SERVICE);
//            wm.getConnectionInfo().getMacAddress();
        }
        return value;
    }

    public static int getCountBufferedImages() {
        if (PortPlatform.isLight()) return 1;
        return 4;
    }

    public static int SCREEN_ORIENTATION_SENSOR_LANDSCAPE = 6;//ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
    public static final int FROYO = 8;//Build.VERSION_CODES.FROYO;//8 2.2
    public static final int HONEYCOMB = 11;//Build.VERSION_CODES.HONEYCOMB;//11 3.0
    public static final int DENSITY_XHIGH = 320;//android.util.DisplayMetrics.DENSITY_XHIGH;
    public static final int DENSITY_XXHIGH = 480;//android.util.DisplayMetrics.DENSITY_XXHIGH;
}

/*
Model:LG-P500
ANDROID_ID:125327887969462891
IMEI:357484041301842
02-01 15:34:11.139: INFO/System.out(26367): device:thunderg
02-01 15:34:11.139: INFO/System.out(26367): display:GRI40
02-01 15:34:11.139: INFO/System.out(26367): manufacturer:LGE
02-01 15:34:11.139: INFO/System.out(26367): product:thunderg
02-01 15:34:11.139: INFO/System.out(26367): serial:unknown
02-01 15:34:11.139: INFO/System.out(26367): release:2.3.3
02-01 15:34:11.139: INFO/System.out(26367): codename:REL
02-01 15:34:11.139: INFO/System.out(26367): sdk:10
Memory after loading 59MB/199MB

Model:X10i
10-03 12:35:21.347: INFO/System.out(2939): device:SonyEricssonX10i
10-03 12:35:21.347: INFO/System.out(2939): display:R1AA037
10-03 12:35:21.347: INFO/System.out(2939): manufacturer:Sony Ericsson
10-03 12:35:21.347: INFO/System.out(2939): product:X10
10-03 12:35:21.347: INFO/System.out(2939): serial:
10-03 12:35:21.347: INFO/System.out(2939): release:1.6
10-03 12:35:21.347: INFO/System.out(2939): codename:Donut
10-03 12:35:21.347: INFO/System.out(2939): sdk:4

Model:GT-I9000
ANDROID_ID:384130251126571562
IMEI:358001045707318
02-03 12:27:29.140: INFO/System.out(7830): device:GT-I9000
02-03 12:27:29.140: INFO/System.out(7830): display:GINGERBREAD.XWJVK
02-03 12:27:29.140: INFO/System.out(7830): manufacturer:samsung
02-03 12:27:29.144: INFO/System.out(7830): product:GT-I9000
02-03 12:27:29.144: INFO/System.out(7830): serial:
02-03 12:27:29.144: INFO/System.out(7830): release:2.3.3
02-03 12:27:29.144: INFO/System.out(7830): codename:REL
02-03 12:27:29.144: INFO/System.out(7830): sdk:10

Model:GT-I9100
ANDROID_ID:144917248524140093
IMEI:359959040510145
        device:GT-I9100
        display:GINGERBREAD.XWKI8
        manufacturer:samsung
        product:GT-I9100
        serial:
        release:2.3.5
        codename:REL
        sdk:10

*/

