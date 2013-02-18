package com.geargames.awt.components;

import com.geargames.common.*;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PFont;
import com.geargames.common.packer.PObject;
import com.geargames.common.util.ArrayList;

/**
 * User: mikhail v. kutuzov
 * Компонент содержит в себе кнопу "вверх" и "вниз" для добавления приращения к содержимому label.
 */
public class PStepSpinBox extends PValueComponent  {
    private short value;
    private boolean initiated;
    private PLabel label;
    private PStepSpinButton buttonUp;
    private PStepSpinButton buttonDown;

    public PStepSpinBox(PObject prototype) {
        super(prototype);
        initiated = false;
    }

    protected void createSlotElementByIndex(IndexObject index, PObject prototype) {
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
                addActiveChild(label, index);
                break;
        }
    }

    public void draw(Graphics graphics, int x, int y) {
        if(!initiated){
            initiate();
        }
        super.draw(graphics, x, y);
    }

    private void initiate(){
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

    public void setStep(byte step){
        buttonUp.setStep(step);
        buttonDown.setStep((byte)-step);
    }

    public void setFont(PFont font){
        label.setFont(font);
    }
}
