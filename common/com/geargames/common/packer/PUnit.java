package com.geargames.common.packer;

import com.geargames.common.Graphics;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 18.09.12
 * Time: 19:30
 */
public abstract class PUnit extends PrototypeIndexes {

    public PUnit(int prototypesCount) {
        super(prototypesCount);
        type = com.geargames.common.Render.T_UNIT;
    }

    public abstract void draw(Graphics graphics, int x, int y, Object unit);
}
