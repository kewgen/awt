package com.geargames.packer;

import com.geargames.common.Render;
import com.geargames.common.env.SystemEnvironment;
import com.geargames.common.packer.Particle;
import com.geargames.common.packer.Particles;
import com.geargames.common.util.ArrayList;

import java.util.Random;

/**/
public class Emitter extends com.geargames.common.packer.EmitProcess {

    public Emitter create(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
        return new Emitter();
    }

    public Emitter() {
        pManager = new Particles(10);
    }

    public void addParticle(int layer, int id, int type, int px, int py, int slot) {
        Particle particle = new Particle(id, type, px, py, slot);
        particle.setIsBody(layer == 0);//это тело эмиттера
        pManager.add(particle);
    }

    public void complete() {//запись параметров завершена, инициализируем эмиттер
        create_time = System.currentTimeMillis();
        pManager.setParticlesCount(0);
        //for (Enumeration e = pManager.elements(); e.hasMoreElements(); ) {
        for (int i = 0; i < pManager.size(); i++) {
            Particle particle = pManager.get(i);
            if (!particle.isBody()) {
                pManager.setParticlesCount(pManager.getParticlesCount() + 1);
            } else {
                render_id = particle.getId();
                render_type = particle.getType();
                emitter_render_dx = (short) particle.getPx();
                emitter_render_dy = (short) particle.getPy();
            }
        }

        particles = new ArrayList();
        rand = new Random();
    }


    public void draw(com.geargames.common.Graphics g, int director_x, int director_y) {
        draw((Graphics) g, director_x, director_y, System.currentTimeMillis());
    }

    public void draw(Graphics g, int director_x, int director_y, long current_time) {
        if (render_emitter_first) {
            drawBody(g, director_x, director_y, current_time);
            drawParticles(g, director_x, director_y, current_time);
        } else {
            drawParticles(g, director_x, director_y, current_time);
            drawBody(g, director_x, director_y, current_time);
        }
    }

    private void drawBody(Graphics g, int director_x, int director_y, long current_time) {
        int tick = (int) (current_time - create_time) / EMIT_INTERVAL;
        drawCommon(g, render_id, render_type, director_x + x_curr + emitter_render_dx, director_y + y_curr + emitter_render_dy, tick);
    }

    private void drawParticles(Graphics g, int director_x, int director_y, long current_time) {
        for (int i = 0; i < particles.size(); i++) {
            PartTemp partTemp = (PartTemp) particles.get(i);
            int type = partTemp.getPType();
            int tick = (int) (current_time - (create_time + partTemp.getEmitTime())) / EMIT_INTERVAL;
//            if (partTemp.getEmitter() != null) {
//                    ((Emitter) partTemp.getEmitter()).draw(g, director_x, director_y, current_time);
//            } else {
            drawCommon(g, pManager.get(type).getId(), pManager.get(type).getType(), director_x + partTemp.getXCurr() + pManager.get(type).getPx()
                    , director_y + partTemp.getYCurr() + pManager.get(type).getPy(), tick);
//            }
        }
    }

    private void drawCommon(Graphics g, int pid, int type, int px, int py, int tick) {
        switch (type) {
            case com.geargames.common.Render.T_FRAME:
                g.getRender().getFrame(pid).draw(g, px, py);
                break;
            case com.geargames.common.Render.T_SPRITE:
                g.getRender().getSprite(pid).draw(g,px, py);
                break;
            case com.geargames.common.Render.T_ANIMATION:
                g.getRender().getAnimation(pid).draw(g, px, py, tick);
                break;
            default:
                SystemEnvironment.getInstance().getDebug().trace(com.geargames.common.String.valueOfC("Packer element type not found:").concat(type));
        }
    }

    public boolean process(Render render) {
        return super.process(render, System.currentTimeMillis(), System.currentTimeMillis() - EMIT_INTERVAL, 0, 0, 0, 0);
    }

    public Emitter emitterAdd(Render render, int pos) {
        return (Emitter) render.createEmitter(pos, 0, 0);
    }

    public void setCreateTime(long create_time) {
        this.create_time = create_time;
    }

    private int render_type, render_id;
    private short emitter_render_dx, emitter_render_dy;


    public String toString() {
        return "Emitter{" +
                "render_type=" + render_type +
                ", render_id=" + render_id +
                ", pManager=" + pManager +
                '}';
    }

    public void setDX(int dx) {
        this.dx = dx;
    }

    public void setDY(int dy) {
        this.dy = dy;
    }

    public int getDX() {
        return dx;
    }

    public int getDY() {
        return dy;
    }
}
