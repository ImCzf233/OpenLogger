package dev.ss.world.event.mixinevents;

import dev.ss.world.event.eventapi.events.*;
import net.minecraft.entity.*;

public class EventAttack implements Event
{
    public EntityLivingBase target;
    
    public EventAttack(EntityLivingBase ent) {
        ent = this.target;
    }
}
