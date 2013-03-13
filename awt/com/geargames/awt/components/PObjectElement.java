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
        Index drawBoundsIndex = prototype.getIndexBySlot(110);
        drawRegion = new Region();
        drawRegion.setMinX(drawBoundsIndex.getX());
        drawRegion.setMinY(drawBoundsIndex.getY());
        PFrame drawBoundsFrame = (PFrame) drawBoundsIndex.getPrototype();
        drawRegion.setWidth(drawBoundsFrame.getWidth());
        drawRegion.setHeight(drawBoundsFrame.getHeight());

        Index touchBoundsIndex = prototype.getIndexBySlot(112);
        if (touchBoundsIndex != null) {
            touchRegion = new Region();
            touchRegion.setMinX(touchBoundsIndex.getX());
            touchRegion.setMinY(touchBoundsIndex.getY());
            PFrame touchBoundsFrame = (PFrame) touchBoundsIndex.getPrototype();
            touchRegion.setWidth(touchBoundsFrame.getWidth());
            touchRegion.setHeight(touchBoundsFrame.getHeight());
        } else {
            touchRegion = drawRegion;
        }
    }

}
