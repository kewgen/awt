package com.geargames.packer;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 10.09.12
 * Time: 16:06
 */
/**
 * @deprecated
 */
public class FontMetrics implements com.geargames.common.FontMetrics {
    private java.awt.FontMetrics fontMetrics;

    public FontMetrics(java.awt.FontMetrics fontMetrics) {
        this.fontMetrics = fontMetrics;
    }

    public int stringWidth(String characters) {
        return fontMetrics.stringWidth(characters);
    }

    public int getHeight() {
        return fontMetrics.getHeight();
    }

    public int charWidth(char character) {
        return fontMetrics.charWidth(character);
    }

    public int getAscent() {
        return fontMetrics.getAscent();
    }
}
