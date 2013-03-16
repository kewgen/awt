package com.geargames.common.util;

/**
 * User: mkutuzov
 * Date: 15.03.13
 */
public class Recorder {
    private static AbstractRecorder recorder;

    public static AbstractRecorder getRecorder() {
        return recorder;
    }

    public static void setRecorder(AbstractRecorder recorder) {
        Recorder.recorder = recorder;
    }

    /**
     * Записать данные data в хранилище под именем name и зашифровать их способом cypher.
     *
     * @param name
     * @param data
     * @param cypher
     * @throws Exception в случае проблем с записью.
     */
    public static void store(String name, ArrayByte data, Cypher cypher) throws Exception{
        recorder.store(name, data, cypher);
    }

    /**
     * Загрузить данные из хранилища по имени name и расшифровать их способом cypher.
     * @param name
     * @param cypher
     * @return расщифрованнные данные из хранилища.
     * @throws Exception в случае проблем с чтением данных.
     */
    public static ArrayByte load(String name, Cypher cypher) throws Exception {
        return recorder.load(name, cypher);
    }

    /**
     * Записать данные data в хранилище под именем name.
     *
     * @param name
     * @param data
     * @throws Exception в случае проблем с записью.
     */
    public static void store(String name, ArrayByte data) throws Exception{
        recorder.store(name, data);
    }

    /**
     * Загрузить данные из локального хранилища по имени name.
     *
     * @param name
     * @return
     * @throws Exception в случае отсутсвия данных.
     */
    public static ArrayByte load(String name) throws Exception {
        return recorder.load(name);
    }


    /**
     * Удалить данные из хранилища.
     *
     * @param name
     * @throws Exception
     */
    public static void delete(String name) throws Exception{
          recorder.delete(name);
    }

    /**
     * Сохранить свойство data под именем name.
     *
     * @param name
     * @param data
     * @throws Exception в случае проблем с записью.
     */
    public static void storeStringProperty(String name, String data) throws Exception{
        recorder.storeStringProperty(name, data);
    }

    public static void storeIntegerProperty(String name, int data) throws  Exception {
        recorder.storeIntegerProperty(name, data);
    }

    public static void storeDoubleProperty(String name, double data) throws Exception {
        recorder.storeDoubleProperty(name,data);
    }

    public static void storeBooleanProperty(String name, boolean data) throws Exception {
        recorder.storeBooleanProperty(name,data);
    }

    /**
     * Загрузить свойство по имени name.
     *
     * @param name
     * @return
     * @throws Exception в случае отсутсвия данных.
     */
    public static String loadStringProperty(String name) throws Exception{
          return recorder.loadStringProperty(name);
    }

    public static int loadIntegerProperty(String name) throws Exception{
          return recorder.loadIntegerProperty(name);
    }

    public static double loadDoubleProperty(String name) throws Exception{
          return recorder.loadDoubleProperty(name);
    }

    public static boolean loadBooleanProperty(String name) throws Exception{
          return recorder.loadBooleanProperty(name);
    }


}
