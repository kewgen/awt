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
    private Region drawRegion;
    private Region touchRegion;

    public Region getDrawRegion() {
        return drawRegion;
    }

    public Region getTouchRegion() {
        return touchRegion;
    }

    public PObjectElement(PObject prototype) {
        Index index = prototype.getIndexBySlot(110);
        drawRegion = new Region();
        drawRegion.setMinX(index.getX());
        drawRegion.setMinY(index.getY());
        PFrame frame = (PFrame) index.getPrototype();
        drawRegion.setWidth(frame.getWidth());
        drawRegion.setHeight(frame.getHeight());

        index = prototype.getIndexBySlot(112);
        if (index != null) {
            touchRegion = new Region();
            touchRegion.setMinX(index.getX());
            touchRegion.setMinY(index.getY());
            frame = (PFrame) index.getPrototype();
            touchRegion.setWidth(frame.getWidth());
            touchRegion.setHeight(frame.getHeight());
        } else {
            touchRegion = drawRegion;
        }
    }

    public boolean isVisible() {
        return true;
    }

}
