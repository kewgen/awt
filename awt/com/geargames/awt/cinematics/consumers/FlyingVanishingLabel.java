package com.geargames.awt.cinematics.consumers;

import com.geargames.awt.VanishingLabel;
import com.geargames.awt.cinematics.CMovingPoint;

/**
 * Created by IntelliJ IDEA.
 * User: mikhail.kutuzov
 * Date: 10.12.11
 */
public class FlyingVanishingLabel {
    private static Flying instance;

    public static Flying show(com.geargames.common.String data, int x, int y, byte color) {
        if (instance == null) {
            instance = new Flying();
            VanishingLabel label = new VanishingLabel();
            label.setTime(50);
            label.setTransparencyTime(50);
            label.setColor(color);
            instance.setDrawable(label);
            CMovingPoint speed = new CMovingPoint();
            speed.setX(0);
            speed.setY(-3);
            speed.setChanger(null);
            instance.setSpeed(speed);
            instance.setFinishAdviser(new VanishFinishAdviser(label));
        }
        ((VanishingLabel) instance.getDrawable()).setData(data);
        instance.setX(x);
        instance.setY(y);
        ((VanishingLabel) instance.getDrawable()).reset();
        instance.initiate();
        return instance;
    }
}
