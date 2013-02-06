package com.geargames.common.packer;

import com.geargames.common.String;
/**
 * User: mikhail v. kutuzov
 * Фонт реализованный на спрайтах.
 */
public abstract class PFont {
    /**
     * Вернуть спрайт данного символа character.
     * @param character
     * @return в случае если данный спрайт не найден возвращаем null.
     */
    public abstract PSprite getSprite(char character);

    /**
     * Вернуть базовую линию.
     * @return
     */
    public abstract int getBaseLine();

    /**
     * Вернуть размер фонта.
     * @return
     */
    public abstract int getSize();

    /**
     * Вернуть ширину символа character для фонта.
     * @param character
     * @return
     */
    public abstract int getWidth(char character);

    /**
     * Вернуть длинну строки символов characters, представленной данным фонтом.
     * @param characters
     * @return
     */
    public abstract int getWidth(String characters);

    public byte getAscent(){
        return (byte)(getSize() - getBaseLine());
    }
}
