package com.geargames.packer;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.Canvas;
import com.geargames.Debug;
import com.geargames.MIDlet;
import com.geargames.Manager;
import com.geargames.common.String;

import java.io.IOException;
import java.io.InputStream;

import static android.graphics.BitmapFactory.*;

/** Port-wrapper класса Image для microedition */
public class Image implements com.geargames.common.Image {

    public Image(Bitmap bitmap) {
        setBitmap(bitmap);
        texture = 0;
    }

    public static Image createImage(int w, int h) throws IOException {
        //Manager.log("Image.createImage(" + w + "," + h + ")");
        return new Image(Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444));//заменить на ARGB_4444   ARGB_8888
    }

    public static Image createImage(String path, MIDlet midlet) throws IOException {
        InputStream is = midlet.getResourceAsStream(path);
        Bitmap bitmap = decodeStream(is);
        is.close();
        return new Image(bitmap);
    }

    public static Image createImage(Resources resources, int id) throws IOException {
        //Manager.log("Image.createImage:" + path);
        Image image = new Image(null);
        image.setBitmap(decodeResource(resources, id, com.geargames.packer.Canvas.getOptions(resources)));
//        image.setBitmap(decodeResource(resources, id));
        return image;
    }

    public static Image createImage(String path, int w, int h) throws IOException {
        Image img = createImage(path, null);
        img.setBitmap(Bitmap.createScaledBitmap(img.bitmap, w, h, true));
        return img;
    }

    public static Image createImage(byte[] imageData, int imageOffset, int imageLength) throws IOException {
        //Manager.log("Image.createImage, from:" + imageOffset + ", len:" + imageLength);

//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;//memory optimize recomendation
//        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, imageOffset, imageLength, options);
        Bitmap bitmap = decodeByteArray(imageData, imageOffset, imageLength);
        return new Image(bitmap);
    }

    public static Image createImageFromAssets(Context context, String fileName) {
        AssetManager manager = context.getAssets();
        InputStream is;
        try{
            is = manager.open(fileName.toString());
        } catch (IOException e) {
            Debug.logEx(e);
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return new Image(bitmap);
    }

    public Image getRescaledImage(int w, int h) throws IOException {
        Bitmap bitmap = Bitmap.createScaledBitmap(this.bitmap, w, h, true);
        return new Image(bitmap);
    }

    public static Image createRGBImage(int[] rgb, int width, int height, boolean processAlpha) {
        return new Image(Bitmap.createBitmap(rgb, width, height, Bitmap.Config.ARGB_8888));
    }

    public Image getSubImage(int x, int y, int w, int h) {
        Bitmap bitmap_ = Bitmap.createBitmap(bitmap, x, y, w, h);
        return new Image(bitmap_);
    }

    public Graphics getGraphics() {
        return new Graphics(this);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void getRGB(int[] rgbData, int offset, int scanlength, int x, int y, int width, int height) {
        bitmap.getPixels(rgbData, offset, scanlength, x, y, width, height);
    }

    public void rebuildFromGraphics() {
    }//затычка для ObjC

    public int getTexture() {
        return texture;
    }

    public void prepareOnTexture() {//подготовка к текстуре
        if (getWidth() == getHeight() && (getWidth() == 256 || getWidth() == 512 || getWidth() == 1024 || getWidth() == 2048)) return;
        if (getWidth() < 256) {
            Debug.logEx(new IllegalArgumentException("Function not ready for w < 256. " + toString()));
        } else if (getWidth() < 512) {
            int w = 512;
            int h = 512;
            resizeCanvas(w, h);
        } else if (getWidth() < 1024) {//размеры дб кратны степени 2
            int w = 1024;
            int h = 1024;
            resizeCanvas(w, h);
        } else if (getWidth() < 2048) {//размеры дб кратны степени 2
            int w = 2048;
            int h = 2048;
            resizeCanvas(w, h);
        } else {
            Debug.logEx(new IllegalArgumentException("Function not ready!" + toString()));
        }
    }

    public void resizeCanvas(int w, int h) {//изменение размеров подкладки без изм разм картинки
        Bitmap newbitmap = Bitmap.createBitmap(w, h, bitmap.getConfig());
        Canvas canvas = new Canvas(newbitmap);
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        canvas.drawBitmap(bitmap, rect, rect, new Paint());
        recycle();
        setBitmap(newbitmap);
    }

    public void resize(int w, int h) {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
        recycle();
        setBitmap(resizedBitmap);
    }

    public void resize_(int w, int h) {//скале картинки, тяжелая альтернатива
        Bitmap newbitmap = Bitmap.createBitmap(w, h, bitmap.getConfig());
        Canvas canvas = new Canvas(newbitmap);
        Rect rectFrom = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Rect rectTo = new Rect(0, 0, w, h);
        canvas.drawBitmap(bitmap, rectFrom, rectTo, new Paint());
        recycle();
        setBitmap(newbitmap);
    }

    public void setTexture(int texture) {//привязка к текстуре
        this.texture = texture;
    }

    public void recycle() {
        if (bitmap == null) return;
        bitmap.recycle();
        setBitmap(null);
    }

    public int getTextureDX() {
        return textureDX;
    }

    public void setTextureDX(int textureDX) {
        this.textureDX = textureDX;
    }

    public int getTextureDY() {
        return textureDY;
    }

    public void setTextureDY(int textureDY) {
        this.textureDY = textureDY;
    }

    public boolean isCreated() {//ресурс хранится в памяти
        return bitmap != null;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        if (bitmap == null) return;
        width = bitmap.getWidth();
        height = bitmap.getHeight();
    }

    public java.lang.String toString() {
        return "Image{" +
                "bitmap=" + bitmap +
                "wh=(" + getWidth() + "," + getHeight() + ")" +
                ", texture=" + texture +
                '}';
    }

    private Bitmap bitmap;
    public int frame_id;
    public int texture;//ид текстуры на которой лежит картинка, записываем при ините картинки
    private int textureDX;//смещение картинки на текстуре
    private int textureDY;//смещение картинки на текстуре
    private int width;
    private int height;

}
