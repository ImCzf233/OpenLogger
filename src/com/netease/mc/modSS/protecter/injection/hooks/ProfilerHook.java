package com.netease.mc.modSS.protecter.injection.hooks;

import com.netease.mc.modSS.protecter.injection.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import com.netease.mc.modSS.utils.*;
import dev.ss.world.event.eventapi.*;
import dev.ss.world.event.eventapi.events.*;
import net.minecraft.client.renderer.*;
import dev.ss.world.event.mixinevents.*;
import java.nio.*;
import org.objectweb.asm.*;

public class ProfilerHook extends ShellClassVisitor
{
    public ProfilerHook(final ClassVisitor cv, final boolean obf) {
        super(cv);
        this.registerMethodVisitor(obf ? "a" : "startSection", "(Ljava/lang/String;)V", mv -> new startSection(mv));
    }
    
    public static void hookOnRender(final String info) {
        if (info.equalsIgnoreCase("hand")) {
            final GLUProjection projection = GLUProjection.getInstance();
            final IntBuffer viewPort = GLAllocation.createDirectIntBuffer(16);
            final FloatBuffer modelView = GLAllocation.createDirectFloatBuffer(16);
            final FloatBuffer projectionPort = GLAllocation.createDirectFloatBuffer(16);
            GL11.glGetFloat(2982, modelView);
            GL11.glGetFloat(2983, projectionPort);
            GL11.glGetInteger(2978, viewPort);
            final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
            projection.updateMatrices(viewPort, modelView, projectionPort, scaledResolution.getScaledWidth() / Minecraft.getMinecraft().displayWidth, scaledResolution.getScaledHeight() / Minecraft.getMinecraft().displayHeight);
            EventManager.call(new Event3D(Wrapper.timer.renderPartialTicks));
        }
        else if (info.equalsIgnoreCase("forgeHudText")) {
            GlStateManager.pushMatrix();
            final Event2D ed2 = new Event2D();
            EventManager.call(ed2);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }
    }
    
    public static class startSection extends MethodVisitor
    {
        public startSection(final MethodVisitor mv) {
            super(262144, mv);
        }
        
        public void visitCode() {
            final MethodVisitor methodVisitor = this.mv;
            this.mv.visitVarInsn(25, 1);
            this.mv.visitMethodInsn(184, "com/netease/mc/modSS/protecter/injection/hooks/ProfilerHook", "hookOnRender", "(Ljava/lang/String;)V", false);
        }
    }
}
