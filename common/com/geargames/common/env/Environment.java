package com.geargames.common.env;

/**
 * User: abarakov
 * Date: 09.02.13 19:28
 */
public abstract class Environment {

    private static AbstractEnvironment instance;

    public static AbstractEnvironment getInstance() {
        return instance;
    }

    public static void setInstance(AbstractEnvironment instance) {
        Environment.instance = instance;
    }

    public static long currentTimeMillis() {
       return instance.currentTimeMillis();
    }

    public static long nanoTime() {
        return instance.nanoTime();
    }

    public static void exit(int status) {
        instance.exit(status);
    }

    public static int availableProcessors() {
        return instance.availableProcessors();
    }

    public static long freeMemory() {
        return instance.freeMemory();
    }

    public static long totalMemory() {
        return instance.totalMemory();
    }

    public static long maxMemory() {
        return instance.maxMemory();
    }

    public static void pause(int milliseconds){
        instance.pause(milliseconds);
    }

}
