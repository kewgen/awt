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

    public ConsoleReceiver(Network network, MicroByteBuffer buffer) {
        super(network);
        answerBuffer = buffer;
        thread = new Thread(){
            @Override
            public void run() {
                receiving();
            }
        };
    }

    @Override
    protected void stopReceiving() {
    }

    @Override
    protected void startReceiving() {
        thread.start();
    }

    @Override
    public int getErrorThreshold() {
        return 3;
    }

    @Override
    protected MicroByteBuffer getAnswersBuffer() {
        return answerBuffer;
    }

}
