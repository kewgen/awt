package com.geargames.common;

import com.geargames.common.packer.PAffine;
import com.geargames.common.packer.PFont;
import com.geargames.common.packer.PFrame;
import com.geargames.common.util.ArrayChar;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 10.09.12
 * Time: 13:08
 */
public interface Graphics {
    int HCENTER = (1 << 0);
    int VCENTER = (1 << 1);
    int LEFT = (1 << 2);
    int RIGHT = (1 << 3);
    int TOP = (1 << 4);
    int BOTTOM = (1 << 5);
    int BASELINE = 6;
    int SOLID = 7;
    int DOTTED = 8;

    void drawImage(Image image, int x, int y, int anchor);

    void drawString(String string, int x, int y, int anchor);

    void drawRegion(Image image, int src_x, int src_y, int w, int h, int dst_x, int dst_y, PAffine affine);

    public void drawFrame(PFrame frame, int dst_x, int dst_y);

    void drawLine(int x1, int y1, int x2, int y2);

    void drawRect(int x, int y, int w, int h);

    void fillRect(int x, int y, int w, int h);

    void setClip(int x, int y, int w, int h);

    void clipRect(int x, int y, int w, int h);

    void resetClip();

    void setColor(int color);

    void setFont(PFont font);

    PFont getFont();

    int getAscent();

    int getBaseLine();

    int getFontSize();

    int getWidth(char character);

    int getWidth(String characters);

    int getWidth(ArrayChar characters, int position, int length);

    Render getRender();

    int getTransparency();

    void setTransparency(int transparency);

    int getScale();

    void setScale(int scale);

    void dropScale();

    void onCache(int len);//включить кеширование картинок

    void addTexture(Image image);

    Image createImage(byte[] array, int i, int data_len) throws IOException;

    Image createImage();
}
