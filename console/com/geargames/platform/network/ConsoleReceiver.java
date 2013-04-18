package com.geargames.platform.network;

import com.geargames.common.network.Network;
import com.geargames.common.network.Receiver;
import com.geargames.common.serialization.MicroByteBuffer;

/**
 * User: mkutuzov
 * Date: 22.03.13
 */
public class ConsoleReceiver extends Receiver {
    private Thread thread;
    private MicroByteBuffer answerBuffer;
    private boolean running;

    public ConsoleReceiver(Network network, MicroByteBuffer buffer) {
        super(network);
        answerBuffer = buffer;
        thread = new Thread(){
            @Override
            public void run() {
                running = true;
                cycle();
                running = false;
            }
        };
    }

    @Override
    protected void onStart() {
        thread.start();
    }

    @Override
    protected void onStop() {
    }

    @Override
    public int getErrorThreshold() {
        return 3;
    }

    @Override
    protected MicroByteBuffer getAnswersBuffer() {
        return answerBuffer;
    }

    public boolean isRunning() {
        return running;
    }
}
