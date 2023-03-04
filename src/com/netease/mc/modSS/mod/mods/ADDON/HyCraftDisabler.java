package com.netease.mc.modSS.mod.mods.ADDON;

import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.utils.*;
import net.minecraftforge.fml.common.eventhandler.*;
import sun.reflect.*;

public class HyCraftDisabler extends Mod
{
    public HyCraftDisabler() {
        super("HyCraftDisabler", "", Category.CLIENT);
    }
    
    @Override
    public void onEnable() {
        Wrapper.modmsg(this, "Start Disabling Hycraft ScreenShot/ClickGUI-Crash");
        try {
            Reflection.registerFieldsToFilter(EventBus.class, "listeners");
        }
        catch (Exception ex) {}
        Wrapper.modmsg(this, "Disabled Hycraft ScreenShot/ClickGUI-Crash");
        super.onEnable();
    }
}
