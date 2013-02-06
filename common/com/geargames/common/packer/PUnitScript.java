package com.geargames.common.packer;

import com.geargames.common.Graphics;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 18.09.12
 * Time: 19:33
 */
public class PUnitScript extends PrototypeIndexes {

    public PUnitScript(int size) {
        super(size);
        type = com.geargames.common.Render.T_UNIT_SCRIPT;
    }

    public void draw(Graphics graphics, int x, int y) {
        super.draw(graphics, currentIndex, x, y);
    }

    public void draw(Graphics graphics, int position, int x, int y, Object unit) {
        Index index = (Index) list.get(position);
        ((PUnit)index.getPrototype()).draw(graphics, x + index.getX(), y + index.getY(), unit);
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    private int currentIndex;
}
