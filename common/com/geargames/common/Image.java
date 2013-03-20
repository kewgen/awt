package com.geargames.common;

import com.geargames.common.resource.Resource;

import java.io.IOException;

/**
 * User: kewgen
 * Date: 10.09.12
 * Time: 14:35
 */
public abstract class Image extends Resource {

    public abstract Image getRescaledImage(int w, int h) throws IOException;

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract void recycle();

    public abstract boolean isCreated();

}
