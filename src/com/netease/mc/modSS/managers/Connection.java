package com.netease.mc.modSS.managers;

import com.netease.mc.modSS.utils.*;
import com.netease.mc.modSS.*;
import net.minecraft.network.*;
import io.netty.channel.*;
import net.minecraftforge.fml.relauncher.*;
import net.minecraft.entity.*;
import dev.ss.world.event.eventapi.*;
import dev.ss.world.event.eventapi.events.*;
import net.minecraft.network.play.client.*;
import dev.ss.world.event.mixinevents.*;
import java.lang.reflect.*;

public class Connection extends ChannelDuplexHandler
{
    private Events manager;
    
    public Connection(final Events events) {
        this.manager = events;
        try {
            final ChannelPipeline pipeline = Wrapper.INSTANCE.mc().getNetHandler().getNetworkManager().channel().pipeline();
            pipeline.addBefore("packet_handler", "ShellPacketHandler", (ChannelHandler)this);
        }
        catch (Exception exception) {
            Wrapper.error("Connection: Error on attaching");
            exception.printStackTrace();
        }
        ShellSock.getClient().modManager.getModulebyName("CommandGetter").setEnabled();
        ShellSock.getClient().modManager.getModulebyName("HUD").setEnabled();
    }
    
    public void channelRead(final ChannelHandlerContext ctx, final Object packet) throws Exception {
        if (!this.manager.onPacket((Packet)packet, Side.IN)) {
            return;
        }
        super.channelRead(ctx, packet);
    }
    
    public void write(final ChannelHandlerContext ctx, final Object packet, final ChannelPromise promise) throws Exception {
        if (packet instanceof C02PacketUseEntity) {
            final Field field = ReflectionHelper.findField((Class)C02PacketUseEntity.class, new String[] { "entityId", "field_149567_a" });
            try {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                final EventAttack attack = new EventAttack((EntityLivingBase)Wrapper.INSTANCE.world().getEntityByID(field.getInt(packet)));
                EventManager.call(attack);
            }
            catch (Exception e) {
                System.out.println(e);
            }
        }
        if (((Packet)packet) instanceof C03PacketPlayer && !ShellSock.mixin) {
            final C03PacketPlayer packetPlayer = (C03PacketPlayer)packet;
            final double x = (double)ReflectionHelper.getPrivateValue((Class)C03PacketPlayer.class, (Object)packetPlayer, new String[] { "x", "field_149479_a" });
            final double y = (double)ReflectionHelper.getPrivateValue((Class)C03PacketPlayer.class, (Object)packetPlayer, new String[] { "y", "field_149477_b" });
            final double z = (double)ReflectionHelper.getPrivateValue((Class)C03PacketPlayer.class, (Object)packetPlayer, new String[] { "z", "field_149478_c" });
            final float yaw = (float)ReflectionHelper.getPrivateValue((Class)C03PacketPlayer.class, (Object)packetPlayer, new String[] { "yaw", "field_149476_e" });
            final float pitch = (float)ReflectionHelper.getPrivateValue((Class)C03PacketPlayer.class, (Object)packetPlayer, new String[] { "pitch", "field_149473_f" });
            final boolean onGroud = (boolean)ReflectionHelper.getPrivateValue((Class)C03PacketPlayer.class, (Object)packetPlayer, new String[] { "onGround", "field_149474_g" });
            final EventPreMotion eventPlayerPre = new EventPreMotion(x, y, z, yaw, pitch, onGroud);
            EventManager.call(eventPlayerPre);
            if (ShellSock.getClient().settingsManager.getSettingByName(ShellSock.getClient().modManager.getModulebyName("ClientSettings"), "InjectionRotation").isEnabled()) {
                ReflectionHelper.setPrivateValue((Class)C03PacketPlayer.class, (Object)packetPlayer, (Object)eventPlayerPre.getX(), new String[] { "x", "field_149479_a" });
                ReflectionHelper.setPrivateValue((Class)C03PacketPlayer.class, (Object)packetPlayer, (Object)eventPlayerPre.getY(), new String[] { "y", "field_149477_b" });
                ReflectionHelper.setPrivateValue((Class)C03PacketPlayer.class, (Object)packetPlayer, (Object)eventPlayerPre.getZ(), new String[] { "z", "field_149478_c" });
                ReflectionHelper.setPrivateValue((Class)C03PacketPlayer.class, (Object)packetPlayer, (Object)eventPlayerPre.getYaw(), new String[] { "yaw", "field_149476_e" });
                ReflectionHelper.setPrivateValue((Class)C03PacketPlayer.class, (Object)packetPlayer, (Object)eventPlayerPre.getPitch(), new String[] { "pitch", "field_149473_f" });
            }
            final EventPostMotion post = new EventPostMotion(eventPlayerPre.getPitch());
            EventManager.call(post);
        }
        if (!this.manager.onPacket((Packet)packet, Side.OUT)) {
            return;
        }
        super.write(ctx, packet, promise);
    }
    
    public enum Side
    {
        IN, 
        OUT;
    }
}
