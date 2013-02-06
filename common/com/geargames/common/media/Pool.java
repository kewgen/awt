package com.geargames.common.media;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 30.08.12
 * Time: 17:46
 * интерфейс пула загрузки, хранения, проигрывания и остановки звуков
 */
public interface Pool {

    void load(Object midlet, com.geargames.common.String path, int id);

    void play(int id, int volume, int loop);

    void stop(int id, int a);
}
