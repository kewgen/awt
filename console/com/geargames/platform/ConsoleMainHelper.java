package com.geargames.platform;

import com.geargames.common.env.Environment;
import com.geargames.common.logging.Debug;
import com.geargames.common.logging.Logger;
import com.geargames.platform.env.ConsoleEnvironment;
import com.geargames.platform.logging.ConsoleDebug;
import com.geargames.platform.logging.ConsoleLogger;

/**
 * User: abarakov
 * Date: 15.03.13
 */
public class ConsoleMainHelper {

    public static void appInitialize() {
        Environment.setInstance(new ConsoleEnvironment());
        Logger.setInstance(new ConsoleLogger());
        Debug.setInstance(new ConsoleDebug());
    }

}