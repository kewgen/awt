package gg.microedition;

import gg.Debug;

import java.io.IOException;
import java.io.InputStream;

/**
 * Port-wrapper for microedition
 */
public abstract class Canvas extends javax.microedition.lcdui.Canvas {

        protected boolean draw_flag;
	protected boolean isTouchSupport;
    protected boolean process_paint;
    public Canvas(MIDlet miDlet, int a) {
        setFullScreenMode(true);
        isTouchSupport = hasPointerEvents();
        log(String.valueOfC("Canvas.Touch ").concatC((isTouchSupport ? "is supported." : "is not supported.")));
    }

    public void paint(javax.microedition.lcdui.Graphics g) {
            paint(new Graphics(g));
    }

    private Graphics graphics;

    public void paint(Graphics g) {
        throw new Error(String.valueOfC("Canvas.paint.error enter."));
    }

    public void repaintStart() {
        repaint();
    }



    public InputStream getResourceAsStream(java.lang.String path) throws IOException {
        return getClass().getResourceAsStream(path.toString());
    }

    public int getGameAction(int keyCode) {
        return super.getGameAction(keyCode);
    }

    public int getKeyCode(int keyCode) {
        return 0;
    }

    public java.lang.String getKeyName(int keyCode) {
        return null;
    }

    public void setFullScreenMode(boolean mode) {
        super.setFullScreenMode(mode);
    }

    protected void setRenderer(Graphics graphics) {}//затычка андроид OpenGL


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

    protected void detectKeys() {
        for (int keyCode = -256; keyCode < 256; keyCode++)
            if ((keyCode < 48 || keyCode > 57) && keyCode != 42 && keyCode != 35) {
                int action = UNDEFINED_KEY;
                try {
                    action = getGameAction(keyCode);
                } catch (Exception e) {
                }
                int absKeyCode = Math.abs(keyCode);
                switch (action) {
                    case 3:
                    case 4:
                    case 7:
                    default:
                        break;

                    case 1:
                        if (KEY_UP == UNDEFINED_KEY || absKeyCode < Math.abs(KEY_UP))
                            KEY_UP = keyCode;
                        break;

                    case 6:
                        if (KEY_DOWN == UNDEFINED_KEY || absKeyCode < Math.abs(KEY_DOWN))
                            KEY_DOWN = keyCode;
                        break;

                    case 2:
                        if (KEY_LEFT == UNDEFINED_KEY || absKeyCode < Math.abs(KEY_LEFT))
                            KEY_LEFT = keyCode;
                        break;

                    case 5:
                        if (KEY_RIGHT == UNDEFINED_KEY || absKeyCode < Math.abs(KEY_RIGHT))
                            KEY_RIGHT = keyCode;
                        break;

                    case 8:
                        if (KEY_FIRE == UNDEFINED_KEY || absKeyCode < Math.abs(KEY_FIRE))
                            KEY_FIRE = keyCode;
                        break;
                }
            }

        if (KEY_UP == UNDEFINED_KEY)
            KEY_UP = -1;
        if (KEY_DOWN == UNDEFINED_KEY)
            KEY_DOWN = -2;
        if (KEY_LEFT == UNDEFINED_KEY)
            KEY_LEFT = -3;
        if (KEY_RIGHT == UNDEFINED_KEY)
            KEY_RIGHT = -4;
        if (KEY_UP == -59 && KEY_DOWN == -60 && KEY_LEFT == -61 && KEY_RIGHT == -62) {
            KEY_OK = -4;
            KEY_CANCEL = -1;
            KEY_FIRE = -26;
            KEY_CLEAR = -8;
        } else if (Math.abs(KEY_UP) == 1 && Math.abs(KEY_DOWN) == 6 && Math.abs(KEY_LEFT) == 2 && Math.abs(KEY_RIGHT) == 5 && Math.abs(KEY_FIRE) == 20) {
            int factor = KEY_UP != 1 ? -1 : 1;
            KEY_OK = 21 * factor;
            KEY_CANCEL = 22 * factor;
            KEY_CLEAR = 8 * factor;
        } else {
            boolean ismpp;
            try {
                ismpp = getGameAction(38) == 1 && getGameAction(40) == 6 && getGameAction(37) == 2 && getGameAction(39) == 5 && getGameAction(10) == 8;
            } catch (Exception e) {
                ismpp = false;
            }
            if (ismpp) {
                KEY_OK = -6;
                KEY_CANCEL = -7;
                KEY_FIRE = 10;
                KEY_CLEAR = -8;
                KEY_UP = 38;
                KEY_DOWN = 40;
                KEY_LEFT = 37;
                KEY_RIGHT = 39;
            } else {
                int keyFire = UNDEFINED_KEY;
                try {
                    keyFire = getKeyCode(8);
                } catch (Exception e) {
                }
                if (keyFire != UNDEFINED_KEY && keyFire != 0 && (keyFire < 48 || keyFire > 57) && keyFire != 42 && keyFire != 35 && (KEY_FIRE == UNDEFINED_KEY || Math.abs(keyFire) < Math.abs(KEY_FIRE)))
                    KEY_FIRE = keyFire;
                else if (KEY_FIRE == UNDEFINED_KEY)
                    KEY_FIRE = -5;
                if (isKeyAvail(-6) || isKeyAvail(-7)) {
                    KEY_OK = -6;
                    KEY_CANCEL = -7;
                } else if (isKeyAvail(-21) && isKeyAvail(-22)) {
                    KEY_OK = -21;
                    KEY_CANCEL = -22;
                } else if (isKeyAvail(21) && isKeyAvail(22)) {
                    KEY_OK = 21;
                    KEY_CANCEL = 22;
                }
                KEY_CLEAR = -8;
                boolean clearDefined = KEY_CLEAR != KEY_DOWN && KEY_CLEAR != KEY_UP && KEY_CLEAR != KEY_LEFT && KEY_CLEAR != KEY_RIGHT && KEY_CLEAR != KEY_FIRE && isKeyAvail(KEY_CLEAR);
                if (!clearDefined && (isKeyAvail(-16) || isKeyAvail(-204))) {
                    if (isKeyAvail(-16))
                        KEY_CLEAR = -16;
                    else
                        KEY_CLEAR = -204;
                    if (isKeyAvail(-202))
                        KEY_OK = -202;
                    else
                        KEY_OK = -6;
                    if (isKeyAvail(-203))
                        KEY_CANCEL = -203;
                    else
                        KEY_CANCEL = -7;
                }
                if (KEY_OK == UNDEFINED_KEY)
                    KEY_OK = -6;
                if (KEY_CANCEL == UNDEFINED_KEY)
                    KEY_CANCEL = -7;
            }
        }
        isQwertyKeyboard = false;
        try {
            java.lang.String bsName = getKeyName(8);
            java.lang.String tabName = getKeyName(9);
            if (bsName == null || tabName == null)
                throw new Exception();
            if (bsName.equals(tabName))
                throw new Exception();
            for (int i = bsName.length() - 1; i >= 0; i--)
                if (bsName.charAt(i) < ' ')
                    throw new Exception();

            for (int i = tabName.length() - 1; i >= 0; i--)
                if (tabName.charAt(i) < ' ')
                    throw new Exception();

            isQwertyKeyboard = true;
        } catch (Exception e) {
        }
        keysDetected = true;
    }

    public final int getGameActionMy(int key) {
        if (!keysDetected)
            return getGameAction(key);
        if (key == UNDEFINED_KEY)
            return -1;
        if (key == KEY_LEFT || key == KEY_NUM4 && !isQwertyKeyboard)
            return Canvas.LEFT;
        if (key == KEY_RIGHT || key == KEY_NUM6 && !isQwertyKeyboard)
            return Canvas.RIGHT;
        if (key == KEY_UP || key == KEY_NUM2 && !isQwertyKeyboard)
            return Canvas.UP;
        if (key == KEY_DOWN || key == KEY_NUM8 && !isQwertyKeyboard)
            return Canvas.DOWN;
        return key != KEY_OK && key != KEY_FIRE && (key != KEY_NUM5 || isQwertyKeyboard) && (key != 10 || !isQwertyKeyboard) ? -1 : Canvas.FIRE;
    }

    public final char getQwertyChar(int keyCode) {
        char retval = '\0';
        try {
            if (getGameActionMy(keyCode) == -1 && keyCode >= 32)
                return (char) keyCode;

        } catch (Exception e) {
            Debug.trace(gg.microedition.String.valueOfC("checking QWERTY key.").concatC(e.toString()));
        }
        return retval;
    }

    protected boolean isKeyValid(int key) {
        return isQwertyKeyboard && key > 0 || key >= 48 && key <= 57 || key == 42 || key == 35 || key == KEY_UP || key == KEY_DOWN || key == KEY_LEFT || key == KEY_RIGHT || key == KEY_FIRE || key == KEY_CLEAR || key == KEY_OK || key == KEY_CANCEL;
    }

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

    public static final int UP = javax.microedition.lcdui.Canvas.UP;
    public static final int DOWN = javax.microedition.lcdui.Canvas.DOWN;
    public static final int LEFT = javax.microedition.lcdui.Canvas.LEFT;
    public static final int RIGHT = javax.microedition.lcdui.Canvas.RIGHT;
    public static final int FIRE = javax.microedition.lcdui.Canvas.FIRE;
    public static final int GAME_A = javax.microedition.lcdui.Canvas.GAME_A;
    public static final int GAME_B = javax.microedition.lcdui.Canvas.GAME_B;
    public static final int GAME_C = javax.microedition.lcdui.Canvas.GAME_C;
    public static final int GAME_D = javax.microedition.lcdui.Canvas.GAME_D;
    public static final int KEY_NUM0 = javax.microedition.lcdui.Canvas.KEY_NUM0;
    public static final int KEY_NUM1 = javax.microedition.lcdui.Canvas.KEY_NUM1;
    public static final int KEY_NUM2 = javax.microedition.lcdui.Canvas.KEY_NUM2;
    public static final int KEY_NUM3 = javax.microedition.lcdui.Canvas.KEY_NUM3;
    public static final int KEY_NUM4 = javax.microedition.lcdui.Canvas.KEY_NUM4;
    public static final int KEY_NUM5 = javax.microedition.lcdui.Canvas.KEY_NUM5;
    public static final int KEY_NUM6 = javax.microedition.lcdui.Canvas.KEY_NUM6;
    public static final int KEY_NUM7 = javax.microedition.lcdui.Canvas.KEY_NUM7;
    public static final int KEY_NUM8 = javax.microedition.lcdui.Canvas.KEY_NUM8;
    public static final int KEY_NUM9 = javax.microedition.lcdui.Canvas.KEY_NUM9;
    public static final int KEY_STAR = javax.microedition.lcdui.Canvas.KEY_STAR;
    public static final int KEY_POUND = javax.microedition.lcdui.Canvas.KEY_POUND;



    // ------------LOGGING----------------
    public static void log(String msg) {
        Debug.trace(msg);
    }

    public static void logEx(Exception ex) {
        Debug.logEx(ex);
    }

}


