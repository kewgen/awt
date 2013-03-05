package com.geargames.awt.components;

import com.geargames.common.util.Region;
import com.geargames.common.packer.Prototype;
import com.geargames.common.Graphics;

/**
 * User: mikhail v. kutuzov
 * Простой динамический пакерный объект, нужен только для соблюдения полиморфизма в общих алгоримах отрисовки.
 */
@Deprecated // Используйте PPrototypeElement вместо PSimplePrototype
public class PSimplePrototype extends PElement {
    private Region region;
    private Prototype prototype;

    public PSimplePrototype() {
        region = new Region();
    }

    public Prototype getPrototype() {
        return prototype;
    }

    public void setPrototype(Prototype prototype) {
        this.prototype = prototype;
    }

    public Region getTouchRegion() {
        return region;
    }

    public Region getDrawRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public void draw(Graphics graphics, int x, int y) {
        prototype.draw(graphics, x, y);
    }

}
