package com.geargames.opengl;

import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.ViewGroup;
import com.geargames.Debug;
import com.geargames.MIDlet;
import com.geargames.PortPlatform;
import com.geargames.common.String;
import com.geargames.packer.Graphics;

/** Port-wrapper for microedition */
//public class GLCanvas extends SurfaceView implements SurfaceHolder.Callback {
public class GLCanvas__ extends GLSurfaceView {//openGL

    public GLCanvas__(MIDlet midlet, int a) {
        super(midlet);
        getHolder().addCallback(this);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        setBitmapOptions();
        draw_flag = true;
        form_show = false;
        form_text = "";

        System.setProperty("http.keepAlive", "false");
    }

    public void setRenderer(android.opengl.GLSurfaceView.Renderer renderer) {
        super.setRenderer(renderer);
    }

    private void setBitmapOptions() {
        options = new BitmapFactory.Options();
        options.inDensity = android.util.DisplayMetrics.DENSITY_HIGH;
        DisplayMetrics dm = getResources().getDisplayMetrics();
        final int minDimension = Math.min(dm.widthPixels, dm.heightPixels);
        float densityDevice = minDimension / 320f * DisplayMetrics.DENSITY_MEDIUM;
        if (densityDevice >= DisplayMetrics.DENSITY_HIGH) {
            options.inDensity = DisplayMetrics.DENSITY_HIGH;
        } else if (densityDevice >= DisplayMetrics.DENSITY_MEDIUM) {
            options.inDensity = DisplayMetrics.DENSITY_MEDIUM;
        } else {
            options.inDensity = DisplayMetrics.DENSITY_LOW;
        }
    }

    private final Runnable mDrawScene = new Runnable() {
        public void run() {
            //Manager.log(String.valueOfC("GLCanvas.run"));
            drawScene();
        }
    };

    private void drawScene() {
        if (surfaceHolder != null) {
            if (form_show) {
                form_show = false;
                showTextInput();
            } else {
                if (draw_flag) {
                    try {
                        android.graphics.Canvas canvas = null;
                        try {
                            canvas = surfaceHolder.lockCanvas(null);
                            if (graphics == null) graphics = new Graphics(canvas);
                            paint(graphics);
                        } finally {
                            draw_flag = false;
                            if (canvas != null) {
                                surfaceHolder.unlockCanvasAndPost(canvas);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                process_paint = false;
                handler.removeCallbacks(mDrawScene);
            }
        }

        handler.postDelayed(mDrawScene, 50);
    }

    @Override
    protected void onDraw(android.graphics.Canvas canvas) {//не работает
        super.onDraw(canvas);
        Debug.trace("GLCanvas.onDraw");
        if (surfaceHolder != null) {
            if (graphics == null) graphics = new Graphics(canvas);
            paint(graphics);
        }
    }

    public void paint(Graphics g) {
        //Debug.log(String.valueOfC("GLCanvas.paint"));
        //g.drawColor(0, PorterDuff.Mode.CLEAR);//очистка экрана
        //throw new Error("GLCanvas.paint.error enter.");
    }

    public void repaintStart() {//запрос перерисовки
        //Debug.log(String.valueOfC("GLCanvas.repaintStart"));
        draw_flag = true;
    }

    public void repaint() {}//затычка для ObjC

    public void setFullScreenMode(boolean mode) {
//        midlet.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        midlet.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        super.surfaceChanged(holder, format, width, height);
        Debug.trace("GLCanvas.surfaceChanged");
//        Port.setW(width);
//        Port.setH(height);
//        PortPlatform.init();
        draw_flag = false;
        this.surfaceHolder = holder;
        if (!PortPlatform.IS_OPENGL) drawScene();//отрисовка на графикс
    }

    public void surfaceCreated(SurfaceHolder arg0) {
        super.surfaceCreated(arg0);
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
        super.surfaceDestroyed(arg0);
        this.surfaceHolder = null;
        if (!PortPlatform.IS_OPENGL) handler.removeCallbacks(mDrawScene);
    }

    public boolean isSurfaceInited() {//флаг разрешающий отрисовку
        return surfaceHolder != null;
    }

    void showTextInput() {

    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    // ------------------- KEYS -----------------------

    public final static byte ACTION_DOWN = MotionEvent.ACTION_DOWN;
    public final static byte ACTION_UP = MotionEvent.ACTION_UP;
    public final static byte ACTION_MOVE = MotionEvent.ACTION_MOVE;

    public boolean onTouchEvent(MotionEvent event) {//метод, который и будет обрабатывать MotionEvent'ы.
        super.onTouchEvent(event);
        //Debug.trace(null, "GLCanvas.onTouch.action:" + event.getAction() + ", code:" + event.toString());
        onTouchEvent(event.getAction(), (int) event.getX(), (int) event.getY());
        return true;//продолжаем обработку
    }

    public boolean onTouchEvent(int action, int x, int y) {
        return true;
    }

    public int getGameAction(int keyCode) {
        return 0;
    }

    public int getKeyCode(int keyCode) {
        return 0;
    }

    public com.geargames.common.String getKeyName(int keyCode) {
        return null;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //Debug.log("GLCanvas.KeyEvent.Action:" + event.getAction() + ", code:" + event.getKeyCode() + ", " + event.toString());
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_0:
                keyPorter(event.getAction(), KEY_NUM0);
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                keyPorter(event.getAction(), UP);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                keyPorter(event.getAction(), RIGHT);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                keyPorter(event.getAction(), DOWN);
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                keyPorter(event.getAction(), LEFT);
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER://энтер клавиатуры
                keyPorter(event.getAction(), FIRE);
                break;
            case KeyEvent.KEYCODE_E:
                keyPorter(event.getAction(), FIRE);
                break;
        }
        return super.dispatchKeyEvent(event);
    }

    private void keyPorter(int action, int key_code) {
        switch (action) {
            case KeyEvent.ACTION_DOWN:
                keyPressed(key_code);
                //Debug.trace(null, "GLCanvas.KeyEvent.code:" + key_code);
                break;
            case KeyEvent.ACTION_UP:
                keyReleased(key_code);
                break;
        }

    }

    protected void keyPressed(int key) {
    }

    protected void keyReleased(int key) {
    }


    private boolean isKeyAvail(int key) {
        int action = UNDEFINED_KEY;
        try {
            action = getGameAction(key);
        } catch (Exception e) {
        }
        if (action != UNDEFINED_KEY && action != 0)
            return true;
        String name = null;
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
        KEY_LEFT = LEFT;
        KEY_RIGHT = RIGHT;
        KEY_UP = UP;
        KEY_DOWN = DOWN;
        KEY_FIRE = FIRE;
        KEY_CLEAR = UNDEFINED_KEY;
        KEY_OK = UNDEFINED_KEY;
        KEY_CANCEL = UNDEFINED_KEY;

    }

    public final int getGameActionMy(int key) {
        if (!keysDetected)
            return getGameAction(key);
        if (key == UNDEFINED_KEY)
            return -1;
        if (key == KEY_LEFT || key == KEY_NUM4 && !isQwertyKeyboard)
            return GLCanvas__.LEFT;
        if (key == KEY_RIGHT || key == KEY_NUM6 && !isQwertyKeyboard)
            return GLCanvas__.RIGHT;
        if (key == KEY_UP || key == KEY_NUM2 && !isQwertyKeyboard)
            return GLCanvas__.UP;
        if (key == KEY_DOWN || key == KEY_NUM8 && !isQwertyKeyboard)
            return GLCanvas__.DOWN;
        return key != KEY_OK && key != KEY_FIRE && (key != KEY_NUM5 || isQwertyKeyboard) && (key != 10 || !isQwertyKeyboard) ? -1 : GLCanvas__.FIRE;
    }

    public final char getQwertyChar(int keyCode) {
        char retval = '\0';
        try {
            if (getGameActionMy(keyCode) == -1 && keyCode >= 32)
                return (char) keyCode;

        } catch (Exception e) {
            //Debug.log("checking QWERTY key." + e.toString());
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
    protected boolean isTouchSupport = true;

    public static final int UP = 1;
    public static final int DOWN = 6;
    public static final int LEFT = 2;
    public static final int RIGHT = 5;
    public static final int FIRE = 8;
    public static final int GAME_A = 9;
    public static final int GAME_B = 10;
    public static final int GAME_C = 11;
    public static final int GAME_D = 12;
    public static final int KEY_NUM0 = 48;
    public static final int KEY_NUM1 = 49;
    public static final int KEY_NUM2 = 50;
    public static final int KEY_NUM3 = 51;
    public static final int KEY_NUM4 = 52;
    public static final int KEY_NUM5 = 53;
    public static final int KEY_NUM6 = 54;
    public static final int KEY_NUM7 = 55;
    public static final int KEY_NUM8 = 56;
    public static final int KEY_NUM9 = 57;
    public static final int KEY_STAR = 42;
    public static final int KEY_POUND = 35;

    private Graphics graphics;
    private SurfaceHolder surfaceHolder;//управляющий поверхностью отрисовки
    private Handler handler = new Handler();
    protected boolean draw_flag;
    public static BitmapFactory.Options options;

    public boolean form_show;
    public java.lang.String form_text;
    protected boolean process_paint;

}
