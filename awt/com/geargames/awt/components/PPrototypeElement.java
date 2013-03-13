package com.geargames.awt.components;

import com.geargames.common.util.Region;
import com.geargames.common.packer.Prototype;
import com.geargames.common.Graphics;

/**
 * User: mikhail v. kutuzov
 * Простой динамический пакерный объект, нужен только для соблюдения полиморфизма в общих алгоримах отрисовки.
 */
public class PPrototypeElement extends PElement {
    private Region region;
    private Prototype prototype;

    public PPrototypeElement() {
    }

    public Prototype getPrototype() {
        return prototype;
    }

    public void setPrototype(Prototype prototype) {
        this.prototype = prototype;
    }

    @Override
    public Region getTouchRegion() {
        return region;
    }

    @Override
    public Region getDrawRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @Override
    public void draw(Graphics graphics, int x, int y) {
        prototype.draw(graphics, x, y);
    }

}
