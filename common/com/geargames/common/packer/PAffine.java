package com.geargames.common.packer;

import com.geargames.common.Graphics;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 18.09.12
 * Time: 19:28
 */
public class PAffine extends PrototypeIndexes {

    public PAffine(int size) {
        super(size);
        transparency = 0;
        scalingX = scalingY = 100;
        rotate = 0;
        hmirror = vmirror = false;

        type = com.geargames.common.Render.T_AFFINE;
    }

    public void draw(Graphics graphics, int x, int y) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Index index = (Index) list.get(i);
            PFrame frame = (PFrame)index.getPrototype();
            frame.draw(graphics, x + index.getX(), y + index.getY(), this);
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getTransparency() {
        return transparency;
    }

    public void setTransparency(int transparency) {
        this.transparency = transparency;
    }

    public int getScalingX() {
        return scalingX;
    }

    public void setScalingX(int scalingX) {
        this.scalingX = scalingX;
    }

    public int getScalingY() {
        return scalingY;
    }

    public void setScalingY(int scalingY) {
        this.scalingY = scalingY;
    }

    public int getRotate() {
        return rotate;
    }

    public void setRotate(int rotate) {
        this.rotate = rotate;
    }

    public boolean isHmirror() {
        return hmirror;
    }

    public void setHmirror(boolean hmirror) {
        this.hmirror = hmirror;
    }

    public boolean isVmirror() {
        return vmirror;
    }

    public void setVmirror(boolean vmirror) {
        this.vmirror = vmirror;
    }

    @Override
    public java.lang.String toString() {
        return "PAffine{" +
                "transparency=" + transparency +
                ", scalingX=" + scalingX +
                ", scalingY=" + scalingY +
                ", rotate=" + rotate +
                ", hmirror=" + hmirror +
                ", vmirror=" + vmirror +
                '}';
    }

    private int transparency;
    private int scalingX;
    private int scalingY;
    private int rotate;
    private boolean hmirror;
    private boolean vmirror;


    private int x;//по сути это смещение не аффин, а их индекса
    private int y;
}
