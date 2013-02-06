package gg;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotFoundException;
import java.io.*;

/**
 * Запись и чтение данных из памяти телефона */
public class Recorder {
    final static boolean DEBUG_RMS = false;

    public static void setContext(gg.microedition.MIDlet midlet) {

    }
    /* *****************************************************************************
  * RMS                                         *
  * *******************************************************************************/
    public static boolean RMSStoreSave(String name, ByteArrayOutputStream baos) {
       byte data[] = baos.toByteArray();
       RecordStore rs;
       try {
           rs = RecordStore.openRecordStore(name, true);
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Debug.logEx(e);
           return false;
       }
       try {
           if (rs.getNumRecords() > 0) {
               RecordEnumeration recordEnum = rs.enumerateRecords(null, null, true);
               int id = recordEnum.nextRecordId();
               rs.setRecord(id, data, 0, data.length);
           } else {
               rs.addRecord(data, 0, data.length);
           }
           rs.closeRecordStore();
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Debug.logEx(e);
           return false;
       }
       return true;
   }

    public static byte[] RMSStoreRead(String name) {
       RecordStore rs;
       try {
           rs = RecordStore.openRecordStore(name, false);
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Debug.trace(gg.microedition.String.valueOfC("RMS. Open(name). Record not found, name:").concatC(name));
           return null;
       }
       byte data[] = null;
       try {
           //if (DEBUG_RMS) logEx("RMS.RMSStoreRead, name:" + name + ", rec count:" + rs.getNumRecords());
           if (rs.getNumRecords() > 0) {
               RecordEnumeration recordEnum = rs.enumerateRecords(null, null, true);
               data = recordEnum.nextRecord();
           }
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Debug.logEx(e);
           return null;
       }
       try {
           if (rs != null) rs.closeRecordStore();
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Debug.logEx(e);
           return null;
       }
       return data;
   }

    public static void RMSStoreClean(String name) {
       Debug.trace(gg.microedition.String.valueOfC("RMS delete, ").concatC(name));
       RecordStore rs;
       try {
           rs = RecordStore.openRecordStore(name, false);
       } catch (RecordStoreNotFoundException e) {
           if (DEBUG_RMS) Debug.trace(gg.microedition.String.valueOfC("RMS.Clean.Record not found, name:").concatC(name));
           return;
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Debug.logEx(e);
           return;
       }
       try {
           RecordEnumeration recordEnum = rs.enumerateRecords(null, null, true);
           while (rs.getNumRecords() > 0) {
               int i = recordEnum.nextRecordId();
               Debug.trace(gg.microedition.String.valueOfC(" rec:").concatI(i));
               rs.deleteRecord(i);
           }
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Debug.logEx(e);
           return;
       }
       try {
           rs.closeRecordStore();
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Debug.logEx(e);
       }
   }

    public static void RMSStoreDelete(String name) {
       try {
           Debug.trace(gg.microedition.String.valueOfC("RMS. Delete, ").concatC(name));
           RecordStore.deleteRecordStore(name);
       } catch (RecordStoreNotFoundException e) {
           if (DEBUG_RMS) Debug.trace(gg.microedition.String.valueOfC("RMS. Delete. Record not found, name:").concatC(name));
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Debug.logEx(e);
           Debug.logEx(e);
       }
   }

    public static int RMSRecordSave(String name, byte[] data, int rec) {
       RecordStore rs;
       try {
           rs = RecordStore.openRecordStore(name, true);
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Debug.logEx(e);
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
           if (DEBUG_RMS) Debug.logEx(e);
           return -1;
       } finally {
           try {
               if (rs != null) rs.closeRecordStore();
           } catch (RecordStoreException e) {
               if (DEBUG_RMS) Debug.logEx(e);
           }
       }
       return rec;
   }

    public static byte[] RMSRecordRead(String name, int rec) {//чтение нужной записи из рмс
       RecordStore rs;
       byte[] data = null;
       try {
           rs = RecordStore.openRecordStore(name, false);
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Debug.trace(gg.microedition.String.valueOfC("RMS. Open(rec). Record not found, name:").concatC(name));
           return data;
       }
       try {
           data = rs.getRecord(rec);
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Debug.trace(gg.microedition.String.valueOfC("RMS. Open(rec). Record not found, name:").concatC(name).concatC(", rec:").concatI(rec));
           if (DEBUG_RMS) Debug.logEx(e);
           return data;
       }
       try {
           if (rs != null) rs.closeRecordStore();
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Debug.logEx(e);
           return data;
       }
       return data;
   }

    public static void RMSRecordDelete(String name, int rec, boolean withClose) {
       RecordStore rs;
       try {
           rs = RecordStore.openRecordStore(name, false);
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Debug.logEx(e);
           return;
       }
       try {
           int size = rs.getRecordSize(rec);
           rs.deleteRecord(rec);
           Debug.trace(gg.microedition.String.valueOfC("RMS delete, ").concatC(name).concatC(", rec:").concatI(rec).concatC("[").concatI(size).concatC("]"));
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Debug.logEx(e);
       }
       try {
           if (withClose) rs.closeRecordStore();
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Debug.logEx(e);
       }
   }

    public static boolean saveRMSBuffer(String name, byte[] data) {//Запись массива
       RecordStore rs;
       try {
           rs = RecordStore.openRecordStore(name, true);
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Debug.logEx(e);
           return false;
       }
       try {
           int rec = rs.addRecord(data, 0, data.length);
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Debug.logEx(e);
           return false;
       } finally {
           try {
               if (rs != null) rs.closeRecordStore();
           } catch (RecordStoreException e) {
               if (DEBUG_RMS) Debug.logEx(e);
           }
       }
       return true;
   }

    public static byte[][] readRMSBuffer(String name) {//чтение всех записей из рмс
       RecordStore rs;
       try {
           rs = RecordStore.openRecordStore(name, false);
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Debug.trace(gg.microedition.String.valueOfC("RMS. Open. Record not found, name:").concatC(name));
           return null;
       }
       byte[][] data = null;
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
           if (DEBUG_RMS) Debug.logEx(e);
           return data;
       }
       try {
           if (rs != null) rs.closeRecordStore();
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Debug.logEx(e);
           return data;
       }
       return data;
   }

    public static int[] RMSMemoryInfol(String name) {
//        Application.logEx("RMS.RMSMemoryInfol, " + name);
       int[] mem = new int[]{0, 0};
       RecordStore rs;
       try {
           rs = RecordStore.openRecordStore(name, false);
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Debug.trace(gg.microedition.String.valueOfC("RMS. Open(rec). Record not found, name:").concatC(name));
           return mem;
       }
       try {
           mem[0] = rs.getSize();
           mem[1] = rs.getSizeAvailable();
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Debug.logEx(e);
           return mem;
       }
       try {
           if (rs != null) rs.closeRecordStore();
       } catch (RecordStoreException e) {
           if (DEBUG_RMS) Debug.logEx(e);
           return mem;
       }
       return mem;
   }


    public static byte[] readRMSBuffer(String name, int rec) {//чтение нужной записи из рмс
        RecordStore rs;
        byte[] data = null;
        try {
            rs = RecordStore.openRecordStore(name, false);
        } catch (RecordStoreException e) {
            return data;
        }
        try {
            data = rs.getRecord(rec);
        } catch (RecordStoreException e) {
            return data;
        }
        try {
            if (rs != null) rs.closeRecordStore();
        } catch (RecordStoreException e) {
            return data;
        }
        return data;
    }

    public static int saveRMSBuffer(String name, byte[] data, int off, int len, int rec, int max) {
        RecordStore rs;
        try {
            rs = RecordStore.openRecordStore(name, true);
        } catch (RecordStoreException e) {
            return -1;
        }
        try {
            boolean set = false;
            if (rs.getNumRecords() > 0) {
                try {
                    rs.setRecord(rec, data, off, len);
                    set = true;
                } catch (Exception e) {
                }
            }
            if (!set) {
                rec = rs.addRecord(data, off, len);
                if (rec >= max) {
                    RMSClean(name, rec);
                    rec = -1;
                }
            }
        } catch (RecordStoreException e) {
            return -1;
        } finally {
            try {
                if (rs != null) rs.closeRecordStore();
            } catch (RecordStoreException e) {
            }
        }
        return rec;
    }

    private static void RMSClean(String name, int rec) {
        RecordStore rs;
        try {
            rs = RecordStore.openRecordStore(name, false);
        } catch (RecordStoreException e) {
            return;
        }
        try {
            int size = rs.getRecordSize(rec);
            rs.deleteRecord(rec);
            rs.closeRecordStore();
        } catch (RecordStoreException e) {
        }
    }

}
