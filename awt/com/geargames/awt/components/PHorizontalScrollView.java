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
 * Реализация HorizontalScrollView для списка элементов (меню) на основе prototype из пакера.
 */
public class PHorizontalScrollView extends HorizontalScrollView {
    private Vector items;
    private PPrototypeElement prototypeElement;
    private Region drawRegion;
    private Region touchRegion;

    public PHorizontalScrollView(PObject prototype) {
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
            setMargin(index1.getX() - (index0.getX() + tmp.getWidth()));
        } else {
            setMargin(0);
        }
    }

    @Override
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

    @Override
    public PPrototypeElement getPrototype() {
        return prototypeElement;
    }

    @Override
    public Region getDrawRegion() {
        return drawRegion;
    }

    @Override
    public Region getTouchRegion() {
        return touchRegion;
    }

    @Override
    public void initiate(Graphics graphics) {
        setInitiated(true);
    }

}
