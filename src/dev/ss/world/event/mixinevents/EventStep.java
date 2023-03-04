package dev.ss.world.event.mixinevents;

import dev.ss.world.event.eventapi.events.*;
import dev.ss.world.event.eventapi.types.*;

public class EventStep implements Event
{
    private float height;
    private final EventType eventType;
    
    public EventStep(final EventType eventType, final float height) {
        this.eventType = eventType;
        this.height = height;
    }
    
    public float getHeight() {
        return this.height;
    }
    
    public void setHeight(final float height) {
        this.height = height;
    }
    
    public EventType getEventType() {
        return this.eventType;
    }
}
