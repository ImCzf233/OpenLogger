package dev.ss.world.event.mixinevents;

import dev.ss.world.event.eventapi.events.*;

public class EventPostMotion implements Event
{
    public EventPostMotion(final float pitch) {
        EventPreMotion.RPPITCH = EventPreMotion.RPITCH;
        EventPreMotion.RPITCH = pitch;
    }
}
