package com.geargames.common;

import com.geargames.common.packer.*;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 21.09.12
 * Time: 14:46
 */
public interface Render {
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
}
