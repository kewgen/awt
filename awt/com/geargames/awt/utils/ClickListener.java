package com.geargames.awt.utils;

import com.geargames.awt.Drawable;

/**
 * User: mikhail.kutuzov
 * Date: 23.11.11
 * Time: 18:36
 */
public abstract class ClickListener {
    public abstract void onEvent(Drawable source, int x, int y);
}
