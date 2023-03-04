package com.netease.mc.modSS.mod.mods.MOVEMENT;

import com.netease.mc.modSS.mod.*;
import net.minecraft.network.*;
import com.netease.mc.modSS.managers.*;
import net.minecraft.network.play.client.*;
import net.minecraftforge.fml.common.gameevent.*;

public class KeepSprint extends Mod
{
    public KeepSprint() {
        super("KeepSprint", "keep you sprinting", Category.MOVEMENT);
    }
    
    @Override
    public boolean onPacket(final Packet packet, final Connection.Side side) {
        if (side == Connection.Side.OUT && packet instanceof C0BPacketEntityAction) {
            final C0BPacketEntityAction pac = (C0BPacketEntityAction)packet;
            if (pac.getAction() == C0BPacketEntityAction.Action.STOP_SPRINTING) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (!KeepSprint.mc.thePlayer.isSprinting()) {
            KeepSprint.mc.thePlayer.setSprinting(true);
        }
    }
}
