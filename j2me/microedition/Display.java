package gg.microedition;

/**
 * Port-wrapper for microedition */
public class Display {

    public static Display getDisplay(MIDlet m) {
        display_me = javax.microedition.lcdui.Display.getDisplay(m);
        if (display == null) display = new Display();
        return display;
    }

    public void setCurrent(javax.microedition.lcdui.Displayable nextDisplayable) {
        display_me.setCurrent(nextDisplayable);
    }

    public static void vibrate(int len) {
        display_me.vibrate(len);
    }


    public javax.microedition.lcdui.Displayable getCurrent() {
        return getCurrent();
    }

    public javax.microedition.lcdui.Display getDisplay() {
        return display_me;
    }

    private static javax.microedition.lcdui.Display display_me;
    private static Display display;
}
