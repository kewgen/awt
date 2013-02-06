package com.geargames.awt.utils;

import com.geargames.awt.Drawable;
import com.geargames.common.Graphics;

/**
 * User: mikhail.kutuzov
 * Date: 21.11.11
 * Time: 18:33
 */
public abstract class GraphicsStrategy {
    public abstract void draw(Graphics graphics, Drawable drawable);

    public abstract void event(int code, int param, int x, int y);
}
