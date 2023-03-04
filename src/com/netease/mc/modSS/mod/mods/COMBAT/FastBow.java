package com.netease.mc.modSS.mod.mods.COMBAT;

import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.utils.*;
import com.netease.mc.modSS.mod.*;
import dev.ss.world.event.mixinevents.*;
import net.minecraft.item.*;
import net.minecraft.network.play.server.*;
import net.minecraftforge.fml.relauncher.*;
import net.minecraft.network.*;
import net.minecraftforge.fml.common.gameevent.*;
import com.netease.mc.modSS.utils.player.*;
import net.minecraft.client.*;
import net.minecraft.network.play.client.*;
import net.minecraft.util.*;

public class FastBow extends Mod
{
    public Setting mode;
    TimerUtils timer;
    
    public FastBow() {
        super("FastBow", "", Category.COMBAT);
        this.mode = new Setting("Mode", this, "Basic", new String[] { "Basic", "Guardian" });
        this.timer = new TimerUtils();
        this.addSetting(this.mode);
    }
    
    @Override
    public void onPacketEvent(final EventPacket event) {
        if (FastBow.mc.thePlayer.inventory.getCurrentItem() != null && this.mode.isMode("Guardian") && FastBow.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBow) {
            final Packet p = event.getPacket();
            if (p instanceof S08PacketPlayerPosLook) {
                final S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook)event.getPacket();
                ReflectionHelper.setPrivateValue((Class)S08PacketPlayerPosLook.class, (Object)packet, (Object)FastBow.mc.thePlayer.rotationYaw, new String[] { "yaw", "field_148936_d" });
                ReflectionHelper.setPrivateValue((Class)S08PacketPlayerPosLook.class, (Object)packet, (Object)FastBow.mc.thePlayer.rotationPitch, new String[] { "pitch", "field_148937_e" });
            }
        }
        super.onPacketEvent(event);
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START && FastBow.mc.thePlayer.isUsingItem() && FastBow.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBow) {
            if (this.mode.isMode("Guardian")) {
                if (FastBow.mc.thePlayer.onGround && FastBow.mc.thePlayer.isCollidedVertically && MoveUtils.isOnGround(1.0E-4)) {
                    ReflectionHelper.setPrivateValue((Class)Minecraft.class, (Object)FastBow.mc, (Object)new Integer(0), new String[] { "rightClickDelayTimer", "field_71467_ac" });
                    if (this.timer.delay(500.0f)) {
                        final double offset = 16.0;
                        for (int i = 0; i < 11; ++i) {
                            FastBow.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(FastBow.mc.thePlayer.posX, FastBow.mc.thePlayer.posY + offset, FastBow.mc.thePlayer.posZ, true));
                            FastBow.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(FastBow.mc.thePlayer.posX, FastBow.mc.thePlayer.posY, FastBow.mc.thePlayer.posZ, true));
                        }
                        this.timer.reset();
                    }
                }
            }
            else if (this.mode.isMode("Basic")) {
                ReflectionHelper.setPrivateValue((Class)Minecraft.class, (Object)FastBow.mc, (Object)new Integer(0), new String[] { "rightClickDelayTimer", "field_71467_ac" });
                for (int j = 0; j < 20; ++j) {
                    FastBow.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer(true));
                }
            }
            FastBow.mc.getNetHandler().addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            FastBow.mc.thePlayer.stopUsingItem();
        }
        super.onClientTick(event);
    }
}
