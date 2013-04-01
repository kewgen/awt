package com.geargames.platform.packer;

import com.geargames.platform.Manager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Image extends com.geargames.common.Image {

    public boolean isLocked() {
        return false;
    }

    public static Image createImage(int w, int h) {
        if (w == 0 || h == 0) return null;
        return new Image(new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB));
    }

    public static Image createImage(String path, Manager manager) throws IOException {
        InputStream is;
        is = manager.getMidlet().getResourceAsStream(path);
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

    @Override
    public Image getRescaledImage(int w, int h) {
        BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        bufferedImage.getGraphics().drawImage(image.getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
        //лиший раз создаём объект
        return new Image(bufferedImage);
    }

    @Override
    public void recycle() {
        image = null;
    }

    public com.geargames.platform.packer.Graphics getGraphics() {
        return new com.geargames.platform.packer.Graphics(image);
    }

    @Override
    public int getWidth() {
        return image.getWidth(null);
    }

    public int getWidth(ImageObserver observer) {
        return image.getWidth(observer);
    }

    @Override
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

    @Override
    public boolean isCreated() {
        return image != null;
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
