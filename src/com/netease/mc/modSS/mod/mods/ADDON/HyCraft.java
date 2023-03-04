package com.netease.mc.modSS.mod.mods.ADDON;

import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.utils.*;

public class HyCraft extends Mod
{
    public HyCraft() {
        super("HyCraftAddon", "", Category.CLIENT);
    }
    
    @Override
    public void onEnable() {
        Wrapper.modmsg(this, "Loaded Hycraft Addon");
        super.onEnable();
    }
}
