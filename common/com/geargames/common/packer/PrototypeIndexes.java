package com.geargames.common.packer;

import com.geargames.common.Graphics;
import com.geargames.common.util.ArrayList;

/**
 * User: kewgen
 * Date: 18.09.12
 * Time: 17:54
 * расширяет сущность до индексной - содержит список прототипов
 */
public class PrototypeIndexes extends Prototype {

    protected PrototypeIndexes(int size) {
        list = new ArrayList(size);
    }

    @Override
    public void draw(Graphics graphics, int x, int y) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Index index = (Index) list.get(i);
            index.draw(graphics, x, y);
        }
    }

    public void draw(Graphics graphics, int pos, int x, int y) {
        Index index = (Index) list.get(pos % list.size());
        index.draw(graphics, x, y);
    }

    protected void add(Index index) {
        list.add(index);
    }

    public ArrayList getIndexes() {
        return list;
    }

    public Index getIndex(int i) {
        return (Index) list.get(i);
    }

    @Override
    public String toString() {
        return "PrototypeIndexes{" +
                "list=" + list.size() +
                '}';
    }

    protected ArrayList list;
}
