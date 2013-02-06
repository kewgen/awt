package gg.microedition;
import java.lang.String;

/**
 * Port-wrapper для microedition
 */
public class Font {

    public static final int STYLE_PLAIN = javax.microedition.lcdui.Font.STYLE_ITALIC;
    public static final int STYLE_BOLD = javax.microedition.lcdui.Font.STYLE_BOLD;
    public static final int SIZE_MEDIUM = javax.microedition.lcdui.Font.SIZE_MEDIUM;
    public static final int FACE_PROPORTIONAL = javax.microedition.lcdui.Font.FACE_PROPORTIONAL;

    public static Font getFont(Graphics g, int face, int style, int size) {
        return new Font(javax.microedition.lcdui.Font.getFont(face, style, size));
    }

    public Font(String name, int style, int size) {
        //font = new javax.microedition.lcdui.Font(name != null ? name.toString() : null, style, size);
    }

    public Font(javax.microedition.lcdui.Font font) {
        this.font = font;
    }


    public javax.microedition.lcdui.Font getFont() {
        return font;
    }

    public int getSize() {
        return font.getSize();
    }

    private javax.microedition.lcdui.Font font;
    public static final String SERIF = Font.SERIF;
    public static final int BOLD = Font.BOLD;

}
