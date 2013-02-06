package com.geargames;

import com.geargames.packer.Canvas;

/**
 * Created by IntelliJ IDEA.
 * User: kewgen
 * Date: 26.03.12
 * Time: 11:44
 */
public abstract class Manager extends com.geargames.common.Manager {

    abstract public MIDlet getMidlet();

    abstract public Canvas getCanvas();//если убрать канваз то можно перенести в common


}
