package com.geargames.common;

import com.geargames.common.util.ArrayIntDual;
import com.geargames.common.util.ArrayObjectDual;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.UTFDataFormatException;

/**
 * Created by IntelliJ IDEA.
 * User: kewgen
 * Date: 13.01.12
 * Time: 12:10
 * Загрузчик таблиц и массивов
 * Конструкция только для однопоточного режима!
 */
public abstract class TablesLoader {

    protected boolean DEBUG = false;
    private byte type;
    private short version;
    protected final byte TYPE_INT = 0;
    protected final byte TYPE_STRING = 1;

    public boolean loadTables(DataInputStream dis, byte type) {
        this.type = type;
        try {
            version = dis.readShort();
            loadTables(dis);
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            //Debug.logEx(ex);
            return false;
        }
    }

    protected void loadTables(DataInputStream dis) throws IOException {//should be overridden
        ArrayIntDual array1 = (ArrayIntDual) loadTable(dis);
        ArrayIntDual array2 = (ArrayIntDual) loadTable(dis);
        array1.free();
        array2.free();
        //и т.п.
    }

    protected Object loadTable(DataInputStream dis) throws IOException {//загрузка в виде массива примитивов

        /*Формат блока таблицы:
        1 - тип таблицы
        4 - длина блока таблицы
        2 - длина блока информации о таблице
        1 - кол-во столбцов - это кол-во массивов
        2 - кол-во строк - это длина каждого массива*/
        byte ttype = dis.readByte();
        int ttable_len = dis.readInt();
        short tinfo_len = dis.readShort();
        ttable_len -= 2;
        byte tcol_count = dis.readByte();
        ttable_len -= 1;
        tinfo_len -= 1;
        short trow_count = dis.readShort();
        ttable_len -= 2;
        tinfo_len -= 2;
        String str = "Load table["+trow_count+","+tcol_count+"]";

        for (int i = tinfo_len; i > 0; i--) {//отсчитываем до начала первого массива
            dis.readByte();
            ttable_len -= 1;
        }

        ArrayIntDual array = new ArrayIntDual(trow_count, tcol_count);
        for (int col = 0; col < tcol_count; col++) {
            /*Формат блока массива:
            1 - тип массива
            2 - длина блока
            2 - длина массива N
            N*data - данные массива*/
            byte atype = dis.readByte();
            ttable_len -= 1;
            short ainfo_len = dis.readShort();
            ttable_len -= 2;
            short aarray_len = dis.readShort();
            ttable_len -= 2;
            ainfo_len -= 2;
            if (DEBUG) str = str+"\n col["+col+"]\t";

            for (int row = 0; row < trow_count; row++) {
                int size = readBlock(dis, atype, array, row, col);
                ttable_len -= size;
                ainfo_len -= size;
                if (DEBUG) {
                    str = str+array.get(row, col)+",";
                }
            }

            for (int i = ainfo_len; i > 0; i--) {//отсчитываем до начала след первого массива
                dis.readByte();
                ttable_len -= 1;
            }
        }
        if (DEBUG) System.out.print(str);//Debug.trace(str);

        for (int i = ttable_len; i > 0; i--) {//отсчитываем до начала след таблицы
            dis.readByte();
            ttable_len -= 1;
        }

        return array;
    }


    protected Object loadTablesAsObjects(DataInputStream dis) throws IOException {//загрузка в виде массива объектов

        /*Формат блока таблицы:
        1 - тип таблицы
        4 - длина блока таблицы
        2 - длина блока информации о таблице
        1 - кол-во столбцов - это кол-во массивов
        2 - кол-во строк - это длина каждого массива*/
        byte ttype = dis.readByte();
        int ttable_len = dis.readInt();
        short tinfo_len = dis.readShort();
        ttable_len -= 2;
        byte tcol_count = dis.readByte();
        ttable_len -= 1;
        tinfo_len -= 1;
        short trow_count = dis.readShort();
        ttable_len -= 2;
        tinfo_len -= 2;
        String str = "Load table["+trow_count+","+tcol_count+"]";

        for (int i = tinfo_len; i > 0; i--) {//отсчитываем до начала первого массива
            dis.readByte();
            ttable_len -= 1;
        }

        ArrayObjectDual array = new ArrayObjectDual(trow_count, tcol_count);
        for (int col = 0; col < tcol_count; col++) {
            /*Формат блока массива:
            1 - тип массива
            2 - длина блока
            2 - длина массива N
            N*data - данные массива*/
            byte atype = dis.readByte();
            ttable_len -= 1;
            short ainfo_len = dis.readShort();
            ttable_len -= 2;
            short aarray_len = dis.readShort();
            ttable_len -= 2;
            ainfo_len -= 2;
            if (DEBUG) {
                str = str+"\n col["+col+"]\t";
            }

            for (int row = 0; row < trow_count; row++) {
                int size = readBlock(dis, atype, array, row, col);
                ttable_len -= size;
                ainfo_len -= size;
                if (DEBUG) {
                    str = str+array.get(row, col)+",";
                }
            }

            for (int i = ainfo_len; i > 0; i--) {//отсчитываем до начала след первого массива
                dis.readByte();
                ttable_len -= 1;
            }
        }
        if (DEBUG) System.out.print(str);//Debug.trace(str);

        for (int i = ttable_len; i > 0; i--) {//отсчитываем до начала след таблицы
            dis.readByte();
            ttable_len -= 1;
        }

        return array;
    }

    private int readBlock(DataInputStream dis, byte atype, ArrayIntDual array, int row, int col) throws IOException {
        int size;//размер блока
        switch (atype) {
            case 1://define('F_BYTE', 	1);
            case 6://define('F_BYTE_S', 		6);
                array.set(row, col, dis.readByte());
                size = 1;
                break;
            case 2://define('F_SHORT', 	2);
            case 7://define('F_SHORT_S', 	        7);
                array.set(row, col, dis.readShort());
                size = 2;
                break;
            case 4://define('F_INT', 		4);
            case 9://define('F_INT_S', 	9);
                array.set(row, col, dis.readInt());
                size = 4;
                break;
            case 12://define('F_STR2_ANSI', 		12);
            default:
                throw new IllegalArgumentException("atype:" + atype);
        }
        return size;
    }

    private int readBlock(DataInputStream dis, byte atype, ArrayObjectDual array, int row, int col) throws IOException {
        int size;//размер блока
        switch (atype) {
            case 1://define('F_BYTE', 	1);
            case 6://define('F_BYTE_S', 		6);
                array.set(row, col, dis.readByte());
                size = 1;
                break;
            case 2://define('F_SHORT', 	2);
            case 7://define('F_SHORT_S', 	        7);
                array.set(row, col, dis.readShort());
                size = 2;
                break;
            case 4://define('F_INT', 		4);
            case 9://define('F_INT_S', 	9);
                array.set(row, col, dis.readInt());
                size = 4;
                break;
            case 12://define('F_STR2_ANSI', 		12);
                int len = dis.readUnsignedShort();
                array.set(row, col, readUTF(dis, len));
//                String string = String.valueOfC(dis.readUTF());
//                array[row][col] = string;
//                int len = string.length();
                size = 2 + len;
                break;
            default:
                throw new IllegalArgumentException("atype:" + atype);
        }
        return size;
    }

    //функция скопирована из API и оптимизирована под один поток
    private static byte bytearr[] = new byte[80];
    private static char chararr[] = new char[80];

    public synchronized final java.lang.String readUTF(DataInputStream dis, int utflen) throws IOException {
        if (bytearr == null || bytearr.length < utflen) {
            bytearr = new byte[utflen * 2];
            chararr = new char[utflen * 2];
        }

        dis.readFully(bytearr, 0, utflen);
        return readUTF(bytearr, utflen);
    }

    public synchronized static final java.lang.String readUTF(byte[] bytes, int utflen) throws IOException {

        int c, char2, char3;
        int count = 0;
        int chararr_count = 0;

        while (count < utflen) {
            c = (int) bytes[count] & 0xff;
            if (c > 127) break;
            count++;
            chararr[chararr_count++] = (char) c;
        }

        while (count < utflen) {
            c = (int) bytes[count] & 0xff;
            switch (c >> 4) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    /* 0xxxxxxx*/
                    count++;
                    chararr[chararr_count++] = (char) c;
                    break;
                case 12:
                case 13:
                    /* 110x xxxx   10xx xxxx*/
                    count += 2;
                    if (count > utflen) throw new UTFDataFormatException("malformed input: partial character at end");
                    char2 = (int) bytes[count - 1];
                    if ((char2 & 0xC0) != 0x80)
                        throw new UTFDataFormatException("malformed input around byte1 " + count);
                    chararr[chararr_count++] = (char) (((c & 0x1F) << 6) |
                            (char2 & 0x3F));
                    break;
                case 14:
                    /* 1110 xxxx  10xx xxxx  10xx xxxx */
                    count += 3;
                    if (count > utflen) throw new UTFDataFormatException("malformed input: partial character at end");
                    char2 = (int) bytes[count - 2];
                    char3 = (int) bytes[count - 1];
                    if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80))
                        throw new UTFDataFormatException("malformed input around byte2 " + (count - 1));
                    chararr[chararr_count++] = (char) (((c & 0x0F) << 12) |
                            ((char2 & 0x3F) << 6) |
                            ((char3 & 0x3F) << 0));
                    break;
                default:
                    /* 10xx xxxx,  1111 xxxx */
                    throw new UTFDataFormatException("malformed input around byte3 " + count);
            }
        }
        // The number of chars produced may be less than utflen
        return new java.lang.String(chararr, 0, chararr_count);
    }

    public byte getType() {
        return type;
    }

    public short getVersion() {
        return version;
    }
}
