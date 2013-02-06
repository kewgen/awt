package com.geargames.awt.components;

import com.geargames.common.util.ArrayList;

/**
 * User: mikhail v. kutuzov
 * Date: 04.12.12
 * Time: 0:02
 */
public class PToggleGroup {
    private ArrayList buttons;

    public PToggleGroup(int amount){
        buttons = new ArrayList(amount);
    }

    public void addButton(PToggleButton button){
        buttons.add(button);
        button.setGroup(this);
    }

    public void reset(PToggleButton button){
        int size = buttons.size();
        for(int i = 0; i < size; i++){
            PToggleButton current = (PToggleButton)buttons.get(i);
            current.setState(current == button);
        }
    }

}
