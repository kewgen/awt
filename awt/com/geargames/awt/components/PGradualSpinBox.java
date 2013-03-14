package com.geargames.awt.components;

import com.geargames.common.Graphics;
import com.geargames.common.String;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PFont;
import com.geargames.common.packer.PObject;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Date: 27.12.12
 * Компонент содержит в себе кнопки "вверх" и "вниз" для добавления приращения к содержимому label.
 * Приращение значения начинает происходить многократно, как только соответствующая кнопка была зажата и до тех пор,
 * пока она не будет отжата.
 */
public class PGradualSpinBox extends PValueComponent {
    private int value;
    private int minValue;
    private int maxValue;
    private boolean initiated;
    private PLabel label;
    private PGradualSpinButton buttonUp;
    private PGradualSpinButton buttonDown;

    public PGradualSpinBox(PObject prototype) {
        super(prototype);
        this.value    = 0;
        this.minValue = 0;
        this.maxValue = 100;
        setStep(1);
        initiated = false;
    }

    @Override
    protected void createSlotElementByIndex(IndexObject index, PObject parentPrototype) {
        switch (index.getSlot()) {
            case 0:
                buttonDown = new PGradualSpinButton((PObject)index.getPrototype());
                buttonDown.setBox(this);
                addActiveChild(buttonDown, index);
                break;
            case 1:
                buttonUp = new PGradualSpinButton((PObject)index.getPrototype());
                buttonUp.setBox(this);
                addActiveChild(buttonUp, index);
                break;
            case 2:
                label = new PSimpleLabel(index);
                addPassiveChild(label, index);
                break;
        }
    }

    @Override
    public void draw(Graphics graphics, int x, int y) {
        if (!initiated) {
            initiate();
        }
        super.draw(graphics, x, y);
    }

    private void initiate() {
        label.setText(String.valueOfI(value));
        initiated = true;
    }

    /**
     * Получить значение.
     * @return
     */
    @Override
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (value < minValue) {
            value = minValue;
        }
        if (value > maxValue) {
            value = maxValue;
        }
        if (this.value != value) {
            this.value = value;
            initiated = false;
            onValueChanged();
        }
    }

    /**
     * Получить минимально допустимое значение.
     * @return
     */
    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int value) {
        this.minValue = value;
        setValue(this.value); // на случай, если this.value < minValue
    }

    /**
     * Получить максимально допустимое значение.
     * @return
     */
    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int value) {
        this.maxValue = value;
        setValue(this.value); // на случай, если this.value > maxValue
    }

    /**
     * Установить значение шага, с которым будет увеличиваться или уменьшаться значение компонента.
     * @return
     */
    public void setStep(int step) {
        buttonUp.setStep(step);
        buttonDown.setStep(-step);
    }

    public void setFont(PFont font) {
        label.setFont(font);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        buttonUp.setEnabled(enabled);
        buttonDown.setEnabled(enabled);
    }

    /**
     * Обработчик события изменения значения.
     */
    public void onValueChanged() {

    }

}
