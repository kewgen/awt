package com.geargames.awt.utils;

import com.geargames.Debug;
import com.geargames.awt.Drawable;
import com.geargames.common.String;
import com.geargames.common.Graphics;
import com.geargames.common.util.Region;

/**
 * user: Mikhail V. Kutuzov
 * date: 19.11.11
 * time: 19:26
 */
public class TextHelper {

    /**
     * Метод предназначен для разбиения текста на подстроки (индексирования). Разбиение всегда происходит по
     * границам слов (символам пробела) или символам перевода строки.
     * Состав массива:
     * набор пар значений [индекс первого символа подстроки][координата подстроки на graphics] и еще одно значение
     * [индекс последнего символа] в самом конце массива, где:
     * [индекс первого символа подстроки] - индекс символа, с которого начинается очередная подстрока;
     * [координата подстроки на graphics] - смещение подстроки по горизонтали слева, вычисляется по заданному
     * горизонтальному выравниванию;
     * [индекс последнего символа] - это индекс пробела, если строка не последняя,
     * если последняя, то [индекс последнего символа] - это индекс последнего символа разбиваемой строки.
     * [индекс последнего символа] не может быть символом перевода строки, если так, то этот индекс указывает
     * на предыдущий символ, если предыдущий символ тоже символ перевода строки или [индекс последнего символа]
     * это первый символ строки...
     *
     * @param text      строка, предназначенная для разбиения на подстроки
     * @param region    прямоугольная область экрана, в пределах которой должен разместиться заданный текст
     * @param graphics  графический контекст на котором будет отрисовываться text в region
     * @param format    настройки горизонтального выравнивания текста (вертикальное выравнивание не учитывается)
     * @return массив состоящий из пар элементов [индекс первого символа][координата строки на graphics по Х].
     * Данный массив всегда имеет нечетную длинну
     */
    public static int[] textIndexing(String text, final Region region, Graphics graphics, int format) {
        int count = graphics.getWidth(text) / region.getWidth();
        count *= 2;
        count += getLineSeparatorAmount(text) + 1;
        int[] indexes = new int[3 + (count * 2)];
        int offsetIndexSubLine = 0;
        int previousIndex = 0;
        String textRemains = String.valueOf(text);
        int index = 0;
        indexes[offsetIndexSubLine++] = previousIndex;
        int width = 0;
        int previousWidth = 0;
        while (textRemains.length() > 0 && (index = getNextWordBreakIndex(textRemains, previousIndex + 1)) != -1) {
            String sub = textRemains.substring(0, index);
            width = graphics.getWidth(sub);
            if (width > region.getWidth()) {
                if (Drawable.DEBUG) {
                    Debug.trace(text.toString());
                }
                if (previousIndex == -1) {
                    if (Drawable.DEBUG) {
                        Debug.trace("" + region.getWidth());
                        Debug.trace("" + offsetIndexSubLine);
                        for (int i = 1; i < offsetIndexSubLine - 1; i += 2) {
                            Debug.trace(text.substring(indexes[i - 1], indexes[i + 1]).toString());
                        }
                        Debug.trace("the longest word " + sub);
                        Debug.trace("the longest word length = " + width);
                    }
                    textRemains = textRemains.substring(index + 1);
                    indexes[offsetIndexSubLine++] = ScrollHelper.getXTextBegin(format, region, width);
                    indexes[offsetIndexSubLine] = indexes[offsetIndexSubLine - 2] + index + 1;
                    offsetIndexSubLine++;
                } else {
                    textRemains = textRemains.substring(previousIndex + 1);
                    indexes[offsetIndexSubLine++] = ScrollHelper.getXTextBegin(format, region, previousWidth);
                    indexes[offsetIndexSubLine] = indexes[offsetIndexSubLine - 2] + previousIndex + 1;
                    offsetIndexSubLine++;
                    previousIndex = -1;
                }
            } else if (textRemains.charAt(index) == String.LINE_SEPARATOR) {
                textRemains = textRemains.substring(index + 1);
                indexes[offsetIndexSubLine++] = ScrollHelper.getXTextBegin(format, region, width);
                indexes[offsetIndexSubLine] = indexes[offsetIndexSubLine - 2] + index + 1;
                offsetIndexSubLine++;
                previousIndex = -1;
            } else {
                previousIndex = index;
                previousWidth = width;
            }
        }
        width = graphics.getWidth(textRemains);
        if (width <= region.getWidth()) {
            indexes[offsetIndexSubLine++] = ScrollHelper.getXTextBegin(format, region, width);
            indexes[offsetIndexSubLine] = text.length();
        } else {
            indexes[offsetIndexSubLine++] = ScrollHelper.getXTextBegin(format, region, previousWidth - graphics.getWidth(String.SPACE));
            indexes[offsetIndexSubLine] = indexes[offsetIndexSubLine - 2] + previousIndex + 1;
            offsetIndexSubLine++;
            textRemains = textRemains.substring(previousIndex + 1);
            width = graphics.getWidth(textRemains);
            indexes[offsetIndexSubLine++] = ScrollHelper.getXTextBegin(format, region, width);
            indexes[offsetIndexSubLine] = text.length();
        }

        int[] result = new int[offsetIndexSubLine + 1];
        for (int i = 0; i < result.length; i++) {
            result[i] = indexes[i];
        }
        return result;
    }

    public static int getMaxWordLength(String tmp, Graphics graphics) {
        int previousEnd = -1;
        int end = 0;
        int length = 0;
        while ((end = getNextWordBreakIndex(tmp, previousEnd + 1)) != -1) {
            String temp = tmp.substring(previousEnd + 1, end + 1);
            int len = graphics.getWidth(temp);
            if (len > length) {
                length = len;
            }
            previousEnd = end;
        }
        int len = graphics.getWidth(tmp.substring(previousEnd + 1));
        len = (len > length ? len : length);
        if (Drawable.DEBUG) {
            Debug.trace("the longest word length " + len);
        }
        return len;
    }

    public static int getRowHeight(Graphics graphics) {
        return graphics.getFontSize();
    }

    /**
     * Находит индекс символа-разделителя слов (пробельный символ или символ-разделитель строк).
     * Поиск начинается с индекса from.
     */
    public static int getNextWordBreakIndex(String string, int from) {
        int spaceIndex = string.indexOf(String.SPACE, from);
        int carriageIndex = string.indexOf(String.LINE_SEPARATOR, from);
        return spaceIndex > carriageIndex ? (carriageIndex != -1 ? carriageIndex : spaceIndex) : (spaceIndex != -1 ? spaceIndex : carriageIndex);
    }

    /**
     * Подсчитывает количество символов-разделителей строк.
     */
    public static int getLineSeparatorAmount(String string) {
        int length = string.length();
        int counter = 0;
        for (int i = 0; i < length; i++) {
            if (string.charAt(i) == String.LINE_SEPARATOR) {
                counter++;
            }
        }
        return counter;
    }
}
