package com.geargames.common.packer;

import com.geargames.common.Graphics;
import com.geargames.common.Image;
import com.geargames.common.Port;
import com.geargames.common.String;
import com.geargames.common.util.ArrayList;
import com.geargames.common.util.ArrayShortDual;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 19.09.12
 * Time: 13:51
 * старая схема работы с пакером
 */
/**
 * @deprecated
 */
public abstract class Render implements com.geargames.common.Render {

    protected boolean DEBUG = false;

    protected static Object self;//наследник должен проинициализировать предка!

    public static com.geargames.common.Render getInstance() {//Deprecated
        return (com.geargames.common.Render) self;
    }

    public void create() {
        string_sprites = new int[STRING_SPRITES_MAX];//строка в спрайтовых ид
        string_sprites_w = new int[STRING_SPRITES_MAX];//ширина каждого спрайта

        //не забываем проинитить
//        arr_i = loader.getData().getArray();
//        SPR_FONT_SYMB = Graph.SPR_FONT_SYMB;
//        SPR_FONT_LATIN = Graph.SPR_FONT_LATIN;
//        SPR_FONT_CYRILLIC = Graph.SPR_FONT_CYRILLIC;
//        AFFINES_COUNT =
//        и все переменные Graph

    }

    public void renderObject(Graphics g, int obj_cur) {
        renderObject(g, null, obj_cur, 0, 0);
    }

    public void renderObject(Graphics g, Object obj, int pid, int o_x, int o_y) {//Тип - Object

        int o_i_slot = 0;
        int o_s = arr_i[OBJ_S][pid];
        int o_e = arr_i[OBJ_E][pid];
        for (int o_i_cur = o_s; o_i_cur <= o_e; o_i_cur++) {
            int o_i_id = arr_i[OBJ_I_ID][o_i_cur];
            int o_i_type = arr_i[OBJ_I_TYPE][o_i_cur];
            int o_i_layer_t = arr_i[OBJ_I_LAYER_TYPE][o_i_cur];
            int o_i_x = arr_i[OBJ_I_X][o_i_cur];
            int o_i_y = arr_i[OBJ_I_Y][o_i_cur];
            o_dx = o_dy = 0;

            if (o_i_layer_t == LAYER_T_SLOT) {
                o_i_slot = arr_i[OBJ_I_SLOT][o_i_cur];

                int result = renderObjSet(g, obj, pid, o_x + o_i_x, o_y + o_i_y, o_i_cur, o_i_slot, o_i_id);

                if (result == -1) continue;
                else o_i_id += result;
            }

            if (g == null) continue;//при расчёте списка запускается служебная отрисовка
            if (o_i_type == T_FRAME) {//элемент
                renderFrame(g, o_i_id, o_x + o_i_x + o_dx, o_y + o_i_y + o_dy, null);
            } else if (o_i_type == T_SPRITE) {//спрайт
                renderSprite(g, o_i_id, o_x + o_i_x + o_dx, o_y + o_i_y + o_dy);
            } else if (o_i_type == T_ANIMATION) {
                int shift = getField(OBJ_I_SHIFT, o_i_cur);//доп смещение кадра из поля shift
                renderAnimation(g, o_i_id, o_x + o_i_x + o_dx, o_y + o_i_y + o_dy, getTick() + shift);
            } else if (o_i_type == T_UNIT_SCRIPT) {
                renderUnitScript(g, o_i_id, o_x + o_i_x + o_dx, o_y + o_i_y + o_dy, -1, null);
            } else if (o_i_type == T_OBJ) {
                renderObject(g, obj, o_i_id, o_x + o_i_x + o_dx, o_y + o_i_y + o_dy);
            } else if (o_i_type == T_AFFINE) {
                renderAffine(g, o_i_id, o_x + o_i_x + o_dx, o_y + o_i_y + o_dy);
            }
        }
    }

    //внутренняя обработка и рендер объекта
    abstract protected int renderObjSet(Graphics g, Object obj, int obj_cur, int o_x, int o_y, int o_i_cur, int o_i_slot, int o_i_id);

    public void renderUnitScript(Graphics g, int pid, int dx, int dy, int un_frame, Object unit) {//Тип - Unit script
        //try {
        if (un_frame == -1) un_frame = (getTick() >> getFPSMult()) % getUnitFrameCount(pid);
        if (un_frame >= getUnitFrameCount(pid)) {
            //Debug.trace(com.geargames.common.String.valueOfC("Warning! Unit frame is exceeds! id:").concatI(pid).concatC(" frame count:").concatI(getUnitFrameCount(pid)).concatC(" frame cur:").concatI(un_frame));
        }
        int uns_i = arr_i[UNS_S][pid] + un_frame;
        int un_cur = arr_i[UNS_I_ID][uns_i];
        int uns_x = arr_i[UNS_I_X][uns_i];
        int uns_y = arr_i[UNS_I_Y][uns_i];
        renderUnit(g, un_cur, dx + uns_x, dy + uns_y, unit);
//            } catch (Exception e) {
        //log("img_pack:" + img_pack + ", pack_type:" + pack_type + ", uns_cur:" + uns_cur);
//                Debug.logEx(e);
//            }
    }

    public int getUnitFrameCount(int uns_cur) {
        return arr_i[UNS_E][uns_cur] - arr_i[UNS_S][uns_cur] + 1;
    }

    public void renderUnit(Graphics g, int pid, int dx, int dy, Object unit) {//Тип - Unit
        int pe_s = arr_i[UN_S][pid];
        int pe_e = arr_i[UN_E][pid];
        for (int i = pe_s; i <= pe_e; i++) {
            int un_x = arr_i[UN_I_X][i];
            int un_y = arr_i[UN_I_Y][i];
            int un_i_cur = arr_i[UN_I_ID][i];
            int un_type = arr_i[UN_I_TYPE][i];
            int un_tile = arr_i[UN_I_BODY_TYLE][i];

            if (un_type == T_FRAME) {//выделяем флаг вывода элемента или спрайта    UNIT TINU
                renderFrame(g, un_i_cur, dx + un_x, dy + un_y, null);
            } else if (un_type == T_SPRITE) {
                renderSprite(g, un_i_cur, dx + un_x, dy + un_y);
            } else if (un_type == T_OBJ) {
                renderObject(g, null, un_i_cur, dx + un_x, dy + un_y);
            } else if (un_type == T_ANIMATION) {
                renderAnimation(g, un_i_cur, dx + un_x, dy + un_y, -1);
            } else if (un_type == T_AFFINE) {
                renderAffine(g, un_i_cur, dx + un_x, dy + un_y);
            }
        }
    }

    public void renderAnimation(Graphics g, int pid, int dx, int dy, int tick) {
        if (tick == -1) tick = getTick() >> getFPSMult();

        int ind = arr_i[AN_S][pid] + tick % (arr_i[AN_E][pid] - arr_i[AN_S][pid] + 1);
        int o_i_type = arr_i[AN_I_TYPE][ind];
        if (o_i_type == T_FRAME) {
            renderFrame(g, arr_i[AN_I_ID][ind], dx + arr_i[AN_I_X][ind], dy + arr_i[AN_I_Y][ind], null);
        } else if (o_i_type == T_SPRITE) {
            renderSprite(g, arr_i[AN_I_ID][ind], dx + arr_i[AN_I_X][ind], dy + arr_i[AN_I_Y][ind]);
        } else if (o_i_type == T_AFFINE) {
            renderAffine(g, arr_i[AN_I_ID][ind], dx + arr_i[AN_I_X][ind], dy + arr_i[AN_I_Y][ind]);
        }
    }

    protected void affinesGenerate() {
        affinnes = new ArrayList(AFFINES_COUNT);

        for (int pid = 0; pid < AFFINES_COUNT; pid++) {
            int pe_i_cur;
            int pe_s = arr_i[AF_S][pid];
            int pe_e = arr_i[AF_E][pid];
            PAffine affine = new PAffine(1);
            affinnes.add(affine);

            affine.setTransparency(arr_i[AffineTransparency][pid]);
            affine.setRotate(arr_i[AffineRotate][pid]);
            affine.setScalingX(arr_i[AffineScalingX][pid] & 0xff);
            affine.setScalingY(arr_i[AffineScalingY][pid] & 0xff);
            affine.setHmirror(arr_i[AffineHMirror][pid] == 1);
            affine.setVmirror(arr_i[AffineVMirror][pid] == 1);

            for (int per_cur = pe_s; per_cur <= pe_e; per_cur++) {//предполагаем, что афины состоят из одного индекса
                affine.setX(arr_i[AF_I_X][per_cur]);
                affine.setY(arr_i[AF_I_Y][per_cur]);
            }

        }
    }

    public void renderAffine(Graphics g, int pid, int dx, int dy) {//Тип - Affine
        if (pid == 0) return;
        //Важно проинициализировать массив Афинн в классе наследнике
        //if (affinnes == null) affinnes = new Affine[Graph.AFFINES_COUNT];

//            try {
        int pe_i_cur;
        int pe_s = arr_i[AF_S][pid];
        int pe_e = arr_i[AF_E][pid];
        PAffine affine = (PAffine) affinnes.get(pid);
        for (int per_cur = pe_s; per_cur <= pe_e; per_cur++) {
            pe_i_cur = arr_i[AF_I_ID][per_cur];
            affine.setX(arr_i[AF_I_X][per_cur]);
            affine.setY(arr_i[AF_I_Y][per_cur]);
            renderFrame(g, pe_i_cur, dx + affine.getX(), dy + affine.getY(), affine);
        }
//            } catch (Exception e) {
//                Debug.trace(String.valueOfC("Error renderAffine, sprite_cur:").concatI(pid));
//                Debug.logEx(e);
//            }

    }

    public void renderSprite(Graphics g, int pid, int dx, int dy) {//Тип - Sprite
        if (pid == 0) return;
//            try {
        int pe_i_cur;
        int pe_s = arr_i[SP_S][pid];
        int pe_e = arr_i[SP_E][pid];
        for (int per_cur = pe_s; per_cur <= pe_e; per_cur++) {
            pe_i_cur = arr_i[SP_I_ID][per_cur];
            int sprite_x_ = arr_i[SP_I_X][per_cur];
            int o_i_type = arr_i[SP_I_TYPE][per_cur];
            if (o_i_type == T_FRAME) {//выделяем флаг вывода элемента или спрайта
                renderFrame(g, pe_i_cur, dx + sprite_x_, dy + arr_i[SP_I_Y][per_cur], null);
            } else if (o_i_type == T_SPRITE) {
                renderSprite(g, pe_i_cur, dx + sprite_x_, dy + arr_i[SP_I_Y][per_cur]);
            } else if (o_i_type == T_AFFINE) {
                renderAffine(g, pe_i_cur, dx + sprite_x_, dy + arr_i[SP_I_Y][per_cur]);
            } else {
                throw new IllegalArgumentException("Unknown type:" + o_i_type);
            }
        }
//            } catch (Exception e) {
//                Debug.trace(String.valueOfC("Error renderSprite, pid:").concatI(pid));
//                Debug.logEx(e);
//            }
    }

    public void renderFrame(Graphics g, int pid, int dx, int dy, PAffine affine) {//Тип - Element
        int img_num = arr_i[FR_IMAGE_ID][pid];

        if (img_num < IMG_COUNT) {
            Image image = getImage(img_num);
            if (image == null) {
//                    Debug.trace(String.valueOfC("Image is null id:").concatI(img_num));// + " el:" + pid);
                return;
            }
            if (!drawRegion(g, image, arr_i[FR_X][pid] + src_dx, arr_i[FR_Y][pid] + src_dy, arr_i[FR_W][pid]
                    , arr_i[FR_H][pid], dx, dy, 0, affine)) {
//                    Debug.trace(String.valueOfC("ERROR! See packer pid:").concatI(pid).concatC("\telem_xy:").concatI(dx).concatC(",").concatI(dy));

            }
        } else {//geometry
            g.setColor(getCOLOR(arr_i[FR_COLOR][pid]));
            renderGeometry(g, pid, dx, dy, img_num);
        }
    }

    protected void renderGeometry(Graphics g, int pid, int dx, int dy, int figure) {
        if (figure == ELEMENT_LINE) {
        } else if (figure == ELEMENT_RECT) {
            int x = dx + (arr_i[FR_X][pid]);
            int y = dy + (arr_i[FR_Y][pid]);
            int w = arr_i[FR_W][pid];
            int h = arr_i[FR_H][pid];
            g.drawRect(x, y, w, h);
        } else if (figure == ELEMENT_FILLRECT) {
            int x = dx + (arr_i[FR_X][pid]);
            int y = dy + (arr_i[FR_Y][pid]);
            int w = arr_i[FR_W][pid];
            int h = arr_i[FR_H][pid];
            g.fillRect(x, y, w, h);
        } else {
//                Debug.trace(String.valueOfC("Figure not found:").concatI(figure));
        }
    }

    public boolean drawRegion(Graphics g, Image src, int x_src, int y_src, int width, int height, int x_dest, int y_dest, int anchor, PAffine affine) {
//        try {

        if (src == null) {
//                    Debug.trace(String.valueOfC("drawRegion Image is null width:").concatI(width).concatC(", height:").concatI(height));
            return false;
        }

        g.drawRegion(src, x_src, y_src, width, height, x_dest, y_dest, affine);
        frame_counter++;

        return true;
//        } catch (Exception ex) {
//                Debug.logEx(ex);
//                int w = src == null ? -1 : src.getWidth();
//                int h = src == null ? -1 : src.getHeight();
//                Debug.trace(String.valueOfC("x_src:").concatI(x_src).concatC(",y_src:").concatI(y_src).concatC(",width:").concatI(width).concatC(",height:").concatI(height).concatC(",x_dest:").concatI(x_dest).concatC(",y_dest:").concatI(y_dest).concatC(",img_w:").concatI(w).concatC(",img_h:").concatI(h));
//            return false;
//        }
    }


    protected void convertData(ArrayShortDual arrayShortDual) {
        //перевод данных к нужным размерам экрана
        //находится в рендере тк значения констант инитятся здесь
        if (Port.IS_DOUBLE_GRAPHIC || Port.IS_FOURTHIRDS_GRAPHIC || Port.IS_HALF_GRAPHIC) {
            //ArrayShortDual arrayShortDual = loader.getData();
            int size = PARAM_BYTE_COUNT + PARAM_SHORT_COUNT + PARAM_INT_COUNT;
            for (int y = 0; y < size; y++) {
                if (y == Render.FR_X ||
                        y == Render.FR_Y ||
                        y == Render.FR_W ||
                        y == Render.FR_H ||
                        y == Render.SP_I_X ||
                        y == Render.SP_I_Y ||
                        y == Render.AN_I_X ||
                        y == Render.AN_I_Y ||
                        y == Render.UN_I_X ||
                        y == Render.UN_I_Y ||
                        y == Render.UNS_I_X ||
                        y == Render.UNS_I_Y ||
                        y == Render.OBJ_I_X ||
                        y == Render.OBJ_I_Y ||
                        y == Render.OBJ_I_YY ||
                        y == Render.EM_I_X ||
                        y == Render.EM_I_Y ||
                        y == Render.AF_I_X ||
                        y == Render.AF_I_Y ||
                        y == Render.EM_P0 ||
                        y == Render.EM_P2 ||
                        y == Render.EM_P3 ||
                        y == Render.EM_P4 ||
                        y == Render.EM_P5 ||
                        y == Render.EM_P12 ||
                        y == Render.EM_P13 ||
                        y == Render.EM_P16 ||
                        y == Render.EM_P17 ||
                        y == Render.EM_P18) {
                    int len = arrayShortDual.length(y);
                    for (int x = 0; x < len; x++) {
                        int value = Port.getConvertedValue(arrayShortDual.get(y, x));
                        arrayShortDual.set(y, x, (short) value);
                    }
                }
            }
        }

    }


    abstract protected int getTick();//глобальный тик

    abstract protected int getFPSMult();//множитель ФПС

    abstract protected Image getImage(int img_num);//запрос картинки к элементу

    abstract protected int getCOLOR(int id);//цвет записанный в указанном фрейме

    public void setODX(int dx) {//смещение отрисовки индекса объекта
        o_dx = dx;
    }

    public void setODY(int dy) {//смещение отрисовки индекса объекта
        o_dy = dy;
    }

    public ArrayList getSpriteWH(int id) {
        int hight = 0;
        int width = 0;
        int pe_i_cur;
        int pe_s = arr_i[Render.SP_S][id];
        int pe_e = arr_i[Render.SP_E][id];
        for (int per_cur = pe_s; per_cur <= pe_e; per_cur++) {
            pe_i_cur = arr_i[Render.SP_I_ID][per_cur];
            int h = 0;
            int w = 0;
            ArrayList wh = getElementWH(pe_i_cur);
            if (arr_i[Render.SP_I_TYPE][per_cur] == 0) {//выделяем флаг вывода элемента или спрайта
                wh = getElementWH(pe_i_cur);
                h = arr_i[Render.SP_I_Y][per_cur] + (Integer)wh.get(HIGHT);
                if (h > hight) hight = h;
                w = arr_i[Render.SP_I_X][per_cur] + (Integer)wh.get(WIDTH);
                if (w > width) width = w;
            } else {
                wh = getSpriteWH(pe_i_cur);
                h = arr_i[Render.SP_I_Y][per_cur] + (Integer)wh.get(HIGHT);
                if (h > hight) hight = h;
                w = arr_i[Render.SP_I_X][per_cur] + (Integer)wh.get(WIDTH);
                if (w > width) width = w;
            }
        }
        //log(e.toString() + ".getSpriteWH, id:" + id);
        ArrayList arr = new ArrayList(2);
        arr.add(width);
        arr.add(hight);
        return arr;
    }

    protected ArrayList getElementWH(int id) {
        ArrayList arr = new ArrayList(2);
        arr.add(arr_i[FR_W][id]);
        arr.add(arr_i[FR_H][id]);
        return arr;
    }


    public int getField(int o_type, int o_i_cur) {
        return arr_i[o_type][o_i_cur];
    }

    public void setClippingRect(int x1, int y1, int x2, int y2) {//область в которой не рисуем полностью попадаемые фреймы. Решается прграмно
        clipping_x1 = x1;
        clipping_y1 = y1;
        clipping_x2 = x2;
        clipping_y2 = y2;
    }

    public void setClipping(boolean flag) {//не рисуем фреймы не попадающие в клип
        isClipping = flag;
    }

    public void setInnerClipping(boolean flag) {//не рисуем фреймы попадающие в клип
        isInerClipping = flag;
    }

    public boolean isInerClipping() {
        return isInerClipping;
    }

    public void dropFrameCounter() {
        frame_counter = 0;
    }

    public int getFrameCounter() {
        return frame_counter;
    }


    public void setSrcDX(int src_dx_) {
        this.src_dx = src_dx_;
    }

    public void setSrcDY(int src_dy_) {
        this.src_dy = src_dy_;
    }

    //работа со строками
    public void drawString(Graphics graphics, String string, int x, int y, int anchor, byte font) {//-1 системный шрифт
        if (string == null) string = String.valueOfC("null");
        if (font >= 0) {
            byte fontOld = getFont();
            setFont(font);
            drawCustomString(graphics, string, x, y, anchor);
            setFont(fontOld);
        } else {
            graphics.drawString(string, x, y, anchor);
        }
    }

    protected void drawString(Graphics graphics, String string, int x, int y, int anchor, boolean custom) {
        if (Port.IS_OPENGL) custom = true;
        if (string == null) string = String.valueOfC("null");
        if (!custom) {
            graphics.drawString(string, x, y, anchor);
        } else {
            drawCustomString(graphics, string, x, y, anchor);
        }
    }

    public void drawCustomString(Graphics graphics, String string, int x, int y, int anchor) {
        if (string == null) string = String.valueOfC("null");
        setStringSprites(string);
        int dx = x;
        int dy = y;
        if (anchor == Graphics.HCENTER) dx -= string_sprites_width / 2;
        else if (anchor == Graphics.RIGHT) dx -= string_sprites_width;
        for (int i = 0; i < string_sprites_len; i++) {
            renderSprite(graphics, string_sprites[i], dx, dy);
            dx += string_sprites_w[i];
        }
    }

    public void setStringColor(byte color) {
        string_color = color;
    }

    public byte getStringColor() {
        return string_color;
    }

    protected void setStringSprites(String string) {//индексирование строки
        if (string == null) return;
        int len = string.length();
        string_sprites_len = 0;
        string_sprites_width = 0;
        for (int i = 0; i < len; i++) {
            char ch = string.charAt(i);
            int c = (int) ch;
            int spr_id = getSpriteId(c);
//            if (string_color == STRING_COLOR_RED && c >= 48 && c <= 57) spr_id = SPR_FONT_NUM_RED + c - 48;//Red color
//            else if (string_color == STRING_COLOR_BLUE && c >= 48 && c <= 57) spr_id = SPR_FONT_NUM_BLUE + c - 48;//Blue color
            if (spr_id > -1) {//рендим букву
                if (i >= STRING_SPRITES_MAX) return;
                string_sprites[i] = spr_id;                 //запись ид
                string_sprites_w[i] = getCharWidth(spr_id); //запись ширины символа
                string_sprites_width += string_sprites_w[i];//сразу считаем общую ширину символа
                string_sprites_len++;
            }
        }
    }

    public int getSpriteId(int c) {
        int spr_id = -1;
        if (c == 123) c = 40;//{ -> (
        else if (c == 125) c = 41;//} -> )
        else if (c == 95) c = 32;//_ -> space
//        if (c >= 32 && c <= 90) {//space-Z
//            spr_id = Render.SPR_FONT_SYMB + c - 32;
//        } else if (c >= 65 && c <= 90) {//A-Z
//            spr_id = Render.SPR_FONT_LATIN + c - 65;
//        } else if (c >= 97 && c <= 122) {//a-z
//            spr_id = Render.SPR_FONT_LATIN + c - 97;
//        } else if (c >= 48 && c <= 57) {//0-9
//            spr_id = Render.SPR_FONT_NUM + c - 48;
//        } else if (c >= 32 && c <= 47) {//Space-/
//            spr_id = Render.SPR_FONT_SYMB + c - 32;
//        } else if (c >= 1040 && c <= 1071) {//kirillic A-Я
//            spr_id = Render.SPR_FONT_CYRILLIC + c - 1040;
//        } else if (c >= 1072 && c <= 1103) {//kirillic а-я
//            spr_id = Render.SPR_FONT_CYRILLIC + c - 1072;
//        } else if (c == 1105) {//kirillic ё
//            spr_id = Render.SPR_FONT_CYRILLIC + 5;
//        } else if (c == 10) {//перенос строки
//            spr_id = Render.SPR_FONT_SYMB;
//        }
        if (spr_id == -1) {
            //Debug.trace(String.valueOfC("Неизвестный символ '").concat((char) c).concatC("' ").concatI(c));
            spr_id = 32;//заменяем на пробел
        }
        return spr_id;
    }

    public int getStringWidth(String string) {
        int width = 0;
        for (int i = string.length() - 1; i >= 0; i--) {
            width += getCharWidth(getSpriteId(string.charAt(i)));
        }
        return width;
    }

    public int getCharWidth(int id) {//ширина символа отмечена в Х первого индекса спрайта
        return arr_i[SP_I_X][arr_i[SP_S][id]];
    }

    public int getCharWidth(char character) {
        int id = getSpriteId(character);
        return getCharWidth(id);
    }

    public int getCustomFontHeight() {
        return fontHeigt;
    }

    public int getCustomFontBaseLine() {//расстояние от нуля по y до верхней точки буквы
        return fontBaseLine;
    }

    public void setFontWidthShift(int fontWidthShift) {
        this.fontWidthShift = fontWidthShift;
    }

    //указатели на начало символов, рус и англ букв в списке спрайтов пакера
    protected int SPR_FONT_LATIN;//начало латинского фонта с кода 32
    protected int SPR_FONT_CYRILLIC;
    protected int SPR_FONT_SYMBOLS;

    protected int fontHeigt = 20;
    protected int fontBaseLine = 10;
    protected int fontWidthShift = 0;//дополнительное межбуквенное расстояние

    public byte font;
    protected byte string_color;

    protected final int STRING_SPRITES_MAX = 300;
    protected int[] string_sprites;//строка в спрайтовых ид
    protected int[] string_sprites_w;//ширина каждого спрайта
    protected int string_sprites_width;//ширина строки
    protected int string_sprites_len;//длина строки

    //рендинг типов из пакера
    protected int o_dx;
    protected int o_dy;

    protected ArrayList affinnes;
    protected int src_dx;
    protected int src_dy;

    protected boolean isClipping;
    protected boolean isInerClipping;
    protected int clipping_x1;
    protected int clipping_y1;
    protected int clipping_x2;
    protected int clipping_y2;

    protected int frame_counter;//счётчик фреймов на один кадр
    protected short arr_i[][];


    //тип слоя индекса
    public static byte LAYER_T_STATIC = 0;
    public static byte LAYER_T_DYNAMIC = 1;
    public static byte LAYER_T_SLOT = 2;

    public static byte IMG_COUNT;
    public int AFFINES_COUNT;
    public int EMITTERS_COUNT;
    public byte ELEMENT_LINE;
    public byte ELEMENT_RECT;
    public byte ELEMENT_FILLRECT;
    public byte ELEMENT_FILLRECT_x4;
    public byte ELEMENT_ARC;


    public static byte FR_IMAGE_ID;
    public static byte FR_X;// = Graph.Frame_I_X;
    public static byte FR_Y;// = Graph.Frame_I_Y;
    public static byte FR_W;// = Graph.Frame_I_W;
    public static byte FR_H;// = Graph.Frame_I_H;
    public static byte FR_COLOR;// = Graph.Frame_I_TYPE;//color записан только для геометрических фигур, для остальных побитно афинны
    public static byte FR_TRANSFORM;// = Graph.Frame_I_TYPE;

    //public static byte SP_ID = Graph.Sprite_;
    public static byte SP_S;// = Graph.Sprite_I_X;
    public static byte SP_E;// = Graph.Sprite_I_Y;

    public static byte SP_I_ID;// = Graph.Sprite_index_I_ID;
    public static byte SP_I_X;// = Graph.Sprite_index_I_X;
    public static byte SP_I_Y;// = Graph.Sprite_index_I_Y;
    public static byte SP_I_TYPE;// = Graph.Sprite_index_I_TYPE;

    //    public static byte AN_ID = Graph.Animation_index_I_ID;
    public static byte AN_S;// = Graph.Animation_I_X;
    public static byte AN_E;// = Graph.Animation_I_Y;

    public static byte AN_I_ID;// = Graph.Animation_index_I_ID;
    public static byte AN_I_X;// = Graph.Animation_index_I_X;
    public static byte AN_I_Y;// = Graph.Animation_index_I_Y;
    public static byte AN_I_TYPE;// = Graph.Animation_index_I_TYPE;

    //    public static byte UN_ID = 20;
    public static byte UN_S;// = Graph.Unit_I_X;
    public static byte UN_E;// = Graph.Unit_I_Y;

    public static byte UN_I_ID;// = Graph.Unit_index_I_ID;
    public static byte UN_I_X;// = Graph.Unit_index_I_X;
    public static byte UN_I_Y;// = Graph.Unit_index_I_Y;
    public static byte UN_I_TYPE;// = Graph.Unit_index_I_TYPE;
    public static byte UN_I_BODY_TYLE;// = Graph.Unit_index_I_H;

    //    public static byte UNS_ID = 28;
    public static byte UNS_S;// = Graph.Unit_script_I_X;
    public static byte UNS_E;// = Graph.Unit_script_I_Y;
    public static byte UNS_BODY_TYPE;// = Graph.Unit_index_I_H;

    public static byte UNS_I_ID;// = Graph.Unit_script_index_I_ID;
    public static byte UNS_I_X;// = Graph.Unit_script_index_I_X;
    public static byte UNS_I_Y;// = Graph.Unit_script_index_I_Y;
    public static byte UNS_I_TYPE;// = Graph.Unit_script_index_I_TYPE;

    //    public static byte OBJ_ID = 36;//ид индекса  T_FRAME,T_SPRITE,T_UNIT
    public static byte OBJ_S;// = Graph.Object_I_X;
    public static byte OBJ_E;// = Graph.Object_I_Y;
//    public static byte OBJ_W = Graph.Object_I_W;
//    public static byte OBJ_H = Graph.Object_I_H;

    public static byte OBJ_I_ID;// = Graph.Object_index_I_ID;
    public static byte OBJ_I_X;// = Graph.Object_index_I_X;
    public static byte OBJ_I_Y;// = Graph.Object_index_I_Y;
    public static byte OBJ_I_SHIFT;// = Graph.Object_index_I_W;
    public static byte OBJ_I_YY;// = Graph.Object_index_I_H;
    public static byte OBJ_I_TYPE;// = Graph.Object_index_I_TYPE;
    public static byte OBJ_I_LAYER_TYPE;// = Graph.Object_index_I_LAYER_TYPE;//static,dynamic,slot,exit,enter,pass
    public static byte OBJ_I_SLOT;// = Graph.Object_index_I_SLOT;

    public static byte AF_S;// = Graph.Affine_I_X;
    public static byte AF_E;// = Graph.Affine_I_Y;
    public static byte AffineTransparency;// = Graph.Affine_I_AF_TR;
    public static byte AffineScalingX;// = Graph.Affine_I_AF_SC_X;
    public static byte AffineScalingY;// = Graph.Affine_I_AF_SC_Y;
    public static byte AffineRotate;// = Graph.Affine_I_AF_RO;
    public static byte EM_P5;// = Graph.Emitter_I_EM5;
    public static byte EM_P6;// = Graph.Emitter_I_EM6;
    public static byte EM_P7;// = Graph.Emitter_I_EM7;
    public static byte EM_P8;// = Graph.Emitter_I_EM8;
    public static byte EM_P9;// = Graph.Emitter_I_EM9;
    public static byte EM_P10;// = Graph.Emitter_I_EM10;
    public static byte EM_P11;// = Graph.Emitter_I_EM11;
    public static byte EM_P12;// = Graph.Emitter_I_EM12;
    public static byte EM_P13;// = Graph.Emitter_I_EM13;
    public static byte EM_P14;// = Graph.Emitter_I_EM14;
    public static byte EM_P15;// = Graph.Emitter_I_EM15;
    public static byte EM_P16;// = Graph.Emitter_I_EM16;
    public static byte EM_P17;// = Graph.Emitter_I_EM17;
    public static byte EM_P18;// = Graph.Emitter_I_EM18;
    public static byte EM_P19;// = Graph.Emitter_I_EM19;
    public static byte EM_P4;// = Graph.Emitter_I_EM4;
    public static byte AffineHMirror;// = Graph.Affine_I_AF_HM;

    public static byte AffineVMirror;// = Graph.Affine_I_AF_VM;
    public static byte AF_I_ID;// = Graph.Affine_index_I_ID;
    public static byte AF_I_X;// = Graph.Affine_index_I_X;
    public static byte AF_I_Y;// = Graph.Affine_index_I_Y;

    //public static byte AF_I_TYPE;// = Graph.Affine_index_I_TYPE;всегда фрейм
    public static byte EM_S;// = Graph.Emitter_I_X;//указатель на первый индекс
    public static byte EM_E;// = Graph.Emitter_I_Y;//указатель на последний индекс
    public static byte EM_I_ID;// = Graph.Emitter_index_I_ID;
    public static byte EM_I_TYPE;// = Graph.Emitter_index_I_TYPE;
    public static byte EM_I_LAYER;// = Graph.Emitter_index_I_LAYER_TYPE;
    public static byte EM_I_SLOT;// = Graph.Emitter_index_I_SLOT;
    public static byte EM_I_SHIFT;// = Graph.Emitter_index_I_W;
    public static byte EM_I_X;// = Graph.Emitter_index_I_X;
    public static byte EM_I_Y;// = Graph.Emitter_index_I_Y;
    public static byte EM_P0;// = Graph.Emitter_I_EM0;//всего 20 параметров
    public static byte EM_P1;// = Graph.Emitter_I_EM1;
    public static byte EM_P2;// = Graph.Emitter_I_EM2;
    public static byte EM_P3;// = Graph.Emitter_I_EM3;

    public static byte PARAM_BYTE_COUNT;
    public static byte PARAM_SHORT_COUNT;
    public static byte PARAM_INT_COUNT;

}
