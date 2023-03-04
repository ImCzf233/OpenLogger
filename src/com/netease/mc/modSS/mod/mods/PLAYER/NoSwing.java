package com.netease.mc.modSS.mod.mods.PLAYER;

import com.netease.mc.modSS.mod.*;
import net.minecraft.network.*;
import com.netease.mc.modSS.managers.*;
import net.minecraft.network.play.client.*;

public class NoSwing extends Mod
{
    public NoSwing() {
        super("NoSwing", "delete the animation packet", Category.PLAYER);
    }
    
    @Override
    public boolean onPacket(final Packet packet, final Connection.Side side) {
        return side != Connection.Side.OUT || !(packet instanceof C0APacketAnimation);
    }
}
