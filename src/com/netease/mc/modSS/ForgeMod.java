package com.netease.mc.modSS;

import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;

@Mod(modid = "shellsock", name = "ShellSock", version = "0.0.1", acceptableRemoteVersions = "*")
public class ForgeMod
{
    public static final String MODID = "shellsock";
    public static final String NAME = "ShellSock";
    public static final String VERSION = "0.0.1";
    
    @Mod.EventHandler
    private static void init(final FMLInitializationEvent E) {
        new ShellSock();
    }
}
