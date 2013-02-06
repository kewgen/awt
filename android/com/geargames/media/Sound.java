package com.geargames.media;

import com.geargames.common.String;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 30.08.12
 * Time: 14:22
 */
public class Sound {

    Sound(String path) {
        this.path = path;
    }

    com.geargames.common.String getPath() {
        return path;
    }

    int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    @Override
    public java.lang.String toString() {
        return "Sound{" +
                "path=" + path +
                ", id=" + id +
                '}';
    }

    private String path;
    private int id;
}
