package com.geargames.awt.components;

import com.geargames.common.env.Environment;
import com.geargames.common.packer.PObject;
import com.geargames.common.Graphics;

/**
 * User: mikhail v. kutuzov
 * Индикатор прогресса.
 */
// ProgressBar
public class PSpriteProgressIndicator extends PObjectElement {
    private int value;
    private int cardinality;
    private int spriteIndex;

    public PSpriteProgressIndicator(PObject prototype){
        super(prototype);

        value = 0;
        spriteIndex = prototype.getIndexBySlot(0).getPrototype().getPID();
        cardinality = prototype.getIndexBySlot(1).getX();
    }

    @Override
    public void draw(Graphics graphics, int x, int y) {
        Environment.getRender().getSprite(spriteIndex + value).draw(graphics, x, y);
    }

    public int getValue() {
        return value;
    }

    public int getCardinality() {
        return cardinality;
    }

    public void setValue(int value) {
        if (value >= cardinality) {
            this.value = cardinality;
        } else
        if (value <= 0) {
            this.value = 0;
        } else {
            this.value = value;
        }
    }

    /**
     * Получить положение индикатора в процентном соотношении.
     * @return - число в процентах
     */
    // getValueAsPercentage
    public int getPercentage() {
        return value * 100 / cardinality;
    }

    /**
     * Установить новое значение индикатора, причем значение указывается в процентном соотношении.
     * @param value - число в процентах
     */
    public void setPercentage(int value) {
        if (value >= 100) {
            this.value = cardinality;
        } else
        if (value <= 0) {
            this.value = 0;
        } else {
            this.value = value * cardinality / 100;
        }
    }

}
