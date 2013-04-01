package com.geargames.common.packer;

import com.geargames.common.util.ArrayChar;

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
     * Вернуть ширину подстроки символов characters, представленной данным шрифтом.
     * @param characters
     * @param position
     * @param length
     * @return
     */
    public abstract int getWidth(ArrayChar characters, int position, int length);

    /**
     * Вернуть ширину строки символов string, представленной данным шрифтом.
     * @param string
     * @return
     */
    public abstract int getWidth(String string);

    /**
     * Вернуть ширину подстроки символов string, представленной данным шрифтом.
     * @param string
     * @param position
     * @param length
     * @return
     */
    public abstract int getWidth(String string, int position, int length);

    public int getAscent(){
        return getSize() - getBaseLine();
    }

}
