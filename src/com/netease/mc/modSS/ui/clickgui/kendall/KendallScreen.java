package com.netease.mc.modSS.ui.clickgui.kendall;

import com.netease.mc.modSS.ui.clickgui.kendall.frame.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.*;
import com.netease.mc.modSS.mod.*;
import java.util.*;
import com.netease.mc.modSS.ui.clickgui.kendall.components.*;
import java.io.*;
import org.lwjgl.opengl.*;
import com.netease.mc.modSS.ui.clickgui.*;
import com.netease.mc.modSS.*;
import com.netease.mc.modSS.file.*;
import org.lwjgl.input.*;
import com.netease.mc.modSS.utils.*;
import com.netease.mc.modSS.managers.*;
import com.netease.mc.modSS.ui.clickgui.kendall.components.impls.*;

public class KendallScreen extends GuiScreen
{
    public ArrayList<KendallFrame> frames;
    public boolean onMoving;
    public ScaledResolution scaledResolution;
    public double x;
    public double y;
    public float moveX;
    public float moveY;
    private final FontRenderer fr;
    public boolean isVIsableSW;
    public KendallButton targetbt;
    
    protected ArrayList<KendallFrame> getFrames() {
        return this.frames;
    }
    
    protected void addFrame(final KendallFrame frame) {
        if (!this.frames.contains(frame)) {
            this.frames.add(frame);
        }
    }
    
    public KendallScreen() {
        this.frames = new ArrayList<KendallFrame>();
        this.onMoving = false;
        this.scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        this.x = 0.0;
        this.y = 0.0;
        this.moveX = 0.0f;
        this.moveY = 0.0f;
        this.fr = Wrapper.INSTANCE.fontRenderer();
        this.isVIsableSW = false;
        this.targetbt = null;
        int x = 20;
        for (final Category category : Category.values()) {
            this.addFrame(new KendallFrame(category, x, 20));
            x += 110;
        }
    }
    
    public void initGui() {
        for (final KendallFrame frame : this.frames) {
            frame.initialize();
        }
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (isHovered((float)this.x, (float)this.y, (float)(this.x + this.width), (float)(this.y + 25.0), mouseX, mouseY) && mouseButton == 0) {
            this.onMoving = true;
        }
        if (this.isVIsableSW && this.targetbt != null) {
            for (final Component cp : this.targetbt.components) {
                cp.mouseClicked(mouseX, mouseY, mouseButton);
            }
            return;
        }
        for (final KendallFrame frame : this.frames) {
            frame.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }
    
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.onMoving = false;
        for (final KendallFrame frame : this.frames) {
            frame.mouseReleased(mouseX, mouseY, state);
        }
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (this.isVIsableSW && keyCode == 1) {
            this.isVIsableSW = false;
            return;
        }
        super.keyTyped(typedChar, keyCode);
        for (final KendallFrame frame : this.frames) {
            frame.keyTyped(typedChar, keyCode);
        }
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution sR = new ScaledResolution(this.mc);
        for (final KendallFrame frame : this.frames) {
            frame.render(mouseX, mouseY);
        }
        GL11.glDisable(3089);
        if (ShellSock.getClient().modManager.getModule(ClickGuiModule.class).KendallMyGod.isVIsableSW) {
            this.isVIsableSW = false;
            this.ShowSettingsWindow(mouseX, mouseY);
        }
    }
    
    public void onGuiClosed() {
        super.onGuiClosed();
        this.mc.entityRenderer.stopUseShader();
        ClickGuiFile.saveClickGui();
        KeybindFile.saveKeybinds();
        ModFile.saveModules();
        SettingsSliderFile.saveState();
        SettingsComboBoxFile.saveState();
        SettingsSliderFile.saveState();
    }
    
    public void ShowSettingsWindow(final int mouseX, final int mouseY) {
        if (this.targetbt == null) {
            this.isVIsableSW = false;
            return;
        }
        this.isVIsableSW = true;
        this.scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        final double width = 250.0;
        final double height = 34 + 29 * this.targetbt.components.size() - 2;
        if (this.x <= 0.0 && this.y <= 0.0) {
            this.x = this.scaledResolution.getScaledWidth() / 2 - width / 2.0;
            this.y = this.scaledResolution.getScaledHeight() / 2 - height / 2.0;
        }
        if (isHovered((float)this.x, (float)this.y, (float)(this.x + width), (float)(this.y + 25.0), mouseX, mouseY) && Mouse.isButtonDown(0) && this.onMoving) {
            if (this.moveX == 0.0f && this.moveY == 0.0f) {
                this.moveX = (float)(mouseX - this.x);
                this.moveY = (float)(mouseY - this.y);
            }
            else {
                this.x = (int)(mouseX - this.moveX);
                this.y = (int)(mouseY - this.moveY);
                final float n = (float)(this.y + 20.0);
            }
        }
        else if (this.moveX != 0.0f || this.moveY != 0.0f) {
            this.moveX = 0.0f;
            this.moveY = 0.0f;
        }
        RenderUtils.renderRoundedRect((float)this.x, (float)this.y, (float)width, (float)height, 5.0f, -14737374);
        FontManager.default18.drawString(this.targetbt.mod.getName(), (float)this.x + 10.0f, (float)this.y + 12.0f, -657929);
        final float ssl = (float)this.x;
        float hqc = (float)this.y + 24.0f;
        for (final Component sjh : this.targetbt.components) {
            if (sjh instanceof KendallMode) {
                final KendallMode fuckssl = (KendallMode)sjh;
                fuckssl.render(ssl, hqc, mouseX, mouseY);
            }
            if (sjh instanceof KendallSIlder) {
                final KendallSIlder fuckrcx = (KendallSIlder)sjh;
                fuckrcx.render(ssl, hqc, mouseX, mouseY);
            }
            if (sjh instanceof KendallOption) {
                final KendallOption fuckhqc = (KendallOption)sjh;
                fuckhqc.render(ssl, hqc, mouseX, mouseY);
            }
            hqc += 29.0f;
        }
    }
    
    public static boolean isHovered(final float x, final float y, final float x2, final float y2, final int mouseX, final int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }
}
