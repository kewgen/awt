package com.geargames.awt.utils;

import com.geargames.awt.Drawable;

/**
 * Users: mikhail.kutuzov, abarakov
 * Date: 23.11.11
 * Time: 18:36
 */
public abstract class TouchListener {

    public abstract void onEvent(Drawable source, int code, int param, int x, int y);
}

