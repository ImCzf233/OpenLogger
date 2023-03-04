package com.netease.mc.modSS.mod.mods.MOVEMENT;

import com.netease.mc.modSS.setting.*;
import net.minecraft.network.*;
import com.netease.mc.modSS.utils.player.*;
import net.minecraft.client.*;
import net.minecraft.potion.*;
import net.minecraft.entity.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.client.settings.*;
import net.minecraft.entity.player.*;
import com.netease.mc.modSS.utils.*;
import net.minecraftforge.fml.relauncher.*;
import java.util.function.*;
import java.util.*;
import net.minecraft.network.play.client.*;
import dev.ss.world.event.mixinevents.*;
import net.minecraft.network.play.server.*;
import com.netease.mc.modSS.managers.*;
import com.netease.mc.modSS.*;
import com.netease.mc.modSS.mod.*;
import net.minecraft.block.*;

public class Speed extends Mod
{
    public Setting mode;
    public Setting verusmode;
    public Setting stopOnDisable;
    public Setting stopOnFlag;
    public Setting timerBoost;
    public Setting speedMulti;
    private long balance;
    private long lastPreMotion;
    private int ticks;
    private int offGroundTicks;
    private int onGroundTicks;
    private int stage;
    private int ticksDisable;
    private int aacCount;
    private double startY;
    private double speed;
    private boolean jumped;
    private boolean bool;
    private boolean verusEpicBypassBooleanTM;
    private boolean touchedGround;
    private final Deque<Packet<?>> packets;
    private float targetYaw;
    private float lastYaw;
    private float yaw;
    private double lastDist;
    TimerUtils lastFall;
    int moratick;
    private boolean istimer;
    
    public Speed() {
        super("Speed", "Lets you move faster", Category.MOVEMENT);
        this.mode = new Setting("Mode", this, "Vulcan", new String[] { "Vulcan", "Vulcan2", "StrafeHop", "Bhop", "Hypixel", "AAC", "NCP", "NCP2", "VulcanHop", "MinemoraHop", "MinelandHop" });
        this.verusmode = new Setting("VerusMode", this, "Off", new String[] { "Hop", "Float", "YPort" });
        this.stopOnDisable = new Setting("StopOnDisable", this, true);
        this.stopOnFlag = new Setting("StopOnFlag", this, true);
        this.timerBoost = new Setting("TimerBoost", this, 1.0, 1.0, 4.0, false);
        this.speedMulti = new Setting("SpeedMultiplier", this, 1.1, 1.0, 2.0, false);
        this.packets = new ArrayDeque<Packet<?>>();
        this.lastFall = new TimerUtils();
        this.addSetting(this.mode);
        this.addSetting(this.verusmode);
        this.addSetting(this.stopOnDisable);
        this.addSetting(this.stopOnFlag);
        this.addSetting(this.timerBoost);
        this.addSetting(this.speedMulti);
    }
    
    @Override
    public void onEnable() {
        this.ticks = 0;
        this.offGroundTicks = 0;
        this.speed = 0.0;
        this.onGroundTicks = 0;
        this.startY = Speed.mc.thePlayer.posY;
        this.packets.clear();
        this.moratick = 0;
        if (this.mode.isMode("Bhop")) {
            this.stage = 0;
        }
        else {
            this.stage = 2;
        }
        if (this.mode.isMode("NCP2")) {
            Wrapper.timer.timerSpeed = 1.0865f;
        }
        this.lastDist = 0.0;
        this.verusEpicBypassBooleanTM = false;
        this.touchedGround = false;
        this.bool = false;
        this.jumped = false;
        this.ticksDisable = 21;
        super.onEnable();
    }
    
    @Override
    public void onMoveEvent(final EventMove event) {
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Bhop": {
                if (Speed.mc.thePlayer.moveForward == 0.0f && Speed.mc.thePlayer.moveStrafing == 0.0f) {
                    this.speed = MoveUtils.defaultSpeed();
                }
                if (this.stage == 1 && Speed.mc.thePlayer.isCollidedVertically && (Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f)) {
                    this.speed = 1.35 + MoveUtils.defaultSpeed() - 0.01;
                }
                if (!PlayerUtils.isInLiquid() && this.stage == 2 && Speed.mc.thePlayer.isCollidedVertically && MoveUtils.isOnGround(0.01) && (Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f)) {
                    if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.jump)) {
                        event.setY(Speed.mc.thePlayer.motionY = 0.41999998688698 + (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1);
                    }
                    else {
                        event.setY(Speed.mc.thePlayer.motionY = 0.41999998688698);
                    }
                    Speed.mc.thePlayer.jump();
                    this.speed *= 1.533;
                }
                else if (this.stage == 3) {
                    final double difference = 0.66 * (this.lastDist - MoveUtils.defaultSpeed());
                    this.speed = this.lastDist - difference;
                }
                else {
                    final List collidingList = Speed.mc.theWorld.getCollidingBoundingBoxes((Entity)Speed.mc.thePlayer, Speed.mc.thePlayer.getEntityBoundingBox().offset(0.0, Speed.mc.thePlayer.motionY, 0.0));
                    if ((collidingList.size() > 0 || Speed.mc.thePlayer.isCollidedVertically) && this.stage > 0) {
                        this.stage = ((Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f) ? 1 : 0);
                    }
                    this.speed = this.lastDist - this.lastDist / 159.0;
                }
                this.speed = Math.max(this.speed, MoveUtils.defaultSpeed());
                if (this.stage > 0) {
                    if (PlayerUtils.isInLiquid()) {
                        this.speed = 0.1;
                    }
                    this.setMotion(event, this.speed);
                }
                if (Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f) {
                    ++this.stage;
                    break;
                }
                break;
            }
            case "AAC": {
                if (Speed.mc.thePlayer.fallDistance > 1.2) {
                    this.lastFall.reset();
                }
                if (!PlayerUtils.isInLiquid() && Wrapper.INSTANCE.player().isCollidedVertically && MoveUtils.isOnGround(0.01) && (Wrapper.INSTANCE.player().moveForward != 0.0f || Wrapper.INSTANCE.player().moveStrafing != 0.0f)) {
                    this.stage = 0;
                    Wrapper.INSTANCE.player().jump();
                    event.setY(Speed.mc.thePlayer.motionY = 0.41999998688698 + MoveUtils.getJumpEffect());
                    if (this.aacCount < 4) {
                        ++this.aacCount;
                    }
                }
                this.speed = this.getAACSpeed(this.stage, this.aacCount);
                if (Wrapper.INSTANCE.player().moveForward != 0.0f || Wrapper.INSTANCE.player().moveStrafing != 0.0f) {
                    if (PlayerUtils.isInLiquid()) {
                        this.speed = 0.075;
                    }
                    this.setMotion(null, this.speed);
                }
                if (Wrapper.INSTANCE.player().moveForward != 0.0f || Wrapper.INSTANCE.player().moveStrafing != 0.0f) {
                    ++this.stage;
                }
                if (Speed.mc.thePlayer.fallDistance > 1.2) {
                    this.lastFall.reset();
                }
                if (!PlayerUtils.isInLiquid() && Wrapper.INSTANCE.player().isCollidedVertically && MoveUtils.isOnGround(0.01) && (Wrapper.INSTANCE.player().moveForward != 0.0f || Wrapper.INSTANCE.player().moveStrafing != 0.0f)) {
                    this.stage = 0;
                    Wrapper.INSTANCE.player().jump();
                    event.setY(Wrapper.INSTANCE.player().motionY = 0.41999998688698 + MoveUtils.getJumpEffect());
                    if (this.aacCount < 4) {
                        ++this.aacCount;
                    }
                }
                this.speed = this.getAACSpeed(this.stage, this.aacCount);
                if (Wrapper.INSTANCE.player().moveForward != 0.0f || Wrapper.INSTANCE.player().moveStrafing != 0.0f) {
                    if (PlayerUtils.isInLiquid()) {
                        this.speed = 0.075;
                    }
                    this.setMotion(event, this.speed);
                }
                if (Wrapper.INSTANCE.player().moveForward != 0.0f || Wrapper.INSTANCE.player().moveStrafing != 0.0f) {
                    ++this.stage;
                    break;
                }
                break;
            }
        }
        super.onMoveEvent(event);
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        final String mode = this.mode.getMode();
        switch (mode) {
            case "StrafeHop": {
                if (Speed.mc.thePlayer.onGround && MoveUtils.isMoving()) {
                    Speed.mc.thePlayer.jump();
                }
                MoveUtils.strafe();
                break;
            }
            case "VulcanHop": {
                if (this.istimer) {
                    Wrapper.timer.timerSpeed = 1.0f;
                    this.istimer = false;
                }
                if (Math.abs(Speed.mc.thePlayer.movementInput.moveStrafe) < 0.1f) {
                    Speed.mc.thePlayer.jumpMovementFactor = 0.026499f;
                }
                else {
                    Speed.mc.thePlayer.jumpMovementFactor = 0.0244f;
                }
                Utils.setKeyPressed(Wrapper.INSTANCE.mcSettings().keyBindJump, GameSettings.isKeyDown(Speed.mc.gameSettings.keyBindJump));
                if (MoveUtils.getSpeed() < 0.2150000035762787 && !Speed.mc.thePlayer.onGround) {
                    MoveUtils.strafe(0.215f);
                }
                if (Speed.mc.thePlayer.onGround && MoveUtils.isMoving()) {
                    Utils.setKeyPressed(Wrapper.INSTANCE.mcSettings().keyBindJump, false);
                    Speed.mc.thePlayer.jump();
                    if (!Speed.mc.thePlayer.isAirBorne) {
                        return;
                    }
                    Wrapper.timer.timerSpeed = 1.25f;
                    this.istimer = true;
                    MoveUtils.strafe();
                    if (MoveUtils.getSpeed() < 0.5) {
                        MoveUtils.strafe(0.4849f);
                        break;
                    }
                    break;
                }
                else {
                    if (!MoveUtils.isMoving()) {
                        Wrapper.timer.timerSpeed = 1.0f;
                        Speed.mc.thePlayer.motionX = 0.0;
                        Speed.mc.thePlayer.motionZ = 0.0;
                        break;
                    }
                    break;
                }
                break;
            }
        }
        super.onClientTick(event);
    }
    
    @Override
    public void onDisable() {
        float speedInAir = (float)ReflectionHelper.getPrivateValue((Class)EntityPlayer.class, (Object)Speed.mc.thePlayer, new String[] { Mappings.speedInAir });
        speedInAir = 0.02f;
        Speed.mc.thePlayer.jumpMovementFactor = 0.02f;
        this.moratick = 0;
        Wrapper.timer.timerSpeed = 1.0f;
        if (this.stopOnDisable.isEnabled()) {
            MoveUtils.stop();
        }
        if (this.packets.isEmpty()) {
            return;
        }
        this.packets.forEach(Wrapper.INSTANCE::sendPacket);
        this.packets.clear();
        super.onDisable();
    }
    
    @Override
    public void onPreMotion(final EventPreMotion event) {
        ++this.ticks;
        ++this.ticksDisable;
        if (this.ticksDisable < 20) {
            return;
        }
        if (this.ticksDisable == 20) {
            this.onEnable();
        }
        if (Speed.mc.thePlayer.ticksExisted == 1) {
            this.toggle();
        }
        if (Objects.requireNonNull(Speed.modManager.getModulebyName("Fly")).isEnabled()) {
            return;
        }
        if (Speed.mc.thePlayer.onGround) {
            this.offGroundTicks = 0;
            ++this.onGroundTicks;
            if (!this.verusmode.isMode("Float")) {
                this.startY = Speed.mc.thePlayer.posY - 0.01;
            }
        }
        else {
            ++this.offGroundTicks;
            this.onGroundTicks = 0;
        }
        final String mode = this.mode.getMode();
        int n = -1;
        switch (mode.hashCode()) {
            case -1721492669: {
                if (mode.equals("Vulcan")) {
                    n = 0;
                    break;
                }
                break;
            }
            case -1826665137: {
                if (mode.equals("Vulcan2")) {
                    n = 1;
                    break;
                }
                break;
            }
            case -1248403467: {
                if (mode.equals("Hypixel")) {
                    n = 2;
                    break;
                }
                break;
            }
            case 77115: {
                if (mode.equals("NCP")) {
                    n = 3;
                    break;
                }
                break;
            }
            case 2390615: {
                if (mode.equals("NCP2")) {
                    n = 4;
                    break;
                }
                break;
            }
            case -166960283: {
                if (mode.equals("MinemoraHop")) {
                    n = 5;
                    break;
                }
                break;
            }
            case -1458876789: {
                if (mode.equals("MinelandHop")) {
                    n = 6;
                    break;
                }
                break;
            }
        }
        Label_1641: {
            switch (n) {
                case 0: {
                    if (!MoveUtils.isMoving()) {
                        MoveUtils.stop();
                        break;
                    }
                    if (Speed.mc.thePlayer.onGround) {
                        final double speed = MoveUtils.getBaseMoveSpeed() - 0.01;
                        MoveUtils.strafe(speed - Math.random() / 2000.0);
                        Speed.mc.thePlayer.jump();
                        this.bool = true;
                        break;
                    }
                    if (this.bool) {
                        if (this.offGroundTicks > 3) {
                            Speed.mc.thePlayer.motionY = MoveUtils.getPredictedMotionY(Speed.mc.thePlayer.motionY);
                        }
                        if (!(PlayerUtils.getBlockRelativeToPlayer(0.0, 2.0, 0.0) instanceof BlockAir)) {
                            MoveUtils.strafe(MoveUtils.getSpeed() * (1.1 - Math.random() / 500.0));
                        }
                    }
                    if (PlayerUtils.isInLiquid() || Speed.mc.thePlayer.hurtTime == 9) {
                        MoveUtils.strafe();
                        break;
                    }
                    break;
                }
                case 1: {
                    if (Speed.mc.thePlayer.onGround && !Speed.modManager.getModulebyName("Scaffold").isEnabled() && Speed.mc.thePlayer.motionY > -0.2) {
                        Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition((Speed.mc.thePlayer.posX + Speed.mc.thePlayer.lastTickPosX) / 2.0, (Speed.mc.thePlayer.posY + Speed.mc.thePlayer.lastTickPosY) / 2.0 - 0.0784000015258789, (Speed.mc.thePlayer.posZ + Speed.mc.thePlayer.lastTickPosZ) / 2.0, false));
                        Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer.C06PacketPlayerPosLook((Speed.mc.thePlayer.posX + Speed.mc.thePlayer.lastTickPosX) / 2.0, (Speed.mc.thePlayer.posY + Speed.mc.thePlayer.lastTickPosY) / 2.0, (Speed.mc.thePlayer.posZ + Speed.mc.thePlayer.lastTickPosZ) / 2.0, Speed.mc.thePlayer.rotationYaw, Speed.mc.thePlayer.rotationPitch, true));
                        Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Speed.mc.thePlayer.posX, Speed.mc.thePlayer.posY, Speed.mc.thePlayer.posZ, false));
                        Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Speed.mc.thePlayer.posX, Speed.mc.thePlayer.posY - 0.0784000015258789, Speed.mc.thePlayer.posZ, false));
                        Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(Speed.mc.thePlayer.posX, Speed.mc.thePlayer.posY, Speed.mc.thePlayer.posZ, Speed.mc.thePlayer.rotationYaw, Speed.mc.thePlayer.rotationPitch, true));
                        MoveUtils.strafe(MoveUtils.getBaseMoveSpeed() * 1.25 * 2.0);
                        break;
                    }
                    if (this.offGroundTicks == 1) {
                        MoveUtils.strafe(MoveUtils.getBaseMoveSpeed() * 0.9100000262260437);
                        break;
                    }
                    break;
                }
                case 2: {
                    if (Speed.mc.thePlayer.onGround && MoveUtils.isMoving()) {
                        Speed.mc.thePlayer.jump();
                        this.stage = 0;
                        this.speed = 1.100000023841858;
                    }
                    this.speed -= 0.004;
                    MoveUtils.setSpeed(MoveUtils.getBaseMoveSpeed() * this.speed);
                    break;
                }
                case 3: {
                    if (MoveUtils.isMoving()) {
                        if (Speed.mc.thePlayer.onGround) {
                            Speed.mc.thePlayer.jump();
                            MoveUtils.strafe(MoveUtils.getSpeed() * this.speedMulti.getValue() + Math.random() / 100.0);
                        }
                        else {
                            MoveUtils.strafe(MoveUtils.getSpeed() * 1.0035);
                        }
                        Speed.mc.thePlayer.jumpMovementFactor = 0.02725f;
                    }
                    if (this.ticks < 20) {
                        Wrapper.timer.timerSpeed = 0.98f;
                    }
                    if (this.ticks == 20) {
                        this.ticks = 0;
                    }
                    if (this.ticks < 9) {
                        Wrapper.timer.timerSpeed = (float)(this.timerBoost.getValue() + Math.random() / 100.0);
                        break;
                    }
                    break;
                }
                case 4: {
                    if (MoveUtils.isMoving()) {
                        if (Speed.mc.thePlayer.onGround) {
                            Speed.mc.thePlayer.jump();
                            float speedInAir = (float)ReflectionHelper.getPrivateValue((Class)EntityPlayer.class, (Object)Speed.mc.thePlayer, new String[] { Mappings.speedInAir });
                            speedInAir = 0.0223f;
                        }
                        MoveUtils.strafe();
                        break;
                    }
                    Speed.mc.thePlayer.motionX = 0.0;
                    Speed.mc.thePlayer.motionZ = 0.0;
                    break;
                }
                case 5: {
                    if (PlayerUtils.isInLiquid() || !MoveUtils.isMoving()) {
                        break;
                    }
                    if (Wrapper.INSTANCE.player().onGround) {
                        Wrapper.INSTANCE.player().jump();
                        this.moratick = 0;
                        break;
                    }
                    switch (++this.moratick) {
                        case 1: {
                            MoveUtils.strafe(MoveUtils.getSpeed() * 1.0110000371932983);
                            Wrapper.timer.timerSpeed = 0.98f;
                            break;
                        }
                        case 2: {
                            MoveUtils.strafe(MoveUtils.getSpeed() * 1.0099999904632568);
                            Wrapper.timer.timerSpeed = 1.01f;
                            break;
                        }
                        case 3: {
                            MoveUtils.strafe(MoveUtils.getSpeed() * 1.0119999647140503);
                            Wrapper.timer.timerSpeed = 0.9f;
                            break;
                        }
                        case 4: {
                            MoveUtils.strafe(MoveUtils.getSpeed() * 1.0);
                            Wrapper.timer.timerSpeed = 1.2f;
                            break;
                        }
                        case 5: {
                            this.moratick = 0;
                            Wrapper.timer.timerSpeed = 1.0f;
                            break;
                        }
                    }
                    break;
                }
                case 6: {
                    if (PlayerUtils.isInLiquid() || !MoveUtils.isMoving()) {
                        break;
                    }
                    if (Wrapper.INSTANCE.player().onGround) {
                        Wrapper.INSTANCE.player().jump();
                        MoveUtils.strafe();
                        this.moratick = 0;
                        break;
                    }
                    float speedInAir = (float)ReflectionHelper.getPrivateValue((Class)EntityPlayer.class, (Object)Speed.mc.thePlayer, new String[] { Mappings.speedInAir });
                    switch (++this.moratick) {
                        case 1: {
                            speedInAir = 10.0223f;
                            Wrapper.timer.timerSpeed = 0.98f;
                            break Label_1641;
                        }
                        case 2: {
                            speedInAir = 10.0323f;
                            Wrapper.timer.timerSpeed = 1.01f;
                            break Label_1641;
                        }
                        case 3: {
                            speedInAir = 10.0223f;
                            Wrapper.timer.timerSpeed = 0.9f;
                            break Label_1641;
                        }
                        case 4: {
                            speedInAir = 10.0253f;
                            Wrapper.timer.timerSpeed = 1.2f;
                            break Label_1641;
                        }
                        case 5: {
                            this.moratick = 0;
                            speedInAir = 0.02f;
                            Wrapper.timer.timerSpeed = 1.0f;
                            break Label_1641;
                        }
                    }
                    break;
                }
            }
        }
        super.onPreMotion(event);
    }
    
    @Override
    public void onStrafeEvent(final EventStrafe event) {
        if (this.ticksDisable <= 20) {
            return;
        }
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Vulcan": {
                if (this.offGroundTicks <= 2) {
                    MoveUtils.strafe();
                    break;
                }
                break;
            }
        }
        super.onStrafeEvent(event);
    }
    
    @Override
    public void onPacketEvent(final EventPacket event) {
        if (this.ticksDisable <= 20 && Speed.mc.thePlayer.ticksExisted <= 1) {
            final String mode = this.mode.getMode();
            switch (mode) {
                case "Vulcan2": {
                    if (!(event.getPacket() instanceof S08PacketPlayerPosLook) || Speed.mc.thePlayer.ticksExisted <= 20) {
                        break;
                    }
                    final S08PacketPlayerPosLook s08 = (S08PacketPlayerPosLook)event.getPacket();
                    if (Speed.mc.thePlayer.getDistanceSq(s08.getX(), s08.getY(), s08.getZ()) < 100.0) {
                        event.setCancelled(true);
                        break;
                    }
                    break;
                }
            }
        }
        super.onPacketEvent(event);
    }
    
    @Override
    public boolean onPacket(final Packet packet, final Connection.Side side) {
        if (this.ticksDisable <= 20 && Speed.mc.thePlayer.ticksExisted <= 1 && packet instanceof S08PacketPlayerPosLook && this.stopOnFlag.isEnabled()) {
            this.ticksDisable = 0;
            this.onDisable();
            ShellSock.getClient().notificationManager.add(new Notification("Disable Speed", Notification.Type.Info));
        }
        return super.onPacket(packet, side);
    }
    
    private void setMotion(final EventMove event, final double speed) {
        double forward = Wrapper.INSTANCE.player().movementInput.moveForward;
        double strafe = Wrapper.INSTANCE.player().movementInput.moveStrafe;
        float yaw = Wrapper.INSTANCE.player().rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            Wrapper.INSTANCE.player().motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
            Wrapper.INSTANCE.player().motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
        }
    }
    
    private double getAACSpeed(final int stage, final int jumps) {
        double value = 0.29;
        final double firstvalue = 0.3019;
        final double thirdvalue = 0.0286 - stage / 1000.0;
        if (stage == 0) {
            value = 0.497;
            if (jumps >= 2) {
                value += 0.1069;
            }
            if (jumps >= 3) {
                value += 0.046;
            }
            final Block block = MoveUtils.getBlockUnderPlayer((EntityPlayer)Wrapper.INSTANCE.player(), 0.01);
            if (block instanceof BlockIce || block instanceof BlockPackedIce) {
                value = 0.59;
            }
        }
        else if (stage == 1) {
            value = 0.3031;
            if (jumps >= 2) {
                value += 0.0642;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        }
        else if (stage == 2) {
            value = 0.302;
            if (jumps >= 2) {
                value += 0.0629;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        }
        else if (stage == 3) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 0.0607;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        }
        else if (stage == 4) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 0.0584;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        }
        else if (stage == 5) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 0.0561;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        }
        else if (stage == 6) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 0.0539;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        }
        else if (stage == 7) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 0.0517;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        }
        else if (stage == 8) {
            value = firstvalue;
            if (MoveUtils.isOnGround(0.05)) {
                value -= 0.002;
            }
            if (jumps >= 2) {
                value += 0.0496;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        }
        else if (stage == 9) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 0.0475;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        }
        else if (stage == 10) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 0.0455;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        }
        else if (stage == 11) {
            value = 0.3;
            if (jumps >= 2) {
                value += 0.045;
            }
            if (jumps >= 3) {
                value += 0.018;
            }
        }
        else if (stage == 12) {
            value = 0.301;
            if (jumps <= 2) {
                this.aacCount = 0;
            }
            if (jumps >= 2) {
                value += 0.042;
            }
            if (jumps >= 3) {
                value += thirdvalue + 0.001;
            }
        }
        else if (stage == 13) {
            value = 0.298;
            if (jumps >= 2) {
                value += 0.042;
            }
            if (jumps >= 3) {
                value += thirdvalue + 0.001;
            }
        }
        else if (stage == 14) {
            value = 0.297;
            if (jumps >= 2) {
                value += 0.042;
            }
            if (jumps >= 3) {
                value += thirdvalue + 0.001;
            }
        }
        if (Wrapper.INSTANCE.player().moveForward <= 0.0f) {
            value -= 0.06;
        }
        if (Wrapper.INSTANCE.player().isCollidedHorizontally) {
            value -= 0.1;
            this.aacCount = 0;
        }
        return value;
    }
}
