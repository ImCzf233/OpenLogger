package com.netease.mc.modSS.mod.mods.PLAYER;

import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.mod.*;
import net.minecraft.block.material.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.client.entity.*;

public class Regen extends Mod
{
    public Setting VALUE_FAST_REGEN;
    public Setting VALUE_HEALTH;
    
    public Regen() {
        super("Regen", "", Category.PLAYER);
        this.VALUE_FAST_REGEN = new Setting("Speed", this, 0.0, 0.0, 100.0, true);
        this.VALUE_HEALTH = new Setting("Health", this, 1.0, 1.0, 10.0, false);
        this.addSetting(this.VALUE_FAST_REGEN);
        this.addSetting(this.VALUE_HEALTH);
    }
    
    public void updateFastRegen() {
        if (Regen.mc.thePlayer == null || Regen.mc.theWorld == null) {
            return;
        }
        final FoodStats foodStats = Regen.mc.thePlayer.getFoodStats();
        final EntityPlayerSP player = Regen.mc.thePlayer;
        if (Regen.mc.thePlayer.getHealth() < this.VALUE_HEALTH.getValue() * 2.0 && Regen.mc.thePlayer.getFoodStats().getFoodLevel() > 16 && !Regen.mc.thePlayer.isUsingItem() && Regen.mc.thePlayer.isCollidedVertically && Regen.mc.thePlayer.onGround && !Regen.mc.gameSettings.keyBindJump.isKeyDown() && !Regen.mc.thePlayer.isInsideOfMaterial(Material.lava) && !Regen.mc.thePlayer.isInWater()) {
            for (int i = 0; i < this.VALUE_FAST_REGEN.getValue(); ++i) {
                Regen.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer());
            }
        }
    }
}
