package com.geargames.common.packer;

import com.geargames.common.Graphics;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 19.09.12
 * Time: 10:52
 */
public class PEmitter extends PrototypeIndexes {

    public PEmitter(int size) {
        super(size);
        type = com.geargames.common.Render.T_EMITTER;
    }

    public void draw(Graphics graphics, int x, int y) {
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public boolean isEmitSimultaneously() {
        return emitSimultaneously;
    }

    public void setEmitSimultaneously(boolean emitSimultaneously) {
        this.emitSimultaneously = emitSimultaneously;
    }

    public int getVelX() {
        return velX;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public int getVelY() {
        return velY;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    public int getAccX() {
        return accX;
    }

    public void setAccX(int accX) {
        this.accX = accX;
    }

    public int getAccY() {
        return accY;
    }

    public void setAccY(int accY) {
        this.accY = accY;
    }

    public int getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
    }

    public int getEmitTime() {
        return emitTime;
    }

    public void setEmitTime(int emitTime) {
        this.emitTime = emitTime;
    }

    public int getEmitVector() {
        return emitVector;
    }

    public void setEmitVector(int emitVector) {
        this.emitVector = emitVector;
    }

    public int getEmitSector() {
        return emitSector;
    }

    public void setEmitSector(int emitSector) {
        this.emitSector = emitSector;
    }

    public int getEmitFreqBase() {
        return emitFreqBase;
    }

    public void setEmitFreqBase(int emitFreqBase) {
        this.emitFreqBase = emitFreqBase;
    }

    public int getEmitFreqInc() {
        return emitFreqInc;
    }

    public void setEmitFreqInc(int emitFreqInc) {
        this.emitFreqInc = emitFreqInc;
    }

    public int getEmitVelBase() {
        return emitVelBase;
    }

    public void setEmitVelBase(int emitVelBase) {
        this.emitVelBase = emitVelBase;
    }

    public int getEmitVelInc() {
        return emitVelInc;
    }

    public void setEmitVelInc(int emitVelInc) {
        this.emitVelInc = emitVelInc;
    }

    public int getEmitLifeBase() {
        return emitLifeBase;
    }

    public void setEmitLifeBase(int emitLifeBase) {
        this.emitLifeBase = emitLifeBase;
    }

    public int getEmitLifeInc() {
        return emitLifeInc;
    }

    public void setEmitLifeInc(int emitLifeInc) {
        this.emitLifeInc = emitLifeInc;
    }

    public int getEmitAccX() {
        return emitAccX;
    }

    public void setEmitAccX(int emitAccX) {
        this.emitAccX = emitAccX;
    }

    public int getEmitAccY() {
        return emitAccY;
    }

    public void setEmitAccY(int emitAccY) {
        this.emitAccY = emitAccY;
    }

    public int getEmitAccLine() {
        return emitAccLine;
    }

    public void setEmitAccLine(int emitAccLine) {
        this.emitAccLine = emitAccLine;
    }

    public boolean isRenderEmitterFirst() {
        return renderEmitterFirst;
    }

    public void setRenderEmitterFirst(boolean renderEmitterFirst) {
        this.renderEmitterFirst = renderEmitterFirst;
    }

    private int radius;
    private boolean emitSimultaneously;
    private int velX;
    private int velY;
    private int accX;
    private int accY;
    private int lifeTime;
    private int emitTime;
    private int emitVector;
    private int emitSector;
    private int emitFreqBase;
    private int emitFreqInc;
    private int emitVelBase;
    private int emitVelInc;
    private int emitLifeBase;
    private int emitLifeInc;
    private int emitAccX;
    private int emitAccY;
    private int emitAccLine;
    private boolean renderEmitterFirst;

}
