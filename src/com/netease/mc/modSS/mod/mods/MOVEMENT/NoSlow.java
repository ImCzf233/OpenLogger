package com.netease.mc.modSS.mod.mods.MOVEMENT;

import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.util.*;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;
import com.netease.mc.modSS.utils.player.*;
import net.minecraft.client.entity.*;
import net.minecraftforge.fml.common.gameevent.*;
import dev.ss.world.event.mixinevents.*;

public class NoSlow extends Mod
{
    private boolean aBoolean;
    private boolean blocking;
    private boolean intaveFunnyBoolean;
    public TimerUtils timer;
    private long delay;
    private int ticks;
    
    public NoSlow() {
        super("NoSlow", "Prevents you from slowing down when using items", Category.MOVEMENT);
        this.timer = new TimerUtils();
        this.addSetting(new Setting("Mode", this, "NCP", new String[] { "NCP", "Delay", "Reverse NCP", "Intave", "Vanilla", "Inject", "AAC", "Inject2", "AAC5" }));
    }
    
    @Override
    public void onEnable() {
        if (ShellSock.getClient().settingsManager.getSettingByName(this, "Mode").isMode("Inject") && !(NoSlow.mc.thePlayer.movementInput instanceof NoSlowUtils)) {
            NoSlow.mc.thePlayer.movementInput = new NoSlowUtils(NoSlow.mc.gameSettings);
        }
        super.onEnable();
    }
    
    @Override
    public void onMotionInjectEvent(final EventMotion event) {
        if (event.isPre()) {
            this.onPreMotionX();
        }
        else {
            this.onPostMotionX();
        }
    }
    
    public void onPreMotionX() {
        final String mode = NoSlow.settingsManager.getSettingByName(this, "Mode").getMode();
        switch (mode) {
            case "NCP": {
                if (NoSlow.mc.thePlayer.isBlocking()) {
                    Wrapper.INSTANCE.sendPacket((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    break;
                }
                break;
            }
            case "Delay": {
                if (!NoSlow.mc.thePlayer.isBlocking()) {
                    this.aBoolean = false;
                }
                if (NoSlow.mc.thePlayer.isBlocking() && NoSlow.mc.thePlayer.ticksExisted % 5 == 0 && this.aBoolean) {
                    Wrapper.INSTANCE.sendPacket((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    this.aBoolean = false;
                }
                if (NoSlow.mc.thePlayer.isBlocking() && NoSlow.mc.thePlayer.ticksExisted % 5 == 1 && !this.aBoolean) {
                    Wrapper.INSTANCE.sendPacket((Packet)new C08PacketPlayerBlockPlacement(NoSlow.mc.thePlayer.getCurrentEquippedItem()));
                    this.aBoolean = true;
                    break;
                }
                break;
            }
            case "Reverse NCP": {
                if (NoSlow.mc.thePlayer.isBlocking()) {
                    Wrapper.INSTANCE.sendPacket((Packet)new C08PacketPlayerBlockPlacement(NoSlow.mc.thePlayer.getCurrentEquippedItem()));
                    break;
                }
                break;
            }
            case "Intave": {
                if (NoSlow.mc.thePlayer.isBlocking() && this.timer.hasReached(this.delay)) {
                    Wrapper.INSTANCE.sendPacket((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    this.timer.reset();
                    break;
                }
                break;
            }
            case "AAC": {
                if (Wrapper.INSTANCE.player().isBlocking() && !Wrapper.INSTANCE.player().isRiding() && MoveUtils.isMoving()) {
                    Wrapper.INSTANCE.sendPacket((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
                    break;
                }
                break;
            }
        }
    }
    
    public void onPostMotionX() {
        final String mode = NoSlow.settingsManager.getSettingByName(this, "Mode").getMode();
        switch (mode) {
            case "AAC5": {
                final EntityPlayerSP thePlayer = NoSlow.mc.thePlayer;
                if (thePlayer.isUsingItem() || thePlayer.isBlocking()) {
                    NoSlow.mc.getNetHandler().addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, thePlayer.getHeldItem(), 0.0f, 0.0f, 0.0f));
                }
            }
            case "NCP": {
                if (NoSlow.mc.thePlayer.isBlocking()) {
                    Wrapper.INSTANCE.sendPacket((Packet)new C08PacketPlayerBlockPlacement(NoSlow.mc.thePlayer.getCurrentEquippedItem()));
                    break;
                }
                break;
            }
            case "Hypixel": {
                if (NoSlow.mc.thePlayer.isUsingItem()) {
                    Wrapper.INSTANCE.sendPacket((Packet)new C08PacketPlayerBlockPlacement(NoSlow.mc.thePlayer.getCurrentEquippedItem()));
                    break;
                }
                break;
            }
            case "Reverse NCP": {
                if (NoSlow.mc.thePlayer.isBlocking()) {
                    Wrapper.INSTANCE.sendPacket((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    break;
                }
                break;
            }
            case "Intave": {
                if (NoSlow.mc.thePlayer.isBlocking() && this.timer.hasReached(this.delay)) {
                    Wrapper.INSTANCE.sendPacket((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    this.delay = 200L;
                    if (this.intaveFunnyBoolean) {
                        this.delay = 100L;
                        this.intaveFunnyBoolean = false;
                    }
                    else {
                        this.intaveFunnyBoolean = true;
                    }
                    this.timer.reset();
                    break;
                }
                break;
            }
            case "AAC": {
                if (Wrapper.INSTANCE.player().isBlocking() && !Wrapper.INSTANCE.player().isRiding() && MoveUtils.isMoving()) {
                    Wrapper.INSTANCE.sendPacket((Packet)new C08PacketPlayerBlockPlacement(Wrapper.INSTANCE.player().inventory.getCurrentItem()));
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (NoSlow.settingsManager.getSettingByName(this, "Mode").isMode("Inject") && ((NoSlow.mc.thePlayer.onGround && !NoSlow.mc.gameSettings.keyBindJump.isKeyDown()) || (NoSlow.mc.gameSettings.keyBindSneak.isKeyDown() && NoSlow.mc.gameSettings.keyBindUseItem.isKeyDown()))) {
            if (!(NoSlow.mc.thePlayer.movementInput instanceof NoSlowUtils)) {
                NoSlow.mc.thePlayer.movementInput = new NoSlowUtils(NoSlow.mc.gameSettings);
            }
            final NoSlowUtils move = (NoSlowUtils)NoSlow.mc.thePlayer.movementInput;
            move.setNSD(true);
            if (event.phase == TickEvent.Phase.START) {
                if (Wrapper.INSTANCE.player().isBlocking() && !Wrapper.INSTANCE.player().isRiding() && MoveUtils.isMoving()) {
                    Wrapper.INSTANCE.sendPacket((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
                }
            }
            else if (event.phase == TickEvent.Phase.END && Wrapper.INSTANCE.player().isBlocking() && !Wrapper.INSTANCE.player().isRiding() && MoveUtils.isMoving()) {
                Wrapper.INSTANCE.sendPacket((Packet)new C08PacketPlayerBlockPlacement(Wrapper.INSTANCE.player().inventory.getCurrentItem()));
            }
        }
        super.onPlayerTick(event);
    }
    
    @Override
    public void onDisable() {
        if (NoSlow.settingsManager.getSettingByName(this, "Mode").isMode("Inject")) {
            if (!(NoSlow.mc.thePlayer.movementInput instanceof NoSlowUtils)) {
                NoSlow.mc.thePlayer.movementInput = new NoSlowUtils(NoSlow.mc.gameSettings);
            }
            final NoSlowUtils move = (NoSlowUtils)NoSlow.mc.thePlayer.movementInput;
            move.setNSD(false);
        }
        super.onDisable();
    }
    
    @Override
    public void onNolsowEvent(final EventNoSlowDown event) {
        if (NoSlow.settingsManager.getSettingByName(this, "Mode").isMode("Inject2")) {
            event.setForward(2.0f);
            event.setStrafe(2.0f);
            Wrapper.modmsg(this, "For:" + event.getForward());
            Wrapper.modmsg(this, "Stra" + event.getStrafe());
        }
        super.onNolsowEvent(event);
    }
}
