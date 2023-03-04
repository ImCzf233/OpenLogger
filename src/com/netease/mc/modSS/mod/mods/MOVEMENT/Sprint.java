package com.netease.mc.modSS.mod.mods.MOVEMENT;

import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.*;
import com.netease.mc.modSS.setting.*;
import net.minecraftforge.fml.common.gameevent.*;

public class Sprint extends Mod
{
    public Sprint() {
        super("Sprint", "Automatically sprints when W is pressed", Category.MOVEMENT);
        ShellSock.getClient().settingsManager.addSetting(new Setting("Mode", this, "Test1", new String[] { "Test1", "Test2" }));
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if ((Sprint.mc.thePlayer.moveForward != 0.0f || Sprint.mc.thePlayer.moveStrafing != 0.0f) && Sprint.mc.thePlayer.getFoodStats().getFoodLevel() > 6 && !Sprint.mc.thePlayer.isSneaking()) {
            Sprint.mc.thePlayer.setSprinting(true);
        }
        if (Sprint.settingsManager.getSettingByName(this, "Mode").isMode("Test1")) {}
        super.onClientTick(event);
    }
}
