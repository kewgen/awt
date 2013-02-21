package com.geargames.common.packer;

import com.geargames.common.String;
/**
 * User: mikhail v. kutuzov, abarakov
 * Шрифт реализованный на спрайтах.
 */
public abstract class PFont {

    /**
     * Вернуть спрайт указанного символа character.
     * @param character
     * @return Возвращает ссылку на спрайт символа или null, если символ в шрифте не определен.
     */
    public abstract PSprite getSprite(char character);

    /**
     * Вернуть базовую линию.
     * @return
     */
    public abstract int getBaseLine();

    /**
     * Вернуть размер шрифта.
     * @return
     */
    public abstract int getSize();

    /**
     * Вернуть ширину символа character для шрифта.
     * @param character
     * @return
     */
    public abstract int getWidth(char character);

    /**
     * Вернуть ширину строки символов characters, представленной данным шрифтом.
     * @param characters
     * @return
     */
    public int getWidth(String characters) {
        int width = 0;
        for (int i = 0; i < characters.length(); i++) {
            width += getWidth(characters.charAt(i));
        }
        return width;
    }

    public int getAscent(){
        return getSize() - getBaseLine();
    }
}
