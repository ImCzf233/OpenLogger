package com.netease.mc.modSS.ui.clickgui.ss;

import java.util.*;
import com.netease.mc.modSS.ui.clickgui.ss.components.*;
import com.netease.mc.modSS.mod.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.gui.*;
import org.lwjgl.input.*;
import java.io.*;
import com.netease.mc.modSS.file.*;
import com.google.common.collect.*;

public class SSUI extends GuiScreen
{
    public static ArrayList<Window> windows;
    public double opacity;
    public int scrollVelocity;
    public static boolean binding;
    private float animpos;
    
    public SSUI() {
        this.opacity = 0.0;
        this.animpos = 75.0f;
        if (SSUI.windows.isEmpty()) {
            int x = 5;
            for (final Category c : Category.values()) {
                SSUI.windows.add(new Window(c, x, 5));
                x += 105;
            }
        }
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.opacity = ((this.opacity + 10.0 < 200.0) ? (this.opacity += 10.0) : 200.0);
        GlStateManager.pushMatrix();
        final ScaledResolution scaledRes = new ScaledResolution(this.mc);
        final float scale = scaledRes.getScaleFactor() / (float)Math.pow(scaledRes.getScaleFactor(), 2.0);
        SSUI.windows.forEach(w -> w.render(mouseX, mouseY));
        GlStateManager.popMatrix();
        if (Mouse.hasWheel()) {
            final int wheel = Mouse.getDWheel();
            this.scrollVelocity = ((wheel < 0) ? -120 : ((wheel > 0) ? 120 : 0));
        }
        SSUI.windows.forEach(w -> w.mouseScroll(mouseX, mouseY, this.scrollVelocity));
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        SSUI.windows.forEach(w -> w.click(mouseX, mouseY, mouseButton));
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1 && !SSUI.binding) {
            this.mc.displayGuiScreen((GuiScreen)null);
            return;
        }
        SSUI.windows.forEach(w -> w.key(typedChar, keyCode));
    }
    
    public void initGui() {
        super.initGui();
    }
    
    public void onGuiClosed() {
        this.saveConfig();
        this.mc.entityRenderer.stopUseShader();
    }
    
    public synchronized void sendToFront(final Window window) {
        int panelIndex = 0;
        for (int i = 0; i < SSUI.windows.size(); ++i) {
            if (SSUI.windows.get(i) == window) {
                panelIndex = i;
                break;
            }
        }
        final Window t = SSUI.windows.get(SSUI.windows.size() - 1);
        SSUI.windows.set(SSUI.windows.size() - 1, SSUI.windows.get(panelIndex));
        SSUI.windows.set(panelIndex, t);
    }
    
    private void saveConfig() {
        ModFile.saveModules();
        SettingsButtonFile.saveState();
        SettingsComboBoxFile.saveState();
        SettingsSliderFile.saveState();
        KeybindFile.saveKeybinds();
    }
    
    static {
        SSUI.windows = (ArrayList<Window>)Lists.newArrayList();
        SSUI.binding = false;
    }
}
