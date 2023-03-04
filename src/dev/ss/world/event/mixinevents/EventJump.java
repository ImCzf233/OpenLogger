package dev.ss.world.event.mixinevents;

import dev.ss.world.event.eventapi.events.callables.*;

public class EventJump extends EventCancellable
{
    public float yaw;
    
    public EventJump(final float yaw) {
        this.yaw = yaw;
    }
}
