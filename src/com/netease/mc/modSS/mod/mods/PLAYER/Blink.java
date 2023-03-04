package com.netease.mc.modSS.mod.mods.PLAYER;

import net.minecraft.network.play.client.*;
import net.minecraft.client.entity.*;
import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.mod.*;
import java.util.*;
import net.minecraft.network.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import dev.ss.world.event.mixinevents.*;
import java.awt.*;
import com.netease.mc.modSS.utils.*;
import org.lwjgl.opengl.*;
import com.netease.mc.modSS.managers.*;
import com.netease.mc.modSS.ui.*;

public class Blink extends Mod
{
    Queue<C03PacketPlayer> packets;
    boolean send;
    private EntityOtherPlayerMP blinkEntity;
    public Setting showplayer;
    public Setting showmode;
    
    public Blink() {
        super("Blink", "Lags you serverside", Category.PLAYER);
        this.packets = new LinkedList<C03PacketPlayer>();
        this.send = false;
        this.showplayer = new Setting("ShowPlayer", this, true);
        this.showmode = new Setting("ShowMode", this, "Render", new String[] { "Render", "Chat", "None" });
        this.addSetting(this.showplayer);
        this.addSetting(this.showmode);
    }
    
    @Override
    public boolean onPacket(final Packet packet, final Connection.Side side) {
        if (side == Connection.Side.OUT && packet instanceof C03PacketPlayer) {
            if (this.showmode.isMode("Chat")) {
                Wrapper.message("Packets:" + String.valueOf(this.packets.size()));
            }
            this.send = false;
            this.packets.add((C03PacketPlayer)packet);
            return this.send;
        }
        return this.send = true;
    }
    
    @Override
    public void onEnable() {
        if (this.showplayer.isEnabled()) {
            (this.blinkEntity = new EntityOtherPlayerMP((World)Blink.mc.theWorld, Blink.mc.thePlayer.getGameProfile())).setPositionAndRotation(Blink.mc.thePlayer.posX, Blink.mc.thePlayer.posY, Blink.mc.thePlayer.posZ, Blink.mc.thePlayer.rotationYaw, Blink.mc.thePlayer.rotationPitch);
            this.blinkEntity.rotationYawHead = Blink.mc.thePlayer.rotationYawHead;
            this.blinkEntity.setSprinting(Blink.mc.thePlayer.isSprinting());
            this.blinkEntity.setInvisible(Blink.mc.thePlayer.isInvisible());
            this.blinkEntity.setSneaking(Blink.mc.thePlayer.isSneaking());
            Blink.mc.theWorld.addEntityToWorld(this.blinkEntity.getEntityId(), (Entity)this.blinkEntity);
        }
        super.onEnable();
    }
    
    @Override
    public void onRender2D(final Event2D event) {
        if (this.showmode.isMode("Render")) {
            RenderUtils.roundedRect(Utils.getScaledRes().getScaledWidth() / 2.0f - 15.0f, Utils.getScaledRes().getScaledHeight() / 2, 30.0, 30.0, 6.0, new Color(0, 0, 0, 80));
            GL11.glEnable(3042);
            FontManager.default16.drawStringWithShadow("" + this.packets.size(), Utils.getScaledRes().getScaledWidth() / 2.0f - FontManager.default16.getStringWidth("" + this.packets.size()) + 4.0f, Utils.getScaledRes().getScaledHeight() / 2 + 10, Colors.getColor(Color.white));
            GL11.glDisable(3042);
        }
    }
    
    @Override
    public void onDisable() {
        while (!this.packets.isEmpty()) {
            Wrapper.INSTANCE.sendPacket((Packet)this.packets.poll());
        }
        this.packets.clear();
        if (this.blinkEntity != null) {
            Blink.mc.theWorld.removeEntityFromWorld(this.blinkEntity.getEntityId());
        }
        super.onDisable();
    }
}
