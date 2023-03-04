package dev.ss.world.event.mixinevents;

import dev.ss.world.event.eventapi.events.*;
import dev.ss.world.event.eventapi.types.*;

public class EventMotion implements Event, Cancellable
{
    public static double y;
    public static float yaw;
    public static float pitch;
    public static boolean onGround;
    public boolean cancel;
    public EventType type;
    
    public boolean isCancel() {
        return this.cancel;
    }
    
    public EventMotion(final double y, final float yaw, final float pitch) {
        EventMotion.y = y;
        EventMotion.yaw = yaw;
        EventMotion.pitch = pitch;
        this.type = EventType.PRE;
    }
    
    public EventMotion(final double y, final float yaw, final float pitch, final boolean onGround) {
        EventMotion.y = y;
        EventMotion.yaw = yaw;
        EventMotion.pitch = pitch;
        EventMotion.onGround = onGround;
        this.type = EventType.PRE;
    }
    
    public EventType getEventType() {
        return this.type;
    }
    
    public EventMotion(final EventType type) {
        this.type = type;
    }
    
    public boolean isPre() {
        return this.type == EventType.PRE;
    }
    
    @Override
    public boolean isCancelled() {
        return this.cancel;
    }
    
    @Override
    public void setCancelled(final boolean cancel) {
        this.cancel = cancel;
    }
    
    public double getY() {
        return EventMotion.y;
    }
    
    public void setY(final double y) {
        EventMotion.y = y;
    }
    
    public float getYaw() {
        return EventMotion.yaw;
    }
    
    public void setYaw(final float yaw) {
        EventMotion.yaw = yaw;
    }
    
    public float getPitch() {
        return EventMotion.pitch;
    }
    
    public void setPitch(final float pitch) {
        EventMotion.pitch = pitch;
    }
    
    public boolean isOnGround() {
        return EventMotion.onGround;
    }
    
    public void setOnGround(final boolean onGround) {
        EventMotion.onGround = onGround;
    }
}
