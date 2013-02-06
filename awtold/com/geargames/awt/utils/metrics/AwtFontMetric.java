package com.geargames.awt.utils.metrics;

import com.geargames.awt.utils.FontMetric;
import com.geargames.packer.Graphics;

/**
 * user: Mikhail V. Kutuzov
 * date: 26.10.11
 * time: 23:48
 */
/**@deprecated*/
public class AwtFontMetric extends FontMetric {

    private static AwtFontMetric instance = new AwtFontMetric();

    public int getWidth(com.geargames.common.String characters, Graphics graphics) {
        return graphics.getFontMetrics().stringWidth(characters.toString());
    }

    public int getHeigth(Graphics graphics) {
        return graphics.getFontMetrics().getHeight();
    }

    public int getWidth(char character, Graphics graphics) {
        return graphics.getFontMetrics().charWidth(character);
    }

    private AwtFontMetric() {
    }

    public static AwtFontMetric getInstance() {
        return instance;
    }

    public int getAscent(Graphics graphics) {
        return graphics.getFontMetrics().getAscent();
    }
}
