package gg.microedition;

import java.io.IOException;
import java.lang.String;

/**
 * Port-wrapper класса Image для microedition
 */
public class Image {

    public static Image createImage(int w, int h) throws IOException {
        return new Image(javax.microedition.lcdui.Image.createImage(w, h));
    }

    public static Image createImage(String patch, int w, int h) throws IOException {
        return null;
    }

    public static Image createImage(String patch) throws IOException {
        return new Image(javax.microedition.lcdui.Image.createImage(patch));
    }

    public static Image createImage(byte[] imageData, int imageOffset, int imageLength) throws IOException {
        return new Image(javax.microedition.lcdui.Image.createImage(imageData, imageOffset, imageLength));
    }

    public Image rescaleImage(int w, int h) throws IOException {
        return null;
    }

    public static Image createRGBImage(int[] rgb, int width, int height, boolean processAlpha) {
        return new Image(javax.microedition.lcdui.Image.createRGBImage(rgb, width, height, processAlpha));
    }

    public Graphics getGraphics() {
        return new Graphics(image);
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }


    private Image(javax.microedition.lcdui.Image image) {
        this.image = image;
    }

    public javax.microedition.lcdui.Image getImage() {
        return image;
    }

    public void getRGB(int[] rgbData, int offset, int scanlength, int x, int y, int width, int height) {
        image.getRGB(rgbData, offset, scanlength, x, y, width, height);
    }

    public void rebuildFromGraphics() {}//затычка для ObjC

    private javax.microedition.lcdui.Image image;
    public int frame_id;
}
