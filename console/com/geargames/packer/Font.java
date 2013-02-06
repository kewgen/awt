package com.geargames.packer;

import com.geargames.common.String;

/**
 * Port-wrapper для microedition
 */
/**
 * @deprecated
 */
public class Font implements com.geargames.common.Font {

    public static final String SERIF = String.valueOfC(java.awt.Font.SERIF);
    public static final int BOLD = java.awt.Font.BOLD;
    public static final int FACE_PROPORTIONAL = 0;

    public static Font getFont(Graphics g, int face, int style, int size) {
        java.awt.Font font1 = g.getFont(false).getAWTFont();
        return new Font(font1);
    }

    public Font(String name, int style, int size) {
        font = new java.awt.Font(name != null ? name.toString() : null, style, size);
    }

    public Font(java.awt.Font font) {
        this.font = font;
    }


    public Font getFont() {
        return new Font(font);
    }

    private java.awt.Font font;

    public java.awt.Font getAWTFont() {
        return font;
    }
}
