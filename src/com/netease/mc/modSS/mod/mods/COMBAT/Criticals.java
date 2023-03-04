package com.netease.mc.modSS.mod.mods.COMBAT;

import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.network.*;
import dev.ss.world.event.mixinevents.*;
import com.netease.mc.modSS.utils.player.*;
import com.netease.mc.modSS.managers.*;
import net.minecraft.network.play.client.*;
import net.minecraftforge.fml.relauncher.*;
import net.minecraft.network.play.server.*;
import java.lang.reflect.*;

public class Criticals extends Mod
{
    public TimerUtils timer;
    private int ticks;
    private int offGroundTicks;
    private boolean attacked;
    int targetid;
    boolean shouldcrit;
    public Setting mode;
    public Setting delay;
    public Setting tickvalue;
    public Setting debug;
    public Setting hurttime;
    
    public Criticals() {
        super("Criticals", "Changes all your hits to critical hits", Category.COMBAT);
        this.timer = new TimerUtils();
        this.mode = new Setting("Mode", this, "NCP", new String[] { "NCP", "Packet", "Low", "Hypixel", "Low2", "Jump", "Down", "Flux", "Position", "NoGround", "NCPPacket", "Spartan", "Matrix", "HYTPacket" });
        this.delay = new Setting("Delay", this, 500.0, 0.0, 1000.0, true);
        this.tickvalue = new Setting("Tick", this, 3.0, 0.0, 20.0, true);
        this.debug = new Setting("Debug", this, true);
        this.hurttime = new Setting("HurtTime", this, 10.0, 0.0, 10.0, true);
        this.addSetting(this.mode);
        this.addSetting(this.delay);
        this.addSetting(this.tickvalue);
        this.addSetting(this.debug);
    }
    
    @Override
    public void onAttackEvent(final EventAttack event) {
        if (Criticals.mc.thePlayer.isOnLadder() || Criticals.mc.thePlayer.isInWater() || Criticals.mc.thePlayer.isInLava() || Criticals.mc.thePlayer.ridingEntity != null || !this.timer.hasReached((float)this.delay.getValue())) {
            return;
        }
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Packet": {
                final double[] array;
                final double[] values = array = new double[] { 0.0625, 0.001 - Math.random() / 10000.0 };
                for (final double d : array) {
                    Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + d, Criticals.mc.thePlayer.posZ, false));
                }
                break;
            }
            case "NCP": {
                Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + 0.11, Criticals.mc.thePlayer.posZ, true));
                Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + 0.1100013579, Criticals.mc.thePlayer.posZ, false));
                Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + 1.3579E-6, Criticals.mc.thePlayer.posZ, false));
                break;
            }
            case "Low": {
                if (Criticals.mc.thePlayer.onGround) {
                    this.offGroundTicks = 0;
                }
                else {
                    ++this.offGroundTicks;
                }
                if (this.offGroundTicks == 1) {
                    Criticals.mc.thePlayer.motionY = -(0.01 - Math.random() / 500.0);
                }
                if (Criticals.mc.thePlayer.onGround) {
                    Criticals.mc.thePlayer.motionY = 0.1 + Math.random() / 500.0;
                    break;
                }
                break;
            }
            case "Low2": {
                if (Criticals.mc.thePlayer.onGround) {
                    Criticals.mc.thePlayer.setPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + (0.3 - Math.random() / 500.0), Criticals.mc.thePlayer.posZ);
                    break;
                }
                break;
            }
            case "Jump": {
                if (Criticals.mc.thePlayer.onGround) {
                    Criticals.mc.thePlayer.jump();
                    break;
                }
                break;
            }
            case "Flux": {
                ++this.ticks;
                if (this.ticks == this.tickvalue.getValue()) {
                    Criticals.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + 0.001, Criticals.mc.thePlayer.posZ, true));
                    Criticals.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY, Criticals.mc.thePlayer.posZ, false));
                    Criticals.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + 0.001, Criticals.mc.thePlayer.posZ, true));
                    Criticals.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY, Criticals.mc.thePlayer.posZ, false));
                    Criticals.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + 0.001, Criticals.mc.thePlayer.posZ, true));
                    Criticals.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY, Criticals.mc.thePlayer.posZ, false));
                }
                if (this.ticks >= 5) {
                    this.ticks = 0;
                    break;
                }
                break;
            }
            case "NCPPacket": {
                Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + 0.11, Criticals.mc.thePlayer.posZ, false));
                Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + 0.1100013579, Criticals.mc.thePlayer.posZ, false));
                Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + 1.3579E-6, Criticals.mc.thePlayer.posZ, false));
                break;
            }
            case "Spartan": {
                Criticals.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + 0.04, Criticals.mc.thePlayer.posZ, true));
                Criticals.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY, Criticals.mc.thePlayer.posZ, false));
                break;
            }
            case "Matrix": {
                if (Criticals.mc.thePlayer.motionX == 0.0 && Criticals.mc.thePlayer.motionZ == 0.0) {
                    Criticals.mc.thePlayer.motionY = 0.2;
                    break;
                }
                break;
            }
            case "HYTPacket": {
                final double x = Criticals.mc.thePlayer.posX;
                final double y = Criticals.mc.thePlayer.posY;
                final double z = Criticals.mc.thePlayer.posZ;
                Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.11, z, true));
                Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.1100013579, z, false));
                Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.3579E-6, z, false));
                break;
            }
        }
        this.attacked = true;
        this.timer.reset();
        super.onAttackEvent(event);
    }
    
    @Override
    public void onPreMotion(final EventPreMotion event) {
        if (this.attacked) {
            final String mode = this.mode.getMode();
            switch (mode) {
                case "Down": {
                    if (this.ticks >= 5) {
                        Criticals.mc.thePlayer.motionY = -MoveUtils.getJumpMotion(0.42f);
                        this.attacked = false;
                        this.ticks = 0;
                        break;
                    }
                    ++this.ticks;
                    break;
                }
                case "Hypixel": {
                    if (Criticals.mc.thePlayer.onGround) {
                        switch (++this.ticks) {
                            case 1: {
                                event.setY(event.getY() + 0.0625 + Math.random() / 100.0);
                                event.setOnGround(false);
                                break;
                            }
                            case 2: {
                                event.setY(event.getY() + 0.03125 + Math.random() / 100.0);
                                event.setOnGround(false);
                                break;
                            }
                            case 3: {
                                this.attacked = false;
                                this.ticks = 0;
                                break;
                            }
                        }
                        break;
                    }
                    this.attacked = false;
                    this.ticks = 0;
                    break;
                }
                case "Position": {
                    if (Criticals.mc.thePlayer.onGround) {
                        switch (++this.ticks) {
                            case 1: {
                                event.setY(event.getY() + 0.0625);
                                event.setOnGround(false);
                                break;
                            }
                            case 2: {
                                event.setY(event.getY() + 0.015625);
                                event.setOnGround(false);
                                break;
                            }
                            case 3: {
                                this.attacked = false;
                                this.ticks = 0;
                                break;
                            }
                        }
                        break;
                    }
                    this.attacked = false;
                    this.ticks = 0;
                    break;
                }
            }
        }
        super.onPreMotion(event);
    }
    
    @Override
    public void onDisable() {
        this.attacked = false;
        this.timer.reset();
        this.ticks = 0;
        super.onDisable();
    }
    
    @Override
    public boolean onPacket(final Packet packet, final Connection.Side side) {
        if (side == Connection.Side.OUT) {
            if (packet instanceof C02PacketUseEntity) {
                final C02PacketUseEntity attack = (C02PacketUseEntity)packet;
                final Field crit = ReflectionHelper.findField((Class)C02PacketUseEntity.class, new String[] { "entityId", "field_149567_a" });
                try {
                    if (!crit.isAccessible()) {
                        crit.setAccessible(true);
                    }
                    this.targetid = crit.getInt(attack);
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        else if (side == Connection.Side.IN && this.debug.isEnabled() && packet instanceof S0BPacketAnimation) {
            final S0BPacketAnimation s08 = (S0BPacketAnimation)packet;
            if (s08.getAnimationType() == 4 && s08.getEntityID() == this.targetid) {
                Wrapper.message("Crit on " + Criticals.mc.theWorld.getEntityByID(this.targetid).getName());
            }
        }
        return super.onPacket(packet, side);
    }
}
