package com.geargames.common.packer;

import com.geargames.common.Graphics;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 18.09.12
 * Time: 18:39
 */
public class PAnimation extends PrototypeIndexes {

    public PAnimation(int size) {
        super(size);
        type = com.geargames.common.Render.T_ANIMATION;
    }

    public void draw(Graphics graphics, int x, int y) {
        super.draw(graphics, indexCur, x, y);
    }

    public void setIndexCur(int indexCur) {
        this.indexCur = indexCur;
    }

    private int indexCur;

}
