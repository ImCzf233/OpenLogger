package com.netease.mc.modSS.utils;

import org.apache.logging.log4j.*;

public class LoggerUtils
{
    public static void info(final String string) {
        LogManager.getLogger("shellsock").info(string);
    }
}
