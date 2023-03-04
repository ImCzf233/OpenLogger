package dev.ss.world.event.mixinevents;

import dev.ss.world.event.eventapi.events.callables.*;

public class EventMoveButton extends EventCancellable
{
    private boolean left;
    private boolean right;
    private boolean backward;
    private boolean forward;
    private boolean sneak;
    private boolean jump;
    
    public void setForward(final boolean forward) {
        this.forward = forward;
    }
    
    public void setBackward(final boolean backward) {
        this.backward = backward;
    }
    
    public void setLeft(final boolean left) {
        this.left = left;
    }
    
    public void setRight(final boolean right) {
        this.right = right;
    }
    
    public void setJump(final boolean jump) {
        this.jump = jump;
    }
    
    public void setSneak(final boolean sneak) {
        this.sneak = sneak;
    }
    
    public boolean isForward() {
        return this.forward;
    }
    
    public boolean isBackward() {
        return this.backward;
    }
    
    public boolean isLeft() {
        return this.left;
    }
    
    public boolean isRight() {
        return this.left;
    }
    
    public boolean isJump() {
        return this.jump;
    }
    
    public boolean isSneak() {
        return this.sneak;
    }
}
