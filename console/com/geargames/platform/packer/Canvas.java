package com.geargames.platform.packer;

import com.geargames.common.Port;
import com.geargames.common.logging.Debug;
import com.geargames.common.String;
import com.geargames.platform.Manager;

/**
 * Port-wrapper for microedition
 */
public class Canvas extends java.awt.Canvas {

    public Canvas(Manager manager, int scaleMin, int scaleMax) {
        isTouchSupport = true;
        Debug.config(String.valueOfC("Canvas.Touch " + (isTouchSupport ? "is supported." : "is not supported.")));
    }

    @Override
    public void paint(java.awt.Graphics g) {
        super.paint(g);
        Debug.debug(String.valueOfC("paint"));
    }

    public void repaintStart() {//метод Android
        //repaint();
    }

    public boolean isSurfaceInited() {//флаг разрешающий отрисовку
        return true;
    }


    public int getGameAction(int keyCode) {
        return 0;//super.getGameAction(keyCode);
    }

    public int getKeyCode(int keyCode) {
        return 0;
    }

    public java.lang.String getKeyName(int keyCode) {
        return null;
    }

    public void setFullScreenMode(boolean mode) {
        //super.setFullScreenMode(mode);
    }

    public void setRenderer(Object graphics) {
    }//затычка андроид OpenGL


    @Override
    public int getWidth() {
        return Port.getW();
    }

    @Override
    public int getHeight() {
        return Port.getH();
    }


    protected void keyPressed(int key) {
    }

    protected void keyReleased(int key) {
    }

    public final static byte ACTION_DOWN = 0;
    public final static byte ACTION_UP = 1;
    public final static byte ACTION_MOVE = 2;

    protected void pointerPressed(int x, int y) {
        onTouchEvent(ACTION_DOWN, x, y);
    }

    protected void pointerReleased(int x, int y) {
        onTouchEvent(ACTION_UP, x, y);
    }

    protected void pointerDragged(int x, int y) {
        onTouchEvent(ACTION_MOVE, x, y);
    }

    public boolean onTouchEvent(int action, int x, int y) {
        return true;
    }

    private boolean isKeyAvail(int key) {
        int action = UNDEFINED_KEY;
        try {
            action = getGameAction(key);
        } catch (Exception e) {
        }
        if (action != UNDEFINED_KEY && action != 0)
            return true;
        java.lang.String name = null;
        try {
            name = getKeyName(key);
        } catch (Exception e) {
        }
        if (name == null || name.length() == 0)
            return false;
        for (int i = 0; i < name.length(); i++)
            if (name.charAt(i) < ' ')
                return false;

        return true;
    }

    public void detectKeys() {
        keysDetected = true;
    }

    public final int getGameActionMy(int key) {
        if (!keysDetected)
            return getGameAction(key);
        if (key == UNDEFINED_KEY)
            return -1;
        if (key == KEY_LEFT || key == KEY_NUM4 && !isQwertyKeyboard)
            return Canvas.EVENT_LEFT;
        if (key == KEY_RIGHT || key == KEY_NUM6 && !isQwertyKeyboard)
            return Canvas.EVENT_RIGHT;
        if (key == KEY_UP || key == KEY_NUM2 && !isQwertyKeyboard)
            return Canvas.EVENT_UP;
        if (key == KEY_DOWN || key == KEY_NUM8 && !isQwertyKeyboard)
            return Canvas.EVENT_DOWN;
        return key != KEY_OK && key != KEY_FIRE && (key != KEY_NUM5 || isQwertyKeyboard) && (key != 10 || !isQwertyKeyboard) ? -1 : Canvas.EVENT_FIRE;
    }

    public final char getQwertyChar(int keyCode) {
        char retval = '\0';
        try {
            if (getGameActionMy(keyCode) == -1 && keyCode >= 32)
                return (char) keyCode;

        } catch (Exception e) {
            Debug.error(String.valueOfC("checking QWERTY key."), e);
        }
        return retval;
    }

    public boolean isKeyValid(int key) {
        return isQwertyKeyboard && key > 0 || key >= 48 && key <= 57 || key == 42 || key == 35 || key == KEY_UP || key == KEY_DOWN || key == KEY_LEFT || key == KEY_RIGHT || key == KEY_FIRE || key == KEY_CLEAR || key == KEY_OK || key == KEY_CANCEL;
    }

    public boolean onScale(float delta) {
        //корректируем delta от текущего масштаба для ровного масштабирования
        //delta = delta - (mScaleFactor - 1L) / 1000L;
        mScaleFactor += delta;

        // Don't let the object get too small or too large.
        mScaleFactor = Math.max(SCALE_MIN, Math.min(mScaleFactor, SCALE_MAX));

        //invalidate();//нужно ли здесь перерисоваться ?
        return true;
    }

    public int getScale() {
        return (int) (mScaleFactor * 100);
    }

    public void dropScale() {
        mScaleFactor = 1.0F;
    }

    private float mScaleFactor = 1.f;
    public boolean isTouchSupport;

    private final float SCALE_MIN = 0.5f;
    private final float SCALE_MAX = 1.0f;

    public static final int UNDEFINED = -1;
    public static final int UNDEFINED_KEY = 0x80000000;

    public static int KEY_LEFT = UNDEFINED_KEY;
    public static int KEY_RIGHT = UNDEFINED_KEY;
    public static int KEY_UP = UNDEFINED_KEY;
    public static int KEY_DOWN = UNDEFINED_KEY;
    public static int KEY_FIRE = UNDEFINED_KEY;
    public static int KEY_CLEAR = UNDEFINED_KEY;
    public static int KEY_OK = UNDEFINED_KEY;
    public static int KEY_CANCEL = UNDEFINED_KEY;

    protected static boolean keysDetected = false;
    protected static boolean isQwertyKeyboard;

    public static final int EVENT_UP = java.awt.Event.UP;
    public static final int EVENT_DOWN = java.awt.Event.DOWN;
    public static final int EVENT_LEFT = java.awt.Event.LEFT;
    public static final int EVENT_RIGHT = java.awt.Event.RIGHT;
    public static final int EVENT_FIRE = java.awt.Event.ENTER;//EVENT_FIRE;
    //    public static final int GAME_A = java.awt.Event.GAME_A;
//    public static final int GAME_B = java.awt.Event.GAME_B;
//    public static final int GAME_C = java.awt.Event.GAME_C;
//    public static final int GAME_D = java.awt.Event.GAME_D;
    public static final int KEY_NUM0 = java.awt.event.KeyEvent.VK_0;
    public static final int KEY_NUM1 = java.awt.event.KeyEvent.VK_1;
    public static final int KEY_NUM2 = java.awt.event.KeyEvent.VK_2;
    public static final int KEY_NUM3 = java.awt.event.KeyEvent.VK_3;
    public static final int KEY_NUM4 = java.awt.event.KeyEvent.VK_4;
    public static final int KEY_NUM5 = java.awt.event.KeyEvent.VK_5;
    public static final int KEY_NUM6 = java.awt.event.KeyEvent.VK_6;
    public static final int KEY_NUM7 = java.awt.event.KeyEvent.VK_7;
    public static final int KEY_NUM8 = java.awt.event.KeyEvent.VK_8;
    public static final int KEY_NUM9 = java.awt.event.KeyEvent.VK_9;
    public static final int KEY_STAR = java.awt.event.KeyEvent.VK_UP;
    public static final int KEY_POUND = java.awt.event.KeyEvent.VK_DOWN;

    public void onPause() {
    }

    public void onResume() {
    }
}


