package com.geargames.util;

import com.geargames.common.util.AbstractRecorder;
import com.geargames.common.util.ArrayByte;
import com.geargames.common.util.Cypher;

import java.io.*;

/**
 * Запись и чтение данных и свойств приложения из ФС.
 */
public class ConsoleRecorder implements AbstractRecorder {
    private File folder;
    private File properties;

    public ConsoleRecorder(File folder, File properties) {
        this.folder = folder;
        this.properties = properties;
    }

    public synchronized void store(String name, ArrayByte data, Cypher cypher) throws Exception {
        cypher.encode(data);
        store(name, data);
    }

    public synchronized ArrayByte load(String name, Cypher cypher) throws Exception {
        ArrayByte data = load(name);
        cypher.decode(data);
        return data;
    }

    public synchronized void store(String name, ArrayByte data) throws Exception {
        File file = new File(folder.getPath() + File.separator + name);

        DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
        try {
            output.write(data.getArray().length);
            output.write(data.getArray());
        } finally {
            output.close();
        }
    }

    public synchronized ArrayByte load(String name) throws Exception {
        File file = new File(folder.getPath() + File.separator + name);

        DataInputStream input = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
        try {
            byte[] array = new byte[input.readInt()];
            input.read(array);
            return new ArrayByte(array, 0);
        } finally {
            input.close();
        }
    }

    public synchronized void delete(String name) throws Exception {
        File file = new File(folder.getPath() + File.separator + name);
        file.delete();
    }

    public synchronized void storeStringProperty(String name, String data) throws Exception {
        System.setProperty(name, data);
        Writer writer = new FileWriter(properties);
        try {
            System.getProperties().store(writer, "");
        } finally {
            writer.close();
        }
    }

    public synchronized void storeIntegerProperty(String name, int data) throws Exception {
        storeStringProperty(name, String.valueOf(data));
    }

    public synchronized void storeDoubleProperty(String name, double data) throws Exception {
        storeStringProperty(name, String.valueOf(data));
    }

    public synchronized void storeBooleanProperty(String name, boolean data) throws Exception {
        storeStringProperty(name, String.valueOf(data));
    }

    public synchronized String loadStringProperty(String name) throws Exception {
        String property = System.getProperties().getProperty(name);
        if (property == null) {
            Reader reader = new FileReader(properties);
            try{
            System.getProperties().load(reader);
            }catch (Exception e){
                //todo
            }
            finally {
                reader.close();
            }
            property = System.getProperties().getProperty(name);
            if(property == null){
                throw new Exception();
            }
        }
        return property;
    }

    public synchronized int loadIntegerProperty(String name) throws Exception {
        return Integer.valueOf(loadStringProperty(name));
    }

    public synchronized double loadDoubleProperty(String name) throws Exception {
        return Double.valueOf(loadStringProperty(name));
    }

    public synchronized boolean loadBooleanProperty(String name) throws Exception {
        return Boolean.valueOf(loadStringProperty(name));
    }
}
