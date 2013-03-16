package com.geargames.common.packer;

import com.geargames.common.Graphics;

/**
 * User: kewgen
 * Date: 18.09.12
 * Time: 17:54
 */
public class PSprite extends PrototypeIndexes {

    public static final int TRANS_NONE = 0;
    public static final int TRANS_MIRROR = 1;
    public static final int TRANS_VERTICAL_MIRROR = 2;
    public static final int TRANS_HV_MIRROR = 3;

    public PSprite(int prototypesCount) {
        super(prototypesCount);
        type = com.geargames.common.Render.T_SPRITE;
    }

    @Override
    public void draw(Graphics graphics, int x, int y) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Index index = (Index) list.get(i);
            index.draw(graphics, x, y);
        }
    }

}
