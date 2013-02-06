package com.geargames.common.packer;

import com.geargames.common.util.ArrayIntegerDual;
import com.geargames.common.util.ArrayList;

/**
 * User: mikhail v. kutuzov
 * Класс осуществляет зачитку фонтов по указателям-индексам фонтов в массиве спрайтов проекта.
 * Реализация заворачивает  в один составной фонт фонты от разных символов некоторой кодовой страницы.
 */
public class PFontManager {
    private ArrayList fonts;

    /**
     * Зачитать фонты из массива спрайтов.
     * Текущая реализация завязана на то, что базовая линия фонта проходит по середине прямоугольника выделенного
     * под символ фонта.
     *
     * @param manager     packer manager как есть
     * @param fontIndexes {(0){(0)SYMBOLS, (1)NUMBERS, (2)RU, (3)ESP, (4)EN} 1{SYMBOLS, NUMBERS, RU, ESP, EN}  ..... }  - вот такой может быть формат
     */
    public void init(PManager manager, ArrayIntegerDual fontIndexes) {
        int size = fontIndexes.length();
        fonts = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            int length = fontIndexes.get(i).length;
            ArrayList composite = new ArrayList(length);
            for (int j = 0; j < length; j++) {
                PSprite sprite = manager.getPSprite(fontIndexes.get(i)[j]);
                Index index = sprite.getIndex(sprite.getIndexes().size() - 2);
                char baseCharacter = (char) index.getX();
                int amount = index.getY();
                index = sprite.getIndex(sprite.getIndexes().size() - 1);
                int height = index.getX();
                int baseLine = height / 2;
                PFontDiapazon font = new PFontDiapazon(baseCharacter, amount, height, baseLine);
                int baseId = sprite.getPID();
                for (int k = 0; k < amount; k++) {
                    PSprite charSprite = manager.getPSprite(baseId + k);
                    font.addSprite((char) (baseCharacter + k), charSprite);
                }
                composite.add(font);
            }
            fonts.add(new PFontComposite(composite));
        }
    }

    /**
     * Вернуть по индексу фонта составной фонт (хотя этого, что он составной никто и не поймёт)
     *
     * @param fontIndex индекс фонта (например 0 - вернёт первый составной фонт)
     * @return сдесь лучше не рисковать получить фонт с несуществующим индексом.
     */
    public PFont getFont(int fontIndex) {
        return (PFont) fonts.get(fontIndex);
    }

    /**
     * Создать фонт размера size на основе фонта font.
     *
     * @param font базовый фонт (на основе диапазона символов)
     * @param size размер нового фонта
     * @return
     */
    public PFont createReSizedFont(PFontDiapazon font, int size) {
        ArrayList characters = font.getCharacters();
        int length = characters.size();
        char firstIndex = font.getFirstIndex();
        PFontDiapazon reSizedFont = new PFontDiapazon(firstIndex, length, size, size >> 1);

        for (int i = 0; i < length; i++) {
            PSprite oldSprite = (PSprite) characters.get(i);
            ArrayList indexes = oldSprite.getIndexes();
            int indexesLength = indexes.size();
            int oldSize = font.getSize();

            PSprite sprite = new PSprite(indexesLength);

            Index oldIndex = (Index) indexes.get(0);
            sprite.add(new Index(oldIndex.getPrototype(), oldIndex.getX() * size / oldSize, oldIndex.getY() * size / oldSize));

            if (i == 0) {
                for (int j = 1; j <= indexesLength - 3; j++) {
                    oldIndex = (Index) indexes.get(j);
                    PFrame oldFrame = (PFrame) oldIndex.getPrototype();
                    PAffine affine = new PAffine(1);
                    affine.add(new Index(oldFrame, (int)((oldIndex.getX() * (double)size) / oldSize), (int)((oldIndex.getY() * (double)size) / oldSize)));
                    affine.setScalingX(size*100/oldSize);
                    affine.setScalingY(size*100/oldSize);
                    sprite.add(new Index(affine, 0, 0));
                }

                oldIndex = (Index) indexes.get(indexesLength - 2);
                sprite.add(oldIndex);

                oldIndex = (Index) indexes.get(indexesLength - 1);
                sprite.add(new Index(oldIndex.getPrototype(), size, 0));
            } else {
                for (int j = 1; j < indexesLength; j++) {
                    oldIndex = (Index) indexes.get(j);
                    PFrame oldFrame = (PFrame) oldIndex.getPrototype();
                    PAffine affine = new PAffine(1);
                    affine.add(new Index(oldFrame, (int)((oldIndex.getX() * (double)size) / oldSize), (int)((oldIndex.getY() * (double)size) / oldSize)));
                    affine.setScalingX(size*100/oldSize);
                    affine.setScalingY(size*100/oldSize);
                    sprite.add(new Index(affine, 0, 0));
                }
            }

            reSizedFont.addSprite((char) (i + firstIndex), sprite);
        }
        return reSizedFont;
    }


    /**
     * Создать фонт размера size на основе составного фонта font(сам составной фонт должен быть составлен на основе
     * PFontDiapazon-ов).
     *
     * @param font базовый фонт
     * @param size размер нового фонта
     * @return
     */
    public PFont createReSizedFont(PFontComposite font, int size) {
        int amount = font.getFontsAmount();
        ArrayList fonts = new ArrayList(font.getFontsAmount());
        for (int i = 0; i < amount; i++) {
            fonts.add(createReSizedFont((PFontDiapazon) font.getFont(i), size));
        }
        return new PFontComposite(fonts);
    }

    public void addFont(PFont font) {
        fonts.add(font);
    }
}
