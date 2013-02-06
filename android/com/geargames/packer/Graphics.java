package com.geargames.packer;

import android.graphics.*;
import android.graphics.Canvas;
import com.geargames.Debug;
import com.geargames.PortPlatform;
import com.geargames.common.FontMetrics;
import com.geargames.common.Render;
import com.geargames.common.String;
import com.geargames.common.packer.PAffine;
import com.geargames.common.packer.PFont;
import com.geargames.common.packer.PFrame;
import com.geargames.common.packer.PManager;
import com.geargames.opengl.GLRenderer;

import java.io.IOException;

/**
 * Порт-wrapper класса Graphics для microedition
 */
public class Graphics implements com.geargames.common.Graphics {

    public void drawImage(com.geargames.common.Image image, int x, int y, int anchor) {
        Bitmap b = ((Image) image).getBitmap();
        if (b == null) return;
        if ((anchor & HCENTER) != 0) {
            x -= b.getWidth() / 2;
        } else if ((anchor & RIGHT) != 0) {
            x -= b.getWidth();
        }
        if ((anchor & VCENTER) != 0) {
            y -= b.getHeight() / 2;
        } else if ((anchor & BOTTOM) != 0) {
            y -= b.getHeight();
        }
        canvas.drawBitmap(b, x, y, paint);
    }

    public void drawImage(com.geargames.common.Image image, int x, int y, int w, int h, int anchor) {
        Bitmap b = ((Image) image).getBitmap();
        if (b == null) return;
        if ((anchor & HCENTER) != 0) {
            x -= w / 2;
        } else if ((anchor & RIGHT) != 0) {
            x -= w;
        }
        if ((anchor & VCENTER) != 0) {
            y -= h / 2;
        } else if ((anchor & BOTTOM) != 0) {
            y -= h;
        }
        canvas.drawBitmap(b, null, new RectF(x, y, x + w, y + h), paint);
    }

    public void drawString(String string, int x, int y, int anchor) {
        paint.setTextSize(16);
        paint.setFakeBoldText(true);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(string.toString(), x, y + 15, paint);
    }

    public void drawRegion(com.geargames.common.Image image, int src_x, int src_y, int w, int h, int i4, int dst_x, int dst_y, int anchor) {
        Bitmap b = ((Image) image).getBitmap();
        if ((anchor & HCENTER) != 0) {
            dst_x -= w / 2;
        } else if ((anchor & RIGHT) != 0) {
            dst_x -= w;
        }
        if ((anchor & VCENTER) != 0) {
            dst_y -= h / 2;
        } else if ((anchor & BOTTOM) != 0) {
            dst_y -= h;
        }
        canvas.drawBitmap(b, new Rect(src_x, src_y, src_x + w, src_y + h), new Rect(dst_x, dst_y, dst_x + w, dst_y + h), paint);
    }

    public void drawRegion(com.geargames.common.Image image, int src_x, int src_y, int w, int h, int dst_x, int dst_y, PAffine affine) {
        if (PortPlatform.IS_OPENGL) {
            glRenderer.renderFrame((Image) image, src_x, src_y, w, h, dst_x, dst_y, affine, ((Image) image).frame_id);
            return;
        }
        if (IS_CASHED && image_cash == null) return;
        Bitmap bitmap;
        if (!IS_CASHED) {
            if (affine == null) {
                drawRegion(image, src_x, src_y, w, h, 0, dst_x, dst_y, 0);
                return;
            }
            bitmap = ((Image) image).getBitmap();
            Matrix matrix = canvas.getMatrix();//сохраним настройки канваза
            if (affine.getTransparency() > 0) {
                //float alpha = 1 - ((float) affine.getTransparency() / 100);
                paint.setAlpha(255 - (int) (affine.getTransparency() * 2.5));
            }
            if (affine.getRotate() != 0) {
                canvas.rotate((float) affine.getRotate(), (float) dst_x, (float) dst_y);
            }
            if (affine.getScalingX() != 100 || affine.getScalingY() != 100) {
                //canvas.scale(w * affine.getScalingX() / 100, h * affine.getScalingY() / 100);
                //awtImage = awtImage.getScaledInstance(w * affine.getScalingX() / 100, h * affine.getScalingY() / 100, java.awt.Image.SCALE_SMOOTH);
            }
            if (affine.isHmirror()) {
                canvas.scale(-1, 1);
                dst_x -= 2 * dst_x + w;
            }
            if (affine.isVmirror()) {
                canvas.scale(1, -1);
                dst_y -= 2 * dst_y + h;
            }
            canvas.drawBitmap(bitmap, new Rect(src_x, src_y, src_x + w, src_y + h), new Rect(dst_x, dst_y, dst_x + w, dst_y + h), paint);
            paint.setAlpha(255);
            canvas.setMatrix(matrix);//восстановление канваза
        } else {
            bitmap = image_cash[((Image) image).frame_id];
            if (bitmap == null) {//bufering
                bitmap = Bitmap.createBitmap(((Image) image).getBitmap(), src_x, src_y, w, h);
                image_cash[((Image) image).frame_id] = bitmap;
            }
            canvas.drawBitmap(bitmap, dst_x, dst_y, paint);
        }

    }

    public void drawFrame(PFrame frame, int dst_x, int dst_y) {

    }


    public void drawLine(int x1, int y1, int x2, int y2) {
//        if (PortPlatform.isDoubleGraphic()) {
//            x1 <<= 1;
//            x2 <<= 1;
//            y1 <<= 1;
//            y2 <<= 1;
//            if (x1 == x2 || y1 == y2) {
//                if (Math.abs(x1 - x2) > Math.abs(y1 - y2)) {
//                    canvas.drawLine(x1, y1 + 1, x2 + 1, y2 + 1, paint);
//                    canvas.drawLine(x1, y1, x2 + 1, y2, paint);
//                } else {
//                    canvas.drawLine(x1 + 1, y1, x2 + 1, y2 + 1, paint);
//                    canvas.drawLine(x1, y1, x2, y2 + 1, paint);
//                }
//            } else {
//                canvas.drawLine(x1, y1, x2, y2, paint);
//            }
//        } else {
        canvas.drawLine(x1, y1, x2, y2, paint);
//        }
    }

    public void drawRect(int x, int y, int w, int h) {
        drawRectCommon(x, y, w, h, Paint.Style.STROKE);
    }

    public void fillRect(int x, int y, int w, int h) {
        drawRectCommon(x, y, w, h, Paint.Style.FILL);
    }

    private void drawRectCommon(int x, int y, int w, int h, Paint.Style style) {
        paint.setStyle(style);
//        if (!PortPlatform.isDoubleGraphic()) canvas.drawRect(x, y, x + w, y + h, paint);
//        else
        canvas.drawRect(x << 1, y << 1, (x + w) << 1, (y + h) << 1, paint);
    }

    public void setClip(int x, int y, int w, int h) {
        if (PortPlatform.IS_OPENGL) {
            glRenderer.setClip(x, y, w, h);
        } else {
//            if (!PortPlatform.isDoubleGraphic()) canvas.clipRect(x, y, x + w, y + h);//, Region.Op.REPLACE);
//            else
            canvas.clipRect(x << 1, y << 1, (x + w) << 1, (y + h) << 1);//, Region.Op.REPLACE);
        }
    }

    public void clipRect(int x, int y, int w, int h) {
        setClip(x, y, w, h);
    }

    public void resetClip() {
        if (PortPlatform.IS_OPENGL) {
            glRenderer.resetClip();
        } else {
            setClip(0, 0, PortPlatform.getW(), PortPlatform.getH());
        }
    }

    public void setColor(int color) {   //Color.BLACK
        paint = new Paint();
        paint.setColor((0xff << 24) | color);
    }

    @Override
    public void setFont(PFont font) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PFont getFont() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getAscent() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getBaseLine() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getFontSize() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getWidth(char character) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getWidth(String characters) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Render getRender() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setFont(Font font) {
//        canvas.setFont(font.getFont());
    }

    public FontMetrics getFontMetrics() {
        return null;//graphics.getFontMetrics();
    }

    public int getTransparency() {
        return transparency;
    }

    public void setTransparency(int transparency) {//0..100 - полная прозрачность
        if (PortPlatform.IS_OPENGL) {
            glRenderer.setTransparency(transparency);
        }
        this.transparency = transparency;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {//масштабирования 0..200, 100 - оригинал
        if (PortPlatform.IS_OPENGL) {
            glRenderer.setScale(scale);
        } else {
            this.scale = scale;
        }
    }

    public void dropScale() {
        if (PortPlatform.IS_OPENGL) {
            glRenderer.dropScale();
        }
        scale = 100;
    }

    private int transparency;//прозрачность при рендере на графикс
    private int scale;//масштабирование при рендере на графикс


    public void onCache(int len) {
        image_cash = new Bitmap[len];
        IS_CASHED = true;
    }

    public void addTexture(com.geargames.common.Image image) {
        if (glRenderer != null) glRenderer.addTexture(image);//поймалось после паузы - не исследовано
        else Debug.trace("glRenderer is null.");//встречается после частых прерываний в паузу
    }

    public Graphics(Image image) {
        canvas = new Canvas(image.getBitmap());
        setColor(Color.WHITE);
    }

    public Graphics(Canvas canvas) {
        this.canvas = canvas;
    }

    private boolean IS_CASHED = false;//включено кеширование картинок.(не для OpenGL) Android быстрее не работает
    private Canvas canvas;
    private Paint paint;
    private static Bitmap[] image_cash;


    public com.geargames.common.Image createImage(byte[] array, int i, int data_len) throws IOException {
        return Image.createImage(array, i, data_len);
    }

    public com.geargames.common.Image createImage() {
        return new Image(null);
    }

    @Override
    public void setFont(com.geargames.common.Font font, boolean fake) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public com.geargames.common.Font getFont(boolean fake) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setPackerManager(PManager packerManager) {
        render = packerManager;
    }

    /**@Deprecated*/
    public void renderObject(Object obj, int id, int x, int y) {
        ((com.geargames.common.packer.Render)render).renderObject(this, obj, id, x, y);
    }

    /**@Deprecated*/
    public void renderUnitScript(int id, int x, int y, int frame, Object unit) {
        ((com.geargames.common.packer.Render)render).renderUnitScript(this, id, x, y, frame, unit);
    }

    /**@Deprecated*/
    public void renderUnit(int id, int x, int y, Object unit) {
        ((com.geargames.common.packer.Render)render).renderUnit(this, id, x, y, unit);
    }

    /**@Deprecated*/
    public void renderAffine(int id, int x, int y) {
        ((com.geargames.common.packer.Render)render).renderAffine(this, id, x, y);
    }

    /**@Deprecated*/
    public void renderAnimation(int id, int x, int y, int tick) {
        ((com.geargames.common.packer.Render)render).renderAnimation(this, id, x, y, tick);
    }

    /**@Deprecated*/
    public void renderSprite(int id, int x, int y) {
        ((com.geargames.common.packer.Render)render).renderSprite(this, id, x, y);
    }

    /**@Deprecated*/
    public void renderFrame(int id, int x, int y, PAffine affine) {
        ((com.geargames.common.packer.Render)render).renderFrame(this, id, x, y, affine);
    }


    public void setRender(com.geargames.common.Render render) {
        this.render = render;
    }

    private com.geargames.common.Render render;

    /**@Deprecated*/
    public void drawString(String header, int o_x, int o_y, int left, byte fontBig) {
        render.drawString(this, header, o_x, o_y, left, fontBig);
    }

    /**@Deprecated*/
    public int getFrameW(int o_i_id) {
        return render.getFrameW(o_i_id);
    }

    /**@Deprecated*/
    public int getStringWidth(String characters) {
        return render.getStringWidth(characters);
    }


    public void setGLRenderer(GLRenderer glRenderer) {
        this.glRenderer = glRenderer;
    }

    public GLRenderer getGlRenderer() {
        return glRenderer;
    }

    private GLRenderer glRenderer;
}
