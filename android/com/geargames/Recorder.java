package com.geargames;

/*
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotFoundException;
*/

import android.content.Context;
import com.geargames.common.util.ArrayByte;

import java.io.*;

/**
 * Запись и чтение данных из памяти телефона */
public class Recorder {
    final static boolean DEBUG_RMS = false;
    private static boolean isEncoded = true;

    /* *****************************************************************************
  * RMS                                         *
  * *******************************************************************************/
    public static boolean RMSStoreSave(MIDlet midlet, String name, ByteArrayOutputStream baos, boolean isEncoded) {

       try {
           byte data[] = baos.toByteArray();

           if (isEncoded) Recorder.xoreArray(Recorder.key, data, 0, data.length);

           FileOutputStream fos = midlet.openFileOutput(name, Context.MODE_PRIVATE);
           fos.write(data);

           fos.flush();
           fos.close();
       } catch (IOException e) {
            return false;
       }
       return true;

   }

    public static synchronized boolean RMSStoreSave(MIDlet midlet, String name, ArrayByte arrayByte, boolean isEncoded, int portData) {//synchronized - исключаем совместный доступ

        try {
            byte[] data = arrayByte.getArray();

            if (isEncoded) Recorder.xoreArray(Recorder.key, data, 0, data.length);

            FileOutputStream fos = midlet.openFileOutput(name, Context.MODE_PRIVATE);
            fos.write(data);

            fos.flush();
            fos.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static ArrayByte RMSStoreRead(MIDlet midlet, String name, boolean isEncoded) {
       //после получения и обработки массива не забываем его убивать! free()
       try {
           ByteArrayOutputStream baos = new ByteArrayOutputStream();
           DataOutputStream dos = new DataOutputStream(baos);

           FileInputStream fis = midlet.openFileInput(name);
           int ch = -1;
           while ((ch = fis.read()) != -1) {
               dos.writeByte(ch);
           }
           fis.close();

           ArrayByte data = new ArrayByte(baos.toByteArray(), 0);

           if (isEncoded) Recorder.xoreArray(Recorder.key, data.getArray(), 0, data.length());

           return data;
       } catch (IOException e) {
           //Debug.trace(com.geargames.microedition.String.valueOfC("Recorder.RMSStoreRead.File not found - ").concat(name));
       }
       return null;

   }

    public static boolean RMSStoreExist(MIDlet midlet, String name) {//очень тяжелая операция

           File file = midlet.getFileStreamPath(name);
           return file != null && file.exists();

   }

    public static boolean RMSStoreClean(MIDlet midlet, String name) {
        try {
            return midlet.deleteFile(name);
        } catch (Exception e) {
            Debug.trace("Recorder.RMSStoreClean.File not deleted -  " + name);
            return false;
        }
   }

    public static boolean RMSStoreDelete(MIDlet midlet, String name) {
        try {
            return midlet.deleteFile(name);
        } catch (Exception e) {
            Debug.trace("Recorder.RMSStoreDelete.File not deleted -  " + name);
            return false;
        }
    }

    public static int RMSRecordSave(MIDlet midlet, String name, byte[] data, int rec) {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);

            short ch = -1;
            try {
                FileInputStream fis = midlet.openFileInput(name);
                while ((ch = (short)((fis.read() << 8) | (fis.read() & 0xff))) != -1) {
                    dos.writeShort(ch);
                    int len = (((fis.read() & 0xff) << 8) | (fis.read() & 0xff));
                    if (ch != rec) {
                        dos.writeShort(len);
                        for (int i = 0; i < len; i++) {
                            dos.writeByte(fis.read());
                        }
                    } else {
                        dos.writeShort(data.length);
                        dos.write(data);
                        break;
                    }
                }
                fis.close();
            } catch (Exception e) {}
            if (ch != rec) {
                dos.writeShort(rec);
                dos.writeShort(data.length);
                dos.write(data);
            }
            FileOutputStream fos = midlet.openFileOutput(name, Context.MODE_PRIVATE);
            fos.write(baos.toByteArray());
            fos.close();
        } catch (IOException e) {}

       return rec;
   }

    public static byte[] RMSRecordRead(MIDlet midlet, String name, int rec) {//чтение нужной записи из рмс

        try {
            FileInputStream fis = midlet.openFileInput(name);
            short ch = -1;
            while ((ch = (short)((fis.read() << 8) | (fis.read() & 0xff))) != -1) {
                int len = (((fis.read() & 0xff) << 8) | (fis.read() & 0xff));
                if (ch != rec) {
                    fis.skip(len);
                } else {
                    byte[] data = new byte[len];
                    fis.read(data);
                    return data;
                }
            }
            fis.close();
        } catch (Exception e) {}
        return null;
   }

    public static void RMSRecordDelete(MIDlet midlet, String name, int rec, boolean withClose) {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);

            FileInputStream fis = midlet.openFileInput(name);
            short ch = -1;
            while ((ch = (short)((fis.read() << 8) | (fis.read() & 0xff))) != -1) {
                int len = (((fis.read() & 0xff) << 8) | (fis.read() & 0xff));
                if (ch != rec) {
                    dos.writeShort(ch);
                    dos.writeShort(len);
                    for (int i = 0; i < len; i++) {
                        dos.writeByte(fis.read());
                    }
                } else {
                    fis.skip(len);
                }
            }
            fis.close();

            FileOutputStream fos = midlet.openFileOutput(name, Context.MODE_PRIVATE);
            fos.write(baos.toByteArray());

            fos.flush();
            fos.close();
        } catch (Exception e) {}

    }

    public static boolean saveRMSBuffer(String name, byte[] data) {//Запись массива
        return true;
    }

    public static byte[][] readRMSBuffer(String name) {//чтение всех записей из рмс
        return null;
    }

    public static int[] RMSMemoryInfol(String name) {
//        Application.logEx("RMS.RMSMemoryInfol, " + name);
       int[] mem = new int[]{0xffffff, 0xffffff};
       return mem;
   }


    public static byte[] readRMSBuffer(MIDlet midlet, String name, int rec) {//чтение нужной записи из рмс

        return RMSRecordRead(midlet, name, rec);

    }

    public static int saveRMSBuffer(MIDlet midlet, String name, byte[] data, int off, int len_, int rec, int max) {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);

            short ch = -1;
            try {
                FileInputStream fis = midlet.openFileInput(name);
                while ((ch = (short)((fis.read() << 8) | (fis.read() & 0xff))) != -1) {
                    dos.writeShort(ch);
                    int len = (((fis.read() & 0xff) << 8) | (fis.read() & 0xff));
                    if (ch != rec) {
                        dos.writeShort(len);
                        for (int i = 0; i < len; i++) {
                            dos.writeByte(fis.read());
                        }
                    } else {
                        dos.writeShort(len_);
                        dos.write(data, off, len_);
                        break;
                    }
                }
                fis.close();
            } catch (Exception e) {}
            if (ch != rec) {
                dos.writeShort(rec);
                dos.writeShort(len_);
                dos.write(data, off, len_);
            }
            FileOutputStream fos = midlet.openFileOutput(name, Context.MODE_PRIVATE);
            fos.write(baos.toByteArray());

            fos.flush();
            fos.close();
        } catch (Exception e) {}

        return rec;

    }

    private static void RMSClean(MIDlet midlet, String name, int rec) {

        RMSRecordDelete(midlet, name, rec, true);

    }

    public static void setEncoded__(boolean encoded) {
        isEncoded = encoded;
    }

    private static void xoreArray(byte[] key, byte[] data, int start_pos, int len) {//ксор ключем данных
        int pos = 0;
        int keyLen = key.length;
        for (int i = start_pos; i < len; i++) {
            data[i] = (byte) ((data[i] & 0xff) ^ key[pos]);
            pos = (pos + 1) % keyLen;
        }
    }
    private static byte[] key = {52, 45, 18, 45, 87, 52, 45, 18, 45, 87, 11, 93, 12, 53, 3, 122, 65, 98, 34, 55, 11, 65, 23, 36, 86, 23, 29, 44, 2, 76, 33, 98, 2, 8, 33, 48, 61, 113, 34, 23, 54, 22, 29, 44, 2, 76, 33, 98, 2, 8, 33, 48, 61, 113, 34, 23, 54, 22, 98, 34, 55, 11, 65, 23, 113, 34, 23, 54, 22, 98, 34, 55, 44, 2, 76, 33, 98, 2, 8, 33, 48, 61, 113, 34, 23, 54, 22, 98, 34, 55, 11, 65, 23, 113, 34, 23, 54, 22, 98, 34, 55};

}
