package com.geargames.common.packer;

import com.geargames.common.Render;
import com.geargames.common.util.ArrayList;

import java.util.Random;

public abstract class EmitProcess {

    protected Random rand;

    private final static short ACC_MULT = 100;
    protected short EMIT_INTERVAL = 1000 / 8;

    private final static short SIN_DIVIDE = 10000;
    private static short[] sin_ = new short[]{
            0, 175, 349, 523, 698, 872, 1045, 1219, 1392, 1564,
            1736, 1908, 2079, 2250, 2419, 2588, 2756, 2924, 3090, 3256,
            3420, 3584, 3746, 3907, 4067, 4226, 4384, 4540, 4695, 4848,
            5000, 5150, 5299, 5446, 5592, 5736, 5878, 6018, 6157, 6293,
            6428, 6561, 6691, 6820, 6947, 7071, 7193, 7314, 7431, 7547,
            7660, 7771, 7880, 7986, 8090, 8192, 8290, 8387, 8480, 8572,
            8660, 8746, 8829, 8910, 8988, 9063, 9135, 9205, 9272, 9336,
            9397, 9455, 9511, 9563, 9613, 9659, 9703, 9744, 9781, 9816,
            9848, 9877, 9903, 9925, 9945, 9962, 9976, 9986, 9994, 9998,
            10000};
    private int vect_x, vect_y;

    protected int dx;
    protected int dy;

    private int x, y;
    public int radius;
    protected int x_curr;
    protected int y_curr;
    public int vel_x;
    public int vel_y; // contains in *EMIT_INTERVAL mode for better precision
    public int acc_x, acc_y, acc_line; // x,y contains in *ACC_MULT mode for better precision, acc_line uses with /100 divider
    protected long create_time;
    public int life_time, emit_time;
    public int emit_vector, emit_sector;
    public int emit_freq_base, emit_freq_inc, emit_next_time;
    public int emit_vel_base, emit_vel_inc; // contains in *EMIT_INTERVAL mode for better precision
    public int emit_life_base, emit_life_inc;
    public int emit_acc_x, emit_acc_y, emit_acc_line; // x,y contains in *ACC_MULT mode for better precision, acc_line uses with /100 divider
    public boolean emit_simultaneously;

    public boolean render_emitter_first;

    protected ArrayList particles;

    public boolean process(Render render, long current_time, long time_process_last, int map_dx, int map_dy, int map_ddx, int map_ddy) {

        int[] xy;
        int time_diff_create = (int) (time_process_last - create_time);
        int time_diff = (int) (current_time - time_process_last);
        if (create_time + life_time < current_time) {
            return false;
        }
        if (create_time + emit_time > time_process_last) {
            if (emit_next_time == 0) {
                emit_next_time = emit_freq_base + (emit_freq_inc > 0 ? (rand.nextInt() & 0xffff) % emit_freq_inc : 0);
            }
            int an_prev = -1;
            for (; create_time + emit_next_time < current_time && emit_next_time < emit_time; emit_next_time += emit_freq_base + (emit_freq_inc > 0 ? (rand.nextInt() & 0xffff) % emit_freq_inc : 0)) {
                PartTemp particleTemp = new PartTemp();
                particleTemp.setEmitTime((short) emit_next_time);
                particleTemp.setType((short) 0);
                if (pManager.getParticlesCount() > 1) {
                    int type_rnd = 0;
                    for (int j = 0; j < pManager.getParticlesCount(); j++) {
                        type_rnd += pManager.get(j).getWeight();
                    }
                    type_rnd = (rand.nextInt() & 0xffff) % type_rnd;
                    for (; particleTemp.getPType() < pManager.getParticlesCount(); particleTemp.setType((short) (particleTemp.getPType() + 1))) {
                        if (type_rnd < pManager.get(particleTemp.getPType()).getWeight()) {
                            break;
                        } else {
                            type_rnd -= pManager.get(particleTemp.getPType()).getWeight();//particle_weights[particleTemp[PARTICLE_TYPE]];
                        }
                    }
                }
                int an_curr = (rand.nextInt() & 0xffff) % emit_sector;
                if (emit_sector > 15 && an_prev >= 0) {
                    if (an_curr - an_prev >= 0 && an_curr - an_prev < emit_sector / 3) {
                        an_curr = (an_curr + emit_sector / 3) % emit_sector;
                    } else if (an_curr - an_prev < 0 && an_curr - an_prev >= -emit_sector / 3) {
                        an_curr = (an_curr + emit_sector * 2 / 3) % emit_sector;
                    }
                }
                an_prev = an_curr;
                int ld = acc_line * emit_next_time / EMIT_INTERVAL;
                if (ld > 100) {
                    ld = 100;
                }
                int vel_x_diff_2 = acc_x * emit_next_time / ACC_MULT;
                int vel_y_diff_2 = acc_y * emit_next_time / ACC_MULT;
                int an = (countAngle(vel_x * (100 - ld) / 100 + vel_x_diff_2, vel_y * (100 - ld) / 100 + vel_y_diff_2) + emit_vector + an_curr) % 360;
//                    Director.log("em t = " + (time_diff_create + emit_step * i) + "; vel = " + (vel_x * (100 - ld) / 100 + vel_x_diff_1 + vel_x_diff_2) + ", " + (vel_y * (100 - ld) / 100 + vel_y_diff_1 + vel_y_diff_2) + "; an = " + an + " (" + emit_vector + ", " + an_curr + ")");
                xy = calcShift(x, y, vel_x, vel_y, acc_x, acc_y, acc_line, emit_next_time);
                particleTemp.setX((short) (map_dx + map_ddx * (emit_next_time - time_diff_create) / EMIT_INTERVAL + xy[0]));
                particleTemp.setXCurr(particleTemp.getX());//[PARTICLE_X_CURR] = (short)(map_dx + map_ddx * (emit_next_time - time_diff_create) / EMIT_INTERVAL + xy[0]);
                particleTemp.setY((short) (map_dy + map_ddy * (emit_next_time - time_diff_create) / EMIT_INTERVAL + xy[1]));
                particleTemp.setYCurr(particleTemp.getY());
                if (radius != 0) {
                    int r = (radius > 0 ? (rand.nextInt() & 0xffff) % radius : -radius);
                    countVector(an, r, 1);
                    particleTemp.setX((short) (particleTemp.getX() + vect_x));
                    particleTemp.setY((short) (particleTemp.getY() + vect_y));
                    particleTemp.setXCurr(particleTemp.getX());
                    particleTemp.setYCurr(particleTemp.getY());
                }
                int vlen = emit_vel_base + (emit_vel_inc > 0 ? (rand.nextInt() & 0xffff) % emit_vel_inc : 0);
                countVector(an, vlen, 1);
                particleTemp.setVelX((short) vect_x);
                particleTemp.setVelY((short) vect_y);
                particleTemp.setLifeTime((short) (emit_life_base + (emit_life_inc > 0 ? (rand.nextInt() & 0xffff) % emit_life_inc : 0)));

                if (pManager.getParticlesCount() > 0 && pManager.get(particleTemp.getPType()).getType() == /*Render.T_EMITTER*/11) {
                    EmitProcess em = emitterAdd(render, pManager.get(particleTemp.getPType()).getId());
                    em.x = em.x_curr = particleTemp.getX() - (map_dx + map_ddx * (emit_next_time - time_diff_create) / EMIT_INTERVAL);
                    em.y = em.y_curr = particleTemp.getY() - (map_dy + map_ddy * (emit_next_time - time_diff_create) / EMIT_INTERVAL);
                    em.vel_x = particleTemp.getVelX();
                    em.vel_y = particleTemp.getVelY();
                    em.create_time = create_time + particleTemp.getEmitTime();
                    em.acc_x = emit_acc_x;
                    em.acc_y = emit_acc_y;
                    em.acc_line = emit_acc_line;
                    particleTemp.setEmitter(em);
                    particles.add(particleTemp);
                } else {
                    if (!pManager.get(particleTemp.getPType()).isBody()) particles.add(particleTemp);
                }
            }
        }
        xy = calcShift(x, y, vel_x, vel_y, acc_x, acc_y, acc_line, time_diff_create + time_diff);
        x_curr = xy[0];
        y_curr = xy[1];
        for (int i = 0; i < particles.size(); i++) {
            PartTemp particle = (PartTemp) particles.get(i);
            if (create_time + particle.getEmitTime() + particle.getLifeTime() <= current_time) {
                //particles.remove(particle);//очень тяжелая линейная операция
                particles.remove(i);
                i--;
                continue;
            }
            xy = calcShift(particle.getX(), particle.getY(), particle.getVelX(), particle.getVelY(), emit_acc_x, emit_acc_y, emit_acc_line, (int) (current_time - (create_time + particle.getEmitTime())));
            particle.setXCurr((short) xy[0]);
            particle.setYCurr((short) xy[1]);
        }
        return true;

    }

    private int[] calcShift(int x_, int y_, int vel_x_, int vel_y_, int acc_x_, int acc_y_, int acc_line_, int time) {

        int[] res = new int[2];
        res[0] = x_;
        res[1] = y_;
        if (acc_line_ > 0) {
            int line_diff = acc_line_ * time / EMIT_INTERVAL;
            if (line_diff > 100) {
                line_diff = 100;
                int time_1 = 100 * EMIT_INTERVAL / acc_line_;
                int vel_x_1 = acc_x_ * time_1 / ACC_MULT;
                int vel_y_1 = acc_y_ * time_1 / ACC_MULT;
                res[0] += (vel_x_ + vel_x_1) / 2 * time_1 / (EMIT_INTERVAL * EMIT_INTERVAL);
                res[1] += (vel_y_ + vel_y_1) / 2 * time_1 / (EMIT_INTERVAL * EMIT_INTERVAL);
                vel_x_ = vel_x_1;
                vel_y_ = vel_y_1;
                time -= time_1;
            } else {
                int vel_x_1 = vel_x_ * (100 - line_diff) / 100 + acc_x_ * time / ACC_MULT;
                int vel_y_1 = vel_y_ * (100 - line_diff) / 100 + acc_y_ * time / ACC_MULT;
                res[0] += (vel_x_ + vel_x_1) / 2 * time / (EMIT_INTERVAL * EMIT_INTERVAL);
                res[1] += (vel_y_ + vel_y_1) / 2 * time / (EMIT_INTERVAL * EMIT_INTERVAL);
                return res;
            }
        }
        int vel_x_1 = vel_x_ + acc_x_ * time / ACC_MULT;
        int vel_y_1 = vel_y_ + acc_y_ * time / ACC_MULT;
        res[0] += (vel_x_ + vel_x_1) / 2 * time / (EMIT_INTERVAL * EMIT_INTERVAL);
        res[1] += (vel_y_ + vel_y_1) / 2 * time / (EMIT_INTERVAL * EMIT_INTERVAL);
        return res;

    }

    private void countVector(int angle, int len, int mult) {

        int si = 0, co = 0;
        int an = angle % 90, qu = angle / 90;
        switch (qu) {
            case 0:
                si = EmitProcess.sin_[an];
                co = EmitProcess.sin_[90 - an];
                break;
            case 1:
                si = EmitProcess.sin_[90 - an];
                co = -EmitProcess.sin_[an];
                break;
            case 2:
                si = -EmitProcess.sin_[an];
                co = -EmitProcess.sin_[90 - an];
                break;
            case 3:
                si = -EmitProcess.sin_[90 - an];
                co = EmitProcess.sin_[an];
                break;
        }
        vect_x = len * si * mult / SIN_DIVIDE;
        vect_y = len * co * mult / SIN_DIVIDE;

    }

    private int countAngle(int x, int y) {

        if (x == 0 && y == 0) {
            return 0;
        }
        int sin2 = x * x * 100 / (x * x + y * y) * 1000000;
        int i = 0;
        for (; i < 90; i++) {
//            Director.log("ca 1 = " + (EmitProcess.sin_[i + 1] * EmitProcess.sin_[i + 1]) + ", " + sin2);
            if (EmitProcess.sin_[i + 1] * EmitProcess.sin_[i + 1] > sin2) {
                break;
            }
        }
        int res = i;
        if (x < 0 && y < 0) {
            res = 180 + i;
        } else if (x < 0) {
            res = 360 - i;
        } else if (y < 0) {
            res = 180 - i;
        }
//        Director.log("ca xy = " + x + ", " + y + "; res = " + res);
        return res;

    }

    public abstract EmitProcess emitterAdd(Render render, int a);

    public void setEmitterInterval(short value) {
        EMIT_INTERVAL = value;
    }

    public class PartTemp {

        private short emitTime;
        private short type;
        private short x;
        private short y;
        private short lifeTime;
        private short XCurr;
        private short YCurr;
        private short velX;
        private short velY;
        private EmitProcess emitProcess;

        public void setEmitTime(short emitTime) {
            this.emitTime = emitTime;
        }

        public void setType(short type) {
            this.type = type;
        }

        public short getPType() {
            return type;
        }

        public void setX(short x) {
            this.x = x;
        }

        public void setY(short y) {
            this.y = y;
        }

        public void setLifeTime(short lifeTime) {
            this.lifeTime = lifeTime;
        }

        public short getX() {
            return x;
        }

        public void setXCurr(short XCurr) {
            this.XCurr = XCurr;
        }

        public short getY() {
            return y;
        }

        public void setYCurr(short YCurr) {
            this.YCurr = YCurr;
        }

        public void setVelX(short velX) {
            this.velX = velX;
        }

        public void setVelY(short velY) {
            this.velY = velY;
        }

        public short getVelX() {
            return velX;
        }

        public short getVelY() {
            return velY;
        }

        public short getEmitTime() {
            return emitTime;
        }

        public short getXCurr() {
            return XCurr;
        }

        public short getYCurr() {
            return YCurr;
        }

        public short getLifeTime() {
            return lifeTime;
        }

        public void setEmitter(EmitProcess emitProcess) {
            this.emitProcess = emitProcess;
        }

        public EmitProcess getEmitter() {
            return emitProcess;
        }
    }

//    abstract void complete();
//    abstract EmitProcess create(int dx, int dy);

    protected Particles pManager;


}