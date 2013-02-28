package com.geargames.env;

import com.geargames.common.env.Environment;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Date: 19.02.13
 * Time: 18:20
 */
public class ConsoleEnvironment extends Environment {

    private static ConsoleEnvironment instance;

    private ConsoleEnvironment() {
    }

    public static ConsoleEnvironment getInstance() {
        if (instance == null) {
            instance = new ConsoleEnvironment();
        }
        return instance;
    }

    public long currentTimeMillis()
    {
        return System.currentTimeMillis();
    }

    public long nanoTime()
    {
        return System.nanoTime();
    }

    public void exit(int status) {
        System.exit(status);
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
