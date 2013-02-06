package com.geargames.common.packer;

import com.geargames.common.util.ArrayList;

/**
 * User: mikhail v. kutuzov
 * Составной фонт заключает в себе части одного фонта для разных языков, дополнительные символы
 * и числа(в дальнейшем под-фонт) в виде отдельных фонтов.
 */
public class PFontComposite extends PFont {
    private ArrayList fonts;
    private int height;
    private int baseLine;
    private int size;
    private int lastIndex = 0;

    PFontComposite(ArrayList fonts) {
        this.fonts = fonts;
        size = fonts.size();
        PFont font = (PFont) fonts.get(0);
        baseLine = font.getBaseLine();
        height = font.getSize();
    }

    /**
     * Вернуть под-фонт с индексом index.
     *
     * @param index индекс под-фонта
     * @return проблему ) если попытайтесь взять не правильный индекс.
     */
    public PFont getFont(int index) {
        return (PFont) fonts.get(index);
    }

    /**
     * Количество под-фонтов.
     *
     * @return
     */
    public int getFontsAmount() {
        return size;
    }

    /**
     * Реализация метода оптимизирована для последовательного отбора символов из одного под-фонта,
     * как чаще всего и бывает.
     *
     * @param character
     * @return
     */
    public PSprite getSprite(char character) {
        PSprite sprite;
        for (int k = 0; k < size; k++) {
            sprite = ((PFont) fonts.get((lastIndex + k)%size)).getSprite(character);
            if (sprite != null) {
                lastIndex = k;
                return sprite;
            }
        }
        return null;
    }

    public int getBaseLine() {
        return baseLine;
    }

    public int getSize() {
        return height;
    }

    public int getWidth(char character) {
        return (byte) getSprite(character).getIndex(0).getX();
    }

    public int getWidth(com.geargames.common.String characters) {
        int length = characters.length();
        int width = 0;
        for (int i = 0; i < length; i++) {
            width += getWidth(characters.charAt(i));
        }
        return width;
    }
}
