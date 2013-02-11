package com.geargames.awt.components;

import com.geargames.common.util.ArrayList;

/**
 * User: mikhail v. kutuzov
 * Date: 04.12.12
 * Time: 0:02
 */
public class PRadioGroup {
    private ArrayList buttons;

    public PRadioGroup(int amount){
        buttons = new ArrayList(amount);
    }

    public void addButton(PRadioButton button){
        buttons.add(button);
        button.setGroup(this);
    }

    public void reset(PRadioButton button){
        int size = buttons.size();
        for(int i = 0; i < size; i++){
            PRadioButton current = (PRadioButton)buttons.get(i);
            current.setState(current == button);
        }
    }

}
