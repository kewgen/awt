package com.geargames.awt.components;

import com.geargames.common.*;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PObject;
import com.geargames.common.util.ArrayList;

/**
 * User: mikhail v. kutuzov
 * Date: 27.12.12
 * Time: 13:06
 */
public class PGradualSpinBox extends PValueComponent {
    private PLabel label;
    private PGradualSpinButton up;
    private PGradualSpinButton down;
    private short value;
    private boolean initiated;

    public PGradualSpinBox(PObject prototype, short value, int fps) {
        super(prototype);
        this.value = value;
        initiated = false;

        ArrayList indexes = prototype.getIndexes();
        for (int i = 0; i < indexes.size(); i++) {
            IndexObject index = (IndexObject) indexes.get(i);
            if (index.isSlot()) {
                switch (index.getSlot()) {
                    case 0:
                        down = new PGradualSpinButton((PObject)index.getPrototype(), fps, false);
                        down.setBox(this);
                        addActiveChild(down, index);
                        break;
                    case 1:
                        up = new PGradualSpinButton((PObject)index.getPrototype(), fps, true);
                        up.setBox(this);
                        addActiveChild(up, index);
                        break;
                    case 2:
                        label = new PSimpleLabel(index);
                        addPassiveChild(label, index);
                        break;

                }
            }
        }
    }


    public void draw(Graphics graphics, int x, int y) {
        if (!initiated) {
            initiate();
        }
        super.draw(graphics, x, y);
    }

    private void initiate() {
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
}
