package com.geargames.awt.utils;

import com.geargames.awt.AWTObject;
import com.geargames.awt.Drawable;
import com.geargames.common.Graphics;

/**
 * Users: mikhail.kutuzov, abarakov
 * Date: 21.11.11
 * Time: 18:33
 */
public abstract class GraphicsStrategy extends AWTObject {

    public abstract void draw(Graphics graphics);

    public abstract boolean event(int code, int param, int x, int y);

    public abstract Drawable getOwner();

}
