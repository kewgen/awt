package com.geargames.platform.network;

import com.geargames.common.network.Network;
import com.geargames.common.network.Sender;
import com.geargames.common.util.Lock;
import com.geargames.platform.util.JavaLock;

/**
 * User: mkutuzov
 * Date: 22.03.13
 */
public class ConsoleSender extends Sender {
    private Thread thread;
    private JavaLock lock;

    public ConsoleSender(Network network) {
        super(network);
        lock = new JavaLock();
        thread = new Thread(){
            @Override
            public void run() {
                sending();
            }
        };
    }

    @Override
    protected void startSending() {
        thread.start();
    }

    @Override
    protected void stopSending() {
    }

    @Override
    protected int getErrorThreshold() {
        return 3;
    }

    @Override
    protected Lock getWorkLock() {
        return lock;
    }
}
