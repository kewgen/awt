package com.geargames.awt.components;

import com.geargames.common.Graphics;
import com.geargames.common.packer.Index;
import com.geargames.common.packer.PObject;

/**
 * User: mikhail v. kutuzov
 * Date: 03.12.12
 * Time: 21:11
 */
public abstract class PButton extends PObjectElement {
    private Index pushed;
    private Index popped;
    private boolean state;

    public PButton(PObject prototype) {
        super(prototype);
        pushed = prototype.getIndexBySlot(0);
        popped = prototype.getIndexBySlot(1);
        state = false;
    }

    public void draw(Graphics graphics, int x, int y) {
        if (!state) {
            popped.draw(graphics, x, y);
        } else {
            pushed.draw(graphics, x, y);
        }
    }

    public Index getPushed() {
        return pushed;
    }

    public Index getPopped() {
        return popped;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
