package com.geargames.common.env; // env -> environment, app

/**
 * User: abarakov
 * Date: 09.02.13 19:28
 */
public abstract class Environment {

    public abstract long currentTimeMillis();

    public abstract long nanoTime();

    public abstract void exit(int status);

    public abstract int availableProcessors();

    public abstract long freeMemory();

    public abstract long totalMemory();

    public abstract long maxMemory();

}
