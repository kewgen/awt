package com.geargames.platform;

import com.geargames.common.Port;
import com.geargames.common.String;
import com.geargames.common.logging.Debug;
import com.geargames.common.network.HTTPCounter;

import javax.swing.*;
import java.awt.Graphics;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Порт-wrapper для console
 */
public abstract class MIDlet extends JFrame implements HTTPCounter {

    protected abstract void startApp() throws IOException;

    public MIDlet() {
        initComponents();
        setVisible(true);
        setSize(Port.getW() + Port.SCREEN_DX * 2, Port.getH() + Port.SCREEN_DY + Port.SCREEN_DX);
    }

    public boolean platformRequest(String url) {
        java.awt.Desktop desktop;
        if (java.awt.Desktop.isDesktopSupported()) {
            desktop = java.awt.Desktop.getDesktop();
            if (desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
                // launch browser
                java.net.URI uri;
                try {
                    uri = new java.net.URI(url.toString());
                    desktop.browse(uri);
                } catch (Exception e) {
                    Debug.error(String.valueOfC("A platform browser has not found an uri"), e);
                }
            }
        }
        return false;
    }

    public void notifyDestroyed() {
        Debug.debug(String.valueOfC("MIDlet.notifyDestroyed"));
        System.exit(0);
    }

    public InputStream getResourceAsStream(String path) throws IOException {

        if (Main.IS_JAR) {
            //абсолютный путь - "/res.."
            //относительный - "res.." относительно папки класса
            java.lang.String p = "/res" + path;
            Debug.debug(String.valueOfC("MIDlet.getResourceAsStream, name:").concat(p));
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
        pack();
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        getManager().onStop();//завершаем процессы
    }

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
    }

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//размер канваза был изменен
        Manager manager = getManager();
        if (manager == null) {
            return;
        }
        int w = getWidth();
        int h = getHeight();
        manager.resizeScreenBuffer(w - Port.SCREEN_DX * 2, h - Port.SCREEN_DY - Port.SCREEN_DX);
    }

    private void formKeyPressed(java.awt.event.KeyEvent evt) {
        int code = evt.getKeyCode();
        switch (code) {
            default:
                getManager().keyPressed(200 + code);
                break;
        }
    }

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
    }//GEN-LAST:event_formKeyPressed


    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        lastMovePoint = evt.getPoint();
    }//GEN-LAST:event_formMouseClicked

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        getManager().onTouchEvent(com.geargames.platform.packer.Canvas.ACTION_DOWN, evt.getX(), evt.getY());
    }//GEN-LAST:event_formMousePressed

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        getManager().onTouchEvent(com.geargames.platform.packer.Canvas.ACTION_UP, evt.getX(), evt.getY());
    }//GEN-LAST:event_formMouseReleased

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        Manager manager = getManager();
        if (!evt.isControlDown()) {
            manager.onTouchEvent(com.geargames.platform.packer.Canvas.ACTION_MOVE, evt.getX(), evt.getY());
        } else {
            if (lastMovePoint != null) {
                double dx = lastMovePoint.getX() - evt.getPoint().getX();
                float delta = 0.03f;
                manager.getCanvas().onScale(dx > 0 ? -delta : delta);
            }
            lastMovePoint = evt.getPoint();
        }
    }

    @Override
    public void paint(Graphics g) {
        Manager manager = getManager();
        if (manager == null) {
            return;
        }
        com.geargames.platform.packer.Graphics graphics = new com.geargames.platform.packer.Graphics(g);
        manager.paint(graphics);
    }

    private java.awt.Point lastMovePoint;

    @Override
    public void nextKBytes() {
    }

    protected abstract Manager getManager();

}
