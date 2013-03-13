package com.geargames;

import com.geargames.common.env.SystemEnvironment;
import com.geargames.common.util.ArrayByte;

import java.io.*;

/**
 * Запись и чтение данных из файла
 */
public class Recorder {
    final static boolean DEBUG_RMS = false;
    private static boolean isEncoded = false;

    private Recorder() {
    }

    private static Recorder self;

    public synchronized static Recorder getInstance() {
        if (self == null) {
            self = new Recorder();
        }
        return self;
    }

    static private MIDlet context;

    public static void setContext(MIDlet midlet) {
        context = midlet;
    }

    /**
     * ***************************************************************************
     * RMF
     * ******************************************************************************
     */
    public static synchronized boolean RMSStoreSave(String name, ByteArrayOutputStream baos, boolean isEncoded) {//synchronized - исключаем совместный доступ
        byte[] data = baos.toByteArray();

        if (isEncoded){
            Recorder.xoreArray(Recorder.key, data, 0, data.length);
        }

        File file = new File(name + ".storage");
        if (file.exists()){
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            DataOutputStream dos = new DataOutputStream(fos);
            dos.writeInt(data.length);//первым байтом кладем размер записи
            fos.write(data);
            dos.close();

            if (DEBUG_RMS) {
                SystemEnvironment.getInstance().getDebug().trace(com.geargames.common.String.valueOfC("Recorder.RMSStoreSave, path ").concat(file.getAbsolutePath()));
            }
            return true;
        } catch (IOException e) {
            SystemEnvironment.getInstance().getDebug().exception(com.geargames.common.String.valueOfC("RMS exception"), e);
        }
        return false;
    }

    public static synchronized boolean RMSStoreSave(String name, ArrayByte arrayByte, boolean isEncoded, int portData) {//synchronized - исключаем совместный доступ
        byte[] data = arrayByte.getArray();

        if (isEncoded) Recorder.xoreArray(Recorder.key, data, 0, data.length);

        File file = new File(name + ".storage");
        if (file.exists()) file.delete();
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            DataOutputStream dos = new DataOutputStream(fos);
            dos.writeInt(data.length);//первым байтом кладем размер записи
            fos.write(data);
            dos.close();

            if (DEBUG_RMS) {
                SystemEnvironment.getInstance().getDebug().trace(com.geargames.common.String.valueOfC("Recorder.RMSStoreSave, path " + file.getAbsolutePath()));
            }
            return true;
        } catch (IOException e) {
            SystemEnvironment.getInstance().getDebug().exception(com.geargames.common.String.valueOfC("RMS store exception"),e);
        }
        return false;
    }

    public static ArrayByte RMSStoreRead(String name, boolean isEncoded) {
        File file = new File(name + ".storage");
        byte data[] = null;
        if (!file.exists()) return null;
        try {
            FileInputStream fis = new FileInputStream(file);
            DataInputStream dis = new DataInputStream(fis);
            int size = dis.readInt();
            data = new byte[size];
            fis.read(data);
            dis.close();

            if (isEncoded) Recorder.xoreArray(Recorder.key, data, 0, data.length);

            if (DEBUG_RMS) {
                SystemEnvironment.getInstance().getDebug().trace(com.geargames.common.String.valueOfC("Recorder.RMSStoreRead, path ").concat(file.getAbsolutePath()));
            }
        } catch (IOException e) {
            SystemEnvironment.getInstance().getDebug().exception(com.geargames.common.String.valueOfC("RMS read exception"),e);
        }
        return new ArrayByte(data, 0);
    }

    public static boolean RMSStoreExist(String name) {
        File file = new File(name + ".storage");
        return file.exists();
    }

    public DataInputStream RMSStoreStreamRead(String name) {//открываем поток из рмс
        File file = new File(name + ".storage");
        DataInputStream dis = null;
        if (!file.exists()) return dis;
        try {
            FileInputStream fis = new FileInputStream(file);
            dis = new DataInputStream(fis);

            if (DEBUG_RMS){
                SystemEnvironment.getInstance().getDebug().trace(com.geargames.common.String.valueOfC("Recorder.RMSStoreStreamRead, path " + file.getAbsolutePath()));
            }
        } catch (IOException e) {
            SystemEnvironment.getInstance().getDebug().exception(com.geargames.common.String.valueOfC("RMS stream read"), e);
        }
        return dis;
    }

    public static boolean RMSStoreClean(String name) {
        File file = new File(name + ".storage");
        if (!file.exists()) return false;
        if (!file.delete()) return false;

        if (DEBUG_RMS){
            SystemEnvironment.getInstance().getDebug().trace(com.geargames.common.String.valueOfC("Recorder.RMSStoreClean, path " + file.getAbsolutePath()));
        }
        return true;
    }

    public static void RMSStoreDelete(String name) {
/*
       try {
           Manager.logEx("RMS. Delete, " + name);
           RecordStore.deleteRecordStore(name);
       } catch (RecordStoreNotFoundException e) {
           if (DEBUG_RMS) Manager.logEx("RMS. Delete. Record not found, name:" + name);
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Manager.logEx(e);
           Manager.logEx(e);
       }
*/
    }

    public static int RMSRecordSave(String name, byte[] data, int rec) {
/*
       RecordStore rs;
       try {
           rs = RecordStore.openRecordStore(name, true);
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Manager.logEx(e);
           return -1;
       }
       try {
           boolean set = false;
           if (rs.getNumRecords() > 0) {
               RecordEnumeration recordEnum = rs.enumerateRecords(null, null, true);
               int id = recordEnum.nextRecordId();
               if (id == rec) {
                   rs.setRecord(rec, data, 0, data.length);
                   set = true;
               }
           }
           if (!set) rec = rs.addRecord(data, 0, data.length);
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Manager.logEx(e);
           return -1;
       } finally {
           try {
               if (rs != null) rs.closeRecordStore();
           } catch (RecordStoreException e) {
               if (DEBUG_RMS) Manager.logEx(e);
           }
       }
*/
        return rec;
    }

    public static byte[] RMSRecordRead(String name, int rec) {//чтение нужной записи из рмс
        byte[] data = null;
/*
        RecordStore rs;
       try {
           rs = RecordStore.openRecordStore(name, false);
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Manager.logEx("RMS. Open(rec). Record not found, name:" + name);
           return data;
       }
       try {
           data = rs.getRecord(rec);
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Manager.logEx("RMS. Open(rec). Record not found, name:" + name + ", rec:" + rec);
           if (DEBUG_RMS) Manager.logEx(e);
           return data;
       }
       try {
           if (rs != null) rs.closeRecordStore();
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Manager.logEx(e);
           return data;
       }
*/
        return data;
    }

    public static void RMSRecordDelete(String name, int rec, boolean withClose) {
/*
       RecordStore rs;
       try {
           rs = RecordStore.openRecordStore(name, false);
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Manager.logEx(e);
           return;
       }
       try {
           int size = rs.getRecordSize(rec);
           rs.deleteRecord(rec);
           Manager.logEx("RMS delete, " + name + ", rec:" + rec + "[" + size + "]");
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Manager.logEx(e);
       }
       try {
           if (withClose) rs.closeRecordStore();
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Manager.logEx(e);
       }
*/
    }

    public static boolean saveRMSBuffer(String name, byte[] data) {//Запись массива
/*
       RecordStore rs;
       try {
           rs = RecordStore.openRecordStore(name, true);
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Manager.logEx(e);
           return false;
       }
       try {
           int rec = rs.addRecord(data, 0, data.length);
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Manager.logEx(e);
           return false;
       } finally {
           try {
               if (rs != null) rs.closeRecordStore();
           } catch (RecordStoreException e) {
               if (DEBUG_RMS) Manager.logEx(e);
           }
       }
*/
        return true;
    }

    public static byte[][] readRMSBuffer(String name) {//чтение всех записей из рмс
/*
       RecordStore rs;
       try {
           rs = RecordStore.openRecordStore(name, false);
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Manager.logEx("RMS. Open. Record not found, name:" + name);
           return null;
       }
*/
        byte[][] data = null;
/*
       try {
           int rec = rs.getNumRecords();
           if (rec > 0) {
               data = new byte[rec][];
               RecordEnumeration recordEnum = rs.enumerateRecords(null, null, true);
               for (int i = 0; i < rec; i++) {
                   data[i] = recordEnum.nextRecord();
               }
           }
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Manager.logEx(e);
           return data;
       }
       try {
           if (rs != null) rs.closeRecordStore();
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Manager.logEx(e);
           return data;
       }
*/
        return data;
    }

    public static int[] RMSMemoryInfol(String name) {
//        Application.logEx("RMS.RMSMemoryInfol, " + name);
        int[] mem = new int[]{0, 0};
/*
       RecordStore rs;
       try {
           rs = RecordStore.openRecordStore(name, false);
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Manager.logEx("RMS. Open(rec). Record not found, name:" + name);
           return mem;
       }
       try {
           mem[0] = rs.getSize();
           mem[1] = rs.getSizeAvailable();
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Manager.logEx(e);
           return mem;
       }
       try {
           if (rs != null) rs.closeRecordStore();
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Manager.logEx(e);
           return mem;
       }
*/
        return mem;
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
