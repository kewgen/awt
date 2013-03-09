package com.geargames.common;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 10.09.12
 * Time: 14:35
 */
public interface Image {

    Image getRescaledImage(int w, int h) throws IOException;

    int getWidth();

    int getHeight();

    void resizeCanvas(int w, int h);

    void recycle();

    boolean isCreated();

}
