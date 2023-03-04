package com.netease.mc.modSS.utils.player;

import net.minecraft.util.*;
import net.minecraft.client.settings.*;

public class NoSlowUtils extends MovementInput
{
    private GameSettings gameSettings;
    boolean NoSlowBoolean;
    
    public NoSlowUtils(final GameSettings par1GameSettings) {
        this.NoSlowBoolean = true;
        this.gameSettings = par1GameSettings;
    }
    
    public void updatePlayerMoveState() {
        super.moveStrafe = 0.0f;
        super.moveForward = 0.0f;
        if (this.gameSettings.keyBindForward.isKeyDown()) {
            ++super.moveForward;
        }
        if (this.gameSettings.keyBindBack.isKeyDown()) {
            --super.moveForward;
        }
        if (this.gameSettings.keyBindLeft.isKeyDown()) {
            ++super.moveStrafe;
        }
        if (this.gameSettings.keyBindRight.isKeyDown()) {
            --super.moveStrafe;
        }
        super.jump = this.gameSettings.keyBindJump.isKeyDown();
        super.sneak = this.gameSettings.keyBindSneak.isKeyDown();
        if (super.sneak) {
            super.moveStrafe *= 0.3;
            super.moveForward *= 0.3;
        }
        if (this.NoSlowBoolean) {
            super.moveStrafe *= 5.0f;
            super.moveForward *= 5.0f;
        }
    }
    
    public void setNSD(final boolean a) {
        this.NoSlowBoolean = a;
    }
}
