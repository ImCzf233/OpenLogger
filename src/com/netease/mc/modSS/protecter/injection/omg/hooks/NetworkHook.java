package com.netease.mc.modSS.protecter.injection.omg.hooks;

import com.netease.mc.modSS.utils.*;
import net.minecraft.network.*;
import dev.ss.world.event.eventapi.types.*;
import dev.ss.world.event.mixinevents.*;
import dev.ss.world.event.eventapi.*;
import dev.ss.world.event.eventapi.events.*;
import io.netty.channel.*;

public class NetworkHook extends ChannelDuplexHandler
{
    public NetworkHook() {
        try {
            final ChannelPipeline pipeline = Wrapper.INSTANCE.mc().getNetHandler().getNetworkManager().channel().pipeline();
            pipeline.addBefore("packet_handler", "PacketEventHandler", (ChannelHandler)this);
        }
        catch (Exception exception) {
            Wrapper.error("Connection: Error on attaching");
            exception.printStackTrace();
        }
    }
    
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        if (msg instanceof Packet) {
            final Packet packet = (Packet)msg;
            final EventPacket event = new EventPacket(EventType.RECIEVE, packet);
            EventManager.call(event);
            if (!event.isCancelled()) {
                super.channelRead(ctx, (Object)event.getPacket());
            }
        }
    }
    
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        if (msg instanceof Packet) {
            final Packet packet = (Packet)msg;
            final EventPacket event = new EventPacket(EventType.SEND, packet);
            EventManager.call(event);
            if (!event.isCancelled()) {
                super.write(ctx, msg, promise);
            }
        }
    }
}
