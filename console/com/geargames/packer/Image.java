package com.geargames.packer;

import com.geargames.MIDlet;
import com.geargames.Manager;
import com.geargames.common.String;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Port-wrapper класса Image для microedition
 */
public class Image implements com.geargames.common.Image {

    public static Image createImage(int w, int h) throws IOException {
        if (w == 0 || h == 0) return null;
        return new Image(new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB));
    }

    public static Image createImage(String path, MIDlet midlet) throws IOException {
        InputStream is;
        is = midlet.getResourceAsStream(path);
        Image image1 = new Image(ImageIO.read(is));
        is.close();
        return image1;
    }

    public static Image createImage(byte[] imageData, int imageOffset, int imageLength) throws IOException {
        if (imageOffset != 0 || imageLength != imageData.length)
            throw new IOException("gg.Image.createImage.Error data size.");
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        Image image1 = new Image(ImageIO.read(bais));
        bais.close();
        return image1;
    }

    public Image getSubImage(int x, int y, int w, int h) {
        BufferedImage bufferedImage_ = image.getSubimage(x, y, w, h);
        return new Image(bufferedImage_);
    }

    public Image getRescaledImage(int w, int h) {
        BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        bufferedImage.getGraphics().drawImage(image.getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
        //лиший раз создаём объект
        return new Image(bufferedImage);
    }

    public void recycle() {
        image = null;
    }

    public Graphics getGraphics() {
        return new Graphics(image);
    }

    public int getWidth() {
        return image.getWidth(null);
    }

    public int getWidth(ImageObserver observer) {
        return image.getWidth(observer);
    }

    public int getHeight() {
        return image.getHeight(null);
    }

    public int getHeight(ImageObserver observer) {
        return image.getHeight(observer);
    }

    public ImageProducer getSource() {
        return null;
    }

    public Object getProperty(String name, ImageObserver observer) {
        return null;
    }


    public Image(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public boolean isCreated() {
        return image != null;
    }

    public void resizeCanvas(int w, int h) {

    }

    public void rebuildFromGraphics() {
    }//затычка для ObjC

    @Override
    public java.lang.String toString() {
        return "Image{" +
                "image=" + image +
                '}';
    }

    private BufferedImage image;
    public int frame_id;
}
