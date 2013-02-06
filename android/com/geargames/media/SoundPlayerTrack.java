package com.geargames.media;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import com.geargames.Debug;
import com.geargames.common.media.Player;
import com.geargames.common.util.ArrayByte;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 16.08.12
 * Time: 13:23
 * Более низкоуровневый плеер, работает с байтовым массивом, нет функции зацикливания
 */
public class SoundPlayerTrack implements Player {


    private AudioTrack player;
    private String filename;
    private boolean isPlayed;//проигрывается
    private ArrayByte data;

    public SoundPlayerTrack(String path, ArrayByte data, boolean isMp3) {
        this.data = data;
        create(path);
    }

    private void create(String path) {//НЕ РАБОТАЕТ
        try {
            Debug.trace(" player, path:" + path);
            if (data == null) return;
            player = new AudioTrack(AudioManager.STREAM_MUSIC, 48000, AudioFormat.CHANNEL_CONFIGURATION_DEFAULT /*CHANNEL_OUT_DEFAULT api > 8*/, AudioFormat.ENCODING_PCM_16BIT, data.length(), AudioTrack.MODE_STREAM);
            player.write(data.getArray(), 0, data.length());
            filename = path;
            isPlayed = false;

            player.release();
            Debug.trace("Prepared." + toString());
            play();
        } catch (IllegalArgumentException e) {
            Debug.trace(e.toString() + " " + toString());
        } catch (Exception e) {
            Debug.logEx(e);
        }
    }

    public void play() {
        if (player == null) return;
        try {
            if (player.getState() != AudioTrack.STATE_INITIALIZED) return;
            player.play();
        } catch (IllegalStateException e) {
            log(e.toString() + " " + toString());
        }
        //log("SoundPlayer.play, name:" + filename + ", state:" + getState());
    }

    public void stop() {
        if (player == null) return;
        try {
            if (player.getState() != AudioTrack.STATE_INITIALIZED) return;
            player.pause();
            //log("SoundPlayer.play, name:" + filename + ", state:" + getState());
        } catch (Exception e) {
            log(e);
        }
    }

    public boolean isReady() {
        return player != null && player.getPlayState() != AudioTrack.PLAYSTATE_PLAYING;
    }

    public void close() {
        if (player == null) return;

        player.stop();
        player.release();

    }

    public void setLoopCount(int loop) {
        if (player == null) return;
        //player.setLooping(loop > 1);
        //log("SoundPlayer.setLoopCount.loop:" + loop + "." + toString());
    }

    public void setVolume(int volume) {//0-5
        if (player == null) return;
        try {
            //player.setVolume(volume, volume);
        } catch (Exception e) {
            log(e);
        }
    }

    private long sms_time = 0;

    public String getTrackName() {
        return filename;
    }

    private void log(String str) {
        Debug.trace(str);
    }

    private void log(Exception e) {
        Debug.trace(null, e);
    }

    @Override
    public String toString() {
        return "SoundPlayer{" +
                "player=" + player +
                ", filename='" + filename + '\'' +
                ", isPlayed=" + isPlayed +
                '}';
    }
}
