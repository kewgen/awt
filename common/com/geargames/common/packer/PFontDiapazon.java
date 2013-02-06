package com.geargames.common.packer;

import com.geargames.common.*;
import com.geargames.common.util.ArrayList;

/**
 * User: mikhail v. kutuzov
 * Данная реализация фонта работает только для символов определённого диапазона некоторой кодовой страницы.
 */
public class PFontDiapazon extends PFont {
    private char firstIndex;
    private ArrayList characters;
    private int amount;
    private int height;
    private int baseLine;

    /**
     * @param firstCharacter код первого символа диапазона(фонта)
     * @param amount количество символов диапазона(фонта)
     * @param height высота букв фонта
     * @param baseLine базовая линия фонта
     */
    PFontDiapazon(char firstCharacter, int amount, int height, int baseLine) {
        this.firstIndex = firstCharacter;
        this.amount = amount;
        this.characters = new ArrayList(amount);
        this.height = height;
        this.baseLine = baseLine;
    }

    public PSprite getSprite(char character) {
        int index = character - firstIndex;
        if (index >= 0 && index < amount) {
            return (PSprite) characters.get(index);
        } else {
            return null;
        }
    }

    public int getBaseLine() {
        return baseLine;
    }

    public int getSize() {
        return height;
    }

    public int getWidth(char character) {
        return (byte)(((PSprite)characters.get(character - firstIndex)).getIndex(0).getX());
    }

    public char getFirstIndex() {
        return firstIndex;
    }

    /**
     * Добавить к фонту спрайт sprite соответсвующий коду символа character.
     * @param character
     * @param sprite
     */
    public void addSprite(char character, PSprite sprite) {
        if(character-firstIndex>=characters.size()){
            characters.add(character - firstIndex, sprite);
        }else{
            characters.set(character - firstIndex, sprite);
        }
    }

    public int getWidth(com.geargames.common.String characters) {
        int width = 0;
        for(int i = 0; i < characters.length(); i++){
            width += getWidth(characters.charAt(i));
        }
        return width;
    }

    public ArrayList getCharacters() {
        return characters;
    }
}
