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

    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    @Override
    public long nanoTime() {
        return System.nanoTime();
    }

    @Override
    public void exit(int status) {
        Runtime.getRuntime().exit(status);
    }

    @Override
    public int availableProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }

    @Override
    public long freeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    @Override
    public long totalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    @Override
    public long maxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

}
