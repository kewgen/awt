package com.geargames.common.util;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 05.06.12
 * Time: 17:52
 */
public class ArrayList extends java.util.ArrayList {
    public ArrayList() {
        super();
    }

    public ArrayList(int initialCapacity) {
        super(initialCapacity);
    }

    public ArrayList copy() {
        ArrayList arrayList = new ArrayList(this.size());
        for (Object object : this) arrayList.add(object);
        return arrayList;
    }
}
