package com.netease.mc.modSS.ui.clickgui.kendall;

import net.minecraft.client.*;
import net.minecraft.util.*;
import java.io.*;
import net.minecraft.client.gui.*;
import java.util.*;
import net.minecraftforge.fml.relauncher.*;
import net.minecraft.client.shader.*;
import com.netease.mc.modSS.utils.*;

public class BLur
{
    private static ShaderGroup blurShader;
    private static Framebuffer buffer;
    private static int lastScale;
    private static int lastScaleWidth;
    private static int lastScaleHeight;
    
    private static void reinitShader() {
        BLur.blurShader.createBindFramebuffers(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        (BLur.buffer = new Framebuffer(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, true)).setFramebufferColor(0.0f, 0.0f, 0.0f, 0.0f);
    }
    
    public static void draw(final int x, final int y, final int width, final int height, final int blur, final float radius) {
        try {
            BLur.blurShader = new ShaderGroup(Minecraft.getMinecraft().renderEngine, Minecraft.getMinecraft().getResourceManager(), Minecraft.getMinecraft().getFramebuffer(), new ResourceLocation("shaders/post/blur.json"));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        final ScaledResolution scale = new ScaledResolution(Minecraft.getMinecraft());
        final int factor = scale.getScaleFactor();
        final int factor2 = scale.getScaledWidth();
        final int factor3 = scale.getScaledHeight();
        if (BLur.lastScale != factor || BLur.lastScaleWidth != factor2 || BLur.lastScaleHeight != factor3) {
            reinitShader();
        }
        BLur.lastScale = factor;
        BLur.lastScaleWidth = factor2;
        BLur.lastScaleHeight = factor3;
        final List<Shader> listShaders = (List<Shader>)ReflectionHelper.getPrivateValue((Class)ShaderGroup.class, (Object)BLur.blurShader, new String[] { "listShaders", "field_148031_d" });
        listShaders.get(0).getShaderManager().getShaderUniform("BlurXY").set((float)x, (float)(factor3 - y - height));
        listShaders.get(1).getShaderManager().getShaderUniform("BlurXY").set((float)x, (float)(factor3 - y - height));
        listShaders.get(0).getShaderManager().getShaderUniform("BlurCoord").set((float)width, (float)height);
        listShaders.get(1).getShaderManager().getShaderUniform("BlurCoord").set((float)width, (float)height);
        listShaders.get(0).getShaderManager().getShaderUniform("Radius").set(radius);
        listShaders.get(1).getShaderManager().getShaderUniform("Radius").set(radius);
        BLur.blurShader.loadShaderGroup(Wrapper.timer.renderPartialTicks);
        Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);
    }
    
    static {
        BLur.lastScale = 0;
        BLur.lastScaleWidth = 0;
        BLur.lastScaleHeight = 0;
    }
}
