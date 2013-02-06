package gg.microedition;

import gg.Affinne;
import gg.Debug;
import gg.Port;

/** Порт-wrapper класса Graphics для microedition */
public class Graphics {

    public static final int HCENTER = javax.microedition.lcdui.Graphics.HCENTER;
    public static final int VCENTER = javax.microedition.lcdui.Graphics.VCENTER;
    public static final int LEFTG = javax.microedition.lcdui.Graphics.LEFT;
    public static final int RIGHTG = javax.microedition.lcdui.Graphics.RIGHT;
    public static final int TOPG = javax.microedition.lcdui.Graphics.TOP;
    public static final int BOTTOM = javax.microedition.lcdui.Graphics.BOTTOM;
    public static final int BASELINE = javax.microedition.lcdui.Graphics.BASELINE;
    public static final int SOLID = javax.microedition.lcdui.Graphics.SOLID;
    public static final int DOTTED = javax.microedition.lcdui.Graphics.DOTTED;

    public Graphics(javax.microedition.lcdui.Graphics g) {
        graphics = g;
        fontMetrics = new FontMetrics();
    }

    public void setColor(int color) {
        graphics.setColor(color);
    }

    public void drawImage(Image image, int x, int y, int anchor) {
        graphics.drawImage(image.getImage(), x, y, anchor);
    }

    public void drawImage(Image image, int x, int y, int w, int h, int anchor) {}

    public void drawString(gg.microedition.String string, int x, int y, int anchor) {
        if (string == null) string = gg.microedition.String.valueOfC("null");
            graphics.drawString(string.toString(), x, y, anchor);
    }

    private javax.microedition.lcdui.Image[] image_cash;

    public void drawRegion(Image image, int src_x, int src_y, int w, int h, int dst_x, int dst_y, Affinne affine) {
        drawRegion(image, src_x, src_y, w, h, 0, dst_x, dst_y, 0);
    }
    public void drawRegion(Image image, int src_x, int src_y, int w, int h, int trans, int dst_x, int dst_y, int anchor) {
        if (Port.IS_BUFFERING) {
            if (image_cash == null) {
                image_cash = new javax.microedition.lcdui.Image[app.Render.EL_END];
            }
            if (image_cash[image.frame_id] == null) {
                try {
                    javax.microedition.lcdui.Image img_ = javax.microedition.lcdui.Image.createImage(image.getImage(), src_x, src_y, w, h, 0);
                    image_cash[image.frame_id] = img_;
                    //Manager.log(gg.microedition.String.valueOfC("add bufer, wh(" + w + "," + h + ")"));
                } catch (Exception e) {
                    Debug.logEx(e);
                }
            }
            graphics.drawImage(image_cash[image.frame_id], dst_x, dst_y, anchor);
        } else {
            graphics.drawRegion(image.getImage(), src_x, src_y, w, h, trans, dst_x, dst_y, anchor);
        }
    }

    public void drawLine(int i, int i1, int i2, int i3) {
        graphics.drawLine(i, i1, i2, i3);
    }

    public void drawRect(int i, int i1, int i2, int i3) {
        graphics.drawRect(i, i1, i2, i3);
    }

    public void fillRect(int i, int i1, int i2, int i3) {
        graphics.fillRect(i, i1, i2, i3);
    }

    public void setClip(int x, int y, int width, int height) {
        graphics.setClip(x, y, width, height);
    }

    public void clipRect(int x, int y, int width, int height) {
        graphics.clipRect(x, y, width, height);
    }

    public void setFont(Font font) {
        graphics.setFont(font.getFont());
    }

    public FontMetrics getFontMetrics() {
        return fontMetrics;
    }

    public int getTransparency() {
        return 0;
    }

    public void setTransparency(int value) {
    }

    public void loadTexture(Image image) {}//затычка андроид OpenGL


    public Graphics(javax.microedition.lcdui.Image image) {
        graphics = image.getGraphics();
    }

    private javax.microedition.lcdui.Graphics graphics;
    private FontMetrics fontMetrics;

}
