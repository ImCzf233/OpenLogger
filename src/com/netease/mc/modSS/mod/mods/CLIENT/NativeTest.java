package com.netease.mc.modSS.mod.mods.CLIENT;

import com.netease.mc.modSS.mod.*;
import net.minecraftforge.fml.common.gameevent.*;
import com.netease.mc.modSS.*;
import com.netease.mc.modSS.protecter.*;

public class NativeTest extends Mod
{
    public static boolean loadedNA;
    
    public NativeTest() {
        super("CAFE1001", "", Category.CLIENT);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (!ShellSock.mixin && !NativeTest.loadedNA && NativeTest.mc.thePlayer != null) {
            ShellSock.getClient().modifyLauncher = new ModifyLauncher();
            NativeTest.loadedNA = true;
        }
    }
    
    static {
        NativeTest.loadedNA = false;
    }
}
