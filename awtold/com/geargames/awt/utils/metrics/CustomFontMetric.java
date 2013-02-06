package com.geargames.awt.utils.metrics;

import com.geargames.awt.utils.FontMetric;
import com.geargames.common.packer.Render;
import com.geargames.packer.Graphics;

/**
 * user: Mikhail V. Kutuzov
 * date: 26.10.11
 * time: 23:47
 */
public class CustomFontMetric extends FontMetric {

    private static CustomFontMetric instance = new CustomFontMetric();

    public int getWidth(com.geargames.common.String characters, Graphics graphics) {
        return Render.getInstance().getStringWidth(characters);
    }

    public int getHeigth(Graphics graphics) {
        return Render.getInstance().getCustomFontHeight();
    }

    public int getWidth(char character, Graphics graphics) {
        com.geargames.common.Render render = Render.getInstance();
        return render.getSpriteDx(render.getSpriteId(character));
    }

    public static CustomFontMetric getInstance() {
        return instance;
    }


    public int getAscent(Graphics graphics) {
        return getHeigth(graphics) - Render.getInstance().getCustomFontBaseLine();
    }
}
