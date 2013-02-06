package com.geargames.opengl;

import com.geargames.Debug;
import com.geargames.common.packer.PAffine;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by IntelliJ IDEA.
 * User: kewgen
 * Date: 10.01.12
 * Time: 18:42
 * Создание, рендер и отрисовка GLФреймов
 */
public class GLFrame {

    public GLFrame(GLTexture texture, int x_src, int y_src, int width, int height) {

        //один раз описываем фрейм текстурными координатами
        this.texture = texture;
        this.width = width;
        this.height = height;

        float txt_w = (float) texture.getWidth();
        float txt_h = (float) texture.getHeight();

        float texX1 = x_src / txt_w;
        float texX2 = (x_src + width) / txt_w;
        float texY1 = y_src / txt_h;
        float texY2 = (y_src + height) / txt_h;

        int pos = 0;
        textureCoordinatesLocal[pos + 0] = texX1;
        textureCoordinatesLocal[pos + 1] = texY1;
        textureCoordinatesLocal[pos + 2] = texX2;
        textureCoordinatesLocal[pos + 3] = texY1;
        textureCoordinatesLocal[pos + 4] = texX1;
        textureCoordinatesLocal[pos + 5] = texY2;
        textureCoordinatesLocal[pos + 6] = texX2;
        textureCoordinatesLocal[pos + 7] = texY2;
        textureCoordinatesLocal[pos + 8] = texX1;
        textureCoordinatesLocal[pos + 9] = texY2;
        textureCoordinatesLocal[pos + 10] = texX2;
        textureCoordinatesLocal[pos + 11] = texY1;

    }

    public void render(GL10 gl, int x, int y, PAffine affinne) {
        //запись вертексов
        if (textureCur == null) textureCur = texture;
        if (textureCur != texture) {
            draw(gl);
            textureCur = texture;//фиксируем последнюю текстуру
        }

        boolean setDraw = false;
        boolean setPush = false;
        int width = this.width;
        int height = this.height;
        if (affinne != null) {
            if (affinne.getTransparency() > 0) {
                if (!setPush) {
                    draw(gl);//впереди преобразования - завершаем отрисовку
                    gl.glPushMatrix();
                    setPush = true;
                }
                float alpha = (float) (1.0 - ((float) affinne.getTransparency() / 100.0));
                gl.glColor4f(alpha, alpha, alpha, alpha);
                setDraw = true;
            }
            if (affinne.getRotate() != 0) {
                if (!setPush) {
                    draw(gl);//впереди преобразования - завершаем отрисовку
                    gl.glPushMatrix();
                    setPush = true;
                }
                gl.glTranslatef(x - affinne.getX(), y - affinne.getY(), 0);//перенос в ценр вращения
                gl.glRotatef(affinne.getRotate(), 0, 0, 1);
                x = affinne.getX();//центр осей уже в нужной точке
                y = affinne.getY();

                setDraw = true;
            }
            if (affinne.isHmirror()) {
                x += width;
                width *= -1;
            }
            if (affinne.isVmirror()) {
                y += height;
                height *= -1;
            }
            if (affinne.getScalingX() != 100 || affinne.getScalingY() != 100) {
                width = width * affinne.getScalingX() / 100;
                height = height * affinne.getScalingY() / 100;
            }
        }

        vertexBuffer.put((short) (x));
        vertexBuffer.put((short) y);
        vertexBuffer.put((short) (x + width));
        vertexBuffer.put((short) y);
        vertexBuffer.put((short) x);
        vertexBuffer.put((short) (y + height));
        vertexBuffer.put((short) (x + width));
        vertexBuffer.put((short) (y + height));
        vertexBuffer.put((short) x);
        vertexBuffer.put((short) (y + height));
        vertexBuffer.put((short) (x + width));
        vertexBuffer.put((short) y);

        textureBuffer.put(textureCoordinatesLocal);

        framesCount++;
        if (framesCount >= FRAMES_MAX) setDraw = true;
        if (setDraw) draw(gl);
        if (setPush) gl.glPopMatrix();

        int error = gl.glGetError();
        if (error != GL10.GL_NO_ERROR) {
            Debug.trace("GLFrame4.Error: " + error + ", " + toString());
            //Debug.logEx(new IllegalArgumentException("GLFrame.Error: " + error));todo закоменчено для продакшена
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "GLFrame{" +
                "texture=" + texture +
                ", height=" + height +
                ", width=" + width +
                '}';
    }

    private int width;
    private int height;
    private GLTexture texture;
    private static final int VERTEX_COUNT = 12;
    private float textureCoordinatesLocal[] = new float[VERTEX_COUNT];


    //фабрика фреймов
    public static final int FRAMES_MAX = 200;//ограничение на буфер фреймов FRAMES_MAX * 12
    static {
        //буферы дб нативными, создаём один раз при инициализации
        ByteBuffer vertexByteBuffer = ByteBuffer.allocateDirect(FRAMES_MAX * VERTEX_COUNT * 2);
        vertexByteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = vertexByteBuffer.asShortBuffer();

        ByteBuffer byteBuf = ByteBuffer.allocateDirect(FRAMES_MAX * VERTEX_COUNT * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuf.asFloatBuffer();
    }

    public static void draw(GL10 gl) {
        //отрисовка фреймов из буфера
        if (GLFrame.textureCur == null || GLFrame.framesCount == 0) {
            return;
        }
        int texture_id = GLFrame.textureCur.getTextureID();

        GLFrame.vertexBuffer.position(0);
        gl.glVertexPointer(2, GL10.GL_SHORT, 0, vertexBuffer);//FloatBuffer.wrap(vertices)); IllegalArgumentException: Must use a native order direct Buffer

        if (texture_id != -1) {
            textureBuffer.position(0);
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, texture_id);
        }

        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, VERTEX_COUNT / 2 * GLFrame.framesCount);

        int error = gl.glGetError();
        if (error != GL10.GL_NO_ERROR) {
            //Debug.logEx(new IllegalArgumentException("GLFrame.draw.Error: " + error));
        }

        clean(gl);
        drawCount++;
    }

    private static void clean(GL10 gl) {
        //послеотрисовочная очистка
        GLFrame.framesCount = 0;
        GLFrame.vertexBuffer.clear();
        GLFrame.textureBuffer.clear();
        gl.glColor4f(GLFrame.transparency, GLFrame.transparency, GLFrame.transparency, GLFrame.transparency);
    }

    public static void setTransparency(float transparency) {
        GLFrame.transparency = transparency;
    }

    private static ShortBuffer vertexBuffer;
    private static FloatBuffer textureBuffer;
    private static int framesCount;//текущее колво фреймов набитых в буфер
    private static GLTexture textureCur;
    public static int drawCount;//число отрисовываемых массивов на один кадр, статистический параметр
    public static float transparency;//текущая прозрачность
}
