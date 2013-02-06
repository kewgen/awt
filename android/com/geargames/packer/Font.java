package com.geargames.packer;

import com.geargames.common.String;

/**
 * Port-wrapper для microedition
 */
public class Font implements com.geargames.common.Font {

    public static final int STYLE_PLAIN = 0;
    public static final int STYLE_BOLD = 0;//javax.microedition.lcdui.Font.STYLE_BOLD;
    public static final int SIZE_MEDIUM = 0;//javax.microedition.lcdui.Font.SIZE_MEDIUM;
    public static final int FACE_PROPORTIONAL = 0;//javax.microedition.lcdui.Font.FACE_PROPORTIONAL;

    public static final String SERIF = String.valueOfC(android.graphics.Typeface.SERIF.toString());
    public static final int BOLD = android.graphics.Typeface.BOLD;


    public static Font getFont(Graphics g, int face, int style, int size) {
        return null;//new Font(javax.microedition.lcdui.Font.getFont(face, style, size));
    }

    public Font(String name, int style, int size) {
        font = null;//new android.graphics.Typeface(name != null ? name.toString() : null, style, size);
    }

    public Font(Object/*android.renderscript.Font*/ font) {
        this.font = font;
    }


    public Object/*javax.microedition.lcdui.Font*/ getFont() {
        return font;
    }

    private Object/*javax.microedition.lcdui.Font*/ font;

}
