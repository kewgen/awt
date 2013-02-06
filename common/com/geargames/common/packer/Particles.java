package com.geargames.common.packer;

import com.geargames.common.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: kewgen
 * Date: 07.09.12
 * Time: 14:17
 * менеджер управления частичками
 */
public class Particles {

    public Particles(int size) {
        list = new ArrayList(size);
    }

    public void add(Particle particle) {
        list.add(particle);
    }

    public int size() {
        return list.size();
    }

    public Particle get(int i) {
        return (Particle) list.get(i);
    }


    public int getParticlesCount() {
        return particlesCount;
    }

    public void setParticlesCount(int particlesCount) {
        this.particlesCount = particlesCount;
    }

    private ArrayList list;

    private int particlesCount;//no body
}
