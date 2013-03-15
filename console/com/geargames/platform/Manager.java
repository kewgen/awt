package com.geargames.platform;

import com.geargames.platform.packer.Canvas;

/**
 * User: kewgen
 * Date: 26.03.12
 * Time: 11:44
 */
public abstract class Manager extends com.geargames.common.Manager {

    abstract public MIDlet getMidlet();

    abstract public Canvas getCanvas();

}
