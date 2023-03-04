package dev.ss.world.event.mixinevents;

import dev.ss.world.event.eventapi.events.callables.*;
import dev.ss.world.event.eventapi.types.*;
import net.minecraft.network.*;
import net.minecraft.network.play.server.*;
import dev.ss.world.event.eventapi.*;
import dev.ss.world.event.eventapi.events.*;

public class EventPacket extends EventCancellable
{
    private final EventType eventType;
    public Packet packet;
    private boolean outgoing;
    
    public EventPacket(final EventType eventType, final Packet packet) {
        this.eventType = eventType;
        this.packet = packet;
        this.outgoing = this.outgoing;
        if (this.packet instanceof S08PacketPlayerPosLook) {
            EventManager.call(new EventPullback());
        }
    }
    
    public EventType getEventType() {
        return this.eventType;
    }
    
    public Packet getPacket() {
        return this.packet;
    }
    
    public void setPacket(final Packet packet) {
        this.packet = packet;
    }
    
    public boolean isOutgoing() {
        return this.outgoing;
    }
    
    public boolean isIncoming() {
        return !this.outgoing;
    }
}
