package com.geargames.awt.utils;

import com.geargames.awt.Eventable;
import com.geargames.awt.Drawable;
import com.geargames.common.Graphics;

/**
 * Users: mikhail.kutuzov, abarakov
 * Date: 21.11.11
 */
public abstract class GraphicsStrategy extends Eventable {

    public abstract void draw(Graphics graphics);

    public abstract Drawable getOwner();

}
