package com.geargames.awt.utils;

import com.geargames.Debug;
import com.geargames.awt.Drawable;
import com.geargames.common.String;
import com.geargames.packer.Graphics;

/**
 * user: Mikhail V. Kutuzov
 * date: 19.11.11
 * time: 19:26
 */

/**
 * user: Mikhail V. Kutuzov
 * date: 19.11.11
 * time: 19:26
 */
public class TextHelper {

    private static String SPACE = String.valueOfC(" ");
    private static String CARRIAGE_DOWN = String.valueOfC("\n");

    /**
     * Метод предназначен для индексирования строки. Разбиения строки на отрезки всегда происходит по
     * границам слов(символам пробела) с помощью массива индексов\длинн строк.
     * состав массива: [индекс первого символа][координата строки на graphics][индекс последнего сивола]
     * [индекс последнего сивола] - индекс пробела, если строка не последняя
     * если последняя , то [индекс последнего сивола] - индекс последнего символа разбиваемой строки.
     *
     * @param data       строка , предназначенная для разбиения
     * @param region     область экрана где должна разместится разбитая строка data
     * @param graphics   графический контекст на котором будем рисовать строчку data в region
     * @param fontMetric метрика шрифта которым будем рисовать на graphics
     * @param format     настройки форматирования для региона(здесь нас интересует только форматирование по Х )
     * @return массив состоящий из элементов [индекс первого символа][координта строки на graphics по Х][индекс последнего сивола]
     */
    public static int[] indexData(String data, final Region region, Graphics graphics, FontMetric fontMetric, int format) {
        int count = (fontMetric.getWidth(data, graphics) / region.getWidth());
        count <<= 1;
        count += getTagsAmount(data) + 1;
        int[] indexes = new int[3 + (count << 1)];
        int j = 0;
        int previousIndex = 0;
        String temp = String.valueOf(data);
        int index = 0;
        indexes[j++] = previousIndex;
        int width = 0;
        int previousWidth = 0;
        String tag = null;
        while (temp.length() > 0 && (index = getNextTagIndex(temp, previousIndex + 1)) != -1) {
            String sub = temp.substring(0, index);
            width = fontMetric.getWidth(sub, graphics);
            if (width > region.getWidth()) {
                if (Drawable.DEBUG) {
                    Debug.trace(data.toString());
                }
                if (previousIndex == -1) {
                    if (Drawable.DEBUG) {
                        Debug.trace("" + region.getWidth());
                        Debug.trace("" + j);
                        for (int i = 1; i < j - 1; i += 2) {
                            Debug.trace(data.substring(indexes[i - 1], indexes[i + 1]).toString());
                        }
                        Debug.trace("the longest word " + sub);
                        Debug.trace("the longest word length = " + width);
                    }
                    temp = temp.substring(index + 1);
                    indexes[j++] = ScrollHelper.getXTextBegin(format, region, width);
                    indexes[j] = indexes[j - 2] + index + 1;
                    j++;
                } else {
                    temp = temp.substring(previousIndex + 1);
                    indexes[j++] = ScrollHelper.getXTextBegin(format, region, previousWidth);
                    indexes[j] = indexes[j - 2] + previousIndex + 1;
                    j++;
                    previousIndex = -1;
                }
            } else if (temp.charAt(index) == CARRIAGE_DOWN.charAt(0)) {
                temp = temp.substring(index + 1);
                indexes[j++] = ScrollHelper.getXTextBegin(format, region, width);
                indexes[j] = indexes[j - 2] + index + 1;
                j++;
                previousIndex = -1;
            } else {
                previousIndex = index;
                previousWidth = width;
            }
        }
        width = fontMetric.getWidth(temp, graphics);
        if (width <= region.getWidth()) {
            indexes[j++] = ScrollHelper.getXTextBegin(format, region, width);
            indexes[j] = data.length();
        } else {
            indexes[j++] = ScrollHelper.getXTextBegin(format, region, previousWidth - fontMetric.getWidth(SPACE, graphics));
            indexes[j] = indexes[j - 2] + previousIndex + 1;
            j++;
            temp = temp.substring(previousIndex + 1);
            width = fontMetric.getWidth(temp, graphics);
            indexes[j++] = ScrollHelper.getXTextBegin(format, region, width);
            indexes[j] = data.length();
        }

        int[] result = new int[j + 1];
        for (int i = 0; i < result.length; i++) {
            result[i] = indexes[i];
        }
        return result;
    }

    public static int getMaxWordLength(String tmp, Graphics graphics, FontMetric fontMetric) {
        int previouseEnd = -1;
        int end = 0;
        int length = 0;
        int len;
        String temp;
        while ((end = getNextTagIndex(tmp, previouseEnd + 1)) != -1) {
            temp = tmp.substring(previouseEnd + 1, end + 1);
            len = fontMetric.getWidth(temp, graphics);
            if (len > length) {
                length = len;
            }
            previouseEnd = end;
        }
        len = fontMetric.getWidth(tmp.substring(previouseEnd + 1), graphics);
        len = (len > length ? len : length);
        if (Drawable.DEBUG) {
            Debug.trace("the longest word length " + len);
        }
        return len;
    }

    public static int getRowHeight(Graphics graphics, FontMetric metric) {
        return metric.getHeigth(graphics);
    }

    public static int getNextTagIndex(String string, int from) {
        int spaceIndex = string.indexOf(SPACE, from);
        int carriageIndex = string.indexOf(CARRIAGE_DOWN, from);
        return spaceIndex > carriageIndex ? (carriageIndex != -1 ? carriageIndex : spaceIndex) : (spaceIndex != -1 ? spaceIndex : carriageIndex);
    }

    public static int getTagsAmount(com.geargames.common.String string) {
        int length = string.length();
        int counter = 0;
        for (int i = 0; i < length; i++) {
            if (string.charAt(i) == CARRIAGE_DOWN.charAt(0)) {
                counter++;
            }
        }
        return counter;
    }
}
