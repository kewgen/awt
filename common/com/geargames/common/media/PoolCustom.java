package com.geargames.common.media;

import com.geargames.common.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 20.06.12
 * Time: 12:30
 * Пул плееров самопальный, есть альтернатива
 */
public abstract class PoolCustom {

    private int buffersCount;
    private ArrayList list;

    public PoolCustom(int buffersCount) {
        this.buffersCount = buffersCount;
        list = new ArrayList(buffersCount);
//        for (int i = 0; i < buffersCount; i++) {
//            set(new Player(path));
//        }
    }

    protected void add(Player player) {
         list.add(player);
    }

    public void play() {//запускаем свободный плеер
        for (int i = 0; i < buffersCount; i++) {
            Player player = (Player)list.get(i);
            if (player.isReady()) {
                player.play();
                //Debug.log("Pool. " + player.toString());
                break;
            }
        }
    }

    public void stop() {//останавливаем все плееры
        for (int i = 0; i < buffersCount; i++) {
            Player player = (Player)list.get(i);
            player.stop();
        }
    }

    public void setLoopCount(int loopCount) {
        for (int i = 0; i < buffersCount; i++) {
            Player player = (Player)list.get(i);
            player.setLoopCount(loopCount);
        }
    }

    public void setVolume(int volume) {
        for (int i = 0; i < buffersCount; i++) {
            Player player = (Player)list.get(i);
            player.setVolume(volume);
        }
    }
}
