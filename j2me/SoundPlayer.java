package gg;

import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VolumeControl;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class SoundPlayer /*implements PlayerListener*/ {

    private VolumeControl vc;
    private Player player;

    private final boolean SOUND_LEVEL_REGULATION = true;
    private final boolean PORT_NO_SND_LISTENER = true;

    private String track_name;

    public SoundPlayer(String name, byte[] data, String type, byte cicle) {//type - audio/midi, audio/x-wav
        try {

            if (data != null) {
                ByteArrayInputStream is = new ByteArrayInputStream(data);
                player = Manager.createPlayer(is, type);
                is.close();
                player.realize();
            } else {
                InputStream is = getClass().getResourceAsStream("/".concat(name));
                player = Manager.createPlayer(is, type);
                player.realize();
            }
/*
            if (!PORT_NO_SND_LISTENER) {
                player.addPlayerListener(this);
            }
*/
            setLoopCount(cicle == 0 ? 127 : cicle);

            track_name = name;
            log("SoundPlayer.init_, name:" + track_name + ", state:" + getState() + ", cicle:" + cicle);
        } catch (Exception e) {
            log(e);
        }
    }

    public void play() {
        try {
            if (player == null) return;

            if (player.getState() == Player.PREFETCHED) {
                stop();
            }

            if (player.getState() != Player.STARTED) {
                player.prefetch();
                player.start();
            }
            log("SoundPlayer.play, name:" + track_name + ", state:" + getState());
        } catch (Exception e) {
            log(e);
        }
    }

    public void stop() {
        try {
            if (player == null) return;

            if (player.getState() == Player.STARTED) {
                player.setMediaTime(0);
                player.stop();
            }
            log("SoundPlayer.play, name:" + track_name + ", state:" + getState());
        } catch (Exception e) {
            log(e);
        }
    }

    int getState() {
        if (player == null) return -1;

        return player.getState();
    }

    public void close() {
        if (player == null) return;

        if (player.getState() != Player.CLOSED) {
            player.close();
        }
    }

    void setLoopCount(int loop) {
        try {
            if (player == null) return;

            player.setLoopCount(loop);
            log("SoundPlayer.setLoopCount, name:" + track_name + ", loop:" + loop);

        } catch (Exception e) {
            log(e);
        }
    }

    public void setVolume(int volume) {//0-5
        try {
            if (player == null) return;

            if (!SOUND_LEVEL_REGULATION) {
                if (volume == 0) {
                    stop();
                } else {
                    play();
                }
                return;
            }

            vc = (VolumeControl) player.getControl("VolumeControl");

            if (volume == 0) {
                stop();
            } else {
                vc.setLevel(volume * 20);
                if (player.getState() != Player.STARTED) {
                    play();
                }
                vc.setLevel(volume * 20);
            }

        } catch (Exception e) {
            log(e);
        }
    }

    private long sms_time = 0;

    public void playerUpdate(Player player, String event, Object eventData) {

        if (event.equals(PlayerListener.DEVICE_UNAVAILABLE)) {
            stop();
            sms_time = System.currentTimeMillis();
        }
        if (event.equals(PlayerListener.DEVICE_AVAILABLE)) play();

    }

    public String getTrackName() {
        return track_name;
    }

    private void log(String str) {
        Debug.trace(gg.microedition.String.valueOfC(str));
    }

    private void log(Exception e) {
        Debug.logEx(e);
    }
}
