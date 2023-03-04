package com.netease.mc.modSS.mod.mods.PLAYER;

import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.mod.*;
import net.minecraft.network.*;
import com.netease.mc.modSS.managers.*;
import com.netease.mc.modSS.utils.*;

public class FastPlace extends Mod
{
    public Setting speed;
    
    public FastPlace() {
        super("FastPlace", "", Category.PLAYER);
        this.addSetting(this.speed = new Setting("Speed", this, 1.0, 0.0, 4.0, true));
        this.speed.setValue(1.0);
    }
    
    @Override
    public boolean onPacket(final Packet packet, final Connection.Side side) {
        Mappings.setRightClickDelayTimer((int)this.speed.getValue());
        return true;
    }
    
    @Override
    public void onEnable() {
        Mappings.setRightClickDelayTimer((int)this.speed.getValue());
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        Mappings.setRightClickDelayTimer(4);
        super.onDisable();
    }
}
