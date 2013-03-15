package com.geargames.common.env;

/**
 * User: abarakov
 * Date: 09.02.13 19:28
 */
public abstract class Environment {

    public static long currentTimeMillis() {
       return AbstractEnvironment.getInstance().currentTimeMillis();
    }

    public static long nanoTime() {
        return AbstractEnvironment.getInstance().nanoTime();
    }

    public static void exit(int status) {
        AbstractEnvironment.getInstance().exit(status);
    }

    public static int availableProcessors() {
        return AbstractEnvironment.getInstance().availableProcessors();
    }

    public static long freeMemory() {
        return AbstractEnvironment.getInstance().freeMemory();
    }

    public static long totalMemory() {
        return AbstractEnvironment.getInstance().totalMemory();
    }

    public static long maxMemory() {
        return AbstractEnvironment.getInstance().maxMemory();
    }

}
