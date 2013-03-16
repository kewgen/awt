package com.geargames.media;

import com.geargames.common.util.Recorder;
import com.geargames.common.String;
import com.geargames.common.env.SystemEnvironment;
import com.geargames.common.util.ArrayByte;

import java.util.Hashtable;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 30.08.12
 * Time: 17:33
 */
public class Pool {

    public Pool(int streamCount) {
        list = new Hashtable<Integer, SoundPlayer>(streamCount);
    }

    public void load(Object midlet, String path, int id) {
        ArrayByte arrayByte = null;
        try {
            arrayByte = Recorder.load(path.toString());
        } catch (Exception e) {

        }
        SoundPlayer player = new SoundPlayer(path.toString(), null, arrayByte, false);
        list.put(id, player);
    }

    public void play(int id, int volume) {
        if (list.get(id) == null) {
            SystemEnvironment.getInstance().getDebug().trace(String.valueOfC("Sound not found, id:").concat(id));
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
