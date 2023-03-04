package com.netease.mc.modSS.mod.mods.CLIENT;

import com.netease.mc.modSS.mod.*;
import net.minecraftforge.fml.common.gameevent.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.network.*;
import com.netease.mc.modSS.managers.*;
import net.minecraft.network.play.client.*;
import com.netease.mc.modSS.*;
import net.minecraftforge.fml.relauncher.*;
import java.lang.reflect.*;

public class CommandGetter extends Mod
{
    public CommandGetter() {
        super("CommandGetter", "", Category.CLIENT);
        this.setVisible(false);
        this.setEnabled();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (Utils.nullCheck()) {
            return;
        }
        super.onClientTick(event);
    }
    
    @Override
    public boolean onPacket(final Packet packet, final Connection.Side side) {
        boolean send = true;
        if (side == Connection.Side.OUT && packet instanceof C01PacketChatMessage && !ShellSock.mixin) {
            final Field field = ReflectionHelper.findField((Class)C01PacketChatMessage.class, new String[] { "message", "field_149440_a" });
            try {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                if (packet instanceof C01PacketChatMessage) {
                    final C01PacketChatMessage p = (C01PacketChatMessage)packet;
                    if (p.getMessage().subSequence(0, 1).equals(".")) {
                        send = false;
                        ShellSock.getClient().commandManager.execute(p.getMessage());
                        return send;
                    }
                    send = true;
                }
            }
            catch (Exception ex) {}
        }
        return send;
    }
}
