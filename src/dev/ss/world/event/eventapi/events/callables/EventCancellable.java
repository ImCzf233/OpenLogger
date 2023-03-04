package dev.ss.world.event.eventapi.events.callables;

import dev.ss.world.event.eventapi.events.*;

public abstract class EventCancellable implements Event, Cancellable
{
    public boolean cancelled;
    
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    @Override
    public void setCancelled(final boolean state) {
        this.cancelled = state;
    }
}
