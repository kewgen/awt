package com.geargames.common.packer;

/**
 * User: kewgen
 * Date: 18.09.12
 * Time: 19:32
 */
public class IndexUnit extends Index {

    public IndexUnit(Prototype iprototype, int ix, int iy, int body) {
        super(iprototype, ix, iy);
        this.body = body;
    }

    public int getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "IndexUnit{" +
                "x=" + getX() +
                ", y=" + getY() +
                ", iprototype=" + getPrototype() +
                ", body=" + body +
                '}';
    }

    private int body;
}
