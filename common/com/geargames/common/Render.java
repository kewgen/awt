package com.geargames.common;

import com.geargames.common.packer.*;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 21.09.12
 * Time: 14:46
 */
public interface Render {
    //тип шрифта
    byte FONT_SYSTEM = -1;
    byte FONT_BIG = 0;
    byte FONT_SMALL = 1;
    byte STRING_COLOR_YELLOW = 0;
    byte STRING_COLOR_RED = 1;
    byte STRING_COLOR_BLUE = 2;
    byte WIDTH = 0;
    byte HIGHT = 1;
    //типы индексного элемента
    byte T_FRAME = 0;
    byte T_SPRITE = 1;
    byte T_ANIMATION = 2;
    byte T_UNIT = 3;
    byte T_UNIT_SCRIPT = 4;
    byte T_OBJ = 5;
    byte T_EMITTER = 11;
    byte T_AFFINE = 12;

    public void create();//инициализация переменных пакера

    PObject getObject(int pid);

    PUnitScript getUnitScript(int pid);

    PUnit getUnit(int pid);

    PAnimation getAnimation(int pid);

    PAffine getAffine(int pid);

    PSprite getSprite(int pid);

    PFrame getFrame(int pid);

    void setODX(int dx);//смещение всей отрисовки

    void setODY(int dy);

    void setStringColor(byte color);

    byte getStringColor();

    int getSpriteDx(int id);//запрос Xа первого по списку индекса

    public int getFrameW(int id);

    public int getFrameH(int id);

    EmitProcess createEmitter(int pos, int i, int i1);


    /**@deprecated*/
    void drawString(Graphics graphics, String string, int x, int y, int anchor, byte font);

    /**@deprecated*/
    void setFont(byte font);

    /**@deprecated*/
    byte getFont();

    /**@deprecated*/
    int getSpriteId(int c);

    /**@deprecated*/
    int getStringWidth(String string);

    /**@deprecated*/
    int getCharWidth(int id);

    /**@deprecated*/
    int getCustomFontHeight();

    /**@deprecated*/
    int getCustomFontBaseLine();

}
