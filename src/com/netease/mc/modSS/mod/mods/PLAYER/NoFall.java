package com.netease.mc.modSS.mod.mods.PLAYER;

import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.mod.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import dev.ss.world.event.mixinevents.*;
import com.netease.mc.modSS.utils.*;
import com.netease.mc.modSS.managers.*;
import net.minecraftforge.fml.relauncher.*;
import java.lang.reflect.*;

public class NoFall extends Mod
{
    public Setting mode;
    TimerUtils timer;
    private float lastTickFallDist;
    private float fallDist;
    private int offGroundTicks;
    private int tick;
    private boolean bool;
    
    public NoFall() {
        super("NoFall", "Gives you zero damage on fall", Category.PLAYER);
        this.mode = new Setting("Mode", this, "Packet", new String[] { "Packet", "Ref", "Vulcan", "Ground", "Spartan" });
        this.timer = new TimerUtils();
        this.addSetting(this.mode);
    }
    
    @Override
    public void onEnable() {
        this.tick = 1;
        super.onEnable();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.mode.isMode("Packet")) {
            if (Wrapper.INSTANCE.player().fallDistance > 2.0f) {
                Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer(true));
            }
        }
        else if (this.mode.isMode("Spartan") && this.timer.hasReached(10.0f) && NoFall.mc.thePlayer.fallDistance > 1.5) {
            Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition(NoFall.mc.thePlayer.posX, NoFall.mc.thePlayer.posY + 10.0, NoFall.mc.thePlayer.posZ, true));
            Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer.C04PacketPlayerPosition(NoFall.mc.thePlayer.posX, NoFall.mc.thePlayer.posY - 10.0, NoFall.mc.thePlayer.posZ, true));
        }
        super.onClientTick(event);
    }
    
    @Override
    public void onPreMotion(final EventPreMotion event) {
        if (NoFall.mc.thePlayer.onGround) {
            this.offGroundTicks = 0;
        }
        else {
            ++this.offGroundTicks;
        }
        if (NoFall.mc.thePlayer.fallDistance == 0.0f) {
            this.fallDist = 0.0f;
        }
        this.fallDist += NoFall.mc.thePlayer.fallDistance - this.lastTickFallDist;
        this.lastTickFallDist = NoFall.mc.thePlayer.fallDistance;
        final boolean isBlockUnder = PlayerUtils.isBlockUnder();
        if (NoFall.modManager.getModulebyName("HighJump").isEnabled() || !isBlockUnder) {
            return;
        }
        if (this.mode.isMode("Vulcan")) {
            double mathGround = Math.round(event.getY() / 0.015625) * 0.015625;
            if (this.fallDist > 1.3 && NoFall.mc.thePlayer.ticksExisted % 15 == 0) {
                NoFall.mc.thePlayer.setPosition(NoFall.mc.thePlayer.posX, mathGround, NoFall.mc.thePlayer.posZ);
                event.setY(mathGround);
                mathGround = Math.round(event.getY() / 0.015625) * 0.015625;
                if (Math.abs(mathGround - event.getY()) < 0.01) {
                    if (NoFall.mc.thePlayer.motionY < -0.4) {
                        NoFall.mc.thePlayer.motionY = -0.4;
                    }
                    Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer(true));
                    Wrapper.timer.timerSpeed = 0.9f;
                }
            }
            else if (Wrapper.timer.timerSpeed == 0.9f) {
                Wrapper.timer.timerSpeed = 1.0f;
            }
        }
        else if (this.mode.isMode("Ground") && NoFall.mc.thePlayer.ticksExisted % 8 == 0 && this.fallDist > 2.0f) {
            event.setOnGround(true);
        }
        super.onPreMotion(event);
    }
    
    @Override
    public boolean onPacket(final Packet packet, final Connection.Side side) {
        if (side == Connection.Side.OUT && Wrapper.INSTANCE.player().fallDistance > 2.0f && packet instanceof C03PacketPlayer) {
            final C03PacketPlayer p = (C03PacketPlayer)packet;
            if (this.mode.isMode("Ref")) {
                final Field field = ReflectionHelper.findField((Class)C03PacketPlayer.class, new String[] { "onGround", "field_149474_g" });
                try {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    field.setBoolean(p, true);
                }
                catch (Exception ex) {}
            }
        }
        return super.onPacket(packet, side);
    }
}
