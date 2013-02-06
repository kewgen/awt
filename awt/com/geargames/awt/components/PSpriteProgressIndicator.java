package com.geargames.awt.components;

import com.geargames.common.packer.*;
import com.geargames.common.Graphics;

/**
 * User: mikhail v. kutuzov
 * Индикатор прогресса.
 */
public abstract class PSpriteProgressIndicator extends PObjectElement {
    private int value;
    private int cardinality;
    private int indicator;

    public PSpriteProgressIndicator(PObject prototype){
        super(prototype);

        value = 0;
        indicator = prototype.getIndexBySlot(0).getPrototype().getPID();
        cardinality = prototype.getIndexBySlot(1).getX();
    }

    public void draw(Graphics graphics, int x, int y) {
        graphics.getRender().getSprite(indicator + value).draw(graphics, x, y);
    }


    public int getValue() {
        return value;
    }

    public int getCardinality() {
        return cardinality;
    }

    public void setValue(int value) {
        if(value > cardinality){
            this.value = cardinality;
        } else {
            this.value = value;
        }
    }
}
