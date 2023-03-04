package com.netease.mc.modSS.mod.mods.MOVEMENT;

import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;

public class Phase extends Mod
{
    public Setting mode;
    
    public Phase() {
        super("Phase", "", Category.MOVEMENT);
        this.addSetting(this.mode = new Setting("Mode", this, "Packet1", new String[] { "Packet1", "Packet2", "AAC4" }));
    }
    
    @Override
    public void onEnable() {
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Packet1": {
                if (Wrapper.INSTANCE.player().isCollidedHorizontally) {
                    Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY - 1.0E-8, Wrapper.INSTANCE.player().posZ, Wrapper.INSTANCE.player().rotationYaw, Wrapper.INSTANCE.player().rotationPitch, false));
                    Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY - 1.0, Wrapper.INSTANCE.player().posZ, Wrapper.INSTANCE.player().rotationYaw, Wrapper.INSTANCE.player().rotationPitch, false));
                    this.toggle();
                    break;
                }
                break;
            }
            case "Packet2": {
                for (int i = 0; i < 10; ++i) {
                    Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.thePlayer.posX + 0.1, Phase.mc.thePlayer.posY, Phase.mc.thePlayer.posZ + 0.1, true));
                }
                this.toggle();
                break;
            }
            case "AAC4": {
                Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(Phase.mc.thePlayer.posX, Phase.mc.thePlayer.posY - 1.0E-8, Phase.mc.thePlayer.posZ, Phase.mc.thePlayer.rotationYaw, Phase.mc.thePlayer.rotationPitch, false));
                Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(Phase.mc.thePlayer.posX, Phase.mc.thePlayer.posY - 1.0, Phase.mc.thePlayer.posZ, Phase.mc.thePlayer.rotationYaw, Phase.mc.thePlayer.rotationPitch, false));
                this.toggle();
                break;
            }
        }
        super.onEnable();
    }
}
