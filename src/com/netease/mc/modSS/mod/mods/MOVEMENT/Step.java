package com.netease.mc.modSS.mod.mods.MOVEMENT;

import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.setting.*;
import dev.ss.world.event.mixinevents.*;
import com.netease.mc.modSS.utils.player.*;
import java.util.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.block.*;

@Info(name = "Step", description = "", category = Category.MOVEMENT)
public class Step extends Mod
{
    public Setting mode;
    public Setting height;
    public Setting timer;
    int stepTicks;
    private float stepHeight;
    private double posX;
    private double posY;
    private double posZ;
    private static int ticksOnGround;
    private static int ticksSinceLastStep;
    
    public Step() {
        super("Step", "", Category.MOVEMENT);
        this.mode = new Setting("Setting", this, "NCP", new String[] { "Vanilla", "Normal", "Low", "Delay", "Low2", "Low3", "Timer", "Matrix", "Hypixel", "NCP" });
        this.height = new Setting("Height", this, 1.5, 1.0, 10.0, false);
        this.timer = new Setting("Timer", this, 0.5, 0.1, 10.0, false);
        this.addSetting(this.mode);
        this.addSetting(this.height);
        this.addSetting(this.timer);
    }
    
    @Override
    public void onEnable() {
        this.stepTicks = (Step.ticksOnGround = (Step.ticksSinceLastStep = 0));
    }
    
    @Override
    public void onDisable() {
        Step.mc.thePlayer.stepHeight = 0.6f;
    }
    
    @Override
    public void onPreMotion(final EventPreMotion event) {
        ++this.stepTicks;
        final String modeString = this.mode.getMode();
        final double direction2 = MoveUtils.getDirection();
        final double x = -Math.sin(direction2) * 0.9;
        final double z = Math.cos(direction2) * 0.9;
        ++Step.ticksSinceLastStep;
        if (Step.mc.thePlayer.onGround) {
            ++Step.ticksOnGround;
        }
        else {
            Step.ticksOnGround = 0;
        }
        if (this.mode.isMode("Vanilla")) {
            Step.mc.thePlayer.stepHeight = (float)(this.height.getValue() + 6.9E-4);
        }
        else if (Step.mc.thePlayer.stepHeight == (float)(this.height.getValue() + 6.9E-4)) {
            Step.mc.thePlayer.stepHeight = 0.6f;
        }
        if ((!Objects.requireNonNull(Step.modManager.getModulebyName("Speed")).isEnabled() || this.mode.isMode("Hypixel")) && Step.mc.thePlayer.onGround && !PlayerUtils.isInLiquid()) {
            final String mode = this.mode.getMode();
            switch (mode) {
                case "NCP":
                case "Hypixel": {
                    Step.mc.thePlayer.stepHeight = (float)(this.height.getValue() + 6.9E-4);
                    break;
                }
            }
        }
        else {
            Step.mc.thePlayer.stepHeight = 0.6f;
        }
        final Block above = PlayerUtils.getBlockRelativeToPlayer(x, 1.1, z);
        final String mode2 = this.mode.getMode();
        switch (mode2) {
            case "Hypixel":
            case "NCP": {
                if (Step.ticksSinceLastStep > 1 && Wrapper.timer.timerSpeed == (float)(this.timer.getValue() + 6.9E-4)) {
                    Wrapper.timer.timerSpeed = 1.0f;
                    break;
                }
                break;
            }
        }
        if (!this.mode.isMode("NCP") && !this.mode.isMode("Hypixel") && !this.mode.isMode("Vanilla") && Step.mc.thePlayer.onGround && Step.mc.thePlayer.isCollidedHorizontally && MoveUtils.isMoving() && (above instanceof BlockAir || above instanceof BlockBush || above instanceof BlockSnow) && !(PlayerUtils.getBlockRelativeToPlayer(x, 0.5, z) instanceof BlockAir)) {
            this.stepTicks = 0;
            this.stepHeight = 1.0f;
            Step.mc.thePlayer.motionY = 0.41999998688697815;
            this.posY = Step.mc.thePlayer.posY;
            this.posZ = Step.mc.thePlayer.posZ;
            this.posX = Step.mc.thePlayer.posX;
            final String mode3 = this.mode.getMode();
            switch (mode3) {
                case "Low": {
                    Step.mc.thePlayer.motionY = 0.37 + Math.random() / 500.0;
                    break;
                }
                case "Low2": {
                    Step.mc.thePlayer.motionY = 0.39 + Math.random() / 500.0;
                    break;
                }
                case "Low3": {
                    Step.mc.thePlayer.motionY = 0.404 + Math.random() / 500.0;
                    break;
                }
                case "Matrix": {
                    Step.mc.thePlayer.jump();
                    break;
                }
            }
        }
        if (MoveUtils.isMoving()) {
            final String mode4 = this.mode.getMode();
            switch (mode4) {
                case "Normal": {
                    if (this.stepTicks == 3 && this.stepHeight == 1.0f) {
                        Step.mc.thePlayer.motionY = -0.14;
                        break;
                    }
                    break;
                }
                case "Delay": {
                    if (this.stepTicks == 2) {
                        Step.mc.thePlayer.motionY = 0.17;
                        break;
                    }
                    break;
                }
                case "Matrix": {
                    if (this.stepTicks == 1) {
                        Step.mc.thePlayer.onGround = true;
                        break;
                    }
                    break;
                }
            }
        }
    }
}
