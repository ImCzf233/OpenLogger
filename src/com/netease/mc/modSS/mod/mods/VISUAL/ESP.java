package com.netease.mc.modSS.mod.mods.VISUAL;

import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.setting.*;
import dev.ss.world.event.mixinevents.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.*;
import java.awt.*;
import java.util.*;
import net.minecraftforge.client.event.*;
import net.minecraft.entity.player.*;
import com.netease.mc.modSS.utils.*;
import org.lwjgl.opengl.*;
import com.netease.mc.modSS.ui.*;
import com.netease.mc.modSS.utils.render.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.*;

public class ESP extends Mod
{
    public ESP() {
        super("ESP", "ESP", Category.VISUAL);
        this.addSetting(new Setting("Mode", this, "Shaded", new String[] { "2D", "Arrow", "Ring", "Shaded", "LiquidBounce", "Shell" }));
    }
    
    @Override
    public void onRender3D(final Event3D event) {
        for (final Object object : Utils.getEntityList()) {
            if (object instanceof EntityLivingBase && !(object instanceof EntityArmorStand)) {
                final EntityLivingBase en = (EntityLivingBase)object;
                final double x = Mappings.getValueDouble(RenderManager.class, ESP.mc.getRenderManager(), "renderPosX", "field_78725_b");
                final double y = Mappings.getValueDouble(RenderManager.class, ESP.mc.getRenderManager(), "renderPosY", "field_78726_c");
                final double z = Mappings.getValueDouble(RenderManager.class, ESP.mc.getRenderManager(), "renderPosZ", "field_78723_d");
                final double posX = en.lastTickPosX + (en.posX - en.lastTickPosX) * Wrapper.timer.renderPartialTicks - x;
                final double posY = en.lastTickPosY + (en.posY - en.lastTickPosY) * Wrapper.timer.renderPartialTicks - y;
                final double posZ = en.lastTickPosZ + (en.posZ - en.lastTickPosZ) * Wrapper.timer.renderPartialTicks - z;
                final String mode = ESP.settingsManager.getSettingByName(this, "Mode").getMode();
                switch (mode) {
                    case "2D": {
                        HUDUtils.ee((Entity)en, 3, 0.0, 1.0, 0, true);
                        continue;
                    }
                    case "Arrow": {
                        HUDUtils.ee((Entity)en, 5, 0.5, 1.0, 0, true);
                        continue;
                    }
                    case "Ring": {
                        HUDUtils.ee((Entity)en, 6, 0.5, 1.0, 0, true);
                        continue;
                    }
                    case "Shaded": {
                        HUDUtils.ee((Entity)en, 2, 0.0, 1.0, 0, true);
                        continue;
                    }
                    case "LiquidBounce": {
                        if (en.hurtTime > 0) {
                            RenderUtils.draw2D(en, posX, posY, posZ, Color.red.getRGB(), Color.black.getRGB());
                            continue;
                        }
                        RenderUtils.draw2D(en, posX, posY, posZ, Color.white.getRGB(), Color.black.getRGB());
                        continue;
                    }
                    case "Shell": {
                        RenderUtils.draw2DShell(en, posX, posY, posZ, Color.white.getRGB(), Color.black.getRGB());
                        continue;
                    }
                }
            }
        }
    }
    
    private void Do2DESP(final RenderWorldLastEvent event) {
        for (final EntityPlayer entity : ESP.mc.theWorld.playerEntities) {
            if (!ValidUtils.isValidEntity((EntityLivingBase)entity) && entity != ESP.mc.thePlayer) {
                GL11.glPushMatrix();
                GL11.glEnable(3042);
                GL11.glDisable(2929);
                GL11.glNormal3f(0.0f, 1.0f, 0.0f);
                GlStateManager.enableBlend();
                GL11.glBlendFunc(770, 771);
                GL11.glDisable(3553);
                GL11.glEnable(3042);
                final float partialTicks = HUDUtils.getTimer().renderPartialTicks;
                ESP.mc.getRenderManager();
                final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - ESP.mc.getRenderManager().viewerPosX;
                ESP.mc.getRenderManager();
                final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - ESP.mc.getRenderManager().viewerPosY;
                ESP.mc.getRenderManager();
                final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - ESP.mc.getRenderManager().viewerPosZ;
                final float DISTANCE = ESP.mc.thePlayer.getDistanceToEntity((Entity)entity);
                final float DISTANCE_SCALE = Math.min(DISTANCE * 0.15f, 0.15f);
                float SCALE = 0.035f;
                SCALE /= 2.0f;
                final float xMid = (float)x;
                final float yMid = (float)y + entity.height + 0.5f - (entity.isChild() ? (entity.height / 2.0f) : 0.0f);
                final float zMid = (float)z;
                GlStateManager.translate((float)x, (float)y + entity.height + 0.5f - (entity.isChild() ? (entity.height / 2.0f) : 0.0f), (float)z);
                GL11.glNormal3f(0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(-ESP.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                GL11.glScalef(-SCALE, -SCALE, -SCALE);
                final Tessellator tesselator = Tessellator.getInstance();
                final WorldRenderer worldRenderer = tesselator.getWorldRenderer();
                final float HEALTH = entity.getHealth();
                int COLOR = -1;
                if (HEALTH > 20.0) {
                    COLOR = -65292;
                }
                else if (HEALTH >= 10.0) {
                    COLOR = -16711936;
                }
                else if (HEALTH >= 3.0) {
                    COLOR = -23296;
                }
                else {
                    COLOR = -65536;
                }
                final Color gray = new Color(0, 0, 0);
                final double thickness = 1.5f + DISTANCE * 0.01f;
                final double xLeft = -30.0;
                final double xRight = 30.0;
                final double yUp = 15.0;
                final double yDown = 140.0;
                final double size = 10.0;
                GL11.glEnable(3042);
                RenderUtils.drawRect(-30.0f, 15.0f, 30.0f, 140.0f, Colors.getColor(255, 255, 255, 100));
                GL11.glEnable(3553);
                GL11.glEnable(2929);
                GlStateManager.disableBlend();
                GL11.glDisable(3042);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glNormal3f(1.0f, 1.0f, 1.0f);
                GL11.glPopMatrix();
            }
        }
    }
    
    public void outline(final Entity en) {
        final int list = GL11.glGenLists(1);
        final int renderColor = 0;
        Utils.checkSetupFBO();
        GL11.glPushMatrix();
        Mappings.setupCameraTransform(0);
        GL11.glDisable(2929);
        Stencil.getInstance().setBuffer(true);
        GL11.glNewList(list, 4864);
        GlStateManager.enableLighting();
        GL11.glEndList();
        GL11.glPolygonMode(1032, 6913);
        GL11.glCallList(list);
        GL11.glPolygonMode(1032, 6912);
        GL11.glCallList(list);
        Stencil.getInstance().setBuffer(false);
        GL11.glPolygonMode(1032, 6914);
        GL11.glCallList(list);
        Stencil.getInstance().cropInside();
        GL11.glPolygonMode(1032, 6913);
        GL11.glCallList(list);
        GL11.glPolygonMode(1032, 6912);
        GL11.glCallList(list);
        GL11.glPolygonMode(1032, 6914);
        GL11.glDeleteLists(list, 1);
        GL11.glPopMatrix();
        final double posX = en.lastTickPosX + (en.posX - en.lastTickPosX) * Wrapper.timer.renderPartialTicks;
        final double posY = en.lastTickPosY + (en.posY - en.lastTickPosY) * Wrapper.timer.renderPartialTicks;
        final double posZ = en.lastTickPosZ + (en.posZ - en.lastTickPosZ) * Wrapper.timer.renderPartialTicks;
        final Render entityRender = ESP.mc.getRenderManager().getEntityRenderObject(en);
        RenderUtils.glColor(renderColor);
        RenderUtils.pre3D();
        GL11.glLineWidth(5.0f);
        if (entityRender != null) {
            final float distance = ESP.mc.thePlayer.getDistanceToEntity(en);
            if (en instanceof EntityLivingBase) {
                GlStateManager.disableLighting();
                final double x = Mappings.getValueDouble(RenderManager.class, ESP.mc.getRenderManager(), "renderPosX", "field_78725_b");
                final double y = Mappings.getValueDouble(RenderManager.class, ESP.mc.getRenderManager(), "renderPosY", "field_78726_c");
                final double z = Mappings.getValueDouble(RenderManager.class, ESP.mc.getRenderManager(), "renderPosZ", "field_78723_d");
                entityRender.doRender(en, posX - x, posY - y, posZ - z, Wrapper.timer.renderPartialTicks, Wrapper.timer.renderPartialTicks);
                GlStateManager.enableLighting();
            }
        }
        RenderUtils.post3D();
        GL11.glEndList();
        GL11.glPolygonMode(1032, 6913);
        GL11.glCallList(list);
        GL11.glPolygonMode(1032, 6912);
        GL11.glCallList(list);
        Stencil.getInstance().setBuffer(false);
        GL11.glPolygonMode(1032, 6914);
        GL11.glCallList(list);
        Stencil.getInstance().cropInside();
        GL11.glPolygonMode(1032, 6913);
        GL11.glCallList(list);
        GL11.glPolygonMode(1032, 6912);
        GL11.glCallList(list);
        GL11.glPolygonMode(1032, 6914);
        GL11.glDeleteLists(list, 1);
        GL11.glPopMatrix();
        ESP.mc.entityRenderer.disableLightmap();
        RenderHelper.disableStandardItemLighting();
    }
    
    private void drawPlayerESP(final double d, final double d1, final double d2, final float angleToRotate, final float r, final float g, final float b) {
        final float x = System.currentTimeMillis() % 2000L / 1000.0f;
        final float red = 0.5f + 0.5f * MathHelper.sin(x * 3.1415927f);
        final float green = 0.5f + 0.5f * MathHelper.sin((x + 1.3333334f) * 3.1415927f);
        final float blue = 0.5f + 0.5f * MathHelper.sin((x + 2.6666667f) * 3.1415927f);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDepthMask(false);
        GL11.glEnable(2848);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2848);
        RenderUtils.drawOutlinedBoundingBox(new AxisAlignedBB(d - 0.5, d1 + 0.1, d2 - 0.5, d + 0.5, d1 + 2.0, d2 + 0.5));
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.2f);
        GL11.glDepthMask(true);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
    }
}
