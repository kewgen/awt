package com.geargames.common.env;

/**
 * User: mkutuzov
 * Date: 09.03.13
 */
public class SystemEnvironment {
    private static SystemEnvironment instance;
    private Debug debug;
    private Environment environment;

    private SystemEnvironment() {
    }

    public static SystemEnvironment getInstance(){
        if(instance == null){
            instance = new SystemEnvironment();
        }
        return instance;
    }

    public void setDebug(Debug debug) {
        this.debug = debug;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Debug getDebug() {
        return debug;
    }

    public Environment getEnvironment() {
        return environment;
    }
}
