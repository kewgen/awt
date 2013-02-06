package com.geargames.common.packer;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 18.09.12
 * Time: 19:13
 */
public class PObject extends PrototypeIndexes {

    public PObject(int prototypesCount) {
        super(prototypesCount);
        type = com.geargames.common.Render.T_OBJ;
    }

    public Index getIndexBySlot(int slot) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            IndexObject index = (IndexObject) list.get(i);
            if (index.isSlot() && index.getSlot() == slot) {
                return index;
            }
        }
        return null;
    }

}
