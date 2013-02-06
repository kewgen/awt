package com.geargames.opengl;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import com.geargames.Debug;
import com.geargames.common.String;
import com.geargames.common.packer.PAffine;
import com.geargames.packer.Image;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: kewgen
 * Date: 10.01.12
 * Time: 18:43
 * Управляющий Текстурами, Фреймами и их рендером, его агрегирует Graphics
 */
public abstract class GLRenderer implements GLSurfaceView.Renderer {

    protected GLRenderer(int totalFrames, int totalTextures) {
        //первичная инициализация openGL
        frames = new GLFrame[totalFrames];
        textures = new Hashtable<Integer, GLTexture>(totalTextures);
        this.totalTextures = totalTextures;
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //вызывается при создании GLПоверхности setDisplay
        GLGraphics = gl;

        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL10.GL_BLEND);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        //при создании поверхности нужно убедить, что старая задача на загрузку текстур завершена или завершить принудительно
        removeTextures(gl);
        stopTexturesLoading();
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //вызывается после создания Поверхности или изменения её размеров
        // Sets the current view port to the new size.
        backingWidth = width;
        backingHeight = height;
/*
        if (height > width || true) {//openGL xperia не знает о переключении в landscape
            backingWidth = height;
            backingHeight = width;
        }
*/
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();

        gl.glViewport(0, 0, backingWidth, backingHeight);
        gl.glOrthof(0, backingWidth, backingHeight, 0, -1.0f, 1.0f);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        GLFrame.setTransparency(1.0f);

    }

    public void onDrawFrame(GL10 gl) {
        //грузим текстуры в другом потоке и отпускаем этот
        if (!isTexturesLoaded() && !isHaveTaskOnTexturesLoading) loadTexturesInOtherThread(gl);//Thread.currentThread()

        //вся отрисовка из этого потока
        // Clears the screen and depth buffer.
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        GLFrame.drawCount = 0;
    }

    public void onPause() {//ручное уведомление о входе в паузу
        //removeTextures(getGLGraphics()); до этого могла быть вызвана функция onDrawFrame
        stopTexturesLoading();
    }

    private void stopTexturesLoading() {
        Debug.trace("GLRenderer.stopTexturesLoading");
        if (threadOfTextureLoader != null) {
            try{
                threadOfTextureLoader.interrupt();
            } catch (Exception e) {
                Debug.trace("Stop of textures loading error." + e.toString());
            }
            threadOfTextureLoader = null;
        }
    }

    protected void loadTexturesInOtherThread(GL10 gl) {
    //задача на загрузку текстур в отдельном потоке
        isHaveTaskOnTexturesLoading = true;

        removeFrames();//фреймы потеряли все взаимосвязи
        threadOfTextureLoader = new Thread(new Runnable() {//отпускаем поток openGL
            public void run() {
                loadTextures();
            }
        });
        threadOfTextureLoader.start();
    }

    protected abstract void loadTextures();//отдельный поток

    public abstract void addTexture(com.geargames.common.Image image);

    public abstract void renderSplash(Image image, int id);

    protected void removeTextures(GL10 gl) {//битмапы лежат в своём кеше, здесь очищаем только opengl память
        isHaveTaskOnTexturesLoading = false;
        for (GLTexture texture : textures.values()) {
            texture.delete(gl);
        }
        textures.clear();
    }

    public void renderFrame(Image image, int src_x, int src_y, int width, int height, int dst_x, int dst_y, PAffine affinne, int id) {//только из потока onDrawFrame

        GLTexture texture = getTexture(image.getTexture());
        if (texture == null) {
            //Debug.logEx(new NullPointerException(String.valueOfC("renderFrame.Texture is null, id:").concatI(id).toString()));//todo закоменчено для продакшена
            return;
        }

        //рендер фрейма с кешированием
        if (frames[id] == null) {
            frames[id] = new GLFrame(texture, image.getTextureDX() + src_x, image.getTextureDY() + src_y, width, height);
        }
        if (getGLGraphics() == null) return;//встречается после частых прерываний в паузу
        frames[id].render(getGLGraphics(), dst_x, dst_y, affinne);
    }

    protected void removeFrames() {
        for (int i = 0; i < frames.length; i++) {
            frames[i] = null;
        }
    }


    public void setClip(int x, int y, int w, int h) {
        finishDraw(getGLGraphics());
        int y1 = backingHeight - (y + h);
        getGLGraphics().glScissor(x, y1, w, h);
        getGLGraphics().glEnable(GL10.GL_SCISSOR_TEST);//разрешаем сетклип
    }

    public void resetClip() {
        finishDraw(getGLGraphics());
        getGLGraphics().glDisable(GL10.GL_SCISSOR_TEST);//сетклип
    }

    public void setScale(int scale) {//масштабирования 0..200, 100 - оригинал
        finishDraw(getGLGraphics());
        float scalef = (float) (scale / 100.0);
        getGLGraphics().glPushMatrix();
        getGLGraphics().glScalef(scalef, scalef, (float) 1.0);
    }

    public void dropScale() {
        finishDraw(getGLGraphics());
        getGLGraphics().glPopMatrix();
    }

    public void setTransparency(int transparency) {//0..100 - полная прозрачность
        finishDraw(getGLGraphics());
        float alpha = (float) (1.0 - ((float) transparency / 100.0));
        GLFrame.setTransparency(alpha);
        getGLGraphics().glColor4f(alpha, alpha, alpha, alpha);
    }

    public void setZoom(int x, int y, int w, int h) {
        getGLGraphics().glViewport(x, y, x + w, y + h);//область в которую зумим экран
    }

    protected void finishDraw(GL10 gl) {//завершение отрисовки
        GLFrame.draw(gl);
    }


    public GLTexture getTexture(int id) {
        return textures.get(id);
    }

    public boolean isTexturesLoaded() {
        return textures.size() == totalTextures;
    }

    public GL10 getGLGraphics() {//рабочая GL поверхность
        return GLGraphics;
    }

    int getBackingHeight() {
        return backingHeight;
    }

    int getBackingWidth() {
        return backingWidth;
    }

    @Override
    public java.lang.String toString() {
        return "GLRenderer{" +
                "GLGraphics=" + getGLGraphics().toString() +
                '}';
    }

    protected int totalTextures;//1,4,16 Не выявлено повышение производительности. На xperia не работает 1.
    protected static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_4444;//даёт несколько фпс и экономит память. На LG P500 критично. Не проверено RGB_565

    private GL10 GLGraphics;
    private static GLFrame frames[];//буфер кешируемых фреймов
    protected static Hashtable<Integer, GLTexture> textures;//буфер текстур
    private int backingHeight;
    private int backingWidth;
    protected boolean isHaveTaskOnTexturesLoading;//задача на загрузку текстуры поставлена
    protected Thread threadOfTextureLoader;//поток загрузки текстур, нужно вырубать при выходе в паузу

}
