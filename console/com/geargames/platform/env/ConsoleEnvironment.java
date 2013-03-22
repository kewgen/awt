package com.geargames.platform.env;

import com.geargames.common.*;
import com.geargames.common.env.AbstractEnvironment;
import com.geargames.common.logging.Debug;

/**
 * Users: mikhail v. kutuzov, abarakov
 * Date: 19.02.13 18:20
 */
public class ConsoleEnvironment extends AbstractEnvironment {

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

    @Override
    public void pause(int milliseconds) {
        try{
            Thread.sleep(milliseconds);
        }catch (Exception e){
            Debug.error(com.geargames.common.String.valueOfC("Pausing has been interrupted"), e);
        }
    }
}
