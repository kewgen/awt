package com.geargames.common;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 05.05.12
 * Time: 13:59
 * для встроенных шрифтов
 */
public interface FontMetrics {

    public int stringWidth(java.lang.String characters);

    public int getHeight();

    public int charWidth(char character);

    public int getAscent();

}
