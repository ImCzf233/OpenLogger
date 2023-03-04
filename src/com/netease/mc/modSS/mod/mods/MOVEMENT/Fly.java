package com.netease.mc.modSS.mod.mods.MOVEMENT;

import com.netease.mc.modSS.setting.*;
import java.util.concurrent.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraftforge.fml.relauncher.*;
import com.netease.mc.modSS.utils.player.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.client.entity.*;
import com.netease.mc.modSS.*;
import com.netease.mc.modSS.mod.*;
import net.minecraft.block.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.network.play.client.*;
import dev.ss.world.event.mixinevents.*;
import dev.ss.world.event.eventapi.types.*;
import net.minecraft.network.play.server.*;

public class Fly extends Mod
{
    public Setting mode;
    public Setting inf;
    public Setting speedvalue;
    boolean firstEnable;
    private final ConcurrentLinkedQueue<Packet<?>> packets;
    private int i;
    private int ticks;
    private int wasos;
    private int offGroundTicks;
    private int onGroundTicks;
    private int hypixelTicks;
    private int pearlSlot;
    private boolean hypixelStart;
    private boolean jumped;
    private boolean flag;
    private boolean pearlFly;
    private boolean startFlyingCapability;
    private float speed;
    private float startSpeed;
    private int saveSlot;
    private float oPositionY;
    private float fov;
    private double moveSpeed;
    private double lastDist;
    private double d;
    private int stage;
    private double startingLocationX;
    private double startingLocationY;
    private double startingLocationZ;
    private double buffer;
    private boolean movingTowardsStartingLocation;
    private boolean bool;
    private int ticksSinceFlag;
    private int boostTicks;
    private boolean dmged;
    private boolean beingDmged;
    private boolean clipped;
    private final TimerUtils pearlTimer;
    private long groundTimer;
    boolean nearEntity;
    private final Vec3 lastReportedPosition;
    private int packet;
    private final int tick = 0;
    private boolean hypixelVanillaFly;
    
    public Fly() {
        super("Fly", "fly go go", Category.MOVEMENT);
        this.mode = new Setting("Mode", this, "Vulcan", new String[] { "Vulcan", "Vulcan2", "Dynamic" });
        this.inf = new Setting("Inf", this, false);
        this.speedvalue = new Setting("Speed", this, 1.0, 1.0, 5.0, false);
        this.packets = new ConcurrentLinkedQueue<Packet<?>>();
        this.i = 0;
        this.pearlSlot = -1;
        this.startSpeed = 0.05f;
        this.saveSlot = -1;
        this.fov = Fly.mc.gameSettings.fovSetting;
        this.pearlTimer = new TimerUtils();
        this.lastReportedPosition = new Vec3(0.0, 0.0, 0.0);
        this.packet = 0;
        this.addSetting(this.mode);
        this.addSetting(this.inf);
        this.addSetting(this.speedvalue);
    }
    
    @Override
    public void onDisable() {
        float speedInAir = (float)ReflectionHelper.getPrivateValue((Class)EntityPlayer.class, (Object)Fly.mc.thePlayer, new String[] { Mappings.speedInAir });
        Fly.mc.gameSettings.fovSetting = this.fov;
        this.clipped = false;
        this.hypixelStart = false;
        speedInAir = 0.02f;
        Fly.mc.thePlayer.capabilities.isFlying = false;
        Fly.mc.thePlayer.capabilities.setFlySpeed(this.startSpeed);
        Wrapper.timer.timerSpeed = 1.0f;
        this.dmged = false;
        this.beingDmged = false;
        this.saveSlot = -1;
        this.speed = 0.0f;
        this.jumped = false;
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Vulcan": {
                Fly.mc.thePlayer.motionY = -0.09800000190735147;
                MoveUtils.stop();
                MoveUtils.strafe(0.1);
                break;
            }
        }
        super.onDisable();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.mode.isMode("Dynamic")) {
            final float flyspeed = (float)this.speedvalue.getValue();
            Fly.mc.thePlayer.jumpMovementFactor = 0.4f;
            Fly.mc.thePlayer.motionX = 0.0;
            Fly.mc.thePlayer.motionY = 0.0;
            Fly.mc.thePlayer.motionZ = 0.0;
            final EntityPlayerSP thePlayer = Fly.mc.thePlayer;
            thePlayer.jumpMovementFactor *= flyspeed * 3.0f;
            if (Wrapper.INSTANCE.mcSettings().keyBindJump.isKeyDown()) {
                final EntityPlayerSP thePlayer2 = Fly.mc.thePlayer;
                thePlayer2.motionY += flyspeed;
            }
            if (Wrapper.INSTANCE.mcSettings().keyBindSneak.isKeyDown()) {
                final EntityPlayerSP thePlayer3 = Fly.mc.thePlayer;
                thePlayer3.motionY -= flyspeed;
            }
        }
        super.onClientTick(event);
    }
    
    @Override
    public void onEnable() {
        this.boostTicks = 0;
        this.fov = Fly.mc.gameSettings.fovSetting;
        this.clipped = false;
        this.hypixelStart = false;
        this.hypixelTicks = 0;
        this.flag = false;
        this.dmged = false;
        this.oPositionY = (float)Fly.mc.thePlayer.posY;
        this.moveSpeed = 0.18;
        this.stage = 0;
        this.jumped = false;
        this.startSpeed = Fly.mc.thePlayer.capabilities.getFlySpeed();
        this.startFlyingCapability = Fly.mc.thePlayer.capabilities.allowFlying;
        this.packet = 0;
        this.bool = false;
        this.ticks = 0;
        this.startingLocationX = Fly.mc.thePlayer.posX;
        this.startingLocationZ = Fly.mc.thePlayer.posZ;
        this.startingLocationY = Fly.mc.thePlayer.posY;
        this.movingTowardsStartingLocation = false;
        this.ticksSinceFlag = 999;
        Wrapper.timer.timerSpeed = 1.0f;
        this.speed = 0.0f;
        this.buffer = 0.0;
        this.ticksSinceFlag = -99999999;
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Vulcan": {
                if (this.inf.isEnabled() && !this.firstEnable) {
                    this.firstEnable = true;
                    ShellSock.getClient().notificationManager.add(new Notification("Having inf on might flag", Notification.Type.Info));
                    break;
                }
                break;
            }
        }
        super.onEnable();
    }
    
    @Override
    public void onPreMotion(final EventPreMotion event) {
        ++this.ticks;
        final double x = Fly.mc.thePlayer.posX - Fly.mc.thePlayer.prevPosX;
        final double z = Fly.mc.thePlayer.posZ - Fly.mc.thePlayer.prevPosZ;
        this.lastDist = Math.hypot(x, z);
        if (Fly.mc.thePlayer.onGround) {
            this.offGroundTicks = 0;
            ++this.onGroundTicks;
        }
        else {
            this.onGroundTicks = 0;
            ++this.offGroundTicks;
        }
        if (Fly.mc.thePlayer.ticksExisted < 2) {
            this.packets.clear();
            Wrapper.timer.timerSpeed = 1.0f;
            this.toggle();
        }
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Vulcan": {
                ++this.hypixelTicks;
                Fly.mc.thePlayer.posY = this.startingLocationY;
                if (this.ticks == 1) {
                    event.setY(event.getY() - 0.1);
                }
                if (this.hypixelTicks == -9) {
                    if (this.inf.isEnabled()) {
                        Fly.mc.thePlayer.motionY = 0.9666666666666667;
                    }
                    else {
                        Fly.mc.thePlayer.motionY = 3.7;
                    }
                    MoveUtils.strafe(3.4);
                }
                if (this.inf.isEnabled() && MoveUtils.getSpeed() > 1.0 && this.hypixelTicks > -6) {
                    MoveUtils.strafe(0.34);
                }
                if (this.hypixelTicks == -6) {
                    MoveUtils.strafe(0.32);
                    Fly.mc.thePlayer.motionY = -0.06546918812805425;
                }
                if (this.hypixelTicks > -6 && Fly.mc.thePlayer.ticksExisted % 2 == 0) {
                    Fly.mc.thePlayer.motionY = -0.09800000190735147;
                }
                if (this.inf.isEnabled() && this.hypixelStart && this.hypixelTicks % 35 == 0) {
                    Fly.mc.thePlayer.motionY = 2.9;
                }
                if (!MoveUtils.isMoving()) {
                    MoveUtils.stop();
                    break;
                }
                break;
            }
            case "Vulcan2": {
                ++this.ticksSinceFlag;
                if (!(PlayerUtils.getBlockRelativeToPlayer(0.0, -0.2, 0.0) instanceof BlockAir) && Fly.mc.thePlayer.getDistanceSq(this.startingLocationX, this.startingLocationY, this.startingLocationZ) > 16.0) {
                    Fly.mc.thePlayer.jump();
                    this.ticksSinceFlag = 0;
                }
                if (this.ticksSinceFlag > 20 || this.ticksSinceFlag < 0) {
                    Fly.mc.thePlayer.motionY = 0.0;
                    switch (this.offGroundTicks) {
                        case 1: {
                            Fly.mc.thePlayer.motionY = (Fly.mc.gameSettings.keyBindJump.isKeyDown() ? 1.0 : 0.5);
                            break;
                        }
                        case 2: {
                            Fly.mc.thePlayer.motionY = (Fly.mc.gameSettings.keyBindSneak.isKeyDown() ? -1.0 : -0.5);
                            break;
                        }
                        case 3: {
                            Fly.mc.thePlayer.motionY = 0.0;
                            this.offGroundTicks = 0;
                            break;
                        }
                    }
                }
                else if (this.ticksSinceFlag >= 4) {
                    Fly.mc.thePlayer.motionY = 0.0;
                    Fly.mc.thePlayer.setPosition(Fly.mc.thePlayer.posX, Math.round(event.getY() / 0.5) * 0.5, Fly.mc.thePlayer.posZ);
                }
                if (((this.ticksSinceFlag <= 20 && this.ticksSinceFlag >= 0 && this.ticksSinceFlag >= 4) || Fly.mc.thePlayer.posY % 0.5 == 0.0) && PlayerUtils.generalAntiPacketLog()) {
                    final double mathGround2 = Math.round(event.getY() / 0.015625) * 0.015625;
                    MoveUtils.strafe(0.1694);
                    Fly.mc.thePlayer.setPosition(Fly.mc.thePlayer.posX, mathGround2, Fly.mc.thePlayer.posZ);
                    event.setY(mathGround2);
                    event.setOnGround(true);
                    Fly.mc.thePlayer.onGround = true;
                }
                if (this.bool || (!PlayerUtils.generalAntiPacketLog() && this.ticks > 15)) {
                    if (PlayerUtils.generalAntiPacketLog()) {
                        Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition((Fly.mc.thePlayer.posX + Fly.mc.thePlayer.lastTickPosX) / 2.0, (Fly.mc.thePlayer.posY + Fly.mc.thePlayer.lastTickPosY) / 2.0, (Fly.mc.thePlayer.posZ + Fly.mc.thePlayer.lastTickPosZ) / 2.0, true));
                    }
                    MoveUtils.strafe(MoveUtils.moveSpeed() * 1.5 * 2.0);
                    Wrapper.timer.timerSpeed = 1.2f + Fly.mc.thePlayer.hurtTime / 3.0f;
                    break;
                }
                MoveUtils.strafe(MoveUtils.moveSpeed());
                break;
            }
        }
        super.onPreMotion(event);
    }
    
    @Override
    public void onPacketEvent(final EventPacket event) {
        if (event.getEventType() == EventType.RECIEVE && event.getPacket() instanceof S08PacketPlayerPosLook) {
            final S08PacketPlayerPosLook s08 = (S08PacketPlayerPosLook)event.getPacket();
            final String mode = this.mode.getMode();
            switch (mode) {
                case "Vulcan": {
                    this.hypixelTicks = -10;
                    this.hypixelStart = true;
                    break;
                }
                case "Vulcan2": {
                    if (Fly.mc.thePlayer.ticksExisted <= 20) {
                        break;
                    }
                    if (Math.abs(s08.getX() - this.startingLocationX) + Math.abs(s08.getY() - this.startingLocationY) + Math.abs(s08.getZ() - this.startingLocationZ) >= 4.0) {
                        this.toggle();
                        break;
                    }
                    if (PlayerUtils.generalAntiPacketLog()) {
                        event.setCancelled(true);
                    }
                    if (!this.bool) {
                        Fly.mc.thePlayer.hurtTime = 9;
                        this.bool = true;
                        break;
                    }
                    break;
                }
            }
        }
        super.onPacketEvent(event);
    }
}
