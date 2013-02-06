package com.geargames.common.packer;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 07.09.12
 * Time: 14:21
 */

public class Particle {

    public Particle(int id, int type, int px, int py, int weight) {
        this.id = id;
        this.type = type;
        this.px = px;
        this.py = py;
        this.weight = weight;
    }

    public boolean isBody() {
        return isBody;
    }

    public void setIsBody(boolean body) {
        isBody = body;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPx() {
        return px;
    }

    public void setPx(int px) {
        this.px = px;
    }

    public int getPy() {
        return py;
    }

    public void setPy(int py) {
        this.py = py;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public EmitProcess getEmitter() {
        return emitProcess;
    }

    public void setEmitter(EmitProcess emitProcess) {
        this.emitProcess = emitProcess;
    }

    private boolean isBody;
    private int id;
    private int type;
    private int px;
    private int py;
    private int weight;
    private EmitProcess emitProcess;

}
