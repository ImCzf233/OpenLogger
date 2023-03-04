package com.netease.mc.modSS.ui.clickgui.shell;

import net.minecraft.client.*;
import com.netease.mc.modSS.utils.misc.*;
import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.managers.*;
import org.lwjgl.opengl.*;
import com.netease.mc.modSS.ui.*;
import com.netease.mc.modSS.utils.render.*;
import com.netease.mc.modSS.*;
import net.minecraft.util.*;
import net.minecraft.client.gui.*;
import java.awt.*;
import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.utils.*;
import com.netease.mc.modSS.font.*;
import com.netease.mc.modSS.ui.clickgui.*;
import com.netease.mc.modSS.file.*;
import org.lwjgl.input.*;

public class GuiClickShell extends GuiScreen
{
    public static Minecraft mc;
    public static ScaledResolution sr;
    public AnimationUtils animationUtils;
    public static Category currentModuleType;
    public static Mod currentModule;
    public static float startX;
    public static float startY;
    public static int moduleStart;
    public static int valueStart;
    boolean previousmouse;
    boolean mouse;
    public float moveX;
    public float moveY;
    boolean bind;
    float hue;
    public static int alpha;
    public static int alphe;
    int time;
    int main_width;
    int main_height;
    int press;
    private int modeIndex;
    private boolean animate;
    private float target;
    private float h;
    
    public GuiClickShell() {
        this.animationUtils = new AnimationUtils();
        this.previousmouse = true;
        this.moveX = 0.0f;
        this.moveY = 0.0f;
        this.bind = false;
        this.time = 0;
        this.main_width = 450;
        this.main_height = 296;
        this.press = 0;
        this.animate = false;
        this.target = GuiClickShell.startY + 60.0f;
        this.h = GuiClickShell.startY + 60.0f;
        this.modeIndex = 0;
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final CFontRenderer font = FontManager.default16;
        GuiClickShell.sr = new ScaledResolution(GuiClickShell.mc);
        GL11.glPushMatrix();
        HGLUtils.drawRoundedRect2(GuiClickShell.startX, GuiClickShell.startY, GuiClickShell.startX + this.main_width, GuiClickShell.startY + this.main_height, 4.0f, Colors.getColor(29, 29, 31));
        RenderUtils.drawRect(GuiClickShell.startX + 101.0f, GuiClickShell.startY + 51.0f, GuiClickShell.startX + this.main_width, GuiClickShell.startY + this.main_height, Colors.getColor(38, 38, 41));
        if (ShellSock.mixin) {
            GuiClickShell.mc.getTextureManager().bindTexture(new ResourceLocation("shellsock", "ui/logo.png"));
            GL11.glEnable(3042);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            Gui.drawModalRectWithCustomSizedTexture((int)GuiClickShell.startX + 14, (int)GuiClickShell.startY + 12, 0.0f, 0.0f, 23, 30, 23.0f, 30.0f);
            FontManager.icon25.drawString("ShellSock", GuiClickShell.startX + 40.0f, GuiClickShell.startY + 20.0f, Color.white.getRGB());
            GL11.glDisable(3042);
        }
        else {
            FontManager.icon25.drawString("ShellSock", GuiClickShell.startX + 40.0f, GuiClickShell.startY + 20.0f, Color.white.getRGB());
        }
        GL11.glPopMatrix();
        RenderUtils.drawRect(GuiClickShell.startX, GuiClickShell.startY + 50.0f, GuiClickShell.startX + this.main_width, GuiClickShell.startY + 51.0f, Colors.getColor(78, 78, 78));
        if (this.isCategoryHovered(GuiClickShell.startX + 110.0f, GuiClickShell.startY + 10.0f, GuiClickShell.startX + 160.0f, GuiClickShell.startY + 40.0f, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            GuiClickShell.currentModuleType = Category.COMBAT;
            GuiClickShell.currentModule = ShellSock.getClient().modManager.getModulesInCategoryNoSort(GuiClickShell.currentModuleType).get(0);
            GuiClickShell.moduleStart = 0;
            GuiClickShell.valueStart = 0;
            for (int x1 = 0; x1 < ShellSock.getClient().settingsManager.getSettingsByMod(GuiClickShell.currentModule).size(); ++x1) {
                final Setting setting = ShellSock.getClient().settingsManager.getSettingsByMod(GuiClickShell.currentModule).get(x1);
            }
        }
        if (GuiClickShell.currentModuleType == Category.COMBAT) {
            FontManager.default20.drawString("COMBAT", GuiClickShell.startX + 113.0f, GuiClickShell.startY + 20.0f, Colors.getColor(245, 245, 247));
        }
        else {
            FontManager.default20.drawString("COMBAT", GuiClickShell.startX + 113.0f, GuiClickShell.startY + 20.0f, Colors.getColor(165, 165, 165));
        }
        if (this.isCategoryHovered(GuiClickShell.startX + 170.0f, GuiClickShell.startY + 10.0f, GuiClickShell.startX + 220.0f, GuiClickShell.startY + 40.0f, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            GuiClickShell.currentModuleType = Category.MOVEMENT;
            GuiClickShell.currentModule = ShellSock.getClient().modManager.getModulesInCategoryNoSort(GuiClickShell.currentModuleType).get(0);
            GuiClickShell.moduleStart = 0;
            GuiClickShell.valueStart = 0;
            for (int x1 = 0; x1 < ShellSock.getClient().settingsManager.getSettingsByMod(GuiClickShell.currentModule).size(); ++x1) {
                final Setting setting2 = ShellSock.getClient().settingsManager.getSettingsByMod(GuiClickShell.currentModule).get(x1);
            }
        }
        if (GuiClickShell.currentModuleType == Category.MOVEMENT) {
            FontManager.default20.drawString("MOVEMENT", GuiClickShell.startX + 166.0f, GuiClickShell.startY + 20.0f, Colors.getColor(245, 245, 247));
        }
        else {
            FontManager.default20.drawString("MOVEMENT", GuiClickShell.startX + 166.0f, GuiClickShell.startY + 20.0f, Colors.getColor(165, 165, 165));
        }
        if (this.isCategoryHovered(GuiClickShell.startX + 230.0f, GuiClickShell.startY + 10.0f, GuiClickShell.startX + 280.0f, GuiClickShell.startY + 40.0f, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            GuiClickShell.currentModuleType = Category.PLAYER;
            GuiClickShell.currentModule = ShellSock.getClient().modManager.getModulesInCategoryNoSort(GuiClickShell.currentModuleType).get(0);
            GuiClickShell.moduleStart = 0;
            GuiClickShell.valueStart = 0;
            if (ShellSock.getClient().settingsManager.getSettingsByMod(GuiClickShell.currentModule) != null) {
                for (int x1 = 0; x1 < ShellSock.getClient().settingsManager.getSettingsByMod(GuiClickShell.currentModule).size(); ++x1) {
                    final Setting setting3 = ShellSock.getClient().settingsManager.getSettingsByMod(GuiClickShell.currentModule).get(x1);
                }
            }
        }
        if (GuiClickShell.currentModuleType == Category.PLAYER) {
            FontManager.default20.drawString("PLAYER", GuiClickShell.startX + 235.0f, GuiClickShell.startY + 20.0f, Colors.getColor(245, 245, 247));
        }
        else {
            FontManager.default20.drawString("PLAYER", GuiClickShell.startX + 235.0f, GuiClickShell.startY + 20.0f, Colors.getColor(165, 165, 165));
        }
        if (this.isCategoryHovered(GuiClickShell.startX + 290.0f, GuiClickShell.startY + 10.0f, GuiClickShell.startX + 340.0f, GuiClickShell.startY + 40.0f, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            GuiClickShell.currentModuleType = Category.CLIENT;
            GuiClickShell.currentModule = ShellSock.getClient().modManager.getModulesInCategoryNoSort(GuiClickShell.currentModuleType).get(0);
            GuiClickShell.moduleStart = 0;
            GuiClickShell.valueStart = 0;
            if (ShellSock.getClient().settingsManager.getSettingsByMod(GuiClickShell.currentModule) != null) {
                for (int x1 = 0; x1 < ShellSock.getClient().settingsManager.getSettingsByMod(GuiClickShell.currentModule).size(); ++x1) {
                    final Setting setting4 = ShellSock.getClient().settingsManager.getSettingsByMod(GuiClickShell.currentModule).get(x1);
                }
            }
        }
        if (GuiClickShell.currentModuleType == Category.CLIENT) {
            FontManager.default20.drawString("CLIENT", GuiClickShell.startX + 292.0f, GuiClickShell.startY + 20.0f, Colors.getColor(245, 245, 247));
        }
        else {
            FontManager.default20.drawString("CLIENT", GuiClickShell.startX + 292.0f, GuiClickShell.startY + 20.0f, Colors.getColor(165, 165, 165));
        }
        if (this.isCategoryHovered(GuiClickShell.startX + 350.0f, GuiClickShell.startY + 10.0f, GuiClickShell.startX + 400.0f, GuiClickShell.startY + 40.0f, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            GuiClickShell.currentModuleType = Category.VISUAL;
            GuiClickShell.currentModule = ShellSock.getClient().modManager.getModulesInCategoryNoSort(GuiClickShell.currentModuleType).get(0);
            GuiClickShell.moduleStart = 0;
            GuiClickShell.valueStart = 0;
            if (ShellSock.getClient().settingsManager.getSettingsByMod(GuiClickShell.currentModule) != null) {
                for (int x1 = 0; x1 < ShellSock.getClient().settingsManager.getSettingsByMod(GuiClickShell.currentModule).size(); ++x1) {
                    final Setting setting5 = ShellSock.getClient().settingsManager.getSettingsByMod(GuiClickShell.currentModule).get(x1);
                }
            }
        }
        if (GuiClickShell.currentModuleType == Category.VISUAL) {
            FontManager.default20.drawString("VISUAL", GuiClickShell.startX + 352.0f, GuiClickShell.startY + 20.0f, Colors.getColor(245, 245, 247));
        }
        else {
            FontManager.default20.drawString("VISUAL", GuiClickShell.startX + 352.0f, GuiClickShell.startY + 20.0f, Colors.getColor(165, 165, 165));
        }
        final int m = Mouse.getDWheel();
        RenderUtils.drawRect(GuiClickShell.startX + 100.0f, GuiClickShell.startY + 51.0f, GuiClickShell.startX + 101.0f, GuiClickShell.startY + this.main_height, Colors.getColor(78, 78, 78));
        if (this.isCategoryHovered(GuiClickShell.startX, GuiClickShell.startY + 51.0f, GuiClickShell.startX + 100.0f, GuiClickShell.startY + this.main_height, mouseX, mouseY) && ShellSock.getClient().modManager.getModulesInCategoryNoSort(GuiClickShell.currentModuleType) != null) {
            if (m < 0 && GuiClickShell.moduleStart < ShellSock.getClient().modManager.getModulesInCategoryNoSort(GuiClickShell.currentModuleType).size() - 1) {
                ++GuiClickShell.moduleStart;
            }
            if (m > 0 && GuiClickShell.moduleStart > 0) {
                --GuiClickShell.moduleStart;
            }
        }
        if (this.isCategoryHovered(GuiClickShell.startX + 102.0f, GuiClickShell.startY + 51.0f, GuiClickShell.startX + this.main_width, GuiClickShell.startY + this.main_height, mouseX, mouseY) && ShellSock.getClient().settingsManager.getSettingsByMod(GuiClickShell.currentModule) != null) {
            if (m < 0 && GuiClickShell.valueStart < ShellSock.getClient().settingsManager.getSettingsByMod(GuiClickShell.currentModule).size() - 1) {
                ++GuiClickShell.valueStart;
            }
            if (m > 0 && GuiClickShell.valueStart > 0) {
                --GuiClickShell.valueStart;
            }
        }
        float mY = GuiClickShell.startY + 6.0f;
        for (int i = 0; i < ShellSock.getClient().modManager.getModulesInCategoryNoSort(GuiClickShell.currentModuleType).size(); ++i) {
            final Mod module = ShellSock.getClient().modManager.getModulesInCategoryNoSort(GuiClickShell.currentModuleType).get(i);
            if (mY > GuiClickShell.startY + this.main_height - 70.0f) {
                break;
            }
            if (i >= GuiClickShell.moduleStart) {
                if (!module.isEnabled()) {
                    RenderUtils.drawRoundRect(GuiClickShell.startX + 400.0f, GuiClickShell.startY + 260.0f, GuiClickShell.startX + 440.0f, GuiClickShell.startY + 280.0f, 4, Colors.getColor(100, 100, 100));
                    GL11.glEnable(3042);
                    FontManager.default18.drawCenteredString(this.bind ? "Press" : Keyboard.getKeyName(module.getKeybind()), GuiClickShell.startX + 420.0f, GuiClickShell.startY + 270.0f, Colors.getColor(255, 255, 255));
                    GL11.glDisable(3042);
                    final boolean animate = false;
                    this.animate = animate;
                    if (animate) {
                        RenderUtils.drawRoundRect(GuiClickShell.startX + 12.0f, mY + 55.0f - 4.0f, GuiClickShell.startX + 14.0f, mY + 55.0f + 10.0f, 1, Colors.getColor(245, 245, 245));
                    }
                    GL11.glEnable(3042);
                    font.drawString(module.getName(), (int)GuiClickShell.startX + 20, (int)mY + 55, new Color(165, 165, 165).getRGB());
                    GL11.glDisable(3042);
                }
                else {
                    RenderUtils.drawRoundRect(GuiClickShell.startX + 400.0f, GuiClickShell.startY + 260.0f, GuiClickShell.startX + 440.0f, GuiClickShell.startY + 280.0f, 4, Colors.getColor(100, 100, 100));
                    GL11.glEnable(3042);
                    FontManager.default18.drawCenteredString(this.bind ? "Press" : Keyboard.getKeyName(module.getKeybind()), GuiClickShell.startX + 420.0f, GuiClickShell.startY + 270.0f, Colors.getColor(255, 255, 255));
                    GL11.glDisable(3042);
                    final boolean animate2 = false;
                    this.animate = animate2;
                    if (animate2) {
                        RenderUtils.drawRoundRect(GuiClickShell.startX + 12.0f, mY + 55.0f - 4.0f, GuiClickShell.startX + 14.0f, mY + 55.0f + 10.0f, 1, Colors.getColor(245, 245, 245));
                    }
                    GL11.glEnable(3042);
                    font.drawString(module.getName(), (int)GuiClickShell.startX + 20, (int)mY + 55, new Color(245, 245, 247).getRGB());
                    GL11.glDisable(3042);
                }
                if (this.isSettingsButtonHovered(GuiClickShell.startX, mY + 45.0f, GuiClickShell.startX + 100.0f, mY + 65.0f, mouseX, mouseY)) {
                    if (!this.previousmouse && Mouse.isButtonDown(0)) {
                        module.toggle();
                        ModFile.saveModules();
                        this.previousmouse = true;
                    }
                    if (!this.previousmouse && Mouse.isButtonDown(1)) {
                        this.previousmouse = true;
                    }
                }
                if (!Mouse.isButtonDown(0)) {
                    this.previousmouse = false;
                }
                if (this.isHovered(GuiClickShell.startX + 400.0f, GuiClickShell.startY + 260.0f, GuiClickShell.startX + 440.0f, GuiClickShell.startY + 280.0f, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                    this.bind = true;
                }
                if (this.isSettingsButtonHovered(GuiClickShell.startX, mY + 45.0f, GuiClickShell.startX + 100.0f, mY + 70.0f, mouseX, mouseY) && Mouse.isButtonDown(1)) {
                    this.animate = true;
                    this.target = mY + 54.0f;
                    GuiClickShell.currentModule = module;
                    GuiClickShell.valueStart = 0;
                }
                mY += 20.0f;
            }
        }
        mY = GuiClickShell.startY + 12.0f;
        final boolean animate3 = true;
        this.animate = animate3;
        if (animate3) {
            this.h = this.animationUtils.animate(this.target, this.h, 0.1f);
            RenderUtils.drawRoundRect(GuiClickShell.startX + 12.0f, this.h - 4.0f, GuiClickShell.startX + 14.0f, this.h + 10.0f, 1, Colors.getColor(245, 245, 245));
            if (this.h == this.target) {
                this.animate = false;
            }
        }
        if (GuiClickShell.currentModule != null && GuiClickShell.currentModule.getDescription() != null) {
            Wrapper.INSTANCE.mc().fontRendererObj.drawString(GuiClickShell.currentModule.getDescription(), (int)GuiClickShell.startX + 110, (int)mY + 275, new Color(245, 245, 245).getRGB());
        }
        if (ShellSock.getClient().settingsManager.getSettingsByMod(GuiClickShell.currentModule) != null) {
            for (int i = 0; i < ShellSock.getClient().settingsManager.getSettingsByMod(GuiClickShell.currentModule).size() && mY <= GuiClickShell.startY + 220.0f; ++i) {
                if (i >= GuiClickShell.valueStart) {
                    final Setting value = ShellSock.getClient().settingsManager.getSettingsByMod(GuiClickShell.currentModule).get(i);
                    if (value.isModeSlider()) {
                        final float x2 = GuiClickShell.startX + 150.0f;
                        double render = 200.0 * (value.getValue() - value.getMin()) / (value.getMax() - value.getMin());
                        RenderUtils.drawRoundRect(x2 + 2.0f, mY + 65.0f, x2 + 202.0f, mY + 70.0f, 3, this.isButtonHovered(x2 + 2.0f, mY + 65.0f, x2 + 202.0f, mY + 70.0f, mouseX, mouseY) ? Colors.getColor(120, 120, 120) : Colors.getColor(81, 81, 84));
                        RenderUtils.drawRoundRect(x2 + 2.0f, mY + 65.0f, x2 + render + 6.5, mY + 70.0f, 3, Colors.getColor(245, 245, 245));
                        RenderUtils.drawRoundRect(x2 + render + 2.0 + 1.0, mY + 61.0f, x2 + render + 2.0 + 5.0, mY + 65.0f + 9.0f, 2, Colors.getColor(245, 245, 245));
                        GL11.glPushMatrix();
                        GL11.glEnable(3042);
                        font.drawString(value.getName(), (int)GuiClickShell.startX + 130, (int)mY + 50, new Color(245, 245, 245).getRGB());
                        font.drawString("" + value.getValue(), (int)GuiClickShell.startX + 140 - font.getStringWidth("" + value.getValue()), (int)mY + 65, new Color(245, 245, 245).getRGB());
                        GL11.glDisable(3042);
                        GL11.glPopMatrix();
                        if (!Mouse.isButtonDown(0)) {
                            this.previousmouse = false;
                        }
                        if (this.isButtonHovered(x2 + 2.0f, mY + 65.0f, x2 + 202.0f, mY + 70.0f, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                            if (!this.previousmouse && Mouse.isButtonDown(0)) {
                                render = value.getMin();
                                final double max = value.getMax();
                                final double inc = 0.01;
                                final double valAbs = mouseX - (x2 + 1.0);
                                double perc = valAbs / 200.0;
                                perc = Math.min(Math.max(0.0, perc), 1.0);
                                final double valRel = (max - render) * perc;
                                double val = render + valRel;
                                val = Math.round(val * (1.0 / inc)) / (1.0 / inc);
                                value.setValue(val);
                            }
                            if (!Mouse.isButtonDown(0)) {
                                this.previousmouse = false;
                            }
                        }
                        mY += 30.0f;
                    }
                    if (value.isModeButton()) {
                        final float x2 = GuiClickShell.startX + 180.0f;
                        final int xx = 30;
                        final int x2x = 45;
                        GL11.glEnable(3042);
                        font.drawString(value.getName(), (int)GuiClickShell.startX + 150, (int)mY + 50, new Color(245, 245, 245).getRGB());
                        GL11.glDisable(3042);
                        if (value.isEnabled()) {
                            RenderUtils.drawRoundRect(GuiClickShell.startX + 130.0f, mY + 46.0f, GuiClickShell.startX + 130.0f + 14.0f, mY + 50.0f + 10.0f, 2, Colors.getColor(0, 0, 0));
                            RenderUtils.drawRoundRect(GuiClickShell.startX + 130.0f + 1.0f, mY + 46.0f + 1.0f, GuiClickShell.startX + 130.0f + 14.0f - 1.0f, mY + 50.0f + 10.0f - 1.0f, 2, Colors.getColor(200, 200, 200));
                        }
                        else {
                            RenderUtils.drawRoundRect(GuiClickShell.startX + 130.0f, mY + 46.0f, GuiClickShell.startX + 130.0f + 14.0f, mY + 50.0f + 10.0f, 2, Colors.getColor(0, 0, 0));
                        }
                        if (this.isCheckBoxHovered(GuiClickShell.startX + 130.0f, mY + 46.0f, GuiClickShell.startX + 130.0f + 14.0f, mY + 50.0f + 10.0f, mouseX, mouseY)) {
                            RenderUtils.drawRect(GuiClickShell.startX + 130.0f + 1.0f, mY + 46.0f + 1.0f, GuiClickShell.startX + 130.0f + 14.0f - 1.0f, mY + 50.0f + 10.0f - 1.0f, Colors.getColor(60, 60, 60));
                            if (!this.previousmouse && Mouse.isButtonDown(0)) {
                                this.previousmouse = true;
                                this.mouse = true;
                            }
                            if (this.mouse) {
                                value.setState(!value.isEnabled());
                                this.mouse = false;
                            }
                        }
                        if (!Mouse.isButtonDown(0)) {
                            this.previousmouse = false;
                        }
                        mY += 20.0f;
                    }
                    if (value.isModeMode()) {
                        final float x2 = GuiClickShell.startX + 130.0f;
                        GL11.glEnable(3042);
                        font.drawStringWithShadow(value.getName(), GuiClickShell.startX + 130.0f, mY + 52.0f, new Color(245, 245, 245).getRGB());
                        GL11.glDisable(3042);
                        RenderUtils.drawRoundRect(x2, mY + 62.0f, x2 + 100.0f, mY + 76.0f, 2, Colors.getColor(80, 80, 80));
                        if (value.getMode() != null) {
                            GL11.glEnable(3042);
                            font.drawStringWithShadow(value.getMode(), x2 + 18.0f - font.getStringWidth(value.getMode()) / 2, mY + 66.0f, new Color(220, 220, 220).getRGB());
                            GL11.glDisable(3042);
                        }
                        if (this.isStringHovered(x2, mY + 62.0f, x2 + 100.0f, mY + 76.0f, mouseX, mouseY) && Mouse.isButtonDown(0) && !this.previousmouse) {
                            final Setting set = value;
                            final int maxIndex = set.getOptions().size();
                            ++this.modeIndex;
                            if (this.modeIndex + 1 > maxIndex) {
                                this.modeIndex = 0;
                            }
                            set.setMode(set.getOptions().get(this.modeIndex));
                            this.previousmouse = true;
                        }
                        mY += 30.0f;
                    }
                }
            }
            if ((this.isHovered(GuiClickShell.startX, GuiClickShell.startY, GuiClickShell.startX + 100.0f, GuiClickShell.startY + 50.0f, mouseX, mouseY) || this.isHovered(GuiClickShell.startX, GuiClickShell.startY + 315.0f, GuiClickShell.startX + 450.0f, GuiClickShell.startY + 350.0f, mouseX, mouseY) || this.isHovered(GuiClickShell.startX + 430.0f, GuiClickShell.startY, GuiClickShell.startX + 450.0f, GuiClickShell.startY + 350.0f, mouseX, mouseY)) && Mouse.isButtonDown(0)) {
                if (this.moveX == 0.0f && this.moveY == 0.0f) {
                    this.moveX = mouseX - GuiClickShell.startX;
                    this.moveY = mouseY - GuiClickShell.startY;
                }
                else {
                    GuiClickShell.startX = mouseX - this.moveX;
                    GuiClickShell.startY = mouseY - this.moveY;
                }
                this.previousmouse = true;
            }
            else if (this.moveX != 0.0f || this.moveY != 0.0f) {
                this.moveX = 0.0f;
                this.moveY = 0.0f;
            }
            final int j = 59;
            final int l = 40;
            final float k = GuiClickShell.startY + 10.0f;
            final float n = GuiClickShell.startX + 5.0f;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public void onGuiClosed() {
        super.onGuiClosed();
        ClickGuiModule.shell_x = GuiClickShell.startX;
        ClickGuiModule.shell_y = GuiClickShell.startY;
        ClickGuiModule.shell_category = GuiClickShell.currentModuleType;
        ClickGuiModule.shell_module = GuiClickShell.currentModule;
        ClickGuiFile.saveClickGui();
        SettingsButtonFile.saveState();
        SettingsComboBoxFile.saveState();
        SettingsSliderFile.saveState();
        KeybindFile.saveKeybinds();
        try {
            Mouse.setNativeCursor((Cursor)null);
        }
        catch (Throwable t) {}
    }
    
    public boolean isStringHovered(final float f, final float y, final float g, final float y2, final int mouseX, final int mouseY) {
        return mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2;
    }
    
    public boolean isSettingsButtonHovered(final float x, final float y, final float x2, final float y2, final int mouseX, final int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }
    
    public boolean isButtonHovered(final float f, final float y, final float g, final float y2, final int mouseX, final int mouseY) {
        return mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2;
    }
    
    public boolean isCheckBoxHovered(final float f, final float y, final float g, final float y2, final int mouseX, final int mouseY) {
        return mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2;
    }
    
    public boolean isCategoryHovered(final float x, final float y, final float x2, final float y2, final int mouseX, final int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }
    
    public boolean isHovered(final float x, final float y, final float x2, final float y2, final int mouseX, final int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }
    
    public void keyTyped(final char typedChar, final int keyCode) {
        if (this.bind) {
            GuiClickShell.currentModule.setKeyBind(keyCode);
            if (keyCode == 1) {
                GuiClickShell.currentModule.setKeyBind(0);
            }
            this.bind = false;
        }
        else if (keyCode == 1) {
            GuiClickShell.mc.displayGuiScreen((GuiScreen)null);
            if (GuiClickShell.mc.currentScreen == null) {
                GuiClickShell.mc.setIngameFocus();
            }
        }
    }
    
    public static void setMod(final Mod mod) {
        GuiClickShell.currentModule = mod;
    }
    
    public static void setX(final float shell_x) {
        GuiClickShell.startX = shell_x;
    }
    
    public static void setY(final float shell_y) {
        GuiClickShell.startY = shell_y;
    }
    
    public static void setCategory(final Category state) {
        GuiClickShell.currentModuleType = state;
    }
    
    static {
        GuiClickShell.mc = Minecraft.getMinecraft();
        GuiClickShell.sr = new ScaledResolution(GuiClickShell.mc);
        GuiClickShell.startX = 100.0f;
        GuiClickShell.startY = 40.0f;
        GuiClickShell.moduleStart = 0;
        GuiClickShell.valueStart = 0;
        GuiClickShell.alphe = 121;
    }
}
