package com.geargames.common.network;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 23.08.12
 * Time: 13:21
 * счётчик полученных байт c сервера
 */
public interface HTTPCounter {
    public void nextKBytes();
}
