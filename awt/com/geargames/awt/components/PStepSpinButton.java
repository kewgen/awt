package com.geargames.awt.components;

import com.geargames.common.packer.PObject;

/**
 * User: mikhail v. kutuzov
 * Кнопка которая даёт подчинённом PStepSpinBox приращение значения при каждом нажатии.
 */
public class PStepSpinButton extends PTouchButton {
    private byte step;
    private PStepSpinBox box;

    public PStepSpinButton(PObject prototype) {
        super(prototype);
    }

    public void setStep(byte step) {
        this.step = step;
    }

    public void setBox(PStepSpinBox box) {
        this.box = box;
    }

    public void action() {
        box.setValue((short)(box.getValue() + step));
    }
}
