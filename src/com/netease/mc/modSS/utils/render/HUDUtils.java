package com.netease.mc.modSS.utils.render;

import net.minecraft.client.*;
import java.lang.reflect.*;
import java.awt.*;
import org.lwjgl.opengl.*;
import net.minecraft.util.*;
import net.minecraft.entity.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.gui.*;

public class HUDUtils
{
    private static final Minecraft mc;
    private static Field timerField;
    
    public static void su() {
        try {
            HUDUtils.timerField = Minecraft.class.getDeclaredField("field_71428_T");
        }
        catch (Exception var4) {
            try {
                HUDUtils.timerField = Minecraft.class.getDeclaredField("timer");
            }
            catch (Exception ex) {}
        }
        if (HUDUtils.timerField != null) {
            HUDUtils.timerField.setAccessible(true);
        }
    }
    
    public static Timer getTimer() {
        try {
            su();
            return (Timer)HUDUtils.timerField.get(HUDUtils.mc);
        }
        catch (IndexOutOfBoundsException | IllegalAccessException ex2) {
            final Exception ex;
            final Exception var1 = ex;
            return null;
        }
    }
    
    public static int rainbowDraw(final long speed, final long... delay) {
        final long time = System.currentTimeMillis() + ((delay.length > 0) ? delay[0] : 0L);
        return Color.getHSBColor(time % (15000L / speed) / (15000.0f / speed), 1.0f, 1.0f).getRGB();
    }
    
    public static int astolfoColorsDraw(final int yOffset, final int yTotal, final float speed) {
        float hue;
        for (hue = System.currentTimeMillis() % (int)speed + (yTotal - yOffset) * 9; hue > speed; hue -= speed) {}
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5f - (hue - 0.5f);
        }
        hue += 0.5f;
        return Color.HSBtoRGB(hue, 0.5f, 1.0f);
    }
    
    public static int astolfoColorsDraw(final int yOffset, final int yTotal) {
        return astolfoColorsDraw(yOffset, yTotal, 2900.0f);
    }
    
    public static void re(final BlockPos bp, final int color, final boolean shade) {
        if (bp != null) {
            final double x = bp.func_177958_n() - HUDUtils.mc.getRenderManager().viewerPosX;
            final double y = bp.func_177956_o() - HUDUtils.mc.getRenderManager().viewerPosY;
            final double z = bp.func_177952_p() - HUDUtils.mc.getRenderManager().viewerPosZ;
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(3042);
            GL11.glLineWidth(2.0f);
            GL11.glDisable(3553);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            final float a = (color >> 24 & 0xFF) / 255.0f;
            final float r = (color >> 16 & 0xFF) / 255.0f;
            final float g = (color >> 8 & 0xFF) / 255.0f;
            final float b = (color & 0xFF) / 255.0f;
            GL11.glColor4d((double)r, (double)g, (double)b, (double)a);
            RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
            if (shade) {
                dbb(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), r, g, b);
            }
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
        }
    }
    
    public static void ee(final Entity e, final int type, final double expand, final double shift, int color, final boolean damage) {
        if (ValidUtils.isValidEntity((EntityLivingBase)e) || e == Wrapper.INSTANCE.player() || ValidUtils.isBot((EntityLivingBase)e) || !ValidUtils.isTeam((EntityLivingBase)e)) {
            return;
        }
        if (e instanceof EntityLivingBase) {
            final double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * getTimer().renderPartialTicks - HUDUtils.mc.getRenderManager().viewerPosX;
            final double y = e.lastTickPosY + (e.posY - e.lastTickPosY) * getTimer().renderPartialTicks - HUDUtils.mc.getRenderManager().viewerPosY;
            final double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * getTimer().renderPartialTicks - HUDUtils.mc.getRenderManager().viewerPosZ;
            final float d = (float)expand / 40.0f;
            if (e instanceof EntityPlayer && damage && ((EntityPlayer)e).hurtTime != 0) {
                color = Color.RED.getRGB();
            }
            GlStateManager.pushMatrix();
            if (type == 3) {
                GL11.glTranslated(x, y - 0.2, z);
                GL11.glRotated((double)(-HUDUtils.mc.getRenderManager().playerViewY), 0.0, 1.0, 0.0);
                GlStateManager.disableDepth();
                GL11.glScalef(0.03f + d, 0.03f + d, 0.03f + d);
                final int outline = Color.black.getRGB();
                Gui.drawRect(-20, -1, -26, 75, outline);
                Gui.drawRect(20, -1, 26, 75, outline);
                Gui.drawRect(-20, -1, 21, 5, outline);
                Gui.drawRect(-20, 70, 21, 75, outline);
                if (color != 0) {
                    Gui.drawRect(-21, 0, -25, 74, color);
                    Gui.drawRect(21, 0, 25, 74, color);
                    Gui.drawRect(-21, 0, 24, 4, color);
                    Gui.drawRect(-21, 71, 25, 74, color);
                }
                else {
                    final int st = rainbowDraw(2L, 0L);
                    final int en = rainbowDraw(2L, 1000L);
                    dGR(-21, 0, -25, 74, st, en);
                    dGR(21, 0, 25, 74, st, en);
                    Gui.drawRect(-21, 0, 21, 4, en);
                    Gui.drawRect(-21, 71, 21, 74, st);
                }
                GlStateManager.enableDepth();
            }
            else if (type == 4) {
                final EntityLivingBase en2 = (EntityLivingBase)e;
                final double r = en2.getHealth() / en2.getMaxHealth();
                final int b = (int)(74.0 * r);
                final int hc = (r < 0.3) ? Color.red.getRGB() : ((r < 0.5) ? Color.orange.getRGB() : ((r < 0.7) ? Color.yellow.getRGB() : Color.green.getRGB()));
                GL11.glTranslated(x, y - 0.2, z);
                GL11.glRotated((double)(-HUDUtils.mc.getRenderManager().playerViewY), 0.0, 1.0, 0.0);
                GlStateManager.disableDepth();
                GL11.glScalef(0.03f + d, 0.03f + d, 0.03f + d);
                final int i = (int)(21.0 + shift * 2.0);
                Gui.drawRect(i, -1, i + 5, 75, Color.black.getRGB());
                Gui.drawRect(i + 1, b, i + 4, 74, Color.DARK_GRAY.getRGB());
                Gui.drawRect(i + 1, 0, i + 4, b, hc);
                GlStateManager.enableDepth();
            }
            else if (type == 6) {
                d3p(x, y, z, 0.699999988079071, 45, 1.5f, color, color == 0);
            }
            else {
                if (color == 0) {
                    color = rainbowDraw(2L, 0L);
                }
                final float a = (color >> 24 & 0xFF) / 255.0f;
                final float r2 = (color >> 16 & 0xFF) / 255.0f;
                final float g = (color >> 8 & 0xFF) / 255.0f;
                final float b2 = (color & 0xFF) / 255.0f;
                if (type == 5) {
                    GL11.glTranslated(x, y - 0.2, z);
                    GL11.glRotated((double)(-HUDUtils.mc.getRenderManager().playerViewY), 0.0, 1.0, 0.0);
                    GlStateManager.disableDepth();
                    GL11.glScalef(0.03f + d, 0.03f, 0.03f + d);
                    final int base = 1;
                    d2p(0.0, 95.0, 10, 3, Color.black.getRGB());
                    for (int i = 0; i < 6; ++i) {
                        d2p(0.0, 95 + (10 - i), 3, 4, Color.black.getRGB());
                    }
                    for (int i = 0; i < 7; ++i) {
                        d2p(0.0, 95 + (10 - i), 2, 4, color);
                    }
                    d2p(0.0, 95.0, 8, 3, color);
                    GlStateManager.enableDepth();
                }
                else {
                    final AxisAlignedBB bbox = e.getEntityBoundingBox().expand(0.1 + expand, 0.1 + expand, 0.1 + expand);
                    final AxisAlignedBB axis = new AxisAlignedBB(bbox.minX - e.posX + x, bbox.minY - e.posY + y, bbox.minZ - e.posZ + z, bbox.maxX - e.posX + x, bbox.maxY - e.posY + y, bbox.maxZ - e.posZ + z);
                    GL11.glBlendFunc(770, 771);
                    GL11.glEnable(3042);
                    GL11.glDisable(3553);
                    GL11.glDisable(2929);
                    GL11.glDepthMask(false);
                    GL11.glLineWidth(2.0f);
                    GL11.glColor4f(r2, g, b2, a);
                    if (type == 1) {
                        RenderGlobal.drawSelectionBoundingBox(axis);
                    }
                    else if (type == 2) {
                        dbb(axis, r2, g, b2);
                    }
                    GL11.glEnable(3553);
                    GL11.glEnable(2929);
                    GL11.glDepthMask(true);
                    GL11.glDisable(3042);
                }
            }
            GlStateManager.popMatrix();
        }
    }
    
    public static void dbb(final AxisAlignedBB abb, final float r, final float g, final float b) {
        final float a = 0.25f;
        final Tessellator ts = Tessellator.getInstance();
        final WorldRenderer vb = ts.getWorldRenderer();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        ts.draw();
    }
    
    public static void dtl(final Entity e, final int color, final float lw) {
        if (e != null) {
            final double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * getTimer().renderPartialTicks - HUDUtils.mc.getRenderManager().viewerPosX;
            final double y = e.getEyeHeight() + e.lastTickPosY + (e.posY - e.lastTickPosY) * getTimer().renderPartialTicks - HUDUtils.mc.getRenderManager().viewerPosY;
            final double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * getTimer().renderPartialTicks - HUDUtils.mc.getRenderManager().viewerPosZ;
            final float a = (color >> 24 & 0xFF) / 255.0f;
            final float r = (color >> 16 & 0xFF) / 255.0f;
            final float g = (color >> 8 & 0xFF) / 255.0f;
            final float b = (color & 0xFF) / 255.0f;
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            GL11.glEnable(2848);
            GL11.glDisable(2929);
            GL11.glDisable(3553);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(3042);
            GL11.glLineWidth(lw);
            GL11.glColor4f(r, g, b, a);
            GL11.glBegin(2);
            GL11.glVertex3d(0.0, (double)HUDUtils.mc.thePlayer.getEyeHeight(), 0.0);
            GL11.glVertex3d(x, y, z);
            GL11.glEnd();
            GL11.glDisable(3042);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDisable(2848);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
        }
    }
    
    public static void dGR(int left, int top, int right, int bottom, final int startColor, final int endColor) {
        if (left < right) {
            final int j = left;
            left = right;
            right = j;
        }
        if (top < bottom) {
            final int j = top;
            top = bottom;
            bottom = j;
        }
        final float f = (startColor >> 24 & 0xFF) / 255.0f;
        final float f2 = (startColor >> 16 & 0xFF) / 255.0f;
        final float f3 = (startColor >> 8 & 0xFF) / 255.0f;
        final float f4 = (startColor & 0xFF) / 255.0f;
        final float f5 = (endColor >> 24 & 0xFF) / 255.0f;
        final float f6 = (endColor >> 16 & 0xFF) / 255.0f;
        final float f7 = (endColor >> 8 & 0xFF) / 255.0f;
        final float f8 = (endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos((double)right, (double)top, 0.0).color(f2, f3, f4, f).endVertex();
        worldrenderer.pos((double)left, (double)top, 0.0).color(f2, f3, f4, f).endVertex();
        worldrenderer.pos((double)left, (double)bottom, 0.0).color(f6, f7, f8, f5).endVertex();
        worldrenderer.pos((double)right, (double)bottom, 0.0).color(f6, f7, f8, f5).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    
    public static void db(final int w, final int h, final int r) {
        final int c = (r == -1) ? -1089466352 : r;
        Gui.drawRect(0, 0, w, h, c);
    }
    
    public static void drawColouredText(final String text, final char lineSplit, int leftOffset, int topOffset, final long colourParam1, final long shift, final boolean rect, final FontRenderer fontRenderer) {
        final int bX = leftOffset;
        int l = 0;
        long colourControl = 0L;
        for (int i = 0; i < text.length(); ++i) {
            final char c = text.charAt(i);
            if (c == lineSplit) {
                ++l;
                leftOffset = bX;
                topOffset += fontRenderer.FONT_HEIGHT + 5;
                colourControl = shift * l;
            }
            else {
                fontRenderer.drawString(String.valueOf(c), (float)leftOffset, (float)topOffset, astolfoColorsDraw((int)colourParam1, (int)colourControl), rect);
                leftOffset += fontRenderer.getCharWidth(c);
                if (c != ' ') {
                    colourControl -= 90L;
                }
            }
        }
    }
    
    public static PositionMode getPostitionMode(final int marginX, final int marginY, final double height, final double width) {
        final int halfHeight = (int)(height / 4.0);
        final int halfWidth = (int)(width / 1.0);
        PositionMode positionMode = null;
        if (marginY < halfHeight) {
            if (marginX < halfWidth) {
                positionMode = PositionMode.UPLEFT;
            }
            if (marginX > halfWidth) {
                positionMode = PositionMode.UPRIGHT;
            }
        }
        if (marginY > halfHeight) {
            if (marginX < halfWidth) {
                positionMode = PositionMode.DOWNLEFT;
            }
            if (marginX > halfWidth) {
                positionMode = PositionMode.DOWNRIGHT;
            }
        }
        return positionMode;
    }
    
    public static void d2p(final double x, final double y, final int radius, final int sides, final int color) {
        final float a = (color >> 24 & 0xFF) / 255.0f;
        final float r = (color >> 16 & 0xFF) / 255.0f;
        final float g = (color >> 8 & 0xFF) / 255.0f;
        final float b = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(r, g, b, a);
        worldrenderer.begin(6, DefaultVertexFormats.POSITION);
        for (int i = 0; i < sides; ++i) {
            final double angle = 6.283185307179586 * i / sides + Math.toRadians(180.0);
            worldrenderer.pos(x + Math.sin(angle) * radius, y + Math.cos(angle) * radius, 0.0).endVertex();
        }
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void d3p(final double x, final double y, final double z, final double radius, final int sides, final float lineWidth, final int color, final boolean chroma) {
        final float a = (color >> 24 & 0xFF) / 255.0f;
        final float r = (color >> 16 & 0xFF) / 255.0f;
        final float g = (color >> 8 & 0xFF) / 255.0f;
        final float b = (color & 0xFF) / 255.0f;
        HUDUtils.mc.entityRenderer.disableLightmap();
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glEnable(2848);
        GL11.glDepthMask(false);
        GL11.glLineWidth(lineWidth);
        if (!chroma) {
            GL11.glColor4f(r, g, b, a);
        }
        GL11.glBegin(1);
        long d = 0L;
        final long ed = 15000L / sides;
        final long hed = ed / 2L;
        for (int i = 0; i < sides * 2; ++i) {
            if (chroma) {
                if (i % 2 != 0) {
                    if (i == 47) {
                        d = hed;
                    }
                    d += ed;
                }
                final int c = rainbowDraw(2L, d);
                final float r2 = (c >> 16 & 0xFF) / 255.0f;
                final float g2 = (c >> 8 & 0xFF) / 255.0f;
                final float b2 = (c & 0xFF) / 255.0f;
                GL11.glColor3f(r2, g2, b2);
            }
            final double angle = 6.283185307179586 * i / sides + Math.toRadians(180.0);
            GL11.glVertex3d(x + Math.cos(angle) * radius, y, z + Math.sin(angle) * radius);
        }
        GL11.glEnd();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDepthMask(true);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        HUDUtils.mc.entityRenderer.enableLightmap();
    }
    
    static {
        mc = Minecraft.getMinecraft();
        HUDUtils.timerField = null;
    }
    
    public enum PositionMode
    {
        UPLEFT, 
        UPRIGHT, 
        DOWNLEFT, 
        DOWNRIGHT;
    }
}
