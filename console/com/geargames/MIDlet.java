package com.geargames;

import com.geargames.common.Port;
import com.geargames.common.String;
import com.geargames.common.network.HTTPCounter;
import com.geargames.common.util.HashMap;
import com.geargames.packer.Canvas;
import com.geargames.packer.Image;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Порт-wrapper для console
 */
public abstract class MIDlet extends JFrame implements HTTPCounter {

    protected abstract void startApp() throws IOException;

    protected abstract void onPause();

    protected abstract void onResume();

    public MIDlet() {
        initComponents();
        setVisible(true);
        setSize(Port.getW() + Port.SCREEN_DX * 2, Port.getH() + Port.SCREEN_DY + Port.SCREEN_DX);
    }

    public boolean platformRequest(String url, boolean inNewView) {//ConnectionNotFoundException {
        java.awt.Desktop desktop;
        if (java.awt.Desktop.isDesktopSupported()) {
            desktop = java.awt.Desktop.getDesktop();
            if (desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
                // launch browser
                java.net.URI uri;
                try {
                    uri = new java.net.URI(url.toString());
                    desktop.browse(uri);
                } catch (IOException e) {
                    Debug.logEx(e);
                } catch (java.net.URISyntaxException e) {
                    Debug.logEx(e);
                }
            }
        }
        return false;
    }

    public void notifyDestroyed() {
        Debug.trace("MIDlet.notifyDestroyed");
        System.exit(0);
    }

    public InputStream getResourceAsStream(String path) throws IOException {

        if (Logger.IS_JAR) {
            //абсолютный путь - "/res.."
            //относительный - "res.." относительно папки класса
            java.lang.String p = "/res" + path;
            Debug.trace("MIDlet.getResourceAsStream, name:" + p);
            return getClass().getResourceAsStream(p);//иЗНУТРи ЖАРА!
        } else {
            File file = new File("./res" + path);
            if (!file.exists()) return null;
            return new FileInputStream("./res" + path);//из внешнего файла
        }

    }


    private void initComponents() {
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }

            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
            }

            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }

            public void componentMoved(java.awt.event.ComponentEvent evt) {
            }
        });

        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                switch (evt.getKeyCode()) {
                    default:
                        formKeyPressed(evt);
                        break;
                }
            }
        });

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }

            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }

            public void mouseMoved(java.awt.event.MouseEvent evt) {
            }
        });


        initComponents2();

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        getManager().onStop();//завершаем процессы
    }//GEN-LAST:event_formWindowClosing

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
    }//GEN-LAST:event_formWindowClosed

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//размер канваза был изменен
        Manager manager = getManager();
        if (manager == null) {
            return;
        }
        int w = getWidth();
        int h = getHeight();
        manager.resizeScreenBuffer(w - Port.SCREEN_DX * 2, h - Port.SCREEN_DY - Port.SCREEN_DX);
    }//GEN-LAST:event_formComponentResized

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        int code = evt.getKeyCode();
        switch (code) {
            default:
                getManager().keyPressed(200 + code);
                break;
        }
    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
    }//GEN-LAST:event_formKeyPressed


    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        lastMovePoint = evt.getPoint();
    }//GEN-LAST:event_formMouseClicked

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        getManager().onTouchEvent(Canvas.ACTION_DOWN, evt.getX(), evt.getY());
    }//GEN-LAST:event_formMousePressed

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        getManager().onTouchEvent(Canvas.ACTION_UP, evt.getX(), evt.getY());
    }//GEN-LAST:event_formMouseReleased

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        Manager manager = getManager();
        if (!evt.isControlDown()/*!isPressControl*/) {
            manager.onTouchEvent(Canvas.ACTION_MOVE, evt.getX(), evt.getY());
        } else {
            if (lastMovePoint != null) {
                double dx = lastMovePoint.getX() - evt.getPoint().getX();
                float delta = 0.03f;
                manager.getCanvas().onScale(dx > 0 ? -delta : delta);
            }
            lastMovePoint = evt.getPoint();
        }
    }//GEN-LAST:event_formMouseReleased

    void initComponents2() {
        //jDesktopPane1.set(jDesktopF, javax.swing.JLayeredPane.DEFAULT_LAYER);
/*
        for (int i = 0; i < Manage.FORM_COUNT; i++) {
            if (manage.jFrameA[i] != null) {
                jDesktopPane1.set(manage.jFrameA[i], javax.swing.JLayeredPane.DEFAULT_LAYER);
            }
        }
*/

    }


    void setFormIcon() {
        try {
            InputStream stm_in;
            stm_in = getClass().getResourceAsStream("logo.gif");
            BufferedImage im = ImageIO.read(stm_in);
            setIconImage(im);
            stm_in.close();
        } catch (Exception e) {
//            manage.logException(e);
        }
    }

    public void paint(Graphics g) {
        Manager manager = getManager();
        if (manager == null) return;
            com.geargames.packer.Graphics graphicsme = new com.geargames.packer.Graphics(g);
            manager.paint(graphicsme);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JFrame jDesktopF;
    private boolean isPressControl;
    private java.awt.Point lastMovePoint;
    // End of variables declaration//GEN-END:variables

    public void updateVersion() {
    }

    public void onVibration() {
    }

    public void marketBind() {
    }

    public Object getmFacebookManager() {
        return null;
    }

    public void facebookShare() {
    }

    public FileInputStream openFileInput(java.lang.String name) {
        return null;
    }

    public void sendPay(com.geargames.common.String str) {
        try {
            platformRequest(str, true);
        } catch (Exception e) {
            Debug.logEx(e);
        }
    }

    public void progressDialogShow(com.geargames.common.String string) {
        Debug.log(string);
    }

    public void progressDialogClose() {
    }

    public void addMetrics(String metricsName, HashMap map, int uid) {
    }

    public void addErrorMetrics(Exception e) {
    }

    public com.geargames.common.String getLanguage() {
        return null;
    }

    public void setLanguage(com.geargames.common.String langCur) {}

    public boolean isDebugMode() {
        return false;
    }

    public void setContentView(Canvas canvas) {
    }//android затычка

    public void runOnUiThread(Runnable runnable) {
    }//android затычка

    public Image getSplash() {
        return null;
    }

    public boolean isSplashTimeUp() {
        return true;
    }

    public Image getImageOfSplash() {
        return null;
    }

    public void setDisplay(Canvas canvas) {
    }

    public void nextKBytes() {
    }

    public int getDPI() {
        return 120;
    }

    public String saveScreenShot(Canvas canvas, String pfp_ss) {
        return String.valueOfC("not ready");
    }

    protected abstract Manager getManager();

    protected abstract void setManager(Manager manager);


}
