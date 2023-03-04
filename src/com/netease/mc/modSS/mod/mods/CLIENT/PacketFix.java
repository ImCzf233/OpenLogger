package com.netease.mc.modSS.mod.mods.CLIENT;

import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.utils.*;

public class PacketFix extends Mod
{
    public PacketFix() {
        super("PacketFix", "Fix Hyt packets", Category.CLIENT);
    }
    
    @Override
    public void onEnable() {
        Wrapper.message("HYT PacketFix");
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
}
