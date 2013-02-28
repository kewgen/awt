package com.geargames.awt.components;

import com.geargames.common.Graphics;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PFont;
import com.geargames.common.packer.PObject;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Компонент содержит в себе кнопки "вверх" и "вниз" для добавления приращения к содержимому label.
 */
public class PStepSpinBox extends PValueComponent {
    private int value;
    private boolean initiated;
    private PLabel label;
    private PStepSpinButton buttonUp;
    private PStepSpinButton buttonDown;

    public PStepSpinBox(PObject prototype) {
        this(prototype, 0);
    }

    public PStepSpinBox(PObject prototype, int value) {
        super(prototype);
        this.value = value;
        setStep(1);
        initiated = false;
    }

    protected void createSlotElementByIndex(IndexObject index, PObject parentPrototype) {
        switch (index.getSlot()) {
            case 0:
                buttonDown = new PStepSpinButton((PObject)index.getPrototype());
                buttonDown.setBox(this);
                addActiveChild(buttonDown, index);
                break;
            case 1:
                buttonUp = new PStepSpinButton((PObject)index.getPrototype());
                buttonUp.setBox(this);
                addActiveChild(buttonUp, index);
                break;
            case 2:
                label = new PSimpleLabel(index);
                addPassiveChild(label, index);
                break;
        }
    }

    public void draw(Graphics graphics, int x, int y) {
        if (!initiated) {
            initiate();
        }
        super.draw(graphics, x, y);
    }

    private void initiate() {
        label.setText(com.geargames.common.String.valueOfI(value));
        initiated = true;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        initiated = false;
    }

    public void setStep(int step) {
        buttonUp.setStep(step);
        buttonDown.setStep(-step);
    }

    public void setFont(PFont font) {
        label.setFont(font);
    }

}
