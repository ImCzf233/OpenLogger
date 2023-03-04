package dev.ss.world.event.mixinevents;

import dev.ss.world.event.eventapi.events.*;

public class Event3D implements Event
{
    public float partialTicks;
    
    public Event3D(final float partialTicks) {
        this.partialTicks = partialTicks;
    }
}
