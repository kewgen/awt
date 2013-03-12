package com.geargames.awt.components;

import com.geargames.common.util.ArrayList;

/**
 * User: mikhail v. kutuzov
 * Date: 04.12.12
 * Time: 0:02
 */
public class PRadioGroup {
    private ArrayList buttons;

    public PRadioGroup(int amount) {
        buttons = new ArrayList(amount);
    }

    public void addButton(PRadioButton button) {
        if (!buttons.contains(button)) {
            buttons.add(button);
            button.setGroup(this);
        }
    }

    public void removeButton(PRadioButton button) {
        int i = buttons.indexOf(button);
        if (i >= 0) {
            buttons.remove(i);
            button.setGroup(null);
        }
    }

    public void reset(PRadioButton button) {
        // Цикл в обратную сторону, на случай, если в методе onClick() кнопки, она будет перемещена в другую группу.
        for (int i = buttons.size() - 1; i >= 0; i--) {
            PRadioButton item = (PRadioButton)buttons.get(i);
            item.setChecked(item == button);
        }
    }

    public PRadioButton getCheckedButton() {
        for (int i = 0; i < buttons.size(); i++) {
            PRadioButton item = (PRadioButton)buttons.get(i);
            if (item.getChecked()) {
                return item;
            }
        }
        return null;
    }

}
