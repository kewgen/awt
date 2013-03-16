package com.geargames.awt.components;

import com.geargames.common.packer.IndexObject;
import com.geargames.common.util.NullRegion;
import com.geargames.common.util.Region;

/**
 * Простой label. Компонент служит исключительно для отображения текста, для touch-событий является "прозрачным".
 */
public class PSimpleLabel extends PLabel {

    public PSimpleLabel(IndexObject index) {
        super(index);
    }

    public PSimpleLabel() {
    }

    @Override
    public Region getDrawRegion() {
        return NullRegion.instance;
    }

    @Override
    public Region getTouchRegion() {
        return NullRegion.instance;
    }

}
