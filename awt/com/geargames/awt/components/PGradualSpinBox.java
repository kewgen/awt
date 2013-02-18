package com.geargames.awt.components;

import com.geargames.common.*;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;

/**
 * User: mikhail v. kutuzov
 * Date: 27.12.12
 * Time: 13:06
 */
public class PGradualSpinBox extends PValueComponent {
    private PLabel label;
    private PGradualSpinButton buttonUp;
    private PGradualSpinButton buttonDown;
    private short value;
    private boolean initiated;

    public PGradualSpinBox(PObject prototype, short value) {
        super(prototype);
        this.value = value;
        initiated = false;
    }

    protected void createSlotElementByIndex(IndexObject index, PObject parentPrototype) {
        switch (index.getSlot()) {
            case 0:
                buttonDown = new PGradualSpinButton((PObject)index.getPrototype(), false);
                buttonDown.setBox(this);
                addActiveChild(buttonDown, index);
                break;
            case 1:
                buttonUp = new PGradualSpinButton((PObject)index.getPrototype(), true);
                buttonUp.setBox(this);
                addActiveChild(buttonUp, index);
                break;
            case 2:
                label = new PSimpleLabel(index);
                addPassiveChild(label, index);
                break;
        }
    }

    public void setFps(int fps) {
        buttonUp.setFps(fps);
        buttonDown.setFps(fps);
    }

    public void draw(Graphics graphics, int x, int y) {
        if (!initiated) {
            initiate();
        }
        super.draw(graphics, x, y);
    }

    private void initiate() {
        label.setData(com.geargames.common.String.valueOfI(value));
        initiated = true;
    }

    public short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
        initiated = false;
    }
}
