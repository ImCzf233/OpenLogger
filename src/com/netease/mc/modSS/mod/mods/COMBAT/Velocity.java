package com.netease.mc.modSS.mod.mods.COMBAT;

import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.setting.*;
import dev.ss.world.event.mixinevents.*;
import dev.ss.world.event.eventapi.types.*;
import net.minecraft.network.play.server.*;
import net.minecraft.network.play.client.*;
import net.minecraftforge.fml.common.gameevent.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.client.entity.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraft.network.*;
import com.netease.mc.modSS.managers.*;

public class Velocity extends Mod
{
    public Velocity() {
        super("Velocity", "", Category.COMBAT);
        this.addSetting(new Setting("Horizontal", this, 90.0, 0.0, 100.0, false));
        this.addSetting(new Setting("Vertical", this, 90.0, 0.0, 100.0, false));
        this.addSetting(new Setting("Chance", this, 90.0, 0.0, 100.0, false));
        this.addSetting(new Setting("Mode", this, "Cancel", new String[] { "Cancel", "Hypixel", "AAC", "Legit", "Ghost", "Vulcan" }));
    }
    
    @Override
    public void onPacketEvent(final EventPacket event) {
        if (Velocity.settingsManager.getSettingByName(this, "Mode").getMode().equals("Hypixel")) {
            if (event.getEventType() == EventType.RECIEVE && (event.getPacket() instanceof S12PacketEntityVelocity || event.getPacket() instanceof S27PacketExplosion)) {
                event.setCancelled(true);
            }
        }
        else if (Velocity.settingsManager.getSettingByName(this, "Mode").getMode().equals("Vulcan")) {
            if (event.getEventType() == EventType.RECIEVE) {
                final double horizontal = Velocity.settingsManager.getSettingByName(this, "Horizontal").getValue();
                final double vertical = Velocity.settingsManager.getSettingByName(this, "Vertical").getValue();
                if (horizontal == 0.0 && vertical == 0.0) {
                    event.setCancelled(true);
                }
            }
            else if (event.getEventType() == EventType.SEND && Velocity.mc.thePlayer.hurtTime > 0 && event.getPacket() instanceof C0FPacketConfirmTransaction) {
                event.setCancelled(true);
            }
        }
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (Velocity.settingsManager.getSettingByName(this, "Mode").getMode().equals("AAC")) {
            final EntityPlayerSP player = Wrapper.INSTANCE.player();
            if (player.hurtTime > 0 && player.hurtTime <= 7) {
                final EntityPlayerSP entityPlayerSP = player;
                entityPlayerSP.motionX *= 0.5;
                final EntityPlayerSP entityPlayerSP2 = player;
                entityPlayerSP2.motionZ *= 0.5;
            }
            if (player.hurtTime > 0 && player.hurtTime < 6) {
                player.motionX = 0.0;
                player.motionZ = 0.0;
            }
        }
        else if (Velocity.settingsManager.getSettingByName(this, "Mode").getMode().equals("Legit")) {
            if (Velocity.mc.thePlayer.maxHurtResistantTime != Velocity.mc.thePlayer.hurtResistantTime || Velocity.mc.thePlayer.maxHurtResistantTime == 0) {
                return;
            }
            Double random = Math.random();
            random *= 100.0;
            if (random < Velocity.settingsManager.getSettingByName(this, "Chance").getValue()) {
                float hori = (float)Velocity.settingsManager.getSettingByName(this, "Horizontal").getValue();
                hori /= 100.0f;
                float verti = (float)Velocity.settingsManager.getSettingByName(this, "Vertical").getValue();
                verti /= 100.0f;
                final EntityPlayerSP thePlayer = Velocity.mc.thePlayer;
                thePlayer.motionX *= hori;
                final EntityPlayerSP thePlayer2 = Velocity.mc.thePlayer;
                thePlayer2.motionZ *= hori;
                final EntityPlayerSP thePlayer3 = Velocity.mc.thePlayer;
                thePlayer3.motionY *= verti;
            }
            else {
                final EntityPlayerSP thePlayer4 = Velocity.mc.thePlayer;
                thePlayer4.motionX *= 1.0;
                final EntityPlayerSP thePlayer5 = Velocity.mc.thePlayer;
                thePlayer5.motionY *= 1.0;
                final EntityPlayerSP thePlayer6 = Velocity.mc.thePlayer;
                thePlayer6.motionZ *= 1.0;
            }
        }
        super.onClientTick(event);
    }
    
    @Override
    public void onLivingUpdate(final LivingEvent.LivingUpdateEvent ev) {
        if (Velocity.settingsManager.getSettingByName(this, "Mode").getMode().equals("Ghost") && Velocity.mc.thePlayer.maxHurtTime > 0 && Velocity.mc.thePlayer.hurtTime == Velocity.mc.thePlayer.maxHurtTime) {
            if (Velocity.settingsManager.getSettingByName(this, "Chance").getValue() != 100.0) {
                final double ch = Math.random();
                if (ch >= Velocity.settingsManager.getSettingByName(this, "Chance").getValue() / 100.0) {
                    return;
                }
            }
            if (Velocity.settingsManager.getSettingByName(this, "Horizontal").getValue() != 100.0) {
                final EntityPlayerSP thePlayer = Velocity.mc.thePlayer;
                thePlayer.motionX *= Velocity.settingsManager.getSettingByName(this, "Horizontal").getValue() / 100.0;
                final EntityPlayerSP thePlayer2 = Velocity.mc.thePlayer;
                thePlayer2.motionZ *= Velocity.settingsManager.getSettingByName(this, "Horizontal").getValue() / 100.0;
            }
            if (Velocity.settingsManager.getSettingByName(this, "Vertical").getValue() != 100.0) {
                final EntityPlayerSP thePlayer3 = Velocity.mc.thePlayer;
                thePlayer3.motionY *= Velocity.settingsManager.getSettingByName(this, "Vertical").getValue() / 100.0;
            }
        }
    }
    
    @Override
    public boolean onPacket(final Packet packet, final Connection.Side side) {
        if (packet instanceof S12PacketEntityVelocity && Velocity.settingsManager.getSettingByName(this, "Mode").getMode().equals("Cancel") && Wrapper.INSTANCE.player().hurtTime >= 0) {
            final S12PacketEntityVelocity p = (S12PacketEntityVelocity)packet;
            if (p.getEntityID() == Wrapper.INSTANCE.player().getEntityId()) {
                return false;
            }
        }
        return !(packet instanceof S12PacketEntityVelocity) || ((S12PacketEntityVelocity)packet).getEntityID() == Wrapper.INSTANCE.player().getEntityId() || true;
    }
}
