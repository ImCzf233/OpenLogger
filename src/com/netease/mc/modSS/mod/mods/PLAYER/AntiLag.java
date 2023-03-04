package com.netease.mc.modSS.mod.mods.PLAYER;

import net.minecraft.network.*;
import java.util.concurrent.*;
import com.netease.mc.modSS.*;
import net.minecraft.util.*;
import com.netease.mc.modSS.mod.*;
import net.minecraftforge.fml.common.gameevent.*;
import java.util.*;
import com.netease.mc.modSS.managers.*;
import net.minecraft.network.play.client.*;
import net.minecraft.client.entity.*;

public class AntiLag extends Mod
{
    private List<Packet> packets;
    private boolean sending;
    private int delay;
    private int setBacks;
    private int ticksSinceLastSetBack;
    private boolean unstuck;
    
    public AntiLag() {
        super("AntiLag", "Automatically remove lag", Category.PLAYER);
        this.packets = new CopyOnWriteArrayList<Packet>();
    }
    
    @Override
    public void onEnable() {
        ShellSock.getClient().notificationManager.add(new Notification(EnumChatFormatting.RED + "May only apply to NCP", Notification.Type.Info));
        super.onEnable();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        ++this.delay;
        if (this.delay >= 1) {
            this.sending = true;
            this.sendPackets();
            this.delay = 0;
        }
        super.onClientTick(event);
    }
    
    public void sendPackets() {
        if (this.packets.size() > 0) {
            for (final Packet packet : this.packets) {
                if (packet instanceof C02PacketUseEntity || packet instanceof C08PacketPlayerBlockPlacement || packet instanceof C07PacketPlayerDigging) {
                    AntiLag.mc.thePlayer.swingItem();
                }
                AntiLag.mc.thePlayer.sendQueue.addToSendQueue(packet);
            }
        }
        this.packets.clear();
        this.sending = false;
    }
    
    @Override
    public void onDisable() {
        this.sending = true;
        this.sendPackets();
        super.onDisable();
    }
    
    @Override
    public boolean onPacket(final Packet packet, final Connection.Side side) {
        if (!this.sending) {
            if ((side == Connection.Side.OUT && packet instanceof C03PacketPlayer) || packet instanceof C08PacketPlayerBlockPlacement || packet instanceof C07PacketPlayerDigging || packet instanceof C02PacketUseEntity) {
                return false;
            }
            final boolean input = AntiLag.mc.gameSettings.keyBindForward.isPressed() || AntiLag.mc.gameSettings.keyBindBack.isPressed() || AntiLag.mc.gameSettings.keyBindRight.isPressed() || AntiLag.mc.gameSettings.keyBindLeft.isPressed();
            if (input && packet instanceof C03PacketPlayer) {
                this.packets.add(packet);
            }
            if (packet instanceof C02PacketUseEntity) {
                this.packets.add(packet);
                final EntityPlayerSP thePlayer = AntiLag.mc.thePlayer;
                thePlayer.rotationYaw -= 180.0f;
            }
        }
        return super.onPacket(packet, side);
    }
}
