package dev.ss.world.event.mixinevents;

import dev.ss.world.event.eventapi.events.callables.*;

public class EventNoSlowDown extends EventCancellable
{
    public float strafe;
    public float forward;
    
    public float getStrafe() {
        return this.strafe;
    }
    
    public void setStrafe(final float strafe) {
        this.strafe = strafe;
    }
    
    public float getForward() {
        return this.forward;
    }
    
    public void setForward(final float forward) {
        this.forward = forward;
    }
}
