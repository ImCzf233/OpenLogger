package com.netease.mc.modSS.mod.mods.PLAYER;

import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.mod.*;
import net.minecraftforge.fml.common.gameevent.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.item.*;

public class FastEat extends Mod
{
    public Setting mode;
    public Setting speed;
    
    public FastEat() {
        super("FastEat", "", Category.PLAYER);
        this.mode = new Setting("Mode", this, "Normal", new String[] { "Normal", "Ghost" });
        this.speed = new Setting("Speed", this, 20.0, 1.0, 100.0, true);
        this.addSetting(this.mode);
        this.addSetting(this.speed);
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (FastEat.mc.thePlayer.isUsingItem()) {
            final Item usingItem = FastEat.mc.thePlayer.getItemInUse().getItem();
            if (usingItem instanceof ItemFood || usingItem instanceof ItemBucketMilk || usingItem instanceof ItemPotion) {
                for (int i = 0; i < (int)(this.speed.getValue() / 2.0); ++i) {
                    final String mode = this.mode.getMode();
                    switch (mode) {
                        case "Normal": {
                            Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer(true));
                            Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition(FastEat.mc.thePlayer.posX, FastEat.mc.thePlayer.posY, FastEat.mc.thePlayer.posZ, FastEat.mc.thePlayer.onGround));
                            break;
                        }
                        case "Ghost": {
                            Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(FastEat.mc.thePlayer.posX, FastEat.mc.thePlayer.posY + 1.0E-9, FastEat.mc.thePlayer.posZ, FastEat.mc.thePlayer.rotationYaw, FastEat.mc.thePlayer.rotationPitch, false));
                            break;
                        }
                    }
                }
            }
        }
        super.onClientTick(event);
    }
}
