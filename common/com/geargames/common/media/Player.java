package com.geargames.common.media;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 21.06.12
 * Time: 19:49
 * плеер проигрывающий один звук
 */
public interface Player {

    void play();

    void stop();

    boolean isReady();

    void setVolume(int volume);

    void setLoopCount(int loopCount);

}
