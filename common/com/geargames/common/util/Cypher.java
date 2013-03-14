package com.geargames.common.util;

/**
 * User: mkutuzov
 * Date: 14.03.13
 * Класс который кодирует/раскодируеи массив байт для записи в локальную ФС должен реализовать этот интерфейс.
 */
public interface Cypher {
    public ArrayByte encode(ArrayByte data);
    public ArrayByte decode(ArrayByte data);
}
