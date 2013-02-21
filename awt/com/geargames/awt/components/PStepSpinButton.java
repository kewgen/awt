package com.geargames.awt.components;

import com.geargames.common.packer.PObject;

/**
 * User: mikhail v. kutuzov
 * Кнопка которая даёт подчинённом PStepSpinBox приращение значения при каждом нажатии.
 */
public class PStepSpinButton extends PTouchButton {
    private byte step;
    private PStepSpinBox parentBox;

    public PStepSpinButton(PObject prototype) {
        super(prototype);
    }

    public void setStep(byte step) {
        this.step = step;
    }

    public void setBox(PStepSpinBox box) {
        this.parentBox = box;
    }

    public void action() {
        parentBox.setValue((short)(parentBox.getValue() + step));
    }
}
