package gg.microedition;

import java.lang.String;

/**
 */
public class FontMetrics {

    protected Font font;

    public int stringWidth(String str) {
        int len = str.length();
        char data[] = new char[len];
        str.getChars(0, len, data, 0);
        return charsWidth(data, 0, len);
    }

    public int charsWidth(char data[], int off, int len) {
        return stringWidth(new java.lang.String(data, off, len));
    }

    public int getAscent() {
        return font.getSize();
    }

    public int charWidth(char ch) {
        if (ch < 256) {
            return getWidths()[ch];
        }
        char data[] = {ch};
        return charsWidth(data, 0, 1);
    }

    public int[] getWidths() {
        int widths[] = new int[256];
        for (char ch = 0; ch < 256; ch++) {
            widths[ch] = charWidth(ch);
        }
        return widths;
    }

}
