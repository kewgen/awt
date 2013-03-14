package com.geargames.util;

import com.geargames.common.util.ArrayByte;
import com.geargames.common.util.Cypher;
import com.geargames.common.util.Recorder;

import java.io.*;

/**
 * Запись и чтение данных из файла
 */
public class ConsoleRecorder implements Recorder {
    private File folder;
    private File properties;

    public ConsoleRecorder(File folder, File properties) {
        this.folder = folder;
        this.properties = properties;
    }

    public void store(String name, ArrayByte data, Cypher cypher) throws Exception {
        cypher.encode(data);
        store(name, data);
    }

    public ArrayByte load(String name, Cypher cypher) throws Exception {
        ArrayByte data = load(name);
        cypher.decode(data);
        return data;
    }

    public void store(String name, ArrayByte data) throws Exception {
        File file = new File(folder.getPath() + File.separator + name);

        DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
        try {
            output.write(data.getArray().length);
            output.write(data.getArray());
        } finally {
            output.close();
        }
    }

    public ArrayByte load(String name) throws Exception {
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

    public void delete(String name) throws Exception {
        File file = new File(folder.getPath() + File.separator + name);
        file.delete();
    }

    public void storeProperty(String name, String data) throws Exception {
        System.setProperty(name, data);
        File file = new File(properties.getPath() + File.separator + name);
        Writer writer = new FileWriter(file);
        System.getProperties().store(writer, "");
    }

    public String loadProperty(String name) throws Exception {
        return System.getProperties().getProperty(name);
    }

}
