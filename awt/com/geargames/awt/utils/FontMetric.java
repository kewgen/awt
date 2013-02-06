package com.geargames.awt.utils;

import com.geargames.packer.Graphics;

/**
 * user: Mikhail V. Kutuzov
 * date: 26.10.11
 * time: 23:45
 */
public abstract class FontMetric {
    /**
     * Возвращаем высоту буквы в пикселях в данном графическом контексте.
     *
     * @param graphics
     * @return
     */
    public abstract int getHeigth(Graphics graphics);

    /**
     * Возвращаем ширину буквы в этом графическом контексте
     *
     * @param character a character to be measured in this context
     * @param graphics  the graphical context
     * @return quantity of pixels
     */
    public abstract int getWidth(char character, Graphics graphics);

    /**
     * Возвращаем ширину строки букв(вообще: не равна сумме ширин букв в строке)
     *
     * @param characters string to mesure
     * @param graphics   the graphical context
     * @return quantity of pixels
     */
    public abstract int getWidth(com.geargames.common.String characters, Graphics graphics);


    public abstract int getAscent(Graphics graphics);
}
