package com.geargames.common.env; // env -> environment, app

/**
 * User: abarakov
 * Date: 09.02.13 19:28
 */

// Posible names: System, Runtime
public class Environment {

    public static long currentTimeMillis()
    {
        return System.currentTimeMillis();
    }

    public static long nanoTime()
    {
        return System.nanoTime();
    }

    public static void exit(int status) {
        System.exit(status);
    }

    public static void gc() {
        System.gc();
    }

    public int availableProcessors()
    {
        return Runtime.getRuntime().availableProcessors();
    }

    public long freeMemory()
    {
        return Runtime.getRuntime().freeMemory();
    }

    public long totalMemory()
    {
        return Runtime.getRuntime().totalMemory();
    }

    public long maxMemory()
    {
        return Runtime.getRuntime().maxMemory();
    }
}
