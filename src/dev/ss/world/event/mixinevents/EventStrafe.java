package dev.ss.world.event.mixinevents;

import dev.ss.world.event.eventapi.events.callables.*;

public class EventStrafe extends EventCancellable
{
    public float forward;
    public float strafe;
    public float friction;
    public float yaw;
    
    public EventStrafe(final float forward, final float strafe, final float friction, final float yaw) {
        this.forward = forward;
        this.strafe = strafe;
        this.friction = friction;
        this.yaw = yaw;
    }
}
