package com.geargames.common.packer;

import com.geargames.common.Graphics;

/**
 * User: kewgen
 * Date: 18.09.12
 * Time: 17:58
 * общее описание сущности
 */
public abstract class Prototype {

    public abstract void draw(Graphics g, int x, int y);

    public byte getType() {
        return type;
    }

    public int getPID() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    protected byte type;
    protected int pid;

}
