package com.geargames.common.packer;

import com.geargames.common.util.ArrayList;

/**
 * User: mikhail v. kutuzov, abarakov
 * Шрифт представляющий один из диапазонов символов некоторой кодовой страницы составного шрифта.
 */
public class PFontCharRange extends PFont {
    private ArrayList characters;
    private char firstCharacter;
    private int amount;
    private int height;
    private int baseLine;

    /**
     * @param firstCharacter код первого символа диапазона шрифта
     * @param amount количество символов в диапазоне шрифта
     * @param height высота букв шрифта
     * @param baseLine базовая линия шрифта
     */
    PFontCharRange(char firstCharacter, int amount, int height, int baseLine) {
        this.firstCharacter = firstCharacter;
        this.amount = amount;
        this.characters = new ArrayList(amount);
        this.height = height;
        this.baseLine = baseLine;
    }

    /**
     * Вернуть спрайт указанного символа character.
     * @param character
     * @return Возвращает ссылку на спрайт символа или null, если символ в шрифте не определен.
     */
    public PSprite getSprite(char character) {
        int index = character - firstCharacter;
        if (index >= 0 && index < amount) {
            return (PSprite) characters.get(index);
        } else {
            return null;
        }
    }

    /**
     * Вернуть код первого символа данного диапазона символов шрифта.
     * @return
     */
    public char getFirstIndex() {
        return firstCharacter;
    }

    /**
     * Вернуть количество символов в данном диапазоне символов шрифта.
     * @return
     */
    public int getCharacterAmount() {
        return amount;
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
        return (((PSprite)characters.get(character - firstCharacter)).getIndex(0).getX());
    }

    /**
     * Добавить символ к данному диапазону символов шрифта с кодом character и соответствующим спрайтом sprite
     * @param character
     * @param sprite
     */
    public void addSprite(char character, PSprite sprite) {
        int index = character - firstCharacter;
        if (index >= characters.size()) {
            characters.add(index, sprite);
        } else {
            characters.set(index, sprite);
        }
    }

    /**
     * Вернуть массив символов данного диапазона символов шрифта.
     * @return
     */
    public ArrayList getCharacters() {
        return characters;
    }
}