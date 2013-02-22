package com.geargames.common.packer;

import com.geargames.common.util.ArrayChar;
import com.geargames.common.util.ArrayList;

/**
 * User: mikhail v. kutuzov, abarakov
 * Составной шрифт заключает в себе диапазоны символов одного шрифта некоторой кодовой страницы (символы разных языков,
 * дополнительные символы и числа). Каждый диапазон символов - это PFontCharRange.
 */
public class PFontComposite extends PFont {
    private ArrayList fonts;
    private int height;
    private int baseLine;
    private int lastIndex = 0;

    public final static char DEFAULT_CHAR = '?';
    private PSprite defaultSprite = null;

    PFontComposite(ArrayList fonts) {
        this.fonts = fonts;
        PFont font = (PFont) fonts.get(0);
        baseLine = font.getBaseLine();
        height = font.getSize();
        defaultSprite = getSprite(DEFAULT_CHAR);
    }

    /**
     * Вернуть ссылку на диапазон символов с индексом index.
     *
     * @param index индекс диапазона символов
     * @return проблема ) если попытайтесь взять не правильный индекс.
     */
    public PFont getFont(int index) {
        return (PFont) fonts.get(index);
    }

    /**
     * Вернуть количество диапазонов символов.
     * @return
     */
    public int getFontsAmount() {
        return fonts.size();
    }

    /**
     * Вернуть спрайт указанного символа character.
     * Реализация метода оптимизирована для последовательного отбора символов из одного из диапазонов символов,
     * как чаще всего и бывает.
     * @param character
     * @return Возвращает ссылку на спрайт символа или null, если символ в шрифте не определен.
     */
    public PSprite getSprite(char character) {
        PSprite sprite;
        for (int k = 0; k < fonts.size(); k++) {
            sprite = ((PFont) fonts.get((lastIndex + k) % fonts.size())).getSprite(character);
            if (sprite != null) {
                lastIndex = k;
                return sprite;
            }
        }
        return defaultSprite;
    }

    /**
     * Вернуть базовую линию.
     * @return
     */
    public int getBaseLine() {
        return baseLine;
    }

    /**
     * Вернуть размер шрифта.
     * @return
     */
    public int getSize() {
        return height;
    }

    /**
     * Вернуть ширину символа character для шрифта.
     * @param character
     * @return
     */
    public int getWidth(char character) {
        return getSprite(character).getIndex(0).getX();
    }

    public int getWidth(ArrayChar characters, int position, int length) {
        int width = 0;
        length += position;
        for (int i = position; i < length; i++){
            width += getWidth(characters.get(i));
        }
        return width;
    }

    public int getWidth(com.geargames.common.String characters) {
        int width = 0;
        for(int i = 0; i < characters.length(); i++){
            width += getWidth(characters.charAt(i));
        }
        return width;
    }

}
