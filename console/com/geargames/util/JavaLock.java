package com.geargames.util;

import com.geargames.common.util.Lock;

/**
 * @author Mikhail_Kutuzov
 *         created: 23.05.12  11:33
 *         Класс семафор для java me.
 */
public class JavaLock extends Lock {
    private boolean locked;

    public JavaLock() {
        locked = false;
    }

    public synchronized void lock() {
        while (true) {
            if (!locked) {
                locked = true;
                break;
            } else {
                try {
                    wait();
                    continue;
                } catch (InterruptedException e) {
                    //TODO
                }
            }
        }
    }

    public synchronized void release() {
        locked = false;
        notifyAll();
    }
}
