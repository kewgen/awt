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
    private PStepSpinButton up;
    private PStepSpinButton down;

    public PStepSpinBox(PObject prototype) {
        super(prototype);

        ArrayList indexes = prototype.getIndexes();
        for (int i = 0; i < indexes.size(); i++) {
            IndexObject index = (IndexObject) indexes.get(i);
            if (index.isSlot()) {
                switch (index.getSlot()) {
                    case 0:
                        down = new PStepSpinButton((PObject)index.getPrototype());
                        down.setBox(this);
                        addActiveChild(down, index);
                        break;
                    case 1:
                        up = new PStepSpinButton((PObject)index.getPrototype());
                        up.setBox(this);
                        addActiveChild(up, index);
                        break;
                    case 2:
                        label = new PSimpleLabel(index);
                        addActiveChild(label, index);
                        break;
                }
            }
        }
        initiated = false;
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
        up.setStep(step);
        down.setStep((byte)-step);
    }

    public void setFont(PFont font){
        label.setFont(font);
    }
}
