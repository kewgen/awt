package gg;

/**
 * Created by IntelliJ IDEA.
 * User: kewgen
 * Date: 23.11.11
 * Time: 19:21
 * To change this template use File | Settings | File Templates.
 */
public class Port {

        public static boolean OPEN_GL = false;
        public static final boolean RETINA = false;
        public static boolean DOUBLE_GRAPHIC = false;
        public static final int TOUCH_ROUND = 3;
        public static final boolean IS_BUFFERING = false;

        private static int SSZ_H = 320;
        private static int SSZ_W = 480;
//    int SSZ_H = 360;//320;
//    int SSZ_W = 640;//480;

        public static String FILE_PATH = "d:/WTK/apps/pfp2/res";
        public static final int SCREEN_DX = 0;
        public static final int SCREEN_DY = 0;

        public static void initPort(int w, int h) {}

        public static void setW(int w) {
            SSZ_W = w;
        }

        public static int getW() {
            return SSZ_W;
        }

        public static void setH(int h) {
            SSZ_H = h;
        }

        public static int getScreenW() {
            return SSZ_W / (DOUBLE_GRAPHIC ? 2 : 1);
        }

        public static int getScreenH() {
            return SSZ_H / (DOUBLE_GRAPHIC ? 2 : 1);
        }

        public static int getH() {
            return SSZ_H;
        }

        private static boolean isOpenGL;
        public static void setOpenGL(boolean flag) {
            isOpenGL = flag;
        }

        public static boolean isOpenGl() {
            return isOpenGL;
        }

        public static boolean isIPhone() {
            return false;
        }

        public static boolean isConsole() {
            return false;
        }

        public static boolean isAndroid() {
            return false;
        }

        public static boolean isDoubleGraphic() {
            return false;
        }

        public static boolean isFourThirdsGraphic() {
            return false;
        }

        public static boolean isRetina() {
            return false;
        }

    public static String getModel() {
        return "j2me";
    }

    public static String getOSVersion() {
        return "j2me";
    }


}
