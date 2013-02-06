package com.geargames.media;

import android.media.MediaPlayer;
import com.geargames.Debug;
import com.geargames.common.media.Player;
import com.geargames.common.util.ArrayByte;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SoundPlayer implements Player {


    private MediaPlayer player;
    private String filename;
    private boolean isPlayed;//проигрывается
    private FileInputStream fis;

    public SoundPlayer(String path, InputStream fis, ArrayByte data, boolean isMp3) {
        this.fis = (FileInputStream) fis;
        create(path);
    }

    private void create(String path) {
        try {
            player = new MediaPlayer();
            filename = path;
            isPlayed = false;

            Debug.trace("create." + toString());
            if (fis == null) {
                //чтение из assets
                //AssetFileDescriptor descriptor = Manager.getInstance().getMidlet().getAssets().openFd(path);
                //player.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            } else {
                //чтение из FileInputStream
                FileDescriptor descriptor = fis.getFD();
                player.setDataSource(descriptor);
            }

            player.setLooping(false);
            player.prepare();

            //Debug.log("Prepared." + toString());

        } catch (java.io.FileNotFoundException e) {
            Debug.logEx(e);
            player = null;
        } catch (IOException e) {//setDataSourceFD failed.: status=0x80000000
            Debug.trace("Sound create error." + e.toString() + toString());
        } catch (IllegalStateException e) {//не поддерживается устройством
            Debug.trace("Sound create error." + e.toString());
        } catch (Exception e) {
            Debug.logEx(e);
        }
    }

    public void play() {
        if (player == null) return;
        player.start();
        //log("SoundPlayer.play, name:" + filename + ", state:" + getState());
    }

    public void stop() {
        if (player == null) return;
        try {
            if (player.isPlaying()) player.pause();
            //log("SoundPlayer.play, name:" + filename + ", state:" + getState());
        } catch (Exception e) {
            log(e);
        }
    }

    public boolean isReady() {
        return player != null && !player.isPlaying();
    }

    public void close() {
        if (player == null) return;

        player.stop();
        player.release();

    }

    public void setLoopCount(int loop) {
        if (player == null) return;
        player.setLooping(loop > 1);
        //log("SoundPlayer.setLoopCount.loop:" + loop + "." + toString());
    }

    public void setVolume(int volume) {//0-5
        if (player == null) return;
        try {
            player.setVolume(volume, volume);
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
