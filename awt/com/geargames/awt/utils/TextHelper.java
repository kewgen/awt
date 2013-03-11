package com.geargames.awt.utils;

import com.geargames.ConsoleDebug;
import com.geargames.awt.Drawable;
import com.geargames.common.String;
import com.geargames.common.Graphics;
import com.geargames.common.env.SystemEnvironment;
import com.geargames.common.util.ArrayChar;
import com.geargames.common.util.ArrayHelper;
import com.geargames.common.util.ArrayInt;
import com.geargames.common.util.Region;

/**
 * user: Mikhail V. Kutuzov
 * date: 19.11.11
 * time: 19:26
 */
public class TextHelper {

    private static char SPACE = ' ';
    private static char CARRIAGE_DOWN = '\n';

    /**
     * Метод предназначен для индексирования строки. Разбиения строки на отрезки всегда происходит по
     * границам слов(символам пробела) или перевода строки.
     *
     * @param data     строка , предназначенная для разбиения
     * @param region   область экрана где должна разместится разбитая строка data
     * @param graphics графический контекст на котором будем рисовать строчку data в region
     * @param format   настройки форматирования для региона(здесь нас интересует только форматирование по Х )
     * @return массив состоящий из элементов [индекс первого символа подстроки][количество символов подстроки][координта подстроки на graphics по Х]
     */
    public static int[] textIndexing(String data, final Region region, Graphics graphics, int format) {
        int count = 3 * (1 + graphics.getWidth(data) / region.getWidth());
        count += getCarrigeDownsAmount(data) + 1;
        ArrayInt indexes = new ArrayInt(3 + (count * 3));

        ArrayChar characters = new ArrayChar(data.length());
        for (int i = 0; i < characters.length(); i++) {
            characters.set(i, data.charAt(i));
        }
        ArrayInt separators = getSeparatorsArray(data);

        int counter = 0;
        int activeIndex = 0;
        int previousIndex = 0;
        boolean complete = false;
        for (int i = 0; i < separators.length(); i++) {
            complete = false;
            int index = separators.get(i);
            if (characters.get(index) == CARRIAGE_DOWN) {
                indexes.set(counter, activeIndex);
                if (graphics.getWidth(characters, activeIndex, index - activeIndex) < region.getWidth()) {
                    indexes.set(counter + 1, index - activeIndex);
                    indexes.set(counter + 2, ScrollHelper.getXTextBegin(format, region, graphics.getWidth(characters, activeIndex, index - activeIndex)));
                    activeIndex = index + 1;
                    complete = true;
                } else {
                    if (previousIndex - activeIndex > 1) {
                        indexes.set(counter + 1, previousIndex - activeIndex);
                        indexes.set(counter + 2, ScrollHelper.getXTextBegin(format, region, graphics.getWidth(characters, activeIndex, previousIndex - activeIndex)));
                        activeIndex = previousIndex;
                        i--;
                        continue;
                    } else {
                        //todo длинная строка
                        indexes.set(counter + 1, index - activeIndex);
                        indexes.set(counter + 2, ScrollHelper.getXTextBegin(format, region, region.getWidth()));
                        activeIndex = index + 1;
                        complete = true;
                    }
                }
                counter += 3;
            } else {
                if (graphics.getWidth(characters, activeIndex, index - activeIndex) > region.getWidth()) {
                    if (previousIndex - activeIndex > 1) {
                        indexes.set(counter, activeIndex);
                        indexes.set(counter + 1, previousIndex - activeIndex);
                        indexes.set(counter + 2, ScrollHelper.getXTextBegin(format, region, graphics.getWidth(characters, activeIndex, previousIndex - activeIndex)));
                        activeIndex = previousIndex;
                    } else {
                        //todo длинная строка
                        indexes.set(counter, activeIndex);
                        indexes.set(counter + 1, index - activeIndex);
                        indexes.set(counter + 2, ScrollHelper.getXTextBegin(format, region, region.getWidth()));
                        activeIndex = index + 1;
                        complete = true;
                    }
                    counter += 3;
                }
            }
            previousIndex = index + 1;
        }
        if (!complete) {
            indexes.set(counter, activeIndex);
            indexes.set(counter + 1, previousIndex - activeIndex);
            indexes.set(counter + 2, ScrollHelper.getXTextBegin(format, region, graphics.getWidth(characters, activeIndex, previousIndex - activeIndex)));
            counter += 3;
        }
        separators.free();
        int[] result = new int[counter];
        for (int i = 0; i < result.length; i++) {
            result[i] = indexes.get(i);
        }
        indexes.free();
        return result;
    }

    private static ArrayInt getSeparatorsArray(String data) {
        int length = data.length();
        int amount = 0;
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                char tmp = data.charAt(i);
                if (tmp == SPACE || tmp == CARRIAGE_DOWN) {
                    amount++;
                }
            }
            if (!isSeparator(data.charAt(0))) {
                amount++;
            }
            if (!isSeparator(data.charAt(length - 1))) {
                amount++;
            }
            ArrayInt separators = new ArrayInt(amount);
            int count = 0;
            for (int i = 0; i < length; i++) {
                if (i == 0 && !isSeparator(data.charAt(i))) {
                    separators.set(count++, i);
                } else if (i == length - 1 && !isSeparator(data.charAt(i))) {
                    separators.set(count++, i);
                } else if (isSeparator(data.charAt(i))) {
                    separators.set(count++, i);
                }
            }
            return separators;
        } else {
            return new ArrayInt(0);
        }
    }

    private static boolean isSeparator(char character) {
        return character == SPACE || character == CARRIAGE_DOWN;
    }

    public static int getMaxWordLength(String tmp, Graphics graphics) {
        int previousEnd = -1;
        int end = 0;
        int length = 0;
        int len;
        ArrayChar string = new ArrayChar(tmp.length());
        for (int i = 0; i < string.length(); i++) {
            string.set(i, tmp.charAt(i));
        }
        while ((end = getNextTagIndex(string, previousEnd + 1)) != -1) {
            len = graphics.getWidth(string, previousEnd + 1, end + 1 - (previousEnd >= 0 ? previousEnd : 0));
            if (len > length) {
                length = len;
            }
            previousEnd = end;
        }
        len = graphics.getWidth(tmp.substring(previousEnd + 1));
        len = (len > length ? len : length);
        if (Drawable.DEBUG) {
            SystemEnvironment.getInstance().getDebug().trace(String.valueOfC("the longest word length ").concat(len));
        }
        string.free();
        return len;
    }

    public static int getRowHeight(Graphics graphics) {
        return graphics.getFontSize();
    }

    public static int getNextTagIndex(ArrayChar string, int from) {
        int spaceIndex = ArrayHelper.findNext(string, from, SPACE);
        int carriageIndex = ArrayHelper.findNext(string, from, CARRIAGE_DOWN);
        return spaceIndex > carriageIndex ? (carriageIndex != -1 ? carriageIndex : spaceIndex) : (spaceIndex != -1 ? spaceIndex : carriageIndex);
    }

    public static int getCarrigeDownsAmount(com.geargames.common.String string) {
        int length = string.length();
        int counter = 0;
        for (int i = 0; i < length; i++) {
            if (string.charAt(i) == CARRIAGE_DOWN) {
                counter++;
            }
        }
        return counter;
    }
}
