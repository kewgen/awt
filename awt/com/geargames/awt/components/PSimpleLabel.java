package com.geargames.awt.components;

import com.geargames.common.packer.IndexObject;
import com.geargames.common.util.NullRegion;
import com.geargames.common.util.Region;

/**
 * Простой label: не реагирует ни на что =) и сразмерами полная неопределённость.
 */
public class PSimpleLabel extends PLabel {
    public PSimpleLabel(IndexObject index) {
        super(index);
    }

    public PSimpleLabel() {
    }

    public Region getDrawRegion() {
        return NullRegion.instance;
    }

    public Region getTouchRegion() {
        return NullRegion.instance;
    }

    public boolean event(int code, int param, int x, int y) {
        return false;
    }
}
