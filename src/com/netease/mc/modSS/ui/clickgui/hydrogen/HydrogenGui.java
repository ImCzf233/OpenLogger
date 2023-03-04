package com.netease.mc.modSS.ui.clickgui.hydrogen;

import com.netease.mc.modSS.utils.render.*;
import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.managers.*;
import com.netease.mc.modSS.ui.clickgui.hydrogen.component.*;
import com.netease.mc.modSS.*;
import net.minecraft.client.renderer.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.client.*;
import net.minecraft.client.shader.*;
import org.lwjgl.input.*;
import java.util.*;
import java.io.*;
import net.minecraft.client.gui.*;
import com.netease.mc.modSS.file.*;

public class HydrogenGui extends GuiScreen
{
    public static ArrayList<Frame> frames;
    public static int color;
    private ParticleGenerator particleGen;
    private int scroll;
    
    public HydrogenGui() {
        HydrogenGui.frames = new ArrayList<Frame>();
        this.particleGen = new ParticleGenerator(60, Utils.getScaledRes().getScaledWidth(), Utils.getScaledRes().getScaledHeight());
        int frameX = 5;
        for (final Category category : Category.values()) {
            final Frame frame = new Frame(category);
            frame.setX(frameX);
            HydrogenGui.frames.add(frame);
            frameX += frame.getWidth() + 1;
        }
    }
    
    public void drawScreen(final int mouseX, int mouseY, final float partialTicks) {
        GlStateManager.translate(0.0f, (float)this.scroll, 0.0f);
        mouseY -= this.scroll;
        func_73734_a(0, 0, this.width, this.height, 1712328720);
        FontManager.default15.drawStringWithShadow("Press \"F9\" to restore the position of frames", (Utils.getScaledRes().getScaledWidth() - Wrapper.INSTANCE.mc().fontRendererObj.getStringWidth("Press \"F9\" to restore the position of frames")) * 2 + Wrapper.INSTANCE.mc().fontRendererObj.getStringWidth("Press \"F9\" to restore the position of frames") - 5, (Utils.getScaledRes().getScaledHeight() - Wrapper.INSTANCE.mc().fontRendererObj.FONT_HEIGHT - 1) * 2, -1);
        for (final Frame frame : HydrogenGui.frames) {
            frame.renderFrame(mouseX, mouseY, this.fontRendererObj);
            frame.updatePosition(mouseX, mouseY);
            for (final Component comp : frame.getComponents()) {
                comp.updateComponent(mouseX, mouseY);
            }
        }
        if (ShellSock.getClient().settingsManager.getSettingByName("Blur").isEnabled()) {
            if (OpenGlHelper.shadersSupported && this.mc.entityRenderer.getShaderGroup() != null) {
                this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
        }
        else if (this.mc.entityRenderer.getShaderGroup() != null) {
            this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            try {
                Mappings.theShaderGroup.set(Minecraft.getMinecraft().entityRenderer, null);
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (Mouse.hasWheel()) {
            Mouse.getDWheel();
        }
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        for (final Frame frame : HydrogenGui.frames) {
            if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
                frame.setDrag(true);
                frame.dragX = mouseX - frame.getX();
                frame.dragY = mouseY - frame.getY();
            }
            if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
                frame.setOpen(!frame.isOpen());
            }
            if (frame.isOpen() && !frame.getComponents().isEmpty()) {
                for (final Component component : frame.getComponents()) {
                    component.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) {
        for (final Frame frame : HydrogenGui.frames) {
            if (frame.isOpen() && keyCode != 1 && !frame.getComponents().isEmpty()) {
                for (final Component component : frame.getComponents()) {
                    component.keyTyped(typedChar, keyCode);
                }
            }
        }
        if (keyCode == 67) {
            HydrogenGui.frames = new ArrayList<Frame>();
            int frameX = 2;
            for (final Category category : Category.values()) {
                final Frame frame2 = new Frame(category);
                final ScaledResolution sr = new ScaledResolution(this.mc);
                if (sr.getScaledWidth() >= frame2.getWidth() * 7) {
                    frame2.setX(frameX);
                    HydrogenGui.frames.add(frame2);
                    frameX += frame2.getWidth();
                }
            }
        }
        if (keyCode == 1) {
            this.mc.displayGuiScreen((GuiScreen)null);
        }
    }
    
    public void onGuiClosed() {
        if (this.mc.entityRenderer.getShaderGroup() != null) {
            this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            try {
                Mappings.theShaderGroup.set(Minecraft.getMinecraft().entityRenderer, null);
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        ClickGuiFile.saveClickGui();
        super.onGuiClosed();
    }
    
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        for (final Frame frame : HydrogenGui.frames) {
            frame.setDrag(false);
        }
        for (final Frame frame : HydrogenGui.frames) {
            if (frame.isOpen() && !frame.getComponents().isEmpty()) {
                for (final Component component : frame.getComponents()) {
                    component.mouseReleased(mouseX, mouseY, state);
                }
            }
        }
    }
    
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    static {
        HydrogenGui.color = -1714430721;
    }
}
