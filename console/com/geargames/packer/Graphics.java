package com.geargames.packer;

import com.geargames.common.Port;
import com.geargames.common.Render;
import com.geargames.common.String;
import com.geargames.common.packer.PAffine;
import com.geargames.Debug;
import com.geargames.Manager;
import com.geargames.common.packer.PFont;
import com.geargames.common.packer.PFrame;
import com.geargames.common.packer.PSprite;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;


/**
 * Порт-wrapper класса Graphics для microedition
 */
public class Graphics implements com.geargames.common.Graphics {

    public Graphics(java.awt.Graphics g) {
        graphics = g;
    }

    public void onCache(int len) {//включить кеширование картинок
        if (image_cash == null) image_cash = new java.awt.Image[len];
    }

    public void addTexture(com.geargames.common.Image image) {}

    public void drawRegion(com.geargames.common.Image image, int src_x, int src_y, int w, int h, int dst_x, int dst_y, PAffine affine) {
        if (image_cash == null) return;
        int frame_id = ((Image) image).frame_id;
        if (image_cash[frame_id] == null) {//bufering
            try {
                Image img_ = Image.createImage(w, h);
                Graphics graphics_ = img_.getGraphics();
                graphics_.drawImage(image, 0 - src_x, 0 - src_y, 0);
                image_cash[frame_id] = img_.getImage();
            } catch (IOException e) {
                Debug.logEx(e);
            }
        }

        //отрисовка без отражения
        if (affine == null) {
            graphics.drawImage(image_cash[frame_id], dst_x, dst_y, null);
            return;
        }

        //отрисовка с аффинными преобразованиями
        Graphics2D graphics2D = (Graphics2D) graphics;
        AffineTransform transformOriginal = graphics2D.getTransform();
        AffineTransform transformNew = (AffineTransform) (transformOriginal.clone());
        int dx = 0;
        int dy = 0;
        java.awt.Image awtImage = image_cash[frame_id];
        Composite compositeOriginal = graphics2D.getComposite();//сохраняем прозрачность
        if (affine.getTransparency() > 0) {
            float alpha = 1 - ((float) affine.getTransparency() / 100);
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        }
        if (affine.getRotate() != 0) {
            transformNew.rotate((float) affine.getRotate() * Math.PI / 180.0, dst_x - affine.getX(), dst_y - affine.getY());
        }
        if (affine.getScalingX() != 100 || affine.getScalingY() != 100) {
            int smooth = java.awt.Image.SCALE_SMOOTH;
            if (w + h > 350) smooth = java.awt.Image.SCALE_FAST;
            awtImage = awtImage.getScaledInstance(w * affine.getScalingX() / 100, h * affine.getScalingY() / 100, smooth);
        }
        if (affine.isHmirror()) {
            transformNew.scale(-1, 1);
            dx = (dst_x << 1) + w;
        }
        if (affine.isVmirror()) {
            transformNew.scale(1, -1);
            dy = (dst_y << 1) + h;
        }
        graphics2D.setTransform(transformNew);
        graphics2D.drawImage(awtImage, dst_x - dx, dst_y - dy, null);
        graphics2D.setTransform(transformOriginal);
        if (affine.getTransparency() > 0) {
            //graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
            graphics2D.setComposite(compositeOriginal);
        }


        //Без буферизации
//        graphics.setClip(dst_x, dst_y, w, h);
//        graphics.drawImage(image.getImage(), dst_x - src_x, dst_y - src_y, null);
//        graphics.setClip(0, 0, Port.getW(), Port.getH());
        //graphics.drawRegion(image.getImage(), src_x, src_y, w, h, mirror, dst_x, dst_y, anchor);
    }

    public void drawFrame(PFrame frame, int dst_x, int dst_y) {
        if (image_cash == null) return;
        int frame_id = frame.getBid();

        if (image_cash[frame_id] == null) {//buffering
            try {
                Image img_ = Image.createImage(frame.getWidth(), frame.getHeight());
                Graphics graphics_ = img_.getGraphics();
                graphics_.drawImage(frame.getImage(), 0 - frame.getSrcX(), 0 - frame.getSrcY(), 0);
                image_cash[frame_id] = img_.getImage();
            } catch (IOException e) {
                Debug.logEx(e);
            }
        }


        PAffine affine = frame.getAffine();
        //отрисовка без отражения
        if (affine == null) {
            if (scale == 100 && (dst_x + frame.getWidth() < 0 || dst_y + frame.getHeight() < 0 || dst_x > Port.getScaledW() || dst_y > Port.getScaledH()))
                return;
            graphics.drawImage(image_cash[frame_id], dst_x, dst_y, null);
            return;
        }

        //отрисовка с аффинными преобразованиями
        Graphics2D graphics2D = (Graphics2D) graphics;
        AffineTransform transformOriginal = graphics2D.getTransform();
        AffineTransform transformNew = (AffineTransform) (transformOriginal.clone());
        int dx = 0;
        int dy = 0;
        java.awt.Image awtImage = image_cash[frame_id];
        Composite compositeOriginal = graphics2D.getComposite();//сохраняем прозрачность
        if (affine.getTransparency() > 0) {
            float alpha = 1 - ((float) affine.getTransparency() / 100);
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        }
        if (affine.getRotate() != 0) {
            transformNew.rotate((float) affine.getRotate() * Math.PI / 180.0, dst_x - affine.getX(), dst_y - affine.getY());
        }
        if (affine.getScalingX() != 100 || affine.getScalingY() != 100) {
            int smooth = java.awt.Image.SCALE_SMOOTH;
            if (frame.getWidth() + frame.getHeight() > 350) smooth = java.awt.Image.SCALE_FAST;
            awtImage = awtImage.getScaledInstance((frame.getWidth() * affine.getScalingX()) / 100, (frame.getHeight() * affine.getScalingY()) / 100, smooth);
        }
        if (affine.isHmirror()) {
            transformNew.scale(-1, 1);
            dx = (dst_x << 1) + frame.getWidth();
        }
        if (affine.isVmirror()) {
            transformNew.scale(1, -1);
            dy = (dst_y << 1) + frame.getHeight();
        }
        graphics2D.setTransform(transformNew);
        graphics2D.drawImage(awtImage, dst_x - dx, dst_y - dy, null);
        graphics2D.setTransform(transformOriginal);
        if (affine.getTransparency() > 0) {
            graphics2D.setComposite(compositeOriginal);
        }
    }

    public void drawImage(com.geargames.common.Image image, int x, int y, int anchor) {
        if (anchor == (HCENTER | VCENTER)) {
            int w = image.getWidth();
            int h = image.getHeight();
            x -= w / 2;
            y -= h / 2;
        }
        graphics.drawImage(((Image) image).getImage(), x, y, null);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        graphics.drawLine(x1, y1, x2, y2);
    }

    public void drawRect(int x, int y, int w, int h) {
        graphics.drawRect(x, y, w, h);
    }

    public void fillRect(int x, int y, int w, int h) {
        graphics.fillRect(x, y, w, h);
    }

    public void clipRect(int x, int y, int w, int h) {
        graphics.clipRect(x, y, w, h);
    }

    public void setClip(Object object) {
        graphics.setClip(null);
    }

    public void setClip(int x, int y, int w, int h) {
        graphics.setClip(x, y, w, h);
    }

    public void resetClip() {
        graphics.setClip(0, 0, Port.getW(), Port.getH());
    }

    public void setColor(int color) {
        graphics.setColor(new Color(color));
    }


    public PFont getFont() {
        return font;
    }

    public void setFont(PFont font) {
        this.font = font;
    }

    public int getAscent() {
        if (font != null) {
            return font.getAscent();
        } else {
            return graphics.getFontMetrics().getAscent();
        }
    }

    public int getBaseLine() {
        if (font != null) {
            return font.getBaseLine();
        } else {
            return getFontSize() - getAscent();
        }
    }

    public int getFontSize() {
        if (font != null) {
            return font.getSize();
        } else {
            return graphics.getFontMetrics().getHeight();
        }
    }

    public int getWidth(char character) {
        if (font != null) {
            return font.getWidth(character);
        } else {
            return graphics.getFontMetrics().charWidth(character);
        }
    }

    public int getWidth(String characters) {
        if (font != null) {
            return font.getWidth(characters);
        } else {
            return graphics.getFontMetrics().stringWidth(characters.toString());
        }
    }

    public Render getRender() {
        return render;
    }

    public int getTransparency() {
        return transparency;
    }

    public void setTransparency(int transparency) {//0..100, 100 - полная прозрачность
        this.transparency = transparency;
        float alpha = 1 - ((float) transparency / 100);
        ((Graphics2D) graphics).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {//масштабирования 0..200, 100 - оригинал
        this.scale = scale;
        double alpha = (double) scale / 100;
        if (affineTransformOriginal == null) affineTransformOriginal = ((Graphics2D) graphics).getTransform();
        ((Graphics2D) graphics).scale(alpha, alpha);
    }

    public void dropScale() {
        scale = 100;
        ((Graphics2D) graphics).setTransform(affineTransformOriginal);
    }

    public void loadTexture(com.geargames.common.Image image) {
    }//затычка андроид OpenGL

    public com.geargames.common.Image createImage(byte[] array, int i, int data_len) throws IOException {
        return Image.createImage(array, i, data_len);
    }

    public com.geargames.common.Image createImage() {
        return new Image(null);
    }


    public Object getGLGraphics() {
        return null;
    }//затычка андроид OpenGL. Ожидание инициализации графикса

    public Graphics(java.awt.Image image) {
        graphics = image.getGraphics();
    }

    public void renderSplash(Image image, short elEnd) {//отрисовка сплеша на андроиде
    }

    public void setManager(Manager manager) {

    }

    private static java.awt.Image[] image_cash;
    private java.awt.Graphics graphics;

    private int transparency;//прозрачность при рендере на графикс
    private int scale;//масштабирование при рендере на графикс
    private AffineTransform affineTransformOriginal;
    private PFont font;

    public void setRender(com.geargames.common.Render render) {
        this.render = render;
    }

    private com.geargames.common.Render render;

    /**
     * Нарисовать строку  string по координате x:y с якорем anchor.
     *
     * @param string
     * @param x
     * @param y
     * @param anchor
     */
    public void drawString(String string, int x, int y, int anchor) {
        if (string == null) {
            return;
        }
        if (font != null) {
            drawCustomString(string, x, y, anchor);
        } else {
            drawSystemString(string, x, y, anchor);
        }
    }

    /**
     * Нарисовать системным фонтом.
     *
     * @param string
     * @param x
     * @param y
     * @param anchor
     */
    public void drawSystemString(String string, int x, int y, int anchor) {
        switch (anchor) {
            case Graphics.RIGHT:
                x -= getWidth(string);
                break;
            case Graphics.HCENTER:
                x -= getWidth(string) >> 1;
                break;
        }
        graphics.drawString(string.toString(), x, y);
    }


    /**
     * Нарисовать кастомным фонтом.
     *
     * @param string
     * @param x
     * @param y
     * @param anchor
     */
    public void drawCustomString(String string, int x, int y, int anchor) {
        switch (anchor) {
            case com.geargames.common.Graphics.HCENTER:
                x -= getWidth(string) >> 1;
                break;
            case com.geargames.common.Graphics.RIGHT:
                x -= getWidth(string);
                break;
        }
        int length = string.length();
        for (int i = 0; i < length; i++) {
            char character = string.charAt(i);
            PSprite sprite = font.getSprite(character);
            sprite.draw(this, x, y);
            x += font.getWidth(character);
        }
    }

    public int getFrameW(int o_i_id) {
        return render.getFrameW(o_i_id);
    }


    public void setGLRenderer(Object glRenderer) {}


    /**@deprecated*/
    private Font fontOld;

    /**@deprecated*/
    public com.geargames.packer.Font getFont(boolean fake) {
        return new com.geargames.packer.Font(graphics.getFont());
    }

    /**@deprecated*/
    public void setFont(com.geargames.common.Font font, boolean fake) {
        graphics.setFont(((com.geargames.packer.Font)font).getFont().getAWTFont());
    }

    /**@deprecated*/
    public void drawString(String string, int x, int y, int anchor, boolean fake) {
        if (string == null) string = String.valueOfC("null");
        switch (anchor) {
            case Graphics.RIGHT:
                x -= getFontMetrics().stringWidth(string.toString());
                break;//getFontMetrics().charWidth(string.charAt(0))
        }
        graphics.drawString(string.toString(), x, y);
    }

    /**@deprecated*/
    public com.geargames.packer.FontMetrics getFontMetrics() {
        return new com.geargames.packer.FontMetrics(graphics.getFontMetrics());
    }

    /**@deprecated*/
    public void renderObject(Object obj, int id, int x, int y) {
        ((com.geargames.common.packer.Render)render).renderObject(this, obj, id, x, y);
    }

    /**@deprecated*/
    public void renderUnitScript(int id, int x, int y, int frame, Object unit) {
        ((com.geargames.common.packer.Render)render).renderUnitScript(this, id, x, y, frame, unit);
    }

    /**@deprecated*/
    public void renderUnit(int id, int x, int y, Object unit) {
        ((com.geargames.common.packer.Render)render).renderUnit(this, id, x, y, unit);
    }

    /**@deprecated*/
    public void renderAffine(int id, int x, int y) {
        ((com.geargames.common.packer.Render)render).renderAffine(this, id, x, y);
    }

    /**@deprecated*/
    public void renderAnimation(int id, int x, int y, int tick) {
        ((com.geargames.common.packer.Render)render).renderAnimation(this, id, x, y, tick);
    }

    /**@deprecated*/
    public void renderSprite(int id, int x, int y) {
        ((com.geargames.common.packer.Render)render).renderSprite(this, id, x, y);
    }

    /**@deprecated*/
    public void renderFrame(int id, int x, int y, PAffine affine) {
        ((com.geargames.common.packer.Render)render).renderFrame(this, id, x, y, affine);
    }


//    public void setRender(com.geargames.common.Render render) {
//        this.render = render;
//    }

    /**@deprecated*/
    public void drawString(String header, int o_x, int o_y, int left, byte fontBig) {
        render.drawString(this, header, o_x, o_y, left, fontBig);
    }

    /**@deprecated*/
    public int getStringWidth(String characters) {
        return render.getStringWidth(characters);
    }


}
