package com.geargames.common.packer;

import com.geargames.common.Graphics;
import com.geargames.common.Image;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 18.09.12
 * Time: 17:52
 */
public class PFrame extends Prototype {

    public PFrame(int srcX, int srcY, int width, int height) {
        this.srcX = srcX;
        this.srcY = srcY;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics g, int x, int y) {
        draw(g, x, y, null);
    }

    public void draw(Graphics g, int x, int y, PAffine affine) {
        this.affine = affine;
        if (g == null || image == null) {
            return;
        }
        g.drawFrame(this, x, y);
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getSrcX() {
        return srcX + srcDX;
    }

    public int getSrcY() {
        return srcY + srcDY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public PAffine getAffine() {
        return affine;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public Image getImage() {
        return image;
    }

    public void setTransform(byte transform) {
        this.transform = transform;
    }

    public byte getTransform() {
        return transform;
    }

    public void setSrcDY(int srcDY) {
        this.srcDY = srcDY;
    }

    public void setSrcDX(int srcDX) {
        this.srcDX = srcDX;
    }

    public String toString() {
        return "PFrame{" +
                "pid=" + pid +
                ", x=" + srcX +
                ", y=" + srcY +
                ", width=" + width +
                ", height=" + height +
                ", image=" + image +
                '}';
    }

    private int srcX;
    private int srcY;
    protected int width;
    protected int height;
    private byte transform;
    private Image image;
    protected PAffine affine;//возможность навешивать аффиные преобразования, по умолчанию null

    private int bid;//идентификатор для буферизации, позволяет вводить дополнительные фреймы
    private int srcDX;//дополнительные смещения на источнике фрейма - имейдже, позволяет рендить тот же фрейм из других координат
    private int srcDY;
}
