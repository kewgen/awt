package com.geargames.common.util;

/**
 * User: mkutuzov
 * Date: 14.03.13
 * На каждой платформе должно быть реализовано расширение этого класса для записи даных в хранилище устройства.
 */
public interface AbstractRecorder {

    /**
     * Записать данные data в хранилище под именем name и зашифровать их способом cypher.
     *
     * @param name
     * @param data
     * @param cypher
     * @throws Exception в случае проблем с записью.
     */
    void store(String name, ArrayByte data, Cypher cypher) throws Exception;

    /**
     * Загрузить данные из хранилища по имени name и расшифровать их способом cypher.
     * @param name
     * @param cypher
     * @return расщифрованнные данные из хранилища.
     * @throws Exception в случае проблем с чтением данных.
     */
    ArrayByte load(String name, Cypher cypher) throws Exception;

    /**
     * Записать данные data в хранилище под именем name.
     *
     * @param name
     * @param data
     * @throws Exception в случае проблем с записью.
     */
    void store(String name, ArrayByte data) throws Exception;

    /**
     * Загрузить данные из локального хранилища по имени name.
     *
     * @param name
     * @return
     * @throws Exception в случае отсутсвия данных.
     */
    ArrayByte load(String name) throws Exception;

    /**
     * Удалить данные из хранилища.
     *
     * @param name
     * @throws Exception
     */
    void delete(String name) throws Exception;

    /**
     * Сохранить свойство data под именем name.
     *
     * @param name
     * @param data
     * @throws Exception в случае проблем с записью.
     */
    void storeStringProperty(String name, String data) throws Exception;

    void storeIntegerProperty(String name, int data) throws Exception;

    void storeDoubleProperty(String name, double data) throws Exception;

    void storeBooleanProperty(String name, boolean data) throws Exception;

    /**
     * Загрузить свойство по имени name.
     *
     * @param name
     * @return
     * @throws Exception в случае отсутсвия данных.
     */
    String loadStringProperty(String name) throws Exception;

    int loadIntegerProperty(String name) throws Exception;

    double loadDoubleProperty(String name) throws Exception;

    boolean loadBooleanProperty(String name) throws Exception;

}
