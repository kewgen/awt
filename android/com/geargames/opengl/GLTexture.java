package com.geargames.opengl;

import android.opengl.GLUtils;
import com.geargames.Debug;
import com.geargames.common.String;
import com.geargames.packer.Image;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by IntelliJ IDEA.
 * User: kewgen
 * Date: 10.01.12
 * Time: 18:42
 * Создание и управление текстурой
 */
public class GLTexture {

    private final boolean DEBUG = true;

    public GLTexture(GL10 gl, Image image) {
        this.image = image;

        image.prepareOnTexture();
        width = image.getWidth();//размеры картинки меняются после подготовки
        height = image.getHeight();

        loadGLTexture(gl);
    }

    private int loadGLTexture(GL10 gl) {//return texture id
        // Generate one texture pointer...
        int[] textures = new int[1];
        gl.glGenTextures(1, textures, 0);
        textureID = textures[0];

        if (textureID == 0) {
            Debug.trace("Texture not gen." + toString());
        }

        // ...and bind it to our array
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureID);

        // Create Nearest Filtered Texture
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);//LINEAR - при уменьшение лучше сглаживание
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

        // Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

        int error1 = gl.glGetError();
        if (error1 != GL10.GL_NO_ERROR) {
            Debug.logEx(new IllegalArgumentException("GLTexture.Error: " + error1 + " " + toString()));
        }

        // Use the Android GLUtils to specify a two-dimensional texture image
        // from our image
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, image.getBitmap(), 0);
        int error = gl.glGetError();
        if (error != GL10.GL_NO_ERROR) {
            //Debug.logEx(new IllegalArgumentException("GLTexture.Not loaded: " + error + " " + toString()));//todo todo закоменчено для продакшена
        } else {
            if (DEBUG) Debug.trace(String.valueOfC("GLTexture.loadGLTexture.").concatC(toString()).toString());
        }
//        gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, image.getHeight(), image.getHeight(),
//              0, texture.format, GL10.GL_UNSIGNED_BYTE, texture.pixels);
        return textureID;
    }

    public void addToGLTexture(GL10 gl, Image image, int x, int y) {
        //Размещение в текстуре атласов
        // ...and bind it to our array
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureID);

        // Create Nearest Filtered Texture
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

        // Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

        GLUtils.texSubImage2D(GL10.GL_TEXTURE_2D, 0, x, y, image.getBitmap());
        if (DEBUG)
            Debug.trace("GLTexture.addToGLTexture. "+getTextureID()+", xy("+x+","+y+")");
    }

    public void delete(GL10 gl) {
        int[] textures = new int[1];
        textures[0] = textureID;
        gl.glDeleteTextures(1, textures, 0);
    }

    public int getTextureID() {
        return textureID;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public java.lang.String toString() {
        return "GLTexture{" +
                "width=" + width +
                ", height=" + height +
                ", textureID=" + textureID +
                ", image=" + image +
                '}';
    }

    private int width;
    private int height;
    private int textureID;
    private Image image;


}
