package com.geargames.media;

import com.geargames.Debug;
import com.geargames.Recorder;
import com.geargames.common.String;
import com.geargames.common.util.ArrayByte;

import java.util.Hashtable;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 30.08.12
 * Time: 17:33
 */
public class Pool implements com.geargames.common.media.Pool {

    public Pool(int streamCount) {
        list = new Hashtable<Integer, SoundPlayer>(streamCount);
    }

    public void load(Object midlet, String path, int id) {
        ArrayByte arrayByte = Recorder.RMSStoreRead(path.toString(), false);
        SoundPlayer player = new SoundPlayer(path.toString(), null, arrayByte, false);
        list.put(id, player);
    }

    public void play(int id, int volume, int loopCount) {
        if (list.get(id) == null) {
            Debug.trace("Sound not found, id:" + id);
            return;
        }
        list.get(id).setVolume(volume);
        list.get(id).play();
    }

    public void stop(int id, int a) {
        if (list.get(id) == null) return;
        list.get(id).stop();
    }

    private Hashtable<Integer, SoundPlayer> list;

    public boolean isReady(int id) {
        return list.get(id) != null;
    }
}
