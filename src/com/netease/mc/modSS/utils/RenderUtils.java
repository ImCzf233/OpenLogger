package com.netease.mc.modSS.utils;

import net.minecraft.client.*;
import org.lwjgl.opengl.*;
import java.awt.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.block.*;
import java.util.*;
import net.minecraft.entity.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.enchantment.*;
import com.netease.mc.modSS.ui.*;
import net.minecraft.item.*;
import com.netease.mc.modSS.managers.*;
import com.netease.mc.modSS.font.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.network.*;
import net.minecraft.util.*;
import net.minecraft.tileentity.*;
import net.minecraft.client.renderer.*;
import net.minecraftforge.fml.relauncher.*;
import java.lang.reflect.*;
import com.netease.mc.modSS.utils.render.*;

public class RenderUtils
{
    private static final Minecraft mc;
    
    public static void rect(final float x1, final float y1, final float x2, final float y2, final int fill) {
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        final float f = (fill >> 24 & 0xFF) / 255.0f;
        final float f2 = (fill >> 16 & 0xFF) / 255.0f;
        final float f3 = (fill >> 8 & 0xFF) / 255.0f;
        final float f4 = (fill & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(7);
        GL11.glVertex2d((double)x2, (double)y1);
        GL11.glVertex2d((double)x1, (double)y1);
        GL11.glVertex2d((double)x1, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    public static void rect(final double x1, final double y1, final double x2, final double y2, final int fill) {
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        final float f = (fill >> 24 & 0xFF) / 255.0f;
        final float f2 = (fill >> 16 & 0xFF) / 255.0f;
        final float f3 = (fill >> 8 & 0xFF) / 255.0f;
        final float f4 = (fill & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(7);
        GL11.glVertex2d(x2, y1);
        GL11.glVertex2d(x1, y1);
        GL11.glVertex2d(x1, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    public static void drawRect(float left, float top, float right, float bottom, final int color) {
        if (left < right) {
            final float var5 = left;
            left = right;
            right = var5;
        }
        if (top < bottom) {
            final float var5 = top;
            top = bottom;
            bottom = var5;
        }
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GLUtils.glColor(color);
        GL11.glBegin(7);
        GL11.glVertex2d((double)left, (double)bottom);
        GL11.glVertex2d((double)right, (double)bottom);
        GL11.glVertex2d((double)right, (double)top);
        GL11.glVertex2d((double)left, (double)top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
    }
    
    public static void startClip(final float x1, float y1, final float x2, float y2) {
        if (y1 > y2) {
            final float temp = y2;
            y2 = y1;
            y1 = temp;
        }
        GL11.glScissor((int)x1, (int)(Display.getHeight() - y2), (int)(x2 - x1), (int)(y2 - y1));
        GL11.glEnable(3089);
    }
    
    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }
    
    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
    
    public static void endClip() {
        GL11.glDisable(3089);
    }
    
    public static void renderOutlines(final double x, double y, final double z, final float width, final float height, final Color c) {
        final float halfwidth = width / 2.0f;
        final float halfheight = height / 2.0f;
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(false);
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        GlStateManager.disableDepth();
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION_COLOR);
        ++y;
        GL11.glLineWidth(1.2f);
        worldRenderer.pos(x - halfwidth, y - halfheight, z - halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x - halfwidth, y + halfheight, z - halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x + halfwidth, y - halfheight, z + halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x + halfwidth, y + halfheight, z + halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x + halfwidth, y - halfheight, z - halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x + halfwidth, y + halfheight, z - halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x - halfwidth, y - halfheight, z + halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x - halfwidth, y + halfheight, z + halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x - halfwidth, y - halfheight, z - halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x - halfwidth, y - halfheight, z + halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x - halfwidth, y - halfheight, z - halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x + halfwidth, y - halfheight, z - halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x + halfwidth, y - halfheight, z - halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x + halfwidth, y - halfheight, z + halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x - halfwidth, y - halfheight, z + halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x + halfwidth, y - halfheight, z + halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x - halfwidth, y + halfheight, z - halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x - halfwidth, y + halfheight, z + halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x - halfwidth, y + halfheight, z - halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x + halfwidth, y + halfheight, z - halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x + halfwidth, y + halfheight, z - halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x + halfwidth, y + halfheight, z + halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x - halfwidth, y + halfheight, z + halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x + halfwidth, y + halfheight, z + halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        tessellator.draw();
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.enableBlend();
        GlStateManager.popMatrix();
    }
    
    public static void renderBoxWithOutline(final double x, final double y, final double z, final float width, final float height, final Color c) {
        renderBox(x, y, z, width, height, c);
        renderOutlines(x, y, z, width, height, c);
    }
    
    public static void draw2DPlayerESP(final EntityPlayer ep, final double d, final double d1, final double d2) {
        final float distance = Minecraft.getMinecraft().thePlayer.getDistanceToEntity((Entity)ep);
        final float scale = (float)(0.09 + Minecraft.getMinecraft().thePlayer.getDistance(ep.posX, ep.posY, ep.posZ) / 10000.0);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glScalef(-scale, -scale, scale);
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glScaled(0.5, 0.5, 0.5);
        drawOutlineRect(-13.0f, -45.0f, 13.0f, 5.0f, -65536);
        GL11.glScaled(2.0, 2.0, 2.0);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glEnable(2896);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
    
    public static void drawOutlineRect(final float drawX, final float drawY, final float drawWidth, final float drawHeight, final int color) {
        rect(drawX, drawY, drawWidth, drawY + 0.5f, color);
        rect(drawX, drawY + 0.5f, drawX + 0.5f, drawHeight, color);
        rect(drawWidth - 0.5f, drawY + 0.5f, drawWidth, drawHeight - 0.5f, color);
        rect(drawX + 0.5f, drawHeight - 0.5f, drawWidth, drawHeight, color);
    }
    
    public static void drawVLine(final float x2, float y2, float x1, final int y1) {
        if (x1 < y2) {
            final float var5 = y2;
            y2 = x1;
            x1 = var5;
        }
        rect(x2, y2 + 1.0f, x2 + 1.0f, x1, y1);
    }
    
    public static void drawHLine(float x2, float y2, final float x1, final int y1) {
        if (y2 < x2) {
            final float var5 = x2;
            x2 = y2;
            y2 = var5;
        }
        rect(x2, x1, y2 + 1.0f, x1 + 1.0f, y1);
    }
    
    public static void drawBorderedRect(float x2, float y2, float x1, float y1, final int insideC, final int borderC) {
        enableGL2D();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        drawVLine(x2 *= 2.0f, y2 *= 2.0f, y1 *= 2.0f, borderC);
        drawVLine((x1 *= 2.0f) - 1.0f, y2, y1, borderC);
        drawHLine(x2, x1 - 1.0f, y2, borderC);
        drawHLine(x2, x1 - 2.0f, y1 - 1.0f, borderC);
        rect(x2 + 1.0f, y2 + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        disableGL2D();
    }
    
    public static void drawBorderedRect(final float x, final float y, final float x2, final float y2, final float l1, final int col1, final int col2) {
        drawRect(x, y, x2, y2, col2);
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    public static void rectBorder(final float x1, final float y1, final float x2, final float y2, final int outline) {
        rect(x1 + 1.0f, y2 - 1.0f, x2, y2, outline);
        rect(x1 + 1.0f, y1, x2, y1 + 1.0f, outline);
        rect(x1, y1, x1 + 1.0f, y2, outline);
        rect(x2 - 1.0f, y1 + 1.0f, x2, y2 - 1.0f, outline);
    }
    
    public static void renderBox(final double x, double y, final double z, final float width, final float height, final Color c) {
        final float halfwidth = width / 2.0f;
        final float halfheight = height / 2.0f;
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(false);
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        GlStateManager.disableDepth();
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        ++y;
        worldRenderer.pos(x - halfwidth, y - halfheight, z + halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x - halfwidth, y + halfheight, z + halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x + halfwidth, y + halfheight, z + halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x + halfwidth, y - halfheight, z + halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x - halfwidth, y - halfheight, z - halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x - halfwidth, y + halfheight, z - halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x + halfwidth, y + halfheight, z - halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x + halfwidth, y - halfheight, z - halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x - halfwidth, y - halfheight, z - halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x - halfwidth, y + halfheight, z - halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x - halfwidth, y + halfheight, z + halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x - halfwidth, y - halfheight, z + halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x + halfwidth, y - halfheight, z - halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x + halfwidth, y + halfheight, z - halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x + halfwidth, y + halfheight, z + halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x + halfwidth, y - halfheight, z + halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x + halfwidth, y + halfheight, z - halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x + halfwidth, y + halfheight, z + halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x - halfwidth, y + halfheight, z + halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x - halfwidth, y + halfheight, z - halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x + halfwidth, y - halfheight, z - halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x + halfwidth, y - halfheight, z + halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x - halfwidth, y - halfheight, z + halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        worldRenderer.pos(x - halfwidth, y - halfheight, z - halfwidth).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        tessellator.draw();
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.enableBlend();
        GlStateManager.popMatrix();
    }
    
    public static void drawOutlineForEntity(final Entity e, final AxisAlignedBB axisalignedbb, final float width, final float red, final float green, final float blue, final float alpha) {
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(width);
        GL11.glColor4f(red, green, blue, alpha);
        drawOutlinedBox(axisalignedbb);
        GL11.glLineWidth(1.0f);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }
    
    public static void drawBorderedCircle(int x, int y, float radius, final int outsideC, final int insideC) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        final float scale = 0.1f;
        GL11.glScalef(scale, scale, scale);
        x *= (int)(1.0f / scale);
        y *= (int)(1.0f / scale);
        radius *= 1.0f / scale;
        drawCircle(x, y, radius, insideC);
        drawUnfilledCircle(x, y, radius, 1.0f, outsideC);
        GL11.glScalef(1.0f / scale, 1.0f / scale, 1.0f / scale);
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    public static void drawUnfilledCircle(final int x, final int y, final float radius, final float lineWidth, final int color) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glLineWidth(lineWidth);
        GL11.glEnable(2848);
        GL11.glBegin(2);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d(x + Math.sin(i * 3.141526 / 180.0) * radius, y + Math.cos(i * 3.141526 / 180.0) * radius);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
    }
    
    public static void drawCircle(final int x, final int y, final float radius, final int color) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(9);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d(x + Math.sin(i * 3.141526 / 180.0) * radius, y + Math.cos(i * 3.141526 / 180.0) * radius);
        }
        GL11.glEnd();
    }
    
    public static void drawFilledBBESP(final AxisAlignedBB axisalignedbb, final int color) {
        GL11.glPushMatrix();
        final float red = (color >> 24 & 0xFF) / 255.0f;
        final float green = (color >> 16 & 0xFF) / 255.0f;
        final float blue = (color >> 8 & 0xFF) / 255.0f;
        final float alpha = (color & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        drawFilledBox(axisalignedbb);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void drawFilledBox(final AxisAlignedBB boundingBox) {
        if (boundingBox == null) {
            return;
        }
        GL11.glBegin(7);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glEnd();
    }
    
    public static void drawBoundingBoxESP(final AxisAlignedBB axisalignedbb, final float width, final int color) {
        GL11.glPushMatrix();
        final float red = (color >> 24 & 0xFF) / 255.0f;
        final float green = (color >> 16 & 0xFF) / 255.0f;
        final float blue = (color >> 8 & 0xFF) / 255.0f;
        final float alpha = (color & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(width);
        GL11.glColor4f(red, green, blue, alpha);
        drawOutlinedBox(axisalignedbb);
        GL11.glLineWidth(1.0f);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void drawOutlinedBox(final AxisAlignedBB boundingBox) {
        if (boundingBox == null) {
            return;
        }
        GL11.glBegin(3);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glEnd();
        GL11.glBegin(3);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glEnd();
        GL11.glBegin(1);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glEnd();
    }
    
    public static void drawBorderedCorneredRect(final float x, final float y, final float x2, final float y2, final float lineWidth, final int lineColor, final int bgColor) {
        rect(x, y, x2, y2, bgColor);
        final float f = (lineColor >> 24 & 0xFF) / 255.0f;
        final float f2 = (lineColor >> 16 & 0xFF) / 255.0f;
        final float f3 = (lineColor >> 8 & 0xFF) / 255.0f;
        final float f4 = (lineColor & 0xFF) / 255.0f;
        rect(x - 1.0f, y - 1.0f, x2 + 1.0f, y, lineColor);
        rect(x - 1.0f, y, x, y2, lineColor);
        rect(x - 1.0f, y2, x2 + 1.0f, y2 + 1.0f, lineColor);
        rect(x2, y, x2 + 1.0f, y2, lineColor);
    }
    
    public static void drawRect(double left, double top, double right, double bottom, final int color) {
        if (left < right) {
            final float var5 = (float)left;
            left = right;
            right = var5;
        }
        if (top < bottom) {
            final float var5 = (float)top;
            top = bottom;
            bottom = var5;
        }
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GLUtils.glColor(color);
        GL11.glBegin(7);
        GL11.glVertex2d(left, bottom);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glVertex2d(left, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
    }
    
    public static double getAnimationStateSmooth(final double target, double current, double speed) {
        final boolean bl;
        final boolean larger = bl = (target > current);
        if (speed < 0.0) {
            speed = 0.0;
        }
        else if (speed > 1.0) {
            speed = 1.0;
        }
        if (target == current) {
            return target;
        }
        final double dif = Math.max(target, current) - Math.min(target, current);
        double factor = Math.max(dif * speed, 1.0);
        if (factor < 0.1) {
            factor = 0.1;
        }
        if (larger) {
            if (current + factor > target) {
                current = target;
            }
            else {
                current += factor;
            }
        }
        else if (current - factor < target) {
            current = target;
        }
        else {
            current -= factor;
        }
        return current;
    }
    
    public static void drawOutlinedBlockESP(final double x, final double y, final double z, final float red, final float green, final float blue, final float alpha, final float lineWidth) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(lineWidth);
        GL11.glColor4f(red, green, blue, alpha);
        drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void drawOutlinedBoundingBox(final AxisAlignedBB aa) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
    }
    
    public static float[] getColor(final BlockPos pos) {
        final Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
        if (Blocks.diamond_ore.equals(block)) {
            return new float[] { 0.0f, 1.0f, 1.0f };
        }
        if (Blocks.lapis_ore.equals(block)) {
            return new float[] { 0.0f, 0.0f, 1.0f };
        }
        if (Blocks.iron_ore.equals(block)) {
            return new float[] { 1.0f, 1.0f, 1.0f };
        }
        if (Blocks.gold_ore.equals(block)) {
            return new float[] { 1.0f, 1.0f, 0.0f };
        }
        if (Blocks.coal_ore.equals(block)) {
            return new float[] { 0.0f, 0.0f, 0.0f };
        }
        if (Blocks.emerald_ore.equals(block)) {
            return new float[] { 0.0f, 1.0f, 0.0f };
        }
        if (Blocks.redstone_ore.equals(block) || Blocks.lit_redstone_ore.equals(block)) {
            return new float[] { 1.0f, 0.0f, 0.0f };
        }
        return new float[] { 0.0f, 0.0f, 0.0f };
    }
    
    public static void renderBlock(final BlockPos pos, final float alpha, final float width) {
        final double x = pos.func_177958_n() - Wrapper.getRenderPosX();
        final double y = pos.func_177956_o() - Wrapper.getRenderPosY();
        final double z = pos.func_177952_p() - Wrapper.getRenderPosZ();
        final float[] color = getColor(pos);
        drawOutlinedBlockESP(x, y, z, color[0], color[1], color[2], alpha, width);
    }
    
    public static void roundedRect(final double x, final double y, double width, double height, final double edgeRadius, final Color color) {
        final double halfRadius = edgeRadius / 2.0;
        width -= halfRadius;
        height -= halfRadius;
        float sideLength = (float)edgeRadius;
        sideLength /= 2.0f;
        start();
        if (color != null) {
            color(color);
        }
        begin(6);
        for (double i = 180.0; i <= 270.0; ++i) {
            final double angle = i * 6.283185307179586 / 360.0;
            vertex(x + sideLength * Math.cos(angle) + sideLength, y + sideLength * Math.sin(angle) + sideLength);
        }
        vertex(x + sideLength, y + sideLength);
        end();
        stop();
        sideLength = (float)edgeRadius;
        sideLength /= 2.0f;
        start();
        if (color != null) {
            color(color);
        }
        GL11.glEnable(2848);
        begin(6);
        for (double i = 0.0; i <= 90.0; ++i) {
            final double angle = i * 6.283185307179586 / 360.0;
            vertex(x + width + sideLength * Math.cos(angle), y + height + sideLength * Math.sin(angle));
        }
        vertex(x + width, y + height);
        end();
        GL11.glDisable(2848);
        stop();
        sideLength = (float)edgeRadius;
        sideLength /= 2.0f;
        start();
        if (color != null) {
            color(color);
        }
        GL11.glEnable(2848);
        begin(6);
        for (double i = 270.0; i <= 360.0; ++i) {
            final double angle = i * 6.283185307179586 / 360.0;
            vertex(x + width + sideLength * Math.cos(angle), y + sideLength * Math.sin(angle) + sideLength);
        }
        vertex(x + width, y + sideLength);
        end();
        GL11.glDisable(2848);
        stop();
        sideLength = (float)edgeRadius;
        sideLength /= 2.0f;
        start();
        if (color != null) {
            color(color);
        }
        GL11.glEnable(2848);
        begin(6);
        for (double i = 90.0; i <= 180.0; ++i) {
            final double angle = i * 6.283185307179586 / 360.0;
            vertex(x + sideLength * Math.cos(angle) + sideLength, y + height + sideLength * Math.sin(angle));
        }
        vertex(x + sideLength, y + height);
        end();
        GL11.glDisable(2848);
        stop();
        rect(x + halfRadius, y + halfRadius, width - halfRadius, height - halfRadius, color);
        rect(x, y + halfRadius, edgeRadius / 2.0, height - halfRadius, color);
        rect(x + width, y + halfRadius, edgeRadius / 2.0, height - halfRadius, color);
        rect(x + halfRadius, y, width - halfRadius, halfRadius, color);
        rect(x + halfRadius, y + height, width - halfRadius, halfRadius, color);
    }
    
    public static void start() {
        enable(3042);
        GL11.glBlendFunc(770, 771);
        disable(3553);
        disable(2884);
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
    }
    
    public static void stop() {
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        enable(2884);
        enable(3553);
        disable(3042);
        color(Color.white);
    }
    
    public void push() {
        GL11.glPushMatrix();
    }
    
    public void pop() {
        GL11.glPopMatrix();
    }
    
    public static void enable(final int glTarget) {
        GL11.glEnable(glTarget);
    }
    
    public static void disable(final int glTarget) {
        GL11.glDisable(glTarget);
    }
    
    public static void begin(final int glMode) {
        GL11.glBegin(glMode);
    }
    
    public static void end() {
        GL11.glEnd();
    }
    
    public static void vertex(final double x, final double y) {
        GL11.glVertex2d(x, y);
    }
    
    public void translate(final double x, final double y) {
        GL11.glTranslated(x, y, 0.0);
    }
    
    public void scale(final double x, final double y) {
        GL11.glScaled(x, y, 1.0);
    }
    
    public void rotate(final double x, final double y, final double z, final double angle) {
        GL11.glRotated(angle, x, y, z);
    }
    
    public static void color(final double red, final double green, final double blue, final double alpha) {
        GL11.glColor4d(red, green, blue, alpha);
    }
    
    public void color(final double red, final double green, final double blue) {
        color(red, green, blue, 1.0);
    }
    
    public static void color(Color color) {
        if (color == null) {
            color = Color.white;
        }
        color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }
    
    public void color(Color color, final int alpha) {
        if (color == null) {
            color = Color.white;
        }
        color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.5);
    }
    
    public static void lineWidth(final double width) {
        GL11.glLineWidth((float)width);
    }
    
    public static void rect(final double x, final double y, final double width, final double height, final boolean filled, final Color color) {
        start();
        if (color != null) {
            color(color);
        }
        begin(filled ? 6 : 1);
        vertex(x, y);
        vertex(x + width, y);
        vertex(x + width, y + height);
        vertex(x, y + height);
        if (!filled) {
            vertex(x, y);
            vertex(x, y + height);
            vertex(x + width, y);
            vertex(x + width, y + height);
        }
        end();
        stop();
    }
    
    public void rect(final double x, final double y, final double width, final double height, final boolean filled) {
        rect(x, y, width, height, filled, null);
    }
    
    public static void rect(final double x, final double y, final double width, final double height, final Color color) {
        rect(x, y, width, height, true, color);
    }
    
    public void rect(final double x, final double y, final double width, final double height) {
        rect(x, y, width, height, true, null);
    }
    
    public void rectCentered(double x, double y, final double width, final double height, final boolean filled, final Color color) {
        x -= width / 2.0;
        y -= height / 2.0;
        rect(x, y, width, height, filled, color);
    }
    
    public void rectCentered(double x, double y, final double width, final double height, final boolean filled) {
        x -= width / 2.0;
        y -= height / 2.0;
        rect(x, y, width, height, filled, null);
    }
    
    public void rectCentered(double x, double y, final double width, final double height, final Color color) {
        x -= width / 2.0;
        y -= height / 2.0;
        rect(x, y, width, height, true, color);
    }
    
    public void rectCentered(double x, double y, final double width, final double height) {
        x -= width / 2.0;
        y -= height / 2.0;
        rect(x, y, width, height, true, null);
    }
    
    public void gradient(final double x, final double y, final double width, final double height, final boolean filled, final Color color1, final Color color2) {
        start();
        GL11.glShadeModel(7425);
        GlStateManager.enableAlpha();
        GL11.glAlphaFunc(516, 0.0f);
        if (color1 != null) {
            color(color1);
        }
        begin(filled ? 7 : 1);
        vertex(x, y);
        vertex(x + width, y);
        if (color2 != null) {
            color(color2);
        }
        vertex(x + width, y + height);
        vertex(x, y + height);
        if (!filled) {
            vertex(x, y);
            vertex(x, y + height);
            vertex(x + width, y);
            vertex(x + width, y + height);
        }
        end();
        GL11.glAlphaFunc(516, 0.1f);
        GlStateManager.disableAlpha();
        GL11.glShadeModel(7424);
        stop();
    }
    
    public void gradient(final double x, final double y, final double width, final double height, final Color color1, final Color color2) {
        this.gradient(x, y, width, height, true, color1, color2);
    }
    
    public void gradientCentered(double x, double y, final double width, final double height, final Color color1, final Color color2) {
        x -= width / 2.0;
        y -= height / 2.0;
        this.gradient(x, y, width, height, true, color1, color2);
    }
    
    public void gradientSideways(final double x, final double y, final double width, final double height, final boolean filled, final Color color1, final Color color2) {
        start();
        GL11.glShadeModel(7425);
        GlStateManager.disableAlpha();
        if (color1 != null) {
            color(color1);
        }
        begin(filled ? 6 : 1);
        vertex(x, y);
        vertex(x, y + height);
        if (color2 != null) {
            color(color2);
        }
        vertex(x + width, y + height);
        vertex(x + width, y);
        end();
        GlStateManager.enableAlpha();
        GL11.glShadeModel(7424);
        stop();
    }
    
    public void gradientSideways(final double x, final double y, final double width, final double height, final Color color1, final Color color2) {
        this.gradientSideways(x, y, width, height, true, color1, color2);
    }
    
    public void gradientSidewaysCentered(double x, double y, final double width, final double height, final Color color1, final Color color2) {
        x -= width / 2.0;
        y -= height / 2.0;
        this.gradientSideways(x, y, width, height, true, color1, color2);
    }
    
    public void polygon(final double x, final double y, double sideLength, final double amountOfSides, final boolean filled, final Color color) {
        sideLength /= 2.0;
        start();
        if (color != null) {
            color(color);
        }
        if (!filled) {
            GL11.glLineWidth(2.0f);
        }
        GL11.glEnable(2848);
        begin(filled ? 6 : 3);
        for (double i = 0.0; i <= amountOfSides / 4.0; ++i) {
            final double angle = i * 4.0 * 6.283185307179586 / 360.0;
            vertex(x + sideLength * Math.cos(angle) + sideLength, y + sideLength * Math.sin(angle) + sideLength);
        }
        end();
        GL11.glDisable(2848);
        stop();
    }
    
    public void polygon(final double x, final double y, final double sideLength, final int amountOfSides, final boolean filled) {
        this.polygon(x, y, sideLength, amountOfSides, filled, null);
    }
    
    public void polygon(final double x, final double y, final double sideLength, final int amountOfSides, final Color color) {
        this.polygon(x, y, sideLength, amountOfSides, true, color);
    }
    
    public void polygon(final double x, final double y, final double sideLength, final int amountOfSides) {
        this.polygon(x, y, sideLength, amountOfSides, true, null);
    }
    
    public void polygonCentered(double x, double y, final double sideLength, final int amountOfSides, final boolean filled, final Color color) {
        x -= sideLength / 2.0;
        y -= sideLength / 2.0;
        this.polygon(x, y, sideLength, amountOfSides, filled, color);
    }
    
    public void polygonCentered(double x, double y, final double sideLength, final int amountOfSides, final boolean filled) {
        x -= sideLength / 2.0;
        y -= sideLength / 2.0;
        this.polygon(x, y, sideLength, amountOfSides, filled, null);
    }
    
    public void polygonCentered(double x, double y, final double sideLength, final int amountOfSides, final Color color) {
        x -= sideLength / 2.0;
        y -= sideLength / 2.0;
        this.polygon(x, y, sideLength, amountOfSides, true, color);
    }
    
    public void polygonCentered(double x, double y, final double sideLength, final int amountOfSides) {
        x -= sideLength / 2.0;
        y -= sideLength / 2.0;
        this.polygon(x, y, sideLength, amountOfSides, true, null);
    }
    
    public void circle(final double x, final double y, final double radius, final boolean filled, final Color color) {
        this.polygon(x, y, radius, 360.0, filled, color);
    }
    
    public void circle(final double x, final double y, final double radius, final boolean filled) {
        this.polygon(x, y, radius, 360, filled);
    }
    
    public void circle(final double x, final double y, final double radius, final Color color) {
        this.polygon(x, y, radius, 360, color);
    }
    
    public void circle(final double x, final double y, final double radius) {
        this.polygon(x, y, radius, 360);
    }
    
    public void circleCentered(double x, double y, final double radius, final boolean filled, final Color color) {
        x -= radius / 2.0;
        y -= radius / 2.0;
        this.polygon(x, y, radius, 360.0, filled, color);
    }
    
    public void circleCentered(double x, double y, final double radius, final boolean filled) {
        x -= radius / 2.0;
        y -= radius / 2.0;
        this.polygon(x, y, radius, 360, filled);
    }
    
    public void circleCentered(double x, double y, final double radius, final boolean filled, final int sides) {
        x -= radius / 2.0;
        y -= radius / 2.0;
        this.polygon(x, y, radius, sides, filled);
    }
    
    public void circleCentered(double x, double y, final double radius, final Color color) {
        x -= radius / 2.0;
        y -= radius / 2.0;
        this.polygon(x, y, radius, 360, color);
    }
    
    public void circleCentered(double x, double y, final double radius) {
        x -= radius / 2.0;
        y -= radius / 2.0;
        this.polygon(x, y, radius, 360);
    }
    
    public void triangle(final double x, final double y, final double sideLength, final boolean filled, final Color color) {
        this.polygon(x, y, sideLength, 3.0, filled, color);
    }
    
    public void triangle(final double x, final double y, final double sideLength, final boolean filled) {
        this.polygon(x, y, sideLength, 3, filled);
    }
    
    public void triangle(final double x, final double y, final double sideLength, final Color color) {
        this.polygon(x, y, sideLength, 3, color);
    }
    
    public void triangle(final double x, final double y, final double sideLength) {
        this.polygon(x, y, sideLength, 3);
    }
    
    public void triangleCentered(double x, double y, final double sideLength, final boolean filled, final Color color) {
        x -= sideLength / 2.0;
        y -= sideLength / 2.0;
        this.polygon(x, y, sideLength, 3.0, filled, color);
    }
    
    public void triangleCentered(double x, double y, final double sideLength, final boolean filled) {
        x -= sideLength / 2.0;
        y -= sideLength / 2.0;
        this.polygon(x, y, sideLength, 3, filled);
    }
    
    public void triangleCentered(double x, double y, final double sideLength, final Color color) {
        x -= sideLength / 2.0;
        y -= sideLength / 2.0;
        this.polygon(x, y, sideLength, 3, color);
    }
    
    public void triangleCentered(double x, double y, final double sideLength) {
        x -= sideLength / 2.0;
        y -= sideLength / 2.0;
        this.polygon(x, y, sideLength, 3);
    }
    
    public static void lineNoGl(final double firstX, final double firstY, final double secondX, final double secondY, final Color color) {
        start();
        if (color != null) {
            color(color);
        }
        lineWidth(1.0);
        GL11.glEnable(2848);
        begin(1);
        vertex(firstX, firstY);
        vertex(secondX, secondY);
        end();
        GL11.glDisable(2848);
        stop();
    }
    
    public void line(final double firstX, final double firstY, final double secondX, final double secondY, final double lineWidth, final Color color) {
        start();
        if (color != null) {
            color(color);
        }
        lineWidth(lineWidth);
        GL11.glEnable(2848);
        begin(1);
        vertex(firstX, firstY);
        vertex(secondX, secondY);
        end();
        GL11.glDisable(2848);
        stop();
    }
    
    public void line(final double firstX, final double firstY, final double secondX, final double secondY, final double lineWidth) {
        this.line(firstX, firstY, secondX, secondY, lineWidth, null);
    }
    
    public void line(final double firstX, final double firstY, final double secondX, final double secondY, final Color color) {
        this.line(firstX, firstY, secondX, secondY, 0.0, color);
    }
    
    public void line(final double firstX, final double firstY, final double secondX, final double secondY) {
        this.line(firstX, firstY, secondX, secondY, 0.0, null);
    }
    
    public static void scissor(double x, double y, double width, double height) {
        final ScaledResolution sr = new ScaledResolution(RenderUtils.mc);
        final double scale = sr.getScaleFactor();
        y = sr.getScaledHeight() - y;
        x *= scale;
        y *= scale;
        width *= scale;
        height *= scale;
        GL11.glScissor((int)x, (int)(y - height), (int)width, (int)height);
    }
    
    public void outlineInlinedGradientRect(final double x, final double y, final double width, final double height, final double inlineOffset, final Color color1, final Color color2) {
        this.gradient(x, y, width, inlineOffset, color1, color2);
        this.gradient(x, y + height - inlineOffset, width, inlineOffset, color2, color1);
        this.gradientSideways(x, y, inlineOffset, height, color1, color2);
        this.gradientSideways(x + width - inlineOffset, y, inlineOffset, height, color2, color1);
    }
    
    public static void renderBreadCrumb(final Vec3 vec3) {
        GlStateManager.disableDepth();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        try {
            final double x = vec3.xCoord - RenderUtils.mc.getRenderManager().viewerPosX;
            final double y = vec3.yCoord - RenderUtils.mc.getRenderManager().viewerPosY;
            final double z = vec3.zCoord - RenderUtils.mc.getRenderManager().viewerPosZ;
            final double distanceFromPlayer = RenderUtils.mc.thePlayer.getDistance(vec3.xCoord, vec3.yCoord - 1.0, vec3.zCoord);
            int quality = (int)(distanceFromPlayer * 4.0 + 10.0);
            if (quality > 350) {
                quality = 350;
            }
            GL11.glPushMatrix();
            GL11.glTranslated(x, y, z);
            final float scale = 0.04f;
            GL11.glScalef(-0.04f, -0.04f, -0.04f);
            GL11.glRotated((double)(-RenderUtils.mc.getRenderManager().playerViewY), 0.0, 1.0, 0.0);
            GL11.glRotated((double)RenderUtils.mc.getRenderManager().playerViewX, 1.0, 0.0, 0.0);
            final Color c = Color.white;
            drawFilledCircleNoGL(0, 0, 0.7, c.hashCode(), quality);
            if (distanceFromPlayer < 4.0) {
                drawFilledCircleNoGL(0, 0, 1.4, new Color(c.getRed(), c.getGreen(), c.getBlue(), 50).hashCode(), quality);
            }
            if (distanceFromPlayer < 20.0) {
                drawFilledCircleNoGL(0, 0, 2.3, new Color(c.getRed(), c.getGreen(), c.getBlue(), 30).hashCode(), quality);
            }
            GL11.glScalef(0.8f, 0.8f, 0.8f);
            GL11.glPopMatrix();
        }
        catch (ConcurrentModificationException ex) {}
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GlStateManager.enableDepth();
        GL11.glColor3d(255.0, 255.0, 255.0);
    }
    
    public static void drawFilledCircleNoGL(final int x, final int y, final double r, final int c, final int quality) {
        final float f = (c >> 24 & 0xFF) / 255.0f;
        final float f2 = (c >> 16 & 0xFF) / 255.0f;
        final float f3 = (c >> 8 & 0xFF) / 255.0f;
        final float f4 = (c & 0xFF) / 255.0f;
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(6);
        for (int i = 0; i <= 360 / quality; ++i) {
            final double x2 = Math.sin(i * quality * 3.141592653589793 / 180.0) * r;
            final double y2 = Math.cos(i * quality * 3.141592653589793 / 180.0) * r;
            GL11.glVertex2d(x + x2, y + y2);
        }
        GL11.glEnd();
    }
    
    public static void rectangle(double left, double top, double right, double bottom, final int color) {
        if (left < right) {
            final double var5 = left;
            left = right;
            right = var5;
        }
        if (top < bottom) {
            final double var5 = top;
            top = bottom;
            bottom = var5;
        }
        final float var6 = (color >> 24 & 0xFF) / 255.0f;
        final float var7 = (color >> 16 & 0xFF) / 255.0f;
        final float var8 = (color >> 8 & 0xFF) / 255.0f;
        final float var9 = (color & 0xFF) / 255.0f;
        final WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var7, var8, var9, var6);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(left, bottom, 0.0).endVertex();
        worldRenderer.pos(right, bottom, 0.0).endVertex();
        worldRenderer.pos(right, top, 0.0).endVertex();
        worldRenderer.pos(left, top, 0.0).endVertex();
        Tessellator.getInstance().draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void rectangleBordered(final double x, final double y, final double x1, final double y1, final double width, final int internalColor, final int borderColor) {
        rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        rectangle(x + width, y, x1 - width, y + width, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        rectangle(x, y, x + width, y1, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        rectangle(x1 - width, y, x1, y1, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void skeetRect(final double x, final double y, final double x1, final double y1, final double size) {
        rectangleBordered(x, y - 4.0, x1 + size, y1 + size, 0.5, new Color(60, 60, 60).getRGB(), new Color(10, 10, 10).getRGB());
        rectangleBordered(x + 1.0, y - 3.0, x1 + size - 1.0, y1 + size - 1.0, 1.0, new Color(40, 40, 40).getRGB(), new Color(40, 40, 40).getRGB());
        rectangleBordered(x + 2.5, y - 1.5, x1 + size - 2.5, y1 + size - 2.5, 0.5, new Color(40, 40, 40).getRGB(), new Color(60, 60, 60).getRGB());
        rectangleBordered(x + 2.5, y - 1.5, x1 + size - 2.5, y1 + size - 2.5, 0.5, new Color(22, 22, 22).getRGB(), new Color(255, 255, 255, 0).getRGB());
    }
    
    public static void skeetRectSmall(final double x, final double y, final double x1, final double y1, final double size) {
        rectangleBordered(x + 4.35, y + 0.5, x1 + size - 84.5, y1 + size - 4.35, 0.5, new Color(48, 48, 48).getRGB(), new Color(10, 10, 10).getRGB());
        rectangleBordered(x + 5.0, y + 1.0, x1 + size - 85.0, y1 + size - 5.0, 0.5, new Color(17, 17, 17).getRGB(), new Color(255, 255, 255, 0).getRGB());
    }
    
    public static void drawModel(final float yaw, final float pitch, final EntityLivingBase entityLivingBase) {
        GlStateManager.resetColor();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, 0.0f, 50.0f);
        GlStateManager.scale(-50.0f, 50.0f, 50.0f);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        final float renderYawOffset = entityLivingBase.renderYawOffset;
        final float rotationYaw = entityLivingBase.rotationYaw;
        final float rotationPitch = entityLivingBase.rotationPitch;
        final float prevRotationYawHead = entityLivingBase.prevRotationYawHead;
        final float rotationYawHead = entityLivingBase.rotationYawHead;
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate((float)(-Math.atan(pitch / 40.0f) * 20.0), 1.0f, 0.0f, 0.0f);
        entityLivingBase.renderYawOffset = yaw - yaw / yaw * 0.4f;
        entityLivingBase.rotationYaw = yaw - yaw / yaw * 0.2f;
        entityLivingBase.rotationPitch = pitch;
        entityLivingBase.rotationYawHead = entityLivingBase.rotationYaw;
        entityLivingBase.prevRotationYawHead = entityLivingBase.rotationYaw;
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        final RenderManager renderManager = RenderUtils.mc.getRenderManager();
        renderManager.setPlayerViewY(180.0f);
        renderManager.setRenderShadow(false);
        renderManager.renderEntityWithPosYaw((Entity)entityLivingBase, 0.0, 0.0, 0.0, 0.0f, 1.0f);
        renderManager.setRenderShadow(true);
        entityLivingBase.renderYawOffset = renderYawOffset;
        entityLivingBase.rotationYaw = rotationYaw;
        entityLivingBase.rotationPitch = rotationPitch;
        entityLivingBase.prevRotationYawHead = prevRotationYawHead;
        entityLivingBase.rotationYawHead = rotationYawHead;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.resetColor();
    }
    
    public static void renderEnchantText(final ItemStack stack, final int x, final float y) {
        RenderHelper.disableStandardItemLighting();
        float enchantmentY = y + 24.0f;
        if (stack.getItem() instanceof ItemArmor) {
            final int protectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack);
            final int unbreakingLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
            final int thornLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack);
            if (protectionLevel > 0) {
                drawEnchantTag("P" + Colors.getColorUnicode(protectionLevel) + protectionLevel, x * 2, enchantmentY);
                enchantmentY += 8.0f;
            }
            if (unbreakingLevel > 0) {
                drawEnchantTag("U" + Colors.getColorUnicode(unbreakingLevel) + unbreakingLevel, x * 2, enchantmentY);
                enchantmentY += 8.0f;
            }
            if (thornLevel > 0) {
                drawEnchantTag("T" + Colors.getColorUnicode(thornLevel) + thornLevel, x * 2, enchantmentY);
                enchantmentY += 8.0f;
            }
        }
        if (stack.getItem() instanceof ItemBow) {
            final int powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
            final int punchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
            final int flameLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
            final int unbreakingLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
            if (powerLevel > 0) {
                drawEnchantTag("Pow" + Colors.getColorUnicode(powerLevel) + powerLevel, x * 2, enchantmentY);
                enchantmentY += 8.0f;
            }
            if (punchLevel > 0) {
                drawEnchantTag("Pun" + Colors.getColorUnicode(punchLevel) + punchLevel, x * 2, enchantmentY);
                enchantmentY += 8.0f;
            }
            if (flameLevel > 0) {
                drawEnchantTag("F" + Colors.getColorUnicode(flameLevel) + flameLevel, x * 2, enchantmentY);
                enchantmentY += 8.0f;
            }
            if (unbreakingLevel2 > 0) {
                drawEnchantTag("U" + Colors.getColorUnicode(unbreakingLevel2) + unbreakingLevel2, x * 2, enchantmentY);
                enchantmentY += 8.0f;
            }
        }
        if (stack.getItem() instanceof ItemSword) {
            final int sharpnessLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
            final int knockbackLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, stack);
            final int fireAspectLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
            final int unbreakingLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
            if (sharpnessLevel > 0) {
                drawEnchantTag("S" + Colors.getColorUnicode(sharpnessLevel) + sharpnessLevel, x * 2, enchantmentY);
                enchantmentY += 8.0f;
            }
            if (knockbackLevel > 0) {
                drawEnchantTag("K" + Colors.getColorUnicode(knockbackLevel) + knockbackLevel, x * 2, enchantmentY);
                enchantmentY += 8.0f;
            }
            if (fireAspectLevel > 0) {
                drawEnchantTag("F" + Colors.getColorUnicode(fireAspectLevel) + fireAspectLevel, x * 2, enchantmentY);
                enchantmentY += 8.0f;
            }
            if (unbreakingLevel2 > 0) {
                drawEnchantTag("U" + Colors.getColorUnicode(unbreakingLevel2) + unbreakingLevel2, x * 2, enchantmentY);
                enchantmentY += 8.0f;
            }
        }
        if (stack.getRarity() == EnumRarity.EPIC) {
            GlStateManager.pushMatrix();
            GlStateManager.disableDepth();
            GL11.glScalef(0.5f, 0.5f, 0.5f);
            final CFontRenderer default15 = FontManager.default15;
            CFontRenderer.drawOutlinedStringCock(Minecraft.getMinecraft().fontRendererObj, "God", x * 2, enchantmentY, new Color(255, 255, 0).getRGB(), new Color(100, 100, 0, 200).getRGB());
            GL11.glScalef(1.0f, 1.0f, 1.0f);
            GlStateManager.enableDepth();
            GlStateManager.popMatrix();
        }
    }
    
    private static void drawEnchantTag(final String text, final int x, final float y) {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        final CFontRenderer default15 = FontManager.default15;
        CFontRenderer.drawOutlinedStringCock(Minecraft.getMinecraft().fontRendererObj, text, x, y, -1, new Color(0, 0, 0, 220).darker().getRGB());
        GL11.glScalef(1.0f, 1.0f, 1.0f);
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }
    
    public static void drawFace(final EntityLivingBase target, final int x, final int y) {
        if (target instanceof EntityPlayer) {
            final NetworkPlayerInfo networkPlayerInfo = RenderUtils.mc.getNetHandler().getPlayerInfo(target.getUniqueID());
            RenderUtils.mc.getTextureManager().bindTexture(networkPlayerInfo.getLocationSkin());
            Gui.drawScaledCustomSizeModalRect(x + 2, y + 2, 8.0f, 8.0f, 8, 8, 24, 24, 64.0f, 64.0f);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.bindTexture(0);
        }
    }
    
    public static void drawUnfilledRect(final float x, final float y, final int width, final int height, final int c, final double lWidth) {
        GL11.glPushMatrix();
        final float f = (c >> 24 & 0xFF) / 255.0f;
        final float f2 = (c >> 16 & 0xFF) / 255.0f;
        final float f3 = (c >> 8 & 0xFF) / 255.0f;
        final float f4 = (c & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(f2, f3, f4, f);
        Gui.drawRect((int)x, (int)y + height, (int)x + (int)lWidth, (int)y, c);
        Gui.drawRect((int)x + 1, (int)y + (int)lWidth, (int)x + width + 1, (int)y, c);
        Gui.drawRect((int)x + width, (int)y + height, (int)x + width + (int)lWidth, (int)y + 1, c);
        Gui.drawRect((int)x, (int)y + height + (int)lWidth, (int)x + width + 1, (int)y + height, c);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        GL11.glPopMatrix();
    }
    
    public static void drawModalRectWithCustomSizedTexture(final float x, final float y, final float u, final float v, final float width, final float height, final float textureWidth, final float textureHeight) {
        final float f = 1.0f / textureWidth;
        final float f2 = 1.0f / textureHeight;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double)x, (double)(y + height), 0.0).tex((double)(u * f), (double)((v + height) * f2)).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + height), 0.0).tex((double)((u + width) * f), (double)((v + height) * f2)).endVertex();
        worldrenderer.pos((double)(x + width), (double)y, 0.0).tex((double)((u + width) * f), (double)(v * f2)).endVertex();
        worldrenderer.pos((double)x, (double)y, 0.0).tex((double)(u * f), (double)(v * f2)).endVertex();
        tessellator.draw();
    }
    
    public static void drawTexturedRect(final float x, final float y, final float width, final float height, final String image) {
        GL11.glPushMatrix();
        final boolean enableBlend = GL11.glIsEnabled(3042);
        final boolean disableAlpha = !GL11.glIsEnabled(3008);
        if (!enableBlend) {
            GL11.glEnable(3042);
        }
        if (!disableAlpha) {
            GL11.glDisable(3008);
        }
        RenderUtils.mc.getTextureManager().bindTexture(new ResourceLocation("shellsock", "shadow/" + image + ".png"));
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        if (!enableBlend) {
            GL11.glDisable(3042);
        }
        if (!disableAlpha) {
            GL11.glEnable(3008);
        }
        GL11.glPopMatrix();
    }
    
    public static void drawTexture(final int x, final int y, final float width, final float height, final String image) {
        GL11.glPushMatrix();
        final boolean enableBlend = GL11.glIsEnabled(3042);
        final boolean disableAlpha = !GL11.glIsEnabled(3008);
        if (!enableBlend) {
            GL11.glEnable(3042);
        }
        if (!disableAlpha) {
            GL11.glDisable(3008);
        }
        RenderUtils.mc.getTextureManager().bindTexture(new ResourceLocation("shellsock", "shadow/" + image + ".png"));
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, (int)width, (int)height, width, height);
        if (!enableBlend) {
            GL11.glDisable(3042);
        }
        if (!disableAlpha) {
            GL11.glEnable(3008);
        }
        GL11.glPopMatrix();
    }
    
    public static void drawShadow(final float x, final float y, final float width, final float height) {
        drawTexture((int)x - 9, (int)y - 9, 9.0f, 9.0f, "paneltopleft");
        drawTexture((int)(x - 9.0f), (int)(y + height), 9.0f, 9.0f, "panelbottomleft");
        drawTexture((int)(x + width), (int)(y + height), 9.0f, 9.0f, "panelbottomright");
        drawTexture((int)(x + width), (int)(y - 9.0f), 9.0f, 9.0f, "paneltopright");
        drawTexture((int)x - 9, (int)y, 9.0f, height, "panelleft");
        drawTexture((int)(x + width), (int)y, 9.0f, height, "panelright");
        drawTexture((int)x, (int)y - 9, width, 9.0f, "paneltop");
        drawTexture((int)x, (int)(y + height), width, 9.0f, "panelbottom");
    }
    
    public static void drawGradientSideways(final double left, final double top, final double right, final double bottom, final int col1, final int col2) {
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        final float f5 = (col2 >> 24 & 0xFF) / 255.0f;
        final float f6 = (col2 >> 16 & 0xFF) / 255.0f;
        final float f7 = (col2 >> 8 & 0xFF) / 255.0f;
        final float f8 = (col2 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);
        GL11.glColor4f(f6, f7, f8, f5);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
        Gui.drawRect(0, 0, 0, 0, 0);
    }
    
    public static void startGlScissor(final int x, final int y, final int width, int height) {
        final int scaleFactor = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
        GL11.glPushMatrix();
        GL11.glEnable(3089);
        final int n = x * scaleFactor;
        final int n2 = Minecraft.getMinecraft().displayHeight - (y + height) * scaleFactor;
        final int n3 = width * scaleFactor;
        height += 14;
        GL11.glScissor(n, n2, n3, height * scaleFactor);
    }
    
    public static void drawRoundRect(final double xPosition, final double yPosition, final double endX, final double endY, final int radius, final int color) {
        final double width = endX - xPosition;
        final double height = endY - yPosition;
        drawRect(xPosition + radius, yPosition + radius, xPosition + width - radius, yPosition + height - radius, color);
        drawRect(xPosition, yPosition + radius, xPosition + radius, yPosition + height - radius, color);
        drawRect(xPosition + width - radius, yPosition + radius, xPosition + width, yPosition + height - radius, color);
        drawRect(xPosition + radius, yPosition, xPosition + width - radius, yPosition + radius, color);
        drawRect(xPosition + radius, yPosition + height - radius, xPosition + width - radius, yPosition + height, color);
        drawFilledCircle(xPosition + radius, yPosition + radius, radius, color, 1);
        drawFilledCircle(xPosition + radius, yPosition + height - radius, radius, color, 2);
        drawFilledCircle(xPosition + width - radius, yPosition + radius, radius, color, 3);
        drawFilledCircle(xPosition + width - radius, yPosition + height - radius, radius, color, 4);
    }
    
    public static void drawRoundRect_up(final double xPosition, final double yPosition, final double endX, final double endY, final int radius, final int color) {
        final double width = endX - xPosition;
        final double height = endY - yPosition;
        drawBorderedRect(xPosition + radius, yPosition + radius, xPosition + width - radius, yPosition + height - radius, 0.0f, color, color);
        drawBorderedRect(xPosition, yPosition + radius, xPosition + radius, yPosition + height, 0.0f, color, color);
        drawBorderedRect(xPosition + width - radius, yPosition + radius, xPosition + width, yPosition + height - radius, 0.0f, color, color);
        drawBorderedRect(xPosition + radius, yPosition, xPosition + width - radius, yPosition + radius, 0.0f, color, color);
        drawBorderedRect(xPosition + radius, yPosition + height - radius, xPosition + width, yPosition + height, 0.0f, color, color);
        drawFilledCircle(xPosition + radius, yPosition + radius, radius, color, 1);
        drawFilledCircle(xPosition + width - radius, yPosition + radius, radius, color, 3);
    }
    
    public static void drawRoundRect_down(final double xPosition, final double yPosition, final double endX, final double endY, final int radius, final int color) {
        final double width = endX - xPosition;
        final double height = endY - yPosition;
        drawBorderedRect(xPosition + radius, yPosition + radius, xPosition + width - radius, yPosition + height - radius, 0.0f, color, color);
        drawBorderedRect(xPosition, yPosition + radius, xPosition + radius, yPosition + height - radius, 0.0f, color, color);
        drawBorderedRect(xPosition + width - radius, yPosition + radius, xPosition + width, yPosition + height - radius, 0.0f, color, color);
        drawBorderedRect(xPosition, yPosition, xPosition + width, yPosition + radius, 0.0f, color, color);
        drawBorderedRect(xPosition + radius, yPosition + height - radius, xPosition + width - radius, yPosition + height, 0.0f, color, color);
        drawFilledCircle(xPosition + radius, yPosition + height - radius, radius, color, 2);
        drawFilledCircle(xPosition + width - radius, yPosition + height - radius, radius, color, 4);
    }
    
    public static void drawRoundRect(final double xPosition, final double yPosition, final double endX, final double endY, final int BorderThincess, final int radius, final int color) {
        final double width = endX - xPosition;
        final double height = endY - yPosition;
        drawBorderedRect(xPosition + radius, yPosition + radius, xPosition + width - radius, yPosition + height - radius, BorderThincess, color, color);
        drawBorderedRect(xPosition, yPosition + radius, xPosition + radius, yPosition + height - radius, BorderThincess, color, color);
        drawBorderedRect(xPosition + width - radius, yPosition + radius, xPosition + width, yPosition + height - radius, BorderThincess, color, color);
        drawBorderedRect(xPosition + radius, yPosition, xPosition + width - radius, yPosition + radius, BorderThincess, color, color);
        drawBorderedRect(xPosition + radius, yPosition + height - radius, xPosition + width - radius, yPosition + height, BorderThincess, color, color);
        drawFilledCircle(xPosition + radius, yPosition + radius, radius, color, 1);
        drawFilledCircle(xPosition + radius, yPosition + height - radius, radius, color, 2);
        drawFilledCircle(xPosition + width - radius, yPosition + radius, radius, color, 3);
        drawFilledCircle(xPosition + width - radius, yPosition + height - radius, radius, color, 4);
    }
    
    public static void color(final int color, final float alpha) {
        final float r = (color >> 16 & 0xFF) / 255.0f;
        final float g = (color >> 8 & 0xFF) / 255.0f;
        final float b = (color & 0xFF) / 255.0f;
        GlStateManager.color(r, g, b, alpha);
    }
    
    public static void color(final int color) {
        color(color, (color >> 24 & 0xFF) / 255.0f);
    }
    
    public static void drawGoodCircle(final double x, final double y, final float radius, final int color) {
        color(color);
        GLUtils.setup2DRendering(() -> {
            GL11.glEnable(2832);
            GL11.glHint(3153, 4354);
            GL11.glPointSize(radius * (2 * RenderUtils.mc.gameSettings.guiScale));
            GLUtils.render(0, () -> GL11.glVertex2d(x, y));
        });
    }
    
    public static void renderRoundedRect(final float x, final float y, final float width, final float height, final float radius, final int color) {
        drawGoodCircle(x + radius, y + radius, radius, color);
        drawGoodCircle(x + width - radius, y + radius, radius, color);
        drawGoodCircle(x + radius, y + height - radius, radius, color);
        drawGoodCircle(x + width - radius, y + height - radius, radius, color);
        drawRect(x + radius, y, width - radius * 2.0f, height, color);
        drawRect(x, y + radius, width, height - radius * 2.0f, color);
    }
    
    public static void drawBorderedRect(final double x, final double y, final double x2, final double d, final float l1, final int col1, final int col2) {
        drawRect(x, y, x2, d, col2);
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, d);
        GL11.glVertex2d(x2, d);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, d);
        GL11.glVertex2d(x2, d);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        Gui.drawRect(0, 0, 0, 0, 0);
    }
    
    public static void drawFilledCircle(final double x, final double y, final double r, final int c, final int id) {
        final float f = (c >> 24 & 0xFF) / 255.0f;
        final float f2 = (c >> 16 & 0xFF) / 255.0f;
        final float f3 = (c >> 8 & 0xFF) / 255.0f;
        final float f4 = (c & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(9);
        if (id == 1) {
            GL11.glVertex2d(x, y);
            for (int i = 0; i <= 90; ++i) {
                final double x2 = Math.sin(i * 3.141526 / 180.0) * r;
                final double y2 = Math.cos(i * 3.141526 / 180.0) * r;
                GL11.glVertex2d(x - x2, y - y2);
            }
        }
        else if (id == 2) {
            GL11.glVertex2d(x, y);
            for (int i = 90; i <= 180; ++i) {
                final double x2 = Math.sin(i * 3.141526 / 180.0) * r;
                final double y2 = Math.cos(i * 3.141526 / 180.0) * r;
                GL11.glVertex2d(x - x2, y - y2);
            }
        }
        else if (id == 3) {
            GL11.glVertex2d(x, y);
            for (int i = 270; i <= 360; ++i) {
                final double x2 = Math.sin(i * 3.141526 / 180.0) * r;
                final double y2 = Math.cos(i * 3.141526 / 180.0) * r;
                GL11.glVertex2d(x - x2, y - y2);
            }
        }
        else if (id == 4) {
            GL11.glVertex2d(x, y);
            for (int i = 180; i <= 270; ++i) {
                final double x2 = Math.sin(i * 3.141526 / 180.0) * r;
                final double y2 = Math.cos(i * 3.141526 / 180.0) * r;
                GL11.glVertex2d(x - x2, y - y2);
            }
        }
        else {
            for (int i = 0; i <= 360; ++i) {
                final double x2 = Math.sin(i * 3.141526 / 180.0) * r;
                final double y2 = Math.cos(i * 3.141526 / 180.0) * r;
                GL11.glVertex2f((float)(x - x2), (float)(y - y2));
            }
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }
    
    public static void stopGlScissor() {
        GL11.glDisable(3089);
        GL11.glPopMatrix();
    }
    
    public static void drawGradientSidewaysV(final double left, final double top, final double right, final double bottom, final int col1, final int col2) {
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        final float f5 = (col2 >> 24 & 0xFF) / 255.0f;
        final float f6 = (col2 >> 16 & 0xFF) / 255.0f;
        final float f7 = (col2 >> 8 & 0xFF) / 255.0f;
        final float f8 = (col2 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glVertex2d(left, bottom);
        GL11.glVertex2d(right, bottom);
        GL11.glColor4f(f6, f7, f8, f5);
        GL11.glVertex2d(right, top);
        GL11.glVertex2d(left, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
        Gui.drawRect(0, 0, 0, 0, 0);
    }
    
    public static void pre3D() {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDisable(2896);
        GL11.glDepthMask(false);
        GL11.glHint(3154, 4354);
    }
    
    public static void post3D() {
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void glColor(final int hex) {
        final float alpha = (hex >> 24 & 0xFF) / 255.0f;
        final float red = (hex >> 16 & 0xFF) / 255.0f;
        final float green = (hex >> 8 & 0xFF) / 255.0f;
        final float blue = (hex & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }
    
    public static void draw2D(final EntityLivingBase entity, final double posX, final double posY, final double posZ, final int color, final int backgroundColor) {
        if (ValidUtils.isValidEntity(entity) || entity == Wrapper.INSTANCE.player() || ValidUtils.isBot(entity) || !ValidUtils.isTeam(entity)) {
            return;
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, posZ);
        GL11.glNormal3f(0.0f, 0.0f, 0.0f);
        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(-0.1, -0.1, 0.1);
        GL11.glDisable(2929);
        GL11.glBlendFunc(770, 771);
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        drawRect(-7.0f, 2.0f, -4.0f, 3.0f, color);
        drawRect(4.0f, 2.0f, 7.0f, 3.0f, color);
        drawRect(-7.0f, 0.5f, -6.0f, 3.0f, color);
        drawRect(6.0f, 0.5f, 7.0f, 3.0f, color);
        drawRect(-7.0f, 3.0f, -4.0f, 3.3f, backgroundColor);
        drawRect(4.0f, 3.0f, 7.0f, 3.3f, backgroundColor);
        drawRect(-7.3f, 0.5f, -7.0f, 3.3f, backgroundColor);
        drawRect(7.0f, 0.5f, 7.3f, 3.3f, backgroundColor);
        GlStateManager.translate(0.0, 21.0 + -(entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) * 12.0, 0.0);
        drawRect(4.0f, -20.0f, 7.0f, -19.0f, color);
        drawRect(-7.0f, -20.0f, -4.0f, -19.0f, color);
        drawRect(6.0f, -20.0f, 7.0f, -17.5f, color);
        drawRect(-7.0f, -20.0f, -6.0f, -17.5f, color);
        drawRect(7.0f, -20.0f, 7.3f, -17.5f, backgroundColor);
        drawRect(-7.3f, -20.0f, -7.0f, -17.5f, backgroundColor);
        drawRect(4.0f, -20.3f, 7.3f, -20.0f, backgroundColor);
        drawRect(-7.3f, -20.3f, -4.0f, -20.0f, backgroundColor);
        GL11.glEnable(2929);
        GlStateManager.popMatrix();
    }
    
    public static void draw2DShell(final EntityLivingBase entity, final double posX, final double posY, final double posZ, final int color, final int backgroundColor) {
        if (ValidUtils.isValidEntity(entity) || entity == Wrapper.INSTANCE.player() || ValidUtils.isBot(entity) || !ValidUtils.isTeam(entity)) {
            return;
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, posZ);
        GL11.glNormal3f(0.0f, 0.0f, 0.0f);
        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(-0.1, -0.1, 0.1);
        GL11.glDisable(2929);
        GL11.glBlendFunc(770, 771);
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        if (entity.hurtTime > 0) {
            drawRect(-7.0, -(entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) * 12.0, 7.0, 0.5, Colors.getColor(255, 0, 0, 120));
        }
        else {
            drawRect(-7.0, -(entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) * 12.0, 7.0, 0.5, Colors.getColor(255, 255, 255, 120));
        }
        GlStateManager.translate(0.0, 21.0 + -(entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) * 12.0, 0.0);
        GL11.glEnable(2929);
        GlStateManager.popMatrix();
    }
    
    public static void drawESPOnStorage(final TileEntityLockable storage, final double x, final double y, final double z) {
        assert !storage.isLocked();
        final TileEntityChest chest = (TileEntityChest)storage;
        Vec3 vec = new Vec3(0.0, 0.0, 0.0);
        Vec3 vec2 = new Vec3(0.0, 0.0, 0.0);
        if (chest.adjacentChestZNeg != null) {
            vec = new Vec3(x + 0.0625, y, z - 0.9375);
            vec2 = new Vec3(x + 0.9375, y + 0.875, z + 0.9375);
        }
        else if (chest.adjacentChestXNeg != null) {
            vec = new Vec3(x + 0.9375, y, z + 0.0625);
            vec2 = new Vec3(x - 0.9375, y + 0.875, z + 0.9375);
        }
        else {
            if (chest.adjacentChestXPos != null || chest.adjacentChestZPos != null) {
                return;
            }
            vec = new Vec3(x + 0.0625, y, z + 0.0625);
            vec2 = new Vec3(x + 0.9375, y + 0.875, z + 0.9375);
        }
        GL11.glPushMatrix();
        pre3D();
        try {
            final Method method = ReflectionHelper.findMethod((Class)EntityRenderer.class, (Object)Wrapper.INSTANCE.mc().entityRenderer, new String[] { "setupCameraTransform", "func_78479_a" }, new Class[] { Float.TYPE, Integer.TYPE });
            method.setAccessible(true);
            method.invoke(Wrapper.INSTANCE.mc().entityRenderer, new Float(Wrapper.timer.renderPartialTicks), new Integer(2));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        GL11.glColor4d(0.7, 0.1, 0.1, 0.5);
        try {
            final Field renderPosX = ReflectionHelper.findField((Class)RenderManager.class, new String[] { "renderPosX", "field_78725_b" });
            if (!renderPosX.isAccessible()) {
                renderPosX.setAccessible(true);
            }
            final Field renderPosY = ReflectionHelper.findField((Class)RenderManager.class, new String[] { "renderPosY", "field_78726_c" });
            if (!renderPosY.isAccessible()) {
                renderPosY.setAccessible(true);
            }
            final Field renderPosZ = ReflectionHelper.findField((Class)RenderManager.class, new String[] { "renderPosZ", "field_78723_d" });
            if (!renderPosZ.isAccessible()) {
                renderPosZ.setAccessible(true);
            }
            drawBoundingBox(new AxisAlignedBB(vec.xCoord - renderPosX.getDouble(Wrapper.INSTANCE.mc().getRenderManager()), vec.yCoord - renderPosY.getDouble(Wrapper.INSTANCE.mc().getRenderManager()), vec.zCoord - renderPosZ.getDouble(Wrapper.INSTANCE.mc().getRenderManager()), vec2.xCoord - renderPosX.getDouble(Wrapper.INSTANCE.mc().getRenderManager()), vec2.yCoord - renderPosY.getDouble(Wrapper.INSTANCE.mc().getRenderManager()), vec2.zCoord - renderPosZ.getDouble(Wrapper.INSTANCE.mc().getRenderManager())));
        }
        catch (IllegalArgumentException | IllegalAccessException ex2) {
            final Exception ex;
            final Exception e = ex;
            e.printStackTrace();
        }
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        post3D();
        GL11.glPopMatrix();
    }
    
    public static void drawBoundingBox(final AxisAlignedBB aa) {
        final MyTessellator tessellator = MyTessellator.getInstance();
        final BufferBuilder vertexBuffer = tessellator.getBuffer();
        vertexBuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexBuffer.pos(aa.minX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.maxZ);
        tessellator.draw();
        vertexBuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.maxZ);
        tessellator.draw();
        vertexBuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.minZ);
        tessellator.draw();
        vertexBuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexBuffer.pos(aa.minX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.minZ);
        tessellator.draw();
        vertexBuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexBuffer.pos(aa.minX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.minZ);
        tessellator.draw();
        vertexBuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.maxZ);
        tessellator.draw();
    }
    
    public static void drawCheckBox(final int x1, final int y1, final int x3, final int y2) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glBegin(7);
        GL11.glVertex2i(x1, y1);
        GL11.glVertex2i(x1, y2);
        GL11.glVertex2i(x3, y2);
        GL11.glVertex2i(x3, y1);
        GL11.glEnd();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
        GL11.glBegin(2);
        GL11.glVertex2i(x1, y1);
        GL11.glVertex2i(x1, y2);
        GL11.glVertex2i(x3, y2);
        GL11.glVertex2i(x3, y1);
        GL11.glEnd();
    }
    
    public static void drawLine3D(final float x, final float y, final float z, final float x1, final float y1, final float z1, final int color) {
        pre3D();
        GL11.glLoadIdentity();
        try {
            final Method method = ReflectionHelper.findMethod((Class)EntityRenderer.class, (Object)Wrapper.INSTANCE.mc().entityRenderer, new String[] { "orientCamera", "func_78467_g" }, new Class[] { Float.TYPE });
            method.setAccessible(true);
            method.invoke(Wrapper.INSTANCE.mc().entityRenderer, new Float(Wrapper.timer.renderPartialTicks));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        final float var11 = (color >> 24 & 0xFF) / 255.0f;
        final float var12 = (color >> 16 & 0xFF) / 255.0f;
        final float var13 = (color >> 8 & 0xFF) / 255.0f;
        final float var14 = (color & 0xFF) / 255.0f;
        GL11.glColor4f(var12, var13, var14, var11);
        GL11.glLineWidth(3.0f);
        GL11.glBegin(3);
        GL11.glVertex3d((double)x, (double)y, (double)z);
        GL11.glVertex3d((double)x1, (double)y1, (double)z1);
        GL11.glEnd();
        post3D();
    }
    
    public static void drawGradientRect(final double left, final double top, final double right, final double bottom, final int startColor, final int endColor) {
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
        worldrenderer.pos(right, top, 0.0).color(f2, f3, f4, f).endVertex();
        worldrenderer.pos(left, top, 0.0).color(f2, f3, f4, f).endVertex();
        worldrenderer.pos(left, bottom, 0.0).color(f6, f7, f8, f5).endVertex();
        worldrenderer.pos(right, bottom, 0.0).color(f6, f7, f8, f5).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    
    public static void doScissor(final int x, final int y, final int width, final int height) {
        final ScaledResolution sr = new ScaledResolution(Wrapper.INSTANCE.mc());
        final double scale = sr.getScaleFactor();
        final double finalHeight = height * scale;
        final double finalY = (sr.getScaledHeight() - y) * scale;
        final double finalX = x * scale;
        final double finalWidth = width * scale;
        GL11.glScissor((int)finalX, (int)(finalY - finalHeight), (int)finalWidth, (int)finalHeight);
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
