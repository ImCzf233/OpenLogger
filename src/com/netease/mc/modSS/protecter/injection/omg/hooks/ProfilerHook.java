package com.netease.mc.modSS.protecter.injection.omg.hooks;

import net.minecraft.profiler.*;
import dev.ss.world.event.eventapi.*;
import dev.ss.world.event.eventapi.events.*;
import net.minecraft.client.renderer.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import com.netease.mc.modSS.utils.*;
import dev.ss.world.event.mixinevents.*;
import java.nio.*;
import java.util.*;

public class ProfilerHook extends Profiler
{
    public Profiler profiler;
    public String lastSection;
    
    public ProfilerHook(final Profiler profiler) {
        this.lastSection = "";
        this.profiler = profiler;
        this.profilingEnabled = profiler.profilingEnabled;
    }
    
    public void clearProfiling() {
        this.profiler.clearProfiling();
    }
    
    public void startSection(final String name) {
        if (name.equals("forgeHudText")) {
            GlStateManager.pushMatrix();
            final Event2D ed2 = new Event2D();
            EventManager.call(ed2);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }
        this.lastSection = name;
        this.profiler.startSection(name);
    }
    
    public void endSection() {
        this.profiler.endSection();
    }
    
    public void endStartSection(final String name) {
        if (name.equals("hand")) {
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
        this.lastSection = name;
        this.profiler.endStartSection(name);
    }
    
    public String getNameOfLastSection() {
        return this.profiler.getNameOfLastSection();
    }
    
    public List<Profiler.Result> getProfilingData(final String p_76321_1_) {
        return this.getProfilingData(p_76321_1_);
    }
}
