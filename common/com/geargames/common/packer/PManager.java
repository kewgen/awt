package com.geargames.common.packer;

import com.geargames.common.Graphics;
import com.geargames.common.Image;
import com.geargames.common.Port;
import com.geargames.common.String;
import com.geargames.common.util.ArrayByte;
import com.geargames.common.util.ArrayIntegerDual;
import com.geargames.common.util.ArrayList;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * User: kewgen
 * Date: 18.09.12
 * Time: 17:49
 * управляющий сущностями пакера
 */
public abstract class PManager implements com.geargames.common.Render {

    protected boolean DEBUG = false;
    private PCreator creator;
    private PUnresolvedFrameManger unresolvedFrameManger;

    public PManager() {
        isDataLoaded = false;
        isImagesLoaded = false;
    }

    public void loadData(InputStream is) {
        loadArray(is);
        createPrototypes();
        addPrototypes();
        isDataLoaded = true;
        end();
    }

    public void loadImages(Graphics graphics, InputStream is) {
        int count = IMG_COUNT + 1;
        images = new ArrayList(count);
        for (int i = 0; i < count; i++) {
            images.add(graphics.createImage());
        }
        loadImagesFromStream(graphics, is, count);
        isImagesLoaded = true;
        end();
    }

    private void end() {//завершение загрузки
        if (isCreated()) {
            updateFrames();
            arrayDual.free();
            arrayDual = null;
        }
    }

    protected ArrayList images;
    protected ArrayList frames;
    protected ArrayList sprites;
    protected ArrayList animations;
    protected ArrayList affines;
    protected ArrayList emitters;
    protected ArrayList units;
    protected ArrayList unitScripts;
    protected ArrayList objects;


    protected void createPrototypes() {
       //создаем перечень всех прототипов
        int count = arrayDual.length(FR_X);
        frames = new ArrayList(count);
        for (int i = 0; i < count; i++) {
            frames.add(new PFrame(arrayDual.get(FR_X, i), arrayDual.get(FR_Y, i), arrayDual.get(FR_W, i), arrayDual.get(FR_H, i)));
        }

        count = arrayDual.length(SP_E);
        sprites = new ArrayList(count);
        int interval = 0;
        for (int i = 0; i < count; i++) {
            interval = arrayDual.get(SP_E, i) - arrayDual.get(SP_S, i);
            sprites.add(new PSprite(interval + 1));
        }

        count = arrayDual.length(AN_E);
        animations = new ArrayList(count);
        for (int i = 0; i < count; i++) {
            interval = arrayDual.get(AN_E, i) - arrayDual.get(AN_S, i);
            animations.add(new PAnimation(interval + 1));
        }

        count = arrayDual.length(AF_E);
        affines = new ArrayList(count);
        for (int i = 0; i < count; i++) {
            interval = arrayDual.get(AF_E, i) - arrayDual.get(AF_S, i);
            affines.add(new PAffine(interval + 1));
        }

        count = arrayDual.length(EM_E);
        emitters = new ArrayList(count);
        for (int i = 0; i < count; i++) {
            interval = arrayDual.get(EM_E, i) - arrayDual.get(EM_S, i);
            emitters.add(new PEmitter(interval + 1));
        }

        count = arrayDual.length(UN_E);
        units = new ArrayList(count);
        for (int i = 0; i < count; i++) {
            interval = arrayDual.get(UN_E, i) - arrayDual.get(UN_S, i);
            units.add(creator.createUnit(i, interval + 1));
        }

        count = arrayDual.length(UNS_E);
        unitScripts = new ArrayList(count);
        for (int i = 0; i < count; i++) {
            interval = arrayDual.get(UNS_E, i) - arrayDual.get(UNS_S, i);
            unitScripts.add(new PUnitScript(interval + 1));
        }

        count = arrayDual.length(OBJ_E);
        objects = new ArrayList(count);
        for (int i = 0; i < count; i++) {
            interval = arrayDual.get(OBJ_E, i) - arrayDual.get(OBJ_S, i);
            objects.add(new PObject(interval + 1));
        }
    }

    protected void addPrototypes() {
        //заполняем индексы прототипов
        int count = sprites.size();
        for (int p = 0; p < count; p++) {
            PrototypeIndexes prototypeIndexes = (PrototypeIndexes) sprites.get(p);
            prototypeIndexes.setPid(p);
            int start = arrayDual.get(SP_S, p);
            int end = arrayDual.get(SP_E, p);
            for (int i = start; i <= end; i++) {
                int id = arrayDual.get(SP_I_ID, i);
                int x = arrayDual.get(SP_I_X, i);
                int y = arrayDual.get(SP_I_Y, i);
                int type = arrayDual.get(SP_I_TYPE, i);
                Index index = new Index(getPrototype(type, id), x, y);
                prototypeIndexes.add(index);
            }
        }

        count = animations.size();
        for (int p = 0; p < count; p++) {
            PrototypeIndexes prototypeIndexes = (PrototypeIndexes) animations.get(p);
            prototypeIndexes.setPid(p);
            int start = arrayDual.get(AN_S, p);
            int end = arrayDual.get(AN_E, p);
            for (int i = start; i <= end; i++) {
                int id = arrayDual.get(AN_I_ID, i);
                int x = arrayDual.get(AN_I_X, i);
                int y = arrayDual.get(AN_I_Y, i);
                int type = arrayDual.get(AN_I_TYPE, i);
                Index index = new Index(getPrototype(type, id), x, y);
                prototypeIndexes.add(index);
            }
        }

        count = affines.size();
        for (int p = 0; p < count; p++) {
            PAffine affine = (PAffine) affines.get(p);
            affine.setPid(p);
            int start = arrayDual.get(AF_S, p);
            int end = arrayDual.get(AF_E, p);
            affine.setTransparency(arrayDual.get(AffineTransparency, p));
            affine.setScalingX(arrayDual.get(AffineScalingX, p) & 0xff);
            affine.setScalingY(arrayDual.get(AffineScalingY, p) & 0xff);
            affine.setHmirror(arrayDual.get(AffineHMirror, p) == 1);
            affine.setVmirror(arrayDual.get(AffineVMirror, p) == 1);
            affine.setRotate(arrayDual.get(AffineRotate, p));
            for (int i = start; i <= end; i++) {
                int id = arrayDual.get(AF_I_ID, i);
                int x = arrayDual.get(AF_I_X, i);
                int y = arrayDual.get(AF_I_Y, i);
                int type = com.geargames.common.Render.T_FRAME;
                Index index = new Index(getPrototype(type, id), x, y);
                affine.setX(x);
                affine.setY(y);
                affine.add(index);
            }
        }

        count = emitters.size();
        for (int p = 0; p < count; p++) {
            PEmitter emitter = (PEmitter) emitters.get(p);
            emitter.setPid(p);
            int start = arrayDual.get(EM_S, p);
            int end = arrayDual.get(EM_E, p);
            for (int i = start; i <= end; i++) {
                int id = arrayDual.get(EM_I_ID, i);
                int x = arrayDual.get(EM_I_X, i);
                int y = arrayDual.get(EM_I_Y, i);
                int type = arrayDual.get(EM_I_TYPE, i);
                int slot = arrayDual.get(OBJ_I_SLOT, i);
                int shift = arrayDual.get(OBJ_I_SHIFT, i);
                int layerType = arrayDual.get(OBJ_I_LAYER_TYPE, i);
                Index index = new IndexObject(getPrototype(type, id), x, y, shift, slot, layerType);
                emitter.add(index);
            }
            emitter.setRadius(arrayDual.get(EM_P0, p));
            emitter.setEmitSimultaneously(arrayDual.get(EM_P1, p) != 0);
            emitter.setVelX(arrayDual.get(EM_P2, p));
            emitter.setVelY(arrayDual.get(EM_P3, p));
            emitter.setAccX(arrayDual.get(EM_P4, p));
            emitter.setAccY(arrayDual.get(EM_P5, p));
            emitter.setLifeTime(arrayDual.get(EM_P6, p));
            emitter.setEmitTime(arrayDual.get(EM_P7, p));
            emitter.setEmitVector(arrayDual.get(EM_P8, p));
            emitter.setEmitSector(arrayDual.get(EM_P9, p));
            emitter.setEmitFreqBase(arrayDual.get(EM_P10, p));
            emitter.setEmitFreqInc(arrayDual.get(EM_P11, p));
            emitter.setEmitVelBase(arrayDual.get(EM_P12, p));
            emitter.setEmitVelInc(arrayDual.get(EM_P13, p));
            emitter.setEmitLifeBase(arrayDual.get(EM_P14, p));
            emitter.setEmitLifeInc(arrayDual.get(EM_P15, p));
            emitter.setEmitAccX(arrayDual.get(EM_P16, p));
            emitter.setEmitAccY(arrayDual.get(EM_P17, p));
            emitter.setEmitAccLine(arrayDual.get(EM_P18, p));
            emitter.setRenderEmitterFirst(arrayDual.get(EM_P19, p) == 0);
        }

        count = units.size();
        for (int p = 0; p < count; p++) {
            PrototypeIndexes prototypeIndexes = (PrototypeIndexes) units.get(p);
            prototypeIndexes.setPid(p);
            int start = arrayDual.get(UN_S, p);
            int end = arrayDual.get(UN_E, p);
            for (int i = start; i <= end; i++) {
                int id = arrayDual.get(UN_I_ID, i);
                int x = arrayDual.get(UN_I_X, i);
                int y = arrayDual.get(UN_I_Y, i);
                int type = arrayDual.get(UN_I_TYPE, i);
                int body = arrayDual.get(UN_I_BODY_TYLE, i);
                Index index = new IndexUnit(getPrototype(type, id), x, y, body);
                prototypeIndexes.add(index);
            }
        }

        count = unitScripts.size();
        for (int p = 0; p < count; p++) {
            PrototypeIndexes prototypeIndexes = (PrototypeIndexes) unitScripts.get(p);
            prototypeIndexes.setPid(p);
            int start = arrayDual.get(UNS_S, p);
            int end = arrayDual.get(UNS_E, p);
            for (int i = start; i <= end; i++) {
                int id = arrayDual.get(UNS_I_ID, i);
                int x = arrayDual.get(UNS_I_X, i);
                int y = arrayDual.get(UNS_I_Y, i);
                int type = arrayDual.get(UNS_I_TYPE, i);
                Index index = new Index(getPrototype(type, id), x, y);
                prototypeIndexes.add(index);
            }
        }

        count = objects.size();
        for (int p = 0; p < count; p++) {
            PrototypeIndexes prototypeIndexes = (PrototypeIndexes) objects.get(p);
            prototypeIndexes.setPid(p);
            int start = arrayDual.get(OBJ_S, p);
            int end = arrayDual.get(OBJ_E, p);
            for (int i = start; i <= end; i++) {
                int id = arrayDual.get(OBJ_I_ID, i);
                int x = arrayDual.get(OBJ_I_X, i);
                int y = arrayDual.get(OBJ_I_Y, i);
                int type = arrayDual.get(OBJ_I_TYPE, i);
                int slot = arrayDual.get(OBJ_I_SLOT, i);
                int shift = arrayDual.get(OBJ_I_SHIFT, i);
                int layerType = arrayDual.get(OBJ_I_LAYER_TYPE, i);
                Index index = new IndexObject(getPrototype(type, id), x, y, shift, slot, layerType);
                prototypeIndexes.add(index);
            }
        }
    }

    private Prototype getPrototype(int ptype, int index) {
        switch (ptype) {
            case com.geargames.common.Render.T_FRAME:
                return (Prototype) frames.get(index);
            case com.geargames.common.Render.T_SPRITE:
                return (Prototype) sprites.get(index);
            case com.geargames.common.Render.T_ANIMATION:
                return (Prototype) animations.get(index);
            case com.geargames.common.Render.T_AFFINE:
                return (Prototype) affines.get(index);
            case com.geargames.common.Render.T_EMITTER:
                return (Prototype) emitters.get(index);
            case com.geargames.common.Render.T_UNIT:
                return (Prototype) units.get(index);
            case com.geargames.common.Render.T_UNIT_SCRIPT:
                return (Prototype) unitScripts.get(index);
            case com.geargames.common.Render.T_OBJ:
                return (Prototype) objects.get(index);
            default:
                return null;
        }
    }

    private void loadArray(InputStream is) {//загрузка в массив

        try {
            int PARAM_COUNT = PARAM_BYTE_COUNT + PARAM_SHORT_COUNT + PARAM_INT_COUNT;

            DataInputStream dis = new DataInputStream(is);

            arrayDual = new ArrayIntegerDual(PARAM_COUNT);
            for (int i = 0; i < PARAM_COUNT; i++) {
                int len = dis.readShort();
                arrayDual.createY(i, len);
                String str = String.valueOfC("Load ").concatI(i).concatC("\tlen:").concatI(len).concatC(" ");
                if (i < PARAM_BYTE_COUNT) {//BYTE
                    ArrayByte arrayByte = new ArrayByte(len);
                    dis.read(arrayByte.getArray(), 0, len);
                    for (int a = 0; a < len; a++) {
                        arrayDual.set(i, a, arrayByte.get(a));
                        if (DEBUG && a < 100){
                            str = str.concatI(arrayDual.get(i, a)).concatC(",");
                        }
                    }
                    arrayByte.free();
                } else if (i < PARAM_BYTE_COUNT + PARAM_SHORT_COUNT) {//SHORT
                    ArrayByte arrayByte = new ArrayByte(len * 2);
                    dis.read(arrayByte.getArray(), 0, len * 2);
                    for (int a = 0; a < len; a++) {
                        arrayDual.set(i, a, ((arrayByte.get(a * 2) << 8) | (arrayByte.get(a * 2 + 1) & 0xff)));
                        if (DEBUG && a < 100){
                            str = str.concatI(arrayDual.get(i, a)).concatC(",");
                        }
                    }
                    arrayByte.free();
                } else {
                    ArrayByte arrayByte = new ArrayByte(len * 4);
                    dis.read(arrayByte.getArray(), 0, len * 4);
                    for (int a = 0; a < len; a++) {
                        arrayDual.set(i, a, ((arrayByte.get(a * 4) << 24) | ((arrayByte.get(a * 4 + 1) & 0xff) << 16)
                                | ((arrayByte.get(a * 4 + 2) & 0xff) << 8) | (arrayByte.get(a * 4 + 3) & 0xff)));
                        if (DEBUG && a < 100) {
                            str = str.concatI(arrayDual.get(i, a)).concatC(",");
                        }
                    }
                    arrayByte.free();
                }
                if (DEBUG) {
                    System.out.println(str);
                }
            }
            dis.close();
            dis = null;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }



    public void loadImagesFromStream(Graphics graphics, InputStream is, int count) {//инициализация имиджей из пакетов
        try {
            DataInputStream dis = new DataInputStream(is);
            readImages(dis, graphics, count, 0);
            dis.close();
            dis = null;//for ObjC - autoreleased
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readImages(DataInputStream dis, Graphics graphics, int imgCount, int firstID) throws IOException {
        //чтение картинок из файла выгруженного пакером
        int len_b0;
        int size;
        ArrayByte data;
        for (int i = 0; i < imgCount; i++) {
            int img_cur = firstID + i;

            len_b0 = dis.readByte() & 0xff;
            if (len_b0 == 0xff) break;

            int len_b1 = dis.readByte() & 0xff;
            int len_b2 = dis.readByte() & 0xff;
            size = len_b0 << 16 | len_b1 << 8 | len_b2;

            data = new ArrayByte(size);
            dis.read(data.getArray(), 0, size);

            addImage(img_cur, graphics, data, size);

            data.free();
        }
    }

    private void addImage(int img_cur, Graphics graphics, ArrayByte data, int data_len) throws IOException {
        if (!((Image) images.get(img_cur)).isCreated()) {
            Image image = graphics.createImage(data.getArray(), 0, data_len);
            if (Port.IS_HALF_GRAPHIC) {//руками добываем картинки нужного размера
                int wh = 512 * 2 / 3;
                Image image2 = image.getRescaledImage(wh, wh);
                image.recycle();
                image = image2;
            }
            images.set(img_cur, image);
            System.out.println(String.valueOfC("Image created(").concatI(img_cur).concatC(")"));
        }
        if (Port.OPEN_GL) graphics.addTexture((Image) images.get(img_cur));
    }

    private void updateFrames() {
        //цепляем картинки к фреймам
        int count = frames.size();
        for (int p = 0; p < count; p++) {
            PFrame frame = (PFrame) frames.get(p);
            int imageID = arrayDual.get(FR_IMAGE_ID, p);
            if (imageID >= IMG_COUNT) continue;
            frame.setImage((Image)images.get(imageID));
            frame.setBid(p);
            frame.setPid(p);
            frame.setTransform(FR_TRANSFORM);
        }

    }


    public boolean isCreated() {//данные и картинки прогружены
        return isDataLoaded && isImagesLoaded;
    }


    public java.lang.String toString() {
        return "PManager{" +
                "objects=" + objects.size() +
                ", unitScripts=" + unitScripts.size() +
                ", units=" + units.size() +
                ", emitters=" + emitters.size() +
                ", affines=" + affines.size() +
                ", animations=" + animations.size() +
                ", sprites=" + sprites.size() +
                ", frames=" + frames.size() +
                '}';
    }

    protected ArrayIntegerDual arrayDual;
    private boolean isDataLoaded;
    private boolean isImagesLoaded;

    public abstract void create();

    public PObject getObject(int pid) {
        return (PObject)objects.get(pid);
    }

    public PUnitScript getUnitScript(int pid) {
        return (PUnitScript)unitScripts.get(pid);
    }

    public PUnit getUnit(int pid) {
        return (PUnit)units.get(pid);
    }

    public PAnimation getAnimation(int pid) {
        return (PAnimation)animations.get(pid);
    }

    public PAffine getAffine(int pid) {
        return (PAffine)affines.get(pid);
    }

    public PSprite getSprite(int pid) {
        return (PSprite)sprites.get(pid);
    }

    public PFrame getFrame(int pid) {
        if(pid < frames.size()){
            return (PFrame)frames.get(pid);
        }else{
            return unresolvedFrameManger.getFrame(pid);
        }
    }

    protected int o_dx;
    protected int o_dy;

    public void setODX(int dx) {
        o_dx = dx;
    }

    public void setODY(int dy) {
        o_dy = dy;
    }

    public void setStringColor(byte color) {
        string_color = color;
    }

    public byte getStringColor() {
        return string_color;
    }

    public EmitProcess createEmitter(int pos, int i, int i1) {
        return null;
    }

    public byte font;
    protected byte string_color;

    public int getSpriteDx(int id) {
        return getPSprite(id).getIndex(0).getX();
    }

    public PObject getPObject(int pid) {
        return (PObject) objects.get(pid);
    }

    public PSprite getPSprite(int pid) {
        return (PSprite) sprites.get(pid);
    }

    private PFrame getPFrame(int id) {
        return (PFrame) frames.get(id);
    }

    public int getFrameW(int id) {
        return getPFrame(id).getWidth();
    }

    public int getFrameH(int id) {
        return getPFrame(id).getHeight();
    }

    public PCreator getCreator() {
        return creator;
    }

    public void setCreator(PCreator creator) {
        this.creator = creator;
    }

    public PUnresolvedFrameManger getUnresolvedFrameManger() {
        return unresolvedFrameManger;
    }

    public void setUnresolvedFrameManger(PUnresolvedFrameManger unresolvedFrameManger) {
        this.unresolvedFrameManger = unresolvedFrameManger;
    }

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
    public static byte FR_X;
    public static byte FR_Y;
    public static byte FR_W;
    public static byte FR_H;
    public static byte FR_COLOR;
    public static byte FR_TRANSFORM;

    public static byte SP_S;
    public static byte SP_E;

    public static byte SP_I_ID;
    public static byte SP_I_X;
    public static byte SP_I_Y;
    public static byte SP_I_TYPE;

    public static byte AN_S;
    public static byte AN_E;

    public static byte AN_I_ID;
    public static byte AN_I_X;
    public static byte AN_I_Y;
    public static byte AN_I_TYPE;

    public static byte UN_S;
    public static byte UN_E;

    public static byte UN_I_ID;
    public static byte UN_I_X;
    public static byte UN_I_Y;
    public static byte UN_I_TYPE;
    public static byte UN_I_BODY_TYLE;

    public static byte UNS_S;
    public static byte UNS_E;
    public static byte UNS_BODY_TYPE;

    public static byte UNS_I_ID;
    public static byte UNS_I_X;
    public static byte UNS_I_Y;
    public static byte UNS_I_TYPE;

    public static byte OBJ_S;
    public static byte OBJ_E;

    public static byte OBJ_I_ID;
    public static byte OBJ_I_X;
    public static byte OBJ_I_Y;
    public static byte OBJ_I_SHIFT;
    public static byte OBJ_I_YY;
    public static byte OBJ_I_TYPE;
    public static byte OBJ_I_LAYER_TYPE;
    public static byte OBJ_I_SLOT;

    public static byte AF_S;
    public static byte AF_E;
    public static byte AffineTransparency;
    public static byte AffineScalingX;
    public static byte AffineScalingY;
    public static byte AffineRotate;
    public static byte EM_P5;
    public static byte EM_P6;
    public static byte EM_P7;
    public static byte EM_P8;
    public static byte EM_P9;
    public static byte EM_P10;
    public static byte EM_P11;
    public static byte EM_P12;
    public static byte EM_P13;
    public static byte EM_P14;
    public static byte EM_P15;
    public static byte EM_P16;
    public static byte EM_P17;
    public static byte EM_P18;
    public static byte EM_P19;
    public static byte EM_P4;
    public static byte AffineHMirror;

    public static byte AffineVMirror;
    public static byte AF_I_ID;
    public static byte AF_I_X;
    public static byte AF_I_Y;

    public static byte EM_S;
    public static byte EM_E;
    public static byte EM_I_ID;
    public static byte EM_I_TYPE;
    public static byte EM_I_LAYER;
    public static byte EM_I_SLOT;
    public static byte EM_I_SHIFT;
    public static byte EM_I_X;
    public static byte EM_I_Y;
    public static byte EM_P0;
    public static byte EM_P1;
    public static byte EM_P2;
    public static byte EM_P3;

    public static byte PARAM_BYTE_COUNT;
    public static byte PARAM_SHORT_COUNT;
    public static byte PARAM_INT_COUNT;

}
