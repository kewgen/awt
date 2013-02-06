package com.geargames.media;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import com.geargames.Debug;
import com.geargames.common.String;

import java.io.File;
import java.util.Hashtable;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 30.08.12
 * Time: 13:45
 * Менеджер управления звуками
 */
public class Pool extends android.media.SoundPool implements com.geargames.common.media.Pool {

    public Pool(int maxStreams) {
        super(maxStreams, AudioManager.STREAM_MUSIC, 0);
        list = new Hashtable<Integer, Sound>(maxStreams);
    }

    public void load(Object midlet, String path, int id) {//загрузка звуков записанных через FileOutputStream
        Activity activity = (Activity) midlet;
        Sound sound = new Sound(path);

        //получить File записанный через FileOutputStream можно только через getFileStreamPath
        File file = activity.getFileStreamPath(path.toString());
        if (!file.exists()) {
            Debug.trace("File not found, " + file.getAbsolutePath());
            return;
        }
        int poolID = super.load(file.getPath(), 1);
        sound.setId(poolID);

        if (poolID == 0) Debug.trace("Error loading, " + sound.toString());
        else {
            Debug.trace("Sound loaded, " + sound.toString());
            list.put(id, sound);
        }

    }

    public void play(int id, int volume, int loop) {//volume 0..100  loop mode (0 = no loop, -1 = loop forever)
        float volLR = (float) (volume / 100.0);
        Sound sound = list.get(id);
        if (sound == null) {
            Debug.trace("Sound not found, id:" + id);
            return;
        }
        super.play(sound.getId(), volLR, volLR, 1, loop, 1.0f);
    }

    public void stop(int id, int a) {
        Sound sound = list.get(id);
        if (sound == null) return;
        super.stop(sound.getId());
    }

    public boolean isReady(int id) {
        Sound sound = list.get(id);
        return sound != null;
    }

    public void releasePool() {
        release();
    }

    @Override
    public java.lang.String toString() {
        return "Pool{" +
                "list=" + list +
                '}';
    }

    private Hashtable<Integer, Sound> list;

}
