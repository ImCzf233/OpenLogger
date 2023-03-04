package com.netease.mc.modSS.ui.clickgui.shell;

import com.netease.mc.modSS.mod.*;
import com.netease.mc.modSS.font.*;
import java.awt.*;
import com.netease.mc.modSS.utils.*;
import net.minecraft.client.gui.*;
import com.netease.mc.modSS.*;
import com.netease.mc.modSS.managers.*;
import org.lwjgl.opengl.*;
import com.netease.mc.modSS.setting.*;
import com.netease.mc.modSS.ui.*;
import java.util.*;
import java.io.*;
import com.netease.mc.modSS.ui.clickgui.*;
import com.netease.mc.modSS.file.*;
import org.lwjgl.input.*;
import java.util.concurrent.*;

public class GuiClickUI extends GuiScreen
{
    private static List<Mod> inSetting;
    private static Category currentCategory;
    private static int x;
    private static int y;
    private static int wheel;
    private boolean need2move;
    private int dragX;
    private int dragY;
    private TranslateUtils translate;
    int valueSizeY;
    private int modeIndex;
    CFontRenderer font1;
    CFontRenderer font2;
    CFontRenderer font3;
    int press;
    
    public GuiClickUI() {
        this.translate = new TranslateUtils(0.0f, 0.0f);
        this.valueSizeY = 0;
        this.font1 = FontManager.default18;
        this.font2 = FontManager.default16;
        this.font3 = FontManager.default14;
        this.press = 0;
        this.need2move = false;
        this.dragX = 0;
        this.dragY = 0;
        this.translate.setX(0.0f);
        this.translate.setY(0.0f);
        this.modeIndex = 0;
    }
    
    public void initGui() {
        super.initGui();
        if (GuiClickUI.x > this.width) {
            GuiClickUI.x = 30;
        }
        if (GuiClickUI.y > this.height) {
            GuiClickUI.y = 30;
        }
        this.need2move = false;
    }
    
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.need2move) {
            GuiClickUI.x = mouseX - this.dragX;
            GuiClickUI.y = mouseY - this.dragY;
        }
        if (!Mouse.isButtonDown(0) && this.need2move) {
            this.need2move = false;
        }
        final String F = "ShellSock";
        RenderUtils.drawBorderedRect(GuiClickUI.x, GuiClickUI.y, GuiClickUI.x + 273, GuiClickUI.y + 198, 3.0f, new Color(20, 20, 20).getRGB(), getColor());
        RenderUtils.drawBorderedRect(GuiClickUI.x + 2, GuiClickUI.y + 2, GuiClickUI.x + 273 - 2, GuiClickUI.y + 198 - 2, 1.0f, getColor(), new Color(20, 20, 20).getRGB());
        Gui.drawRect(GuiClickUI.x + 70, GuiClickUI.y + 35, GuiClickUI.x + 269, GuiClickUI.y + 195, new Color(0, 0, 0).getRGB());
        FontManager.default30.drawStringWithShadow(F, GuiClickUI.x + 10, GuiClickUI.y + 8, new Color(180, 180, 180).getRGB());
        this.font2.drawStringWithShadow(ShellSock.getClient().VERSION, GuiClickUI.x + 12, GuiClickUI.y + 24, new Color(180, 180, 180).getRGB());
        RenderUtils.drawGradientSideways(GuiClickUI.x + 70, GuiClickUI.y + 35, GuiClickUI.x + 80, GuiClickUI.y + 195, new Color(20, 20, 20).getRGB(), new Color(0, 0, 0, 0).getRGB());
        int cateY = 0;
        for (final Category category : Category.values()) {
            final int strX = GuiClickUI.x + 40;
            final int strY = GuiClickUI.y + 55 + cateY;
            final boolean hover = mouseX > GuiClickUI.x + 5 && mouseX < GuiClickUI.x + 65 && mouseY > strY && mouseY < strY + 20;
            FontManager.default20.drawCenteredStringWithShadow(category.name().substring(0, 1).toUpperCase() + category.name().substring(1).toLowerCase(), strX - 1, strY + 6, (category == GuiClickUI.currentCategory) ? getColor() : new Color(hover ? 255 : 140, hover ? 255 : 140, hover ? 255 : 140).getRGB());
            cateY += 20;
        }
        final int startX = GuiClickUI.x + 80 + 2;
        final int startY = GuiClickUI.y + 9 + 2 + 28;
        final int length = 185;
        float moduleY = this.translate.getY();
        RenderUtils.startGlScissor(startX, startY + 14, length, 140);
        final ModManager modManager = ShellSock.getClient().modManager;
        for (final Mod m : ModManager.getModules()) {
            if (m.getCategory() != GuiClickUI.currentCategory) {
                continue;
            }
            RenderUtils.drawRoundRect(startX, startY + moduleY, startX + length, startY + moduleY + 24.0f, 3, new Color(20, 20, 20).getRGB());
            GL11.glEnable(3042);
            this.font1.drawStringWithShadow(m.getName(), startX + 8, startY + 9 + moduleY, -1);
            RenderUtils.drawRoundRect(startX + length - 25, startY + moduleY + 7.0f, startX + length - 5, startY + moduleY + 17.0f, 5, new Color(0, 0, 0).getRGB());
            final boolean onToggleButton = mouseX > startX + length - 25 && mouseX < startX + length - 5 && mouseY > startY + moduleY + 7.0f && mouseY < startY + moduleY + 17.0f;
            final int left = m.isEnabled() ? (startX + length - 14) : (startX + length - 24);
            final int right = m.isEnabled() ? (startX + length - 6) : (startX + length - 16);
            RenderUtils.drawRoundRect(left, startY + moduleY + 8.0f, right, startY + moduleY + 16.0f, 4, getColor());
            RenderUtils.drawRoundRect(startX + length - 24, startY + moduleY + 8.0f, startX + length - 16, startY + moduleY + 16.0f, 4, new Color(0, 0, 0, 150).getRGB());
            if (onToggleButton) {
                RenderUtils.drawRoundRect(startX + length - 25, startY + moduleY + 7.0f, startX + length - 5, startY + moduleY + 17.0f, 5, new Color(0, 0, 0, 100).getRGB());
            }
            final boolean showSetting = GuiClickUI.inSetting.contains(m);
            int valueSizeY;
            if (ShellSock.getClient().settingsManager.getSettingsByMod(m) != null) {
                valueSizeY = ShellSock.getClient().settingsManager.getSettingsByMod(m).size() * 20 + 5;
            }
            else {
                valueSizeY = 5;
            }
            float valueY = moduleY + 35.0f;
            if (showSetting) {
                RenderUtils.drawRect(startX + 3, startY + moduleY + 24.0f, startX + length - 3, startY + moduleY + 30.0f, new Color(30, 30, 30).getRGB());
                RenderUtils.drawRoundRect(startX + 3, startY + moduleY + 24.0f, startX + length - 3, startY + moduleY + 24.0f + valueSizeY, 3, new Color(30, 30, 30).getRGB());
                for (final Setting setting : ShellSock.getClient().settingsManager.getSettingsByMod(m)) {
                    if (setting.isModeMode()) {
                        this.font2.drawStringWithShadow(" ", startX + 10, startY + valueY - 1.0f, -1);
                        GL11.glEnable(3042);
                        this.font2.drawStringWithShadow(setting.getName(), startX + 10, startY + valueY - 1.0f, Colors.getColor(255, 255, 255));
                        RenderUtils.drawRoundRect(startX + length - 85, startY + valueY - 4.0f, startX + length - 6, startY + valueY + 8.0f, 3, new Color(10, 10, 10).getRGB());
                        final int longValue = (startX + length - 6 - (startX + length - 85)) / 2;
                        if (setting.getMode() != null) {
                            GL11.glEnable(3042);
                            this.font2.drawCenteredStringWithShadow(setting.getMode(), startX + length - 6 - longValue, startY + valueY - 0.5f, getColor());
                            this.font2.drawCenteredStringWithShadow(" ", startX + length - 6 - longValue, startY + valueY - 0.5f, getColor());
                        }
                        final boolean hover2 = mouseX > startX + length - 85 && mouseX < startX + length - 6 && mouseY > startY + valueY - 4.0f && mouseY < startY + valueY + 8.0f;
                        if (hover2) {
                            RenderUtils.drawRoundRect(startX + length - 85, startY + valueY - 4.0f, startX + length - 6, startY + valueY + 8.0f, 3, new Color(0, 0, 0, 100).getRGB());
                        }
                    }
                    if (setting.isModeSlider()) {
                        final Setting s = setting;
                        this.font2.drawStringWithShadow(" ", startX + 10, startY + valueY - 3.0f, -1);
                        GL11.glEnable(3042);
                        this.font2.drawStringWithShadow(s.getName(), startX + 10, startY + valueY - 3.0f, -1);
                        final double max = s.getMax();
                        final double min = s.getMin();
                        final double valn = s.getValue();
                        final int longValue2 = startX + length - 6 - (startX + length - 83);
                        this.font3.drawStringWithShadow(" ", startX + length - 84, startY + valueY - 2.0f, -1);
                        RenderUtils.drawRoundRect(startX + length - 85, startY + valueY + 5.0f, startX + length - 6, startY + valueY + 7.0f, 1, new Color(10, 10, 10).getRGB());
                        GL11.glEnable(3042);
                        this.font3.drawStringWithShadow(s.getValue() + "", startX + length - 84, startY + valueY - 2.0f, -1);
                        RenderUtils.drawRoundRect(startX + length - 85, startY + valueY + 5.0f, startX + length - 6, startY + valueY + 7.0f, 1, new Color(10, 10, 10).getRGB());
                        RenderUtils.drawRoundRect(startX + length - 85, startY + valueY + 5.0f, startX + length - 85 + longValue2 * (valn - min) / (max - min) + 2.0, startY + valueY + 7.0f, 1, getColor());
                        final boolean hover3 = mouseX > startX + length - 88 && mouseX < startX + length - 3 && mouseY > startY + valueY + 2.0f && mouseY < startY + valueY + 11.0f;
                        if (hover3) {
                            RenderUtils.drawRoundRect(startX + length - 85, startY + valueY + 5.0f, startX + length - 6, startY + valueY + 7.0f, 1, new Color(0, 0, 0, 100).getRGB());
                            if (Mouse.isButtonDown(0)) {
                                final double inc = 0.01;
                                final double valAbs = mouseX - (startX + length - 85);
                                double perc = valAbs / (longValue2 * Math.max(Math.min(valn / max, 0.0), 1.0));
                                perc = Math.min(Math.max(0.0, perc), 1.0);
                                final double valRel = (max - min) * perc;
                                double val = min + valRel;
                                val = Math.round(val * (1.0 / inc)) / (1.0 / inc);
                                s.setValue(val);
                                SettingsSliderFile.saveState();
                            }
                        }
                    }
                    if (setting.isModeButton()) {
                        final Setting s = setting;
                        this.font2.drawStringWithShadow(" ", startX + 10, startY + valueY - 3.0f, -1);
                        GL11.glEnable(3042);
                        this.font2.drawStringWithShadow(s.getName(), startX + 10, startY + valueY - 3.0f, -1);
                        final boolean hover2 = mouseX > startX + length - 18 && mouseX < startX + length - 6 && mouseY > startY + valueY - 4.0f && mouseY < startY + valueY + 8.0f;
                        RenderUtils.drawRoundRect(startX + length - 18, startY + valueY - 4.0f, startX + length - 6, startY + valueY + 8.0f, 2, new Color(10, 10, 10).getRGB());
                        if (s.isEnabled()) {
                            RenderUtils.drawRoundRect(startX + length - 17, startY + valueY - 3.0f, startX + length - 7, startY + valueY + 7.0f, 2, getColor());
                        }
                        if (hover2) {
                            RenderUtils.drawRoundRect(startX + length - 18, startY + valueY - 4.0f, startX + length - 6, startY + valueY + 8.0f, 2, new Color(0, 0, 0, 100).getRGB());
                        }
                    }
                    valueY += 20.0f;
                }
            }
            moduleY += (showSetting ? (26 + valueSizeY) : 26);
        }
        RenderUtils.stopGlScissor();
        RenderUtils.drawGradientSidewaysV(GuiClickUI.x + 3, GuiClickUI.y + 35, GuiClickUI.x + 273 - 3, GuiClickUI.y + 45, new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0).getRGB());
        final int real = Mouse.getDWheel();
        final float moduleHeight = moduleY - this.translate.getY();
        if (Mouse.hasWheel() && mouseX > startX && mouseY > startY && mouseX < startX + 270 && mouseY < startY + 237) {
            if (real > 0 && GuiClickUI.wheel < 0) {
                for (int i = 0; i < 5 && GuiClickUI.wheel < 0; ++i) {
                    GuiClickUI.wheel += 5;
                }
            }
            else {
                for (int i = 0; i < 5 && real < 0 && moduleHeight > 158.0f; ++i) {
                    if (Math.abs(GuiClickUI.wheel) >= moduleHeight - 154.0f) {
                        break;
                    }
                    GuiClickUI.wheel -= 5;
                }
            }
        }
        this.translate.interpolate(0.0f, GuiClickUI.wheel, 0.15f);
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        final boolean hover2top = mouseX > GuiClickUI.x && mouseX < GuiClickUI.x + 273 && mouseY > GuiClickUI.y && mouseY < GuiClickUI.y + 35;
        if (hover2top && mouseButton == 0) {
            this.dragX = mouseX - GuiClickUI.x;
            this.dragY = mouseY - GuiClickUI.y;
            this.need2move = true;
        }
        else {
            int cateY = 0;
            for (final Category category : Category.values()) {
                final int strX = GuiClickUI.x + 40;
                final int strY = GuiClickUI.y + 55 + cateY;
                final boolean hover = mouseX > GuiClickUI.x + 5 && mouseX < GuiClickUI.x + 65 && mouseY > strY && mouseY < strY + 20;
                if (hover && mouseButton == 0) {
                    GuiClickUI.currentCategory = category;
                    GuiClickUI.wheel = 0;
                    this.translate.setY(0.0f);
                    break;
                }
                cateY += 20;
            }
            final int startX = GuiClickUI.x + 80 + 2;
            final int startY = GuiClickUI.y + 9 + 2 + 25;
            final int length = 185;
            float moduleY = this.translate.getY();
            final ModManager modManager = ShellSock.getClient().modManager;
            for (final Mod m : ModManager.getModules()) {
                if (m.getCategory() != GuiClickUI.currentCategory) {
                    continue;
                }
                final boolean onToggleButton = mouseX > startX + length - 25 && mouseX < startX + length - 5 && mouseY > startY + moduleY + 7.0f && mouseY < startY + moduleY + 20.0f && mouseY < startY + 14 + 140 && mouseY > startY;
                final boolean onModuleRect = mouseX > startX && mouseX < startX + length && mouseY > startY + moduleY && mouseY < startY + moduleY + 28.0f && mouseY < startY + 14 + 140 && mouseY > startY;
                if (onToggleButton && mouseButton == 0) {
                    m.toggle();
                }
                if (onModuleRect && mouseButton == 1) {
                    if (GuiClickUI.inSetting.contains(m)) {
                        GuiClickUI.inSetting.remove(m);
                    }
                    else if (ShellSock.getClient().settingsManager.getSettingsByMod(m) != null && !ShellSock.getClient().settingsManager.getSettingsByMod(m).isEmpty()) {
                        GuiClickUI.inSetting.add(m);
                    }
                }
                final boolean showSetting = GuiClickUI.inSetting.contains(m);
                if (ShellSock.getClient().settingsManager.getSettingsByMod(m) != null) {
                    this.valueSizeY = ShellSock.getClient().settingsManager.getSettingsByMod(m).size() * 20 + 5;
                }
                else {
                    this.valueSizeY = 5;
                }
                float valueY = moduleY + 35.0f;
                if (showSetting) {
                    if (ShellSock.getClient().settingsManager.getSettingsByMod(m) != null) {
                        RenderUtils.drawRect(startX + 3, startY + moduleY + 24.0f, startX + length - 3, startY + moduleY + 24.0f + this.valueSizeY, new Color(30, 30, 30).getRGB());
                    }
                    for (final Setting setting : ShellSock.getClient().settingsManager.getSettingsByMod(m)) {
                        if (setting.isModeMode()) {
                            final Setting s = setting;
                            final boolean hover2 = mouseX > startX + length - 85 && mouseX < startX + length - 6 && mouseY > startY + valueY - 4.0f && mouseY < startY + valueY + 11.0f && mouseY < startY + 14 + 140 && mouseY > startY;
                            if (hover2) {
                                if (mouseButton == 1) {
                                    final Setting set = s;
                                    final int maxIndex = set.getOptions().size();
                                    ++this.modeIndex;
                                    if (this.modeIndex + 1 > maxIndex) {
                                        this.modeIndex = 0;
                                    }
                                    set.setMode(set.getOptions().get(this.modeIndex));
                                    SettingsComboBoxFile.saveState();
                                }
                                if (mouseButton == 0) {
                                    final Setting set = s;
                                    final int maxIndex = set.getOptions().size();
                                    ++this.modeIndex;
                                    if (this.modeIndex + 1 > maxIndex) {
                                        this.modeIndex = 0;
                                    }
                                    set.setMode(set.getOptions().get(this.modeIndex));
                                    SettingsComboBoxFile.saveState();
                                }
                            }
                        }
                        if (setting.isModeButton()) {
                            final Setting s = setting;
                            final boolean hover2 = mouseX > startX + length - 18 && mouseX < startX + length - 6 && mouseY > startY + valueY - 4.0f && mouseY < startY + valueY + 11.0f && mouseY < startY + 14 + 140 && mouseY > startY;
                            if (hover2 && (mouseButton == 0 || mouseButton == 2)) {
                                s.setState(!s.isEnabled());
                                SettingsButtonFile.saveState();
                            }
                        }
                        valueY += 20.0f;
                    }
                }
                moduleY += (showSetting ? (26 + this.valueSizeY) : 26);
            }
        }
    }
    
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
        final int startY = GuiClickUI.y + 9 + 2 + 28;
        final boolean hover2top = mouseX > GuiClickUI.x + 1 && mouseX < GuiClickUI.x + 349 && mouseY > GuiClickUI.y + 1 && mouseY < GuiClickUI.y + 9 && mouseY < startY + 14 + 140 && mouseY > startY;
        if (hover2top && state == 0) {
            this.dragX = mouseX - GuiClickUI.x;
            this.dragY = mouseY - GuiClickUI.y;
            this.need2move = false;
        }
    }
    
    public void onGuiClosed() {
        super.onGuiClosed();
        ClickGuiModule.memoriseX = GuiClickUI.x;
        ClickGuiModule.memoriseY = GuiClickUI.y;
        ClickGuiModule.memoriseWheel = GuiClickUI.wheel;
        ClickGuiModule.memoriseML = GuiClickUI.inSetting;
        ClickGuiModule.memoriseCatecory = GuiClickUI.currentCategory;
        ClickGuiFile.saveClickGui();
        try {
            Mouse.setNativeCursor((Cursor)null);
        }
        catch (Throwable t) {}
    }
    
    public static void setInSetting(final List<Mod> moduleList) {
        GuiClickUI.inSetting = moduleList;
    }
    
    public static void setWheel(final int state) {
        GuiClickUI.wheel = state;
    }
    
    public static void setX(final int state) {
        GuiClickUI.x = state;
    }
    
    public static void setY(final int state) {
        GuiClickUI.y = state;
    }
    
    public static void setCategory(final Category state) {
        GuiClickUI.currentCategory = state;
    }
    
    public static int getColor() {
        return new Color((int)ShellSock.getClient().settingsManager.getSettingByName(ShellSock.getClient().modManager.getModulebyName("ClickGUI"), "Red").getValue(), (int)ShellSock.getClient().settingsManager.getSettingByName(ShellSock.getClient().modManager.getModulebyName("ClickGUI"), "Green").getValue(), (int)ShellSock.getClient().settingsManager.getSettingByName(ShellSock.getClient().modManager.getModulebyName("ClickGUI"), "Blue").getValue()).getRGB();
    }
    
    static {
        GuiClickUI.inSetting = new CopyOnWriteArrayList<Mod>();
    }
}
