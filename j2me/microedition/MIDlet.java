package gg.microedition;

import app.Manager;

import java.lang.String;

import javax.microedition.io.ConnectionNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/** Порт-wrapper для microedition */
public abstract class MIDlet extends javax.microedition.midlet.MIDlet {


    protected abstract void startApp() throws javax.microedition.midlet.MIDletStateChangeException;

    protected abstract void pauseApp();

    protected abstract void destroyApp(boolean b) throws javax.microedition.midlet.MIDletStateChangeException;

    public boolean platformRequest_(String url) throws ConnectionNotFoundException {
        return super.platformRequest(url.toString());
    }

    public void notifyDestroyed_() {
        super.notifyDestroyed();
    }

    public void setDisplay(javax.microedition.lcdui.Displayable nextDisplayable) {
        Display.getDisplay(this).setCurrent(nextDisplayable);
    }

    public InputStream getResourceAsStream(String path) throws IOException {
        return getClass().getResourceAsStream(path.toString());
    }

    public void repaint() {//затычка для доп функции в консольной версии
    }

    public int system_key_code;
    protected Manager manager;

}
