package com.geargames.awt;

import com.geargames.common.Graphics;

/**
 * User: abarakov
 * Date: 14.03.13
 */
// PostDrawable
public interface PostDrawable {

    /**
     * A method to be used to call an ancestor's drawing method.
     *
     * @param graphics графический контекст, на котором должен быть нарисован объект.
     */
    void postDraw(Graphics graphics);

}