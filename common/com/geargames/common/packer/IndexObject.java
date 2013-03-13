package com.geargames.common.packer;

/**
 * User: kewgen
 * Date: 18.09.12
 * Time: 19:20
 * расширение индекса для объекта
 */
public class IndexObject extends Index {
    public IndexObject(Prototype prototype, int x, int y, int shift, int slot, int layerType) {
        super(prototype, x, y);
        this.shift = shift;
        this.slot = slot;
        this.layerType = layerType;
    }



    public int getShift() {
        return shift;
    }

    public int getSlot() {
        return slot;
    }

    public int getLayerType() {
        return layerType;
    }

    public boolean isSlot() {
        return layerType == PManager.LAYER_T_SLOT;
    }

    @Override
    public String toString() {
        return "IndexObject{" +
                "" + super.toString() +
                ", shift=" + shift +
                ", slot=" + slot +
                ", layerType=" + layerType +
                '}';
    }

    private int shift;
    private int slot;
    private int layerType;

}
