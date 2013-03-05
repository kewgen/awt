package com.geargames.awt.components;

import com.geargames.common.Graphics;
import com.geargames.common.packer.Index;
import com.geargames.common.packer.IndexObject;
import com.geargames.common.packer.PFrame;
import com.geargames.common.packer.PObject;
import com.geargames.common.util.Region;

import java.util.Vector;

/**
 * User: mikhail v. kutuzov
 * Реализация VerticalScrollView для списка элементов (меню) на основе prototype из пакера.
 */
public class PVerticalScrollView extends VerticalScrollView {
    private Vector items;
    private PPrototypeElement prototypeElement;
    private Region drawRegion;
    private Region touchRegion;

    public PVerticalScrollView(PObject prototype) {
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

        setInitiated(false);
        setStuck(false);
        setStrictlyClipped(false);

        IndexObject index0 = (IndexObject) prototype.getIndexBySlot(0);
        IndexObject index1 = (IndexObject) prototype.getIndexBySlot(1);

        prototypeElement = new PPrototypeElement();
        prototypeElement.setPrototype(index0.getPrototype());

        Region tmp = new Region();
        PObject objectPrototype = (PObject)index0.getPrototype();
        index = objectPrototype.getIndexBySlot(110);
        prototypeElement.setRegion(tmp);
        tmp.setMinX(index.getX());
        tmp.setMinY(index.getY());
        frame = (PFrame)index.getPrototype();
        tmp.setWidth(frame.getWidth());
        tmp.setHeight(frame.getHeight());
        if (index1 != null) {
            setMargin(index1.getY() - (index0.getY() + tmp.getHeight()));
        } else {
            setMargin(0);
        }
    }

    public Vector getItems() {
        return items;
    }

    /**
     * Установить элементы списка.
     * @param items
     */
    public void setItems(Vector items) {
        this.items = items;
        setInitiated(false);
    }

    public PPrototypeElement getPrototype() {
        return prototypeElement;
    }

    public Region getDrawRegion() {
        return drawRegion;
    }

    public Region getTouchRegion() {
        return touchRegion;
    }

    public void initiate(Graphics graphics) {
        setInitiated(true);
    }

}
