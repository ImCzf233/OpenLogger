package com.netease.mc.modSS.mod.mods.VISUAL;

import com.netease.mc.modSS.mod.*;
import dev.ss.world.event.mixinevents.*;
import java.util.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.entity.*;
import com.netease.mc.modSS.managers.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import com.netease.mc.modSS.utils.*;
import java.awt.*;
import net.minecraft.client.entity.*;
import com.netease.mc.modSS.font.*;

public class KillerMark extends Mod
{
    public ArrayList<EntityLivingBase> targets;
    private EntityLivingBase target;
    
    public KillerMark() {
        super("KillerMark", "AUTO TAG killer", Category.VISUAL);
        this.targets = new ArrayList<EntityLivingBase>();
    }
    
    @Override
    public void onRender3D(final Event3D event) {
        for (final EntityLivingBase entity : this.targets) {
            final EntityLivingBase var = entity;
            final RenderManager renderManager = Wrapper.INSTANCE.mc().getRenderManager();
            final double renderPosX = renderManager.viewerPosX;
            final double renderPosY = renderManager.viewerPosY;
            final double renderPosZ = renderManager.viewerPosZ;
            final double xPos = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * Wrapper.timer.renderPartialTicks - renderPosX;
            final double yPos = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * Wrapper.timer.renderPartialTicks - renderPosY;
            final double zPos = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * Wrapper.timer.renderPartialTicks - renderPosZ;
            this.renderkiller(var, "Killer", xPos, yPos, zPos);
        }
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        this.TargetUpdate();
        super.onClientTick(event);
    }
    
    void TargetUpdate() {
        if (Utils.getEntityList() == null) {
            return;
        }
        for (final Object object : Utils.getEntityList()) {
            if (object == null) {
                return;
            }
            if (!(object instanceof EntityLivingBase)) {
                continue;
            }
            final EntityLivingBase entity = (EntityLivingBase)object;
            if (!(entity instanceof EntityPlayer) || entity.getHeldItem() == null || entity.getHeldItem().getItem() == null || !(entity.getHeldItem().getItem() instanceof ItemSword) || entity.getName() == Wrapper.INSTANCE.player().getName()) {
                continue;
            }
            this.target = entity;
        }
        if (this.target != null && !this.targets.contains(this.target)) {
            this.targets.add(this.target);
            Wrapper.modmsg(this, "Add " + this.target.getName() + " to killer list");
            this.target = null;
        }
    }
    
    @Override
    public void onEnable() {
        this.targets.clear();
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        this.targets.clear();
        super.onDisable();
    }
    
    private void renderkiller(final EntityLivingBase entity, final String tag, final double x, double y, final double z) {
        final EntityPlayerSP player = Wrapper.INSTANCE.player();
        final CFontRenderer fontRenderer = FontManager.default25;
        y += (entity.isSneaking() ? 0.5 : 0.7);
        float distance = player.getDistanceToEntity((Entity)entity) / 4.0f;
        if (distance < 1.6f) {
            distance = 1.6f;
        }
        if (entity instanceof EntityPlayer) {
            final EntityPlayer entityPlayer = (EntityPlayer)entity;
            Utils.getPlayerName(entityPlayer);
        }
        final RenderManager renderManager = Wrapper.INSTANCE.mc().getRenderManager();
        float scale = distance;
        scale /= 30.0f;
        scale *= 0.3;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y + 1.4f, (float)z);
        GL11.glNormal3f(1.0f, 1.0f, 1.0f);
        GL11.glRotatef(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        GL11.glScalef(-scale, -scale, scale);
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        final Tessellator var14 = Tessellator.getInstance();
        final int width = fontRenderer.getStringWidth(tag) / 2;
        FontManager.default25.drawString(tag, MathUtils.getMiddle(-width - 2, width + 2) - width, -(fontRenderer.getHeight() - 1), Color.RED.getRGB());
        GL11.glPushMatrix();
        GL11.glPopMatrix();
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
}
