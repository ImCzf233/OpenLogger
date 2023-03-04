package com.netease.mc.modSS.mod.mods.CLIENT;

import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.network.*;
import com.netease.mc.modSS.managers.*;
import net.minecraft.network.play.server.*;
import net.minecraft.network.play.client.*;
import java.lang.reflect.*;
import net.minecraftforge.fml.common.gameevent.*;

public class Disabler extends Mod
{
    public Setting spartan;
    public Setting mac;
    public Setting mac_vl;
    public Setting mac_debug;
    public Setting mac_point_ent;
    public Setting mac_hook_ent;
    public Setting mac_get_thread;
    public Setting dis_thread;
    public Setting dis_value;
    public Setting mac_info;
    public Setting mac_vl_value;
    public Setting mac_combat;
    public Setting mac_combat_value;
    public Setting mac_vl_null;
    public TimerUtils timer;
    
    public Disabler() {
        super("Disabler", "", Category.CLIENT);
        this.spartan = new Setting("Spartan", this, false);
        this.mac = new Setting("MAC", this, false);
        this.mac_vl = new Setting("MAC_VL", this, true);
        this.mac_debug = new Setting("MAC_DEBUG", this, true);
        this.mac_point_ent = new Setting("MAC_POINT_ENT", this, true);
        this.mac_hook_ent = new Setting("MAC_HOOK_ENT", this, true);
        this.mac_get_thread = new Setting("THREAD", this, true);
        this.dis_thread = new Setting("MAC_DIS_THREAD", this, false);
        this.dis_value = new Setting("DisValue", this, 0.0, 0.0, 50.0, true);
        this.mac_info = new Setting("MAC_INFO", this, false);
        this.mac_vl_value = new Setting("MAC_VL_VALUE", this, 1.0, 0.0, 50.0, true);
        this.mac_combat = new Setting("MAC_COMBAT", this, true);
        this.mac_combat_value = new Setting("MAC_COMBAT_VALUE", this, 2.0, 0.0, 4.0, false);
        this.mac_vl_null = new Setting("MAC_VL_NULL", this, false);
        this.timer = new TimerUtils();
        this.addSetting(this.spartan);
        this.addSetting(this.mac);
        this.addSetting(this.mac_debug);
        this.addSetting(this.mac_vl);
        this.addSetting(this.mac_get_thread);
        this.addSetting(this.dis_thread);
        this.addSetting(this.mac_hook_ent);
        this.addSetting(this.mac_point_ent);
        this.addSetting(this.mac_info);
        this.addSetting(this.dis_value);
        this.addSetting(this.mac_vl_value);
        this.addSetting(this.mac_combat);
        this.addSetting(this.mac_combat_value);
        this.addSetting(this.mac_vl_null);
    }
    
    @Override
    public void onEnable() {
        if (this.mac.isEnabled()) {
            Wrapper.message("Disable MAC");
        }
        if (this.mac_get_thread.isEnabled()) {
            final ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
            final int noThreads = currentGroup.activeCount();
            final Thread[] lstThreads = new Thread[noThreads];
            currentGroup.enumerate(lstThreads);
            for (int i = 0; i < noThreads; ++i) {
                Wrapper.message("Thread: " + i + " [ " + lstThreads[i].getName() + " ]");
            }
        }
        super.onEnable();
    }
    
    @Override
    public boolean onPacket(final Packet packet, final Connection.Side side) {
        if (packet instanceof C17PacketCustomPayload && this.mac.isEnabled() && side == Connection.Side.OUT) {
            Wrapper.modmsg(this, "C17 " + ((C17PacketCustomPayload)packet).getChannelName());
        }
        if (packet instanceof S3FPacketCustomPayload && this.mac.isEnabled() && side == Connection.Side.IN) {
            Wrapper.modmsg(this, "S3F " + ((S3FPacketCustomPayload)packet).getChannelName());
        }
        if (!Disabler.mc.isIntegratedServerRunning() && this.spartan.isEnabled()) {
            Wrapper.INSTANCE.sendPacket((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(Disabler.mc.thePlayer.posX, Disabler.mc.thePlayer.posY, Disabler.mc.thePlayer.posZ, Disabler.mc.thePlayer.rotationYaw, 91.0f, Disabler.mc.thePlayer.onGround));
        }
        return super.onPacket(packet, side);
    }
    
    @Override
    public void onDisable() {
        if (this.mac.isEnabled()) {
            try {
                final Class MAC = Class.forName("cn.margele.netease.clientside.MargeleAntiCheat");
                final Field DEBUG = MAC.getDeclaredField("f");
                DEBUG.setAccessible(true);
                DEBUG.set(MAC, false);
                final Field Minecraft = MAC.getDeclaredField("mc");
                Minecraft.setAccessible(true);
                Minecraft.set(MAC, null);
                final Field cheatingVl = MAC.getDeclaredField("g");
                cheatingVl.setAccessible(true);
                cheatingVl.set(MAC, null);
                final Field notCheatingVl = MAC.getDeclaredField("h");
                notCheatingVl.setAccessible(true);
                notCheatingVl.set(MAC, null);
            }
            catch (NoSuchFieldException ex) {}
            catch (ClassNotFoundException ex2) {}
            catch (IllegalAccessException ex3) {}
        }
        if (this.dis_thread.isEnabled()) {
            final ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
            final int noThreads = currentGroup.activeCount();
            final Thread[] lstThreads = new Thread[noThreads];
            currentGroup.enumerate(lstThreads);
            final Thread dis = lstThreads[(int)this.dis_value.getValue()];
            dis.interrupt();
            Wrapper.message("Dis -> " + dis.getName());
        }
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.mac.isEnabled()) {
            if (this.mac_combat.isEnabled()) {
                Wrapper.INSTANCE.player().jumpMovementFactor = (float)this.mac_combat_value.getValue();
            }
            try {
                final Class MAC = Class.forName("cn.margele.netease.clientside.MargeleAntiCheat");
                final Class Protection = Class.forName("cn.margele.netease.clientside.utils.ProtectedMixinMethods");
                if (this.mac_vl.isEnabled()) {
                    final Field cheatingVl = MAC.getDeclaredField("cheatingVl");
                    cheatingVl.setAccessible(true);
                    cheatingVl.set(MAC, (int)this.mac_vl_value.getValue());
                    final Field isCheating = MAC.getDeclaredField("isCheating");
                    isCheating.setAccessible(true);
                    final Field notCheatingVl = MAC.getDeclaredField("notCheatingVl");
                    notCheatingVl.setAccessible(true);
                    notCheatingVl.setInt(MAC, 1);
                    isCheating.set(MAC, false);
                }
                if (this.mac_debug.isEnabled()) {
                    final Field DEBUG = MAC.getDeclaredField("DEBUG");
                    DEBUG.setAccessible(true);
                    DEBUG.set(MAC, true);
                }
                if (this.mac_hook_ent.isEnabled()) {
                    final Field hook = MAC.getDeclaredField("hookedPointedEntity");
                    hook.setAccessible(true);
                    hook.set(MAC, null);
                }
                if (this.mac_point_ent.isEnabled()) {
                    final Field point = MAC.getDeclaredField("pointedEntityField");
                    point.setAccessible(true);
                    point.set(MAC, null);
                }
                if (this.mac_info.isEnabled()) {
                    try {
                        final Field cheatingVl = MAC.getDeclaredField("cheatingVl");
                        cheatingVl.setAccessible(true);
                        final Field notCheatingVl2 = MAC.getDeclaredField("notCheatingVl");
                        notCheatingVl2.setAccessible(true);
                        final Field isCheating2 = MAC.getDeclaredField("isCheating");
                        isCheating2.setAccessible(true);
                        if (this.timer.hasReached(5000.0f)) {
                            Wrapper.message("cheatingVl : " + cheatingVl.getInt(MAC));
                            Wrapper.message("notCheatingVl : " + notCheatingVl2.getInt(MAC));
                            Wrapper.message("isCheating : " + isCheating2.getBoolean(MAC));
                            this.timer.reset();
                        }
                    }
                    catch (NoSuchFieldException ex) {}
                    catch (IllegalAccessException ex2) {}
                }
            }
            catch (NoSuchFieldException ex3) {}
            catch (ClassNotFoundException ex4) {}
            catch (IllegalAccessException ex5) {}
        }
        super.onClientTick(event);
    }
}
