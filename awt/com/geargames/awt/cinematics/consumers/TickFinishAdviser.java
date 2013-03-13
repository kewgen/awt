package com.geargames.awt.cinematics.consumers;

import com.geargames.awt.cinematics.FinishAdviser;

/**
 * @author Mikhail_Kutuzov
 *         created: 16.05.12  22:35
 */
public class TickFinishAdviser extends FinishAdviser {
    private int tick;

    public TickFinishAdviser(int tick) {
        this.tick = tick;
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    @Override
    public void onTick() {
        if (tick != -1) {
            tick--;
        }
    }

    @Override
    public boolean isFinished() {
        return tick == -1;
    }
}
