package com.geargames.awt.components;

import com.geargames.common.util.Region;
import com.geargames.common.packer.Index;
import com.geargames.common.packer.PFrame;
import com.geargames.common.packer.PObject;

/**
 * User: mikhail v. kutuzov
 * Базовый элемент GUI для всех элементов собирающихся по объектам из пакера.
 */
public abstract class PObjectElement extends PElement {
    private Region region;

    public Region getDrawRegion() {
        return region;
    }

    public Region getTouchRegion() {
        return region;
    }

    public PObjectElement(PObject prototype) {
        Index index = prototype.getIndexBySlot(110);
        region = new Region();
        region.setMinX(index.getX());
        region.setMinY(index.getY());
        PFrame frame = (PFrame)index.getPrototype();
        region.setWidth(frame.getWidth());
        region.setHeight(frame.getHeight());
    }


    public boolean isVisible() {
        return true;
    }

}
