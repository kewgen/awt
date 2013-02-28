package com.geargames.awt.components;

import com.geargames.common.packer.PObject;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Кнопка которая даёт родительскому PStepSpinBox приращение значения при каждом нажатии.
 */
public class PStepSpinButton extends PTouchButton {
    private int step;
    private PStepSpinBox parentBox;

    public PStepSpinButton(PObject prototype) {
        super(prototype);
        step = 1;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void setBox(PStepSpinBox box) {
        this.parentBox = box;
    }

    public void action() {
        parentBox.setValue(parentBox.getValue() + step);
    }

}
