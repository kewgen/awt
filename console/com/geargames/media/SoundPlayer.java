package com.geargames.media;

import com.geargames.Debug;
import com.geargames.common.media.Player;
import com.geargames.common.util.ArrayByte;
import javazoom.jl.player.JavaSoundAudioDevice;
import javazoom.jl.player.advanced.AdvancedPlayer;

import javax.sound.sampled.*;
import java.io.*;

public class SoundPlayer implements Runnable, Player {

    private Thread thread;
    private String filename;//if == null - данные в байтовом виде
    private ArrayByte data;
    private boolean isMP3;
    private int loopCount;
    private JavaSoundAudioDevice device;
    private AdvancedPlayer advancedPlayer;
    private boolean isPlayed;//проигрывается
    private boolean taskToStop;//получена команда на остановку проигрывания

    private Position curPosition;

    private final int EXTERNAL_BUFFER_SIZE = 524288; // 128Kb
    private int volume;

    enum Position {
        LEFT, RIGHT, NORMAL
    }

    public SoundPlayer(String file, InputStream is, ArrayByte data, boolean isMp3) {
        //if data == null поднимаем поток по пути файла
        filename = file;
        this.data = data;
        create(isMp3);
    }

    private void create(boolean isMp3) {
        this.isMP3 = isMp3;
        curPosition = Position.NORMAL;
        taskToStop = false;
        isPlayed = false;
        setLoopCount(1);
        Debug.trace("Sound created." + toString());
    }

    public void play() {
        //музыка уже играет
        if (isMP3 && device != null && device.isOpen()) return;

        thread = new Thread(this);
        thread.setName("SoundPlayer, " + filename);
        thread.start();
    }

    public void stop() {
        if (isMP3 && advancedPlayer != null) {
            if (isPlayed) taskToStop = true;
            setLoopCount(0);//останавливаем циклы
            device.close();
//            advancedPlayer.stop();
//            advancedPlayer.close();
        }
    }

    public boolean isReady() {
        return !isPlayed;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setLoopCount(int loopCount) {
        this.loopCount = loopCount;
    }

    public void run() {
        if (isMP3) {
            int loopCount_ = loopCount;
            while (loopCount_ > 0 && !taskToStop) {
                playMp3();
                loopCount_--;
            }
            taskToStop = false;
        } else {
            playWav();
        }
    }

    private void playWav() {

        AudioInputStream audioInputStream = null;
        try {
            if (data == null) {
                File soundFile = new File(filename);
                if (!soundFile.exists()) {
                    System.err.println("Wave file not found: " + filename);
                    return;
                }
                audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            } else {
                InputStream is = new DataInputStream(new ByteArrayInputStream(data.getArray()));
                audioInputStream = AudioSystem.getAudioInputStream(is);
                is.close();
            }
        } catch (UnsupportedAudioFileException e) {
            Debug.trace(toString());
            Debug.logEx(e);
            return;
        } catch (IOException e) {
            Debug.logEx(e);
            return;
        }

        AudioFormat format = audioInputStream.getFormat();
        SourceDataLine auline = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

        try {
            auline = (SourceDataLine) AudioSystem.getLine(info);
            auline.open(format);
        } catch (LineUnavailableException e) {
            Debug.logEx(e);
            return;
        } catch (IllegalArgumentException e) {
            Debug.logEx(e);
            Debug.trace(e.toString() + "\n" + toString());
            return;
        } catch (Exception e) {
            Debug.logEx(e);
            return;
        }

        isPlayed = true;
        if (auline.isControlSupported(FloatControl.Type.PAN)) {
            FloatControl pan = (FloatControl) auline.getControl(FloatControl.Type.PAN);
            if (curPosition == Position.RIGHT)
                pan.setValue(1.0f);
            else if (curPosition == Position.LEFT)
                pan.setValue(-1.0f);
        }

        auline.start();
        int nBytesRead = 0;
        byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];

        try {
            while (nBytesRead != -1) {
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
                if (nBytesRead >= 0)
                    auline.write(abData, 0, nBytesRead);
            }
            audioInputStream.close();
        } catch (IOException e) {
            Debug.logEx(e);
        } finally {
            auline.drain();
            auline.close();
            isPlayed = false;
        }

    }

    private void playMp3() {
        try {
            isPlayed = true;

            InputStream is;
            if (data == null) {
                is = new FileInputStream(filename);
            } else {
                is = new DataInputStream(new ByteArrayInputStream(data.getArray()));
            }
            device = new JavaSoundAudioDevice();
            advancedPlayer = new AdvancedPlayer(is, device);

            advancedPlayer.play();

            is.close();

        } catch (Exception e) {
            Debug.logEx(e);
        }
            isPlayed = false;
    }

    @Override
    public String toString() {
        return "SoundPlayer{" +
                "filename='" + filename + '\'' +
                ", isMP3=" + isMP3 +
                ", loopCount=" + loopCount +
                ", isPlayed=" + isPlayed +
                '}';
    }
}
